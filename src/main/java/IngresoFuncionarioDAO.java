import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IngresoFuncionarioDAO {

    private Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/proyecto1?verifyServerCertificate=false&useSSL=true",
                "root", "1234");
    }

    public void agregarIngreso(String cedula, java.sql.Date fechaIngreso, java.sql.Time horaIngreso, String nombreUsuarioGuarda) throws SQLException, ClassNotFoundException {
        Connection con = null;
        PreparedStatement pstmt = null;
        String sql = "INSERT INTO ingresos (cedula, fecha, hora, nombre_usuario_guarda, tipoIngreso) VALUES (?, ?, ?, ?, ?)";

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, cedula);
            pstmt.setDate(2, fechaIngreso);
            pstmt.setTime(3, horaIngreso);
            pstmt.setString(4, nombreUsuarioGuarda);
            pstmt.setString(5, "Funcionario");
            pstmt.executeUpdate();
            System.out.println("Ingreso para cédula '" + cedula + "' agregado correctamente.");
        } finally {
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (con != null) try { con.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public void eliminarIngreso(int id) throws SQLException, ClassNotFoundException {
        Connection con = null;
        PreparedStatement pstmt = null;
        String sql = "DELETE FROM ingresos WHERE id = ?";

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, id);
            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("Ingreso con ID '" + id + "' eliminado correctamente.");
            } else {
                System.out.println("No se encontró ingreso con ID '" + id + "'.");
            }
        } finally {
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (con != null) try { con.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public List<Map<String, Object>> obtenerTodosLosIngresos() throws SQLException, ClassNotFoundException {
        List<Map<String, Object>> listaIngresos = new ArrayList<>();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "SELECT i.*, p.nombre1, p.apellido1, p.placaVehiculo, v.tipoVehiculo " +
                     "FROM ingresos i " +
                     "LEFT JOIN personas p ON i.cedula = p.cedula " +
                     "LEFT JOIN vehiculos v ON p.placaVehiculo = v.placa " +
                     "WHERE i.tipoIngreso = 'Funcionario'";

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Map<String, Object> ingreso = new HashMap<>();
                ingreso.put("id", rs.getInt("id"));
                ingreso.put("cedula", rs.getString("cedula"));
                ingreso.put("fecha", rs.getDate("fecha"));
                ingreso.put("hora", rs.getTime("hora"));
                ingreso.put("nombre_usuario_guarda", rs.getString("nombre_usuario_guarda"));
                ingreso.put("tipoIngreso", rs.getString("tipoIngreso"));
                ingreso.put("placa", rs.getString("placaVehiculo"));
                ingreso.put("nombre_funcionario", rs.getString("nombre1") + " " + rs.getString("apellido1"));
                ingreso.put("tipo_vehiculo", rs.getString("tipoVehiculo"));
                listaIngresos.add(ingreso);
            }
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (con != null) try { con.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return listaIngresos;
    }
}