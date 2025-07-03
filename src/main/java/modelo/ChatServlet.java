package modelo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/ChatServlet")
public class ChatServlet extends HttpServlet {
    private static final String GEMINI_API_KEY = "AIzaSyBhvhVpbSJ8H5kAap-wELrXLhlzyBN8q48";
    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String userMessage = request.getParameter("message");
        response.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            // Obtener o inicializar el historial de chat
            List<String> chatHistory = (List<String>) session.getAttribute("chatHistory");
            if (chatHistory == null) {
                chatHistory = new ArrayList<>();
                session.setAttribute("chatHistory", chatHistory);
            }

            // Obtener datos de la base de datos
            String contextoDatos = conectarYObtenerDatos();
            if (contextoDatos == null) {
                out.print("Error al obtener datos de la base de datos.");
                return;
            }

            

            // Preparar el contexto inicial con instrucción explícita
            String contextoInicial = "Aquí están los datos de ingresos registrados en la base de datos. " +
                "Hay tres tipos de ingreso: 'Funcionario', 'Externo' y 'VehiculoExterno'. " +
                "Cada registro contiene ID, cédula, fecha, hora, motivo de ingreso, nombre del guarda que aprobó, " +
                "y si es un ingreso de vehículo externo o funcionario, también la placa del vehículo. " +
                "Analiza esta información:\n" +
                "Datos:\n" + contextoDatos;

            // Construir el mensaje completo con el historial
            StringBuilder contexto = new StringBuilder();
            if (chatHistory.isEmpty()) {
                contexto.append(contextoInicial).append("\n\n");
            }
            for (String prevMessage : chatHistory) {
                contexto.append(prevMessage).append("\n");
            }
            contexto.append("Usuario: ").append(userMessage);

            // Enviar solicitud a Gemini
            String geminiResponse = sendToGemini(contexto.toString());
            chatHistory.add("Usuario: " + userMessage);
            chatHistory.add("Gemini: " + geminiResponse);
            out.print(geminiResponse);
        } catch (Exception e) {
            e.printStackTrace();
            out.print("Error al procesar la solicitud: " + e.getMessage());
        } finally {
            out.close();
        }
    }

    private String conectarYObtenerDatos() {
        Connection con = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/proyecto1?verifyServerCertificate=false&useSSL=true", "root", "1234");
            System.out.println("✅ Conexión exitosa a la base de datos");

            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM ingresos");
            ResultSet rs = pstmt.executeQuery();

            StringBuilder datosFormateados = new StringBuilder();
            while (rs.next()) {
                String texto = String.format("ID: %d | Cédula: %s | Fecha: %s | Hora: %s | Motivo: %s | Guarda: %s | Placa: %s | Tipo: %s",
                    rs.getInt("id"),
                    rs.getString("cedula"),
                    rs.getDate("fecha"),
                    rs.getTime("hora"),
                    rs.getString("motivo") != null ? rs.getString("motivo") : "N/A",
                    rs.getString("nombre_usuario_guarda"),
                    rs.getString("placa_vehiculo") != null ? rs.getString("placa_vehiculo") : "N/A",
                    rs.getString("tipoIngreso"));
                datosFormateados.append(texto).append("\n");
            }

            rs.close();
            pstmt.close();
            return datosFormateados.toString();
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("❌ Error al conectar a la base de datos: " + e.getMessage());
            return null;
        } finally {
            try {
                if (con != null) con.close();
            } catch (SQLException e) {
                System.out.println("❌ Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }

    private String sendToGemini(String contexto) throws IOException {
        URL url = new URL(GEMINI_API_URL + "?key=" + GEMINI_API_KEY);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        String jsonInputString = "{"
            + "\"contents\": [{"
            + "\"parts\": [{"
            + "\"text\": \"" + escapeJson(contexto) + "\""
            + "}]"
            + "}]"
            + "}";

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8))) {
                StringBuilder errorResponse = new StringBuilder();
                String errorLine;
                while ((errorLine = br.readLine()) != null) {
                    errorResponse.append(errorLine.trim());
                }
                System.out.println("❌ Error de Gemini API: " + errorResponse.toString());
                return "Error al conectar con Gemini: Código " + responseCode;
            }
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }

            String responseText = response.toString();
            int startIndex = responseText.indexOf("\"text\": \"") + 9;
            int endIndex = responseText.indexOf("\"", startIndex);
            if (startIndex >= 9 && endIndex > startIndex) {
                String cleanText = responseText.substring(startIndex, endIndex).replace("\\n", "\n").trim();
                return cleanText.isEmpty() ? "No se pudo procesar la respuesta." : cleanText;
            } else {
                return "No se pudo extraer la respuesta.";
            }
        } finally {
            conn.disconnect();
        }
    }

    private String escapeJson(String text) {
        return text.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace("\n", "\\n")
                   .replace("\r", "\\r")
                   .replace("\t", "\\t");
    }
}