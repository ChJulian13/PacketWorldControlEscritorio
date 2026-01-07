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
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pojo.Colaborador;
import pojo.Rol;
import pojo.Sucursal;
import utilidad.Constantes;
import utilidad.Utilidades;
import utilidad.Validaciones;

/**
 * FXML Controller class
 *
 * @author julia
 */
public class FXMLColaboradorFormularioController implements Initializable {

    @FXML private TextField tfNombre;
    @FXML private TextField tfApellidoPaterno;
    @FXML private TextField tfApellidoMaterno;
    @FXML private TextField tfCurp;
    @FXML private TextField tfNumPersonal;
    @FXML private ComboBox<Sucursal> cbSurcusal;
    @FXML private TextField tfCorreo;
    
    @FXML private TextField tfPassword; 
    
    @FXML private ComboBox<Rol> cbRol;
    
    private ObservableList<Rol> roles;
    private ObservableList<Sucursal> sucursales;
    private INotificador observador;
    
    @FXML private TextField tfNumLicencia;
    @FXML private Pane pDatosConductor;
    
    private Colaborador colaboradorEdicion;
    
    @FXML private Label lbPassword; 
    
    @FXML private Button btnCambiarPassword; 
    private Colaborador usuarioSesion;
    @FXML
    private TextField tfPasswordConfirmar;
    @FXML
    private Label lbPasswordConfirmar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarRolesColaborador();
        cargarSucursalesActivas();
        
        pDatosConductor.setVisible(false);
        tfNumLicencia.setText("");
        
        cbRol.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                String nombreRol = newValue.getRol().trim(); 
                if ("Conductor".equalsIgnoreCase(nombreRol)) {
                    pDatosConductor.setVisible(true);
                } else {
                    pDatosConductor.setVisible(false);
                    tfNumLicencia.setText(""); 
                }
            }
        });
    }
    
    public void inicializarDatos(Colaborador colaboradorEdicion, INotificador observador, Colaborador usuarioSesion) {
        this.colaboradorEdicion = colaboradorEdicion;
        this.observador = observador;
        this.usuarioSesion = usuarioSesion; 
        if (colaboradorEdicion != null) {
            tfNombre.setText(colaboradorEdicion.getNombre());
            tfApellidoPaterno.setText(colaboradorEdicion.getApellidoPaterno());
            tfApellidoMaterno.setText(colaboradorEdicion.getApellidoMaterno());
            tfCurp.setText(colaboradorEdicion.getCurp());
            tfCorreo.setText(colaboradorEdicion.getCorreo());
            tfNumLicencia.setText(colaboradorEdicion.getNumeroLicencia());
            
            tfNumPersonal.setText(colaboradorEdicion.getNoPersonal());
            tfNumPersonal.setEditable(false);
            tfNumPersonal.setDisable(true);

            int posicionSucursal = obtenerPosicionSucursal(colaboradorEdicion.getIdSucursal());
            cbSurcusal.getSelectionModel().select(posicionSucursal);

            int posicionRol = obtenerPosicionRol(colaboradorEdicion.getIdRol());
            cbRol.getSelectionModel().select(posicionRol);
            cbRol.setDisable(true); 

            tfPassword.setVisible(false);
            tfPassword.setManaged(false); 
            //lbPassword.setVisible(false); 
            //lbPassword.setManaged(false);
            
            tfPasswordConfirmar.setVisible(false);
            tfPasswordConfirmar.setManaged(false);
            lbPasswordConfirmar.setVisible(false);
            lbPasswordConfirmar.setManaged(false);
            
            btnCambiarPassword.setVisible(true);
            btnCambiarPassword.setManaged(true);
            
        } else {
            lbPassword.setVisible(true);
            lbPassword.setManaged(true);
            tfPassword.setVisible(true);
            tfPassword.setManaged(true);
            tfPassword.setDisable(false);
            tfPassword.setText("");
            
            lbPasswordConfirmar.setVisible(true);
            lbPasswordConfirmar.setManaged(true);
            tfPasswordConfirmar.setVisible(true);
            tfPasswordConfirmar.setManaged(true);
            tfPasswordConfirmar.setText("");

            btnCambiarPassword.setVisible(false);
            btnCambiarPassword.setManaged(false);
        }

        if (usuarioSesion != null && "Ejecutivo de tienda".equalsIgnoreCase(usuarioSesion.getRol())) {
            btnCambiarPassword.setDisable(true);
            if (colaboradorEdicion == null) { 
                for (Rol rol : cbRol.getItems()) {
                    if ("Conductor".equalsIgnoreCase(rol.getRol())) {
                        cbRol.getSelectionModel().select(rol);
                        break;
                    }
                }
            }
            cbRol.setDisable(true); 
            
            if (colaboradorEdicion == null) {
                for (Sucursal suc : cbSurcusal.getItems()) {
                    if (suc.getIdSucursal() == usuarioSesion.getIdSucursal()) {
                        cbSurcusal.getSelectionModel().select(suc);
                        break;
                    }
                }
            }
            cbSurcusal.setDisable(true); 
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
        if (Validaciones.esVacio(tfNombre.getText()) || 
            Validaciones.esVacio(tfApellidoPaterno.getText()) ||
            Validaciones.esVacio(tfCurp.getText()) ||
            Validaciones.esVacio(tfCorreo.getText()) ||
            Validaciones.esVacio(tfNumPersonal.getText())) {

            Utilidades.mostrarAlertaSimple("Campos vacíos", "Por favor llena todos los campos de texto obligatorios.", Alert.AlertType.WARNING);
            return false;
        }

        if (!Validaciones.esSoloTexto(tfNombre.getText())) {
            Utilidades.mostrarAlertaSimple("Nombre inválido", "El nombre solo debe contener letras y espacios.", Alert.AlertType.WARNING);
            return false;
        }
        if (!Validaciones.esSoloTexto(tfApellidoPaterno.getText())) {
            Utilidades.mostrarAlertaSimple("Apellido inválido", "El apellido paterno solo debe contener letras y espacios.", Alert.AlertType.WARNING);
            return false;
        }
        if (!Validaciones.esVacio(tfApellidoMaterno.getText()) && !Validaciones.esSoloTexto(tfApellidoMaterno.getText())) {
            Utilidades.mostrarAlertaSimple("Apellido inválido", "El apellido materno solo debe contener letras y espacios.", Alert.AlertType.WARNING);
            return false;
        }

        if (!Validaciones.esNumPersonalValido(tfNumPersonal.getText())) {
            Utilidades.mostrarAlertaSimple("Formato inválido", 
                "El No. de Personal debe iniciar con 'PW' seguido de 3 a 10 números (y no puede ser '000').", 
                Alert.AlertType.WARNING);
            return false;
        }

        if (!Validaciones.esCorreoValido(tfCorreo.getText())) {
            Utilidades.mostrarAlertaSimple("Correo inválido", "Por favor ingresa un correo electrónico válido.", Alert.AlertType.WARNING);
            return false;
        }

        if (!Validaciones.esCurpValida(tfCurp.getText())) {
            Utilidades.mostrarAlertaSimple("CURP inválida", "El formato de la CURP no es correcto. Verifica los caracteres.", Alert.AlertType.WARNING);
            return false;
        }

        if (colaboradorEdicion == null) {
            if (Validaciones.esVacio(tfPassword.getText())) {
                Utilidades.mostrarAlertaSimple("Contraseña requerida", "Para registrar un nuevo colaborador, la contraseña es obligatoria.", Alert.AlertType.WARNING);
                return false;
            }
            if (!tfPassword.getText().equals(tfPasswordConfirmar.getText())) {
                Utilidades.mostrarAlertaSimple("Contraseñas no coinciden", 
                    "La contraseña y la confirmación no son iguales. Por favor verifica.", 
                    Alert.AlertType.WARNING);
                return false;
            }
            if (!Validaciones.esPasswordSegura(tfPassword.getText())) {
                Utilidades.mostrarAlertaSimple("Contraseña insegura", 
                    "La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula, un número y un carácter especial (@#$%^&+=!).", 
                    Alert.AlertType.WARNING);
                return false;
            }
        }

        if (cbSurcusal.getSelectionModel().getSelectedItem() == null) {
            Utilidades.mostrarAlertaSimple("Selección requerida", "Debes seleccionar una Sucursal.", Alert.AlertType.WARNING);
            return false;
        }

        if (cbRol.getSelectionModel().getSelectedItem() == null) {
            Utilidades.mostrarAlertaSimple("Selección requerida", "Debes seleccionar un Rol.", Alert.AlertType.WARNING);
            return false;
        }

        if (pDatosConductor.isVisible()) {
            if (Validaciones.esVacio(tfNumLicencia.getText())) {
                Utilidades.mostrarAlertaSimple("Dato requerido", "Para los conductores, el N° de Licencia es obligatorio.", Alert.AlertType.WARNING);
                return false;
            }
            
            if (!Validaciones.esLicenciaValida(tfNumLicencia.getText())) {
                Utilidades.mostrarAlertaSimple("Licencia inválida", 
                    "El número de licencia contiene caracteres inválidos o longitud incorrecta (Min 5, Max 20).", 
                    Alert.AlertType.WARNING);
                return false;
            }
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
            
            colaborador.setNombre(Utilidades.capitalizarTexto(tfNombre.getText()));
            colaborador.setApellidoPaterno(Utilidades.capitalizarTexto(tfApellidoPaterno.getText()));
            
            if (!tfApellidoMaterno.getText().trim().isEmpty()) {
                colaborador.setApellidoMaterno(Utilidades.capitalizarTexto(tfApellidoMaterno.getText()));
            } else {
                colaborador.setApellidoMaterno("");
            }

            colaborador.setCurp(tfCurp.getText().trim().toUpperCase());         
            colaborador.setCorreo(tfCorreo.getText().trim().toLowerCase());     
            colaborador.setNoPersonal(tfNumPersonal.getText().trim().toUpperCase()); 
            
            Rol rolSeleccionado = cbRol.getSelectionModel().getSelectedItem();
            colaborador.setIdRol(rolSeleccionado.getIdRol());
            
            Sucursal sucursalSeleccionada = cbSurcusal.getSelectionModel().getSelectedItem();
            colaborador.setIdSucursal(sucursalSeleccionada.getIdSucursal()); 
            
            if (pDatosConductor.isVisible()) {
                colaborador.setNumeroLicencia(tfNumLicencia.getText().trim().toUpperCase());
            } else {
                colaborador.setNumeroLicencia(null);
            }
            
            if (colaboradorEdicion == null) {
                colaborador.setContrasenia(tfPassword.getText());
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

    @FXML
    private void clicCambiarPassword(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLColaboradorContrasenia.fxml"));
            Parent root = loader.load();
            
            FXMLColaboradorContraseniaController controlador = loader.getController();
            controlador.inicializarDatos(colaboradorEdicion.getIdColaborador());
            
            Stage escenario = new Stage();
            escenario.setScene(new Scene(root));
            escenario.setTitle("Seguridad");
            
            escenario.initModality(Modality.WINDOW_MODAL);
            escenario.initOwner( btnCambiarPassword.getScene().getWindow() );
            
            escenario.showAndWait();
            
        } catch (IOException ex) {
            ex.printStackTrace();
            Utilidades.mostrarAlertaSimple("Error", "No se pudo abrir la ventana de cambio de contraseña.", Alert.AlertType.ERROR);
        }
    }
}