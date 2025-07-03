<%-- 1. DIRECTIVAS DE PÁGINA: Imports y configuración --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, java.util.ArrayList, java.util.Map, java.util.HashMap, java.sql.*" %>

<%-- prettier-ignore --%>
<%!
    // Método para conectar a la base de datos y obtener oficiales
    private List<Map<String, Object>> obtenerTodosLosOficiales(String busqueda) throws Exception {
        List<Map<String, Object>> listaOficiales = new ArrayList<>();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT nombre1, nombre2, apellido1, apellido2, cedula, numeroTelefono, nombreUsuario, contraseña FROM usuarios WHERE tipoUsuario = 'Guarda'";
        if (busqueda != null && !busqueda.trim().isEmpty()) {
            sql += " AND (nombre1 LIKE ? OR apellido1 LIKE ? OR cedula LIKE ? OR nombreUsuario LIKE ?)";
        }

        try {
            String url = "jdbc:mysql://localhost:3306/proyecto1?verifyServerCertificate=false&useSSL=true&useUnicode=true&characterEncoding=UTF-8";
            
            String user = "root";
            String password = "1234";
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, user, password);

            pstmt = con.prepareStatement(sql);
            if (busqueda != null && !busqueda.trim().isEmpty()) {
                String parametroLike = "%" + busqueda + "%";
                pstmt.setString(1, parametroLike);
                pstmt.setString(2, parametroLike);
                pstmt.setString(3, parametroLike);
                pstmt.setString(4, parametroLike);
            }

            rs = pstmt.executeQuery();
            while (rs.next()) {
                Map<String, Object> oficial = new HashMap<>();
                oficial.put("nombre1", rs.getString("nombre1"));
                oficial.put("nombre2", rs.getString("nombre2"));
                oficial.put("apellido1", rs.getString("apellido1"));
                oficial.put("apellido2", rs.getString("apellido2"));
                oficial.put("cedula", rs.getString("cedula"));
                oficial.put("numeroTelefono", rs.getString("numeroTelefono"));
                oficial.put("nombreUsuario", rs.getString("nombreUsuario"));
                oficial.put("contraseña", rs.getString("contraseña"));
                listaOficiales.add(oficial);
            }
        } catch (SQLException e) {
            throw new Exception("Error en la base de datos: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new Exception("Driver JDBC no encontrado: " + e.getMessage());
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) {}
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) {}
            if (con != null) try { con.close(); } catch (SQLException e) {}
        }
        return listaOficiales;
    }

    // Método para agregar un oficial
    private void agregarOficial(String nombre1, String nombre2, String apellido1, String apellido2, String cedula, String telefono, String nombreUsuario, String contrasena) throws Exception {
        Connection con = null;
        PreparedStatement pstmt = null;

        String sql = "INSERT INTO usuarios (nombre1, nombre2, apellido1, apellido2, nombreUsuario, contraseña, numeroTelefono, cedula, tipoUsuario) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            String url = "jdbc:mysql://localhost:3306/proyecto1?verifyServerCertificate=false&useSSL=true&useUnicode=true&characterEncoding=UTF-8";
            String user = "root";
            String password = "1234";
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, user, password);

            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, nombre1);
            pstmt.setString(2, nombre2 != null && !nombre2.isEmpty() ? nombre2 : null);
            pstmt.setString(3, apellido1);
            pstmt.setString(4, apellido2);
            pstmt.setString(5, nombreUsuario);
            pstmt.setString(6, contrasena);
            pstmt.setString(7, telefono);
            pstmt.setString(8, cedula);
            pstmt.setString(9, "Guarda");
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Error al agregar oficial: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new Exception("Driver JDBC no encontrado: " + e.getMessage());
        } finally {
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) {}
            if (con != null) try { con.close(); } catch (SQLException e) {}
        }
    }

    // Método para editar un oficial
    private void editarOficial(String nombre1, String nombre2, String apellido1, String apellido2, String telefono, String nombreUsuarioNuevo, String contrasena, String cedula, String nombreUsuarioOriginal) throws Exception {
        Connection con = null;
        PreparedStatement pstmt = null;

        String sql = "UPDATE usuarios SET cedula = ?, nombre1 = ?, nombre2 = ?, apellido1 = ?, apellido2 = ?, numeroTelefono = ?, nombreUsuario = ?, contraseña = ? WHERE nombreUsuario = ?";

        try {
            String url = "jdbc:mysql://localhost:3306/proyecto1?verifyServerCertificate=false&useSSL=true&useUnicode=true&characterEncoding=UTF-8";
            String user = "root";
            String password = "1234";
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, user, password);

            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, cedula);
            pstmt.setString(2, nombre1);
            pstmt.setString(3, nombre2 != null && !nombre2.isEmpty() ? nombre2 : null);
            pstmt.setString(4, apellido1);
            pstmt.setString(5, apellido2);
            pstmt.setString(6, telefono);
            pstmt.setString(7, nombreUsuarioNuevo);
            pstmt.setString(8, contrasena);
            pstmt.setString(9, nombreUsuarioOriginal);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Error al editar oficial: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new Exception("Driver JDBC no encontrado: " + e.getMessage());
        } finally {
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) {}
            if (con != null) try { con.close(); } catch (SQLException e) {}
        }
    }

    // Método para eliminar un oficial
    private void eliminarOficial(String nombreUsuario) throws Exception {
        Connection con = null;
        PreparedStatement pstmt = null;

        String sql = "DELETE FROM usuarios WHERE nombreUsuario = ? AND tipoUsuario = 'Guarda'";

        try {
            String url = "jdbc:mysql://localhost:3306/proyecto1?verifyServerCertificate=false&useSSL=true&useUnicode=true&characterEncoding=UTF-8";
            String user = "root";
            String password = "1234";
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, user, password);

            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, nombreUsuario);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Error al eliminar oficial: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new Exception("Driver JDBC no encontrado: " + e.getMessage());
        } finally {
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) {}
            if (con != null) try { con.close(); } catch (SQLException e) {}
        }
    }
%>

<%
request.setCharacterEncoding("UTF-8"); // Aseguramos que la codificación sea UTF-8
    // 2. LÓGICA JAVA: Procesar acciones y obtener la lista de oficiales
    List<Map<String, Object>> listaOficiales = null;
    String errorDB = null;
    String mensaje = null;
    String busqueda = request.getParameter("buscarOficial");

    // Procesar solicitudes POST (agregar/editar) y GET (eliminar)
    try {
        if ("POST".equalsIgnoreCase(request.getMethod())) {
            String action = request.getParameter("action");
            String nombre1 = request.getParameter("nombre1");
            String nombre2 = request.getParameter("nombre2");
            String apellido1 = request.getParameter("apellido1");
            String apellido2 = request.getParameter("apellido2");
            String cedula = request.getParameter("cedula");
            String telefono = request.getParameter("telefono");
            String nombreUsuario = request.getParameter("nombreUsuario");
            String contrasena = request.getParameter("contrasena");
            String nombreUsuarioOriginal = request.getParameter("nombreUsuarioOriginal");

            if ("agregar".equals(action)) {
                agregarOficial(nombre1, nombre2, apellido1, apellido2, cedula, telefono, nombreUsuario, contrasena);
                mensaje = "Oficial agregado exitosamente.";
            } else if ("editar".equals(action)) {
                editarOficial(nombre1, nombre2, apellido1, apellido2, telefono, nombreUsuario, contrasena, cedula, nombreUsuarioOriginal);
                mensaje = "Oficial editado exitosamente.";
            }
        } else if ("GET".equalsIgnoreCase(request.getMethod()) && request.getParameter("eliminar") != null) {
            String nombreUsuario = request.getParameter("nombreUsuario");
            eliminarOficial(nombreUsuario);
            mensaje = "Oficial eliminado exitosamente.";
        }

        // Obtener la lista de oficiales después de cualquier operación
        listaOficiales = obtenerTodosLosOficiales(busqueda);
    } catch (Exception e) {
        e.printStackTrace();
        errorDB = "Error al procesar la operación: " + e.getMessage();
    }
%>

<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <title>Panel de Oficiales • Admin</title>
  <link rel="stylesheet" href="estilosPEA.css" />
  <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600&display=swap" rel="stylesheet">
  <style>
    /* Estilos para las notificaciones */
    .notification {
      position: fixed;
      top: 20px;
      left: 50%;
      transform: translateX(-50%);
      padding: 10px 20px;
      border-radius: 5px;
      color: white;
      font-family: 'Inter', sans-serif;
      font-size: 16px;
      z-index: 1000;
      opacity: 0;
      transition: opacity 0.5s ease-in-out;
    }
    .notification.success {
      background-color: #28a745;
    }
    .notification.error {
      background-color: #dc3545;
    }
    .notification.show {
      opacity: 1;
    }
  </style>
</head>
<body>
  <header class="navbar">
    <h1 class="logo">Administración</h1>
    <nav>
      <ul>
        <li class="active"><a href="gestionOficiales.jsp">Oficiales</a></li>
        <li><a href="panelEstudiantes.jsp">Estudiantes</a></li>
        <li><a href="login.jsp">Salir</a></li>
      </ul>
    </nav>
  </header>

  <main class="container">
    <div class="main-content">
      <% if (mensaje != null) { %>
        <div id="notification" class="notification success"><%= mensaje %></div>
      <% } %>
      <% if (errorDB != null) { %>
        <div id="notification" class="notification error"><%= errorDB %></div>
      <% } %>
      <section class="form-section">
        <h3 id="form-titulo">Gestión de Oficiales</h3>
        <form id="form-oficial" action="gestionOficiales.jsp" method="post">
          <input type="hidden" id="nombreUsuarioOriginal" name="nombreUsuarioOriginal">
          <input type="hidden" id="action" name="action" value="agregar">
          <div class="form-group">
            <label for="nombre1">Primer nombre *</label>
            <input type="text" id="nombre1" name="nombre1" required>
          </div>
          <div class="form-group">
            <label for="nombre2">Segundo nombre (opcional)</label>
            <input type="text" id="nombre2" name="nombre2">
          </div>
          <div class="form-group">
            <label for="apellido1">Primer apellido *</label>
            <input type="text" id="apellido1" name="apellido1" required>
          </div>
          <div class="form-group">
            <label for="apellido2">Segundo apellido *</label>
            <input type="text" id="apellido2" name="apellido2" required>
          </div>
          <div class="form-group">
            <label for="cedula">Cédula *</label>
            <input type="text" id="cedula" name="cedula" maxlength="15" required>
          </div>
          <div class="form-group">
            <label for="telefono">Teléfono *</label>
            <input type="text" id="telefono" name="telefono" maxlength="15" required>
          </div>
          <div class="form-group">
            <label for="nombreUsuario">Nombre de Usuario *</label>
            <input type="text" id="nombreUsuario" name="nombreUsuario" required>
          </div>
          <div class="form-group">
            <label for="contrasena">Contraseña *</label>
            <input type="password" id="contrasena" name="contrasena" required>
          </div>
          <div class="footer-buttons">
            <button type="button" class="clear-button" onclick="limpiarYResetearForm()">
            <img src="https://img.icons8.com/?size=100&id=8068&format=png&color=FFFFFF" alt="Limpiar Icono" style="width: 20px; height: 20px; vertical-align: middle;">
            Limpiar</button>
            <button type="submit" class="add-button" id="btnAgregar">
            <img src="https://img.icons8.com/?size=100&id=HskoXdFIpCjN&format=png&color=FFFFFF" alt="Agregar Icono" style="width: 20px; height: 20px; vertical-align: middle;">
            Guardar</button>
          </div>
          <div class="footer-buttons">
            <button type="button" class="edit-button" id="btnEditarForm" onclick="document.getElementById('form-oficial').submit();" style="display:none;"> 
            <img src="https://img.icons8.com/?size=100&id=UkLBG0sZoWV0&format=png&color=FFFFFF" alt="Editar Icono" style="width: 20px; height: 20px; vertical-align: middle;">
            Guardar Cambios</button>
          </div>
        </form>
      </section>
      
      <section class="table-section">
        <div class="form-group">
          <input type="text" id="buscarOficial" name="buscarOficial" placeholder="🔍 Buscar Oficial…" onkeyup="filtrarTabla()" value="<%= busqueda != null ? busqueda : "" %>">
        </div>
        <table id="oficialesTable">
          <thead>
            <tr>
              <th>Nombre completo</th>
              <th>Cédula</th>
              <th>Teléfono</th>
              <th>Nombre de Usuario</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>
            <% if (listaOficiales != null && !listaOficiales.isEmpty()) { %>
                <% for (Map<String, Object> oficial : listaOficiales) { %>
                    <%
                        // Preparamos las variables dentro del bucle para más claridad
                        String nombreCompleto = oficial.get("nombre1") + " "
                                + (oficial.get("nombre2") != null ? oficial.get("nombre2") + " " : "")
                                + oficial.get("apellido1") + " " + oficial.get("apellido2");
                        String cedula = (String) oficial.get("cedula");
                        String telefono = (String) oficial.get("numeroTelefono");
                        String nombreUsuario = (String) oficial.get("nombreUsuario");
                        String contrasena = (String) oficial.get("contraseña");

                        // Escapamos las comillas para JavaScript
                        String jsNombre1 = ((String) oficial.get("nombre1")).replace("'", "\\'").replace("\"", "");
                        String jsNombre2 = (oficial.get("nombre2") != null) ? ((String) oficial.get("nombre2")).replace("'", "\\'").replace("\"", "") : "";
                        String jsApellido1 = ((String) oficial.get("apellido1")).replace("'", "\\'").replace("\"", "");
                        String jsApellido2 = ((String) oficial.get("apellido2")).replace("'", "\\'").replace("\"", "");
                        String jsCedula = cedula.replace("'", "\\'").replace("\"", "");
                        String jsTelefono = telefono.replace("'", "\\'").replace("\"", "");
                        String jsNombreUsuario = nombreUsuario.replace("'", "\\'").replace("\"", "");
                        String jsContrasena = contrasena.replace("'", "\\'").replace("\"", "");
                    %>
                    <tr>
                        <td><%= nombreCompleto %></td>
                        <td><%= cedula %></td>
                        <td><%= telefono %></td>
                        <td><%= nombreUsuario %></td>
                        <td>
                            <a href="#" 
                               style="margin-right:10px;"
                               onclick="cargarDatosParaEditar(
                                   '<%= jsNombre1 %>',
                                   '<%= jsNombre2 %>',
                                   '<%= jsApellido1 %>',
                                   '<%= jsApellido2 %>',
                                   '<%= jsCedula %>',
                                   '<%= jsTelefono %>',
                                   '<%= jsNombreUsuario %>',
                                   '<%= jsContrasena %>',
                                   '<%= jsNombreUsuario %>')">
                                   <img src="https://img.icons8.com/?size=100&id=86374&format=png&color=FCC419" alt="Editar Icono" style="width: 20px; height: 20px; vertical-align: middle;">
                                   </a>
                            <a href="gestionOficiales.jsp?eliminar=true&nombreUsuario=<%= nombreUsuario %>" 
                               onclick="return confirm('¿Confirmas que deseas eliminar al oficial <%= nombreUsuario %>?');">
                               <img src="https://img.icons8.com/?size=100&id=99950&format=png&color=FF2323" alt="Eliminar Icono" style="width: 20px; height: 20px; vertical-align: middle;">
                               </a>
                        </td>
                    </tr>
                <% } %>
            <% } else { %>
                <tr><td colspan="5" style="text-align:center;">No hay oficiales registrados.</td></tr>
            <% } %>
          </tbody>
        </table>
      </section>
    </div>
  </main>
  
  <footer class="footer">
    © 2025 Seguridad y Tecnología • Todos los derechos reservados
  </footer>

  <script>
    const form = document.getElementById('form-oficial');
    const btnAgregar = document.getElementById('btnAgregar');
    const btnEditarForm = document.getElementById('btnEditarForm');
    const formTitulo = document.getElementById('form-titulo');
    const actionInput = document.getElementById('action');

    function cargarDatosParaEditar(nombre1, nombre2, apellido1, apellido2, cedula, telefono, nombreUsuario, contrasena, nombreUsuarioOriginal) {
       document.getElementById('nombre1').value = nombre1;
       document.getElementById('nombre2').value = nombre2;
       document.getElementById('apellido1').value = apellido1;
       document.getElementById('apellido2').value = apellido2;
       document.getElementById('cedula').value = cedula;
       document.getElementById('telefono').value = telefono;
       document.getElementById('nombreUsuario').value = nombreUsuario;
       document.getElementById('contrasena').value = contrasena;
       document.getElementById('nombreUsuarioOriginal').value = nombreUsuarioOriginal;
       actionInput.value = 'editar';
       formTitulo.innerText = 'Editando a ' + nombreUsuarioOriginal;
       btnAgregar.style.display = 'none';
       btnEditarForm.style.display = 'inline-block';
       window.scrollTo(0, 0);
    }

    function limpiarYResetearForm() {
       form.reset();
       actionInput.value = 'agregar';
       formTitulo.innerText = 'Gestión de Oficiales';
       btnAgregar.style.display = 'inline-block';
       btnEditarForm.style.display = 'none';
    }
    
    document.addEventListener('DOMContentLoaded', function() {
       limpiarYResetearForm();
       // Mostrar y ocultar notificaciones automáticamente
       const notification = document.getElementById('notification');
       if (notification) {
         notification.classList.add('show');
         setTimeout(() => {
           notification.classList.remove('show');
           setTimeout(() => { notification.remove(); }, 500); // Eliminar después de desvanecerse
         }, 3000); // Mostrar durante 3 segundos
       }
    });

    function filtrarTabla() {
        var input = document.getElementById('buscarOficial').value.toLowerCase();
        var table = document.getElementById('oficialesTable');
        var tr = table.getElementsByTagName('tr');
        for (var i = 1; i < tr.length; i++) {
            var found = false;
            var td = tr[i].getElementsByTagName('td');
            for (var j = 0; j < td.length - 1; j++) {
                if (td[j] && td[j].textContent.toLowerCase().indexOf(input) > -1) {
                    found = true;
                    break;
                }
            }
            tr[i].style.display = found ? '' : 'none';
        }
    }
  </script>
</body>
</html>