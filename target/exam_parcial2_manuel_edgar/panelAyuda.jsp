<%-- prettier-ignore --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ayuda • Proyecto</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="estilosPA.css">
</head>
<body>
    <header class="navbar">
        <h1 class="logo">Gestión de Seguridad</h1>
        <nav>
            <ul>
                <li><a href="panelDerechosAutor.jsp">Derechos de Autor</a></li>
                <li class="active"><a href="#">Ayuda</a></li>
                <li><a href="login.jsp?logout=true">Salir</a></li>
            </ul>
        </nav>
    </header>

    <main class="container">
        <h2 class="page-title">Ayuda</h2>
        <p class="intro-text">
            Bienvenido al sistema de <strong>Seguridad</strong> del Colegio Técnico Profesional de Upala. Esta aplicación permite a los oficiales gestionar los ingresos y salidas de estudiantes y funcionarios, así como administrar los registros de usuarios y vehículos. A continuación, encontrarás una guía básica para usar el sistema y un video explicativo.
        </p>

        <div class="help-sections">
            <!-- Introducción al Sistema -->
            <div class="help-card">
                <h3>¿Qué es este sistema?</h3>
                <p>
                    Este sistema está diseñado para facilitar el control de accesos en el CTP UPALA. Los administradores pueden gestionar oficiales y estudiantes, mientras que los oficiales pueden registrar funcionarios, registrar ingresos y aprobar salidas de estudiantes, y consultar registros. Todo está integrado con una base de datos de MySQL.
                </p>
            </div>

            <!-- Instrucciones Básicas -->
            <div class="help-card">
                <h3>Cómo usar el sistema</h3>
                <ul>
                    <li><strong>Iniciar Sesión:</strong> Usa tu nombre de usuario y contraseña en la página de login. Los administradores acceden al panel de gestión, y los oficiales al menu de inicio.</li>
                    <li><strong>Gestionar Oficiales (Admin):</strong> En "Oficiales", agrega, edita o elimina oficiales de seguridad.</li>
                    <li><strong>Gestionar Estudiantes (Admin):</strong> En "Estudiantes", registra o actualiza la información de los estudiantes.</li>
                    <li><strong>Registrar Funcionarios (Oficial):</strong> En "Funcionarios", ingresa los datos de los funcionarios, incluyendo vehículos si aplica.</li>
                    <li><strong>Aprobar Ingresos/Salidas (Oficial):</strong> Usa "Salida de Estudiantes" o "Ingreso Funcionario" para registrar y aprobar movimientos, que se guardan con el ID del oficial.</li>
                </ul>
            </div>

            <!-- Video Explicativo -->
            <div class="help-card video-card">
                <h3>Video Explicativo</h3>
                <p>Aprende cómo usar el sistema con este video introductorio:</p>
                <div class="video-container">
                    <iframe 
                        src="https://www.youtube.com/embed/dQw4w9WgXcQ" 
                        title="Video Explicativo del Sistema" 
                        frameborder="0" 
                        allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture" 
                        allowfullscreen>
                    </iframe>
                </div>
                <p><a href="https://www.youtube.com/watch?v=dQw4w9WgXcQ" target="_blank">Ver en YouTube</a></p>
            </div>

            <!-- Contacto para Soporte -->
            <div class="help-card">
                <h3>Contacto para Soporte</h3>
                <p>Si tienes problemas o necesitas ayuda adicional, contáctanos:</p>
                <p><strong>Correo:</strong> soporte@seguridad.ctpu</p>
                <p><strong>Teléfono:</strong> +506 7028-0576</p>
            </div>
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