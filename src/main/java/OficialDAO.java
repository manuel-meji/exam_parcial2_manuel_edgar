
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    public List<Map<String, Object>> obtenerTodosLosOficiales(String busqueda) throws SQLException, ClassNotFoundException {
        List<Map<String, Object>> listaOficiales = new ArrayList<>();
        Connection con = null;
        PreparedStatement pstmt = null; // Cambiado a PreparedStatement por seguridad
        ResultSet rs = null;

        // La consulta base
        String sql = "SELECT * FROM usuarios WHERE tipoUsuario='Guarda'";

        // Si se proporciona un término de búsqueda, se añade la condición a la consulta
        if (busqueda != null && !busqueda.trim().isEmpty()) {
            sql += " AND (nombre1 LIKE ? OR apellido1 LIKE ? OR cedula LIKE ? OR nombreUsuario LIKE ?)";
        }

        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/proyecto1?verifyServerCertificate=false&useSSL=true",
                    "root",
                    "1234"); 

            pstmt = con.prepareStatement(sql);

            // Si hay un término de búsqueda, se asignan los valores a los placeholders '?'
            if (busqueda != null && !busqueda.trim().isEmpty()) {
                String parametroLike = "%" + busqueda + "%";
                pstmt.setString(1, parametroLike);
                pstmt.setString(2, parametroLike);
                pstmt.setString(3, parametroLike);
                pstmt.setString(4, parametroLike);
            }

            rs = pstmt.executeQuery();

            while (rs.next()) {
                Map<String, Object> fila = new HashMap<>();
                fila.put("nombre1", rs.getString("nombre1"));
                fila.put("nombre2", rs.getString("nombre2"));
                fila.put("apellido1", rs.getString("apellido1"));
                fila.put("apellido2", rs.getString("apellido2"));
                fila.put("cedula", rs.getString("cedula"));
                fila.put("numeroTelefono", rs.getString("numeroTelefono"));
                fila.put("nombreUsuario", rs.getString("nombreUsuario"));
                fila.put("contraseña", rs.getString("contraseña"));
                listaOficiales.add(fila);
            }
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close(); // Se cierra el PreparedStatement
            if (con != null) con.close();
        }
        return listaOficiales;
    }

    public void eliminarOficial(String nombreUsuario) throws SQLException, ClassNotFoundException {
        Connection con = null;
        PreparedStatement pstmt = null;

        // La consulta SQL usa '?' para la seguridad
        String sql = "DELETE FROM usuarios WHERE nombreUsuario = ? AND tipoUsuario = 'Guarda'";

        try {
            // Usamos tu método de conexión
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/proyecto1?verifyServerCertificate=false&useSSL=true",
                    "root",
                    "1234"); // ¡TU CONTRASEÑA!

            pstmt = con.prepareStatement(sql);
            // Asignamos el nombre de usuario de forma segura al placeholder
            pstmt.setString(1, nombreUsuario);

            // executeUpdate() devuelve el número de filas afectadas.
            // Podemos usarlo para saber si realmente se borró algo.
            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Oficial con usuario '" + nombreUsuario + "' eliminado correctamente.");
            } else {
                System.out.println("No se encontró ningún oficial con el usuario '" + nombreUsuario + "' para eliminar.");
            }

        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }

    public void editarOficial(String nombre1, String nombre2, String apellido1, String apellido2, String telefono, String nombreUsuarioNuevo, String contrasena, String id, String nombreUsuarioOriginal) throws SQLException, ClassNotFoundException {

        Connection con = null;
        PreparedStatement pstmt = null;

        // La consulta UPDATE con '?' para máxima seguridad
        String sql = "UPDATE usuarios SET cedula = ?, nombre1 = ?, nombre2 = ?, "
                + "apellido1 = ?, apellido2 = ?, numeroTelefono = ?, "
                + "nombreUsuario = ?, contraseña = ? "
                + "WHERE nombreUsuario = ?";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/proyecto1?verifyServerCertificate=false&useSSL=true",
                    "root",
                    "1234"); // ¡TU CONTRASEÑA!

            pstmt = con.prepareStatement(sql);

            // Asignamos todos los parámetros a los placeholders
            pstmt.setString(1, id);
            pstmt.setString(2, nombre1);
            pstmt.setString(3, nombre2);
            pstmt.setString(4, apellido1);
            pstmt.setString(5, apellido2);
            pstmt.setString(6, telefono);
            pstmt.setString(7, nombreUsuarioNuevo);
            pstmt.setString(8, contrasena);
            pstmt.setString(9, nombreUsuarioOriginal); // El WHERE clause

            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("Oficial '" + nombreUsuarioOriginal + "' editado correctamente.");
            } else {
                System.out.println("No se encontró al oficial '" + nombreUsuarioOriginal + "' para editar.");
            }

        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }
}
