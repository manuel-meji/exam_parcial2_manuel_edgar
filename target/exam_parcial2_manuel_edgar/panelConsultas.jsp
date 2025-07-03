<%-- prettier-ignore --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, java.util.ArrayList" %>
<%
    // Verificar autenticación
    String tipoUsuario = (String) session.getAttribute("tipoUsuario");
    String nombreAdmin = (String) session.getAttribute("nombreAdmin");
    if (tipoUsuario == null || !tipoUsuario.equals("Administrador") || nombreAdmin == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Panel de Consultas • Admin</title>
    <link rel="stylesheet" href="estilosPC.css">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600&display=swap" rel="stylesheet">
</head>
<body>
    <header class="navbar">
        <h1 class="logo">Administración</h1>
        <nav>
            <ul>
                <li><a href="menuAdministrador.jsp">Menú</a></li>
                <li><a href="login.jsp">Salir</a></li>
            </ul>
        </nav>
    </header>

    <main class="container">
        <div class="main-content">
            <section class="chat-section">
                <h3>Chat con IA (Gemini)</h3>
                <div id="chat-messages" class="chat-messages"></div>
                <div class="chat-input">
                    <input type="text" id="user-input" placeholder="Escribe tu pregunta sobre ingresos..." required>
                    <div class="footer-buttons">
                        <button type="button" class="add-button" onclick="sendMessage()">
                            <img src="https://img.icons8.com/?size=100&id=UkLBG0sZoWV0&format=png&color=FFFFFF" alt="Enviar Icono" style="width: 20px; height: 20px;">
                            Enviar
                        </button>
                        <button type="button" class="clear-button" onclick="clearChat()">
                            <img src="https://img.icons8.com/?size=100&id=8068&format=png&color=FFFFFF" alt="Limpiar Icono" style="width: 20px; height: 20px;">
                            Limpiar
                        </button>
                    </div>
                </div>
            </section>
        </div>
    </main>

    <footer class="footer">
        © 2025 Seguridad y Tecnología • Todos los derechos reservados
    </footer>

    <script>
        async function sendMessage() {
            const input = document.getElementById('user-input');
            const message = input.value.trim();
            if (!message) return;

            const chatMessages = document.getElementById('chat-messages');
            const notification = document.getElementById('notification');

            // Añadir mensaje del usuario
            const userMessage = document.createElement('div');
            userMessage.className = 'message user';
            userMessage.textContent = message;
            chatMessages.appendChild(userMessage);

            // Enviar mensaje al servlet
            try {
                const response = await fetch('ChatServlet', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                    body: `message=${encodeURIComponent(message)}`
                });
                const data = await response.text();

                // Añadir respuesta de Gemini
                const botMessage = document.createElement('div');
                botMessage.className = 'message bot';
                botMessage.textContent = data;
                chatMessages.appendChild(botMessage);

                if (notification) {
                    notification.classList.remove('show');
                    notification.remove();
                }
            } catch (error) {
                const errorMessage = document.createElement('div');
                errorMessage.className = 'message error';
                errorMessage.textContent = 'Error al conectar con la IA. Intenta de nuevo.';
                chatMessages.appendChild(errorMessage);

                const errorNotification = document.createElement('div');
                errorNotification.id = 'notification';
                errorNotification.className = 'notification error';
                errorNotification.textContent = 'Error al conectar con la IA.';
                document.body.appendChild(errorNotification);
                errorNotification.classList.add('show');
                setTimeout(() => {
                    errorNotification.classList.remove('show');
                    setTimeout(() => errorNotification.remove(), 500);
                }, 3000);
            }

            input.value = '';
            chatMessages.scrollTop = chatMessages.scrollHeight;
        }

        function clearChat() {
            document.getElementById('chat-messages').innerHTML = '';
            document.getElementById('user-input').value = '';
            const notification = document.getElementById('notification');
            if (notification) {
                notification.classList.remove('show');
                notification.remove();
            }
        }

        document.getElementById('user-input').addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                sendMessage();
            }
        });

        document.addEventListener('DOMContentLoaded', function() {
            const notification = document.getElementById('notification');
            if (notification) {
                notification.classList.add('show');
                setTimeout(() => {
                    notification.classList.remove('show');
                    setTimeout(() => notification.remove(), 500);
                }, 3000);
            }
        });
    </script>
</body>
</html>