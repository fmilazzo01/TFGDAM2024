package com.mycompany.proyectodam;

import com.mycompany.bd.clientes;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Franco
 */
public class ClienteController implements Initializable {

    @FXML
    private DatePicker DatePickerFechaInicio;

    @FXML
    private DatePicker DatePickerFechaNacimiento;

    @FXML
    private TextField TextfieldApellidos;

    @FXML
    private TextField TextfieldNombre;

    @FXML
    private TextField TextfieldTelefono;

    @FXML
    private TextField Textfieldtelemergencias;

    @FXML
    private TextField textfieldCorreos;

    @FXML
    private Button CloseButton;

    @FXML
    private Button ButtonCancelar;

    @FXML
    private Button ButtonEditar;

    @FXML
    private Button ButtonGuardar;

    // variable para almacenar los datos originales del cliente
    private clientes clienteOriginal;

    // metodo para inicializar los textfields con los datos del cliente
    public void initData(clientes cliente) {

        clienteOriginal = cliente;
        TextfieldApellidos.setText(cliente.getApellidos());
        DatePickerFechaNacimiento.setValue(cliente.getFechanacimiento());
        DatePickerFechaInicio.setValue(cliente.getFechainicio());
        TextfieldNombre.setText(cliente.getNombre());
        TextfieldTelefono.setText(cliente.getTelefono());
        Textfieldtelemergencias.setText(cliente.getTelemergencias());
        textfieldCorreos.setText(cliente.getEmail());
        setEditableFields(false);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

// Metodo para activar o desactivar la edicion de campos
    private void setEditableFields(boolean editable) {
        TextfieldApellidos.setDisable(!editable);
        TextfieldApellidos.setEditable(editable);
        DatePickerFechaNacimiento.setDisable(!editable);
        DatePickerFechaInicio.setDisable(!editable);
        TextfieldNombre.setDisable(!editable);
        TextfieldNombre.setEditable(editable);
        TextfieldTelefono.setDisable(!editable);
        TextfieldTelefono.setEditable(editable);
        Textfieldtelemergencias.setDisable(!editable);
        Textfieldtelemergencias.setEditable(editable);
        textfieldCorreos.setDisable(!editable);
        textfieldCorreos.setEditable(editable);
        ButtonGuardar.setDisable(!editable);
        ButtonCancelar.setDisable(!editable);
        ButtonEditar.setDisable(editable);
    }

    // metodo para cancelar la edición y volver a los datos originales
    @FXML
    private void cancelarEdicion() {
        initData(clienteOriginal);
        setEditableFields(false); // Desactivar la edición de campos
    }

    // metodo para activar la edición de campos
    @FXML
    private void editarCampos() {
        setEditableFields(true); // Activar la edición de campos
    }

    // metodo para guardar los cambios editados
    @FXML
    private void guardarCambios() {
        // Obtener los nuevos valores de los campos
        String nuevosApellidos = TextfieldApellidos.getText();
        LocalDate nuevaFechaNacimiento = DatePickerFechaNacimiento.getValue();
        LocalDate nuevaFechaInicio = DatePickerFechaInicio.getValue();
        String nuevoNombre = TextfieldNombre.getText();
        String nuevoTelefono = TextfieldTelefono.getText();
        String nuevoTeleEmergencias = Textfieldtelemergencias.getText();
        String nuevoEmail = textfieldCorreos.getText();

        // Actualizar los datos del cliente en la bd
        clienteOriginal.setApellidos(nuevosApellidos);
        clienteOriginal.setFechanacimiento(nuevaFechaNacimiento);
        clienteOriginal.setFechainicio(nuevaFechaInicio);
        clienteOriginal.setNombre(nuevoNombre);
        clienteOriginal.setTelefono(nuevoTelefono);
        clienteOriginal.setTelemergencias(nuevoTeleEmergencias);
        clienteOriginal.setEmail(nuevoEmail);

        if (clienteOriginal.actualizarDatos()) {
            setEditableFields(false); // si fue exitosa desactivar edicion de campos
            AlertUtils.showAlert(Alert.AlertType.INFORMATION, "Éxito", "Los datos del cliente se actualizaron correctamente.");

        } else {
            System.out.println("Error al actualizar los datos del cliente en la base de datos.");
            AlertUtils.showAlert(Alert.AlertType.ERROR, "Error", "No se pudo actualizar los datos del cliente.");
        }
    }

    public void cerrarVentana() {
        Stage stage = (Stage) CloseButton.getScene().getWindow();
        stage.close();
    }
}
