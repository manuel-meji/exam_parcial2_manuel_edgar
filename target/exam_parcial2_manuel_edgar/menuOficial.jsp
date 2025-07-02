<%-- prettier-ignore --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String idOficialActual = (String) session.getAttribute("idOficialActual");
    String tipoUsuario = (String) session.getAttribute("tipoUsuario");
    if (tipoUsuario == null || !tipoUsuario.equals("Guarda")) {
        System.out.println("Acceso denegado: No es un oficial de seguridad. Tipo: " + tipoUsuario );
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Menú Oficial • Gestión de Seguridad</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="estilosMO.css">
</head>
<body>
    <header class="navbar">
        <h1 class="logo">Menú Principal</h1>
        <nav>
            <ul>
                <li class="active"><a href="#">Menú Oficial</a></li>
                <li><a href="panelAyuda.jsp">Ayuda</a></li>
                <li><a href="login.jsp?logout=true">Salir</a></li>
            </ul>
        </nav>
    </header>

    <main class="container">
        <h2 class="page-title">Menú Oficial</h2>
        <p class="intro-text">Bienvenido, <strong><%= idOficialActual %></strong>. Selecciona una opción para continuar:</p>
        <div class="menu-grid">
            <button class="menu-button" onclick="window.location.href='panelFuncionarios.jsp'">
                Gestionar Funcionarios
            </button>
            <button class="menu-button" onclick="window.location.href='panelIngresoFuncionarios.jsp'">
                Ingreso de Funcionarios
            </button>
            <button class="menu-button" onclick="window.location.href='panelSalidaEstudiante.jsp'">
                Salida de Estudiantes
            </button>
            <button class="menu-button" onclick="window.location.href='panelAyuda.jsp'">
                Ayuda
            </button>
            <button class="menu-button" onclick="window.location.href='panelDerechosAutor.jsp'">
                Derechos de Autor
            </button>
            <button class="menu-button logout-button" onclick="window.location.href='login.jsp?logout=true'">
                Cerrar Sesión
            </button>
        </div>
    </main>

    <footer class="footer">
        © 2025 Seguridad y Tecnología • Todos los derechos reservados
    </footer>

    <script>
        document.addEventListener('DOMContentLoaded', function() {
            // Código para manejar dropdowns (por compatibilidad)
            const dropdowns = document.querySelectorAll('.dropdown');
            dropdowns.forEach(dropdown => {
                dropdown.addEventListener('mouseenter', () => {
                    dropdown.querySelector('.dropdown-menu').style.display = 'block';
                });
                dropdown.addEventListener('mouseleave', () => {
                    dropdown.querySelector('.dropdown-menu').style.display = 'none';
                });
            });
        });
    </script>
</body>
</html>