// Archivo: GestionOficialesServlet.java (COMPLETO Y MODIFICADO)

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/gestionOficiales")
public class GestionOficialesServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        OficialDAO dao = new OficialDAO();
        List<Map<String, Object>> listaOficiales = null;

        try {
            listaOficiales = dao.obtenerTodosLosOficiales();
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<h1>Error al conectar con la base de datos: " + e.getMessage() + "</h1>");
            return;
        }

        out.println("<!DOCTYPE html><html lang=\"es\"><head>");
        out.println("<meta charset=\"UTF-8\"><title>Panel de Administrador</title>");
        out.println("<link rel=\"stylesheet\" href=\"estilosPOA.css\">");
        out.println("<link href=\"https://fonts.googleapis.com/css2?family=Inter:wght@400;600;700&display=swap\" rel=\"stylesheet\">");
        out.println("</head><body>");
        out.println("<header class=\"navbar\"><h1 class=\"logo\">Administración</h1><nav><ul><li class=\"active\"><a href=\"gestionOficiales\">Oficiales</a></li><li><a href=\"panelEstudiantes\">Estudiantes</a></li></ul></nav></header>");
        out.println("<div class=\"container\"><div class=\"main-content\">");

        // --- FORMULARIO MODIFICADO ---
        // Le añadimos un id="formOficial" y un campo oculto
        out.println("<form class=\"form-section\" id=\"formOficial\" action=\"agregarOficial\" method=\"POST\">");
        out.println("   <input type=\"hidden\" id=\"nombreUsuarioOriginal\" name=\"nombreUsuarioOriginal\">");
        out.println("   <h3 id=\"form-titulo\">Gestión de Oficiales</h3>");
        // ... (el resto de los campos del formulario se quedan igual) ...
        out.println("   <div class=\"form-group\"><label for=\"nombre1\">Primer nombre</label><input type=\"text\" id=\"nombre1\" name=\"nombre1\" required></div>");
        out.println("   <div class=\"form-group\"><label for=\"nombre2\">Segundo nombre</label><input type=\"text\" id=\"nombre2\" name=\"nombre2\"></div>");
        out.println("   <div class=\"form-group\"><label for=\"apellido1\">Primer apellido</label><input type=\"text\" id=\"apellido1\" name=\"apellido1\" required></div>");
        out.println("   <div class=\"form-group\"><label for=\"apellido2\">Segundo apellido</label><input type=\"text\" id=\"apellido2\" name=\"apellido2\"></div>");
        out.println("   <div class=\"form-group\"><label for=\"identificacion\">Número de identificación</label><input type=\"text\" id=\"identificacion\" name=\"cedula\" required></div>");
        out.println("   <div class=\"form-group\"><label for=\"telefono\">Número de teléfono</label><input type=\"text\" id=\"telefono\" name=\"telefono\" required></div>");
        out.println("   <div class=\"form-group\"><label for=\"userName\">Nombre de usuario</label><input type=\"text\" id=\"userName\" name=\"nombreUsuario\" required></div>");
        out.println("   <div class=\"form-group\"><label for=\"contrasena\">Contraseña</label><input type=\"password\" id=\"contrasena\" name=\"contrasena\" required></div>");

        // --- BOTONES DEL FORMULARIO MODIFICADOS ---
        out.println("   <div class=\"footer-buttons\"><button type=\"button\" class=\"clear-button\" onclick=\"limpiarYResetearForm()\">Limpiar</button><button type=\"submit\" class=\"add-button\" id=\"btnAgregar\">Agregar</button></div>");
        out.println("   <div class=\"footer-buttons\"><button type=\"button\" class=\"edit-button\" id=\"btnEditarForm\" onclick=\"document.getElementById('formOficial').submit();\">Guardar Cambios</button></div>");
        out.println("</form>");

        out.println("<div class=\"table-section\"><table>");
        out.println("<thead><tr><th>Nombre</th><th>ID</th><th>Teléfono</th><th>Usuario</th><th>Contraseña</th><th>Acciones</th></tr></thead><tbody>");

        if (listaOficiales != null && !listaOficiales.isEmpty()) {
            for (Map<String, Object> oficial : listaOficiales) {
                String nombreCompleto = oficial.get("nombre1") + " " + oficial.get("apellido1") + " " + oficial.get("apellido2");
                out.println("<tr>");
                out.println("<td>" + nombreCompleto + "</td>");
                out.println("<td>" + oficial.get("cedula") + "</td>");
                out.println("<td>" + oficial.get("numeroTelefono") + "</td>");
                out.println("<td>" + oficial.get("nombreUsuario") + "</td>");
                out.println("<td>" + oficial.get("contraseña") + "</td>");

                // --- CELDA DE ACCIONES MODIFICADA ---
                String nombreUsuario = (String) oficial.get("nombreUsuario");
                String urlEliminar = "eliminarOficial?usuario=" + nombreUsuario;
                // Preparamos los datos para la función JS de editar. ¡Cuidado con las comillas!
                String datosParaEditar = String.format("'%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s'",
                        oficial.get("cedula"), oficial.get("nombre1"), oficial.get("nombre2"), oficial.get("apellido1"), oficial.get("apellido2"),
                        oficial.get("numeroTelefono"), oficial.get("nombreUsuario"), oficial.get("contraseña"), nombreUsuario);

                out.println("<td>");
                out.println("<a href=\"#\" onclick=\"cargarDatosParaEditar(" + datosParaEditar + ")\" style='margin-right:10px;'>Editar</a>");
                out.println("<a href=\"" + urlEliminar + "\" onclick=\"return confirm('¿Estás seguro de que quieres eliminar al usuario \\'" + nombreUsuario + "\\'?');\">Eliminar</a>");
                out.println("</td>");
                out.println("</tr>");
            }
        } else {
            out.println("<tr><td colspan=\"6\" style=\"text-align:center;\">No hay oficiales registrados.</td></tr>");
        }

        out.println("</tbody></table></div></div></div>");
        out.println("<footer class=\"footer\">© 2025 Seguridad y Tecnología • Todos los derechos reservados</footer>");

        // --- SCRIPT DE JAVASCRIPT ---
        out.println("<script>");
        out.println("const form = document.getElementById('formOficial');");
        out.println("const btnAgregar = document.getElementById('btnAgregar');");
        out.println("const btnEditarForm = document.getElementById('btnEditarForm');");
        out.println("const formTitulo = document.getElementById('form-titulo');");

        out.println("function cargarDatosParaEditar(cedula, nombre1, nombre2, apellido1, apellido2, telefono, usuario, contrasena, usuarioOriginal) {");
        out.println("   document.getElementById('identificacion').value = cedula;");
        out.println("   document.getElementById('nombre1').value = nombre1;");
        out.println("   document.getElementById('nombre2').value = nombre2;");
        out.println("   document.getElementById('apellido1').value = apellido1;");
        out.println("   document.getElementById('apellido2').value = apellido2;");
        out.println("   document.getElementById('telefono').value = telefono;");
        out.println("   document.getElementById('userName').value = usuario;");
        out.println("   document.getElementById('contrasena').value = contrasena;");
        out.println("   document.getElementById('nombreUsuarioOriginal').value = usuarioOriginal;");
        out.println("   form.action = 'editarOficial';"); // Cambiamos el action del form
        out.println("   formTitulo.innerText = 'Editando a ' + usuarioOriginal;");
        out.println("   btnAgregar.style.display = 'none';"); // Ocultamos el botón Agregar
        out.println("   btnEditarForm.style.display = 'inline-block';"); // Mostramos el botón Guardar Cambios
        out.println("   window.scrollTo(0, 0);"); // Llevamos la vista al principio de la página
        out.println("}");

        out.println("function limpiarYResetearForm() {");
        out.println("   form.reset();"); // Limpia todos los campos
        out.println("   form.action = 'agregarOficial';"); // Reseteamos el action del form
        out.println("   formTitulo.innerText = 'Gestión de Oficiales';");
        out.println("   btnAgregar.style.display = 'inline-block';"); // Mostramos Agregar
        out.println("   btnEditarForm.style.display = 'none';"); // Ocultamos Guardar Cambios
        out.println("}");

        // Estado inicial del formulario
        out.println("document.addEventListener('DOMContentLoaded', function() { limpiarYResetearForm(); });");
        out.println("</script>");

        out.println("</body></html>");
    }
}
