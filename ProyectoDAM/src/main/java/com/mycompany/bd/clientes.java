package com.mycompany.bd;

import com.mycompany.proyectodam.AlertUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import javafx.scene.control.Alert.AlertType;

/**
 * Clase con metodo constructor y getter y setter
 *
 * @author Franco
 */
public class clientes {

    int id;
    String nombre, apellidos, telefono, telemergencias, email;
    LocalDate fechanacimiento, fechainicio, membresia_hasta;

    public clientes(int id, String nombre, String apellidos, LocalDate fechanacimiento, String telefono, String telemergencias, LocalDate fechainicio, LocalDate membresia_hasta, String email) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.fechanacimiento = fechanacimiento;
        this.telefono = telefono;
        this.telemergencias = telemergencias;
        this.fechainicio = fechainicio;
        this.membresia_hasta = membresia_hasta;
        this.email = email;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getTelemergencias() {
        return telemergencias;
    }

    public void setTelemergencias(String telemergencias) {
        this.telemergencias = telemergencias;
    }

    public LocalDate getFechanacimiento() {
        return fechanacimiento;
    }

    public void setFechanacimiento(LocalDate fechanacimiento) {
        this.fechanacimiento = fechanacimiento;
    }

    public LocalDate getFechainicio() {
        return fechainicio;
    }

    public void setFechainicio(LocalDate fechainicio) {
        this.fechainicio = fechainicio;
    }

    public LocalDate getMembresia_hasta() {
        return membresia_hasta;
    }

    public void setMembresia_hasta(LocalDate membresia_hasta) {
        this.membresia_hasta = membresia_hasta;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    

    

    // metodo para actualizar los datos del cliente en la base de datos
    public boolean actualizarDatos() {
        String query = "UPDATE clientes SET nombre = ?, apellidos = ?, fechanacimiento = ?, telefono = ?, telemergencias = ?, fechainicio = ?, email = ? WHERE id = ?";
        try (Connection conn = connect.ConnectDb(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, nombre);
            ps.setString(2, apellidos);
            ps.setDate(3, java.sql.Date.valueOf(fechanacimiento));
            ps.setString(4, telefono);
            ps.setString(5, telemergencias);
            ps.setDate(6, java.sql.Date.valueOf(fechainicio));
            ps.setString(7, email);
            ps.setInt(8, id);
            
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                return true; // Devuelve true si se actualizo al menos una fila
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static int obtenerIdGimnasio(int clienteId) {
    int gimnasioId = 0;
    String query = "SELECT usuario_id FROM clientes WHERE id = ?";
    try (Connection conn = connect.ConnectDb();
         PreparedStatement pstmt = conn.prepareStatement(query)) {
        pstmt.setInt(1, clienteId);
        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                gimnasioId = rs.getInt("usuario_id");
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return gimnasioId;
}

    

}
