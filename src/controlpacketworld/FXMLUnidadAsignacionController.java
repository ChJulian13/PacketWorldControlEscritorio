package controlpacketworld;

import controlpacketworld.interfaz.INotificador;
import dominio.ColaboradorImp;
import dominio.UnidadImp;
import dto.Respuesta;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import pojo.Colaborador;
import pojo.Unidad;
import utilidad.Constantes;
import utilidad.Utilidades;

public class FXMLUnidadAsignacionController implements Initializable {

    @FXML
    private Label lbUnidad;
    
    @FXML
    private TableView<Colaborador> tvColaboradores;
    @FXML
    private TableColumn colNumPersonal;
    @FXML
    private TableColumn colNombre;
    @FXML
    private TableColumn colApellidoPaterno;
    @FXML
    private TableColumn colApellidoMaterno;
    @FXML
    private TableColumn colCurp;
    @FXML
    private TableColumn colCorreo;
    @FXML
    private TableColumn colNumLicencia;
    @FXML
    private TableColumn colSucursal;

    private Unidad unidadSeleccionada;
    private INotificador observador;
    
    private ObservableList<Colaborador> conductoresDisponibles;
    
    private boolean esReasignacion;
    @FXML
    private TextField tfBarraBusqueda;
    @FXML
    private Button btnMostrarTodos;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        btnMostrarTodos.setVisible(false);
    }
    
    private void configurarTabla() {
        colNumPersonal.setCellValueFactory(new PropertyValueFactory<>("noPersonal"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellidoPaterno.setCellValueFactory(new PropertyValueFactory<>("apellidoPaterno"));
        colApellidoMaterno.setCellValueFactory(new PropertyValueFactory<>("apellidoMaterno"));
        colCurp.setCellValueFactory(new PropertyValueFactory<>("curp"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        colNumLicencia.setCellValueFactory(new PropertyValueFactory<>("numeroLicencia"));
        colSucursal.setCellValueFactory(new PropertyValueFactory<>("sucursal")); 
    }

    public void inicializarDatos(Unidad unidad, INotificador observador, boolean esReasignacion) {
        this.unidadSeleccionada = unidad;
        this.observador = observador;
        this.esReasignacion = esReasignacion; 
        
        if (unidad != null) {
            String accion = esReasignacion ? "Reasignando unidad: " : "Asignando unidad: ";
            lbUnidad.setText(accion + unidad.getMarca() + " " + unidad.getModelo() + " (" + unidad.getVin() + ")");
            cargarListaConductores();
        }
    }
    
    private void cargarListaConductores() {
        HashMap<String, Object> respuesta;
        if (this.esReasignacion) {
            respuesta = ColaboradorImp.obtenerTodosLosConductores();
        } else {
            respuesta = ColaboradorImp.obtenerConductoresDisponibles();
        }
        
        if (!(boolean) respuesta.get(Constantes.KEY_ERROR)) {
            List<Colaborador> lista = (List<Colaborador>) respuesta.get("colaboradores");
            conductoresDisponibles = FXCollections.observableArrayList(lista);
            tvColaboradores.setItems(conductoresDisponibles);
        } else {
            Utilidades.mostrarAlertaSimple("Error", "No se pudieron cargar los conductores.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void clicAsignar(ActionEvent event) {
        Colaborador conductorSeleccionado = tvColaboradores.getSelectionModel().getSelectedItem();
        
        if (conductorSeleccionado == null) {
            Utilidades.mostrarAlertaSimple("Selección requerida", "Por favor selecciona un conductor de la tabla.", Alert.AlertType.WARNING);
            return;
        }

        Respuesta respuesta;
        if (this.esReasignacion) {
            respuesta = UnidadImp.cambiarConductor(unidadSeleccionada.getIdUnidad(), conductorSeleccionado.getIdColaborador());
        } else {
            respuesta = UnidadImp.asignarConductor(unidadSeleccionada.getIdUnidad(), conductorSeleccionado.getIdColaborador());
        }

        if (!respuesta.isError()) {
            Utilidades.mostrarAlertaSimple("Operación Exitosa", respuesta.getMensaje(), Alert.AlertType.INFORMATION);
            if (observador != null) {
                String operacion = esReasignacion ? "reasignación" : "asignación";
                observador.notificarOperacionExitosa(operacion, unidadSeleccionada.getModelo());
            }
            cerrarVentana();
        } else {
            Utilidades.mostrarAlertaSimple("Error", respuesta.getMensaje(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void clicCancelar(ActionEvent event) {
        cerrarVentana();
    }
    
    private void cerrarVentana() {
        Stage stage = (Stage) lbUnidad.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void clicMostrarTodos(ActionEvent event) {
        tfBarraBusqueda.setText("");
        tvColaboradores.setItems(conductoresDisponibles);
        btnMostrarTodos.setVisible(false);
    }

    @FXML
    private void clicBuscar(ActionEvent event) {
        String textoBusqueda = tfBarraBusqueda.getText();
        
        if (textoBusqueda != null && !textoBusqueda.trim().isEmpty()) {
            ObservableList<Colaborador> resultados = FXCollections.observableArrayList();
            
            for (Colaborador c : conductoresDisponibles) {
                String nombreCompleto = (c.getNombre() + " " + c.getApellidoPaterno() + " " + c.getApellidoMaterno()).toLowerCase();
                String noPersonal = c.getNoPersonal().toLowerCase();
                String busqueda = textoBusqueda.toLowerCase();
                
                if (noPersonal.contains(busqueda) || nombreCompleto.contains(busqueda)) {
                    resultados.add(c);
                }
            }
            
            tvColaboradores.setItems(resultados);
            btnMostrarTodos.setVisible(true);
        } else {
            clicMostrarTodos(null);
        }
    }
}