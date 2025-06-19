import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/guardarEstudiante")
public class GuardarEstudianteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        // Retrieve form parameters
        String primerNombre = request.getParameter("primerNombre");
        String segundoNombre = request.getParameter("segundoNombre");
        String primerApellido = request.getParameter("primerApellido");
        String segundoApellido = request.getParameter("segundoApellido");
        String identificacion = request.getParameter("identificacion");
        String fechaNacimiento = request.getParameter("fechaNacimiento");
        String carnet = request.getParameter("carnet");
        String nacionalidad = request.getParameter("nacionalidad");
        String distrito = request.getParameter("direccion");

        // Validate required fields
        StringBuilder errorMessage = new StringBuilder();
        if (isEmpty(primerNombre)) errorMessage.append("Primer Nombre es requerido.<br>");
        if (isEmpty(primerApellido)) errorMessage.append("Primer Apellido es requerido.<br>");
        if (isEmpty(segundoApellido)) errorMessage.append("Segundo Apellido es requerido.<br>");
        if (isEmpty(identificacion)) errorMessage.append("Número de identificación es requerido.<br>");
        if (isEmpty(fechaNacimiento)) errorMessage.append("Fecha de nacimiento es requerida.<br>");
        if (isEmpty(carnet)) errorMessage.append("Carnet estudiantil es requerido.<br>");
        if (isEmpty(nacionalidad)) errorMessage.append("Nacionalidad es requerida.<br>");
        if (isEmpty(distrito)) errorMessage.append("Distrito es requerido.<br>");

        // Validate date format
        java.sql.Date sqlFechaNacimiento = null;
        if (!isEmpty(fechaNacimiento)) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                sdf.setLenient(false); // Strict parsing
                java.util.Date parsedDate = sdf.parse(fechaNacimiento);
                sqlFechaNacimiento = new java.sql.Date(parsedDate.getTime());
            } catch (ParseException e) {
                errorMessage.append("Fecha de nacimiento debe estar en formato AAAA-MM-DD.<br>");
            }
        }

        if (errorMessage.length() > 0) {
            sendErrorResponse(out, errorMessage.toString());
            return;
        }

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            // Load MySQL driver
            Class.forName("com.mysql.jdbc.Driver");
            // Establish database connection
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/proyecto1?verifyServerCertificate=false&useSSL=true", "root", "1234");

            // Prepare SQL insert query
            String sql = "INSERT INTO estudiantes (nombre1, nombre2, apellido1, apellido2, cedula, fechaNacimiento, carnet, nacionalidad, direccion) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, primerNombre);
            pstmt.setString(2, segundoNombre != null ? segundoNombre : "");
            pstmt.setString(3, primerApellido);
            pstmt.setString(4, segundoApellido);
            pstmt.setString(5, identificacion);
            pstmt.setDate(6, sqlFechaNacimiento); // Use java.sql.Date
            pstmt.setString(7, carnet);
            pstmt.setString(8, nacionalidad);
            pstmt.setString(9, distrito);

            // Execute the insert query
            int rowsAffected = pstmt.executeUpdate();

            // Generate HTML response for success
            sendSuccessResponse(out, rowsAffected > 0 ? "Estudiante agregado correctamente." : "Error al agregar estudiante.");

        } catch (SQLException e) {
            sendErrorResponse(out, "Error al agregar estudiante: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            sendErrorResponse(out, "Error inesperado al agregar estudiante: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    private void sendSuccessResponse(PrintWriter out, String message) {
        out.println("<!DOCTYPE html>");
        out.println("<html lang=\"es\">");
        out.println("<head>");
        out.println("  <meta charset=\"UTF-8\" />");
        out.println("  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"/>");
        out.println("  <title>Resultado de Guardado</title>");
        out.println("  <link rel=\"stylesheet\" href=\"estilosPEA.css\" />");
        out.println("  <link href=\"https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600&display=swap\" rel=\"stylesheet\">");
        out.println("  <style>");
        out.println("    .message-container {");
        out.println("      display: flex;");
        out.println("      justify-content: center;");
        out.println("      align-items: center;");
        out.println("      height: 100vh;");
        out.println("      background-color: #f0f2f5;");
        out.println("    }");
        out.println("    .message-box {");
        out.println("      background-color: white;");
        out.println("      padding: 20px 40px;");
        out.println("      border-radius: 10px;");
        out.println("      box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);");
        out.println("      text-align: center;");
        out.println("    }");
        out.println("    .message-box h3 {");
        out.println("      color: #28a745;");
        out.println("      margin-bottom: 20px;");
        out.println("    }");
        out.println("    .message-box a {");
        out.println("      display: inline-block;");
        out.println("      padding: 10px 20px;");
        out.println("      background-color: #007bff;");
        out.println("      color: white;");
        out.println("      text-decoration: none;");
        out.println("      border-radius: 5px;");
        out.println("      margin-top: 10px;");
        out.println("    }");
        out.println("  </style>");
        out.println("</head>");
        out.println("<body>");
        out.println("  <div class=\"message-container\">");
        out.println("    <div class=\"message-box\">");
        out.println("      <h3>" + message + "</h3>");
        out.println("      <a href=\"/panelEstudiantes\">Volver al Panel de Estudiantes</a>");
        out.println("    </div>");
        out.println("  </div>");
        out.println("</body>");
        out.println("</html>");
    }

    private void sendErrorResponse(PrintWriter out, String message) {
        out.println("<!DOCTYPE html>");
        out.println("<html lang=\"es\">");
        out.println("<head>");
        out.println("  <meta charset=\"UTF-8\" />");
        out.println("  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"/>");
        out.println("  <title>Error</title>");
        out.println("  <link rel=\"stylesheet\" href=\"estilosPEA.css\" />");
        out.println("  <link href=\"https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600&display=swap\" rel=\"stylesheet\">");
        out.println("  <style>");
        out.println("    .message-container {");
        out.println("      display: flex;");
        out.println("      justify-content: center;");
        out.println("      align-items: center;");
        out.println("      height: 100vh;");
        out.println("      background-color: #f0f2f5;");
        out.println("    }");
        out.println("    .message-box {");
        out.println("      background-color: white;");
        out.println("      padding: 20px 40px;");
        out.println("      border-radius: 10px;");
        out.println("      box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);");
        out.println("      text-align: center;");
        out.println("    }");
        out.println("    .message-box h3 {");
        out.println("      color: #dc3545;");
        out.println("      margin-bottom: 20px;");
        out.println("    }");
        out.println("    .message-box a {");
        out.println("      display: inline-block;");
        out.println("      padding: 10px 20px;");
        out.println("      background-color: #007bff;");
        out.println("      color: white;");
        out.println("      text-decoration: none;");
        out.println("      border-radius: 5px;");
        out.println("      margin-top: 10px;");
        out.println("    }");
        out.println("  </style>");
        out.println("</head>");
        out.println("<body>");
        out.println("  <div class=\"message-container\">");
        out.println("    <div class=\"message-box\">");
        out.println("      <h3>" + message + "</h3>");
        out.println("      <a href=\"/panelEstudiantes\">Volver al Panel de Estudiantes</a>");
        out.println("    </div>");
        out.println("  </div>");
        out.println("</body>");
        out.println("</html>");
    }
}