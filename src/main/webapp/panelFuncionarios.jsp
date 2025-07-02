<%-- ================================================================== --%>
<%-- 1. DIRECTIVAS DE PÁGINA E IMPORTACIONES                        --%>
<%-- ================================================================== --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.sql.*, java.util.*" %>

<%-- ================================================================== --%>
<%-- 2. LÓGICA DEL SERVIDOR (CRUD COMPLETO Y AUTÓNOMO)                --%>
<%-- ================================================================== --%>
<%
 request.setCharacterEncoding("UTF-8");
    // -----------------------------------------------------------------
    // ¡ATENCIÓN! CONFIGURACIÓN DE LA BASE DE DATOS
    // -----------------------------------------------------------------
    String DB_URL = "jdbc:mysql://localhost:3306/proyecto1?verifyServerCertificate=false&useSSL=true&useUnicode=true&characterEncoding=UTF-8";
    String DB_USER = "root";
    String DB_PASS = "1234"; // ¡TU CONTRASEÑA!
    String DB_DRIVER = "com.mysql.jdbc.Driver";

    // -----------------------------------------------------------------
    // LÓGICA DE ACCIONES (MANEJO DE AGREGAR, EDITAR, ELIMINAR)
    // -----------------------------------------------------------------
    String action = request.getParameter("action");
    if (action != null && !action.isEmpty()) {

        Connection conn = null;
        try {
            Class.forName(DB_DRIVER);
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            conn.setAutoCommit(false); // INICIAMOS TRANSACCIÓN PARA TODAS LAS ACCIONES

            // --- ACCIÓN: AGREGAR FUNCIONARIO (Lógica adaptada de FuncionarioDAO.java) ---
            if ("agregar".equals(action) && "POST".equalsIgnoreCase(request.getMethod())) {
                String sqlVehiculo = "INSERT INTO vehiculos (placa, tipoVehiculo) VALUES (?, ?)";
                String sqlPersona = "INSERT INTO personas (nombre1, nombre2, apellido1, apellido2, cedula, ocupacion, placaVehiculo, tipoPersona) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                String placa = request.getParameter("placa");
                
                if (placa != null && !placa.trim().isEmpty()) {
                    try (PreparedStatement pstmtVehiculo = conn.prepareStatement(sqlVehiculo)) {
                        pstmtVehiculo.setString(1, placa);
                        pstmtVehiculo.setString(2, request.getParameter("tipoVehiculo"));
                        pstmtVehiculo.executeUpdate();
                    }
                }

                try (PreparedStatement pstmtPersona = conn.prepareStatement(sqlPersona)) {
                    pstmtPersona.setString(1, request.getParameter("primerNombre"));
                    pstmtPersona.setString(2, request.getParameter("segundoNombre"));
                    pstmtPersona.setString(3, request.getParameter("primerApellido"));
                    pstmtPersona.setString(4, request.getParameter("segundoApellido"));
                    pstmtPersona.setString(5, request.getParameter("identificacion"));
                    pstmtPersona.setString(6, request.getParameter("rolFuncionario"));
                    pstmtPersona.setString(7, placa);
                    pstmtPersona.setString(8, "Funcionario");
                    pstmtPersona.executeUpdate();
                }
            }

            // --- ACCIÓN: EDITAR FUNCIONARIO (Lógica adaptada de FuncionarioDAO.java) ---
            else if ("editar".equals(action) && "POST".equalsIgnoreCase(request.getMethod())) {
                String cedulaOriginal = request.getParameter("cedulaOriginal");
                String placaNueva = request.getParameter("placa");
                if (placaNueva != null && placaNueva.trim().isEmpty()) placaNueva = null;
                String placaAntigua = null;

                // 1. Obtener placa antigua
                try (PreparedStatement pstmtSelect = conn.prepareStatement("SELECT placaVehiculo FROM personas WHERE cedula = ?")) {
                    pstmtSelect.setString(1, cedulaOriginal);
                    try (ResultSet rs = pstmtSelect.executeQuery()) {
                        if (rs.next()) placaAntigua = rs.getString("placaVehiculo");
                    }
                }
                
                // 2. Insertar o actualizar vehículo nuevo
                if (placaNueva != null) {
                    String sqlUpsert = "INSERT INTO vehiculos (placa, tipoVehiculo) VALUES (?, ?) ON DUPLICATE KEY UPDATE tipoVehiculo=VALUES(tipoVehiculo)";
                    try (PreparedStatement pstmt = conn.prepareStatement(sqlUpsert)) {
                        pstmt.setString(1, placaNueva);
                        pstmt.setString(2, request.getParameter("tipoVehiculo"));
                        pstmt.executeUpdate();
                    }
                }

                // 3. Actualizar la persona
                String sqlUpdate = "UPDATE personas SET cedula = ?, nombre1 = ?, nombre2 = ?, apellido1 = ?, apellido2 = ?, ocupacion = ?, placaVehiculo = ? WHERE cedula = ? AND tipoPersona = 'Funcionario'";
                try (PreparedStatement pstmt = conn.prepareStatement(sqlUpdate)) {
                    pstmt.setString(1, request.getParameter("identificacion"));
                    pstmt.setString(2, request.getParameter("primerNombre"));
                    pstmt.setString(3, request.getParameter("segundoNombre"));
                    pstmt.setString(4, request.getParameter("primerApellido"));
                    pstmt.setString(5, request.getParameter("segundoApellido"));
                    pstmt.setString(6, request.getParameter("rolFuncionario"));
                    pstmt.setString(7, placaNueva);
                    pstmt.setString(8, cedulaOriginal);
                    pstmt.executeUpdate();
                }

                // 4. Borrar vehículo antiguo si es necesario
                if (placaAntigua != null && !placaAntigua.equals(placaNueva)) {
                    try (PreparedStatement pstmt = conn.prepareStatement("DELETE FROM vehiculos WHERE placa = ?")) {
                        pstmt.setString(1, placaAntigua);
                        pstmt.executeUpdate();
                    }
                }
            }

            // --- ACCIÓN: ELIMINAR FUNCIONARIO (Lógica adaptada de FuncionarioDAO.java) ---
            else if ("eliminar".equals(action) && "GET".equalsIgnoreCase(request.getMethod())) {
                String cedula = request.getParameter("cedula");
                String placaParaBorrar = null;

                // 1. Obtener la placa antes de borrar
                try (PreparedStatement pstmtSelect = conn.prepareStatement("SELECT placaVehiculo FROM personas WHERE cedula = ?")) {
                    pstmtSelect.setString(1, cedula);
                    try (ResultSet rs = pstmtSelect.executeQuery()) {
                        if (rs.next()) placaParaBorrar = rs.getString("placaVehiculo");
                    }
                }

                // 2. Eliminar la persona
                try (PreparedStatement pstmtDelete = conn.prepareStatement("DELETE FROM personas WHERE cedula = ? AND tipoPersona = 'Funcionario'")) {
                    pstmtDelete.setString(1, cedula);
                    pstmtDelete.executeUpdate();
                }

                // 3. Eliminar el vehículo si existía
                if (placaParaBorrar != null && !placaParaBorrar.trim().isEmpty()) {
                    try (PreparedStatement pstmtDeleteVehiculo = conn.prepareStatement("DELETE FROM vehiculos WHERE placa = ?")) {
                        pstmtDeleteVehiculo.setString(1, placaParaBorrar);
                        pstmtDeleteVehiculo.executeUpdate();
                    }
                }
            }

            conn.commit(); // CONFIRMAMOS LA TRANSACCIÓN SI TODO FUE BIEN

        } catch (Exception e) {
            e.printStackTrace();
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); } // DESHACEMOS CAMBIOS SI ALGO FALLÓ
        } finally {
            if (conn != null) try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); } // CERRAMOS LA CONEXIÓN
        }

        // Redirigir para evitar reenvío de formulario y recargar la página
        response.sendRedirect("panelFuncionarios.jsp");
        return;
    }

    // -----------------------------------------------------------------
    // LÓGICA DE CARGA DE DATOS (Lógica de obtenerTodosLosFuncionarios)
    // -----------------------------------------------------------------
    List<Map<String, Object>> listaFuncionarios = new ArrayList<>();
    String errorDB = null;

    try {
        Class.forName(DB_DRIVER);
        String sql = "SELECT p.*, v.tipoVehiculo FROM personas p LEFT JOIN vehiculos v ON p.placaVehiculo = v.placa WHERE p.tipoPersona = 'Funcionario' ORDER BY p.apellido1, p.nombre1";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Map<String, Object> f = new HashMap<>();
                f.put("id", rs.getString("cedula"));
                f.put("puesto", rs.getString("ocupacion"));
                f.put("nombreCompleto", rs.getString("nombre1") + " " + rs.getString("apellido1"));
                f.put("placa", rs.getString("placaVehiculo"));
                
                String tipoVehiculo = rs.getString("tipoVehiculo");
                f.put("vehiculo", (tipoVehiculo == null || tipoVehiculo.trim().isEmpty()) ? "No registra" : tipoVehiculo);
                
                f.put("primerNombre", rs.getString("nombre1"));
                f.put("segundoNombre", rs.getString("nombre2"));
                f.put("primerApellido", rs.getString("apellido1"));
                f.put("segundoApellido", rs.getString("apellido2"));
                listaFuncionarios.add(f);
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
        errorDB = "Error al conectar o leer de la base de datos: " + e.getMessage();
    }
%>

<%-- ================================================================== --%>
<%-- 3. PARTE HTML (LA VISTA)                                         --%>
<%-- ================================================================== --%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Funcionarios • Oficial</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="estilosPFO.css">
</head>
<body>
    <header class="navbar">
        <h1 class="logo">Gestión de Seguridad</h1>
        <nav>
            <ul>
                <li><a href="<%= request.getContextPath() %>/panelSalidaEstudiante.jsp">Salida de Estudiantes</a></li>
                <li class="active"><a href="#">Funcionarios</a></li>
                <li><a href="<%= request.getContextPath() %>/panelIngresoFuncionarios.jsp">Ingreso Funcionario</a></li>
                <li><a href="<%= request.getContextPath() %>/login.jsp">Salir</a></li>
            </ul>
        </nav>
    </header>

    <main class="container">
        <% if (errorDB != null) { %>
            <h1 style="color: red; text-align: center;"><%= errorDB %></h1>
        <% } else { %>
            <h2 class="page-title">Registra los funcionarios del CTP UPALA</h2>
            <div class="main-content">
                <section class="form-section">
                    <form id="formFuncionario" action="panelFuncionarios.jsp" method="POST">
                        <input type="hidden" name="action" id="actionInput" value="agregar">
                        <input type="hidden" id="cedulaOriginal" name="cedulaOriginal">
                        <h3 id="form-titulo">Registrar Funcionario</h3>
                        <div class="form-row">
                            <div class="form-group full"><label for="rolFuncionario">Rol: *</label><select id="rolFuncionario" name="rolFuncionario" required><option value="">--</option><option>Profesor(a)</option><option>Administrador(a)</option><option>Secretario(a)</option><option>Vigilante</option><option>Otro</option></select></div>
                            <div class="form-group"><label for="primerNombre">Primer nombre: *</label><input type="text" id="primerNombre" name="primerNombre" required></div>
                            <div class="form-group"><label for="segundoNombre">Segundo nombre:</label><input type="text" id="segundoNombre" name="segundoNombre"></div>
                            <div class="form-group"><label for="primerApellido">Primer apellido: *</label><input type="text" id="primerApellido" name="primerApellido" required></div>
                            <div class="form-group"><label for="segundoApellido">Segundo apellido: *</label><input type="text" id="segundoApellido" name="segundoApellido" required></div>
                            <div class="form-group"><label for="identificacion">ID: *</label><input type="text" id="identificacion" name="identificacion" required></div>
                            <div class="form-group"><label for="tipoVehiculo">Vehículo: *</label><select id="tipoVehiculo" name="tipoVehiculo" required><option value="">--</option><option>Automóvil</option><option>Motocicleta</option><option>Bicicleta</option><option>Camión</option><option>Autobús</option><option>Otro</option></select></div>
                            <div class="form-group"><label for="placa">Placa:</label><input type="text" id="placa" name="placa"></div>
                        </div>
                        <div class="form-actions">
                            <button type="button" onclick="limpiarYResetearForm()" class="btn-clear">
                            <img src="https://img.icons8.com/?size=100&id=8068&format=png&color=FFFFFF" alt="Limpiar" style="width: 20px; height: 20px; vertical-align: middle;">
                            Limpiar</button>
                            <button type="submit" id="btnAgregar" class="btn-primary">
                            <img src="https://img.icons8.com/?size=100&id=UkLBG0sZoWV0&format=png&color=FFFFFF" alt="Agregar" style="width: 20px; height: 20px; vertical-align: middle;">
                            Registrar</button>
                            <button type="button" id="btnEditarForm" onclick="document.getElementById('formFuncionario').submit();" class="btn-secondary" style="display:none;">
                            <img src="https://img.icons8.com/?size=100&id=83208&format=png&color=FFFFFF" alt="Guardar Cambios" style="width: 20px; height: 20px; vertical-align: middle;">
                            Guardar Cambios</button>
                        </div>
                    </form>
                </section>
                
                <section class="table-section">
                    <table>
                        <thead>
                            <tr><th>Puesto</th><th>Nombre Completo</th><th>ID</th><th>Vehículo</th><th>Placa</th><th>Acciones</th></tr>
                        </thead>
                        <tbody>
                            <% if (listaFuncionarios != null && !listaFuncionarios.isEmpty()) {
                                for (Map<String, Object> f : listaFuncionarios) { 
                                    String cedulaOriginal = (String) f.get("id");
                                    String urlEliminar = "panelFuncionarios.jsp?action=eliminar&cedula=" + cedulaOriginal;
                            
                                    String p_cedula = cedulaOriginal;
                                    String p_rol = f.get("puesto")!=null ? f.get("puesto").toString().replace("'","\\'") : "";
                                    String p_nombre1 = f.get("primerNombre")!=null ? f.get("primerNombre").toString().replace("'","\\'") : "";
                                    String p_nombre2 = f.get("segundoNombre")!=null ? f.get("segundoNombre").toString().replace("'","\\'") : "";
                                    String p_apellido1 = f.get("primerApellido")!=null ? f.get("primerApellido").toString().replace("'","\\'") : "";
                                    String p_apellido2 = f.get("segundoApellido")!=null ? f.get("segundoApellido").toString().replace("'","\\'") : "";
                                    String p_vehiculo = f.get("vehiculo")!=null ? f.get("vehiculo").toString().replace("'","\\'") : "";
                                    String p_placa = f.get("placa")!=null ? f.get("placa").toString().replace("'","\\'") : "";
                            
                                    String datosEditar = "'" + p_cedula + "', '" + p_rol + "', '" + p_nombre1 + "', '" + p_nombre2 + "', '" + p_apellido1 + "', '" + p_apellido2 + "', '" + p_vehiculo + "', '" + p_placa + "'";
                            %>
                            <tr>
                                <td><%= f.get("puesto") %></td>
                                <td><%= f.get("nombreCompleto") %></td>
                                <td><%= f.get("id") %></td>
                                <td><%= f.get("vehiculo") %></td>
                                <td><%= f.get("placa") != null ? f.get("placa") : "" %></td>
                                <td>
                                    <a href="#" onclick="cargarDatosParaEditar(<%= datosEditar %>)" style='margin-right:10px;'>
                                        <img src="https://img.icons8.com/?size=100&id=86374&format=png&color=FCC419" alt="Editar" style="width: 20px; height: 20px; vertical-align: middle;">
                                    </a>
                                    <a href="<%= urlEliminar %>" onclick="return confirm('¿Estás seguro de que quieres eliminar a este funcionario?');">
                                        <img src="https://img.icons8.com/?size=100&id=99950&format=png&color=FF2323" alt="Eliminar" style="width: 20px; height: 20px; vertical-align: middle;">
                                    </a>
                                </td>
                            </tr>
                            <%  } 
                            } else { %>
                                <tr><td colspan="6" style="text-align:center;">No hay funcionarios registrados.</td></tr>
                            <% } %>
                        </tbody>
                    </table>
                </section>
            </div>
        <% } %>
    </main>

    <footer class="footer">© 2025 Seguridad y Tecnología • Todos los derechos reservados</footer>
    
    <script>
        const form = document.getElementById('formFuncionario');
        const actionInput = document.getElementById('actionInput');
        const btnAgregar = document.getElementById('btnAgregar');
        const btnEditarForm = document.getElementById('btnEditarForm');
        const formTitulo = document.getElementById('form-titulo');

        function cargarDatosParaEditar(cedula, rol, nombre1, nombre2, apellido1, apellido2, tipoVehiculo, placa) {
            document.getElementById('cedulaOriginal').value = cedula;
            document.getElementById('identificacion').value = cedula;
            document.getElementById('rolFuncionario').value = rol;
            document.getElementById('primerNombre').value = nombre1;
            document.getElementById('segundoNombre').value = nombre2 || '';
            document.getElementById('primerApellido').value = apellido1;
            document.getElementById('segundoApellido').value = apellido2;
            document.getElementById('tipoVehiculo').value = tipoVehiculo;
            document.getElementById('placa').value = placa || '';
            
            actionInput.value = 'editar'; 
            formTitulo.innerText = 'Editando Funcionario: ' + nombre1 + ' ' + apellido1;
            btnAgregar.style.display = 'none';
            btnEditarForm.style.display = 'inline-block';
            window.scrollTo(0, 0);
        }

        function limpiarYResetearForm() {
            form.reset();
            actionInput.value = 'agregar';
            formTitulo.innerText = 'Registrar Funcionario';
            btnAgregar.style.display = 'inline-block';
            btnEditarForm.style.display = 'none';
        }
        
        document.addEventListener('DOMContentLoaded', function() {
            limpiarYResetearForm();
        });
    </script>
</body>
</html>