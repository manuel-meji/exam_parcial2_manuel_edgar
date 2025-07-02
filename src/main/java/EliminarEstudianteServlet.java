import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import modelo.EstudianteDAO;

@WebServlet("/eliminarEstudiante")
public class EliminarEstudianteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String carnet = request.getParameter("carnet");
        if (carnet != null && !carnet.trim().isEmpty()) {
            EstudianteDAO dao = new EstudianteDAO();
            try {
                dao.eliminarEstudiante(carnet);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        response.sendRedirect(request.getContextPath() + "/panelEstudiantes");
    }
}