package com.mycompany.proyectodam;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Clase con el metodo editado para llamar pop ups
 * @author Franco
 */
public class AlertUtils {

    private static final Image ICON_IMAGE = new Image(AlertUtils.class.getResourceAsStream("/com/mycompany/imgs/logo2.png"));
  

     
     //metodo que muestra un popup
    public static void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        
        // Agregar el icono al alerta
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(ICON_IMAGE);
        
     
        alert.getDialogPane().getStyleClass().add("alert-dialog");
        
        alert.showAndWait();
    }
}