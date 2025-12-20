
package controlpacketworld;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;


public class FXMLEnvioRegistrarController implements Initializable {

    @FXML
    private ComboBox<String> cbBuscarClientePor;
    @FXML
    private ComboBox<?> cbConductor;
    @FXML
    private ComboBox<?> cbEstado;
    @FXML
    private ComboBox<?> cbCiudad;
    @FXML
    private ComboBox<?> cbMunicipio;
    @FXML
    private ComboBox<?> cbColonia;
    @FXML
    private ComboBox<?> cbSucursal;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbBuscarClientePor.getItems().addAll("Nombre", "ID / Cédula", "Teléfono", "Correo");
        
        // Opción B: Seleccionar un valor por defecto (opcional)
        cbBuscarClientePor.getSelectionModel().selectFirst();
    }    
    
}
