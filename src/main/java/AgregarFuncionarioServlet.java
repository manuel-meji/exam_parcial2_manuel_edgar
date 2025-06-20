import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 // Ajusta esta ruta

@WebServlet("/agregarFuncionario")
public class AgregarFuncionarioServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String rol = request.getParameter("rolFuncionario");
        String nombre1 = request.getParameter("primerNombre");
        String nombre2 = request.getParameter("segundoNombre");
        String apellido1 = request.getParameter("primerApellido");
        String apellido2 = request.getParameter("segundoApellido");
        String identificacion = request.getParameter("identificacion");
        String tipoVehiculo = request.getParameter("tipoVehiculo");
        String placa = request.getParameter("placa");

        FuncionarioDAO dao = new FuncionarioDAO();
        try {
            dao.agregarFuncionario(rol, nombre1, nombre2, apellido1, apellido2, identificacion, tipoVehiculo, placa);
        } catch (Exception e) {
            e.printStackTrace();
            // Podrías redirigir a una página de error o mostrar un mensaje
        }

        response.sendRedirect(request.getContextPath() + "/panelFuncionarios");
    }
}