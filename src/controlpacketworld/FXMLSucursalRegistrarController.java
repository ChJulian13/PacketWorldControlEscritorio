package controlpacketworld;

import com.google.gson.reflect.TypeToken;
import controlpacketworld.interfaz.INotificador;
import dominio.DireccionImp;
import dominio.SucursalImp;
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
import pojo.Direccion;
import pojo.Sucursal;
import utilidad.Constantes;
import utilidad.GsonUtil;
import utilidad.Utilidades;
import utilidad.Validaciones;

public class FXMLSucursalRegistrarController implements Initializable {

    @FXML
    private TextField tfNombre;
    @FXML
    private TextField tfCodigo;
    @FXML
    private TextField tfCalle;
    @FXML
    private TextField tfNumero;
    @FXML
    private TextField tfCodigoPostal;
    @FXML
    private TextField tfEstado;
    @FXML
    private TextField tfCiudad;
    
    @FXML
    private ComboBox<Direccion> cbColonia;
    @FXML
    private ComboBox<String> cbEstatus;
    
    @FXML
    private Button btnRegistrar;
    @FXML
    private Button btnCancelar;
    
    private ObservableList<Direccion> colonias;
    private ObservableList<String> estatusList;
    private boolean esEdicion = false;
    private Sucursal sucursalEdicion;
    private INotificador notificador;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        inicializarComponentes();
    }
    
    private void inicializarComponentes() {
        colonias = FXCollections.observableArrayList();
        cbColonia.setItems(colonias);
        
        cbColonia.setConverter(new StringConverter<Direccion>() {
            @Override
            public String toString(Direccion object) {
                return object != null ? object.getColonia() : "";
            }
            @Override
            public Direccion fromString(String string) { return null; }
        });

        estatusList = FXCollections.observableArrayList("Activa", "Inactiva");
        cbEstatus.setItems(estatusList);

        tfCodigoPostal.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) { 
                cargarColonias(tfCodigoPostal.getText());
            }
        });
    }
    
    public void inicializarValores(INotificador notificador, Sucursal sucursal) {
        this.notificador = notificador;
        this.sucursalEdicion = sucursal;
        this.esEdicion = (sucursal != null);

        if (esEdicion) {
             btnRegistrar.setText("Actualizar");
             cargarDatosEdicion();
             cbEstatus.setDisable(true); 
             tfCodigoPostal.setDisable(true);
        } else {
             cbEstatus.setValue("Activa");
             cbEstatus.setDisable(true);  
        }
    }
    
    private void cargarDatosEdicion() {
        tfNombre.setText(sucursalEdicion.getNombre());
        tfCodigo.setText(sucursalEdicion.getCodigo());
        tfCalle.setText(sucursalEdicion.getCalle());
        tfNumero.setText(sucursalEdicion.getNumero());
        cbEstatus.setValue(sucursalEdicion.getEstatus());
        
        String cp = extraerCP(sucursalEdicion.getDireccionCompleta());
        tfCodigoPostal.setText(cp);
        tfCodigo.setDisable(true);
        
        cargarColonias(cp);
        seleccionarColonia(sucursalEdicion.getIdColonia());
    }
    
    private String extraerCP(String direccion) {
        if(direccion != null && direccion.contains("C.P.")) {
            try {
                int index = direccion.indexOf("C.P.");
                return direccion.substring(index + 5, index + 10).trim();
            } catch (Exception e) { return ""; }
        }
        return "";
    }

    private void cargarColonias(String codigoPostal) {
        colonias.clear();
        tfCiudad.clear();
        tfEstado.clear();
        
        if (codigoPostal != null && codigoPostal.length() == 5) {
            HashMap<String, Object> respuesta = DireccionImp.obtenerDireccion(codigoPostal);
            
            if (!(boolean) respuesta.get(Constantes.KEY_ERROR)) {
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
                if (dir.getIdColonia().equals(idColonia)) {
                    cbColonia.getSelectionModel().select(dir);
                    break;
                }
            }
        });
    }

    @FXML
    private void clicRegistrar(ActionEvent event) {
        if (sonCamposValidos()) {
            Sucursal sucursal = new Sucursal();
            sucursal.setNombre(tfNombre.getText());
            sucursal.setCodigo(tfCodigo.getText());
            sucursal.setCalle(tfCalle.getText());
            sucursal.setNumero(tfNumero.getText());
            sucursal.setEstatus(cbEstatus.getValue());
            
            Direccion colonia = cbColonia.getSelectionModel().getSelectedItem();
            sucursal.setIdColonia(colonia.getIdColonia());
            
            if (esEdicion) {
                sucursal.setIdSucursal(sucursalEdicion.getIdSucursal());
                sucursal.setIdDireccion(sucursalEdicion.getIdDireccion());
                actualizarSucursal(sucursal);
            } else {
                registrarSucursal(sucursal);
            }
        }
    }

    private boolean sonCamposValidos() {
        if (Validaciones.esVacio(tfNombre.getText()) || 
            Validaciones.esVacio(tfCodigo.getText()) || 
            Validaciones.esVacio(tfCalle.getText()) || 
            Validaciones.esVacio(tfNumero.getText()) || 
            Validaciones.esVacio(tfCodigoPostal.getText())) {
            
            Utilidades.mostrarAlertaSimple("Campos vacíos", "Por favor llena todos los campos.", Alert.AlertType.WARNING);
            return false;
        }

        if (!Validaciones.esNumericoConLongitud(tfCodigoPostal.getText(), 5)) {
            Utilidades.mostrarAlertaSimple("C.P. inválido", "El código postal debe tener 5 dígitos.", Alert.AlertType.WARNING);
            return false;
        }

        if (cbColonia.getSelectionModel().getSelectedItem() == null) {
            Utilidades.mostrarAlertaSimple("Selección requerida", "Debes ingresar un CP válido y seleccionar una colonia.", Alert.AlertType.WARNING);
            return false;
        }

        if (cbEstatus.getValue() == null) {
            Utilidades.mostrarAlertaSimple("Estatus requerido", "Debes seleccionar un estatus para la sucursal.", Alert.AlertType.WARNING);
            return false;
        }

        return true;
    }
    
    private void registrarSucursal(Sucursal sucursal) {
        HashMap<String, Object> respuesta = SucursalImp.registrarSucursal(sucursal);
        procesarRespuesta(respuesta, "registrado");
    }

    private void actualizarSucursal(Sucursal sucursal) {
        HashMap<String, Object> respuesta = SucursalImp.editarSucursal(sucursal);
        procesarRespuesta(respuesta, "actualizado");
    }

    private void procesarRespuesta(HashMap<String, Object> respuesta, String accion) {
        if (!(boolean) respuesta.get(Constantes.KEY_ERROR)) {
            if (notificador != null) {
                notificador.notificarOperacionExitosa(accion, tfNombre.getText());
            }
            cerrarVentana();
        } else {
            Utilidades.mostrarAlertaSimple("Error", (String) respuesta.get(Constantes.KEY_MENSAJE), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void clicCancelar(ActionEvent event) {
        cerrarVentana();
    }
    
    private void cerrarVentana() {
        Stage stage = (Stage) tfNombre.getScene().getWindow();
        stage.close();
    }
}