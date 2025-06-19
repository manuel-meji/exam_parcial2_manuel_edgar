
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



@WebServlet("/eliminarOficial")
public class EliminarOficialServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. Obtenemos el nombre de usuario que viene en la URL
        // El nombre del parámetro ("usuario") debe coincidir con el del enlace que creamos.
        String nombreUsuarioParaEliminar = request.getParameter("usuario");

        if (nombreUsuarioParaEliminar != null && !nombreUsuarioParaEliminar.trim().isEmpty()) {
            OficialDAO dao = new OficialDAO();
            try {
                // 2. Llamamos al método del DAO para eliminar
                dao.eliminarOficial(nombreUsuarioParaEliminar);
            } catch (Exception e) {
                e.printStackTrace();
                // En un proyecto real, aquí manejarías el error de forma más elegante.
            }
        }

        // 3. Haya funcionado o no, redirigimos de vuelta a la página principal
        // para que la tabla se refresque y el usuario vea el resultado.
        response.sendRedirect(request.getContextPath() + "/gestionOficiales");
    }
}
