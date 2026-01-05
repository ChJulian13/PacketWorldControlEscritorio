/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controlpacketworld;

import dominio.ColaboradorImp;
import dto.Respuesta;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import utilidad.Utilidades;

/**
 * FXML Controller class
 *
 * @author julia
 */
public class FXMLColaboradorContraseniaController implements Initializable {

    @FXML
    private PasswordField pfActual;
    @FXML
    private PasswordField pfNueva;
    @FXML
    private PasswordField pfConfirmar;
    
    private int idColaborador;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    
    
    public void inicializarDatos(int idColaborador) {
        this.idColaborador = idColaborador;
    }

    @FXML
    private void clicGuardar(ActionEvent event) {
        String passActual = pfActual.getText();
        String passNueva = pfNueva.getText();
        String passConfirmar = pfConfirmar.getText();
                if (passActual.isEmpty() || passNueva.isEmpty() || passConfirmar.isEmpty()) {
            Utilidades.mostrarAlertaSimple("Campos vacíos", "Todos los campos son obligatorios.", Alert.AlertType.WARNING);
            return;
        }
        
        if (!passNueva.equals(passConfirmar)) {
            Utilidades.mostrarAlertaSimple("Error de validación", "Las nuevas contraseñas no coinciden.", Alert.AlertType.WARNING);
            return;
        }
        
        if (passActual.equals(passNueva)) {
            Utilidades.mostrarAlertaSimple("Aviso", "La nueva contraseña debe ser diferente a la actual.", Alert.AlertType.WARNING);
            return;
        }
     
        enviarCambioPassword(passActual, passNueva);
    }
    
    private void enviarCambioPassword(String passwordActual, String passwordNueva) {
        Respuesta respuesta = ColaboradorImp.cambiarPassword(idColaborador, passwordActual, passwordNueva);
        
        if (!respuesta.isError()) {
            Utilidades.mostrarAlertaSimple("Éxito", respuesta.getMensaje(), Alert.AlertType.INFORMATION);
            cerrarVentana();
        } else {
            Utilidades.mostrarAlertaSimple("Error", respuesta.getMensaje(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void clicCancelar(ActionEvent event) {
        cerrarVentana();
    }
    
    private void cerrarVentana() {
        Stage escenario = (Stage) pfActual.getScene().getWindow();
        escenario.close();
    }
}