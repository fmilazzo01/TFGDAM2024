package com.mycompany.bd;

/**
 *
 * @author Franco
 */
public class usuarios {

    int id;
    String nombreUsuario, contrasena, email, direccion, telefono, gimnasioNombre;
    double precioMes, precioTrimestre, precioSemestre, precioAnio;

    public usuarios(int id, String nombreUsuario, String contrasena, String email, String direccion, String telefono, String gimnasioNombre, double precioMes, double precioTrimestre, double precioSemestre, double precioAnio) {
        this.id = id;
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.email = email;
        this.direccion = direccion;
        this.telefono = telefono;
        this.gimnasioNombre = gimnasioNombre;
        this.precioMes = precioMes;
        this.precioTrimestre = precioTrimestre;
        this.precioSemestre = precioSemestre;
        this.precioAnio = precioAnio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getGimnasioNombre() {
        return gimnasioNombre;
    }

    public void setGimnasioNombre(String gimnasioNombre) {
        this.gimnasioNombre = gimnasioNombre;
    }

    public double getPrecioMes() {
        return precioMes;
    }

    public void setPrecioMes(double precioMes) {
        this.precioMes = precioMes;
    }

    public double getPrecioTrimestre() {
        return precioTrimestre;
    }

    public void setPrecioTrimestre(double precioTrimestre) {
        this.precioTrimestre = precioTrimestre;
    }

    public double getPrecioSemestre() {
        return precioSemestre;
    }

    public void setPrecioSemestre(double precioSemestre) {
        this.precioSemestre = precioSemestre;
    }

    public double getPrecioAnio() {
        return precioAnio;
    }

    public void setPrecioAnio(double precioAnio) {
        this.precioAnio = precioAnio;
    }

}
