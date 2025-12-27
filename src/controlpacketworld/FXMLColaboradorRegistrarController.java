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
import javafx.scene.control.ComboBox;
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
public class FXMLColaboradorRegistrarController implements Initializable {

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

    /**
     * Initializes the controller class.
     */
   @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarRolesColaborador();
        cargarSucursalesActivas();
        
        // 1. Estado inicial: Oculto y limpio
        pDatosConductor.setVisible(false);
        tfNumLicencia.setText("");

        // 2. Listener Inteligente
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
                    tfNumLicencia.setText(""); // IMPORTANTE: Borrar lo que haya escrito
                }
            }
        });
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
            registrarColaborador(colaborador);
        }
    }
    
}
 