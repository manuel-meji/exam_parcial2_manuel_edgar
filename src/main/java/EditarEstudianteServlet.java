import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/editarEstudiante")
public class EditarEstudianteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String carnet = request.getParameter("carnet");
        String nombre1 = request.getParameter("nombre1");
        String nombre2 = request.getParameter("nombre2");
        String apellido1 = request.getParameter("apellido1");
        String apellido2 = request.getParameter("apellido2");
        String cedula = request.getParameter("cedula");
        String nacionalidad = request.getParameter("nacionalidad");
        String direccion = request.getParameter("direccion");
        String fechaNacimientoStr = request.getParameter("fechaNacimiento");
        String carnetOriginal = request.getParameter("carnetOriginal");

        Date fechaNacimiento = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            java.util.Date parsedDate = sdf.parse(fechaNacimientoStr);
            fechaNacimiento = new Date(parsedDate.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/panelEstudiantes");
            return;
        }

        EstudianteDAO dao = new EstudianteDAO();
        try {
            dao.editarEstudiante(carnet, nombre1, nombre2, apellido1, apellido2, cedula, nacionalidad, direccion, fechaNacimiento, carnetOriginal);
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.sendRedirect(request.getContextPath() + "/panelEstudiantes");
    }
}