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
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author julia
 */
public class FXMLColaboradoresController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void tfBuscador(ActionEvent event) {
    }

    @FXML
    private void clicRegistrar(ActionEvent event) {
        try {
            Parent vista = FXMLLoader.load(getClass().getResource("FXMLColaboradorRegistrar.fxml"));
            Scene scRegistrarColaborador = new Scene(vista);
            Stage stRegistrar = new Stage();
            stRegistrar.setScene(scRegistrarColaborador);
            stRegistrar.setTitle("Registrar colaborador");
            stRegistrar.initModality(Modality.APPLICATION_MODAL);
            stRegistrar.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void clicEditar(ActionEvent event) {
    }

    @FXML
    private void clicEliminar(ActionEvent event) {
    }

    @FXML
    private void clicBuscar(ActionEvent event) {
    }
    
}
