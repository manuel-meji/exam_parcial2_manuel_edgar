import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import modelo.EstudianteDAO;

@WebServlet("/panelEstudiantes")
public class PanelEstudiantesServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        EstudianteDAO dao = new EstudianteDAO();
        List<Map<String, Object>> listaEstudiantes = null;

        try {
            listaEstudiantes = dao.obtenerTodosLosEstudiantes();
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<h1>Error al conectar con la base de datos: " + e.getMessage() + "</h1>");
            return;
        }

        out.println("<!DOCTYPE html>");
        out.println("<html lang=\"es\">");
        out.println("<head>");
        out.println("  <meta charset=\"UTF-8\" />");
        out.println("  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"/>");
        out.println("  <title>Panel de Estudiantes • Admin</title>");
        out.println("  <link rel=\"stylesheet\" href=\"estilosPEA.css\" />");
        out.println("  <link href=\"https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600&display=swap\" rel=\"stylesheet\">");
        out.println("</head>");
        out.println("<body>");
        out.println("  <header class=\"navbar\">");
        out.println("    <h1 class=\"logo\">Administración</h1>");
        out.println("    <nav>");
        out.println("      <ul>");
        out.println("        <li><a href=\"" + request.getContextPath() + "/gestionOficiales\">Oficiales</a></li>");
        out.println("        <li class=\"active\"><a href=\"" + request.getContextPath() + "/panelEstudiantes\">Estudiantes</a></li>");
        out.println("        <li><a href=\"" + request.getContextPath() + "/index.html\">Salir</a></li>");
        out.println("      </ul>");
        out.println("    </nav>");
        out.println("  </header>");

        out.println("  <main class=\"container\">");
        out.println("    <div class=\"main-content\">");
        out.println("      <section class=\"form-section\">");
        out.println("        <h3 id=\"form-titulo\">Gestión de Estudiantes</h3>");
        out.println("        <form id=\"form-estudiante\" action=\"" + request.getContextPath() + "/guardarEstudiante\" method=\"post\">");
        out.println("          <input type=\"hidden\" id=\"carnetOriginal\" name=\"carnetOriginal\">");
        out.println("          <div class=\"form-group\">");
        out.println("            <label for=\"carnet\">Carnet *</label>");
        out.println("            <input type=\"text\" id=\"carnet\" name=\"carnet\" maxlength=\"6\" required>");
        out.println("          </div>");
        out.println("          <div class=\"form-group\">");
        out.println("            <label for=\"nombre1\">Nombre1 *</label>");
        out.println("            <input type=\"text\" id=\"nombre1\" name=\"nombre1\" required>");
        out.println("          </div>");
        out.println("          <div class=\"form-group\">");
        out.println("            <label for=\"nombre2\">Nombre2 (opcional)</label>");
        out.println("            <input type=\"text\" id=\"nombre2\" name=\"nombre2\">");
        out.println("          </div>");
        out.println("          <div class=\"form-group\">");
        out.println("            <label for=\"apellido1\">Apellido1 *</label>");
        out.println("            <input type=\"text\" id=\"apellido1\" name=\"apellido1\" required>");
        out.println("          </div>");
        out.println("          <div class=\"form-group\">");
        out.println("            <label for=\"apellido2\">Apellido2 *</label>");
        out.println("            <input type=\"text\" id=\"apellido2\" name=\"apellido2\" required>");
        out.println("          </div>");
        out.println("          <div class=\"form-group\">");
        out.println("            <label for=\"cedula\">Cédula *</label>");
        out.println("            <input type=\"text\" id=\"cedula\" name=\"cedula\" maxlength=\"15\" required>");
        out.println("          </div>");
        out.println("          <div class=\"form-group\">");
        out.println("            <label for=\"nacionalidad\">Nacionalidad (opcional)</label>");
        out.println("            <select id=\"nacionalidad\" name=\"nacionalidad\">");
        out.println("              <option value=\"\">Seleccione...</option>");
        out.println("              <option value=\"nacional\">Nacional</option>");
        out.println("              <option value=\"extranjero\">Extranjero</option>");
        out.println("            </select>");
        out.println("          </div>");
        out.println("          <div class=\"form-group\">");
        out.println("            <label for=\"direccion\">Dirección *</label>");
        out.println("            <select id=\"direccion\" name=\"direccion\" required>");
        out.println("              <option value=\"\">Seleccione...</option>");
        out.println("              <option>Upala</option>");
        out.println("              <option>Aguas Claras</option>");
        out.println("              <option>San José de Upala</option>");
        out.println("              <option>Bijagua</option>");
        out.println("              <option>Delicias</option>");
        out.println("              <option>Dos Ríos</option>");
        out.println("              <option>Yolillal</option>");
        out.println("              <option>Canalete</option>");
        out.println("            </select>");
        out.println("          </div>");
        out.println("          <div class=\"form-group\">");
        out.println("            <label for=\"fechaNacimiento\">Fecha de Nacimiento *</label>");
        out.println("            <input type=\"date\" id=\"fechaNacimiento\" name=\"fechaNacimiento\" required>");
        out.println("          </div>");
        out.println("          <div class=\"footer-buttons\">");
        out.println("            <button type=\"button\" class=\"clear-button\" onclick=\"limpiarYResetearForm()\">Limpiar</button>");
        out.println("            <button type=\"submit\" class=\"add-button\" id=\"btnAgregar\">Guardar</button>");
        out.println("          </div>");
        out.println("          <div class=\"footer-buttons\">");
        out.println("            <button type=\"button\" class=\"edit-button\" id=\"btnEditarForm\" onclick=\"document.getElementById('form-estudiante').submit();\" style=\"display:none;\">✏️ Guardar Cambios</button>");
        out.println("          </div>");
        out.println("        </form>");
        out.println("      </section>");
        out.println("      <section class=\"table-section\">");
        out.println("        <div class=\"form-group\">");
        out.println("          <input type=\"text\" id=\"buscarEstudiante\" placeholder=\"🔍 Buscar Estudiante…\" onkeyup=\"filtrarTabla()\">");
        out.println("        </div>");
        out.println("        <table id=\"estudiantesTable\">");
        out.println("          <thead>");
        out.println("            <tr>");
        out.println("              <th>Nombre completo</th>");
        out.println("              <th>Carnet</th>");
        out.println("              <th>Cédula</th>");
        out.println("              <th>Fecha de Nacimiento</th>");
        out.println("              <th>Edad</th>");
        out.println("              <th>Nacionalidad</th>");
        out.println("              <th>Dirección</th>");
        out.println("              <th>Acciones</th>");
        out.println("            </tr>");
        out.println("          </thead>");
        out.println("          <tbody>");
        if (listaEstudiantes != null && !listaEstudiantes.isEmpty()) {
            for (Map<String, Object> estudiante : listaEstudiantes) {
                String nombreCompleto = estudiante.get("nombre1") + " "
                        + (estudiante.get("nombre2") != null ? estudiante.get("nombre2") + " " : "")
                        + estudiante.get("apellido1") + " " + estudiante.get("apellido2");
                String carnet = (String) estudiante.get("carnet");
                String cedula = (String) estudiante.get("cedula");
                Date fechaNacimiento = (Date) estudiante.get("fechaNacimiento");
                String fechaNacimientoStr = fechaNacimiento != null ? fechaNacimiento.toString() : "";
                int edad = estudiante.get("edad") != null ? (int) estudiante.get("edad") : 0;
                out.println("<tr>");
                out.println("<td>" + nombreCompleto + "</td>");
                out.println("<td>" + carnet + "</td>");
                out.println("<td>" + cedula + "</td>");
                out.println("<td>" + fechaNacimientoStr + "</td>");
                out.println("<td>" + edad + "</td>");
                out.println("<td>" + (estudiante.get("nacionalidad") != null ? estudiante.get("nacionalidad") : "") + "</td>");
                out.println("<td>" + estudiante.get("direccion") + "</td>");
                out.println("<td>");
                String datosParaEditar = String.format("'%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s'",
                        carnet,
                        estudiante.get("nombre1"),
                        estudiante.get("nombre2") != null ? estudiante.get("nombre2") : "",
                        estudiante.get("apellido1"),
                        estudiante.get("apellido2"),
                        cedula,
                        estudiante.get("nacionalidad") != null ? estudiante.get("nacionalidad") : "",
                        estudiante.get("direccion"),
                        fechaNacimientoStr,
                        carnet);
                out.println("<a href=\"#\" onclick=\"cargarDatosParaEditar(" + datosParaEditar + ")\" style='margin-right:10px;'>Editar</a>");
                out.println("<a href=\"eliminarEstudiante?carnet=" + carnet + "\" onclick=\"return confirm('¿Estás seguro de que quieres eliminar al estudiante \\'" + carnet + "\\'?');\">Eliminar</a>");
                out.println("</td>");
                out.println("</tr>");
            }
        } else {
            out.println("<tr><td colspan=\"8\" style=\"text-align:center;\">No hay estudiantes registrados.</td></tr>");
        }
        out.println("          </tbody>");
        out.println("        </table>");
        out.println("      </section>");
        out.println("    </div>");
        out.println("  </main>");
        out.println("  <footer class=\"footer\">");
        out.println("    © 2025 Seguridad y Tecnología • Todos los derechos reservados");
        out.println("  </footer>");
        out.println("  <script>");
        out.println("const form = document.getElementById('form-estudiante');");
        out.println("const btnAgregar = document.getElementById('btnAgregar');");
        out.println("const btnEditarForm = document.getElementById('btnEditarForm');");
        out.println("const formTitulo = document.getElementById('form-titulo');");
        out.println("function cargarDatosParaEditar(carnet, nombre1, nombre2, apellido1, apellido2, cedula, nacionalidad, direccion, fechaNacimiento, carnetOriginal) {");
        out.println("   document.getElementById('carnet').value = carnet;");
        out.println("   document.getElementById('nombre1').value = nombre1;");
        out.println("   document.getElementById('nombre2').value = nombre2;");
        out.println("   document.getElementById('apellido1').value = apellido1;");
        out.println("   document.getElementById('apellido2').value = apellido2;");
        out.println("   document.getElementById('cedula').value = cedula;");
        out.println("   document.getElementById('nacionalidad').value = nacionalidad;");
        out.println("   document.getElementById('direccion').value = direccion;");
        out.println("   document.getElementById('fechaNacimiento').value = fechaNacimiento;");
        out.println("   document.getElementById('carnetOriginal').value = carnetOriginal;");
        out.println("   form.action = 'editarEstudiante';");
        out.println("   formTitulo.innerText = 'Editando a ' + carnetOriginal;");
        out.println("   btnAgregar.style.display = 'none';");
        out.println("   btnEditarForm.style.display = 'inline-block';");
        out.println("   window.scrollTo(0, 0);");
        out.println("}");
        out.println("function limpiarYResetearForm() {");
        out.println("   form.reset();");
        out.println("   form.action = '" + request.getContextPath() + "/guardarEstudiante';");
        out.println("   formTitulo.innerText = 'Gestión de Estudiantes';");
        out.println("   btnAgregar.style.display = 'inline-block';");
        out.println("   btnEditarForm.style.display = 'none';");
        out.println("}");
        out.println("document.addEventListener('DOMContentLoaded', function() { limpiarYResetearForm(); });");
        out.println("function filtrarTabla() {");
        out.println("    var input = document.getElementById('buscarEstudiante').value.toLowerCase();");
        out.println("    var table = document.getElementById('estudiantesTable');");
        out.println("    var tr = table.getElementsByTagName('tr');");
        out.println("    for (var i = 1; i < tr.length; i++) { // Start from 1 to skip header row");
        out.println("        var found = false;");
        out.println("        var td = tr[i].getElementsByTagName('td');");
        out.println("        for (var j = 0; j < td.length - 1; j++) { // Exclude actions column");
        out.println("            if (td[j] && td[j].textContent.toLowerCase().indexOf(input) > -1) {");
        out.println("                found = true;");
        out.println("                break;");
        out.println("            }");
        out.println("        }");
        out.println("        tr[i].style.display = found ? '' : 'none';");
        out.println("    }");
        out.println("    // Handle case when no matches are found");
        out.println("    var noResults = true;");
        out.println("    for (var i = 1; i < tr.length; i++) {");
        out.println("        if (tr[i].style.display !== 'none') {");
        out.println("            noResults = false;");
        out.println("            break;");
        out.println("        }");
        out.println("    }");
        out.println("    if (noResults && input !== '') {");
        out.println("        var tbody = table.getElementsByTagName('tbody')[0];");
        out.println("        tbody.innerHTML = '<tr><td colspan=\"8\" style=\"text-align:center;\">No se encontraron estudiantes.</td></tr>';");
        out.println("    } else if (input === '' && listaEstudiantes.length === 0) {");
        out.println("        var tbody = table.getElementsByTagName('tbody')[0];");
        out.println("        tbody.innerHTML = '<tr><td colspan=\"8\" style=\"text-align:center;\">No hay estudiantes registrados.</td></tr>';");
        out.println("    }");
        out.println("}");
        out.println("</script>");
        out.println("</body>");
        out.println("</html>");
    }
}