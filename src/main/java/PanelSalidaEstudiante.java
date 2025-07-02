
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import modelo.EstudianteDAO;

@WebServlet("/panelSalidaEstudiante")
public class PanelSalidaEstudiante extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        EstudianteDAO estudianteDAO = new EstudianteDAO();
        SalidaEstudianteDAO salidaDAO = new SalidaEstudianteDAO();
        List<Map<String, Object>> listaEstudiantes = null;
        List<Map<String, Object>> listaSalidas = null;

        try {
            listaEstudiantes = estudianteDAO.obtenerTodosLosEstudiantes();
            listaSalidas = salidaDAO.obtenerTodasLasSalidas();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            out.println("<h1>Error al conectar con la base de datos: " + e.getMessage() + "</h1>");
            return;
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<h1>Error inesperado: " + e.getMessage() + "</h1>");
            return;
        }

        out.println("<!DOCTYPE html>");
        out.println("<html lang=\"es\">");
        out.println("<head>");
        out.println("  <meta charset=\"UTF-8\">");
        out.println("  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
        out.println("  <title>Salida de Estudiantes • Oficial</title>");
        out.println(
                "  <link href=\"https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600&display=swap\" rel=\"stylesheet\">");
        out.println("  <link rel=\"stylesheet\" href=\"estilosPSEO.css\">");
        out.println("  <script>");
        out.println("    function filtrarEstudiantes() {");
        out.println("      var input = document.getElementById('buscarEstudiante').value.toLowerCase();");
        out.println("      var select = document.getElementById('selectEstudiante');");
        out.println("      var options = select.getElementsByTagName('option');");
        out.println("      for (var i = 0; i < options.length; i++) {");
        out.println("        var txt = options[i].text.toLowerCase();");
        out.println("        if (txt.indexOf(input) > -1) {");
        out.println("          options[i].style.display = '';");
        out.println("        } else {");
        out.println("          options[i].style.display = 'none';");
        out.println("        }");
        out.println("      }");
        out.println("    }");
        out.println("    function aprobarSalida() {");
        out.println("      console.log('Aprobar clicked');");
        out.println("      var select = document.getElementById('selectEstudiante');");
        out.println("      var motivo = document.getElementById('motivoSalida').value;");
        out.println("      if (select.value && motivo) {");
        out.println("        var carnet = select.value.split(' - ')[0];");
        out.println("        console.log('Carnet: ' + carnet + ', Motivo: ' + motivo);");
        out.println("        var fecha = new Date().toISOString().split('T')[0];");
        out.println("        var hora = new Date().toTimeString().split(' ')[0];");
        out.println("        var nombreUsuarioGuarda = 'Mexicano';");
        out.println("        var xhr = new XMLHttpRequest();");
        out.println("        xhr.open('POST', '" + request.getContextPath() + "/aprobarSalida', true);");
        out.println("        xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');");
        out.println("        xhr.onreadystatechange = function() {");
        out.println("          if (xhr.readyState === 4) {");
        out.println("            console.log('Response: ' + xhr.status + ' - ' + xhr.responseText);");
        out.println("            if (xhr.status === 200 && xhr.responseText === 'success') {");
        out.println("              location.reload();");
        out.println("            } else {");
        out.println("              alert('Error: ' + xhr.responseText);");
        out.println("            }");
        out.println("          }");
        out.println("        };");
        out.println(
                "        xhr.send('action=aprobar&carnet=' + encodeURIComponent(carnet) + '&motivo=' + encodeURIComponent(motivo) + '&nombreUsuarioGuarda=' + encodeURIComponent(nombreUsuarioGuarda));");
        out.println("      } else {");
        out.println("        alert('Seleccione un estudiante y un motivo.');");
        out.println("      }");
        out.println("    }");
        out.println("    function eliminarSalida(id) {");
        out.println("      console.log('Eliminar clicked', id);");
        out.println("      if (confirm('¿Estás seguro de que quieres eliminar esta salida?')) {");
        out.println("        var xhr = new XMLHttpRequest();");
        out.println("        xhr.open('POST', '" + request.getContextPath() + "/eliminarSalida', true);");
        out.println("        xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');");
        out.println("        xhr.onreadystatechange = function() {");
        out.println("          if (xhr.readyState === 4) {");
        out.println("            console.log('Response: ' + xhr.status + ' - ' + xhr.responseText);");
        out.println("            if (xhr.status === 200 && xhr.responseText === 'success') {");
        out.println("              location.reload();");
        out.println("            } else {");
        out.println("              alert('Error: ' + xhr.responseText);");
        out.println("            }");
        out.println("          }");
        out.println("        };");
        out.println("        xhr.send('action=eliminar&id=' + encodeURIComponent(id));");
        out.println("      }");
        out.println("    }");
        out.println("  </script>");
        out.println("</head>");
        out.println("<body>");
        out.println("  <header class=\"navbar\">");
        out.println("    <h1 class=\"logo\">Gestión de Seguridad</h1>");
        out.println("    <nav>");
        out.println("      <ul>");
        out.println("        <li class=\"active\">"
                + "<a href=\"http://localhost:8080/exam_parcial2_manuel_edgar/panelSalidaEstudiante\">"
                + "Salida de Estudiantes</a></li>");
        out.println("        <li>"
                + "<a href=\"http://localhost:8080/exam_parcial2_manuel_edgar/panelFuncionarios\">"
                + "Funcionarios</a></li>");
        out.println("        <li>"
                + "<a href=\"http://localhost:8080/exam_parcial2_manuel_edgar/panelIngresoFuncionario\">"
                + "Ingreso Funcionario</a></li>");
        out.println("        <li>"
                + "<a href=\"http://localhost:8080/exam_parcial2_manuel_edgar/index.html\" >"
                + "Salir</a></li>");
        out.println("      </ul>");
        out.println("    </nav>");
        out.println("  </header>");
        out.println("  <main class=\"container\">");
        out.println(
                "    <h2 class=\"page-title\">Registra las salidas de los estudiantes del Colegio Técnico Profesional de Upala</h2>");
        out.println("    <div class=\"main-content\">");
        out.println("      <section class=\"form-section\">");
        out.println("        <div class=\"form-inline\">");
        out.println("          <div class=\"search-group\">");
        out.println("            <label for=\"buscarEstudiante\">Buscar estudiante:</label>");
        out.println(
                "            <input type=\"text\" id=\"buscarEstudiante\" placeholder=\"Nombre o ID…\" onkeyup=\"filtrarEstudiantes()\">");
        out.println("            <button class=\"search-button\" type=\"button\">");
        out.println(
                "              <img src=\"https://img.icons8.com/?size=100&id=132&format=png&color=FFFFFF\" alt=\"Buscar\" style=\"width:20px; vertical-align:middle;\">");
        out.println("              Buscar");
        out.println("            </button>");
        out.println("          </div>");
        out.println("          <div class=\"form-group\">");
        out.println("            <label for=\"selectEstudiante\">Seleccione el estudiante:</label>");
        out.println("            <select id=\"selectEstudiante\">");
        out.println("              <option value=\"\">-- vacio --</option>");
        if (listaEstudiantes != null) {
            for (Map<String, Object> estudiante : listaEstudiantes) {
                String nombreCompleto = (String) estudiante.get("nombre1") + " " + (String) estudiante.get("apellido1");
                String optionValue = (String) estudiante.get("carnet") + " - " + nombreCompleto;
                out.println("<option value=\"" + estudiante.get("carnet") + "\">" + optionValue + "</option>");
            }
        }
        out.println("            </select>");
        out.println("          </div>");
        out.println("          <div class=\"form-group\">");
        out.println("            <label for=\"motivoSalida\">Motivo de salida:</label>");
        out.println("            <select id=\"motivoSalida\">");
        out.println("              <option value=\"\">-- seleccione --</option>");
        out.println("              <option>Salida por cita médica o dental</option>");
        out.println("              <option>Salida por problemas de salud</option>");
        out.println("              <option>Salida por llamado de padres o encargados</option>");
        out.println("              <option>Fallecimiento de familiar o allegado</option>");
        out.println("              <option>Salida de almuerzo</option>");
        out.println("              <option>Salida por tarde libre</option>");
        out.println("              <option>Salida por ausencia de profesor</option>");
        out.println("              <option>Salida por otro motivo</option>");
        out.println("            </select>");
        out.println("          </div>");
        out.println("          <div class=\"actions-inline\">");
        out.println("            <button class=\"approve-button\" type=\"button\" onclick=\"aprobarSalida()\">");
        out.println(
                "              <img src=\"https://img.icons8.com/?size=100&id=FFdft2aGmPoF&format=png&color=FFFFFF\" style=\"width:20px; vertical-align:middle;\" alt=\"Aprobar\">");
        out.println("              Aprobar salida");
        out.println("            </button>");
        out.println(
                "            <button class=\"delete-button\" type=\"button\" onclick=\"eliminarSalida(prompt('Ingrese el ID de la salida a eliminar:'))\">");
        out.println(
                "              <img src=\"https://img.icons8.com/?size=100&id=68138&format=png&color=FFFFFF\" style=\"width:20px; vertical-align:middle;\" alt=\"Eliminar\">");
        out.println("              Eliminar Salida");
        out.println("            </button>");
        out.println("          </div>");
        out.println("        </div>");
        out.println("      </section>");
        out.println("      <section class=\"table-section\">");
        out.println("        <table>");
        out.println("          <thead>");
        out.println("            <tr>");
        out.println("              <th>ID Salida</th>");
        out.println("              <th>Nombre Estudiante</th>");
        out.println("              <th>ID</th>");
        out.println("              <th>Motivo de salida</th>");
        out.println("              <th>Fecha de salida</th>");
        out.println("              <th>Hora de salida</th>");
        out.println("              <th>Nombre de guarda</th>");
        out.println("            </tr>");
        out.println("          </thead>");
        out.println("          <tbody>");
        if (listaSalidas != null) {
            EstudianteDAO estDAO = new EstudianteDAO();
            List<Map<String, Object>> estudiantes;
            try {
                estudiantes = estDAO.obtenerTodosLosEstudiantes();
                for (Map<String, Object> salida : listaSalidas) {
                    String nombreEstudiante = "";
                    for (Map<String, Object> estudiante : estudiantes) {
                        if (estudiante.get("carnet").equals(salida.get("carnet"))) {
                            nombreEstudiante = (String) estudiante.get("nombre1") + " "
                                    + (String) estudiante.get("apellido1");
                            break;
                        }
                    }
                    out.println("<tr>");
                    out.println("<td>" + salida.get("id") + "</td>");
                    out.println("<td>" + nombreEstudiante + "</td>");
                    out.println("<td>" + salida.get("carnet") + "</td>");
                    out.println("<td>" + salida.get("motivo") + "</td>");
                    out.println("<td>" + (salida.get("fecha") != null ? salida.get("fecha").toString() : "") + "</td>");
                    out.println("<td>" + (salida.get("hora") != null ? salida.get("hora").toString() : "") + "</td>");
                    out.println("<td>" + salida.get("nombre_usuario_guarda") + "</td>");
                    out.println("</tr>");
                }
            } catch (java.lang.ClassNotFoundException | SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        out.println("          </tbody>");
        out.println("        </table>");
        out.println("      </section>");
        out.println("    </div>");
        out.println("  </main>");
        out.println("  <footer class=\"footer\">");
        out.println("    © 2025 Seguridad y Tecnología • Todos los derechos reservados");
        out.println("  </footer>");
        out.println("</body>");
        out.println("</html>");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        String action = request.getParameter("action");
        SalidaEstudianteDAO dao = new SalidaEstudianteDAO();

        if ("aprobar".equals(action)) {
            String carnet = request.getParameter("carnet");
            String motivo = request.getParameter("motivo");
            String nombreUsuarioGuarda = request.getParameter("nombreUsuarioGuarda");
            java.sql.Date fecha = new java.sql.Date(new java.util.Date().getTime());
            java.sql.Time hora = new java.sql.Time(new java.util.Date().getTime());
            try {
                dao.agregarSalida(carnet, fecha, hora, motivo, nombreUsuarioGuarda);
                response.getWriter().write("success");
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                response.getWriter().write("error: " + e.getMessage());
            }
        } else if ("eliminar".equals(action)) {
            String idStr = request.getParameter("id");
            try {
                int id = Integer.parseInt(idStr);
                dao.eliminarSalida(id);
                response.getWriter().write("success");
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                response.getWriter().write("error: " + e.getMessage());
            } catch (NumberFormatException e) {
                response.getWriter().write("error: ID inválido");
            }
        }
    }
}
