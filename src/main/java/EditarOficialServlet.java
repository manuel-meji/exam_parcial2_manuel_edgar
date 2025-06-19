
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/editarOficial")
public class EditarOficialServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obtenemos todos los datos del formulario
        String nombre1 = request.getParameter("nombre1");
        String nombre2 = request.getParameter("nombre2");
        String apellido1 = request.getParameter("apellido1");
        String apellido2 = request.getParameter("apellido2");
        String cedula = request.getParameter("cedula");
        String telefono = request.getParameter("telefono");
        String nombreUsuarioNuevo = request.getParameter("nombreUsuario");
        String contrasena = request.getParameter("contrasena");

        // ¡CRUCIAL! Obtenemos el nombre de usuario original del campo oculto
        String nombreUsuarioOriginal = request.getParameter("nombreUsuarioOriginal");

        OficialDAO dao = new OficialDAO();
        try {
            dao.editarOficial(nombre1, nombre2, apellido1, apellido2, telefono, nombreUsuarioNuevo, contrasena, cedula, nombreUsuarioOriginal);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Después de editar, redirigimos a la página principal para ver los cambios
        response.sendRedirect(request.getContextPath() + "/gestionOficiales");
    }
}
