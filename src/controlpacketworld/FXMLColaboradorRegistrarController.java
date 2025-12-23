/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controlpacketworld;

import dominio.CatalogoImp;
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
import pojo.Rol;
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
    private ComboBox<?> cbSurcusal;
    @FXML
    private TextField tfCorreo;
    @FXML
    private TextField tfPassword;
    @FXML
    private ComboBox<Rol> cbRol;
    
    private ObservableList<Rol> roles;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        cargarRolesColaborador();
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

    @FXML
    private void clicCancelar(ActionEvent event) {
    }

    @FXML
    private void clicGuardar(ActionEvent event) {
    }
    
}
 