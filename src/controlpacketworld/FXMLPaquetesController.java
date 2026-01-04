
package controlpacketworld;

import controlpacketworld.interfaz.INotificador;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pojo.Direccion;
import pojo.Envio;
import pojo.Paquete;
import utilidad.Utilidades;


public class FXMLPaquetesController implements Initializable, INotificador{
    
    private boolean esModoEdicion;
    private Envio envio;
    private Direccion direccion;
    private Integer cpOrigen;
    private Integer cpDestino;
    @FXML
    private TableView<Paquete> tvPaquetes;
    @FXML
    private TableColumn<?, ?> colDescripcion;
    @FXML
    private TableColumn<?, ?> colPeso;
    @FXML
    private TableColumn<?, ?> colAlto;
    @FXML
    private TableColumn<?, ?> colAncho;
    @FXML
    private TableColumn<?, ?> colProfundidad;
    @FXML
    private Button clicConfirmar;
    @FXML
    private Label lbCosto;
    @FXML
    private Label lbNoGuia;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        esModoEdicion = false;
    }

    @Override
    public void notificarOperacionExitosa(String operacion, String nombre) {
    }
    @Override
    public void enviarObjeto(Object paquete) {
        
    }
    
    public void cargarInformacion(Envio envio, Direccion direccion, Integer cpSucursal, Integer cpDestino){
        this.envio = envio;
        this.direccion = direccion;
        this.cpOrigen = cpSucursal;
        this.cpDestino = cpDestino;
    }

    @FXML
    private void clicAgregar(ActionEvent event) {
        try {
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("FXMLPaqueteRegistrar.fxml"));
            Parent vista = cargador.load();
            FXMLPaqueteRegistrarController controlador = cargador.getController();
            controlador.cargarInformacion();
            Scene scRegistrarPaquete = new Scene(vista);
            Stage stRegistrarPaquete = new Stage();
            stRegistrarPaquete.setScene(scRegistrarPaquete);
            stRegistrarPaquete.setTitle("Registrar paquete");
            stRegistrarPaquete.initModality(Modality.APPLICATION_MODAL);
            stRegistrarPaquete.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void clicModificar(ActionEvent event) {
        // TODO: validación de que se seleccionó una celda
        try {
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("FXMLPaqueteRegistrar.fxml"));
            Parent vista = cargador.load();
            FXMLPaqueteRegistrarController controlador = cargador.getController();
            controlador.cargarInformacion(); // Enviar paquete
            Scene scRegistrarPaquete = new Scene(vista);
            Stage stRegistrarPaquete = new Stage();
            stRegistrarPaquete.setScene(scRegistrarPaquete);
            stRegistrarPaquete.setTitle("Registrar paquete");
            stRegistrarPaquete.initModality(Modality.APPLICATION_MODAL);
            stRegistrarPaquete.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void clicEliminar(ActionEvent event) {

    }

    @FXML
    private void clicConfirmar(ActionEvent event) {
        Utilidades.mostrarAlertaSimple("Envio", "Envio guardado.", Alert.AlertType.INFORMATION);
    }

    @FXML
    private void clicCancelar(ActionEvent event) {
        Stage escenario = (Stage) tvPaquetes.getScene().getWindow();
        escenario.close();
    }
    
}
