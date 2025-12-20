
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


public class FXMLEnvioController implements Initializable {


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    

    @FXML
    private void clicBuscarEnvio(ActionEvent event) {
    }

    @FXML
    private void clicCrearEnvio(ActionEvent event) {
        try {
            Parent vista = FXMLLoader.load(getClass().getResource("FXMLEnvioRegistrar.fxml"));
            Scene scEnvio = new Scene(vista);
            Stage stEnvio = new Stage();
            stEnvio.setScene(scEnvio);
            stEnvio.setTitle("Registrar envío");
            stEnvio.initModality(Modality.APPLICATION_MODAL);
            stEnvio.showAndWait();
        } catch (IOException ex) {
           ex.printStackTrace();
        }
    }

    @FXML
    private void clicEditarEnvio(ActionEvent event) {
        try {
            Parent vista = FXMLLoader.load(getClass().getResource("FXMLEnvioRegistrar.fxml"));
            Scene scEnvio = new Scene(vista);
            Stage stEnvio = new Stage();
            stEnvio.setScene(scEnvio);
            stEnvio.setTitle("Editar envío");
            stEnvio.initModality(Modality.APPLICATION_MODAL);
            stEnvio.showAndWait();
        } catch (IOException ex) {
           ex.printStackTrace();
        }
    }

    @FXML
    private void clicGestionarPaquetes(ActionEvent event) {
        try {
            Parent vista = FXMLLoader.load(getClass().getResource("FXMLPaquetes.fxml"));
            Scene scEnvio = new Scene(vista);
            Stage stEnvio = new Stage();
            stEnvio.setScene(scEnvio);
            stEnvio.setTitle("Gestionar paquetes");
            stEnvio.initModality(Modality.APPLICATION_MODAL);
            stEnvio.showAndWait();
        } catch (IOException ex) {
           ex.printStackTrace();
        }
    }
    
}
