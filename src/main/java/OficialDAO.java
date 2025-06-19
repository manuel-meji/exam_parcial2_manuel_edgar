
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
                    "messi.34.ed*");

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
}
