package com.mycompany.proyectodam;

import com.mycompany.bd.connect;
import com.mycompany.bd.usuarios;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controlador de la clase que permite cambiar datos del gimnasio como los precios
 *
 * @author Franco
 */

public class UsuarioController implements Initializable {

    @FXML
    private Button ButtonCancelar;

    @FXML
    private Button ButtonEditar;

    @FXML
    private Button ButtonGuardar;

    @FXML
    private Button CloseButton;

    @FXML
    private TextField TextfieldAnio;

    @FXML
    private TextField TextfieldDireccion;

    @FXML
    private TextField TextfieldMes;

    @FXML
    private TextField TextfieldNombreGym;

    @FXML
    private TextField TextfieldSemestre;

    @FXML
    private TextField TextfieldTelefono;

    @FXML
    private TextField TextfieldTrimestre;

    @FXML
    private TextField textfieldEmail;

    private String nombreUsuario;
    private usuarios usuarioActual;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void initData2(String nombreUsuario) {
       setEditableFields(false);  // desactivar la edicion  
        this.nombreUsuario = nombreUsuario;
        // Obtener los datos del usuario actual y mostrarlos
        usuarioActual = obtenerDatosUsuario(nombreUsuario);
        if (usuarioActual != null) {
            mostrarDatosUsuario(usuarioActual);
        }
    }

    @FXML
    void editarCampos(ActionEvent event) {
        setEditableFields(true); // activar la edicion
    }

    @FXML
    void cancelarEdicion(ActionEvent event) {
        if (usuarioActual != null) {
            mostrarDatosUsuario(usuarioActual);
            setEditableFields(false);  // desactivar la edicion    
        }
    }

    @FXML
    void cerrarVentana(ActionEvent event) {
        Stage stage = (Stage) CloseButton.getScene().getWindow();
        stage.close();
    }

    // metodo que guarda los campos realizados 
    @FXML
    void guardarCambios(ActionEvent event) {
        if (usuarioActual != null) {
            usuarioActual.setDireccion(TextfieldDireccion.getText());
            usuarioActual.setTelefono(TextfieldTelefono.getText());
            usuarioActual.setEmail(textfieldEmail.getText());
            usuarioActual.setGimnasioNombre(TextfieldNombreGym.getText());

            usuarioActual.setPrecioMes(Double.parseDouble(TextfieldMes.getText()));
            usuarioActual.setPrecioTrimestre(Double.parseDouble(TextfieldTrimestre.getText()));
            usuarioActual.setPrecioSemestre(Double.parseDouble(TextfieldSemestre.getText()));
            usuarioActual.setPrecioAnio(Double.parseDouble(TextfieldAnio.getText()));

            // Actualizar en la base de datos
            if (connect.actualizarUsuario(usuarioActual)) {
                setEditableFields(false); // desactivar la edicion  
            }
        }
    }

    // muestro los datos en los campos
    private void mostrarDatosUsuario(usuarios usuario) {
        TextfieldDireccion.setText(usuario.getDireccion());
        TextfieldTelefono.setText(usuario.getTelefono());
        textfieldEmail.setText(usuario.getEmail());
        TextfieldNombreGym.setText(usuario.getGimnasioNombre());
        TextfieldMes.setText(String.valueOf(connect.getPrecioMensual(usuario.getNombreUsuario())));
        TextfieldTrimestre.setText(String.valueOf(connect.getPrecioTrimestral(usuario.getNombreUsuario())));
        TextfieldSemestre.setText(String.valueOf(connect.getPrecioSemestral(usuario.getNombreUsuario())));
        TextfieldAnio.setText(String.valueOf(connect.getPrecioAnual(usuario.getNombreUsuario())));
    }

private void setEditableFields(boolean editable) {
    TextfieldDireccion.setDisable(!editable);
    TextfieldDireccion.setEditable(editable);
    TextfieldTelefono.setDisable(!editable);
    TextfieldTelefono.setEditable(editable);
    textfieldEmail.setDisable(!editable);
    textfieldEmail.setEditable(editable);
    TextfieldNombreGym.setDisable(!editable);
    TextfieldNombreGym.setEditable(editable);
    TextfieldMes.setDisable(!editable);
    TextfieldMes.setEditable(editable);
    TextfieldTrimestre.setDisable(!editable);
    TextfieldTrimestre.setEditable(editable);
    TextfieldSemestre.setDisable(!editable);
    TextfieldSemestre.setEditable(editable);
    TextfieldAnio.setDisable(!editable);
    TextfieldAnio.setEditable(editable);
    ButtonGuardar.setDisable(!editable);
    ButtonCancelar.setDisable(!editable);
    ButtonEditar.setDisable(editable);
}

    private usuarios obtenerDatosUsuario(String nombreUsuario) {
        return connect.getUsuarioActual(nombreUsuario); // metodo que obtiene los datos del usuario actual
    }

}
