/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controlpacketworld;

import controlpacketworld.interfaz.INotificador;
import dominio.CatalogoImp;
import dominio.ColaboradorImp;
import dominio.SucursalImp;
import dto.Respuesta;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import pojo.Colaborador;
import pojo.Rol;
import pojo.Sucursal;
import utilidad.Constantes;
import utilidad.Utilidades;

/**
 * FXML Controller class
 *
 * @author julia
 */
public class FXMLColaboradorFormularioController implements Initializable {

    @FXML
    private TextField tfNombre;
    @FXML
    private TextField tfApellidoPaterno;
    @FXML
    private TextField tfApellidoMaterno;
    @FXML
    private TextField tfCurp;
    @FXML
    private TextField tfNumPersonal;
    @FXML
    private ComboBox<Sucursal> cbSurcusal;
    @FXML
    private TextField tfCorreo;
    @FXML
    private TextField tfPassword;
    @FXML
    private ComboBox<Rol> cbRol;
    
    private ObservableList<Rol> roles;
    
    private ObservableList<Sucursal> sucursales;
    
    private INotificador observador;
    @FXML
    private TextField tfNumLicencia;
    @FXML
    private Pane pDatosConductor;
    
    private Colaborador colaboradorEdicion;
    @FXML
    private Label lbPassword;
    @FXML
    private CheckBox cbCambiarPassword;

    /**
     * Initializes the controller class.
     */
   @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarRolesColaborador();
        cargarSucursalesActivas();
        
        pDatosConductor.setVisible(false);
        tfNumLicencia.setText("");
        
        cbCambiarPassword.selectedProperty().addListener((obs, oldVal, newVal) -> {
            tfPassword.setDisable(!newVal);
            if (!newVal) {
                tfPassword.setText("");
            }
        });

        cbRol.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Obtenemos el nombre del rol y quitamos espacios en blanco por si acaso
                String nombreRol = newValue.getRol().trim(); 

                if ("Conductor".equalsIgnoreCase(nombreRol)) {
                    // CASO A: Es Conductor -> MOSTRAR
                    pDatosConductor.setVisible(true);
                    // Opcional: Solicitar foco en el campo de licencia para agilizar
                    // tfNumLicencia.requestFocus(); 
                } else {
                    // CASO B: Es cualquier otro rol (Admin, Ejecutivo, etc.) -> OCULTAR
                    pDatosConductor.setVisible(false);
                    tfNumLicencia.setText(""); // Borrar lo que haya escrito
                }
            }
        });
    }
    
    public void inicializarDatos(Colaborador colaboradorEdicion, INotificador observador){
        this.colaboradorEdicion = colaboradorEdicion;
        this.observador = observador;
        if (colaboradorEdicion != null) {
            tfNombre.setText(colaboradorEdicion.getNombre());
            tfApellidoPaterno.setText(colaboradorEdicion.getApellidoPaterno());
            tfApellidoMaterno.setText(colaboradorEdicion.getApellidoMaterno());
            tfCurp.setText(colaboradorEdicion.getCurp());
            tfCorreo.setText(colaboradorEdicion.getCorreo());
            tfNumLicencia.setText(colaboradorEdicion.getNumeroLicencia());
            int posicionSucursal = obtenerPosicionSucursal(colaboradorEdicion.getIdSucursal());
            cbSurcusal.getSelectionModel().select(posicionSucursal);
            tfNumPersonal.setText(colaboradorEdicion.getNoPersonal());
            int posicionRol = obtenerPosicionRol(colaboradorEdicion.getIdRol());
            cbRol.getSelectionModel().select(posicionRol);
            cbRol.setDisable(true);
            tfNumPersonal.setEditable(false);
            tfNumPersonal.setDisable(true);
            tfPassword.setText("");
            lbPassword.setVisible(false);
            lbPassword.setManaged(false); // Libera el espacio visual
            cbCambiarPassword.setVisible(true);
            cbCambiarPassword.setManaged(true);
            cbCambiarPassword.setSelected(false); // Desmarcado por defecto

            // 3. Preparar campo de texto
            tfPassword.setText("");
            tfPassword.setDisable(true); // Bloqueado hasta que marque el check
        } else {
            lbPassword.setVisible(true);
            lbPassword.setManaged(true);

            // 2. Ocultar el CheckBox
            cbCambiarPassword.setVisible(false);
            cbCambiarPassword.setManaged(false);

            // 3. Campo habilitado siempre
            tfPassword.setDisable(false);
            tfPassword.setText("");
        }
    }
    
    private int obtenerPosicionSucursal(int idSucursal) {
        for (int i = 0; i < sucursales.size(); i++) {
            if (sucursales.get(i).getIdSucursal() == idSucursal)
                return i;
        }
        
        return -1;
    }
    
    private int obtenerPosicionRol(int idRol) {
        for (int i = 0; i < roles.size(); i++) {
            if (roles.get(i).getIdRol() == idRol)
                return i;
        }
        
        return -1;
    }
    
    private void cerrarVentana() {
        ((Stage) tfNombre.getScene().getWindow()).close();
    }
    
    private void registrarColaborador(Colaborador colaborador) {
        Respuesta respuesta = ColaboradorImp.registrar(colaborador);
        if(!respuesta.isError()) {
            Utilidades.mostrarAlertaSimple("Colaborador registrado", respuesta.getMensaje(), Alert.AlertType.INFORMATION);
            observador.notificarOperacionExitosa("registro", colaborador.getNombre());
            cerrarVentana();
        } else {
            Utilidades.mostrarAlertaSimple("Error al registrar", respuesta.getMensaje(), Alert.AlertType.ERROR);
        }
    }

    private void cargarRolesColaborador() {
        HashMap<String, Object> respuesta = CatalogoImp.obtenerRolesSistema();
        if (!(boolean) respuesta.get(Constantes.KEY_ERROR)) {
            List<Rol> rolesAPI = (List<Rol>)respuesta.get(Constantes.KEY_LISTA);
            roles = FXCollections.observableArrayList();
            roles.addAll(rolesAPI);
            cbRol.setItems(roles);
        } else {
            Utilidades.mostrarAlertaSimple("ERROR", respuesta.get(Constantes.KEY_MENSAJE).toString(), Alert.AlertType.NONE);
        }
    }
    
    private void cargarSucursalesActivas() {
        HashMap<String, Object> respuesta = SucursalImp.obtenerSucursalesActivasSistema();
        if (!(boolean) respuesta.get(Constantes.KEY_ERROR)) {
            List<Sucursal> sucursalAPI = (List<Sucursal>)respuesta.get(Constantes.KEY_LISTA);
            sucursales = FXCollections.observableArrayList();
            sucursales.addAll(sucursalAPI);
            cbSurcusal.setItems(sucursales);
        } else {
            Utilidades.mostrarAlertaSimple("ERROR", respuesta.get(Constantes.KEY_MENSAJE).toString(), Alert.AlertType.NONE);
        }
    }
    
    private boolean sonCamposValidos() {
        if (tfNombre.getText().trim().isEmpty() || 
            tfApellidoPaterno.getText().trim().isEmpty() ||
            tfCurp.getText().trim().isEmpty() ||
            tfCorreo.getText().trim().isEmpty() ||
            tfNumPersonal.getText().trim().isEmpty()) {

            Utilidades.mostrarAlertaSimple("Campos vacíos", "Por favor llena todos los campos de texto obligatorios.", Alert.AlertType.WARNING);
            return false;
        }
        
        if (colaboradorEdicion == null && tfPassword.getText().trim().isEmpty()) {
            Utilidades.mostrarAlertaSimple("Contraseña requerida", "Para registrar un nuevo colaborador, la contraseña es obligatoria.", Alert.AlertType.WARNING);
            return false;
        }
        
        // CASO B: Edición (colaboradorEdicion no es null)
        // Solo validamos si el CheckBox está MARCADO.
        if (colaboradorEdicion != null && cbCambiarPassword.isSelected() && tfPassword.getText().trim().isEmpty()) {
            Utilidades.mostrarAlertaSimple("Contraseña requerida", "Si activaste 'Cambiar contraseña', debes escribir la nueva clave.", Alert.AlertType.WARNING);
            return false;
        }

        if (cbSurcusal.getSelectionModel().getSelectedItem() == null) {
            Utilidades.mostrarAlertaSimple("Selección requerida", "Debes seleccionar una Sucursal.", Alert.AlertType.WARNING);
            return false;
        }

        if (pDatosConductor.isVisible() && tfNumLicencia.getText().trim().isEmpty()) {
            Utilidades.mostrarAlertaSimple("Dato requerido", "Para los conductores, el N° de Licencia es obligatorio.", Alert.AlertType.WARNING);
            return false;
        }

        return true;
    }

    @FXML
    private void clicCancelar(ActionEvent event) {
        cerrarVentana();
    }

    @FXML
    private void clicGuardar(ActionEvent event) {
        if (sonCamposValidos()) {
            Colaborador colaborador = new Colaborador();
            colaborador.setNombre(tfNombre.getText());
            colaborador.setApellidoPaterno(tfApellidoPaterno.getText());
            colaborador.setApellidoMaterno(tfApellidoMaterno.getText());
            colaborador.setCurp(tfCurp.getText());
            colaborador.setCorreo(tfCorreo.getText());
            colaborador.setNoPersonal(tfNumPersonal.getText());
            colaborador.setContrasenia(tfPassword.getText());
            Rol rolSeleccionado = cbRol.getSelectionModel().getSelectedItem();
            colaborador.setIdRol(rolSeleccionado.getIdRol());
            Sucursal sucursalSeleccionada = cbSurcusal.getSelectionModel().getSelectedItem();
            colaborador.setIdSucursal(sucursalSeleccionada.getIdSucursal()); 
            
            if (pDatosConductor.isVisible()) {
                colaborador.setNumeroLicencia(tfNumLicencia.getText());
            } else {
                colaborador.setNumeroLicencia(null);
            }
            
            if (colaboradorEdicion == null) {
                registrarColaborador(colaborador);
            } else {
                editarColaborador(colaborador);
            }
        }
    }
    
    private void editarColaborador(Colaborador colaborador) {
        colaborador.setIdColaborador(colaboradorEdicion.getIdColaborador());
        Respuesta respuesta = ColaboradorImp.editar(colaborador);
        if (!respuesta.isError()) {
            Utilidades.mostrarAlertaSimple("Colaborador editado", respuesta.getMensaje(), Alert.AlertType.INFORMATION);
            observador.notificarOperacionExitosa("editar", colaborador.getNombre());
            cerrarVentana();
        } else {
            Utilidades.mostrarAlertaSimple("Error al editar", respuesta.getMensaje(), Alert.AlertType.ERROR);
        }
    }
    
}
 