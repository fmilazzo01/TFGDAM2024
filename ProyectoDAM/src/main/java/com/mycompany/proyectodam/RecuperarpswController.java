package com.mycompany.proyectodam;

import com.mycompany.bd.connect;
import java.io.IOException;
import java.net.URL;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 * Controlador del menu para recuperar contrasenas de usuarios
 *
 * @author Franco
 */
public class RecuperarpswController implements Initializable {

    @FXML
    private TextField JTextFieldCorreo;

    @FXML
    private TextField JTextFieldUser;

    @FXML
    private TextField JTextFieldCodigoSeg;

    @FXML
    private AnchorPane PaneRecuperar1;

    @FXML
    private AnchorPane PaneRecuperar2;

    @FXML
    private AnchorPane PaneRecuperar3;
    @FXML
    private Label lblPregunta;
    @FXML
    private PasswordField JTextFieldConfirmarContra;

    @FXML
    private PasswordField JTextFieldContrasena;
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    private String codigoRecuperacion; //variable para almacenar el codigo de recuperacion
    private String emailUsuario; //variable para almacenar el codigo de recuperacion

    @FXML
    void Siguiente(ActionEvent event) { //metodo para ir de la primera opcion de recuperar a la segunda

        emailUsuario = JTextFieldCorreo.getText(); // obtengo el email del usuario
        if (connect.comprobarEMAILRegistrado(emailUsuario)) {
            PaneRecuperar1.setVisible(false);
            PaneRecuperar2.setVisible(true);

            String recipientEmail = emailUsuario;
            String subject = "Código de recuperación de tu cuenta de GymWizz";
            codigoRecuperacion = Metodos.generarCodigoRecuperacion(); // Generaramos el codigo de recuperacion
            String body = "Recibimos una solicitud para restablecer tu contraseña.\n"
                    + "Ingresa el siguiente código para restablecer la contraseña:  " + codigoRecuperacion;

            EmailSender.sendEmail(recipientEmail, subject, body);
            AlertUtils.showAlert(Alert.AlertType.INFORMATION, "Codigo de recuperacion enviado", "Hemos enviado un codigo de recuperacion a  " + emailUsuario);
            System.out.println("codigo correcto= " + codigoRecuperacion);
        } else {

            AlertUtils.showAlert(Alert.AlertType.ERROR, "Correo desconocido", "El correo electronico " + emailUsuario + " no esta en nuestra base de datos");
            System.out.println("No existe ese correo en la bd");
        }
    }

    @FXML
    void Siguiente2(ActionEvent event) { //metodo para ir de la segunda opcion de recuperar a la tercera y ultima

        if (JTextFieldCodigoSeg.getText().equals(codigoRecuperacion)) { //VERIFICO SI EL USUARIO INTRODUJO EL CODIGO CORRECTO

            PaneRecuperar2.setVisible(false);
            PaneRecuperar3.setVisible(true);

        } else {
            AlertUtils.showAlert(Alert.AlertType.ERROR, "Codigo equivado", "El codigo que has introducido no es correcto, verificalo porfavor");
        }
    }

    @FXML
    void Siguiente3(ActionEvent event) throws IOException {
        if (JTextFieldContrasena.getText().equals(JTextFieldConfirmarContra.getText())) {
            String contrasena = JTextFieldContrasena.getText();

            if (connect.validarContrasena(contrasena)) {
                // Si la contraseña es valida cambiarla

                connect.cambiarContrasenaPorEmail(emailUsuario, contrasena);
                AlertUtils.showAlert(Alert.AlertType.INFORMATION, "Perfecto", "La contraseña ha sido cambiada con éxito.");
                App.setRoot("login", 900, 580);
            } else {
                // Si la contraseña no es valida muestra un mensaje de error
                AlertUtils.showAlert(Alert.AlertType.ERROR, "Error", "La contraseña no cumple con los requisitos de seguridad.");
            }
        } else {
            // Si las contraseñas no coinciden muestra un mensaje de error
            AlertUtils.showAlert(Alert.AlertType.ERROR, "Error", "Las contraseñas no coinciden.");

        }
    }

 

    @FXML
    void VolverLogin(MouseEvent event) throws IOException {
        App.setRoot("login", 900, 580);
    }

    public void exit(ActionEvent event) { //boton cerrar con x
        Platform.exit();
    }

}
