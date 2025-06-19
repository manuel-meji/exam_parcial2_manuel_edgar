
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OficialDAO {

    /**
     * Inserta un nuevo oficial en la base de datos utilizando tu método de
     * conexión.
     */
    public void agregarOficial(String nombre1, String nombre2, String apellido1, String apellido2, String cedula, String telefono, String nombreUsuario, String contrasena) throws SQLException, ClassNotFoundException {

        Connection con = null;
        PreparedStatement pstmt = null;

        // La consulta SQL con '?' para la seguridad.
        String sql = "INSERT INTO usuarios (nombre1, nombre2, apellido1, apellido2, nombreUsuario, contraseña, numeroTelefono, cedula, tipoUsuario) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            // ==========================================================
            //      AQUÍ EMPIEZA TU CÓDIGO DE CONEXIÓN EXACTO
            // ==========================================================

            // 1. Registrar el driver de MySQL (versión 5.x)
            Class.forName("com.mysql.jdbc.Driver");

            // 2. Establecer la conexión con tu URL y credenciales
            // ¡RECUERDA CAMBIAR "contraseña" POR TU CONTRASEÑA REAL!
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/proyecto1?verifyServerCertificate=false&useSSL=true",
                    "root",
                    "1234");

            // Esta línea no es estrictamente necesaria, ya que true es el valor por defecto, pero la mantenemos.
            con.setAutoCommit(true);

            System.out.println("Conexión exitosa a la base de datos...");

            // ==========================================================
            //      AQUÍ TERMINA TU CÓDIGO DE CONEXIÓN
            // ==========================================================
            // 3. Preparar la consulta usando PreparedStatement (el cambio de seguridad)
            pstmt = con.prepareStatement(sql);

            // 4. Asignar los valores a los placeholders (?)
            pstmt.setString(1, nombre1);
            pstmt.setString(2, nombre2);
            pstmt.setString(3, apellido1);
            pstmt.setString(4, apellido2);
            pstmt.setString(5, nombreUsuario);
            pstmt.setString(6, contrasena);
            pstmt.setString(7, telefono);
            pstmt.setString(8, cedula);
            pstmt.setString(9, "Guarda");

            // 5. Ejecutar la inserción
            pstmt.executeUpdate();

            System.out.println("Oficial '" + nombreUsuario + "' agregado correctamente a la BD.");

        } finally {
            // 6. CERRAR LOS RECURSOS EN EL ORDEN CORRECTO
            // Esto es crucial para no dejar conexiones abiertas.
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (con != null) {
                try {
                    con.close();
                    System.out.println("Conexión cerrada.");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
     public List<Map<String, Object>> obtenerTodosLosOficiales() throws SQLException, ClassNotFoundException {
        List<Map<String, Object>> listaOficiales = new ArrayList<>();
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        String sql = "SELECT * FROM usuarios WHERE tipoUsuario='Guarda'";

        try {
            // Usamos tu método de conexión
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/proyecto1?verifyServerCertificate=false&useSSL=true", 
                    "root", 
                    "messi.34.ed*"); // ¡RECUERDA USAR TU CONTRASEÑA!
            
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);

            // Recorremos los resultados y los guardamos en la lista
            while (rs.next()) {
                Map<String, Object> fila = new HashMap<>();
                fila.put("nombre1", rs.getString("nombre1"));
                fila.put("nombre2", rs.getString("nombre2"));
                fila.put("apellido1", rs.getString("apellido1"));
                fila.put("apellido2", rs.getString("apellido2"));
                fila.put("cedula", rs.getString("cedula"));
                fila.put("numeroTelefono", rs.getString("numeroTelefono"));
                fila.put("nombreUsuario", rs.getString("nombreUsuario"));
                // ¡No pasamos la contraseña por seguridad!
                
                listaOficiales.add(fila);
            }
        } finally {
            // Cerramos todos los recursos
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (con != null) con.close();
        }
        
        return listaOficiales;
    }
}
