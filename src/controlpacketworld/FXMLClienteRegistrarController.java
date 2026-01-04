/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controlpacketworld;

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
    private void clicRegistrar(ActionEvent event) {
        if (validarCampos()) {
            registrarCliente();
        }
    }

    @FXML
    private void clicCancelar(ActionEvent event) {
        cerrarVentana();
    }

    private void registrarCliente() {
        Cliente cliente = new Cliente();
        cliente.setNombre(tfNombre.getText());
        cliente.setApellidoPaterno(tfApellidoPaterno.getText());
        cliente.setApellidoMaterno(tfApellidoMaterno.getText());
        cliente.setTelefono(tfTelefono.getText());
        cliente.setCorreo(tfCorreo.getText());
        cliente.setCalle(tfCalle.getText());
        cliente.setNumero(tfNumero.getText());
        
        Direccion direccionSeleccionada = cbColonia.getSelectionModel().getSelectedItem();
        
        if (direccionSeleccionada != null) {
            cliente.setIdColonia(direccionSeleccionada.getIdColonia());
        }

        HashMap<String, Object> respuesta = ClienteImp.registrarCliente(cliente);
        
        boolean error = (boolean) respuesta.get(Constantes.KEY_ERROR);
        String mensaje = (String) respuesta.get(Constantes.KEY_MENSAJE);

        if (!error) {
            Utilidades.mostrarAlertaSimple("Registro Exitoso", 
                    "El cliente se ha guardado correctamente.", 
                    Alert.AlertType.INFORMATION);
            cerrarVentana();
        } else {
            Utilidades.mostrarAlertaSimple("Error al registrar", 
                    mensaje, 
                    Alert.AlertType.ERROR);
        }
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
        
        // 4. Validar teléfono (10 dígitos)
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


