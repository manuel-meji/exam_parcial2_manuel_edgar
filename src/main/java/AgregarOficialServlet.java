
import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// Importamos la clase DAO

// La anotación @WebServlet mapea este servlet a la URL "/agregarOficial"
// que pusimos en el 'action' del formulario HTML.
@WebServlet("/agregarOficial")
public class AgregarOficialServlet extends HttpServlet {

    // El formulario envía los datos con el método POST, así que implementamos doPost.
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Recoger los datos del formulario usando los atributos 'name'
        String nombre1 = request.getParameter("nombre1");
        String nombre2 = request.getParameter("nombre2");
        String apellido1 = request.getParameter("apellido1");
        String apellido2 = request.getParameter("apellido2");
        String cedula = request.getParameter("cedula");
        String telefono = request.getParameter("telefono");
        String nombreUsuario = request.getParameter("nombreUsuario");
        String contrasena = request.getParameter("contrasena");

        // (Opcional) Imprimir en la consola del servidor para verificar que llegan los datos
        System.out.println("Datos recibidos: " + cedula + ", " + nombre1);

        // 2. Crear una instancia del DAO y llamar al método para insertar
        OficialDAO dao = new OficialDAO();
        try {
            dao.agregarOficial(nombre1, nombre2, apellido1, apellido2, cedula, telefono, nombreUsuario, contrasena);

            // 3. Si la inserción fue exitosa, redirigimos al usuario de vuelta
            // a la página del formulario. El navegador recargará la página.
            response.sendRedirect("panelOficialesAdmin.html?estado=exito");

        } catch (SQLException e) {
            // Si ocurre un error en la base de datos...
            e.printStackTrace(); // Imprime el error completo en la consola del servidor (muy útil para depurar)

            // Redirigimos a la página de nuevo pero con un indicador de error
            response.sendRedirect("panelOficialesAdmin.html?estado=error");
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
