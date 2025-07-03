package modelo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

@WebServlet("/ChatServlet")
public class ChatServlet extends HttpServlet {
    private static final String GEMINI_API_KEY = "AIzaSyBhvhVpbSJ8H5kAap-wELrXLhlzyBN8q48";
    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userMessage = request.getParameter("message");
        response.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            // Obtener datos de la base de datos
            String contextoDatos = conectarYObtenerDatos();
            if (contextoDatos == null) {
                out.print("Error al obtener datos de la base de datos.");
                return;
            }

            // Preparar el contexto para Gemini
            String contexto = "Aquí están los datos de ingresos registrados en la base de datos. "
                    + "Hay tres tipos de ingreso: 'Funcionario', 'Externo' y 'VehiculoExterno'. "
                    + "Cada registro contiene ID, cédula, fecha, hora, motivo de ingreso, nombre del guarda que aprobó, "
                    + "y si es un ingreso de vehículo externo o funcionario, también la placa del vehículo. "
                    + "Analiza esta información:\n\n" + contextoDatos + "\n\n"
                    + "Responde a la siguiente pregunta del usuario: " + userMessage;

            // Enviar solicitud a Gemini
            String geminiResponse = sendToGemini(contexto);
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
            return "Error al conectar con Gemini: Código " + responseCode;
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }

            // Extraer el texto de la respuesta (simplificado, asume estructura básica)
            String responseText = response.toString();
            int startIndex = responseText.indexOf("\"text\": \"") + 9;
            int endIndex = responseText.lastIndexOf("\"");
            if (startIndex >= 9 && endIndex > startIndex) {
                return responseText.substring(startIndex, endIndex).replace("\\n", "\n");
            } else {
                return "Error al procesar la respuesta de Gemini.";
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
