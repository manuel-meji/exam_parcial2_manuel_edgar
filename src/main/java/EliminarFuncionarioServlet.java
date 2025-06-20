import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 // Ajusta esta ruta

@WebServlet("/eliminarFuncionario")
public class EliminarFuncionarioServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cedula = request.getParameter("cedula");

        if (cedula != null && !cedula.trim().isEmpty()) {
            FuncionarioDAO dao = new FuncionarioDAO();
            try {
                // El servlet solo necesita llamar a este método.
                // No le importa que por dentro se hagan 3 operaciones y una transacción.
                dao.eliminarFuncionario(cedula);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Redirigimos de vuelta para ver el resultado.
        response.sendRedirect(request.getContextPath() + "/panelFuncionarios");
    }
}
