package com.mycompany.bd;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * CLASE CON METODOS PARA LA CONEXION CON LA BD Y CONSULTAS
 *
 * @author Franco
 */
public class connect {

    Connection conn = null;

//    // conexion con la BD para localhost con laragon
//    public static Connection ConnectDb() {
//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
//            Connection conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/gym", "root", "123");
//            //   System.out.println("//////////////////////////////////conexion establecida//////////////////////////////////");
//            return conn;
//        } catch (Exception e) {
//
//            return null;
//        }
//
//    }
    public static Connection ConnectDb() {    // conexion con la BD para https://freesqldatabase.com/ //https://www.phpmyadmin.co/db_structure.php?server=1&db=sql7712665
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = (Connection) DriverManager.getConnection("jdbc:mysql://sql7.freesqldatabase.com:3306/sql7712665", "sql7712665", "aI6eVECu5K");
            //   System.out.println("////////////////////////////////// conexion establecida //////////////////////////////////");
            return conn;
        } catch (Exception e) {
            return null;
        }
    }

    // metodo que obtiene datos de todos los clientes de un determinado usuario
    public static ObservableList<clientes> getDatausers(String nombreUsuario) {
        Connection conn = ConnectDb();
        ObservableList<clientes> list = FXCollections.observableArrayList();
        try {
            String query = "SELECT clientes.*, MAX(pagos.membresia_hasta) AS membresia_hasta "
                    + "FROM clientes "
                    + "JOIN usuarios ON clientes.usuario_id = usuarios.id "
                    + "LEFT JOIN pagos ON clientes.id = pagos.cliente_id "
                    + "WHERE usuarios.nombre_usuario = ? "
                    + "GROUP BY clientes.id";

            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, nombreUsuario); // Establecer el nombre de usuario como parametro

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String membresiaHastaStr = rs.getString("membresia_hasta");
                LocalDate membresiaHasta = null;
                if (membresiaHastaStr != null) {
                    membresiaHasta = LocalDate.parse(membresiaHastaStr);
                }

                list.add(new clientes(
                        Integer.parseInt(rs.getString("id")),
                        rs.getString("nombre"),
                        rs.getString("apellidos"),
                        LocalDate.parse(rs.getString("fechanacimiento")),
                        rs.getString("telefono"),
                        rs.getString("telemergencias"),
                        LocalDate.parse(rs.getString("fechainicio")),
                        membresiaHasta,
                        rs.getString("email")
                ));
            }
        } catch (NumberFormatException | SQLException e) {

        } finally {
            try {
                conn.close(); // Cerrar la conexion
            } catch (SQLException e) {

            }
        }
        return list;
    }

    // metodo que obtiene datos de todos los clientes de un determinado usuario (QUE ESTEN ATRASADOS EN EL PAGO)
    public static ObservableList<clientes> getClientesAtrasados(String nombreUsuario) {
        Connection conn = ConnectDb();
        ObservableList<clientes> list = FXCollections.observableArrayList();
        try {
            String query = "SELECT c.*, p.membresia_hasta "
                    + "FROM clientes c "
                    + "INNER JOIN pagos p ON c.id = p.cliente_id "
                    + "INNER JOIN usuarios u ON c.usuario_id = u.id "
                    + "WHERE p.membresia_hasta < CURDATE() AND u.nombre_usuario = ?";

            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, nombreUsuario); // Establecer el nombre de usuario como parámetro

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new clientes(
                        Integer.parseInt(rs.getString("id")),
                        rs.getString("nombre"),
                        rs.getString("apellidos"),
                        LocalDate.parse(rs.getString("fechanacimiento")),
                        rs.getString("telefono"),
                        rs.getString("telemergencias"),
                        LocalDate.parse(rs.getString("fechainicio")),
                        LocalDate.parse(rs.getString("membresia_hasta")),
                        rs.getString("email")
                ));
            }
        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close(); // Cerrar la conexión
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public static double obtenerDineroRecaudadoEsteMes(String nombreUsuario) {
        double dineroRecaudado = 0.0;
        String query = "SELECT SUM(p.cantidad) AS dinero_total FROM pagos p "
                + "INNER JOIN clientes c ON p.cliente_id = c.id "
                + "INNER JOIN usuarios u ON c.usuario_id = u.id "
                + "WHERE MONTH(p.fecha_pago) = MONTH(CURDATE()) AND u.nombre_usuario = ?";
        try (Connection conn = connect.ConnectDb(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, nombreUsuario); // Establecer el nombre de usuario como parámetro
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                dineroRecaudado = rs.getDouble("dinero_total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dineroRecaudado;
    }

    // metodo que valida el login de usuario y devuelve true si es correcto
    public static boolean validarLogin(String nombreUsuario, String contrasena) {
        Connection conn = ConnectDb();
        String query = "SELECT contrasena FROM usuarios WHERE nombre_usuario = ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, nombreUsuario);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String hashedPasswordFromDB = rs.getString("contrasena");  // Obtener la contrasena hasheada de la base de datos
                String hashedPassword = hashPassword(contrasena); // Hashear la contrasena 
                // Comparar las contrasenas hasheadas
                return hashedPasswordFromDB.equals(hashedPassword);     // Comparar las contrasenas hasheadas SI COINCIDEN EL METODO DEVUELVE TRUE
            } else {
                return false; // Si no se encuentra un usuario con el nombre de usuario proporcionado
            }
        } catch (SQLException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //metodo para registrar un usuario en la bd junto con los precios de membresia
    public static boolean registrarUsuario(String nombreUsuario, String contrasena, String email, String direccion, String telefono, String nombreGym, double precioMes, double precioTrimestre, double precioSemestre, double precioAnio) {
        Connection conn = ConnectDb();
        String queryUsuarios = "INSERT INTO usuarios (nombre_usuario, contrasena, email, direccion, telefono, gimnasio_nombre) VALUES (?, ?, ?, ?, ?, ?)";
        String queryPrecios = "INSERT INTO precios (gimnasio_id, precio_mes, precio_trimestre, precio_semestre, precio_anio) VALUES (?, ?, ?, ?, ?)";

        try {
            conn.setAutoCommit(false); // Iniciar transaccion
            try (PreparedStatement psUsuarios = conn.prepareStatement(queryUsuarios, Statement.RETURN_GENERATED_KEYS); PreparedStatement psPrecios = conn.prepareStatement(queryPrecios)) {
                String hashedPassword = hashPassword(contrasena); // cifro la contraseña

                // Insertar usuario
                psUsuarios.setString(1, nombreUsuario);
                psUsuarios.setString(2, hashedPassword); // almaceno la contraseña cifrada en la bd
                psUsuarios.setString(3, email);
                psUsuarios.setString(4, direccion);
                psUsuarios.setString(5, telefono);
                psUsuarios.setString(6, nombreGym);

                int rowsUsuariosAffected = psUsuarios.executeUpdate();

                // Obtener el id del gimnasio (usuario) recien insertado
                int gimnasioId = 0;
                ResultSet generatedKeys = psUsuarios.getGeneratedKeys();
                if (generatedKeys.next()) {
                    gimnasioId = generatedKeys.getInt(1);
                }

                // Insertar precios
                psPrecios.setInt(1, gimnasioId);
                psPrecios.setDouble(2, precioMes);
                psPrecios.setDouble(3, precioTrimestre);
                psPrecios.setDouble(4, precioSemestre);
                psPrecios.setDouble(5, precioAnio);

                int rowsPreciosAffected = psPrecios.executeUpdate();

                conn.commit(); // Confirmar transacion

                return rowsUsuariosAffected > 0 && rowsPreciosAffected > 0; // si al menos se insertó una fila en usuarios y en precios, fue correcto
            } catch (SQLException e) {
                conn.rollback(); // Rollback en caso de excepción
                e.printStackTrace();
                return false;
            }
        } catch (SQLException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                conn.setAutoCommit(true); // Restaurar autocommit a true
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // METODO QUE COMPRUEBA SI YA EXISTE ESE EMAIL EN LA BD
    public static boolean comprobarEMAILRegistrado(String email) {
        String query = "SELECT COUNT(*) AS count FROM usuarios WHERE email = ?";

        try (Connection conn = ConnectDb(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt("count") > 0;
            }
        } catch (SQLException e) {
        }

        return false; // En caso de error devuelve falso por 
    }

    // METODO QUE COMPRUEBA SI YA EXISTE ESE nombre de usuario EN LA BD
    public static boolean comprobarSiUsuarioExistente(String nombreUsuario) {
        String query = "SELECT COUNT(*) AS count FROM usuarios WHERE nombre_usuario = ?";

        try (Connection conn = ConnectDb(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, nombreUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt("count") > 0;
            }
        } catch (SQLException e) {
        }

        return false;
    }

    // metodo para hashear la contraseña usando SHA-256
    private static String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");       // Obtener una instancia de MessageDigest para SHA-256
        byte[] hash = md.digest(password.getBytes());    // Obtener el array de bytes del hash de la contrasena
        return Base64.getEncoder().encodeToString(hash); // Convertir el array de bytes a una cadena codificada en base64 y devolverla
    }

    // metodo para cambiar la contraseña por correo electrónico
    public static boolean cambiarContrasenaPorEmail(String email, String nuevaContrasena) {
        Connection conn = ConnectDb();
        if (conn == null) {
            return false; // No se pudo establecer la conexión
        }

        String query = "UPDATE usuarios SET contrasena = ? WHERE email = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            // Hashear la nueva contraseña
            String hashedPassword = hashPassword(nuevaContrasena);

            ps.setString(1, hashedPassword);
            ps.setString(2, email);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0; // Si se actualizó al menos una fila, el cambio fue exitoso
        } catch (SQLException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //metodo para verificar si la contrasena tiene un formato valido
    public static boolean validarContrasena(String contrasena) {
        return contrasena.length() >= 6 && contrasena.matches("^(?=.*[A-Z])(?=.*[!@#$&*])(?=.*[0-9])(?=.*[a-z]).{6,}$");
    }

    // metodo para obtener la cantidad de clientes nuevos registrados en los ultimos 5 años para un usuario específico
    public static Map<String, Integer> obtenerClientesNuevosUltimos5Anios(String nombreUsuario) {
        Map<String, Integer> clientesNuevosPorAnio = new HashMap<>();
        Connection conn = ConnectDb();

        if (conn == null) {
            return clientesNuevosPorAnio; // Retornar el mapa vacio si no se pudo establecer la conexion
        }
        String query = "SELECT YEAR(fechainicio) AS Anio, COUNT(*) AS ClientesNuevos "
                + "FROM clientes "
                + "INNER JOIN usuarios ON clientes.usuario_id = usuarios.id "
                + "WHERE fechainicio >= DATE_SUB(CURDATE(), INTERVAL 5 YEAR) "
                + "AND usuarios.nombre_usuario = ? "
                + "GROUP BY YEAR(fechainicio)";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, nombreUsuario);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String anio = rs.getString("Anio");
                int clientesNuevos = rs.getInt("ClientesNuevos");
                clientesNuevosPorAnio.put(anio, clientesNuevos);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return clientesNuevosPorAnio;
    }

    //metodo para obtener el email de un usuario
    public static String getEmailUsuario(String nombreUsuario) {
        String correoUsuario = "";
        Connection conn = ConnectDb();
        String query = "SELECT email FROM usuarios WHERE nombre_usuario = ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, nombreUsuario);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                correoUsuario = rs.getString("email");
            }
        } catch (SQLException e) {
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
            }
        }
        return correoUsuario;
    }

    // ---------------------------------- METODOS PARA OBTENER EL PRECIO DE LAS MEMBRESIAS -----------------------------
// Metodo para obtener el precio de un MES en el gym
    public static double getPrecioMensual(String nombreUsuario) {
        String query = "SELECT p.precio_mes FROM precios p "
                + "JOIN usuarios u ON p.gimnasio_id = u.id "
                + "WHERE u.nombre_usuario = ?";
        try (Connection conn = ConnectDb(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, nombreUsuario);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("precio_mes");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

// Metodo para obtener el precio de un TRIMESTRE en el gym
    public static double getPrecioTrimestral(String nombreUsuario) {
        String query = "SELECT p.precio_trimestre FROM precios p "
                + "JOIN usuarios u ON p.gimnasio_id = u.id "
                + "WHERE u.nombre_usuario = ?";
        try (Connection conn = ConnectDb(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, nombreUsuario);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("precio_trimestre");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

// Metodo para obtener el precio de un SEMESTRE en el gym
    public static double getPrecioSemestral(String nombreUsuario) {
        String query = "SELECT p.precio_semestre FROM precios p "
                + "JOIN usuarios u ON p.gimnasio_id = u.id "
                + "WHERE u.nombre_usuario = ?";
        try (Connection conn = ConnectDb(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, nombreUsuario);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("precio_semestre");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

// Metodo para obtener el precio de un ANIO en el gym
    public static double getPrecioAnual(String nombreUsuario) {
        String query = "SELECT p.precio_anio FROM precios p "
                + "JOIN usuarios u ON p.gimnasio_id = u.id "
                + "WHERE u.nombre_usuario = ?";
        try (Connection conn = ConnectDb(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, nombreUsuario);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("precio_anio");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    // metodo para calcular el monto del pago basado en el tipo de pago y nombre de usuario
    public static double calcularMontoPago(String tipoPago, String nombreUsuario) {
        switch (tipoPago) {
            case "Mensualidad":
                return getPrecioMensual(nombreUsuario);
            case "Trimestre":
                return getPrecioTrimestral(nombreUsuario);
            case "Semestre":
                return getPrecioSemestral(nombreUsuario);
            case "Año":
                return getPrecioAnual(nombreUsuario);
            default:
                return 0.0;
        }
    }
    // ---------------------------------- ----------------------------------  -----------------------------

    // Metodo para obtener los datos del usuario actual
    public static usuarios getUsuarioActual(String nombreUsuario) {
        Connection conn = ConnectDb();
        String query = "SELECT * FROM usuarios WHERE nombre_usuario = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, nombreUsuario);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new usuarios(
                        rs.getInt("id"),
                        rs.getString("nombre_usuario"),
                        rs.getString("contrasena"),
                        rs.getString("email"),
                        rs.getString("direccion"),
                        rs.getString("telefono"),
                        rs.getString("gimnasio_nombre"),
                        0.0,
                        0.0,
                        0.0,
                        0.0
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

// metodo para actualizar los datos del usuario y precios del gym
    public static boolean actualizarUsuario(usuarios usuario) {
        Connection conn = ConnectDb();
        boolean success = false;
        try {
            conn.setAutoCommit(false); //desactivamos el auto commit 
            // Actualizar la tabla usuarios
            String queryUsuarios = "UPDATE usuarios SET direccion = ?, telefono = ?, email = ?, gimnasio_nombre = ? WHERE id = ?";
            try (PreparedStatement psUsuarios = conn.prepareStatement(queryUsuarios)) {
                psUsuarios.setString(1, usuario.getDireccion());
                psUsuarios.setString(2, usuario.getTelefono());
                psUsuarios.setString(3, usuario.getEmail());
                psUsuarios.setString(4, usuario.getGimnasioNombre());
                psUsuarios.setInt(5, usuario.getId());
                psUsuarios.executeUpdate();
            }

            // Actualizar la tabla precios
            String queryPrecios = "UPDATE precios SET precio_mes = ?, precio_trimestre = ?, precio_semestre = ?, precio_anio = ? WHERE gimnasio_id = ?";
            try (PreparedStatement psPrecios = conn.prepareStatement(queryPrecios)) {
                psPrecios.setDouble(1, usuario.getPrecioMes());
                psPrecios.setDouble(2, usuario.getPrecioTrimestre());
                psPrecios.setDouble(3, usuario.getPrecioSemestre());
                psPrecios.setDouble(4, usuario.getPrecioAnio());
                psPrecios.setInt(5, usuario.getId());
                psPrecios.executeUpdate();
            }
            conn.commit();      // Commit 
            success = true;
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conn.rollback();         // Rollback de la  en caso de error
            } catch (SQLException rollbackException) {
                rollbackException.printStackTrace();
            }
        } finally {
            try {
                // Restaurar el auto-commit
                conn.setAutoCommit(true);
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return success;
    }

}
