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
import javafx.stage.Modality;
import javafx.stage.Stage;
import utilidad.Utilidades;

/**
 * FXML Controller class
 *
 * @author julia
 */
public class FXMLClientesController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    private void irPantallaFormulario() {
        try {
            // Cargar el archivo FXML de la ventana de registro
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLClienteRegistrar.fxml"));
            Parent root = loader.load();

            // Configurar el escenario (Stage)
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL); // Bloquea la ventana de atrás
            stage.setTitle("Registrar Cliente");
            stage.setScene(scene);
            
            // Mostrar la ventana y esperar a que se cierre
            stage.showAndWait();

            // Opcional: Recargar la tabla de clientes al cerrar la ventana de registro
            // cargarClientes(); 

        } catch (IOException e) {
            Utilidades.mostrarAlertaSimple("Error de navegación", 
                "No se pudo cargar la ventana de registro: " + e.getMessage(), 
                Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void clicRegistrar(ActionEvent event) {
        irPantallaFormulario();
    }
    
}
