/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controlpacketworld;

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
            irPantallaInicio();
        } else {
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Campos vacíos");
            //alerta.setHeaderText("Falta información");
            alerta.setContentText("Debes ingresar tu número de personal y tu contraseña para iniciar sesión");
            alerta.show(); //show solo es para permitir cambiar el foco, showandwait solo hasta quee presiones aceptar se perderá el foco
        }
    }
    
    private void irPantallaInicio(){
        try {
            //Crear la Scena - Escena
            //Stage -> Scena -> Parent -> FXML
            Parent vista = FXMLLoader.load(getClass().getResource("FXMLInicio.fxml"));
            Scene escenaPrincipal = new Scene(vista);
            
            // Obtener escenario actual
            Stage stPrincipal = (Stage) tfNoPersonal.getScene().getWindow();
            // Cammbio de escena
            stPrincipal.setScene(escenaPrincipal);
            stPrincipal.setTitle("Inicio");
            stPrincipal.show();
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
