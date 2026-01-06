package controlpacketworld;

import controlpacketworld.interfaz.INotificador;
import dominio.SucursalImp;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
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
    private TableColumn<Sucursal, String> colCodigo;
    @FXML
    private TableColumn<Sucursal, String> colNombre;
    @FXML
    private TableColumn<Sucursal, String> colEstatus;
    @FXML
    private TableColumn<Sucursal, String> colCalle;
    @FXML
    private TableColumn<Sucursal, String> colNumero;
    @FXML
    private TableColumn<Sucursal, String> colColonia;
    @FXML
    private TableColumn<Sucursal, String> colCodigoPostal;
    @FXML
    private TableColumn<Sucursal, String> colCiudad;
    @FXML
    private TableColumn<Sucursal, String> colEstado;
    
    private ObservableList<Sucursal> listaSucursales;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarDatosTabla();
    }

    private void configurarTabla() {
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colEstatus.setCellValueFactory(new PropertyValueFactory<>("estatus"));
        colCalle.setCellValueFactory(new PropertyValueFactory<>("calle"));
        colNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));
        colColonia.setCellValueFactory(new PropertyValueFactory<>("nombreColonia"));
        
        colCodigoPostal.setCellValueFactory(cellData -> 
            new SimpleStringProperty(extraerDatoDireccion(cellData.getValue().getDireccionCompleta(), "CP")));
            
        colCiudad.setCellValueFactory(cellData -> 
            new SimpleStringProperty(extraerDatoDireccion(cellData.getValue().getDireccionCompleta(), "CIUDAD")));
            
        colEstado.setCellValueFactory(cellData -> 
            new SimpleStringProperty(extraerDatoDireccion(cellData.getValue().getDireccionCompleta(), "ESTADO")));
    }
    
    private String extraerDatoDireccion(String direccionCompleta, String tipo) {
        if (direccionCompleta == null || direccionCompleta.isEmpty()) return "";
        try {
            String[] partes = direccionCompleta.split(", ");
            if (tipo.equals("CP")) {
                for (String parte : partes) {
                    if (parte.startsWith("C.P.")) return parte.replace("C.P.", "").trim();
                }
            } else if (tipo.equals("CIUDAD") && partes.length >= 2) {
                return partes[partes.length - 2];
            } else if (tipo.equals("ESTADO") && partes.length >= 1) {
                return partes[partes.length - 1];
            }
        } catch (Exception e) { return ""; }
        return "";
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