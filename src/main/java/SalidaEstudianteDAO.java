import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SalidaEstudianteDAO {

    private Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/proyecto1?verifyServerCertificate=false&useSSL=true",
                "root", "1234");
    }

    public void agregarSalida(String carnet, java.sql.Date fecha, java.sql.Time hora, String motivo, String nombreUsuarioGuarda) throws SQLException, ClassNotFoundException {
        Connection con = null;
        PreparedStatement pstmt = null;
        String sql = "INSERT INTO salidas_estudiantes (carnet, fecha, hora, motivo, nombre_usuario_guarda) VALUES (?, ?, ?, ?, ?)";

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, carnet);
            pstmt.setDate(2, fecha);
            pstmt.setTime(3, hora);
            pstmt.setString(4, motivo);
            pstmt.setString(5, nombreUsuarioGuarda);
            pstmt.executeUpdate();
            System.out.println("Salida para carnet '" + carnet + "' agregada correctamente.");
        } finally {
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (con != null) try { con.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public void eliminarSalida(int id) throws SQLException, ClassNotFoundException {
        Connection con = null;
        PreparedStatement pstmt = null;
        String sql = "DELETE FROM salidas_estudiantes WHERE id = ?";

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, id);
            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("Salida con ID '" + id + "' eliminada correctamente.");
            } else {
                System.out.println("No se encontró salida con ID '" + id + "'.");
            }
        } finally {
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (con != null) try { con.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public List<Map<String, Object>> obtenerTodasLasSalidas() throws SQLException, ClassNotFoundException {
        List<Map<String, Object>> listaSalidas = new ArrayList<>();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM salidas_estudiantes";

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Map<String, Object> salida = new HashMap<>();
                salida.put("id", rs.getInt("id"));
                salida.put("carnet", rs.getString("carnet"));
                salida.put("fecha", rs.getDate("fecha"));
                salida.put("hora", rs.getTime("hora"));
                salida.put("motivo", rs.getString("motivo"));
                salida.put("nombre_usuario_guarda", rs.getString("nombre_usuario_guarda"));
                listaSalidas.add(salida);
            }
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (con != null) try { con.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return listaSalidas;
    }
}