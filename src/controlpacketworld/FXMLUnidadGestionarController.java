package controlpacketworld;

import controlpacketworld.interfaz.INotificador;
import dominio.UnidadImp;
import dto.Respuesta;
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
import pojo.Unidad;
import utilidad.Utilidades;

public class FXMLUnidadGestionarController implements Initializable, INotificador {

    @FXML
    private TableView<Unidad> tvAsignaciones;
    @FXML
    private TableColumn colMarca;
    @FXML
    private TableColumn colModelo;
    @FXML
    private TableColumn colVin;
    @FXML
    private TableColumn colConductor; 

    private ObservableList<Unidad> listaUnidades;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarDatos();
    }

    private void configurarTabla() {
        colMarca.setCellValueFactory(new PropertyValueFactory("marca"));
        colModelo.setCellValueFactory(new PropertyValueFactory("modelo"));
        colVin.setCellValueFactory(new PropertyValueFactory("vin"));
        colConductor.setCellValueFactory(new PropertyValueFactory("conductorLegible"));
    }

    private void cargarDatos() {
        HashMap<String, Object> respuesta = UnidadImp.obtenerUnidadesAsignadas();
        
        if (!(boolean) respuesta.get("error")) {
            List<Unidad> lista = (List<Unidad>) respuesta.get("unidades");
            listaUnidades = FXCollections.observableArrayList(lista);
            tvAsignaciones.setItems(listaUnidades);
        } else {
            Utilidades.mostrarAlertaSimple("Error", (String) respuesta.get("mensaje"), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void clicAsignar(ActionEvent event) {
        Unidad unidadSeleccionada = tvAsignaciones.getSelectionModel().getSelectedItem();
        
        if (unidadSeleccionada != null) {
            irPantallaSeleccionConductor(unidadSeleccionada);
        } else {
            Utilidades.mostrarAlertaSimple("Selección requerida", "Selecciona una unidad para asignar o cambiar su conductor.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void clicDesasignar(ActionEvent event) {
        Unidad unidadSeleccionada = tvAsignaciones.getSelectionModel().getSelectedItem();
        
        if (unidadSeleccionada != null) {
            if (unidadSeleccionada.getIdConductor() != null && unidadSeleccionada.getIdConductor() > 0) {
                
                boolean confirmar = Utilidades.mostrarAlertaConfirmacion("Desasignar", 
                        "¿Estás seguro de quitar a " + unidadSeleccionada.getNombreConductor() + " de la unidad " + unidadSeleccionada.getModelo() + "?");
                
                if (confirmar) {
                    Respuesta respuesta = UnidadImp.desasignarConductor(unidadSeleccionada.getIdConductor());
                    if (!respuesta.isError()) {
                        Utilidades.mostrarAlertaSimple("Éxito", "Unidad liberada correctamente.", Alert.AlertType.INFORMATION);
                        cargarDatos(); 
                    } else {
                        Utilidades.mostrarAlertaSimple("Error", respuesta.getMensaje(), Alert.AlertType.ERROR);
                    }
                }
            } else {
                Utilidades.mostrarAlertaSimple("Aviso", "La unidad seleccionada no tiene conductor asignado.", Alert.AlertType.WARNING);
            }
        } else {
            Utilidades.mostrarAlertaSimple("Selección requerida", "Selecciona una unidad para desasignar.", Alert.AlertType.WARNING);
        }
    }

    private void irPantallaSeleccionConductor(Unidad unidad) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLUnidadAsignacion.fxml"));
            Parent root = loader.load();
            
            FXMLUnidadAsignacionController controller = loader.getController();
            controller.inicializarDatos(unidad, this);
            
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Seleccionar Conductor");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void notificarOperacionExitosa(String operacion, String nombre) {
        cargarDatos(); 
    }

    @Override
    public void enviarObjeto(Object object) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
}