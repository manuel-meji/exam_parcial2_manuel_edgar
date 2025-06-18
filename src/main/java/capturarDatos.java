import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JOptionPane;

@WebServlet("/capturarDatos")
public class capturarDatos extends HttpServlet {
    Connection con = null;
    Statement stmt = null;
    ResultSet rs = null;

    private static final long serialVersionUID = 1L;

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        System.out.println("hcjhdc");
        String usuario;
        String clave;
        String query = "";
        PrintWriter pw; // objeto que se utiliza para enviarle la respuesta al usuario.

        res.setContentType("text/html"); // se le indica al navegador el tipo de contenido que tendr� la respuesta que
                                         // se enviar� al cliente.
        pw = res.getWriter(); // se crear el objeto para la enviar la respuesta.

        try {
            usuario = req.getParameter("nombre"); // recibe el usuario de la p�gina index.html.
            clave = req.getParameter("clave"); // recibe la clave de la p�gina index.html.

            System.out.println(usuario + " clave: " + clave);

            Class.forName("com.mysql.jdbc.Driver");

            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/proyecto1?verifyServerCertificate=false&useSSL=true", "root", "1234");
            con.setAutoCommit(true);

            System.out.println("Conexi�n exitosa...");
            System.out.println("TAAAAAAAAAAAAAAAAAAAAAAA");
            stmt = con.createStatement();

            System.out.println("TAAAAAAAAAAAAAAAAAAAAAAA");

            if (!usuario.equals("")) {
                query = "select * from usuarios where nombreUsuario='" + usuario + "' and contraseña='" + clave + "'";
                if(loginAdmin(usuario, clave)){
                    res.sendRedirect("menu.html");
                }

            } else {
                query = "select * from Usuarios";

                System.err.println(query);
            }

            rs = stmt.executeQuery(query);

            // el siguiente c�digo edita una p�gina html, para enviar a desplegar al usuario
            // el resultado.
            pw.println("<HTML><HEAD><TITLE>Leyendo par�metros</TITLE></HEAD>");
            pw.println("<BODY BGCOLOR=\"#CCBBAA\">");
            pw.println("<H2>Leyendo los datos de la tabla USUARIOS<H2><P>");
            pw.println("<UL>\n");
            pw.println("Usuario		Clave<br><br>");
            while (rs.next()) // recorre la tabla y env�a el resultado del contenido de la misma.
            {
                pw.println(rs.getString("Usuario") + "  &nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp "
                        + rs.getString("Clave"));
                pw.println("<br><br>");
            }
            pw.println("</BODY></HTML>");
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error de seguimiento en getConnection() : " + e.getMessage());
        }
    }

    public boolean loginAdmin(String nombreUsuario, String contrasena) {
        System.out.println("Holaaaaaaa");
        try {
            String sql = "SELECT * FROM usuarios WHERE nombreUsuario = '" + nombreUsuario + "'"
                    + " AND tipoUsuario = 'Administrador'";

            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                String contrasenaDB = rs.getString("contraseña");
                String nombreUsuarioBD = rs.getString("nombreUsuario");
                if (contrasenaDB != null && contrasenaDB.equals(contrasena) && nombreUsuarioBD.equals(nombreUsuario)) {
                    String nombre1 = rs.getString("nombre1");
                    String nombre2 = rs.getString("nombre2");
                    String apellido1 = rs.getString("apellido1");
                    String apellido2 = rs.getString("apellido2");
                    // sesionIniciadaAdmin = true;
                    return true;
                   
                } else {
                    JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectas");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Usuario no encontrado");
            }

            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al intentar iniciar sesión: " + e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error inesperado: " + e.getMessage());
        }
        return false;
    }
}