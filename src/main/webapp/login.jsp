<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.sql.Connection, java.sql.DriverManager, java.sql.ResultSet, java.sql.Statement, java.sql.SQLException" %>

<%!
    /*
      <!-- CAMBIO AQUÍ: Comentario simplificado para no confundir al compilador -->
      DECLARACIONES:
      Aquí van las variables de instancia y los métodos auxiliares.
    */
    Connection con = null;
    Statement stmt = null;
    ResultSet rs = null;
    String idOficialActual;

    public String loginOficial(String idAcceso, String contrasena) {
        try {
            String sql = "SELECT * FROM usuarios WHERE nombreUsuario = '" + idAcceso + "' AND tipoUsuario = 'Guarda'";
            ResultSet rsMethod = stmt.executeQuery(sql);
            if (rsMethod.next()) {
                String contrasenaDB = rsMethod.getString("contraseña");
                String nombreUsuarioBD = rsMethod.getString("nombreUsuario");
                if (contrasenaDB != null && contrasenaDB.equals(contrasena) && nombreUsuarioBD.equals(idAcceso)) {
                    idOficialActual = idAcceso;
                    rsMethod.close(); // Buena práctica: cerrar ResultSet local
                    return idOficialActual; // Retornar el nombre de usuario del oficial
                }
            }
            rsMethod.close(); // Cerrar ResultSet local
        } catch (SQLException e) {
            System.out.println("Error al intentar iniciar sesión (Oficial): " + e.getMessage());
        }
        return null;
    }

    public String loginAdmin(String nombreUsuario, String contrasena) {
        try {
            String sql = "SELECT * FROM usuarios WHERE nombreUsuario = '" + nombreUsuario + "' AND tipoUsuario = 'Administrador'";
            ResultSet rsMethod = stmt.executeQuery(sql);
            if (rsMethod.next()) {
                String contrasenaDB = rsMethod.getString("contraseña");
                String nombreUsuarioBD = rsMethod.getString("nombreUsuario");
                if (contrasenaDB != null && contrasenaDB.equals(contrasena) && nombreUsuarioBD.equals(nombreUsuario)) {
                    String nombreCompleto = rsMethod.getString("nombre1") + " " + rsMethod.getString("apellido1");
                    rsMethod.close(); // Buena práctica: cerrar ResultSet local
                    return nombreCompleto;
                }
            }
            rsMethod.close(); // Buena práctica: cerrar ResultSet local
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
%>

<%
    /*
     
      SCRIPTLET PRINCIPAL:
      Aquí va la lógica que estaba en el método doPost.
    */
    String loginStatus = "none";
    String nombreAdmin = null;
    String errorMessage = null;

    if ("POST".equalsIgnoreCase(request.getMethod())) {
        String usuario = request.getParameter("nombre");
        String clave = request.getParameter("clave");

        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/proyecto1?verifyServerCertificate=false&useSSL=true", "root", "1234");
            stmt = con.createStatement();

            nombreAdmin = loginAdmin(usuario, clave);
            if (nombreAdmin != null) {
                loginStatus = "admin_success";
            } else if (loginOficial(usuario, clave) != null) {
                response.sendRedirect(request.getContextPath() + "/panelSalidaEstudiante.jsp");
                return;
            } else {
                loginStatus = "login_failed";
                errorMessage = "Usuario o contraseña incorrectos.";
            }

        } catch (Exception e) {
            e.printStackTrace();
            loginStatus = "login_failed";
            errorMessage = "Ocurrió un error en el sistema. Por favor, intente más tarde.";
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <% if ("admin_success".equals(loginStatus)) { %>
        <title>Menú Principal</title>
        <style>
            body { margin: 0; padding: 0; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; height: 100vh; display: flex; justify-content: center; align-items: center; background: url('jack-b-8Wqm1W59Baw-unsplash.jpg') no-repeat center center/cover; position: relative; }
            body::before { content: ''; position: absolute; top: 0; left: 0; height: 100%; width: 100%; background-color: rgba(0, 0, 0, 0.5); z-index: 0; }
            .menu-container { position: relative; z-index: 1; backdrop-filter: blur(12px); background-color: rgba(255, 255, 255, 0.15); padding: 40px 60px; border-radius: 20px; box-shadow: 0 12px 25px rgba(0, 0, 0, 0.3); text-align: center; border: 1px solid rgba(255, 255, 255, 0.3); color: white; }
            .menu-container h1 { font-size: 32px; margin-bottom: 10px; }
            .menu-container p { font-size: 18px; margin-bottom: 30px; }
            .menu-container button { background: linear-gradient(to right, #0066ff, #00c3ff); color: white; border: none; padding: 15px 30px; margin: 15px 0; font-size: 18px; font-weight: bold; border-radius: 12px; cursor: pointer; transition: background 0.3s ease, transform 0.2s ease; width: 100%; box-shadow: 0 6px 10px rgba(0, 0, 0, 0.15); }
            .menu-container button:hover { background: linear-gradient(to right, #0052cc, #00aaff); transform: scale(1.05); }
        </style>
    <% } else { %>
        <title>Inicio de sesión</title>
        <link rel="stylesheet" href="estilosLogin.css">
    <% } %>
</head>
<body>

<% if ("admin_success".equals(loginStatus)) { %>
    <div class="menu-container">
        <h1>Bienvenido</h1>
        <p>Hola, <span id="nombreUsuario"><%= nombreAdmin %></span></p>
        <button type="button" onclick="window.location.href = 'gestionOficiales'">
            Mantenimiento de Oficiales
        </button>
        <button type="button" onclick="window.location.href='panelEstudiantes.jsp'">
             Mantenimiento de Estudiantes
        </button>
    </div>
<% } else { %>
    <form action="login.jsp" method="post">
        <label for="" align="center" style="font-size: 20px;">Inicio de Sesión</label><br>
        <% if ("login_failed".equals(loginStatus)) { %>
            <p style="color: red; background-color: white; padding: 5px; border-radius: 5px; text-align: center;"><%= errorMessage %></p>
        <% } %>
        <label for="nombre">Nombre de usuario:</label>
        <input type="text" id="nombre" placeholder="NombreUsuario.12" name="nombre" required>
        <label for="clave">Contraseña:</label>
        <input type="password" placeholder="Usuario123" id="clave" name="clave" required>
        <input type="submit" value="Iniciar sesión" name="iniciar">
        <input type="button" value="Limpiar" onclick="limpiarCampos()">
    </form>
    <script>
        function limpiarCampos() {
            document.getElementById("nombre").value = "";
            document.getElementById("clave").value = "";
        }
    </script>
<% } %>

</body>
</html>