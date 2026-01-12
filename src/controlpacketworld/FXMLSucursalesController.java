package controlpacketworld;

import controlpacketworld.interfaz.INotificador;
import dominio.SucursalImp;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pojo.Sucursal;
import utilidad.Constantes;
import utilidad.Utilidades;

public class FXMLSucursalesController implements Initializable, INotificador {

    @FXML
    private TableView<Sucursal> tvSucursales;
    @FXML
    private TableColumn colCodigo;
    @FXML
    private TableColumn colNombre;
    @FXML
    private TableColumn colEstatus;
    @FXML
    private TableColumn colCalle;
    @FXML
    private TableColumn colNumero;
    @FXML
    private TableColumn colColonia;
    @FXML
    private TableColumn colCP;
    @FXML
    private TableColumn colCiudad;
    @FXML
    private TableColumn colEstado;
    
    private ObservableList<Sucursal> listaSucursales;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarDatosTabla();
    }

    private void configurarTabla() {
        colCodigo.setCellValueFactory(new PropertyValueFactory("codigo"));
        colNombre.setCellValueFactory(new PropertyValueFactory("nombre"));
        colEstatus.setCellValueFactory(new PropertyValueFactory("estatus"));
        
        colCalle.setCellValueFactory(new PropertyValueFactory("calle"));
        colNumero.setCellValueFactory(new PropertyValueFactory("numero"));
        colColonia.setCellValueFactory(new PropertyValueFactory("nombreColonia"));
        colCP.setCellValueFactory(new PropertyValueFactory("cp"));
        colCiudad.setCellValueFactory(new PropertyValueFactory("ciudad"));
        colEstado.setCellValueFactory(new PropertyValueFactory("estado"));
    }

    private void cargarDatosTabla() {
        listaSucursales = FXCollections.observableArrayList();
        List<Sucursal> sucursalesWS = SucursalImp.obtenerSucursales();
        if (sucursalesWS != null) {
            listaSucursales.addAll(sucursalesWS);
            tvSucursales.setItems(listaSucursales);
        } else {
            Utilidades.mostrarAlertaSimple("Error de conexión", "No se pudo cargar la lista de sucursales.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void clicRegistrar(ActionEvent event) {
        irFormulario(false, null);
    }

    @FXML
    private void clicActualizar(ActionEvent event) {
        Sucursal sucursal = tvSucursales.getSelectionModel().getSelectedItem();
        if (sucursal != null) {
            irFormulario(true, sucursal);
        } else {
            Utilidades.mostrarAlertaSimple("Selección requerida", "Selecciona una sucursal para editar.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void clicEliminar(ActionEvent event) {
        Sucursal sucursal = tvSucursales.getSelectionModel().getSelectedItem();
        if (sucursal != null) {
            boolean confirmar = Utilidades.mostrarAlertaConfirmacion("Eliminar Sucursal", 
                "¿Estás seguro de eliminar la sucursal " + sucursal.getNombre() + "?");
            
            if (confirmar) {
                HashMap<String, Object> respuesta = SucursalImp.eliminarSucursal(sucursal.getIdSucursal());
                if (!(boolean) respuesta.get(Constantes.KEY_ERROR)) {
                    Utilidades.mostrarAlertaSimple("Sucursal eliminada", (String) respuesta.get(Constantes.KEY_MENSAJE), Alert.AlertType.INFORMATION);
                    cargarDatosTabla();
                } else {
                    Utilidades.mostrarAlertaSimple("Error", (String) respuesta.get(Constantes.KEY_MENSAJE), Alert.AlertType.ERROR);
                }
            }
        } else {
            Utilidades.mostrarAlertaSimple("Selección requerida", "Selecciona una sucursal para eliminar.", Alert.AlertType.WARNING);
        }
    }

    private void irFormulario(boolean esEdicion, Sucursal sucursal) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLSucursalRegistrar.fxml"));
            Parent root = loader.load();
            
            FXMLSucursalRegistrarController controlador = loader.getController();
            controlador.inicializarValores(this, sucursal);
            
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(esEdicion ? "Editar Sucursal" : "Registrar Sucursal");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();
            Utilidades.mostrarAlertaSimple("Error", "No se pudo cargar el formulario.", Alert.AlertType.ERROR);
        }
    }

    @Override
    public void notificarOperacionExitosa(String operacion, String nombre) {
        cargarDatosTabla();
        Utilidades.mostrarAlertaSimple("Operación exitosa", "La sucursal " + nombre + " se ha " + operacion + " correctamente.", Alert.AlertType.INFORMATION);
    }

    @Override
    public void enviarObjeto(Object object) {}
}