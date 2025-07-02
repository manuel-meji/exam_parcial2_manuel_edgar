<%-- ================================================================== --%>
<%-- 1. DIRECTIVAS DE PÁGINA E IMPORTACIONES                        --%>
<%-- ================================================================== --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.sql.*, java.util.*" %>

<%-- ================================================================== --%>
<%-- 2. LÓGICA DEL SERVIDOR (CRUD COMPLETO Y AUTÓNOMO)                --%>
<%-- ================================================================== --%>
<%
    // -----------------------------------------------------------------
    // ¡ATENCIÓN! CONFIGURACIÓN DE LA BASE DE DATOS
    // -----------------------------------------------------------------
    String DB_URL = "jdbc:mysql://localhost:3306/proyecto1?verifyServerCertificate=false&useSSL=true";
    String DB_USER = "root";
    String DB_PASS = "1234"; // ¡TU CONTRASEÑA!
    String DB_DRIVER = "com.mysql.jdbc.Driver";

    // -----------------------------------------------------------------
    // LÓGICA DE ACCIONES AJAX (MANEJO DE AGREGAR/ELIMINAR)
    // -----------------------------------------------------------------
    if ("POST".equalsIgnoreCase(request.getMethod())) {
        response.setContentType("text/plain; charset=UTF-8");
        String action = request.getParameter("action");

        if ("agregar".equals(action)) {
            String sql = "INSERT INTO ingresos (cedula, fecha, hora, nombre_usuario_guarda, tipoIngreso) VALUES (?, ?, ?, ?, ?)";
            try {
                Class.forName(DB_DRIVER);
                try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, request.getParameter("cedula"));
                    pstmt.setDate(2, new java.sql.Date(new java.util.Date().getTime()));
                    pstmt.setTime(3, new java.sql.Time(new java.util.Date().getTime()));
                    pstmt.setString(4, request.getParameter("nombreUsuarioGuarda"));
                    pstmt.setString(5, "Funcionario");
                    pstmt.executeUpdate();
                    out.print("success");
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("Error al agregar: " + e.getMessage());
            }
        }
        else if ("eliminar".equals(action)) {
            String sql = "DELETE FROM ingresos WHERE id = ?";
            try {
                Class.forName(DB_DRIVER);
                try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, Integer.parseInt(request.getParameter("id")));
                    pstmt.executeUpdate();
                    out.print("success");
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("Error al eliminar: " + e.getMessage());
            }
        }
        return;
    }

    // -----------------------------------------------------------------
    // LÓGICA DE CARGA DE DATOS (PARA MOSTRAR LA PÁGINA EN GET)
    // -----------------------------------------------------------------
    List<Map<String, Object>> listaFuncionarios = new ArrayList<>();
    List<Map<String, Object>> listaIngresos = new ArrayList<>();
    String errorDB = null;

    try {
        Class.forName(DB_DRIVER);
        String sqlFuncionarios = "SELECT p.*, v.tipoVehiculo FROM personas p LEFT JOIN vehiculos v ON p.placaVehiculo = v.placa WHERE p.tipoPersona = 'Funcionario'";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sqlFuncionarios)) {
            while (rs.next()) {
                Map<String, Object> f = new HashMap<>();
                f.put("id", rs.getString("cedula"));
                f.put("nombreCompleto", rs.getString("nombre1") + " " + rs.getString("apellido1"));
                f.put("placa", rs.getString("placaVehiculo"));
                listaFuncionarios.add(f);
            }
        }
        String sqlIngresos = "SELECT i.*, p.nombre1, p.apellido1, p.placaVehiculo, v.tipoVehiculo FROM ingresos i LEFT JOIN personas p ON i.cedula = p.cedula LEFT JOIN vehiculos v ON p.placaVehiculo = v.placa WHERE i.tipoIngreso = 'Funcionario' ORDER BY i.id DESC";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sqlIngresos)) {
            while (rs.next()) {
                Map<String, Object> ingreso = new HashMap<>();
                ingreso.put("id", rs.getInt("id"));
                ingreso.put("cedula", rs.getString("cedula"));
                ingreso.put("fecha", rs.getDate("fecha"));
                ingreso.put("hora", rs.getTime("hora"));
                ingreso.put("nombre_usuario_guarda", rs.getString("nombre_usuario_guarda"));
                ingreso.put("placa", rs.getString("placaVehiculo"));
                ingreso.put("nombre_funcionario", rs.getString("nombre1") + " " + rs.getString("apellido1"));
                ingreso.put("tipo_vehiculo", rs.getString("tipoVehiculo"));
                listaIngresos.add(ingreso);
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
    <title>Ingreso de Funcionarios • Oficial</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="estilosPSEO.css">
    
    <script>
        // --- INICIO DEL JAVASCRIPT CORREGIDO ---

        // Variable global para guardar una copia de todas las opciones originales.
        var opcionesOriginales = [];

        // Función que se ejecuta cuando la página ha cargado completamente.
        document.addEventListener('DOMContentLoaded', function() {
            var select = document.getElementById('comboBoxFuncionarios');
            // Guardamos todas las opciones en nuestra variable global.
            for (var i = 0; i < select.options.length; i++) {
                opcionesOriginales.push(select.options[i]);
            }
        });

        function filtrarFuncionarios() {
            var input = document.getElementById('txtBusqueda').value.toLowerCase();
            var select = document.getElementById('comboBoxFuncionarios');
            
            // 1. Limpiamos completamente el select.
            select.innerHTML = '';
            
            // 2. Iteramos sobre nuestra copia maestra de opciones.
            for (var i = 0; i < opcionesOriginales.length; i++) {
                var opcionActual = opcionesOriginales[i];
                
                // Si la opción coincide con la búsqueda (o si la búsqueda está vacía),
                // la volvemos a añadir al select.
                if (opcionActual.text.toLowerCase().includes(input)) {
                    select.appendChild(opcionActual);
                }
            }
        }
        
        // --- FIN DEL JAVASCRIPT CORREGIDO ---


        // Las otras funciones se mantienen igual
        function agregarIngreso() {
            var select = document.getElementById('comboBoxFuncionarios');
            if (!select.value) {
                alert('Seleccione un funcionario.');
                return;
            }
            var xhr = new XMLHttpRequest();
            var url = '<%= request.getContextPath() %>/panelIngresoFuncionarios.jsp';
            xhr.open('POST', url, true);
            xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
            xhr.onreadystatechange = function() {
                if (xhr.readyState === 4) {
                    if (xhr.status === 200 && xhr.responseText.trim() === 'success') {
                        location.reload();
                    } else {
                        alert('Error al agregar: ' + xhr.responseText);
                    }
                }
            };
            var params = 'action=agregar&cedula=' + encodeURIComponent(select.value) + '&nombreUsuarioGuarda=Mexicano';
            xhr.send(params);
        }

        function eliminarIngreso(idIngreso) {
            if (!confirm('¿Estás seguro de que quieres eliminar el ingreso con ID ' + idIngreso + '?')) {
                return;
            }
            var xhr = new XMLHttpRequest();
            var url = '<%= request.getContextPath() %>/panelIngresoFuncionarios.jsp';
            xhr.open('POST', url, true);
            xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
            xhr.onreadystatechange = function() {
                if (xhr.readyState === 4) {
                    if (xhr.status === 200 && xhr.responseText.trim() === 'success') {
                        location.reload();
                    } else {
                        alert('Error al eliminar: ' + xhr.responseText);
                    }
                }
            };
            xhr.send('action=eliminar&id=' + encodeURIComponent(idIngreso));
        }
    </script>
</head>
<body>
    <header class="navbar">
        <h1 class="logo">Gestión de Seguridad</h1>
        <nav>
            <ul>
                <li><a href="<%= request.getContextPath() %>/panelSalidaEstudiante.jsp">Salida de Estudiantes</a></li>
                <li><a href="<%= request.getContextPath() %>/panelFuncionarios.jsp">Funcionarios</a></li>
                <li class="active"><a href="#">Ingreso Funcionario</a></li>
                <li><a href="<%= request.getContextPath() %>/login.jsp">Salir</a></li>
            </ul>
        </nav>
    </header>

    <main class="container">
        <% if (errorDB != null) { %>
            <h1 style="color: red; text-align: center;"><%= errorDB %></h1>
        <% } else { %>
            <h2 class="page-title">Registra los ingresos de los funcionarios</h2>
            <div class="main-content">
                <section class="form-section">
                    <div class="form-inline">
                        <div class="search-group">
                            <label for="txtBusqueda">Busca un funcionario:</label>
                            <input type="text" id="txtBusqueda" placeholder="Cédula, Nombre o Placa…" onkeyup="filtrarFuncionarios()">
                        </div>
                        <div class="form-group">
                            <label for="comboBoxFuncionarios">Funcionario:</label>
                            <select id="comboBoxFuncionarios">
                                <option value="">-- Seleccione un funcionario --</option>
                                <% for (Map<String, Object> f : listaFuncionarios) {
                                    String placa = (String) f.get("placa");
                                    String optionText = f.get("id") + " - " + f.get("nombreCompleto") + " (Placa: " + (placa != null ? placa : "N/A") + ")";
                                %>
                                <option value="<%= f.get("id") %>"><%= optionText %></option>
                                <% } %>
                            </select>
                        </div>
                        <div class="actions-inline">
                            <button class="approve-button" type="button" onclick="agregarIngreso()">Agregar Ingreso</button>
                        </div>
                    </div>
                </section>
                
                <section class="table-section">
                    <table>
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Nombre Funcionario</th>
                                <th>Cédula</th>
                                <th>Tipo Vehículo</th>
                                <th>Placa</th>
                                <th>Fecha</th>
                                <th>Hora</th>
                                <th>Guarda</th>
                                <th>Acción</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% if (listaIngresos.isEmpty()) { %>
                                <tr><td colspan="9" style="text-align:center;">No hay ingresos registrados.</td></tr>
                            <% } else {
                                for (Map<String, Object> ingreso : listaIngresos) { %>
                            <tr>
                                <td><%= ingreso.get("id") %></td>
                                <td><%= ingreso.get("nombre_funcionario") %></td>
                                <td><%= ingreso.get("cedula") %></td>
                                <td><%= ingreso.get("tipo_vehiculo") != null ? ingreso.get("tipo_vehiculo") : "N/A" %></td>
                                <td><%= ingreso.get("placa") != null ? ingreso.get("placa") : "N/A" %></td>
                                <td><%= ingreso.get("fecha") %></td>
                                <td><%= ingreso.get("hora") %></td>
                                <td><%= ingreso.get("nombre_usuario_guarda") %></td>
                                <td>
                                    <button class="delete-button-small" onclick="eliminarIngreso(<%= ingreso.get("id") %>)">Eliminar</button>
                                </td>
                            </tr>
                            <%  }
                            } %>
                        </tbody>
                    </table>
                </section>
            </div>
        <% } %>
    </main>

    <footer class="footer">
        © 2025 Seguridad y Tecnología • Todos los derechos reservados
    </footer>
</body>
</html>