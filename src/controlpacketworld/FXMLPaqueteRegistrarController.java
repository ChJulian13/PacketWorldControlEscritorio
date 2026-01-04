
package controlpacketworld;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLPaqueteRegistrarController implements Initializable {

    @FXML
    private TextField tfPeso;
    @FXML
    private TextArea taDescripcion;
    @FXML
    private TextField tfAncho;
    @FXML
    private TextField tfProfundidad;
    @FXML
    private TextField tfAlto;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    public void cargarInformacion(){
        
    }

    @FXML
    private void clicAgregar(ActionEvent event) {
    }

    @FXML
    private void clicCancelar(ActionEvent event) {
    }
    
}
