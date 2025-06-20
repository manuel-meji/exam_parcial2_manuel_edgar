import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

 // Asegúrate de que esta ruta sea correcta

@WebServlet("/panelFuncionarios")
public class PanelFuncionariosServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        FuncionarioDAO dao = new FuncionarioDAO();
        List<Map<String, Object>> listaFuncionarios = null;

        try {
            listaFuncionarios = dao.obtenerTodosLosFuncionarios();
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<h1>Error al obtener la lista de funcionarios: " + e.getMessage() + "</h1>");
            return;
        }

        // --- INICIO DEL HTML COMPLETO ---
        out.println("<!DOCTYPE html>");
        out.println("<html lang=\"es\">");
        out.println("<head>");
        out.println("  <meta charset=\"UTF-8\">");
        out.println("  <title>Funcionarios • Oficial</title>");
        out.println("  <link href=\"https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600&display=swap\" rel=\"stylesheet\">");
        out.println("  <link rel=\"stylesheet\" href=\"estilosPFO.css\">");
        out.println("</head>");
        out.println("<body>");
        
        // --- Navbar superior (COMPLETA) ---
        out.println("  <header class=\"navbar\"><h1 class=\"logo\">Gestión de Seguridad</h1><nav><ul><li><a href=\"#\">Salida de Estudiantes</a></li><li class=\"active\"><a href=\"#\">Funcionarios</a></li><li class=\"dropdown\"><a href=\"#\">Ingresos ▼</a><ul class=\"dropdown-menu\"><li><a href=\"#\">Ingreso Funcionario</a></li><li><a href=\"#\">Ingreso Persona Externa</a></li><li><a href=\"#\">Ingreso Vehículo Externo</a></li></ul></li></ul></nav></header>");
        
        out.println("  <main class=\"container\">");
        out.println("    <h2 class=\"page-title\">Registra los funcionarios del CTP UPALA</h2>");
        out.println("    <div class=\"main-content\">");
        
        // --- Formulario (COMPLETO) ---
        out.println("      <section class=\"form-section\">");
        out.println("        <form id=\"formFuncionario\" action=\"agregarFuncionario\" method=\"POST\">");
        out.println("          <input type=\"hidden\" id=\"cedulaOriginal\" name=\"cedulaOriginal\">");
        out.println("          <h3 id=\"form-titulo\">Registrar Funcionario</h3>");
        out.println("          <div class=\"form-row\">");
        out.println("            <div class=\"form-group full\"><label for=\"rolFuncionario\">Seleccione el rol del funcionario: *</label><select id=\"rolFuncionario\" name=\"rolFuncionario\" required><option value=\"\">-- Seleccione --</option><option>Profesor(a)</option><option>Administrador(a)</option><option>Secretario(a)</option><option>Vigilante</option><option>Otro</option></select></div>");
        out.println("            <div class=\"form-group\"><label for=\"primerNombre\">Primer nombre: *</label><input type=\"text\" id=\"primerNombre\" name=\"primerNombre\" required></div>");
        out.println("            <div class=\"form-group\"><label for=\"segundoNombre\">Segundo nombre:</label><input type=\"text\" id=\"segundoNombre\" name=\"segundoNombre\"></div>");
        out.println("            <div class=\"form-group\"><label for=\"primerApellido\">Primer apellido: *</label><input type=\"text\" id=\"primerApellido\" name=\"primerApellido\" required></div>");
        out.println("            <div class=\"form-group\"><label for=\"segundoApellido\">Segundo apellido: *</label><input type=\"text\" id=\"segundoApellido\" name=\"segundoApellido\" required></div>");
        out.println("            <div class=\"form-group\"><label for=\"identificacion\">Número de identificación: *</label><input type=\"text\" id=\"identificacion\" name=\"identificacion\" required></div>");
        out.println("            <div class=\"form-group\"><label for=\"tipoVehiculo\">Tipo de vehículo: *</label><select id=\"tipoVehiculo\" name=\"tipoVehiculo\" required><option value=\"\">-- Seleccione --</option><option>Automóvil</option><option>Motocicleta</option><option>Bicicleta</option><option>Camión</option><option>Autobús</option><option>Otro</option></select></div>");
        out.println("            <div class=\"form-group\"><label for=\"placa\">Número de placa:</label><input type=\"text\" id=\"placa\" name=\"placa\"></div>");
        out.println("          </div>");
        out.println("          <div class=\"form-actions\">");
        out.println("            <button type=\"button\" onclick=\"limpiarYResetearForm()\" class=\"btn-clear\"><img src=\"https://img.icons8.com/?size=100&id=KFTWyqy1G61u&format=png&color=FFFFFF\" width=\"20\" height=\"20\" alt=\"Borrar\"> Limpiar</button>");
        out.println("            <button type=\"submit\" id=\"btnAgregar\" class=\"btn-primary\"><img src=\"https://img.icons8.com/?size=100&id=MCdDfCTzd5GC&format=png&color=FFFFFF\" width=\"20\" height=\"20\" alt=\"Registrar\"> Registrar</button>");
        out.println("            <button type=\"button\" id=\"btnEditarForm\" onclick=\"document.getElementById('formFuncionario').submit();\" class=\"btn-secondary\" style=\"display:none;\"><img src=\"https://img.icons8.com/?size=100&id=8192&format=png&color=FFFFFF\" width=\"20\" height=\"20\" alt=\"Editar\"> Guardar Cambios</button>");
        out.println("          </div>");
        out.println("        </form>");
        out.println("      </section>");
        
        // --- Tabla (COMPLETA) ---
        out.println("      <section class=\"table-section\">");
        out.println("        <table>");
        out.println("          <thead><tr><th>Puesto</th><th>Nombre Completo</th><th>ID</th><th>Vehículo</th><th>Placa</th><th>Acciones</th></tr></thead>");
        out.println("          <tbody>");

        if (listaFuncionarios != null && !listaFuncionarios.isEmpty()) {
            for (Map<String, Object> f : listaFuncionarios) {
                out.println("<tr>");
                
                // --- CELDAS DE DATOS (CORREGIDAS) ---
                out.println("    <td>" + f.get("puesto") + "</td>");
                out.println("    <td>" + f.get("nombreCompleto") + "</td>");
                out.println("    <td>" + f.get("id") + "</td>");
                out.println("    <td>" + f.get("vehiculo") + "</td>");
                out.println("    <td>" + f.get("placa") + "</td>");
                
                // ****** INICIO DE LA SECCIÓN QUE FALTABA ******

                // --- CELDA DE ACCIONES (AÑADIDA) ---
                String cedulaOriginal = (String) f.get("id");
                String urlEliminar = "eliminarFuncionario?cedula=" + cedulaOriginal;

                // Preparamos los datos para la función JS de editar.
                String datosEditar = String.format("'%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s'",
                    cedulaOriginal,
                    f.get("puesto") != null ? f.get("puesto").toString().replace("'", "\\'") : "",
                    f.get("primerNombre") != null ? f.get("primerNombre").toString().replace("'", "\\'") : "",
                    f.get("segundoNombre") != null ? f.get("segundoNombre").toString().replace("'", "\\'") : "",
                    f.get("primerApellido") != null ? f.get("primerApellido").toString().replace("'", "\\'") : "",
                    f.get("segundoApellido") != null ? f.get("segundoApellido").toString().replace("'", "\\'") : "",
                    f.get("vehiculo") != null ? f.get("vehiculo").toString().replace("'", "\\'") : "",
                    f.get("placa") != null ? f.get("placa").toString().replace("'", "\\'") : ""
                );

                out.println("    <td>");
                out.println("        <a href=\"#\" onclick=\"cargarDatosParaEditar(" + datosEditar + ")\" style='margin-right:10px;'>Editar</a>");
                out.println("        <a href=\"" + urlEliminar + "\" onclick=\"return confirm('¿Estás seguro de que quieres eliminar a este funcionario?');\">Eliminar</a>");
                out.println("    </td>");

                // ****** FIN DE LA SECCIÓN QUE FALTABA ******

                out.println("</tr>");
            }
        } else {
            out.println("<tr><td colspan=\"6\" style=\"text-align:center;\">No hay funcionarios registrados.</td></tr>");
        }

        out.println("          </tbody></table></section>");
        
        // --- Footer y JavaScript (COMPLETO) ---
        out.println("  <footer class=\"footer\">© 2025 Seguridad y Tecnología • Todos los derechos reservados</footer>");
        
        out.println("<script>");
        out.println("const form = document.getElementById('formFuncionario');");
        out.println("const btnAgregar = document.getElementById('btnAgregar');");
        out.println("const btnEditarForm = document.getElementById('btnEditarForm');");
        out.println("const formTitulo = document.getElementById('form-titulo');");

        out.println("function cargarDatosParaEditar(cedula, rol, nombre1, nombre2, apellido1, apellido2, tipoVehiculo, placa) {");
        out.println("   document.getElementById('cedulaOriginal').value = cedula;");
        out.println("   document.getElementById('identificacion').value = cedula;");
        out.println("   document.getElementById('rolFuncionario').value = rol;");
        out.println("   document.getElementById('primerNombre').value = nombre1;");
        out.println("   document.getElementById('segundoNombre').value = nombre2 || '';");
        out.println("   document.getElementById('primerApellido').value = apellido1;");
        out.println("   document.getElementById('segundoApellido').value = apellido2;");
        out.println("   document.getElementById('tipoVehiculo').value = tipoVehiculo;");
        out.println("   document.getElementById('placa').value = placa || '';");
        out.println("   form.action = 'editarFuncionario';");
        out.println("   formTitulo.innerText = 'Editando Funcionario: ' + nombre1 + ' ' + apellido1;");
        out.println("   btnAgregar.style.display = 'none';");
        out.println("   btnEditarForm.style.display = 'inline-block';");
        out.println("   window.scrollTo(0, 0);");
        out.println("}");

        out.println("function limpiarYResetearForm() {");
        out.println("   form.reset();");
        out.println("   form.action = 'agregarFuncionario';");
        out.println("   formTitulo.innerText = 'Registrar Funcionario';");
        out.println("   btnAgregar.style.display = 'inline-block';");
        out.println("   btnEditarForm.style.display = 'none';");
        out.println("}");
        
        out.println("document.addEventListener('DOMContentLoaded', function() { limpiarYResetearForm(); });");
        out.println("</script>");
        
        out.println("</body></html>");
    }
}