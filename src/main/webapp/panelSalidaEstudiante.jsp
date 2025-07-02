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
    // LÓGICA DE ACCIONES AJAX (Aprobar/Eliminar Salida)
    // -----------------------------------------------------------------
    if ("POST".equalsIgnoreCase(request.getMethod())) {
        response.setContentType("text/plain; charset=UTF-8");
        String action = request.getParameter("action");

        if ("aprobar".equals(action)) {
            String sql = "INSERT INTO salidas_estudiantes (carnet, fecha, hora, motivo, nombre_usuario_guarda) VALUES (?, ?, ?, ?, ?)";
            try {
                Class.forName(DB_DRIVER);
                try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, request.getParameter("carnet"));
                    pstmt.setDate(2, new java.sql.Date(new java.util.Date().getTime()));
                    pstmt.setTime(3, new java.sql.Time(new java.util.Date().getTime()));
                    pstmt.setString(4, request.getParameter("motivo"));
                    pstmt.setString(5, request.getParameter("nombreUsuarioGuarda"));
                    pstmt.executeUpdate();
                    out.print("success");
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print("Error al aprobar salida: " + e.getMessage());
            }
        }
        else if ("eliminar".equals(action)) {
            String sql = "DELETE FROM salidas_estudiantes WHERE id = ?";
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
                out.print("Error al eliminar salida: " + e.getMessage());
            }
        }
        return;
    }

    // -----------------------------------------------------------------
    // LÓGICA DE CARGA DE DATOS (PARA MOSTRAR LA PÁGINA EN GET)
    // -----------------------------------------------------------------
    List<Map<String, Object>> listaEstudiantes = new ArrayList<>();
    List<Map<String, Object>> listaSalidas = new ArrayList<>();
    String errorDB = null;

    try {
        Class.forName(DB_DRIVER);
        
        // *** CAMBIO CRÍTICO: Se consulta la tabla 'estudiantes' ***
        String sqlEstudiantes = "SELECT carnet, nombre1, apellido1 FROM estudiantes ORDER BY apellido1, nombre1";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sqlEstudiantes)) {
            while (rs.next()) {
                Map<String, Object> estudiante = new HashMap<>();
                estudiante.put("carnet", rs.getString("carnet"));
                estudiante.put("nombre1", rs.getString("nombre1"));
                estudiante.put("apellido1", rs.getString("apellido1"));
                listaEstudiantes.add(estudiante);
            }
        }

        // *** CAMBIO CRÍTICO: El JOIN ahora se hace con la tabla 'estudiantes' (alias 'e') ***
        String sqlSalidas = "SELECT s.*, e.nombre1, e.apellido1 " +
                            "FROM salidas_estudiantes s " +
                            "LEFT JOIN estudiantes e ON s.carnet = e.carnet " +
                            "ORDER BY s.id DESC";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sqlSalidas)) {
            while (rs.next()) {
                Map<String, Object> salida = new HashMap<>();
                salida.put("id", rs.getInt("id"));
                salida.put("carnet", rs.getString("carnet"));
                salida.put("motivo", rs.getString("motivo"));
                salida.put("fecha", rs.getDate("fecha"));
                salida.put("hora", rs.getTime("hora"));
                salida.put("nombre_usuario_guarda", rs.getString("nombre_usuario_guarda"));
                // Obtenemos el nombre del estudiante desde la tabla 'estudiantes'
                salida.put("nombre_estudiante", rs.getString("nombre1") + " " + rs.getString("apellido1"));
                listaSalidas.add(salida);
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
    <title>Salida de Estudiantes • Oficial</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="estilosPSEO.css">
    
    <script>
        var opcionesOriginales = [];
        document.addEventListener('DOMContentLoaded', function() {
            var select = document.getElementById('selectEstudiante');
            for (var i = 0; i < select.options.length; i++) {
                opcionesOriginales.push(select.options[i]);
            }
        });

        function filtrarEstudiantes() {
            var input = document.getElementById('buscarEstudiante').value.toLowerCase();
            var select = document.getElementById('selectEstudiante');
            select.innerHTML = '';
            for (var i = 0; i < opcionesOriginales.length; i++) {
                var opcionActual = opcionesOriginales[i];
                if (opcionActual.text.toLowerCase().includes(input)) {
                    select.appendChild(opcionActual);
                }
            }
        }

        function aprobarSalida() {
            var selectEstudiante = document.getElementById('selectEstudiante');
            var motivo = document.getElementById('motivoSalida').value;
            if (!selectEstudiante.value || !motivo) {
                alert('Debe seleccionar un estudiante y un motivo de salida.');
                return;
            }
            var xhr = new XMLHttpRequest();
            var url = '<%= request.getContextPath() %>/panelSalidaEstudiante.jsp';
            xhr.open('POST', url, true);
            xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
            xhr.onreadystatechange = function() {
                if (xhr.readyState === 4) {
                    if (xhr.status === 200 && xhr.responseText.trim() === 'success') {
                        location.reload();
                    } else {
                        alert('Error al aprobar salida: ' + xhr.responseText);
                    }
                }
            };
            var params = 'action=aprobar&carnet=' + encodeURIComponent(selectEstudiante.value) + 
                         '&motivo=' + encodeURIComponent(motivo) +
                         '&nombreUsuarioGuarda=Mexicano';
            xhr.send(params);
        }

        function eliminarSalida(idSalida) {
            if (!confirm('¿Estás seguro de que quieres eliminar la salida con ID ' + idSalida + '?')) {
                return;
            }
            var xhr = new XMLHttpRequest();
            var url = '<%= request.getContextPath() %>/panelSalidaEstudiante.jsp';
            xhr.open('POST', url, true);
            xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
            xhr.onreadystatechange = function() {
                if (xhr.readyState === 4) {
                    if (xhr.status === 200 && xhr.responseText.trim() === 'success') {
                        location.reload();
                    } else {
                        alert('Error al eliminar salida: ' + xhr.responseText);
                    }
                }
            };
            xhr.send('action=eliminar&id=' + encodeURIComponent(idSalida));
        }
    </script>
</head>
<body>
    <header class="navbar">
        <h1 class="logo">Gestión de Seguridad</h1>
        <nav>
            <ul>
                <li class="active"><a href="#">Salida de Estudiantes</a></li>
                <li><a href="<%= request.getContextPath() %>/panelFuncionarios.jsp">Funcionarios</a></li>
                <li><a href="<%= request.getContextPath() %>/panelIngresoFuncionarios.jsp">Ingreso Funcionario</a></li>
                <li><a href="<%= request.getContextPath() %>/login.jsp">Salir</a></li>
            </ul>
        </nav>
    </header>

    <main class="container">
        <% if (errorDB != null) { %>
            <h1 style="color: red; text-align: center;"><%= errorDB %></h1>
        <% } else { %>
            <h2 class="page-title">Registra las salidas de los estudiantes del CTP UPALA</h2>
            <div class="main-content">
                <section class="form-section">
                    <div class="form-inline">
                        <div class="search-group">
                            <label for="buscarEstudiante">Buscar estudiante:</label>
                            <input type="text" id="buscarEstudiante" placeholder="Nombre o ID…" onkeyup="filtrarEstudiantes()">
                        </div>
                        <div class="form-group">
                            <label for="selectEstudiante">Seleccione el estudiante:</label>
                            <select id="selectEstudiante">
                                <option value="">-- vacio --</option>
                                <% for (Map<String, Object> estudiante : listaEstudiantes) {
                                    String nombreCompleto = (String) estudiante.get("nombre1") + " " + (String) estudiante.get("apellido1");
                                    String optionText = (String) estudiante.get("carnet") + " - " + nombreCompleto;
                                %>
                                <option value="<%= estudiante.get("carnet") %>"><%= optionText %></option>
                                <% } %>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="motivoSalida">Motivo de salida:</label>
                            <select id="motivoSalida">
                                <option value="">-- seleccione --</option>
                                <option>Cita médica o dental</option>
                                <option>Problemas de salud</option>
                                <option>Llamado de padres o encargados</option>
                                <option>Fallecimiento de familiar</option>
                                <option>Almuerzo</option>
                                <option>Tarde libre</option>
                                <option>Ausencia de profesor</option>
                                <option>Otro</option>
                            </select>
                        </div>
                        <div class="actions-inline">
                            <button class="approve-button" type="button" onclick="aprobarSalida()">
                            <img src="https://img.icons8.com/?size=100&id=HskoXdFIpCjN&format=png&color=FFFFFF" alt="Aprobar Salida Icono" style="width: 20px; height: 20px; vertical-align: middle;">
                            Aprobar salida</button>
                            <button class="delete-button" type="button" onclick="eliminarSalida(prompt('Ingrese el ID de la salida a eliminar:'))">
                            <img src="https://img.icons8.com/?size=100&id=99950&format=png&color=FFFFFF" alt="Eliminar Salida Icono" style="width: 20px; height: 20px; vertical-align: middle;">
                            Eliminar Salida</button>
                        </div>
                    </div>
                </section>
                
                <section class="table-section">
                    <table>
                        <thead>
                            <tr>
                                <th>ID Salida</th>
                                <th>Nombre Estudiante</th>
                                <th>Carnet</th>
                                <th>Motivo</th>
                                <th>Fecha</th>
                                <th>Hora</th>
                                <th>Guarda</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% if (listaSalidas.isEmpty()) { %>
                                <tr><td colspan="7" style="text-align:center;">No hay salidas registradas.</td></tr>
                            <% } else {
                                for (Map<String, Object> salida : listaSalidas) { %>
                            <tr>
                                <td><%= salida.get("id") %></td>
                                <td><%= salida.get("nombre_estudiante") %></td>
                                <td><%= salida.get("carnet") %></td>
                                <td><%= salida.get("motivo") %></td>
                                <td><%= salida.get("fecha") %></td>
                                <td><%= salida.get("hora") %></td>
                                <td><%= salida.get("nombre_usuario_guarda") %></td>
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