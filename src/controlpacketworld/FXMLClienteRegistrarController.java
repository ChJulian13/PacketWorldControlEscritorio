/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controlpacketworld;

import com.google.gson.reflect.TypeToken;
import controlpacketworld.interfaz.INotificador;
import dominio.ClienteImp;
import dominio.DireccionImp;
import dto.Respuesta;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
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
import utilidad.GsonUtil;
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
    @FXML
    private Button btnRegistrar;
    @FXML
    private Button btnCancelar;
    
    private ObservableList<Direccion> colonias;
    private boolean esEdicion = false;
    private Cliente clienteEdicion;
    private INotificador notificador;
    @FXML
    private TextField tfEstado;
    @FXML
    private TextField tfCiudad;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colonias = FXCollections.observableArrayList();
        cbColonia.setItems(colonias);
        
        cbColonia.setConverter(new StringConverter<Direccion>() {
            @Override
            public String toString(Direccion object) {
                return object != null ? object.getColonia() : ""; 
            }

            @Override
            public Direccion fromString(String string) {
                return null; 
            }
        });

        tfCodigoPostal.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { 
                cargarColonias(tfCodigoPostal.getText());
            }
        });
        
        tfEstado.setEditable(false);
        tfCiudad.setEditable(false);
    }
    
    public void inicializarValores(INotificador notificador, Cliente cliente) {
        this.notificador = notificador;
        this.clienteEdicion = cliente;
        this.esEdicion = (cliente != null);

        if (esEdicion) {
            btnRegistrar.setText("Actualizar");
            cargarDatosEdicion();    
        }
    }

    private void cargarDatosEdicion() {
        tfNombre.setText(clienteEdicion.getNombre());
        tfApellidoPaterno.setText(clienteEdicion.getApellidoPaterno());
        tfApellidoMaterno.setText(clienteEdicion.getApellidoMaterno());
        tfTelefono.setText(clienteEdicion.getTelefono());
        tfCorreo.setText(clienteEdicion.getCorreo());
        
        tfCalle.setText(clienteEdicion.getCalle());
        tfNumero.setText(clienteEdicion.getNumero());
        tfCodigoPostal.setText(clienteEdicion.getCodigoPostal());

        cargarColonias(clienteEdicion.getCodigoPostal());
        seleccionarColonia(clienteEdicion.getIdColonia());
    }

    private void cargarColonias(String codigoPostal) {
        colonias.clear();
        tfCiudad.clear();
        tfEstado.clear();
        
        if (codigoPostal != null && !codigoPostal.isEmpty() && codigoPostal.length() == 5) {
            
            HashMap<String, Object> respuesta = DireccionImp.obtenerDireccion(codigoPostal);
            
            if( !(boolean) respuesta.get(Constantes.KEY_ERROR)){
                Object listaObjeto = respuesta.get(Constantes.KEY_LISTA);
                
                if (listaObjeto != null) {
                    String jsonLista = GsonUtil.GSON.toJson(listaObjeto);
                    Type tipoLista = new TypeToken<List<Direccion>>(){}.getType();
                    List<Direccion> listaReal = GsonUtil.GSON.fromJson(jsonLista, tipoLista);
                    
                    colonias.addAll(listaReal);
                    
                    if (!listaReal.isEmpty()) {
                        Direccion datosUbicacion = listaReal.get(0);
                        tfCiudad.setText(datosUbicacion.getCiudad());
                        tfEstado.setText(datosUbicacion.getEstado());
                    }
                }
            }
        }
    }

    private void seleccionarColonia(Integer idColonia) {
        Platform.runLater(() -> {
            for (Direccion dir : cbColonia.getItems()) {
                if (dir.getIdColonia() != null && dir.getIdColonia().equals(idColonia)) {
                    cbColonia.getSelectionModel().select(dir);
                    break;
                }
            }
        });
    }

    @FXML
    private void clicCancelar(ActionEvent event) {
        cerrarVentana();
    }
    
    @FXML
    private void clicRegistrar(ActionEvent event) {
        if (sonCamposValidos()) {
            Cliente cliente = new Cliente();
            cliente.setNombre(tfNombre.getText());
            cliente.setApellidoPaterno(tfApellidoPaterno.getText());
            cliente.setApellidoMaterno(tfApellidoMaterno.getText());
            cliente.setTelefono(tfTelefono.getText());
            cliente.setCorreo(tfCorreo.getText());
            
            cliente.setCalle(tfCalle.getText());
            cliente.setNumero(tfNumero.getText());
            cliente.setCodigoPostal(tfCodigoPostal.getText());
            
            Direccion coloniaSeleccionada = cbColonia.getSelectionModel().getSelectedItem();
            if(coloniaSeleccionada != null){
                cliente.setIdColonia(coloniaSeleccionada.getIdColonia());
            }

            if (esEdicion) {
                cliente.setIdCliente(clienteEdicion.getIdCliente());
                cliente.setIdDireccion(clienteEdicion.getIdDireccion()); 
                
                Direccion direccionEdicion = new Direccion();
                direccionEdicion.setIdDireccion(clienteEdicion.getIdDireccion());
                direccionEdicion.setCalle(tfCalle.getText());
                direccionEdicion.setNumero(tfNumero.getText());
                if (coloniaSeleccionada != null) {
                    direccionEdicion.setIdColonia(coloniaSeleccionada.getIdColonia());
                }
                
                Respuesta respuestaDireccion = DireccionImp.editar(direccionEdicion);

                if (!respuestaDireccion.isError()) {
                    actualizarCliente(cliente);
                } else {
                    Utilidades.mostrarAlertaSimple("Error en Dirección", 
                            "No se pudo actualizar la dirección: " + respuestaDireccion.getMensaje(), 
                            Alert.AlertType.ERROR);
                }
            } else {
                registrarCliente(cliente);
            }
        }
    }

    private boolean sonCamposValidos() {
        if (Validaciones.esVacio(tfNombre.getText()) || 
            Validaciones.esVacio(tfApellidoPaterno.getText()) ||
            Validaciones.esVacio(tfTelefono.getText()) || 
            Validaciones.esVacio(tfCorreo.getText()) ||
            Validaciones.esVacio(tfCalle.getText()) || 
            Validaciones.esVacio(tfNumero.getText()) ||
            Validaciones.esVacio(tfCodigoPostal.getText())) {
            
            Utilidades.mostrarAlertaSimple("Campos vacíos", "Por favor llena todos los campos obligatorios.", Alert.AlertType.WARNING);
            return false;
        }

        if (!Validaciones.esSoloTexto(tfNombre.getText()) || 
            !Validaciones.esSoloTexto(tfApellidoPaterno.getText()) ||
            (!tfApellidoMaterno.getText().isEmpty() && !Validaciones.esSoloTexto(tfApellidoMaterno.getText()))) {
            Utilidades.mostrarAlertaSimple("Datos inválidos", "Los campos de nombre y apellidos solo deben contener letras.", Alert.AlertType.WARNING);
            return false;
        }

        if (!Validaciones.esCorreoValido(tfCorreo.getText())) {
            Utilidades.mostrarAlertaSimple("Correo inválido", "Por favor ingresa un formato de correo electrónico válido.", Alert.AlertType.WARNING);
            return false;
        }

        if (!Validaciones.esNumericoConLongitud(tfTelefono.getText(), 10)) {
            Utilidades.mostrarAlertaSimple("Teléfono inválido", "El teléfono debe tener exactamente 10 dígitos numéricos.", Alert.AlertType.WARNING);
            return false;
        }

        if (!Validaciones.esNumericoConLongitud(tfCodigoPostal.getText(), 5)) {
            Utilidades.mostrarAlertaSimple("C.P. inválido", "El código postal debe tener 5 dígitos.", Alert.AlertType.WARNING);
            return false;
        }

        if (cbColonia.getSelectionModel().getSelectedItem() == null) {
            Utilidades.mostrarAlertaSimple("Selección requerida", "Debes seleccionar una colonia de la lista.", Alert.AlertType.WARNING);
            return false;
        }

        return true;
    }

    private void registrarCliente(Cliente cliente) {
        HashMap<String, Object> respuesta = ClienteImp.registrarCliente(cliente);
        procesarRespuesta(respuesta, cliente.getNombre());
    }

    private void actualizarCliente(Cliente cliente) {
        HashMap<String, Object> respuesta = ClienteImp.editarCliente(cliente);
        procesarRespuesta(respuesta, cliente.getNombre());
    }

    private void procesarRespuesta(HashMap<String, Object> respuesta, String nombreCliente) {
        if (!(boolean) respuesta.get(Constantes.KEY_ERROR)) {
            if (notificador != null) {
                notificador.notificarOperacionExitosa(esEdicion ? "actualizado" : "registrado", nombreCliente);
            } else {
                Utilidades.mostrarAlertaSimple("Operación exitosa", (String) respuesta.get(Constantes.KEY_MENSAJE), Alert.AlertType.INFORMATION);
            }
            cerrarVentana();
        } else {
            Utilidades.mostrarAlertaSimple("Error", (String) respuesta.get(Constantes.KEY_MENSAJE), Alert.AlertType.ERROR);
        }
    }

    private void cerrarVentana() {
        Stage stage = (Stage) tfNombre.getScene().getWindow();
        stage.close();
    }
}