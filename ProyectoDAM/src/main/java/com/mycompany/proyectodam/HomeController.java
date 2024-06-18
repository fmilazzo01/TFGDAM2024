package com.mycompany.proyectodam;

import com.mycompany.bd.clientes; // importamos la clase clientes
import com.mycompany.bd.usuarios; // importamos la clase usuarios
import com.mycompany.bd.connect;
import static com.mycompany.proyectodam.App.xOffset;
import static com.mycompany.proyectodam.App.yOffset;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * Controlador para el menu principal del programa
 *
 * @author Franco
 */
public class HomeController implements Initializable {

    @FXML
    private Button ButtonPagar;

    @FXML
    private Button buttonLogOut;

    @FXML
    private Label LabelUsuarioMenu;

    @FXML
    private Label LabelUsuarioMenu1;

    @FXML
    private Label LabelUsuarioMenu11;

    @FXML
    private Label LabelUsuarioMenu111;

    @FXML
    private Label labelNombreUsuario;

    @FXML
    private Button buttonActualizar;

    @FXML
    private Button buttonBorrar;

    @FXML
    private TextField textfieldFiltroAtrasados;

    @FXML
    private Label LabelHora;

    @FXML
    private BarChart<String, Double> chart;

    //  ---------------------- table clientes----------------
    @FXML
    private TableView<clientes> table_clientes;

    @FXML
    private TableColumn<clientes, Integer> col_id;

    @FXML
    private TableColumn<clientes, String> col_apellidos;

    @FXML
    private TableColumn<clientes, String> col_fechainicio;

    @FXML
    private TableColumn<clientes, String> col_fechanac;

    @FXML
    private TableColumn<clientes, String> col_nombre;

    @FXML
    private TableColumn<clientes, Integer> col_telefono;

    @FXML
    private TableColumn<clientes, Integer> col_telemergencias;

    @FXML
    private TableColumn<clientes, String> col_vencimientoMembresia;
    @FXML
    private TableColumn<clientes, String> col_email;
    // -------------- table clientes atrasados--------------------
    @FXML
    private TableView<clientes> table_clientesAtrasados;

    @FXML
    private TableColumn<clientes, Integer> col_id1;

    @FXML
    private TableColumn<clientes, String> col_apellidos1;

    @FXML
    private TableColumn<clientes, String> col_fechainicio1;

    @FXML
    private TableColumn<clientes, String> col_fechanac1;

    @FXML
    private TableColumn<clientes, String> col_nombre1;

    @FXML
    private TableColumn<clientes, Integer> col_telefono1;

    @FXML
    private TableColumn<clientes, Integer> col_telemergencias1;

    @FXML
    private TableColumn<clientes, String> col_vencimientoMembresia1;

    @FXML
    private TableColumn<clientes, String> col_email1;

    // ---------------------------------------------------------
    @FXML
    private Pane PaneAtrasados;

    @FXML
    private Pane PaneClientes;

    @FXML
    private Pane PaneInicio;

    @FXML
    private Pane PaneNuevoCliente;

    @FXML
    private TextField RegistroApellidos;

    @FXML
    private DatePicker RegistroNacimiento;

    @FXML
    private TextField RegistroNombre;

    @FXML
    private TextField RegistroNumTel;

    @FXML
    private TextField RegistroTelEmergencias;

    @FXML
    private TextField RegistroEmail;

    @FXML
    private TextField textfieldFiltro;

    @FXML
    private Label labelClientesDeudores;

    @FXML
    private Label labelGananciasMensuales;

    @FXML
    private Label labelTotalClientes;

    @FXML
    private ComboBox<String> ComboBoxGenero;

    @FXML
    private ComboBox<String> ComboBoxDiasEntreno;

    @FXML
    private Pane SoporteAlargado;

    @FXML
    private Pane SoporteEscondido;

    // ----------- TABLA  CLIENTES ----------
    ObservableList<clientes> listaClientes; // creo la lista
    int index = -1;
    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    //--------------------------------------------

    ObservableList<clientes> listaClientesAtrasados; // creo la lista

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        UpdateTable(); // llamo a que se actualize la tabla osea que aparesca en este caso
        UpdateTableClientesAtrasados();
        UpdateDashboard();
        updateLabelHora(); // llamo el metodo que pone la hora en el dashboard

        labelNombreUsuario.setText(App.NombreUsuarioLogeado);

        // datos del  combobox genero
        ObservableList<String> list = FXCollections.observableArrayList("Masculino", "Femenino");
        ComboBoxGenero.setItems(list);

        // datos del combobox dias de entramiento
        ObservableList<String> list2 = FXCollections.observableArrayList("1", "2", "3", "4", "5");
        ComboBoxDiasEntreno.setItems(list2);

        // PARA PODER BORRAR UN CLIENTE DANDO AL SUPRIMIR
        table_clientes.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.DELETE) { // Verifica si la tecla presionada es la tecla suprimir
                DeleteFromTable(); // Llama al metodo para eliminar el cliente
            }
        });

        // Agregar eventos de doble clic a las tableviews
        table_clientes.setOnMouseClicked(this::handleTableViewClick);
        table_clientesAtrasados.setOnMouseClicked(this::handleTableViewClick);

    }

    //maneja el evento de click en al tableview
    private void handleTableViewClick(MouseEvent event) {
        if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {   // Verifica si se hizo doble click en el boton izquierdo del mouse
            TableView<clientes> table = (TableView<clientes>) event.getSource(); // Obtiene la TableView a partir del evento
            clientes selectedCliente = table.getSelectionModel().getSelectedItem(); // Obtiene el cliente seleccionado en la TableView
            if (selectedCliente != null) {         // Verifica si se ha seleccionado un cliente
                System.out.println("Nombre del cliente: " + selectedCliente.getNombre());
                openClienteWindow(selectedCliente);  // Abre la ventana de cliente con la informacion  del cliente seleccionado
            }
            index = table.getSelectionModel().getSelectedIndex(); // Actualiza el indice seleccionado para que funcionen los botones de borrar y pagar
        }
    }

    // metodo que anade los clientes  a el grafico del dashboard
    public void ActualizarGrafico() {
        chart.getData().clear();     // limpiar el grafico antes de anadir nuevos datos

        XYChart.Series<String, Double> graficox = new XYChart.Series<>(); // Crear una nueva serie de datos para el grafico
        graficox.setName("Clientes Nuevos");

        Map<String, Integer> clientesNuevosPorAnio = connect.obtenerClientesNuevosUltimos5Anios(App.NombreUsuarioLogeado); // Obtener los datos de clientes nuevos por año de la base de datos
        for (Map.Entry<String, Integer> entry : clientesNuevosPorAnio.entrySet()) { // Recorrer cada entrada en el mapa de datos
            XYChart.Data<String, Double> data = new XYChart.Data<>(entry.getKey(), (double) entry.getValue());  // Crear un nuevo dato para el grafico con el año y la cantidad de clientes

            // Agregar un listener para asegurarnos de que el nodo este creado
            data.nodeProperty().addListener(new ChangeListener<Node>() {
                @Override
                public void changed(ObservableValue<? extends Node> ov, Node oldNode, Node newNode) {
                    if (newNode != null) {  // verificar si el nuevo nodo esta creado
                        Tooltip tooltip = new Tooltip(entry.getValue() + " clientes");  // Crear un Tooltip con el numero de clientes

                        tooltip.setShowDelay(Duration.millis(0)); // sacar delay del tooltip 

                        Tooltip.install(newNode, tooltip);   // poner el tooltip en el nodo del dato
                    }
                }
            });
            graficox.getData().add(data);     // agregar el dato a la serie
        }
        chart.getData().add(graficox);    // agregar la serie de datos al gráfico
    }

    // pongo  y actualizo los datos en el dashboard
    public void UpdateDashboard() {
        int totalClientesActivos = listaClientes.size();
        labelTotalClientes.setText("" + totalClientesActivos);

        int totalClientesDeudores = listaClientesAtrasados.size();
        labelClientesDeudores.setText("" + totalClientesDeudores);

        double dineroRecaudadoEsteMes = connect.obtenerDineroRecaudadoEsteMes(App.NombreUsuarioLogeado); // obtenemos el dinero que recaudo ese usuario en el mes
        labelGananciasMensuales.setText("$" + dineroRecaudadoEsteMes);

        ActualizarGrafico();

    }

    // metodo que pone los datos de la consulta en la tabla
    public void UpdateTable() {
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        col_apellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        col_fechanac.setCellValueFactory(new PropertyValueFactory<>("fechanacimiento"));
        col_telefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        col_telemergencias.setCellValueFactory(new PropertyValueFactory<>("telemergencias"));
        col_fechainicio.setCellValueFactory(new PropertyValueFactory<>("fechainicio"));
        col_vencimientoMembresia.setCellValueFactory(new PropertyValueFactory<>("membresia_hasta"));
        col_email.setCellValueFactory(new PropertyValueFactory<>("email"));

        listaClientes = connect.getDatausers(App.NombreUsuarioLogeado); // obtengo los datos de la consulta  DEL usuario QUE SE LOGEA y los pongo la lista 
        table_clientes.setItems(listaClientes); //pongo la lista en la tabla

    }

    public void UpdateTableClientesAtrasados() {
        col_id1.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_nombre1.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        col_apellidos1.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        col_fechanac1.setCellValueFactory(new PropertyValueFactory<>("fechanacimiento"));
        col_telefono1.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        col_telemergencias1.setCellValueFactory(new PropertyValueFactory<>("telemergencias"));
        col_fechainicio1.setCellValueFactory(new PropertyValueFactory<>("fechainicio"));
        col_vencimientoMembresia1.setCellValueFactory(new PropertyValueFactory<>("membresia_hasta"));
        col_email1.setCellValueFactory(new PropertyValueFactory<>("email"));

        listaClientesAtrasados = connect.getClientesAtrasados(App.NombreUsuarioLogeado); // Obtiene los datos de los clientes atrasados desde la base de datos
        table_clientesAtrasados.setItems(listaClientesAtrasados); // Pone los datos en el TableView
    }

    public void AgregarClientes() throws IOException {

        // comprobacion de campos vacios
        if (RegistroNombre.getText().isEmpty()
                || RegistroApellidos.getText().isEmpty()
                || RegistroNumTel.getText().isEmpty()
                || RegistroTelEmergencias.getText().isEmpty()
                || RegistroEmail.getText().isEmpty()
                || RegistroNacimiento.getValue() == null) {
            AlertUtils.showAlert(AlertType.ERROR, "Error", "Por favor, complete todos los campos");
            return;
        }

        conn = connect.ConnectDb();
        String sql = "INSERT INTO clientes (nombre, apellidos, fechanacimiento, telefono, telemergencias, fechainicio, email, usuario_id) "
                + "SELECT ?, ?, ?, ?, ?, CURRENT_DATE, ?, u.id "
                + "FROM usuarios u "
                + "WHERE u.nombre_usuario = ?";
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, RegistroNombre.getText());
            pst.setString(2, RegistroApellidos.getText());
            pst.setString(4, RegistroNumTel.getText());
            pst.setString(5, RegistroTelEmergencias.getText());
            pst.setString(6, RegistroEmail.getText());

            // Convertir la fecha de DatePicker a String
            LocalDate fechaNacimiento = RegistroNacimiento.getValue();
            String fechaNacimientoStr = fechaNacimiento.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            pst.setString(3, fechaNacimientoStr);

            pst.setString(7, App.NombreUsuarioLogeado); // Nombre de usuario para determinar el gimnasio

            pst.execute();

            UpdateTable(); // Llamo a que se actualice la tabla
            UpdateTableClientesAtrasados(); // Actualizo los clientes atrasados tambien

            // despues de agregar el cliente enviar correo  de bienvenida
            String subject = "¡Bienvenido/a a nuestro gimnasio!";
            String body = "Hola " + RegistroNombre.getText() + ", ¡Bienvenido/a a nuestro gimnasio! Esperamos que disfrutes de tu tiempo con nosotros.\n\n"
                    + "Adjuntamos una rutina de entrenamiento para que puedas comenzar de la mejor manera posible!!!.";

            // determinar cual rutina enviar segun la seleccion en los combobox
            String filePath = obtenerRutinaFileName();

            System.out.println(filePath);
            // Enviar email con rutina adjunta
            EmailSender.sendEmailWithAttachment(RegistroEmail.getText(), subject, body, filePath);

            AlertUtils.showAlert(AlertType.INFORMATION, "Éxito", "Cliente agregado exitosamente");

            // Limpiar campos
            RegistroNombre.setText("");
            RegistroApellidos.setText("");
            RegistroNumTel.setText("");
            RegistroTelEmergencias.setText("");
            RegistroEmail.setText("");
            RegistroNacimiento.setValue(null);

            UpdateDashboard();

        } catch (SQLException e) {
            AlertUtils.showAlert(AlertType.ERROR, "Error", "Error al agregar cliente, revise los datos nuevamente");
            System.out.println("error al agregar cliente " + e.getMessage());
        }

    }

    // metodo para determinar el nombre de archivo de la rutina segun la seleccion de los ComboBox
    private String obtenerRutinaFileName() {
        String genero = ComboBoxGenero.getValue();
        String diasEntreno = ComboBoxDiasEntreno.getValue();
     // verifica si ambos valores de los combobox no son nulos
        if (genero != null && diasEntreno != null) { // si es femenino
            if (genero.equals("Femenino")) {
                if (diasEntreno.equals("3")) {
                    return "Rutina3DIASMujer.pdf";
                } else if (diasEntreno.equals("4")) {
                    return "Rutina4DIASMujer.pdf";
                } else if (diasEntreno.equals("5")) {
                    return "Rutina5DIASMujer.pdf";
                }
            } else { // si es masculino
                if (diasEntreno.equals("3")) {
                    return "Rutina3DIAS.pdf";
                } else if (diasEntreno.equals("4")) {
                    return "Rutina4DIAS.pdf";
                } else if (diasEntreno.equals("5")) {
                    return "Rutina5DIAS.pdf";
                }
            }
        }
        // en caso de no ser ningun de los caso devuelve una rutina default
        return "RutinaFullBody.pdf";
    }

    // metodo para borrrar un cliente de la tableview
    public void DeleteFromTable() {

        clientes selectedCliente = table_clientes.getSelectionModel().getSelectedItem(); // Obtiene el cliente seleccionado
        if (selectedCliente != null) { // Verifica si se ha seleccionado un cliente en la tabla
            String selectedClienteNombre = selectedCliente.getNombre(); // Obtiene el nombre del cliente seleccionado

            // mostrar dialogo de confirmacion 
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmar Eliminación");
            alert.setHeaderText("¿Estás seguro de que deseas eliminar al cliente " + selectedClienteNombre + "?");
            alert.setContentText("Esta acción no se puede deshacer.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Si el usuario da a OK=
                conn = connect.ConnectDb();
                String sql = "DELETE FROM clientes WHERE id = ?";
                try {
                    pst = conn.prepareStatement(sql);
                    pst.setInt(1, selectedCliente.getId());
                    pst.executeUpdate();

                    AlertUtils.showAlert(AlertType.INFORMATION, "Éxito", "Cliente eliminado exitosamente");

                    UpdateTable(); // Actualiza la tabla para reflejar los cambios
                    UpdateTableClientesAtrasados(); // actualizo los clientes atrasados tmb
                    UpdateDashboard();
                } catch (Exception e) {
                    AlertUtils.showAlert(AlertType.ERROR, "Error", "Error al eliminar el cliente: " + e.getMessage());
                }
            }
        } else {
            AlertUtils.showAlert(AlertType.WARNING, "Advertencia", "Por favor, selecciona un cliente para borrar");
        }
    }

    // BOTON PARA QUE AL CLICKEAR EL TABLEVIEW EL CLIENTE PUEDA REGISTRAR UN PAGO DEL CLIENTE
    public void PagarButton() {
        clientes selectedCliente = table_clientes.getSelectionModel().getSelectedItem(); // Obtiene el cliente seleccionado
        if (selectedCliente != null) { // Verifica si se ha seleccionado un cliente en la tabla
            String selectedClienteNombre = selectedCliente.getNombre(); // Obtiene el nombre del cliente seleccionado

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Seleccionar Tipo de Pago");
            alert.setHeaderText("Selecciona el tipo de pago para el cliente: " + selectedClienteNombre);
            alert.setContentText("Elige una opción:");

            ButtonType mensualidadButton = new ButtonType("Mensualidad");
            ButtonType trimestreButton = new ButtonType("Trimestre");
            ButtonType semestreButton = new ButtonType("Semestre");
            ButtonType anoButton = new ButtonType("Año");

            alert.getButtonTypes().setAll(mensualidadButton, trimestreButton, semestreButton, anoButton);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent()) {
                String selectedOption = result.get().getText();
                LocalDate fechaPago = LocalDate.now(); // Obtiene la fecha actual para el pago

                // Calcula el monto del pago dependiendo del tipo de pago seleccionado
                double montoPago = connect.calcularMontoPago(selectedOption, App.NombreUsuarioLogeado);

                // Calcula la fecha de vencimiento de la membresía
                LocalDate membresiaHasta = Metodos.calcularFechaVencimiento(selectedOption, fechaPago);

                // Antes de insertar un nuevo pago, elimina los pagos anteriores para el cliente seleccionado DE ESTE MODO NO SE REPETIRAN EN LA TABLA DEUDORES
                String eliminarPagosAntiguosQuery = "DELETE FROM pagos WHERE cliente_id = ?";
                try {
                    Connection conn = connect.ConnectDb();
                    PreparedStatement eliminarPagosAntiguosStmt = conn.prepareStatement(eliminarPagosAntiguosQuery);
                    eliminarPagosAntiguosStmt.setInt(1, selectedCliente.getId());
                    eliminarPagosAntiguosStmt.executeUpdate();
                    conn.close();
                } catch (SQLException e) {
                    AlertUtils.showAlert(Alert.AlertType.ERROR, "Error", "Error al eliminar pagos antiguos: " + e.getMessage());
                    return; // Si hay un error al eliminar pagos antiguos, salir del método sin insertar el nuevo pago
                }

                // Inserta el pago en la base de datos junto con la fecha de vencimiento
                try {
                    Connection conn = connect.ConnectDb();
                    String sql = "INSERT INTO pagos (cliente_id, fecha_pago, tipo_pago, membresia_hasta, cantidad) VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement pst = conn.prepareStatement(sql);
                    pst.setInt(1, selectedCliente.getId());
                    pst.setDate(2, java.sql.Date.valueOf(fechaPago));
                    pst.setString(3, selectedOption);
                    pst.setDate(4, java.sql.Date.valueOf(membresiaHasta)); // Establece la fecha de vencimiento
                    pst.setDouble(5, montoPago); // Establece el monto del pago
                    pst.executeUpdate();
                    conn.close();

                    String message = "Se ha registrado el pago de tipo " + selectedOption + " para el cliente " + selectedClienteNombre;
                    AlertUtils.showAlert(Alert.AlertType.INFORMATION, "Pago Confirmado", message);
                    UpdateTableClientesAtrasados();
                    UpdateTable();
                    UpdateDashboard();
                } catch (SQLException e) {
                  
                    AlertUtils.showAlert(Alert.AlertType.ERROR, "Error", "Error al registrar el pago en la base de datos.");
                }
            }
        }
    }

    //METODO QUE PERMITE ENCONTRAR UN CLIENTE EN EL TABLEVIEW de clientes
    @FXML
    void filtrarClientes(KeyEvent event) {
        String searchText = Metodos.removeAccents(textfieldFiltro.getText().toLowerCase()); // Obtener el texto del TextField y convertirlo a minúsculas y sin acentos

        ObservableList<clientes> clientesFiltrados = FXCollections.observableArrayList(); // Crear una nueva lista observable para almacenar los clientes filtrados

        for (clientes cliente : listaClientes) { // Iterar sobre la lista de clientes original
            if (Metodos.removeAccents(cliente.getNombre().toLowerCase()).contains(searchText) || Metodos.removeAccents(cliente.getApellidos().toLowerCase()).contains(searchText)) { // Verificar si el nombre o los apellidos del cliente contienen el texto de búsqueda
                clientesFiltrados.add(cliente); // Agregar el cliente a la lista filtrada
            }
        }

        table_clientes.setItems(clientesFiltrados); // Actualizar la tabla con la lista de clientes filtrados
    }

    //METODO QUE PERMITE ENCONTRAR UN CLIENTE EN EL TABLEVIEW de atrasado
    @FXML
    void filtrarClientesAtrasados(KeyEvent event) {
        String searchText = Metodos.removeAccents(textfieldFiltroAtrasados.getText().toLowerCase()); // Obtener el texto del TextField y convertirlo a minúsculas y sin acentos

        ObservableList<clientes> clientesFiltrados2 = FXCollections.observableArrayList(); // Crear una nueva lista observable para almacenar los clientes filtrados

        for (clientes cliente : listaClientesAtrasados) { // Iterar sobre la lista de clientes atrasados
            if (Metodos.removeAccents(cliente.getNombre().toLowerCase()).contains(searchText) || Metodos.removeAccents(cliente.getApellidos().toLowerCase()).contains(searchText)) { // Verificar si el nombre o los apellidos del cliente contienen el texto de búsqueda
                clientesFiltrados2.add(cliente); // Agregar el cliente a la lista filtrada
            }
        }

        table_clientesAtrasados.setItems(clientesFiltrados2); // Actualizar la tabla con la lista de clientes filtrados
    }

    // metodo para seleccionar los clientes de la tabla con un moouseclick 
    @FXML
    void getSelected(MouseEvent event) {
        index = table_clientes.getSelectionModel().getSelectedIndex();
    }

    // metodo para poner la hora en el dashboard
    private void updateLabelHora() {
        // Crear un datetimeformatter para el formato
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd MMM yyyy");

        // Crear un AnimationTimer para actualizar continuamente la hora
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Obtener la hora actual
                LocalDateTime currentTime = LocalDateTime.now();

                // Formatear la hora
                String formattedTime = currentTime.format(formatter);

                // Actualizar el texto del LabelHora
                LabelHora.setText(formattedTime);
            }
        };
        timer.start();
    }

    public void exit(ActionEvent event) { //boton cerrar con x
        Platform.exit();
    }

    // metodo para enviar un correo de asistencia
    @FXML
    void TextSupport(ActionEvent event) {
        // Obtener el email del usuario actualmente logeado
        String usuarioAfectado = App.NombreUsuarioLogeado; // obtengo el nombre del usuario
        String correoUsuario = connect.getEmailUsuario(usuarioAfectado); // Aquí asumo que tienes un método en la clase connect para obtener el email del usuario

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Comunicate con el soporte");
        dialog.setHeaderText("Envía un correo al soporte:");
        dialog.setContentText("Escribe tu mensaje:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(message -> {

            String messageWithUser = "Usuario afectado: " + usuarioAfectado + "\n\n" + "Correo del usuario afectado: " + correoUsuario + "\n\n" + message;
            String recipientEmail = "thegymapp@outlook.com";
            String subject = "Correo de soporte";

            System.out.println("Enviado correo de soporte del usuario: " + usuarioAfectado);
            EmailSender.sendEmail(recipientEmail, subject, messageWithUser);
        });
    }

    // metodo para abrir el ficher de clientes
    private void openClienteWindow(clientes cliente) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("cliente.fxml"));
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);

            stage.initModality(Modality.APPLICATION_MODAL);
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            ClienteController controller = loader.getController();

            controller.initData(cliente); // Pasar los datos del cliente al controlador de la ventana Cliente

            // Funcionalidad para arrastrar la ventana
            scene.setOnMousePressed(event -> {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            });
            scene.setOnMouseDragged(event -> {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            });

            // Listener para el evento de cierre de la ventana ( al cerrar la ventana se actualizan los datos)
            stage.setOnHiding(e -> {
                UpdateTable(); // llamo a que se actualize la tabla 
                UpdateTableClientesAtrasados();
                UpdateDashboard();
            });

            stage.show();
        } catch (IOException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error al abrir la ventana del cliente");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void openUsuarioWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("usuario.fxml"));
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            Scene scene = new Scene(loader.load());

            // Obtener el controlador del FXMLLoader
            UsuarioController controller2 = loader.getController();

            String nombreUsuario = App.NombreUsuarioLogeado;

            // Pasar el nombre de usuario al controlador
            controller2.initData2(nombreUsuario);

            // Funcionalidad para arrastrar la ventana
            scene.setOnMousePressed(event -> {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            });
            scene.setOnMouseDragged(event -> {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            });

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error al abrir la ventana del cliente");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }


    // -------------------CAMBIO DE PANEL CON BOTONES------------------------
    @FXML
    void GoInicio(ActionEvent event) {
        hideAllPanes();
        // UpdateDashboard(); // actualizo el dasbhoard antes de entrar al inicio
        PaneInicio.setVisible(true);
    }

    @FXML
    void GoClientes(ActionEvent event) {
        hideAllPanes();
        PaneClientes.setVisible(true);
    }

    @FXML
    void GoAtrasados(ActionEvent event) {
        hideAllPanes();
        PaneAtrasados.setVisible(true);
    }

    @FXML
    void GoNuevoCliente(ActionEvent event) {
        hideAllPanes();
        PaneNuevoCliente.setVisible(true);
    }

    private void hideAllPanes() { //metodo que oculta todos los paneles
        PaneInicio.setVisible(false);
        PaneClientes.setVisible(false);
        PaneAtrasados.setVisible(false);
        PaneNuevoCliente.setVisible(false);
    }
    // -------------------CAMBIO DE PANEL CON BOTONES------------------------

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

    @FXML
    void LogOut(ActionEvent event) throws IOException {
        App.setRoot("login", 900, 580);
    }

}
