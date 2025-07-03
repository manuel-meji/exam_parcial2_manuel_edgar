<%-- prettier-ignore --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // Verificar autenticación
    String tipoUsuario = (String) session.getAttribute("tipoUsuario");
    String nombreAdmin = (String) session.getAttribute("nombreAdmin");
    System.out.println("menuAdministrador.jsp - tipoUsuario: " + tipoUsuario + ", nombreAdmin: " + nombreAdmin);
    if (tipoUsuario == null || !tipoUsuario.equals("Administrador") || nombreAdmin == null) {
        System.out.println("Redirigiendo a login.jsp porque tipoUsuario o nombreAdmin es null");
        response.sendRedirect("login.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Menú Administrador • Gestión de Seguridad CTP UPALA</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="estilos.css">
</head>
<body>
    <div class="admin-menu-container">
        <img src="images/logoCTPU.png" alt="Logo CTP UPALA" class="logo">
        <h1>Gestión Administrativa</h1>
        <h2>Bienvenido, <%= nombreAdmin %></h2>
        <p>Selecciona una opción para continuar:</p>
        <div class="menu-grid">
            <button class="menu-button" onclick="window.location.href='gestionOficiales.jsp'">
                <img src="https://img.icons8.com/?size=100&id=ws0nvjEAlQSt&format=png&color=FFFFFF" alt="Ícono de oficiales" class="menu-icon">
                Mantenimiento de Oficiales
            </button>
            <button class="menu-button" onclick="window.location.href='panelEstudiantes.jsp'">
                <img src="https://img.icons8.com/?size=100&id=9477&format=png&color=FFFFFF" alt="Ícono de estudiantes" class="menu-icon">
                Mantenimiento de Estudiantes
            </button>
            <button class="menu-button" onclick="window.location.href='panelAyuda.jsp'">
                <img src="https://img.icons8.com/?size=100&id=59807&format=png&color=FFFFFF" alt="Ícono de ayuda" class="menu-icon">
                Ayuda
            </button>
            <button class="menu-button" onclick="window.location.href='panelDerechosAutor.jsp'">
                <img src="https://img.icons8.com/?size=100&id=7764&format=png&color=FFFFFF" alt="Ícono de derechos de autor" class="menu-icon">
                Derechos de Autor
            </button>
            <button class="menu-button logout-button" onclick="window.location.href='login.jsp?logout=true'">
                <img src="https://img.icons8.com/?size=100&id=24337&format=png&color=FFFFFF" alt="Ícono de cerrar sesión" class="menu-icon">
                Cerrar Sesión
            </button>
        </div>
    </div>

    <footer class="footer">
        © 2025 Seguridad y Tecnología • Todos los derechos reservados
    </footer>
</body>
</html>