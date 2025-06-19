
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// Importamos el DAO


// La URL a la que responderá este servlet.
@WebServlet("/gestionOficiales")
public class GestionOficialesServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // Este método se ejecutará cuando se llame al servlet desde el menú (GET).
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        OficialDAO dao = new OficialDAO();
        List<Map<String, Object>> listaOficiales = null;

        try {
            // 1. Obtenemos la lista de oficiales de la BD
            listaOficiales = dao.obtenerTodosLosOficiales();
        } catch (Exception e) {
            e.printStackTrace();
            // Podríamos mostrar un mensaje de error en la página
        }

        // 2. Imprimimos TODO el HTML de la página
        out.println("<!DOCTYPE html>");
        out.println("<html lang=\"es\">");
        out.println("<head>");
        out.println("    <meta charset=\"UTF-8\">");
        out.println("    <title>Panel de Administrador</title>");
        out.println("    <link rel=\"stylesheet\" href=\"estilosPOA.css\">");
        out.println("    <link href=\"https://fonts.googleapis.com/css2?family=Inter:wght@400;600;700&display=swap\" rel=\"stylesheet\">");
        out.println("</head>");
        out.println("<body>");
        out.println("    <header class=\"navbar\">");
        out.println("        <h1 class=\"logo\">Administración</h1>");
        out.println("        <nav>");
        out.println("            <ul>");
        out.println("                <li class=\"active\"><a href=\"gestionOficiales\">Oficiales</a></li>");
        // Enlace al otro servlet
        out.println("                <li><a href=\"panelEstudiantes\">Estudiantes</a></li>");
        out.println("            </ul>");
        out.println("        </nav>");
        out.println("    </header>");

        out.println("    <div class=\"container\">");
        out.println("        <div class=\"main-content\">");
        // El action del formulario apunta al servlet que GUARDA los datos
        out.println("            <form class=\"form-section\" action=\"agregarOficial\" method=\"POST\">");
        out.println("                <h3>Gestión de Oficiales</h3>");
        // ... (aquí irían todos los divs del formulario: nombre1, nombre2, etc.) ...
        // Este código es idéntico al del HTML
        out.println("                <div class=\"form-group\"><label for=\"nombre1\">Primer nombre</label><input type=\"text\" id=\"nombre1\" name=\"nombre1\" required></div>");
        out.println("                <div class=\"form-group\"><label for=\"nombre2\">Segundo nombre</label><input type=\"text\" id=\"nombre2\" name=\"nombre2\"></div>");
        out.println("                <div class=\"form-group\"><label for=\"apellido1\">Primer apellido</label><input type=\"text\" id=\"apellido1\" name=\"apellido1\" required></div>");
        out.println("                <div class=\"form-group\"><label for=\"apellido2\">Segundo apellido</label><input type=\"text\" id=\"apellido2\" name=\"apellido2\"></div>");
        out.println("                <div class=\"form-group\"><label for=\"identificacion\">Número de identificación</label><input type=\"text\" id=\"identificacion\" name=\"cedula\" required></div>");
        out.println("                <div class=\"form-group\"><label for=\"telefono\">Número de teléfono</label><input type=\"text\" id=\"telefono\" name=\"telefono\" required></div>");
        out.println("                <div class=\"form-group\"><label for=\"userName\">Nombre de usuario</label><input type=\"text\" id=\"userName\" name=\"nombreUsuario\" required></div>");
        out.println("                <div class=\"form-group\"><label for=\"contrasena\">Contraseña</label><input type=\"password\" id=\"contrasena\" name=\"contrasena\" required></div>");
        out.println("                <div class=\"footer-buttons\"><button type=\"button\" class=\"clear-button\" onclick=\"limpiarCampos()\">Limpiar</button><button type=\"submit\" class=\"add-button\">Agregar</button></div>");
        out.println("                <div class=\"footer-buttons\"><button type=\"button\" class=\"edit-button\">Editar</button><button type=\"button\" class=\"delete-button\">Eliminar</button></div>");
        out.println("            </form>");

        out.println("            <div class=\"table-section\">");
        out.println("                <table>");
        out.println("                    <thead><tr><th>Nombre</th><th>ID</th><th>Teléfono</th><th>Usuario</th></tr></thead>");
        out.println("                    <tbody>");

        // 3. LA PARTE DINÁMICA: Generamos las filas de la tabla
        if (listaOficiales != null) {
            for (Map<String, Object> oficial : listaOficiales) {
                String nombreCompleto = oficial.get("nombre1") + " " + oficial.get("apellido1") + " " + oficial.get("apellido2");
                out.println("<tr>");
                out.println("    <td>" + nombreCompleto + "</td>");
                out.println("    <td>" + oficial.get("cedula") + "</td>");
                out.println("    <td>" + oficial.get("numeroTelefono") + "</td>");
                out.println("    <td>" + oficial.get("nombreUsuario") + "</td>");
                out.println("</tr>");
            }
        }

        out.println("                    </tbody>");
        out.println("                </table>");
        out.println("            </div>");
        out.println("        </div>");
        out.println("    </div>");
        out.println("    <footer class=\"footer\">© 2025 Seguridad y Tecnología • Todos los derechos reservados</footer>");
        out.println("    <script>function limpiarCampos() { const campos = ['nombre1', 'nombre2', 'apellido1', 'apellido2', 'identificacion', 'telefono', 'userName', 'contrasena']; campos.forEach(id => document.getElementById(id).value = ''); }</script>");
        out.println("</body>");
        out.println("</html>");
    }
}
