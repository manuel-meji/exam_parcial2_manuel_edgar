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
        <h1 class="logo">Derechos de Autor</h1>
        <nav>
            <ul>
                <li class="active"><a href="#">Derechos de Autor</a></li>
                <li><a href="panelAyuda.jsp">Ayuda</a></li>
                <li><a href="login.jsp?logout=true">Salir</a></li>
            </ul>
        </nav>
    </header>

    <main class="container">
        <h2 class="page-title">Derechos de Autor</h2>
        <p class="intro-text">Este proyecto fue desarrollado como parte de un trabajo académico para el curso de Programación II. A continuación, presentamos a los desarrolladores responsables:</p>
        <div class="developers-grid">
            <!-- Desarrollador 1 -->
            <div class="developer-card">
                <img src="images/ing_mejicano.jpg" alt="Foto de Desarrollador 1" class="developer-photo">
                <h3>Manuel Mejicano Ortiz</h3>
                <p><strong>Correo:</strong> manuel.mejicano@ucr.ac.cr</p>
                <p><strong>Carrera:</strong> Informática Empresarial</p>
                <p><strong>Carné:</strong> C4H071</p>
                <p><strong>Rol:</strong> Desarrollador y Diseñador</p>
            </div>
            <!-- Desarrollador 2 -->
            <div class="developer-card">
                <img src="images/ing_duarte.jpg" alt="Foto de Desarrollador 2" class="developer-photo">
                <h3>Edgar Duarte Alemán</h3>
                <p><strong>Correo:</strong> edgar.duarte@ucr.ac.cr</p>
                <p><strong>Carrera:</strong> Informática Empresarial</p>
                <p><strong>Carné:</strong> C4E773</p>
                <p><strong>Rol:</strong> Desarrollador y QA</p>
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