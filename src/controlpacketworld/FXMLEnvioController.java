
package controlpacketworld;

import controlpacketworld.interfaz.INotificador;
import dominio.EnvioImp;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pojo.Envio;
import utilidad.Constantes;
import utilidad.Utilidades;
import utilidad.Validaciones;


public class FXMLEnvioController implements Initializable, INotificador{

    private Integer idSucursal;
    @FXML
    private TableView<Envio> tvEnvios;
    @FXML
    private TableColumn<?, ?> colNoGuia;
    @FXML
    private TableColumn<?, ?> colEstatus;
    @FXML
    private TableColumn<?, ?> colSucursal;
    @FXML
    private TableColumn<?, ?> colDestinatarioDireccion;
    @FXML
    private TableColumn<?, ?> colConductor;
    @FXML
    private TableColumn<?, ?> colCosto;

    private ObservableList<Envio> envios;
    
    @FXML
    private TextField tfNoGuia;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        envios = FXCollections.observableArrayList();
        tvEnvios.setItems(envios);
    }
    @Override
    public void notificarOperacionExitosa(String operacion, String nombre) {
        cargarInformacionEnvios(null);
    }
    @Override
    public void enviarObjeto(Object object) {
    }
    
    public void cargarInformacionSucursal(Integer idSucursal){
        this.idSucursal = idSucursal;
        cargarInformacionEnvios(null);
    }

    @FXML
    private void clicBuscarEnvio(ActionEvent event) {
        if ( !Validaciones.esVacio(tfNoGuia.getText())){
            cargarInformacionEnvios(tfNoGuia.getText());
        } else {
            tfNoGuia.setText("");
            Utilidades.mostrarAlertaSimple("Envio", "Introduzca un número de guía.", Alert.AlertType.INFORMATION);
        }
    }

    @FXML
    private void clicCrearEnvio(ActionEvent event) {
        try {
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("FXMLEnvioRegistrar.fxml"));
            Parent vista = cargador.load();
            FXMLEnvioRegistrarController controlador = cargador.getController();
            controlador.cargarInformacion(idSucursal, this);

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
        if (tvEnvios.getSelectionModel().getSelectedItem() != null){
            try {
                FXMLLoader cargador = new FXMLLoader(getClass().getResource("FXMLEnvioRegistrar.fxml"));
                Parent vista = cargador.load();
                FXMLEnvioRegistrarController controlador = cargador.getController();
                controlador.cargarInformacionModoEdicion(tvEnvios.getSelectionModel().getSelectedItem(), this);

                Scene scEnvio = new Scene(vista);
                Stage stEnvio = new Stage();
                stEnvio.setScene(scEnvio);
                stEnvio.setTitle("Registrar envío");
                stEnvio.initModality(Modality.APPLICATION_MODAL);
                stEnvio.showAndWait();
            } catch (IOException ex) {
               ex.printStackTrace();
            }
        } else {
            Utilidades.mostrarAlertaSimple("Envio", "Debe de seleccionar un envío para poder modificarlo.", Alert.AlertType.INFORMATION);
        }
    }

    @FXML
    private void clicGestionarPaquetes(ActionEvent event) {
        if (tvEnvios.getSelectionModel().getSelectedItem() != null){
            try {
                FXMLLoader cargador = new FXMLLoader(getClass().getResource("FXMLPaquetes.fxml"));
                Parent vista = cargador.load();
                FXMLPaquetesController controlador = cargador.getController();
                controlador.cargarInformacion(this, tvEnvios.getSelectionModel().getSelectedItem());

                Scene scEnvio = new Scene(vista);
                Stage stEnvio = new Stage();
                stEnvio.setScene(scEnvio);
                stEnvio.setTitle("Gestionar paquetes");
                stEnvio.initModality(Modality.APPLICATION_MODAL);
                stEnvio.showAndWait();
            } catch (IOException ex) {
               ex.printStackTrace();
            }
        } else {
            Utilidades.mostrarAlertaSimple("Envio", "Debe de seleccionar un envío para poder modificar los paquetes.", Alert.AlertType.INFORMATION);
        }
    }
    
    public void configurarTabla() {
        // Atributos de pojo.Envio que se utilizará en cada columna de la tabla
        colNoGuia.setCellValueFactory(new PropertyValueFactory("noGuia"));
        colEstatus.setCellValueFactory(new PropertyValueFactory("estatus"));
        colSucursal.setCellValueFactory(new PropertyValueFactory("sucursal"));
        colDestinatarioDireccion.setCellValueFactory(new PropertyValueFactory("destinatarioDireccion"));
        colConductor.setCellValueFactory(new PropertyValueFactory("conductor"));
        colCosto.setCellValueFactory(new PropertyValueFactory("costo"));
    }
    
    public void cargarInformacionEnvios(String noGuia) {
        HashMap<String, Object> respuesta;
        if ( noGuia == null ){
            respuesta = EnvioImp.obtenerEnvio(null);
        } else {
             respuesta = EnvioImp.obtenerEnvio(noGuia);
        }

        boolean esError = (boolean) respuesta.get("error");
        if( !esError ){
            List<Envio> enviosAPI = (List<Envio>) respuesta.get(Constantes.KEY_LISTA);
            
            if (enviosAPI.isEmpty()) {
                Utilidades.mostrarAlertaSimple("Sin coincidencias", "No se encontró información para el número de guía: " + noGuia, Alert.AlertType.INFORMATION);
            }
            
            envios.clear();
            envios.addAll(enviosAPI);
            tvEnvios.refresh();
        } else {
            Utilidades.mostrarAlertaSimple("Error al cargar", respuesta.get(Constantes.KEY_MENSAJE).toString(), Alert.AlertType.ERROR);
        }
    }
    
}
