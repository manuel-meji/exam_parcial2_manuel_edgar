
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



@WebServlet("/editarFuncionario")
public class EditarFuncionarioServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String rol = request.getParameter("rolFuncionario");
        String nombre1 = request.getParameter("primerNombre");
        String nombre2 = request.getParameter("segundoNombre");
        String apellido1 = request.getParameter("primerApellido");
        String apellido2 = request.getParameter("segundoApellido");
        String identificacionNueva = request.getParameter("identificacion");
        String tipoVehiculo = request.getParameter("tipoVehiculo");
        String placaNueva = request.getParameter("placa");
        String cedulaOriginal = request.getParameter("cedulaOriginal"); // Del campo oculto

        FuncionarioDAO dao = new FuncionarioDAO();
        try {
            dao.editarFuncionario(rol, nombre1, nombre2, apellido1, apellido2, identificacionNueva, tipoVehiculo, placaNueva, cedulaOriginal);
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect(request.getContextPath() + "/panelFuncionarios");
    }
}
