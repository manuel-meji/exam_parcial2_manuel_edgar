import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet ("/capturarDatos")
public class capturarDatos extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
	    
       
        String usuario;
        String clave;
        String query="";
        PrintWriter pw; //objeto que se utiliza para enviarle la respuesta al usuario.
        
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        res.setContentType( "text/html" );  // se le indica al navegador el tipo de contenido que tendr� la respuesta que 
        									//se enviar� al cliente.
        pw = res.getWriter(); // se crear el objeto para la enviar la respuesta.

        try{         		
        	usuario = req.getParameter("nombre"); //recibe el usuario de la p�gina index.html.
        	clave = req.getParameter("contraseña"); // recibe la clave de la p�gina index.html.
        	           
        	Class.forName("com.mysql.jdbc.Driver"); 
        	        	        	  
      	    con = DriverManager.getConnection("jdbc:mysql://localhost:3306/proyecto1?verifyServerCertificate=false&useSSL=true", "root", "Manuel2004");
        	con.setAutoCommit(true);
        	   
        	System.out.println("Conexi�n exitosa...");
 
        	stmt = con.createStatement();
        	
        	if (!usuario.equals(""))
        		query = "select * from usuarios where nombreUsuario='"+usuario+"' and contraseña='"+clave+"'";
        	else
        		query = "select * from Usuarios";

        	System.err.println(query);
        	
	        rs = stmt.executeQuery(query);
        	
	        // el siguiente c�digo edita una p�gina html, para enviar a desplegar al usuario el resultado.
	        pw.println("<HTML><HEAD><TITLE>Leyendo par�metros</TITLE></HEAD>");
        	pw.println("<BODY BGCOLOR=\"#CCBBAA\">");
        	pw.println("<H2>Leyendo los datos de la tabla USUARIOS<H2><P>");
        	pw.println("<UL>\n");
        	pw.println("Usuario		Clave<br><br>");
        	while (rs.next()) //recorre la tabla y env�a el resultado del contenido de la misma.
	            {
	        		pw.println(rs.getString("Usuario") +  "  &nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp&nbsp;&nbsp " + rs.getString("Clave"));
	        		pw.println("<br><br>");
	            }          	
        		pw.println("</BODY></HTML>");
        		pw.close();
	         } 
        catch (Exception e) {
	             e.printStackTrace();
	             System.out.println("Error de seguimiento en getConnection() : " + e.getMessage());
	         }
	} 
}	