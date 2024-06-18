package com.mycompany.proyectodam;

import com.mycompany.bd.connect;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * Controlador del login del programa
 *
 * @author Franco
 */
public class LoginController implements Initializable {

    @FXML
    private Button LOG_ButtonIniciarSesion;

    @FXML
    private TextField LOG_NomUsuario;

    @FXML
    private PasswordField LOG_PasswordField;

    @FXML
    private Label LOG_labelmenu;

    @FXML
    private Label LOG_labeltexto;

    @FXML
    private Button REG_ButtonRegistrarse;

    @FXML
    private TextField REG_Direccion;

    @FXML
    private TextField REG_NomGYM;

    @FXML
    private TextField REG_NomUsuario;

    @FXML
    private Button LOG_TRANSICION;

    @FXML
    private Button REG_TRANSICION;

    @FXML
    private PasswordField REG_contrasena;

    @FXML
    private TextField REG_email;

    @FXML
    private Label REG_labelmenu1;

    @FXML
    private Label REG_labeltexto;

    @FXML
    private TextField REG_telefono;

    @FXML
    private AnchorPane WhitePane;

    @FXML
    private AnchorPane layer2;

    @FXML
    private Button cerrar_registro;

    @FXML
    private Button cerrrar_login;

    @FXML
    private Label REG_Label1;

    @FXML
    private Label REG_Label2;

    @FXML
    private Label REG_Label3;

    @FXML
    private Label REG_Label4;

    @FXML
    private Label REG_Label5;

    @FXML
    private Label REG_Label6;

    @FXML
    private Label LOG_Label1;

    @FXML
    private Label LOG_Label2;

    @FXML
    private Hyperlink hyperlinkRecuperar;

    @FXML
    private Pane SoporteAlargado;

    @FXML
    private Pane SoporteEscondido;

    // Guardar las posiciones iniciales
    private double whitePaneInitialX; // posicion inicial del panel
    private double layer2InitialX; // posicion inicial del menu lateral

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ocultarRegistro();
        mostrarLogin();

    }

    //metodo para desplazar el panel para mostrar el registro
    @FXML
    void btn(MouseEvent event) {
        TranslateTransition slide = new TranslateTransition(); // Define una transicion de desplazamiento
        slide.setDuration(Duration.seconds(0.7)); //duracion de la transición
        slide.setNode(layer2); // Define el nodo que se desplazara ( el panel que se mueve)
        slide.setToX(600); // Mueve el panel hacia la derecha
        slide.play(); //inicia la animacion

        WhitePane.setTranslateX(-300); // mueve el panel blanco hacia la izquierda
        mostrarRegistro(); // Muestra el formulario de registro
        ocultarLogin(); // Oculta el formulario de inicio de sesión

    }

    //metodo para desplazar el panel para mostrar el login
    @FXML
    void btn2(MouseEvent event) {
        TranslateTransition slide = new TranslateTransition();
        slide.setDuration(Duration.seconds(0.6));
        slide.setNode(layer2);
        slide.setToX(layer2InitialX); // posicion inicial del panel
        slide.play();

        WhitePane.setTranslateX(whitePaneInitialX); // Restablece la posición inicial del panel blanco
        mostrarLogin();
        ocultarRegistro();

//        slide.setOnFinished((e -> { 
//            // codigo que se ejecuta despues de que la animación haya terminado
//        }));
    }

    // metodo del boton de login que valida el inicio de sesion y abre la app
    @FXML
    void LOGIN(MouseEvent event) throws IOException {
        String nombreUsuario = LOG_NomUsuario.getText();
        String contrasena = LOG_PasswordField.getText();

        // Validar el inicio de ses
        if (connect.validarLogin(nombreUsuario, contrasena)) {
            System.out.println("Inicio de sesion exitoso para el usuario: " + nombreUsuario);
            App.NombreUsuarioLogeado = nombreUsuario; // almaceno el nombre del usuario en la variable

            App.setRoot("home", 1100, 580);
        } else {
            AlertUtils.showAlert(Alert.AlertType.INFORMATION, "Error", "Nombre de usuario o contraseña incorrectos. Por favor, inténtalo de nuevo.");

        }
    }

    //METODO PARA EL BOTON DE REGISTRAR EL USUARIO
    @FXML
    void register(ActionEvent event) {
        String nombreUsuario = REG_NomUsuario.getText();
        String contrasena = REG_contrasena.getText();
        String email = REG_email.getText();
        String direccion = REG_Direccion.getText();
        String telefono = REG_telefono.getText();
        String nombreGym = REG_NomGYM.getText();

        // Verificar si hay algun campo vacio
        if (nombreUsuario.isEmpty() || contrasena.isEmpty() || email.isEmpty() || nombreGym.isEmpty()) {
            AlertUtils.showAlert(Alert.AlertType.WARNING, "Advertencia", "Por favor, completa todos los campos.");
            return;
        }
        // Validar longitud y formato del nombre de usuario y nombre del gimnasio
        if (nombreUsuario.length() < 4 || nombreGym.length() < 4) {
            AlertUtils.showAlert(Alert.AlertType.WARNING, "Advertencia", "El nombre de usuario y el nombre del gimnasio deben tener al menos 4 caracteres.");
            return;
        }
        // Validar formato del email
        if (!email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
            AlertUtils.showAlert(Alert.AlertType.WARNING, "Advertencia", "El formato del correo electrónico no es válido.");
            return;
        }
        // Validar longitud y complejidad de la contraseña
        if (contrasena.length() < 6 || !contrasena.matches("^(?=.*[A-Z])(?=.*[!@#$&*])(?=.*[0-9])(?=.*[a-z]).{6,}$")) {
            AlertUtils.showAlert(Alert.AlertType.WARNING, "Advertencia", "La contraseña debe tener al menos 6 caracteres, incluir al menos una mayúscula, un carácter especial y un dígito.");
            return;
        }
        // Verificar si el nombre de usuario ya esta en uso
        if (connect.comprobarSiUsuarioExistente(nombreUsuario)) {
            AlertUtils.showAlert(Alert.AlertType.WARNING, "Advertencia", "El nombre de usuario ya está en uso. Por favor, elige otro.");
            return;
        }
        // Verificar si el email ya esta en uso
        if (connect.comprobarEMAILRegistrado(email)) {
            AlertUtils.showAlert(Alert.AlertType.WARNING, "Advertencia", "El correo electrónico ya está registrado. Por favor, utiliza otro.");
            return;
        }
        // Registrar el usuario
        if (connect.registrarUsuario(nombreUsuario, contrasena, email, direccion, telefono, nombreGym, 30, 80, 140, 200)) {
            AlertUtils.showAlert(Alert.AlertType.INFORMATION, "Éxito", "Registro completado con éxito, ya puedes iniciar sesion.");
            // al registrar con exito limpio los campos y hago la transicion
            REG_Direccion.setText("");
            REG_NomGYM.setText("");
            REG_NomUsuario.setText("");
            REG_contrasena.setText("");
            REG_email.setText("");
            REG_telefono.setText("");
            btn2(null); // llamo el evento de la animacion

        } else {
            AlertUtils.showAlert(Alert.AlertType.ERROR, "Error", "Ocurrió un error durante el registro. Por favor, inténtalo de nuevo.");
        }
    }

    @FXML
    void recuperarpsw(ActionEvent event) throws IOException {
        App.setRoot("recuperarpsw", 600, 580);

    }

    public void exit(ActionEvent event) { //boton cerrar con x
        Platform.exit();
    }

    @FXML
    void minimizar(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    // METODOS PARA MOSTRAR Y OCULTAR EL SOPORTE
    @FXML
    void MostrarSoporte(MouseEvent event) {
        SoporteEscondido.setVisible(false);
        SoporteAlargado.setVisible(true);
    }

    @FXML
    void EsconderSoporte(MouseEvent event) {
        SoporteAlargado.setVisible(false);
        SoporteEscondido.setVisible(true);

    }

    // metodo para enviar un correo de asistencia
    @FXML
    void TextSupport(ActionEvent event) {
        TextInputDialog emailDialog = new TextInputDialog();  // Crear un cuadro de dialogo para ingresar el correo electrónico
        emailDialog.setTitle("Comunícate con el soporte");
        emailDialog.setHeaderText("Envía un correo al soporte:");
        emailDialog.setContentText("Ingresa tu correo electrónico:");

        Optional<String> emailResult = emailDialog.showAndWait();  // Mostrar el cuadro de dialogo para itnroducir el email y obtener el resultado
        emailResult.ifPresent(email -> {
            TextInputDialog messageDialog = new TextInputDialog();
            messageDialog.setTitle("Comunícate con el soporte");
            messageDialog.setHeaderText("Envía un correo al soporte:");
            messageDialog.setContentText("Escribe tu mensaje:");

            Optional<String> messageResult = messageDialog.showAndWait();   // Mostrar el cuadro de dialogo y obtener el resultado del mensaje
            messageResult.ifPresent(message -> {
                String recipientEmail = "thegymapp@outlook.com";
                String subject = "Correo de soporte desde el login";
                String finalMessage = "Ticket enviado por: " + email + "\n\n" + message;
                EmailSender.sendEmail(recipientEmail, subject, finalMessage); //ENVIAR EL CORREO CON TICKET DE SOPORTE
                AlertUtils.showAlert(AlertType.CONFIRMATION, "Correo enviado", "Tu mensaje ha sido enviado al soporte.");
            });
        });
    }

    // Metodo para ocultar los elementos de registro
    public void ocultarRegistro() {
        REG_TRANSICION.setVisible(false);
        REG_ButtonRegistrarse.setVisible(false);
        REG_Direccion.setVisible(false);
        REG_NomGYM.setVisible(false);
        REG_NomUsuario.setVisible(false);
        REG_contrasena.setVisible(false);
        REG_email.setVisible(false);
        REG_telefono.setVisible(false);
        REG_labelmenu1.setVisible(false);
        REG_labeltexto.setVisible(false);
        cerrar_registro.setVisible(false);
        REG_Label1.setVisible(false);
        REG_Label2.setVisible(false);
        REG_Label3.setVisible(false);
        REG_Label4.setVisible(false);
        REG_Label5.setVisible(false);
        REG_Label6.setVisible(false);

    }

// Metodo para mostrar los elementos de registro
    public void mostrarRegistro() {
        REG_TRANSICION.setVisible(true);
        REG_ButtonRegistrarse.setVisible(true);
        REG_Direccion.setVisible(true);
        REG_NomGYM.setVisible(true);
        REG_NomUsuario.setVisible(true);
        REG_contrasena.setVisible(true);
        REG_email.setVisible(true);
        REG_telefono.setVisible(true);
        REG_labelmenu1.setVisible(true);
        REG_labeltexto.setVisible(true);
        cerrar_registro.setVisible(true);
        REG_Label1.setVisible(true);
        REG_Label2.setVisible(true);
        REG_Label3.setVisible(true);
        REG_Label4.setVisible(true);
        REG_Label5.setVisible(true);
        REG_Label6.setVisible(true);
    }

// Metodo para ocultar los elementos de inicio de sesión
    public void ocultarLogin() {
        LOG_TRANSICION.setVisible(false);
        LOG_ButtonIniciarSesion.setVisible(false);
        LOG_NomUsuario.setVisible(false);
        LOG_PasswordField.setVisible(false);
        LOG_labelmenu.setVisible(false);
        LOG_labeltexto.setVisible(false);
        cerrrar_login.setVisible(false);
        LOG_Label1.setVisible(false);
        LOG_Label2.setVisible(false);
        hyperlinkRecuperar.setVisible(false);

    }

// Metodo para mostrar los elementos de inicio de sesión
    public void mostrarLogin() {
        LOG_TRANSICION.setVisible(true);
        LOG_ButtonIniciarSesion.setVisible(true);
        LOG_NomUsuario.setVisible(true);
        LOG_PasswordField.setVisible(true);
        LOG_labelmenu.setVisible(true);
        LOG_labeltexto.setVisible(true);
        cerrrar_login.setVisible(true);
        LOG_Label1.setVisible(true);
        LOG_Label2.setVisible(true);
        hyperlinkRecuperar.setVisible(true);

    }

}
