/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controlpacketworld;

import controlpacketworld.interfaz.INotificador;
import dominio.ClienteImp;
import dominio.DireccionImp;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import pojo.Cliente;
import pojo.Direccion;
import utilidad.Constantes;
import utilidad.Utilidades;
import utilidad.Validaciones;


/**
 * FXML Controller class
 *
 * @author julia
 */



public class FXMLClienteRegistrarController implements Initializable {

    @FXML
    private TextField tfNombre;
    @FXML
    private TextField tfApellidoPaterno;
    @FXML
    private TextField tfApellidoMaterno;
    @FXML
    private TextField tfCodigoPostal;
    @FXML
    private TextField tfCalle;
    @FXML
    private TextField tfNumero;
    @FXML
    private TextField tfTelefono;
    @FXML
    private TextField tfCorreo;
    @FXML
    private ComboBox<Direccion> cbColonia;
    private ObservableList<Direccion> direcciones;
    @FXML
    private Button btnRegistrar;
    @FXML
    private Button btnCancelar;
    
    private boolean esEdicion = false;
    private Cliente clienteEdicion;
    private INotificador notificador;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       direcciones = FXCollections.observableArrayList();
       cbColonia.setItems(direcciones);

       configurarComboBoxColonia();
       configurarListenerCodigoPostal();
    }
    
    public void inicializarValores(INotificador notificador, Cliente cliente) {
        this.notificador = notificador;
        this.clienteEdicion = cliente;
        
        if (cliente != null) {
            this.esEdicion = true;
            cargarDatosEdicion();
            btnRegistrar.setText("Actualizar"); 
        }
    }

    private void cargarDatosEdicion() {
        // Cargar datos personales
        tfNombre.setText(clienteEdicion.getNombre());
        tfApellidoPaterno.setText(clienteEdicion.getApellidoPaterno());
        tfApellidoMaterno.setText(clienteEdicion.getApellidoMaterno());
        tfTelefono.setText(clienteEdicion.getTelefono());
        tfCorreo.setText(clienteEdicion.getCorreo());

        // Cargar datos de dirección (Directamente del cliente, sin getDireccion())
        tfCalle.setText(clienteEdicion.getCalle());
        tfNumero.setText(clienteEdicion.getNumero());
        
        String cp = clienteEdicion.getCodigoPostal();
        tfCodigoPostal.setText(cp);
        
        // 1. Cargar las colonias disponibles para ese CP
        cargarInformacionCodigoPostal(cp);
        
        // 2. Seleccionar la colonia correcta en el combo
        // Usamos el idColonia almacenado directamente en Cliente
        Integer idColoniaCliente = clienteEdicion.getIdColonia();
        
        if(idColoniaCliente != null) {
            for(Direccion d : cbColonia.getItems()){
                // Comparamos los IDs
                if(d.getIdColonia() == idColoniaCliente.intValue()){ 
                    cbColonia.getSelectionModel().select(d);
                    break;
                }
            }
        }
    }

    @FXML
    private void clicRegistrar(ActionEvent event) {
        if (!validarCampos()) {
             return; 
        }

        // Creación del objeto Cliente y llenado de datos
        Cliente cliente = new Cliente();
        cliente.setNombre(tfNombre.getText());
        cliente.setApellidoPaterno(tfApellidoPaterno.getText());
        cliente.setApellidoMaterno(tfApellidoMaterno.getText());
        cliente.setTelefono(tfTelefono.getText());
        cliente.setCorreo(tfCorreo.getText());
        
        // Llenamos los datos de dirección en el Cliente (para la lógica plana)
        cliente.setCalle(tfCalle.getText());
        cliente.setNumero(tfNumero.getText());
        cliente.setCodigoPostal(tfCodigoPostal.getText());

        // Objeto Dirección auxiliar para usar los endpoints de Dirección
        Direccion direccionAux = new Direccion();
        direccionAux.setCalle(tfCalle.getText());
        direccionAux.setNumero(tfNumero.getText());
        direccionAux.setCodigoPostal(tfCodigoPostal.getText());
        
        Direccion coloniaSeleccionada = cbColonia.getValue();
        if(coloniaSeleccionada != null){
            // Seteamos IDs en Cliente
            cliente.setIdColonia(coloniaSeleccionada.getIdColonia());
            // Para visualización o lógica extra si hiciera falta
            cliente.setNombreColonia(coloniaSeleccionada.getColonia());
            
            // Seteamos en el auxiliar
            direccionAux.setIdColonia(coloniaSeleccionada.getIdColonia());
            direccionAux.setColonia(coloniaSeleccionada.getColonia());
        }

        if (esEdicion) {
            // --- LÓGICA DE EDICIÓN ---
            // Recuperamos IDs críticos
            cliente.setIdCliente(clienteEdicion.getIdCliente());
            cliente.setIdDireccion(clienteEdicion.getIdDireccion()); // ID FK en cliente
            
            // ID Primario de la dirección para editarla
            direccionAux.setIdDireccion(clienteEdicion.getIdDireccion());
            
            actualizarCliente(cliente, direccionAux);
        } else {
            // --- LÓGICA DE REGISTRO ---
            // En el registro, normalmente se envía el objeto Cliente y el backend maneja la creación de la dirección
            // O se usa el objeto direccionAux si tu backend lo requiere separado, 
            // pero basándome en tu código anterior, el registro usa solo 'cliente'.
            
            // Pasamos la dirección auxiliar dentro del cliente si tu backend espera un objeto anidado en el registro,
            // pero como tu POJO es plano, enviamos el cliente tal cual.
            registrarCliente(cliente);
        }
    }

    private void registrarCliente(Cliente cliente) {
        Mensaje msj = ClienteImp.registrarCliente(cliente);
        if (!msj.isError()) {
            Utilidades.mostrarAlertaSimple("Registro exitoso", "El cliente se registró correctamente", Alert.AlertType.INFORMATION);
            cerrarVentana();
            notificador.notificarOperacion("Se registró un nuevo cliente", true);
        } else {
            Utilidades.mostrarAlertaSimple("Error", msj.getMensaje(), Alert.AlertType.ERROR);
        }
    }

    private void actualizarCliente(Cliente cliente, Direccion direccion) {
        // 1. Actualizar la dirección primero (Endpoint específico de Dirección)
        Mensaje msjDireccion = DireccionImp.editarDireccion(direccion);
        
        if(!msjDireccion.isError()){
            // 2. Si la dirección se actualizó bien, actualizamos los datos personales del cliente
            Mensaje msjCliente = ClienteImp.editarCliente(cliente);
            
            if (!msjCliente.isError()) {
                Utilidades.mostrarAlertaSimple("Éxito", "La información del cliente se actualizó correctamente", Alert.AlertType.INFORMATION);
                cerrarVentana();
                notificador.notificarOperacion("Cliente actualizado: " + cliente.getNombre(), true);
            } else {
                Utilidades.mostrarAlertaSimple("Error al actualizar cliente", msjCliente.getMensaje(), Alert.AlertType.ERROR);
            }
        } else {
             Utilidades.mostrarAlertaSimple("Error al actualizar dirección", msjDireccion.getMensaje(), Alert.AlertType.ERROR);
        }
    }

    private void configurarComboBoxColonia() {
        cbColonia.setConverter(new StringConverter<Direccion>() {
            @Override
            public String toString(Direccion direccion) {
                return direccion == null ? "" : direccion.getColonia();
            }

            @Override
            public Direccion fromString(String string) {
                return null;
            }
        });
    }

    private void configurarListenerCodigoPostal() {
        tfCodigoPostal.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue) {
                    cargarInformacionCodigoPostal(tfCodigoPostal.getText());
                }
            }
        });
    }

    public void cargarInformacionCodigoPostal(String codigoPostal){
        if( Validaciones.esVacio(codigoPostal) ){
            cbColonia.getItems().clear();
            return;
        }
        if( !Validaciones.esNumericoConLongitud(codigoPostal, 5) ){
            Utilidades.mostrarAlertaSimple("Codigo postal", "Introduzca un código postal valido.", Alert.AlertType.WARNING);
            cbColonia.getItems().clear();
            return;
        }
        
        HashMap<String, Object> respuesta = DireccionImp.obtenerDireccion(codigoPostal);
        boolean esError = (boolean) respuesta.get(Constantes.KEY_ERROR);
        
        if( !esError ){
            List<Direccion> direccionesAPI = (List<Direccion>) respuesta.get(Constantes.KEY_LISTA);
            
            if( direccionesAPI.isEmpty() ){
                Utilidades.mostrarAlertaSimple("Sin coincidencias", "No se encontraron resultados para el código postal introducido.", Alert.AlertType.INFORMATION);
                direcciones.clear();
                return;
            }
            
            direcciones.clear();
            direcciones.addAll(direccionesAPI);
            cbColonia.getSelectionModel().selectFirst();
        } else {
            Utilidades.mostrarAlertaSimple("Error", "Error al cargar las colonias: " + respuesta.get(Constantes.KEY_MENSAJE), Alert.AlertType.ERROR);
        }
    }
    
    @FXML
    private void clicCancelar(ActionEvent event) {
        cerrarVentana();
    }

    private boolean validarCampos() {
        if (tfNombre.getText().trim().isEmpty() || tfApellidoPaterno.getText().trim().isEmpty() ||
            tfTelefono.getText().trim().isEmpty() || tfCorreo.getText().trim().isEmpty() ||
            tfCalle.getText().trim().isEmpty() || tfCodigoPostal.getText().trim().isEmpty()) {
            
            Utilidades.mostrarAlertaSimple("Campos vacíos", 
                    "Por favor llena todos los campos obligatorios (*).", 
                    Alert.AlertType.WARNING);
            return false;
        }

        if (cbColonia.getSelectionModel().getSelectedItem() == null) {
            Utilidades.mostrarAlertaSimple("Colonia requerida", 
                    "Por favor ingresa un CP válido y selecciona una colonia.", 
                    Alert.AlertType.WARNING);
            return false;
        }
        
        if (!Validaciones.esCorreoValido(tfCorreo.getText())) {
            Utilidades.mostrarAlertaSimple("Correo inválido", 
                    "El correo electrónico no tiene un formato correcto.", 
                    Alert.AlertType.WARNING);
            return false;
        }
        
        if (!Validaciones.esNumericoConLongitud(tfTelefono.getText(), 10)) {
            Utilidades.mostrarAlertaSimple("Teléfono inválido", 
                    "El teléfono debe contener exactamente 10 números.", 
                    Alert.AlertType.WARNING);
            return false;
        }

        return true;
    }

    private void cerrarVentana() {
        Stage stage = (Stage) tfNombre.getScene().getWindow();
        stage.close();
    }
}