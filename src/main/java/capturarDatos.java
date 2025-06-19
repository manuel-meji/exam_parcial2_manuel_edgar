
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

@WebServlet("/capturarDatos")
public class capturarDatos extends HttpServlet {

    Connection con = null;
    Statement stmt = null;
    ResultSet rs = null;

    private static final long serialVersionUID = 1L;

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String usuario = req.getParameter("nombre"); // Get username from index.html
        String clave = req.getParameter("clave");   // Get password from index.html

        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/proyecto1?verifyServerCertificate=false&useSSL=true", "root", "messi.34.ed*");
            stmt = con.createStatement();

            String nombreCompleto = loginAdmin(usuario, clave);
            System.out.println(nombreCompleto);
            if (nombreCompleto != null) {
                res.setContentType("text/html");
                PrintWriter out = res.getWriter();
                out.println("<!DOCTYPE html>");
                out.println("<html lang=\"es\">");
                out.println("<head>");
                out.println("    <meta charset=\"UTF-8\">");
                out.println("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
                out.println("    <title>Menú Principal</title>");
                out.println("    <style>");
                out.println("        body {");
                out.println("            margin: 0;");
                out.println("            padding: 0;");
                out.println("            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;");
                out.println("            height: 100vh;");
                out.println("            display: flex;");
                out.println("            justify-content: center;");
                out.println("            align-items: center;");
                out.println("            background: url('jack-b-8Wqm1W59Baw-unsplash.jpg') no-repeat center center/cover;");
                out.println("            position: relative;");
                out.println("        }");
                out.println("        body::before {");
                out.println("            content: '';");
                out.println("            position: absolute;");
                out.println("            top: 0;");
                out.println("            left: 0;");
                out.println("            height: 100%;");
                out.println("            width: 100%;");
                out.println("            background-color: rgba(0, 0, 0, 0.5);");
                out.println("            z-index: 0;");
                out.println("        }");
                out.println("        .menu-container {");
                out.println("            position: relative;");
                out.println("            z-index: 1;");
                out.println("            backdrop-filter: blur(12px);");
                out.println("            background-color: rgba(255, 255, 255, 0.15);");
                out.println("            padding: 40px 60px;");
                out.println("            border-radius: 20px;");
                out.println("            box-shadow: 0 12px 25px rgba(0, 0, 0, 0.3);");
                out.println("            text-align: center;");
                out.println("            border: 1px solid rgba(255, 255, 255, 0.3);");
                out.println("            color: white;");
                out.println("        }");
                out.println("        .menu-container h1 {");
                out.println("            font-size: 32px;");
                out.println("            margin-bottom: 10px;");
                out.println("        }");
                out.println("        .menu-container p {");
                out.println("            font-size: 18px;");
                out.println("            margin-bottom: 30px;");
                out.println("        }");
                out.println("        .menu-container button {");
                out.println("            background: linear-gradient(to right, #0066ff, #00c3ff);");
                out.println("            color: white;");
                out.println("            border: none;");
                out.println("            padding: 15px 30px;");
                out.println("            margin: 15px 0;");
                out.println("            font-size: 18px;");
                out.println("            font-weight: bold;");
                out.println("            border-radius: 12px;");
                out.println("            cursor: pointer;");
                out.println("            transition: background 0.3s ease, transform 0.2s ease;");
                out.println("            width: 100%;");
                out.println("            box-shadow: 0 6px 10px rgba(0, 0, 0, 0.15);");
                out.println("        }");
                out.println("        .menu-container button:hover {");
                out.println("            background: linear-gradient(to right, #0052cc, #00aaff);");
                out.println("            transform: scale(1.05);");
                out.println("        }");
                out.println("    </style>");
                out.println("</head>");
                out.println("<body>");
                out.println("    <div class=\"menu-container\">");
                out.println("        <h1>Bienvenido</h1>");
                out.println("        <p>Hola, <span id=\"nombreUsuario\">" + nombreCompleto + "</span> </p>");
                // AHORA:
                out.println("        <button type=\"button\" onclick=\"window.location.href = 'gestionOficiales'\">");
                out.println("            Mantenimiento de Oficiales");
                out.println("        </button>");
                out.println("        <button type=\"button\" onclick=\"window.location.href='http://localhost:8080/exam_parcial2_manuel_edgar/panelEstudiantes'\">");
                out.println("            Mantenimiento de Estudiantes");
                out.println("        </button>");
                out.println("    </div>");
                out.println("</body>");
                out.println("</html>");
            } else {
                res.sendRedirect("index.html");
            }
        } catch (Exception e) {
            e.printStackTrace();
            res.sendRedirect("index.html");
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public String loginAdmin(String nombreUsuario, String contrasena) {
        try {
            String sql = "SELECT * FROM usuarios WHERE nombreUsuario = '" + nombreUsuario + "'"
                    + " AND tipoUsuario = 'Administrador'";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                String contrasenaDB = rs.getString("contraseña");
                String nombreUsuarioBD = rs.getString("nombreUsuario");
                if (contrasenaDB != null && contrasenaDB.equals(contrasena) && nombreUsuarioBD.equals(nombreUsuario)) {
                    String nombreCompleto = rs.getString("nombre1") + " " + rs.getString("apellido1");
                    return nombreCompleto;
                }
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
