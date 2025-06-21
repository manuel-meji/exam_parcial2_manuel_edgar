import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

@WebServlet("/panelIngresoFuncionario")
public class PanelIngresoFuncionarioServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
        IngresoFuncionarioDAO ingresoDAO = new IngresoFuncionarioDAO();
        List<Map<String, Object>> listaFuncionarios = null;
        List<Map<String, Object>> listaIngresos = null;

        try {
            listaFuncionarios = funcionarioDAO.obtenerTodosLosFuncionarios();
            listaIngresos = ingresoDAO.obtenerTodosLosIngresos();
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
        out.println("  <title>Ingreso de Funcionarios • Oficial</title>");
        out.println("  <link href=\"https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600&display=swap\" rel=\"stylesheet\">");
        out.println("  <link rel=\"stylesheet\" href=\"estilosPSEO.css\">");
        out.println("  <script>");
        out.println("    function filtrarFuncionarios() {");
        out.println("      var input = document.getElementById('txtBusqueda').value.toLowerCase();");
        out.println("      var select = document.getElementById('comboBoxFuncionarios');");
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
        out.println("    function agregarIngreso() {");
        out.println("      console.log('Agregar clicked');");
        out.println("      var select = document.getElementById('comboBoxFuncionarios');");
        out.println("      if (select.value) {");
        out.println("        var cedula = select.value.split(' - ')[0];");
        out.println("        console.log('Cédula: ' + cedula);");
        out.println("        var fecha = new Date().toISOString().split('T')[0];");
        out.println("        var hora = new Date().toTimeString().split(' ')[0];");
        out.println("        var nombreUsuarioGuarda = 'Oficial1'; // Placeholder, replace with session data");
        out.println("        var xhr = new XMLHttpRequest();");
        out.println("        xhr.open('POST', '" + request.getContextPath() + "/agregarIngreso', true);");
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
        out.println("        xhr.send('action=agregar&cedula=' + encodeURIComponent(cedula) + '&nombreUsuarioGuarda=' + encodeURIComponent(nombreUsuarioGuarda));");
        out.println("      } else {");
        out.println("        alert('Seleccione un funcionario.');");
        out.println("      }");
        out.println("    }");
        out.println("    function eliminarIngreso(id) {");
        out.println("      console.log('Eliminar clicked', id);");
        out.println("      if (confirm('¿Estás seguro de que quieres eliminar este ingreso?')) {");
        out.println("        var xhr = new XMLHttpRequest();");
        out.println("        xhr.open('POST', '" + request.getContextPath() + "/eliminarIngreso', true);");
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
        out.println("        <li><a href=\"" + request.getContextPath() + "/panelSalidaEstudiante\">Salida de Estudiantes</a></li>");
        out.println("        <li class=\"active\"><a href=\"#\">Ingreso de Funcionarios</a></li>");
        out.println("        <li class=\"dropdown\">");
        out.println("          <a href=\"#\">Ingresos ▼</a>");
        out.println("          <ul class=\"dropdown-menu\">");
        out.println("            <li><a href=\"" + request.getContextPath() + "/panelIngresoFuncionario\">Ingreso Funcionario</a></li>");
        out.println("            <li><a href=\"#\">Ingreso Persona Externa</a></li>");
        out.println("            <li><a href=\"#\">Ingreso Vehículo Externo</a></li>");
        out.println("          </ul>");
        out.println("        </li>");
        out.println("      </ul>");
        out.println("    </nav>");
        out.println("  </header>");
        out.println("  <main class=\"container\">");
        out.println("    <h2 class=\"page-title\">Registra los ingresos de los funcionarios</h2>");
        out.println("    <div class=\"main-content\">");
        out.println("      <section class=\"form-section\">");
        out.println("        <div class=\"form-inline\">");
        out.println("          <div class=\"search-group\">");
        out.println("            <label for=\"txtBusqueda\">Busca un funcionario:</label>");
        out.println("            <input type=\"text\" id=\"txtBusqueda\" placeholder=\"Cédula, Nombre o Placa…\" onkeyup=\"filtrarFuncionarios()\">");
        out.println("            <button class=\"search-button\" type=\"button\">");
        out.println("              <img src=\"https://img.icons8.com/?size=100&id=132&format=png&color=FFFFFF\" alt=\"Buscar\" style=\"width:20px; vertical-align:middle;\">");
        out.println("              Buscar");
        out.println("            </button>");
        out.println("          </div>");
        out.println("          <div class=\"form-group\">");
        out.println("            <label for=\"comboBoxFuncionarios\">Cédula del funcionario:</label>");
        out.println("            <select id=\"comboBoxFuncionarios\">");
        out.println("              <option value=\"\">-- vacio --</option>");
        if (listaFuncionarios != null) {
            for (Map<String, Object> funcionario : listaFuncionarios) {
                String nombreCompleto = (String) funcionario.get("nombreCompleto");
                String placa = (String) funcionario.get("placa");
                String optionValue = (String) funcionario.get("id") + " - " + nombreCompleto + " (Placa: " + (placa != null ? placa : "N/A") + ")";
                out.println("<option value=\"" + funcionario.get("id") + "\">" + optionValue + "</option>");
            }
        }
        out.println("            </select>");
        out.println("          </div>");
        out.println("          <div class=\"actions-inline\">");
        out.println("            <button class=\"approve-button\" type=\"button\" onclick=\"agregarIngreso()\">");
        out.println("              <img src=\"https://img.icons8.com/?size=100&id=FFdft2aGmPoF&format=png&color=FFFFFF\" style=\"width:20px; vertical-align:middle;\" alt=\"Agregar\">");
        out.println("              Agregar");
        out.println("            </button>");
        out.println("            <button class=\"delete-button\" type=\"button\" onclick=\"eliminarIngreso(prompt('Ingrese el ID del ingreso a eliminar:'))\">");
        out.println("              <img src=\"https://img.icons8.com/?size=100&id=68138&format=png&color=FFFFFF\" style=\"width:20px; vertical-align:middle;\" alt=\"Eliminar\">");
        out.println("              Eliminar");
        out.println("            </button>");
        out.println("          </div>");
        out.println("        </div>");
        out.println("      </section>");
        out.println("      <section class=\"table-section\">");
        out.println("        <table>");
        out.println("          <thead>");
        out.println("            <tr>");
        out.println("              <th>ID Ingreso</th>");
        out.println("              <th>Nombre Funcionario</th>");
        out.println("              <th>Cédula</th>");
        out.println("              <th>Tipo de Vehículo</th>");
        out.println("              <th>Placa</th>");
        out.println("              <th>Fecha Ingreso</th>");
        out.println("              <th>Hora Ingreso</th>");
        out.println("              <th>Nombre de Guarda</th>");
        out.println("            </tr>");
        out.println("          </thead>");
        out.println("          <tbody>");
        if (listaIngresos != null) {
            for (Map<String, Object> ingreso : listaIngresos) {
                out.println("<tr>");
                out.println("<td>" + ingreso.get("id") + "</td>");
                out.println("<td>" + ingreso.get("nombre_funcionario") + "</td>");
                out.println("<td>" + ingreso.get("cedula") + "</td>");
                out.println("<td>" + (ingreso.get("tipo_vehiculo") != null ? ingreso.get("tipo_vehiculo") : "N/A") + "</td>");
                out.println("<td>" + (ingreso.get("placa") != null ? ingreso.get("placa") : "N/A") + "</td>");
                out.println("<td>" + (ingreso.get("fecha") != null ? ingreso.get("fecha").toString() : "") + "</td>");
                out.println("<td>" + (ingreso.get("hora") != null ? ingreso.get("hora").toString() : "") + "</td>");
                out.println("<td>" + ingreso.get("nombre_usuario_guarda") + "</td>");
                out.println("</tr>");
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

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        String action = request.getParameter("action");
        IngresoFuncionarioDAO dao = new IngresoFuncionarioDAO();

        if ("agregar".equals(action)) {
            String cedula = request.getParameter("cedula");
            String nombreUsuarioGuarda = request.getParameter("nombreUsuarioGuarda");
            java.sql.Date fechaIngreso = new java.sql.Date(new java.util.Date().getTime());
            java.sql.Time horaIngreso = new java.sql.Time(new java.util.Date().getTime());
            try {
                dao.agregarIngreso(cedula, fechaIngreso, horaIngreso, nombreUsuarioGuarda);
                response.getWriter().write("success");
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                response.getWriter().write("error: " + e.getMessage());
            }
        } else if ("eliminar".equals(action)) {
            String idStr = request.getParameter("id");
            try {
                int id = Integer.parseInt(idStr);
                dao.eliminarIngreso(id);
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