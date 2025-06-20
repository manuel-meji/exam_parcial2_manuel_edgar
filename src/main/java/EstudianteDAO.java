import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EstudianteDAO {

    private Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/proyecto1?verifyServerCertificate=false&useSSL=true",
                "root", "1234");
    }

    public void agregarEstudiante(String carnet, String nombre1, String nombre2, String apellido1, String apellido2,
                                 String cedula, String nacionalidad, String direccion, java.sql.Date fechaNacimiento) throws SQLException, ClassNotFoundException {
        Connection con = null;
        PreparedStatement pstmt = null;
        String sql = "INSERT INTO estudiantes (carnet, nombre1, nombre2, apellido1, apellido2, cedula, nacionalidad, direccion, fechaNacimiento, edad) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, carnet);
            pstmt.setString(2, nombre1);
            pstmt.setString(3, nombre2 != null ? nombre2 : null);
            pstmt.setString(4, apellido1);
            pstmt.setString(5, apellido2);
            pstmt.setString(6, cedula);
            pstmt.setString(7, nacionalidad != null ? nacionalidad : null);
            pstmt.setString(8, direccion);
            pstmt.setDate(9, fechaNacimiento);
            // Calculate age based on fechaNacimiento
            long age = calculateAge(fechaNacimiento);
            pstmt.setInt(10, (int) age);
            pstmt.executeUpdate();
            System.out.println("Estudiante con carnet '" + carnet + "' agregado correctamente.");
        } finally {
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (con != null) try { con.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public List<Map<String, Object>> obtenerTodosLosEstudiantes() throws SQLException, ClassNotFoundException {
        List<Map<String, Object>> listaEstudiantes = new ArrayList<>();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM estudiantes";

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Map<String, Object> estudiante = new HashMap<>();
                estudiante.put("carnet", rs.getString("carnet"));
                estudiante.put("nombre1", rs.getString("nombre1"));
                estudiante.put("nombre2", rs.getString("nombre2"));
                estudiante.put("apellido1", rs.getString("apellido1"));
                estudiante.put("apellido2", rs.getString("apellido2"));
                estudiante.put("cedula", rs.getString("cedula"));
                estudiante.put("nacionalidad", rs.getString("nacionalidad"));
                estudiante.put("direccion", rs.getString("direccion"));
                estudiante.put("fechaNacimiento", rs.getDate("fechaNacimiento"));
                estudiante.put("edad", rs.getInt("edad"));
                listaEstudiantes.add(estudiante);
            }
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (con != null) try { con.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return listaEstudiantes;
    }

    public void editarEstudiante(String carnet, String nombre1, String nombre2, String apellido1, String apellido2,
                                String cedula, String nacionalidad, String direccion, java.sql.Date fechaNacimiento,
                                String carnetOriginal) throws SQLException, ClassNotFoundException {
        Connection con = null;
        PreparedStatement pstmt = null;
        String sql = "UPDATE estudiantes SET carnet = ?, nombre1 = ?, nombre2 = ?, apellido1 = ?, apellido2 = ?, cedula = ?, nacionalidad = ?, direccion = ?, fechaNacimiento = ?, edad = ? WHERE carnet = ?";

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, carnet);
            pstmt.setString(2, nombre1);
            pstmt.setString(3, nombre2 != null ? nombre2 : null);
            pstmt.setString(4, apellido1);
            pstmt.setString(5, apellido2);
            pstmt.setString(6, cedula);
            pstmt.setString(7, nacionalidad != null ? nacionalidad : null);
            pstmt.setString(8, direccion);
            pstmt.setDate(9, fechaNacimiento);
            long age = calculateAge(fechaNacimiento);
            pstmt.setInt(10, (int) age);
            pstmt.setString(11, carnetOriginal);
            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("Estudiante con carnet '" + carnetOriginal + "' editado correctamente.");
            } else {
                System.out.println("No se encontró estudiante con carnet '" + carnetOriginal + "'.");
            }
        } finally {
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (con != null) try { con.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public void eliminarEstudiante(String carnet) throws SQLException, ClassNotFoundException {
        Connection con = null;
        PreparedStatement pstmt = null;
        String sql = "DELETE FROM estudiantes WHERE carnet = ?";

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, carnet);
            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("Estudiante con carnet '" + carnet + "' eliminado correctamente.");
            } else {
                System.out.println("No se encontró estudiante con carnet '" + carnet + "'.");
            }
        } finally {
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (con != null) try { con.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    private long calculateAge(java.sql.Date fechaNacimiento) {
        if (fechaNacimiento == null) return 0;
        long diffInMillies = new java.util.Date().getTime() - fechaNacimiento.getTime();
        return diffInMillies / (1000L * 60 * 60 * 24 * 365);
    }
}