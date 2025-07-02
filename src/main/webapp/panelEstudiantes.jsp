<%-- 1. DIRECTIVAS DE PÁGINA: Imports y configuración --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, java.util.ArrayList, java.util.Map, java.util.HashMap, java.sql.*" %>

<%-- prettier-ignore --%>
<%! // Aseguramos que la codificación sea UTF-8
    // Método para conectar a la base de datos y obtener estudiantes
    private List<Map<String, Object>> obtenerTodosLosEstudiantes() throws Exception {
        List<Map<String, Object>> estudiantes = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            String url = "jdbc:mysql://localhost:3306/proyecto1?verifyServerCertificate=false&useSSL=true&useUnicode=true&characterEncoding=UTF-8";
            String user = "root";
            String password = "1234";
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);

            String sql = "SELECT carnet, nombre1, nombre2, apellido1, apellido2, cedula, nacionalidad, direccion, fechaNacimiento, " +
                         "TIMESTAMPDIFF(YEAR, fechaNacimiento, CURDATE()) AS edad FROM estudiantes";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Map<String, Object> estudiante = new HashMap<>();
                estudiante.put("carnet", rs.getString("carnet"));
                estudiante.put("nombre1", rs.getString("nombre1"));
                estudiante.put("nombre2", rs.getString("nombre2"));
                estudiante.put("apellido1", rs.getString("apellido1"));
                estudiante.put("apellido2", rs.getString("apellido2"));
                estudiante.put("cedula", rs.getString("cedula"));
                estudiante.put("nacionalidad", rs.getString("nacionalidad"));
                estudiante.put("direccion", rs.getString("direccion"));
                estudiante.put("fechaNacimiento", rs.getDate("fechaNacimiento"));
                estudiante.put("edad", rs.getInt("edad"));
                estudiantes.add(estudiante);
            }
        } catch (SQLException e) {
            throw new Exception("Error en la base de datos: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new Exception("Driver JDBC no encontrado: " + e.getMessage());
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) {}
            if (stmt != null) try { stmt.close(); } catch (SQLException e) {}
            if (conn != null) try { conn.close(); } catch (SQLException e) {}
        }
        return estudiantes;
    }

    // Método para agregar un estudiante
    private void agregarEstudiante(String carnet, String nombre1, String nombre2, String apellido1, String apellido2,
                                  String cedula, String nacionalidad, String direccion, String fechaNacimiento) throws Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            String url = "jdbc:mysql://localhost:3306/proyecto1?verifyServerCertificate=false&useSSL=true&useUnicode=true&characterEncoding=UTF-8";
            String user = "root";
            String password = "1234";
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);

            String sql = "INSERT INTO estudiantes (carnet, nombre1, nombre2, apellido1, apellido2, cedula, nacionalidad, direccion, fechaNacimiento) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, carnet);
            pstmt.setString(2, nombre1);
            pstmt.setString(3, nombre2 != null && !nombre2.isEmpty() ? nombre2 : null);
            pstmt.setString(4, apellido1);
            pstmt.setString(5, apellido2);
            pstmt.setString(6, cedula);
            pstmt.setString(7, nacionalidad != null && !nacionalidad.isEmpty() ? nacionalidad : null);
            pstmt.setString(8, direccion);
            pstmt.setString(9, fechaNacimiento);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Error al agregar estudiante: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new Exception("Driver JDBC no encontrado: " + e.getMessage());
        } finally {
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) {}
            if (conn != null) try { conn.close(); } catch (SQLException e) {}
        }
    }

    // Método para editar un estudiante
    private void editarEstudiante(String carnetOriginal, String carnet, String nombre1, String nombre2, String apellido1, String apellido2,
                                 String cedula, String nacionalidad, String direccion, String fechaNacimiento) throws Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            String url = "jdbc:mysql://localhost:3306/proyecto1?verifyServerCertificate=false&useSSL=true&useUnicode=true&characterEncoding=UTF-8";
            String user = "root";
            String password = "1234";
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);

            String sql = "UPDATE estudiantes SET carnet = ?, nombre1 = ?, nombre2 = ?, apellido1 = ?, apellido2 = ?, " +
                         "cedula = ?, nacionalidad = ?, direccion = ?, fechaNacimiento = ? WHERE carnet = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, carnet);
            pstmt.setString(2, nombre1);
            pstmt.setString(3, nombre2 != null && !nombre2.isEmpty() ? nombre2 : null);
            pstmt.setString(4, apellido1);
            pstmt.setString(5, apellido2);
            pstmt.setString(6, cedula);
            pstmt.setString(7, nacionalidad != null && !nacionalidad.isEmpty() ? nacionalidad : null);
            pstmt.setString(8, direccion);
            pstmt.setString(9, fechaNacimiento);
            pstmt.setString(10, carnetOriginal);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Error al editar estudiante: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new Exception("Driver JDBC no encontrado: " + e.getMessage());
        } finally {
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) {}
            if (conn != null) try { conn.close(); } catch (SQLException e) {}
        }
    }

    // Método para eliminar un estudiante
    private void eliminarEstudiante(String carnet) throws Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            String url = "jdbc:mysql://localhost:3306/proyecto1?verifyServerCertificate=false&useSSL=true&useUnicode=true&characterEncoding=UTF-8";
            String user = "root";
            String password = "1234";
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);

            String sql = "DELETE FROM estudiantes WHERE carnet = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, carnet);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Error al eliminar estudiante: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new Exception("Driver JDBC no encontrado: " + e.getMessage());
        } finally {
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) {}
            if (conn != null) try { conn.close(); } catch (SQLException e) {}
        }
    }
%>

<%
request.setCharacterEncoding("UTF-8"); // Aseguramos que la codificación sea UTF-8
    // 2. LÓGICA JAVA: Procesar acciones y obtener la lista de estudiantes
    List<Map<String, Object>> listaEstudiantes = null;
    String errorDB = null;
    String mensaje = null;

    // Procesar solicitudes POST (agregar/editar) y GET (eliminar)
    try {
        if ("POST".equalsIgnoreCase(request.getMethod())) {
            String action = request.getParameter("action");
            String carnet = request.getParameter("carnet");
            String nombre1 = request.getParameter("nombre1");
            String nombre2 = request.getParameter("nombre2");
            String apellido1 = request.getParameter("apellido1");
            String apellido2 = request.getParameter("apellido2");
            String cedula = request.getParameter("cedula");
            String nacionalidad = request.getParameter("nacionalidad");
            String direccion = request.getParameter("direccion");
            String fechaNacimiento = request.getParameter("fechaNacimiento");
            String carnetOriginal = request.getParameter("carnetOriginal");

            if ("agregar".equals(action)) {
                agregarEstudiante(carnet, nombre1, nombre2, apellido1, apellido2, cedula, nacionalidad, direccion, fechaNacimiento);
                mensaje = "Estudiante agregado exitosamente.";
            } else if ("editar".equals(action)) {
                editarEstudiante(carnetOriginal, carnet, nombre1, nombre2, apellido1, apellido2, cedula, nacionalidad, direccion, fechaNacimiento);
                mensaje = "Estudiante editado exitosamente.";
            }
        } else if ("GET".equalsIgnoreCase(request.getMethod()) && request.getParameter("eliminar") != null) {
            String carnet = request.getParameter("carnet");
            eliminarEstudiante(carnet);
            mensaje = "Estudiante eliminado exitosamente.";
        }

        // Obtener la lista de estudiantes después de cualquier operación
        listaEstudiantes = obtenerTodosLosEstudiantes();
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
  <title>Panel de Estudiantes • Admin</title>
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
        <li><a href="gestionOficiales.jsp">Oficiales</a></li>
        <li class="active"><a href="panelEstudiantes.jsp">Estudiantes</a></li>
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
        <h3 id="form-titulo">Gestión de Estudiantes</h3>
        <form id="form-estudiante" action="panelEstudiantes.jsp" method="post">
          <input type="hidden" id="carnetOriginal" name="carnetOriginal">
          <input type="hidden" id="action" name="action" value="agregar">
          <div class="form-group">
            <label for="carnet">Carnet *</label>
            <input type="text" id="carnet" name="carnet" maxlength="6" required>
          </div>
          <div class="form-group">
            <label for="nombre1">Primer nombre *</label>
            <input type="text" id="nombre1" name="nombre1" required>
          </div>
          <div class="form-group">
            <label for="nombre2">Segunda nombre (opcional)</label>
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
            <label for="nacionalidad">Nacionalidad (opcional)</label>
            <select id="nacionalidad" name="nacionalidad">
              <option value="">Seleccione...</option>
              <option value="nacional">Nacional</option>
              <option value="extranjero">Extranjero</option>
            </select>
          </div>
          <div class="form-group">
            <label for="direccion">Dirección *</label>
            <select id="direccion" name="direccion" required>
              <option value="">Seleccione...</option>
              <option>Upala</option>
              <option>Aguas Claras</option>
              <option>San José de Upala</option>
              <option>Bijagua</option>
              <option>Delicias</option>
              <option>Dos Ríos</option>
              <option>Yolillal</option>
              <option>Canalete</option>
            </select>
          </div>
          <div class="form-group">
            <label for="fechaNacimiento">Fecha de Nacimiento *</label>
            <input type="date" id="fechaNacimiento" name="fechaNacimiento" required>
          </div>
          <div class="footer-buttons">
            <button type="button" class="clear-button" onclick="limpiarYResetearForm()">Limpiar</button>
            <button type="submit" class="add-button" id="btnAgregar">Guardar</button>
          </div>
          <div class="footer-buttons">
            <button type="button" class="edit-button" id="btnEditarForm" onclick="document.getElementById('form-estudiante').submit();" style="display:none;"> Guardar Cambios</button>
          </div>
        </form>
      </section>
      
      <section class="table-section">
        <div class="form-group">
          <input type="text" id="buscarEstudiante" placeholder="🔍 Buscar Estudiante…" onkeyup="filtrarTabla()">
        </div>
        <table id="estudiantesTable">
          <thead>
            <tr>
              <th>Nombre completo</th>
              <th>Carnet</th>
              <th>Cédula</th>
              <th>Fecha de Nacimiento</th>
              <th>Edad</th>
              <th>Nacionalidad</th>
              <th>Dirección</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>
            <% if (listaEstudiantes != null && !listaEstudiantes.isEmpty()) { %>
                <% for (Map<String, Object> estudiante : listaEstudiantes) { %>
                    <%
                        // Preparamos las variables dentro del bucle para más claridad
                        String nombreCompleto = estudiante.get("nombre1") + " "
                                + (estudiante.get("nombre2") != null ? estudiante.get("nombre2") + " " : "")
                                + estudiante.get("apellido1") + " " + estudiante.get("apellido2");
                        String carnet = (String) estudiante.get("carnet");
                        String cedula = (String) estudiante.get("cedula");
                        Date fechaNacimiento = (Date) estudiante.get("fechaNacimiento");
                        String fechaNacimientoStr = (fechaNacimiento != null) ? fechaNacimiento.toString() : "";
                        int edad = (estudiante.get("edad") != null) ? (int) estudiante.get("edad") : 0;
                        String nacionalidad = (estudiante.get("nacionalidad") != null) ? (String) estudiante.get("nacionalidad") : "";
                        String direccion = (String) estudiante.get("direccion");

                        // Escapamos las comillas para JavaScript
                        String jsNombre1 = ((String) estudiante.get("nombre1")).replace("'", "\\'").replace("\"", "");
                        String jsNombre2 = (estudiante.get("nombre2") != null) ? ((String) estudiante.get("nombre2")).replace("'", "\\'").replace("\"", "") : "";
                        String jsApellido1 = ((String) estudiante.get("apellido1")).replace("'", "\\'").replace("\"", "");
                        String jsApellido2 = ((String) estudiante.get("apellido2")).replace("'", "\\'").replace("\"", "");
                        String jsCarnet = carnet.replace("'", "\\'").replace("\"", "");
                        String jsCedula = cedula.replace("'", "\\'").replace("\"", "");
                        String jsNacionalidad = nacionalidad.replace("'", "\\'").replace("\"", "");
                        String jsDireccion = direccion.replace("'", "\\'").replace("\"", "");
                        String jsFechaNacimiento = fechaNacimientoStr.replace("'", "\\'").replace("\"", "");
                    %>
                    <tr>
                        <td><%= nombreCompleto %></td>
                        <td><%= carnet %></td>
                        <td><%= cedula %></td>
                        <td><%= fechaNacimientoStr %></td>
                        <td><%= edad %></td>
                        <td><%= nacionalidad %></td>
                        <td><%= direccion %></td>
                        <td>
                            <a href="#" 
                               style="margin-right:10px;"
                               onclick="cargarDatosParaEditar(
                                   '<%= jsCarnet %>',
                                   '<%= jsNombre1 %>',
                                   '<%= jsNombre2 %>',
                                   '<%= jsApellido1 %>',
                                   '<%= jsApellido2 %>',
                                   '<%= jsCedula %>',
                                   '<%= jsNacionalidad %>',
                                   '<%= jsDireccion %>',
                                   '<%= jsFechaNacimiento %>',
                                   '<%= jsCarnet %>')">Editar</a>
                            <a href="panelEstudiantes.jsp?eliminar=true&carnet=<%= carnet %>" 
                               onclick="return confirm('¿Confirmas que deseas eliminar al estudiante <%= carnet %>?');">Eliminar</a>
                        </td>
                    </tr>
                <% } %>
            <% } else { %>
                <tr><td colspan="8" style="text-align:center;">No hay estudiantes registrados.</td></tr>
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
    const form = document.getElementById('form-estudiante');
    const btnAgregar = document.getElementById('btnAgregar');
    const btnEditarForm = document.getElementById('btnEditarForm');
    const formTitulo = document.getElementById('form-titulo');
    const actionInput = document.getElementById('action');

    function cargarDatosParaEditar(carnet, nombre1, nombre2, apellido1, apellido2, cedula, nacionalidad, direccion, fechaNacimiento, carnetOriginal) {
       document.getElementById('carnet').value = carnet;
       document.getElementById('nombre1').value = nombre1;
       document.getElementById('nombre2').value = nombre2;
       document.getElementById('apellido1').value = apellido1;
       document.getElementById('apellido2').value = apellido2;
       document.getElementById('cedula').value = cedula;
       document.getElementById('nacionalidad').value = nacionalidad;
       document.getElementById('direccion').value = direccion;
       document.getElementById('fechaNacimiento').value = fechaNacimiento;
       document.getElementById('carnetOriginal').value = carnetOriginal;
       actionInput.value = 'editar';
       formTitulo.innerText = 'Editando a ' + carnetOriginal;
       btnAgregar.style.display = 'none';
       btnEditarForm.style.display = 'inline-block';
       window.scrollTo(0, 0);
    }

    function limpiarYResetearForm() {
       form.reset();
       actionInput.value = 'agregar';
       formTitulo.innerText = 'Gestión de Estudiantes';
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
        var input = document.getElementById('buscarEstudiante').value.toLowerCase();
        var table = document.getElementById('estudiantesTable');
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