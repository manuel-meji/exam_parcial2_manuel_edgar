
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

public class FuncionarioDAO {

    /**
     * MÉTODO IMPLEMENTADO: Inserta un nuevo funcionario y su vehículo en la
     * base de datos utilizando una transacción para garantizar la integridad de
     * los datos.
     */
    public void agregarFuncionario(String rol, String nombre1, String nombre2, String apellido1, String apellido2, String identificacion, String tipoVehiculo, String placa) throws SQLException, ClassNotFoundException {

        Connection con = null;
        PreparedStatement pstmtPersona = null;
        PreparedStatement pstmtVehiculo = null;

        // Las dos sentencias SQL, ahora seguras con '?'
        String sqlPersona = "INSERT INTO personas (nombre1, nombre2, apellido1, apellido2, cedula, ocupacion, placaVehiculo, tipoPersona) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlVehiculo = "INSERT INTO vehiculos (placa, tipoVehiculo) VALUES (?, ?)";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/proyecto1?verifyServerCertificate=false&useSSL=true",
                    "root",
                    "1234"); // ¡TU CONTRASEÑA!

            // --- INICIO DE LA TRANSACCIÓN ---
            con.setAutoCommit(false);

            // 1. Preparar y ejecutar la inserción en la tabla 'vehiculos' (si hay placa)
            // Es preferible insertar primero en la tabla que no tiene claves foráneas.
            if (placa != null && !placa.trim().isEmpty()) {
                pstmtVehiculo = con.prepareStatement(sqlVehiculo);
                pstmtVehiculo.setString(1, placa);
                pstmtVehiculo.setString(2, tipoVehiculo);
                pstmtVehiculo.executeUpdate();
                System.out.println("Vehículo con placa '" + placa + "' insertado.");
            }

            // 2. Preparar y ejecutar la inserción en la tabla 'personas'
            pstmtPersona = con.prepareStatement(sqlPersona);
            pstmtPersona.setString(1, nombre1);
            pstmtPersona.setString(2, nombre2);
            pstmtPersona.setString(3, apellido1);
            pstmtPersona.setString(4, apellido2);
            pstmtPersona.setString(5, identificacion);
            pstmtPersona.setString(6, rol);
            pstmtPersona.setString(7, placa); // Se inserta la placa asociada
            pstmtPersona.setString(8, "Funcionario"); // tipoPersona fijo
            pstmtPersona.executeUpdate();
            System.out.println("Persona (Funcionario) '" + nombre1 + " " + apellido1 + "' insertada.");

            // --- FIN DE LA TRANSACCIÓN ---
            // Si todo ha ido bien hasta aquí, confirmamos los cambios.
            con.commit();
            System.out.println("Transacción completada exitosamente.");

        } catch (SQLException e) {
            System.out.println("¡ERROR! Ocurrió un problema durante la transacción. Revirtiendo cambios...");
            e.printStackTrace();
            // Si algo falla, revertimos TODOS los cambios hechos dentro de esta transacción.
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            // Relanzamos la excepción para que el servlet sepa que algo falló
            throw e;
        } finally {
            // Cerramos todos los recursos y restauramos el auto-commit
            if (pstmtPersona != null) {
                pstmtPersona.close();
            }
            if (pstmtVehiculo != null) {
                pstmtVehiculo.close();
            }
            if (con != null) {
                con.setAutoCommit(true); // Dejamos la conexión como estaba
                con.close();
            }
        }
    }

        public List<Map<String, Object>> obtenerTodosLosFuncionarios() throws SQLException, ClassNotFoundException {
        List<Map<String, Object>> listaFuncionarios = new ArrayList<>();
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        // --- ESTA ES LA NUEVA CONSULTA CON LEFT JOIN ---
        // Seleccionamos campos de ambas tablas.
        // p.* significa "todos los campos de la tabla personas".
        // v.tipoVehiculo significa "el campo tipoVehiculo de la tabla vehiculos".
        String sql = "SELECT p.*, v.tipoVehiculo " +
                     "FROM personas p " +
                     "LEFT JOIN vehiculos v ON p.placaVehiculo = v.placa " +
                     "WHERE p.tipoPersona = 'Funcionario'";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/proyecto1?verifyServerCertificate=false&useSSL=true", 
                    "root", 
                    "1234"); // ¡TU CONTRASEÑA!
            
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Map<String, Object> fila = new HashMap<>();
                
                // Obtenemos los datos de la tabla 'personas' (alias 'p')
                fila.put("puesto", rs.getString("ocupacion"));
                fila.put("nombreCompleto", rs.getString("nombre1") + " " + rs.getString("apellido1"));
                fila.put("id", rs.getString("cedula"));
                fila.put("placa", rs.getString("placaVehiculo"));
                
                // --- MANEJO DE DATOS DEL JOIN ---
                // Obtenemos el tipo de vehículo de la tabla 'vehiculos' (alias 'v')
                String tipoVehiculo = rs.getString("tipoVehiculo");
                
                // Si el funcionario no tiene vehículo, el tipoVehiculo será null.
                // Lo manejamos para mostrar un texto amigable.
                if (tipoVehiculo == null || tipoVehiculo.trim().isEmpty()) {
                    fila.put("vehiculo", "No registra");
                } else {
                    fila.put("vehiculo", tipoVehiculo);
                }
                
                // Añadimos también todos los datos individuales para la función de editar
                fila.put("primerNombre", rs.getString("nombre1"));
                fila.put("segundoNombre", rs.getString("nombre2"));
                fila.put("primerApellido", rs.getString("apellido1"));
                fila.put("segundoApellido", rs.getString("apellido2"));
                
                listaFuncionarios.add(fila);
            }
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (con != null) con.close();
        }
        
        return listaFuncionarios;
    }

        public void eliminarFuncionario(String cedula) throws SQLException, ClassNotFoundException {
        Connection con = null;
        PreparedStatement pstmtSelectPlaca = null;
        PreparedStatement pstmtDeletePersona = null;
        PreparedStatement pstmtDeleteVehiculo = null;
        ResultSet rs = null;
        String placaParaBorrar = null;

        String sqlSelectPlaca = "SELECT placaVehiculo FROM personas WHERE cedula = ?";
        String sqlDeletePersona = "DELETE FROM personas WHERE cedula = ? AND tipoPersona = 'Funcionario'";
        String sqlDeleteVehiculo = "DELETE FROM vehiculos WHERE placa = ?";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/proyecto1?verifyServerCertificate=false&useSSL=true", 
                    "root", 
                    "1234"); // ¡TU CONTRASEÑA!

            // --- INICIO DE LA TRANSACCIÓN ---
            con.setAutoCommit(false);
            System.out.println("Transacción iniciada para eliminar al funcionario con cédula: " + cedula);

            // PASO 1: Averiguar la placa del funcionario antes de borrarlo.
            pstmtSelectPlaca = con.prepareStatement(sqlSelectPlaca);
            pstmtSelectPlaca.setString(1, cedula);
            rs = pstmtSelectPlaca.executeQuery();
            if (rs.next()) {
                placaParaBorrar = rs.getString("placaVehiculo");
            }

            // PASO 2: Eliminar el registro de la tabla 'personas'.
            pstmtDeletePersona = con.prepareStatement(sqlDeletePersona);
            pstmtDeletePersona.setString(1, cedula);
            int filasPersona = pstmtDeletePersona.executeUpdate();
            if (filasPersona > 0) {
                System.out.println("Registro de persona eliminado exitosamente.");
            } else {
                 System.out.println("No se encontró a la persona para eliminar. Revirtiendo transacción.");
                 con.rollback(); // Si no se encuentra, no tiene sentido continuar.
                 return;
            }

            // PASO 3: Si se encontró una placa, eliminar el registro de la tabla 'vehiculos'.
            if (placaParaBorrar != null && !placaParaBorrar.trim().isEmpty()) {
                pstmtDeleteVehiculo = con.prepareStatement(sqlDeleteVehiculo);
                pstmtDeleteVehiculo.setString(1, placaParaBorrar);
                int filasVehiculo = pstmtDeleteVehiculo.executeUpdate();
                if(filasVehiculo > 0) {
                     System.out.println("Registro de vehículo asociado (" + placaParaBorrar + ") eliminado exitosamente.");
                } else {
                    System.out.println("Advertencia: La persona tenía una placa asignada ("+placaParaBorrar+") pero no se encontró en la tabla de vehículos.");
                }
            } else {
                System.out.println("La persona no tenía vehículo asociado. No se borra nada de la tabla de vehículos.");
            }

            // --- FIN DE LA TRANSACCIÓN ---
            // Si todos los pasos se completaron sin errores, se confirman los cambios.
            con.commit();
            System.out.println("Transacción completada exitosamente.");

        } catch (SQLException e) {
            System.err.println("¡ERROR! Ocurrió un problema durante la transacción. Revirtiendo cambios...");
            e.printStackTrace();
            // Si algo falla, se deshacen TODOS los cambios hechos.
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw e; // Relanzamos la excepción para informar del fallo.
        } finally {
            // Cerramos todos los recursos en el orden correcto
            if (rs != null) rs.close();
            if (pstmtSelectPlaca != null) pstmtSelectPlaca.close();
            if (pstmtDeletePersona != null) pstmtDeletePersona.close();
            if (pstmtDeleteVehiculo != null) pstmtDeleteVehiculo.close();
            if (con != null) {
                con.setAutoCommit(true); // Muy importante: devolver la conexión a su estado normal.
                con.close();
            }
        }
    }
    public void editarFuncionario(String rol, String nombre1, String nombre2, String apellido1, String apellido2, String identificacionNueva, String tipoVehiculo, String placaNueva, String cedulaOriginal) throws SQLException, ClassNotFoundException {
        
        Connection con = null;
        PreparedStatement pstmtSelectPlacaAntigua = null;
        PreparedStatement pstmtUpdatePersona = null;
        PreparedStatement pstmtUpsertVehiculo = null;
        PreparedStatement pstmtDeleteVehiculoAntiguo = null;
        ResultSet rs = null;
        String placaAntigua = null;

        String sqlSelectPlacaAntigua = "SELECT placaVehiculo FROM personas WHERE cedula = ?";
        String sqlUpsertVehiculo = "INSERT INTO vehiculos (placa, tipoVehiculo) VALUES (?, ?) ON DUPLICATE KEY UPDATE tipoVehiculo=VALUES(tipoVehiculo)";
        String sqlUpdatePersona = "UPDATE personas SET cedula = ?, nombre1 = ?, nombre2 = ?, apellido1 = ?, apellido2 = ?, ocupacion = ?, placaVehiculo = ? WHERE cedula = ? AND tipoPersona = 'Funcionario'";
        String sqlDeleteVehiculoAntiguo = "DELETE FROM vehiculos WHERE placa = ?";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/proyecto1?verifyServerCertificate=false&useSSL=true", 
                    "root", 
                    "1234"); // ¡TU CONTRASEÑA!

            con.setAutoCommit(false); // Iniciamos la transacción

            // 1. OBTENER LA PLACA ANTIGUA del funcionario
            pstmtSelectPlacaAntigua = con.prepareStatement(sqlSelectPlacaAntigua);
            pstmtSelectPlacaAntigua.setString(1, cedulaOriginal);
            rs = pstmtSelectPlacaAntigua.executeQuery();
            if (rs.next()) {
                placaAntigua = rs.getString("placaVehiculo");
            }

            // Normalizamos la placa nueva para evitar problemas con strings vacíos
            if (placaNueva != null && placaNueva.trim().isEmpty()) {
                placaNueva = null;
            }

            // 2. INSERTAR O ACTUALIZAR el nuevo vehículo (si se proporcionó una placa nueva)
            if (placaNueva != null) {
                pstmtUpsertVehiculo = con.prepareStatement(sqlUpsertVehiculo);
                pstmtUpsertVehiculo.setString(1, placaNueva);
                pstmtUpsertVehiculo.setString(2, tipoVehiculo);
                pstmtUpsertVehiculo.executeUpdate();
                System.out.println("Vehículo nuevo/actualizado: " + placaNueva);
            }

            // 3. ACTUALIZAR LA PERSONA con todos sus nuevos datos
            pstmtUpdatePersona = con.prepareStatement(sqlUpdatePersona);
            pstmtUpdatePersona.setString(1, identificacionNueva);
            pstmtUpdatePersona.setString(2, nombre1);
            pstmtUpdatePersona.setString(3, nombre2);
            pstmtUpdatePersona.setString(4, apellido1);
            pstmtUpdatePersona.setString(5, apellido2);
            pstmtUpdatePersona.setString(6, rol);
            pstmtUpdatePersona.setString(7, placaNueva);
            pstmtUpdatePersona.setString(8, cedulaOriginal);
            pstmtUpdatePersona.executeUpdate();
            System.out.println("Persona actualizada: " + cedulaOriginal);

            // 4. BORRAR EL VEHÍCULO ANTIGUO si es diferente del nuevo y ya no está en uso
            boolean placasSonDiferentes = placaAntigua != null && !placaAntigua.equals(placaNueva);
            if (placasSonDiferentes) {
                // Opcional pero recomendado: verificar si otro funcionario usa la placa antigua antes de borrarla.
                // Por simplicidad aquí, asumimos que no hay placas compartidas.
                pstmtDeleteVehiculoAntiguo = con.prepareStatement(sqlDeleteVehiculoAntiguo);
                pstmtDeleteVehiculoAntiguo.setString(1, placaAntigua);
                pstmtDeleteVehiculoAntiguo.executeUpdate();
                System.out.println("Vehículo antiguo huérfano eliminado: " + placaAntigua);
            }

            con.commit(); // Confirmamos la transacción
            System.out.println("Transacción de edición completada con éxito.");

        } catch (SQLException e) {
            e.printStackTrace();
            if (con != null) con.rollback();
            throw e;
        } finally {
            // Cerramos todos los recursos
            if (rs != null) rs.close();
            if (pstmtSelectPlacaAntigua != null) pstmtSelectPlacaAntigua.close();
            if (pstmtUpdatePersona != null) pstmtUpdatePersona.close();
            if (pstmtUpsertVehiculo != null) pstmtUpsertVehiculo.close();
            if (pstmtDeleteVehiculoAntiguo != null) pstmtDeleteVehiculoAntiguo.close();
            if (con != null) {
                con.setAutoCommit(true);
                con.close();
            }
        }
    }
}
