<%-- prettier-ignore --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // Verificar autenticación
    String tipoUsuario = (String) session.getAttribute("tipoUsuario");
    String nombreAdmin = (String) session.getAttribute("nombreAdmin");
    System.out.println("panelConsultas.jsp - tipoUsuario: " + tipoUsuario + ", nombreAdmin: " + nombreAdmin);
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
    <title>Panel de Consultas • Gestión de Seguridad CTP UPALA</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="estilos.css">
</head>
<body>
    <div class="chat-container">
        <img src="images/logoCTPU.png" alt="Logo CTP UPALA" class="logo">
        <h1>Gestión de Seguridad CTP UPALA</h1>
        <h2>Bienvenido, <%= nombreAdmin %></h2>
        <p>Consulta los datos de ingresos con nuestra inteligencia artificial.</p>
        <div class="chat-box">
            <div id="chat-messages" class="chat-messages"></div>
            <div class="chat-input">
                <input type="text" id="user-input" placeholder="Escribe tu pregunta..." required>
                <div class="chat-actions">
                    <button class="btn btn-primary" onclick="sendMessage()">
                        <img src="https://img.icons8.com/?size=100&id=53386&format=png&color=000000" alt="Ícono de enviar" class="btn-icon">
                        Enviar
                    </button>
                    <button class="btn btn-secondary" onclick="clearChat()">
                        <img src="https://img.icons8.com/?size=100&id=104&format=png&color=000000" alt="Ícono de limpiar" class="btn-icon">
                        Limpiar
                    </button>
                </div>
            </div>
        </div>
        <button class="btn btn-back" onclick="window.location.href='menuAdministrador.jsp'">
            <img src="https://img.icons8.com/?size=100&id=39944&format=png&color=000000" alt="Ícono de volver" class="btn-icon">
            Volver al Menú
        </button>
    </div>

    <footer class="footer">
        © 2025 Seguridad y Tecnología • Todos los derechos reservados
    </footer>

    <script>
        async function sendMessage() {
            const input = document.getElementById('user-input');
            const message = input.value.trim();
            if (!message) return;

            const chatMessages = document.getElementById('chat-messages');
            
            // Añadir mensaje del usuario
            const userMessage = document.createElement('div');
            userMessage.className = 'message user';
            userMessage.innerHTML = `<span>${message}</span>`;
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
                botMessage.innerHTML = `<span>${data}</span>`;
                chatMessages.appendChild(botMessage);
            } catch (error) {
                const errorMessage = document.createElement('div');
                errorMessage.className = 'message bot error';
                errorMessage.innerHTML = `<span>Error al conectar con la IA. Intenta de nuevo.</span>`;
                chatMessages.appendChild(errorMessage);
            }

            // Limpiar input y desplazar al final
            input.value = '';
            chatMessages.scrollTop = chatMessages.scrollHeight;
        }

        function clearChat() {
            document.getElementById('chat-messages').innerHTML = '';
            document.getElementById('user-input').value = '';
        }

        // Enviar mensaje con la tecla Enter
        document.getElementById('user-input').addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                sendMessage();
            }
        });
    </script>
</body>
</html>