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
        PrintWriter pw; 

        try {
            usuario = req.getParameter("nombre"); // recibe el usuario de la p�gina index.html.
            clave = req.getParameter("clave"); // recibe la clave de la p�gina index.html.

            System.out.println(usuario + " clave: " + clave);

            Class.forName("com.mysql.jdbc.Driver");

            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/proyecto1?verifyServerCertificate=false&useSSL=true", "root", "messi.34.ed*");
            con.setAutoCommit(true);

            System.out.println("Conexi�n exitosa...");
            
            stmt = con.createStatement();

           

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