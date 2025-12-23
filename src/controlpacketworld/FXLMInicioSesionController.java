/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controlpacketworld;

import dominio.InicioSesionImp;
import dto.RSAutenticacionAdmin;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pojo.Colaborador;
import utilidad.Utilidades;

/**
 * FXML Controller class
 *
 * @author julia
 */
public class FXLMInicioSesionController implements Initializable {

    @FXML
    private TextField tfNoPersonal;
    @FXML
    private PasswordField pfContrasenia;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void clicIniciarSesion(ActionEvent event) {
        String noPersonal = tfNoPersonal.getText();
        String contrasenia = pfContrasenia.getText();
        
        if(!noPersonal.isEmpty() && !contrasenia.isEmpty()) {
            //irPantallaInicio();
            verificarCredenciales(noPersonal, contrasenia);
        } else {
            Utilidades.mostrarAlertaSimple("Campos requeridos", "Ingrese todos los datos requeridos", Alert.AlertType.WARNING);
        }
    }
    
    private void verificarCredenciales(String noPersonal, String contrasenia) {
        RSAutenticacionAdmin respuesta = InicioSesionImp.verificarCredenciales(noPersonal, contrasenia);
        
        if (!respuesta.isError()) {
            Utilidades.mostrarAlertaSimple("Credenciales verificadas", "Bienvenido (a) administrador (a) " + respuesta.colaborador.getNombre() + " al sistema de PacketWorld.", Alert.AlertType.INFORMATION);
            irPantallaInicio(respuesta.getColaborador()); 
        } else {
            Utilidades.mostrarAlertaSimple("Error de credenciales", respuesta.getMensaje(), Alert.AlertType.ERROR);
        }
    }
    
    private void irPantallaInicio(Colaborador colaborador){
        try {
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("FXMLInicio.fxml"));
            Parent vista = cargador.load();
            FXMLInicioController controlador = cargador.getController();
            controlador.cargarInformacion(colaborador);
            Scene escenaInicio = new Scene(vista);
            
            Stage stInicio = (Stage) tfNoPersonal.getScene().getWindow();
            stInicio.setScene(escenaInicio);
            stInicio.setTitle("Inicio");
            stInicio.show();
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
