import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import modelo.EstudianteDAO;

@WebServlet("/guardarEstudiante")
public class GuardarEstudianteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        String carnet = request.getParameter("carnet");
        String nombre1 = request.getParameter("nombre1");
        String nombre2 = request.getParameter("nombre2");
        String apellido1 = request.getParameter("apellido1");
        String apellido2 = request.getParameter("apellido2");
        String cedula = request.getParameter("cedula");
        String nacionalidad = request.getParameter("nacionalidad");
        String direccion = request.getParameter("direccion");
        String fechaNacimientoStr = request.getParameter("fechaNacimiento");

        StringBuilder errorMessage = new StringBuilder();
        if (isEmpty(carnet)) errorMessage.append("Carnet es requerido.<br>");
        if (isEmpty(nombre1)) errorMessage.append("Nombre1 es requerido.<br>");
        if (isEmpty(apellido1)) errorMessage.append("Apellido1 es requerido.<br>");
        if (isEmpty(apellido2)) errorMessage.append("Apellido2 es requerido.<br>");
        if (isEmpty(cedula)) errorMessage.append("Cédula es requerida.<br>");
        if (isEmpty(direccion)) errorMessage.append("Dirección es requerida.<br>");
        if (isEmpty(fechaNacimientoStr)) errorMessage.append("Fecha de Nacimiento es requerida.<br>");

        Date fechaNacimiento = null;
        if (!isEmpty(fechaNacimientoStr)) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                sdf.setLenient(false);
                java.util.Date parsedDate = sdf.parse(fechaNacimientoStr);
                fechaNacimiento = new Date(parsedDate.getTime());
            } catch (ParseException e) {
                errorMessage.append("Fecha de Nacimiento debe estar en formato AAAA-MM-DD.<br>");
            }
        }

        if (errorMessage.length() > 0) {
            sendErrorResponse(out, errorMessage.toString());
            return;
        }

        EstudianteDAO dao = new EstudianteDAO();
        try {
            dao.agregarEstudiante(carnet, nombre1, nombre2, apellido1, apellido2, cedula, nacionalidad, direccion, fechaNacimiento);
            sendSuccessResponse(out, "Estudiante agregado correctamente.");
        } catch (Exception e) {
            sendErrorResponse(out, "Error al agregar estudiante: " + e.getMessage());
            e.printStackTrace();
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
        out.println("      <a href=\"http://localhost:8080/exam_parcial2_manuel_edgar/panelEstudiantes\">Volver al Panel de Estudiantes</a>");
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