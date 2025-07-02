<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Derechos de Autor • Proyecto</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="estilosPDA.css">
</head>
<body>
    <header class="navbar">
        <h1 class="logo">Gestión de Seguridad</h1>
        <nav>
            <ul>
                <li><a href="panelSalidaEstudiante.jsp">Salida de Estudiantes</a></li>
                <li><a href="panelFuncionarios.jsp">Funcionarios</a></li>
                <li><a href="panelIngresoFuncionarios.jsp">Ingreso Funcionario</a></li>
                <li><a href="gestionOficiales.jsp">Oficiales</a></li>
                <li><a href="panelEstudiantes.jsp">Estudiantes</a></li>
                <li class="active"><a href="#">Derechos de Autor</a></li>
                <li><a href="login.jsp?logout=true">Salir</a></li>
            </ul>
        </nav>
    </header>

    <main class="container">
        <h2 class="page-title">Derechos de Autor</h2>
        <p class="intro-text">Este proyecto fue desarrollado como parte de un trabajo académico para el curso de Programación Avanzada. A continuación, presentamos a los desarrolladores responsables:</p>
        <div class="developers-grid">
            <!-- Desarrollador 1 -->
            <div class="developer-card">
                <img src="images/developer1.jpg" alt="Foto de Desarrollador 1" class="developer-photo">
                <h3>Juan Pérez Gómez</h3>
                <p><strong>Correo:</strong> juan.perez@estudiante.edu</p>
                <p><strong>Carrera:</strong> Ingeniería en Sistemas</p>
                <p><strong>Carné:</strong> 202100123</p>
                <p><strong>Rol:</strong> Desarrollador principal, encargado del diseño de la base de datos y la lógica del backend.</p>
            </div>
            <!-- Desarrollador 2 -->
            <div class="developer-card">
                <img src="images/developer2.jpg" alt="Foto de Desarrollador 2" class="developer-photo">
                <h3>María López Vargas</h3>
                <p><strong>Correo:</strong> maria.lopez@estudiante.edu</p>
                <p><strong>Carrera:</strong> Ingeniería en Computación</p>
                <p><strong>Carné:</strong> 202100456</p>
                <p><strong>Rol:</strong> Diseñadora de la interfaz de usuario y responsable de la experiencia visual del sistema.</p>
            </div>
        </div>
    </main>

    <footer class="footer">
        © 2025 Seguridad y Tecnología • Todos los derechos reservados
    </footer>

    <script>
        document.addEventListener('DOMContentLoaded', function() {
            // Código para manejar dropdown si es necesario (copiado de otras páginas)
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