<%-- prettier-ignore --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.sql.*" %>
<%!
    Connection con = null;
    String idOficialActual;

    public String loginOficial(String idAcceso, String contrasena) {
        try {
            String sql = "SELECT * FROM usuarios WHERE nombreUsuario = ? AND tipoUsuario = 'Guarda'";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, idAcceso);
            ResultSet rsMethod = pstmt.executeQuery();
            if (rsMethod.next()) {
                String contrasenaDB = rsMethod.getString("contraseña");
                String nombreUsuarioBD = rsMethod.getString("nombreUsuario");
                System.out.println("Usuario encontrado: " + nombreUsuarioBD + ", contraseña DB: " + contrasenaDB);
                if (contrasenaDB != null && contrasenaDB.equals(contrasena) && nombreUsuarioBD.equals(idAcceso)) {
                    idOficialActual = idAcceso;
                    rsMethod.close();
                    pstmt.close();
                    return idOficialActual;
                }
            } else {
                System.out.println("No se encontró usuario con nombreUsuario: " + idAcceso + " y tipoUsuario: Guarda");
            }
            rsMethod.close();
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("Error al intentar iniciar sesión (Oficial): " + e.getMessage());
        }
        return null;
    }

    public String loginAdmin(String nombreUsuario, String contrasena) {
        try {
            String sql = "SELECT * FROM usuarios WHERE nombreUsuario = ? AND tipoUsuario = 'Administrador'";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, nombreUsuario);
            ResultSet rsMethod = pstmt.executeQuery();
            if (rsMethod.next()) {
                String contrasenaDB = rsMethod.getString("contraseña");
                String nombreUsuarioBD = rsMethod.getString("nombreUsuario");
                System.out.println("Usuario encontrado (Admin): " + nombreUsuarioBD + ", contraseña DB: " + contrasenaDB);
                if (contrasenaDB != null && contrasenaDB.equals(contrasena) && nombreUsuarioBD.equals(nombreUsuario)) {
                    String nombre1 = rsMethod.getString("nombre1");
                    String apellido1 = rsMethod.getString("apellido1");
                    String nombreCompleto = (nombre1 != null ? nombre1 : "") + " " + (apellido1 != null ? apellido1 : "");
                    System.out.println("Nombre completo: " + nombreCompleto);
                    rsMethod.close();
                    pstmt.close();
                    return nombreCompleto.trim();
                } else {
                    System.out.println("Credenciales no válidas para usuario: " + nombreUsuario);
                }
            } else {
                System.out.println("No se encontró usuario con nombreUsuario: " + nombreUsuario + " y tipoUsuario: Administrador");
            }
            rsMethod.close();
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("Error al intentar iniciar sesión (Admin): " + e.getMessage());
        }
        return null;
    }
%>

<%
    String loginStatus = "none";
    String nombreAdmin = null;
    String errorMessage = null;

    if ("POST".equalsIgnoreCase(request.getMethod())) {
        String usuario = request.getParameter("nombre");
        String clave = request.getParameter("clave");
        System.out.println("Intento de login - Usuario: " + usuario + ", Método: POST");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/proyecto1?verifyServerCertificate=false&useSSL=true", "root", "1234");
            System.out.println("Conexión a la base de datos establecida");

            nombreAdmin = loginAdmin(usuario, clave);
            if (nombreAdmin != null && !nombreAdmin.isEmpty()) {
                loginStatus = "admin_success";
                session.setAttribute("tipoUsuario", "Administrador");
                session.setAttribute("nombreAdmin", nombreAdmin);
                System.out.println("Login admin exitoso: " + nombreAdmin);
            } else if (loginOficial(usuario, clave) != null) {
                session.setAttribute("idOficialActual", idOficialActual);
                session.setAttribute("tipoUsuario", "Guarda");
                System.out.println("Login oficial exitoso: idOficialActual=" + idOficialActual + ", tipoUsuario=Guarda");
                response.sendRedirect(request.getContextPath() + "/menuOficial.jsp");
                return;
            } else {
                loginStatus = "login_failed";
                errorMessage = "Usuario o contraseña incorrectos.";
                System.out.println("Login fallido: usuario o contraseña incorrectos");
            }
        } catch (Exception e) {
            e.printStackTrace();
            loginStatus = "login_failed";
            errorMessage = "Ocurrió un error en el sistema. Por favor, intente más tarde.";
            System.out.println("Error en login: " + e.getMessage());
        } finally {
            try {
                if (con != null) con.close();
                System.out.println("Conexión a la base de datos cerrada");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Handle logout
    if ("GET".equalsIgnoreCase(request.getMethod()) && "true".equals(request.getParameter("logout"))) {
        session.invalidate();
        loginStatus = "none";
        System.out.println("Sesión invalidada por logout");
    }
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Inicio de Sesión • Gestión de Seguridad CTP UPALA</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="estilosLogin.css">
</head>
<body>
    <div class="login-container">
        <!-- Sección de información -->
        <div class="info-section">
            <img src="images/logoCTPU.png" alt="Logo CTP UPALA" class="logo">
            <h1>Gestión de Seguridad CTP UPALA</h1>
            <p>
                Bienvenido al sistema de gestión de seguridad del Colegio Técnico Profesional de Upala. Esta plataforma permite a los oficiales y administradores controlar los ingresos y salidas de estudiantes y funcionarios, gestionar registros de usuarios y vehículos, y garantizar la seguridad del campus.
            </p>
            <p>
                <strong>Funcionalidades clave:</strong>
            </p>
            <ul>
                <li>Registro y aprobación de ingresos/salidas.</li>
                <li>Gestión de oficiales y estudiantes (admin).</li>
                <li>Consulta de registros en tiempo real.</li>
            </ul>
        </div>

        <!-- Sección de formulario -->
        <div class="form-section">
            <h2>Iniciar Sesión</h2>
            <% if ("login_failed".equals(loginStatus)) { %>
                <div id="notification" class="notification error"><%= errorMessage %></div>
            <% } %>
            <form action="login.jsp" method="post">
                <div class="form-group">
                    <label for="nombre">Nombre de usuario</label>
                    <div class="input-wrapper">
                        <img src="https://img.icons8.com/?size=100&id=82790&format=png&color=000000" alt="Ícono de usuario" class="input-icon">
                        <input type="text" id="nombre" name="nombre" placeholder="NombreUsuario.12" required>
                    </div>
                </div>
                <div class="form-group">
                    <label for="clave">Contraseña</label>
                    <div class="input-wrapper">
                        <img src="https://img.icons8.com/?size=100&id=10480&format=png&color=000000" alt="Ícono de contraseña" class="input-icon">
                        <input type="password" id="clave" name="clave" placeholder="Usuario123" required>
                    </div>
                </div>
                <div class="form-actions">
                    <button type="submit" class="btn btn-primary">
                        <img src="https://img.icons8.com/?size=100&id=115996&format=png&color=000000" alt="Ícono de iniciar sesión" class="btn-icon">
                        Iniciar Sesión
                    </button>
                    <button type="button" class="btn btn-secondary" onclick="limpiarCampos()">
                        <img src="https://img.icons8.com/?size=100&id=104&format=png&color=000000" alt="Ícono de limpiar" class="btn-icon">
                        Limpiar
                    </button>
                </div>
            </form>
        </div>
    </div>

    <!-- Menú de administrador -->
    <% if ("admin_success".equals(loginStatus)) { %>
        <div class="admin-menu">
            <h2>Bienvenido, <%= nombreAdmin %></h2>
            <p>Selecciona una opción para continuar:</p>
            <div class="menu-grid">
                <button class="menu-button" onclick="window.location.href='gestionOficiales.jsp'">
                    <img src="https://img.icons8.com/?size=100&id=82790&format=png&color=000000" alt="Ícono de oficiales" class="menu-icon">
                    Mantenimiento de Oficiales
                </button>
                <button class="menu-button" onclick="window.location.href='panelEstudiantes.jsp'">
                    <img src="https://img.icons8.com/?size=100&id=11283&format=png&color=000000" alt="Ícono de estudiantes" class="menu-icon">
                    Mantenimiento de Estudiantes
                </button>
                <button class="menu-button" onclick="window.location.href='panelAyuda.jsp'">
                    <img src="https://img.icons8.com/?size=100&id=10810&format=png&color=000000" alt="Ícono de ayuda" class="menu-icon">
                    Ayuda
                </button>
                <button class="menu-button" onclick="window.location.href='panelDerechosAutor.jsp'">
                    <img src="https://img.icons8.com/?size=100&id=9847&format=png&color=000000" alt="Ícono de derechos de autor" class="menu-icon">
                    Derechos de Autor
                </button>
                <button class="menu-button logout-button" onclick="window.location.href='login.jsp?logout=true'">
                    <img src="https://img.icons8.com/?size=100&id=11584&format=png&color=000000" alt="Ícono de cerrar sesión" class="menu-icon">
                    Cerrar Sesión
                </button>
            </div>
        </div>
    <% } %>

    <footer class="footer">
        © 2025 Seguridad y Tecnología • Todos los derechos reservados
    </footer>

    <script>
        function limpiarCampos() {
            document.getElementById("nombre").value = "";
            document.getElementById("clave").value = "";
        }
        document.addEventListener('DOMContentLoaded', function() {
            const notification = document.getElementById('notification');
            if (notification) {
                notification.classList.add('show');
                setTimeout(() => {
                    notification.classList.remove('show');
                    setTimeout(() => { notification.remove(); }, 500);
                }, 3000);
            }
        });
    </script>
</body>
</html>