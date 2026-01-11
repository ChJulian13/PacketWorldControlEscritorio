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
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pojo.Colaborador;
import pojo.Unidad;
import pojo.UnidadBaja;
import utilidad.Utilidades;

public class FXMLUnidadesController implements Initializable, INotificador {

    @FXML private TableView<Unidad> tvUnidades;
    @FXML private TableColumn colMarca;
    @FXML private TableColumn colModelo;
    @FXML private TableColumn colVin;
    @FXML private TableColumn colTipo;
    @FXML private TableColumn colNii;
    @FXML private TableColumn colAnio;
    @FXML private TableColumn colEstatus;
    @FXML private TableColumn colConductor;
    
    @FXML private TextField tfBarraBusqueda;
    @FXML private Button btnMostrarTodos;

    @FXML private MenuItem miAsignar;
    @FXML private MenuItem miReasignar;
    @FXML private MenuItem miDesasignar;

    private ObservableList<Unidad> unidades;
    private Colaborador colaboradorSesion;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnEliminar;
    @FXML
    private MenuButton mbGestionar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        configurarListenerSeleccion(); 
        cargarInformacionUnidades();
        
        if(btnMostrarTodos != null) {
            btnMostrarTodos.setVisible(false);
        }
    } 
    
    private void configurarListenerSeleccion() {
        habilitarOpcionesMenu(false, false, false, false);

        tvUnidades.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, nuevaUnidad) -> {
            if (nuevaUnidad != null) {
                
                if ("Baja".equalsIgnoreCase(nuevaUnidad.getEstatus())) {
                    mbGestionar.setDisable(true); 
                    habilitarOpcionesMenu(false, false, false, false);
                } else {
                    mbGestionar.setDisable(false);
                    
                    boolean tieneConductor = nuevaUnidad.getIdConductor() != null && nuevaUnidad.getIdConductor() > 0;
                    
                    if (tieneConductor) {
                        habilitarOpcionesMenu(true, false, true, true);
                    } else {
                        habilitarOpcionesMenu(true, true, false, false);
                    }
                }
            } else {
                mbGestionar.setDisable(true);
                habilitarOpcionesMenu(false, false, false, false);
            }
        });
    }

    private void habilitarOpcionesMenu(boolean gestionar, boolean asignar, boolean reasignar, boolean desasignar) {
        if (btnEditar != null) btnEditar.setDisable(!gestionar);
        if (btnEliminar != null) btnEliminar.setDisable(!gestionar);

        if (miAsignar != null) miAsignar.setDisable(!asignar);
        if (miReasignar != null) miReasignar.setDisable(!reasignar);
        if (miDesasignar != null) miDesasignar.setDisable(!desasignar);
    }

    public void inicializarColaborador(Colaborador colaborador) {
        this.colaboradorSesion = colaborador;
    }
    
    private void configurarTabla(){
        colMarca.setCellValueFactory(new PropertyValueFactory("marca"));
        colModelo.setCellValueFactory(new PropertyValueFactory("modelo"));
        colAnio.setCellValueFactory(new PropertyValueFactory("anio"));
        colVin.setCellValueFactory(new PropertyValueFactory("vin"));
        colTipo.setCellValueFactory(new PropertyValueFactory("nombreTipoUnidad"));
        colNii.setCellValueFactory(new PropertyValueFactory("nii"));
        colEstatus.setCellValueFactory(new PropertyValueFactory("estatus"));
        colConductor.setCellValueFactory(new PropertyValueFactory("conductorLegible"));
    }
    
    private void cargarInformacionUnidades() {
        HashMap<String, Object> respuesta = UnidadImp.obtenerUnidadesAsignadas();
        
        boolean esError = (boolean) respuesta.get("error");
        if (!esError) {
            List<Unidad> unidadAPI = (List<Unidad>) respuesta.get("unidades");
            unidades = FXCollections.observableArrayList();
            unidades.addAll(unidadAPI);
            tvUnidades.setItems(unidades);
        } else {
            Utilidades.mostrarAlertaSimple("Error al cargar", "" + respuesta.get("mensaje"), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void clicRegistrar(ActionEvent event) {
        irFormulario(null);
    }

    @FXML
    private void clicEditar(ActionEvent event) {
        Unidad unidad = tvUnidades.getSelectionModel().getSelectedItem();
        if (unidad != null && !"Baja".equalsIgnoreCase(unidad.getEstatus())) {
            irFormulario(unidad);
        } else {
            Utilidades.mostrarAlertaSimple("Atención", "No se puede editar una unidad no seleccionada o dada de baja.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void clicDarBaja(ActionEvent event) {
        Unidad unidadSeleccionada = tvUnidades.getSelectionModel().getSelectedItem();
        
        if (unidadSeleccionada != null) {
            if ("Baja".equalsIgnoreCase(unidadSeleccionada.getEstatus())) return;

            javafx.scene.control.TextInputDialog dialogo = new javafx.scene.control.TextInputDialog();
            dialogo.setTitle("Dar de baja unidad");
            dialogo.setHeaderText("Baja de unidad: " + unidadSeleccionada.getMarca() + " " + unidadSeleccionada.getModelo());
            dialogo.setContentText("Motivo de la baja:");

            java.util.Optional<String> resultado = dialogo.showAndWait();
            
            if (resultado.isPresent() && !resultado.get().trim().isEmpty()) {
                String motivo = resultado.get();
                
                int idColaboradorLogueado = (colaboradorSesion != null) ? colaboradorSesion.getIdColaborador() : 1;
                
                UnidadBaja baja = new pojo.UnidadBaja();
                baja.setIdUnidad(unidadSeleccionada.getIdUnidad());
                baja.setMotivoBaja(motivo);
                baja.setIdColaborador(idColaboradorLogueado);
                
                Respuesta respuesta = UnidadImp.darBaja(baja);
                
                if (!respuesta.isError()) {
                    Utilidades.mostrarAlertaSimple("Éxito", "La unidad ha sido dada de baja correctamente.", Alert.AlertType.INFORMATION);
                    cargarInformacionUnidades(); 
                } else {
                    Utilidades.mostrarAlertaSimple("Error", respuesta.getMensaje(), Alert.AlertType.ERROR);
                }
                
            } else if (resultado.isPresent() && resultado.get().trim().isEmpty()){
                Utilidades.mostrarAlertaSimple("Motivo requerido", "Debes ingresar un motivo para dar de baja la unidad.", Alert.AlertType.WARNING);
            }
        } else {
            Utilidades.mostrarAlertaSimple("Selección requerida", "Selecciona una unidad de la lista para dar de baja.", Alert.AlertType.WARNING);
        }
    }
    
    private void irFormulario(Unidad unidad) {
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("FXMLUnidadFormulario.fxml"));
        try {
            Parent vista = cargador.load();
            FXMLUnidadFormularioController controlador = cargador.getController();
            controlador.inicializarDatos(unidad, this);
            Scene escena = new Scene(vista);
            Stage escenario = new Stage();
            escenario.setScene(escena);
            escenario.setTitle("Formulario unidad");
            escenario.initModality(Modality.APPLICATION_MODAL);
            escenario.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void buscarUnidades(String textoBusqueda) {
        HashMap<String, Object> respuesta = UnidadImp.buscar(textoBusqueda);
        
        boolean esError = (boolean) respuesta.get("error");
        if (!esError) {
            List<Unidad> unidadAPI = (List<Unidad>) respuesta.get("unidades");
            unidades.clear(); 
            unidades.addAll(unidadAPI);
            tvUnidades.setItems(unidades);
        } else {
            Utilidades.mostrarAlertaSimple("Error al buscar", "" + respuesta.get("mensaje"), Alert.AlertType.NONE);
        }
    }
    
    @Override
    public void notificarOperacionExitosa(String operacion, String nombre) {
        System.out.print("Operación: " + operacion + "nombre unidad: " + nombre);
        cargarInformacionUnidades();
    }

    @FXML
    private void clicBuscar(ActionEvent event) {
        String busqueda = tfBarraBusqueda.getText();
     
        if (busqueda != null && !busqueda.trim().isEmpty()) {
            buscarUnidades(busqueda);
            
            if(btnMostrarTodos != null) {
                btnMostrarTodos.setVisible(true);
            }
        } else {
            cargarInformacionUnidades();
            if(btnMostrarTodos != null) {
                btnMostrarTodos.setVisible(false);
            }
        }
    }

    @Override
    public void enviarObjeto(Object object) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @FXML
    private void clicAsignar(ActionEvent event) {
        Unidad unidadSeleccionada = tvUnidades.getSelectionModel().getSelectedItem();
        if (unidadSeleccionada != null) {
            if ("Baja".equalsIgnoreCase(unidadSeleccionada.getEstatus())) return;
            if (unidadSeleccionada.getIdConductor() != null && unidadSeleccionada.getIdConductor() > 0) return;

            irPantallaSeleccionConductor(unidadSeleccionada, false);
        }
    }

    @FXML
    private void clicDesasignar(ActionEvent event) {
        Unidad unidadSeleccionada = tvUnidades.getSelectionModel().getSelectedItem();
        
        if (unidadSeleccionada != null) {
            if (unidadSeleccionada.getIdConductor() != null && unidadSeleccionada.getIdConductor() > 0) {
                
                boolean confirmar = Utilidades.mostrarAlertaConfirmacion("Desasignar", 
                        "¿Estás seguro de quitar a " + unidadSeleccionada.getNombreConductor() + " de la unidad " + unidadSeleccionada.getModelo() + "?");
                
                if (confirmar) {
                    Respuesta respuesta = UnidadImp.desasignarConductor(unidadSeleccionada.getIdConductor());
                    if (!respuesta.isError()) {
                        Utilidades.mostrarAlertaSimple("Éxito", "Unidad liberada correctamente.", Alert.AlertType.INFORMATION);
                        cargarInformacionUnidades(); 
                    } else {
                        Utilidades.mostrarAlertaSimple("Error", respuesta.getMensaje(), Alert.AlertType.ERROR);
                    }
                }
            } 
        }
    }
    
    private void irPantallaSeleccionConductor(Unidad unidad, boolean esReasignacion) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLUnidadAsignacion.fxml"));
            Parent root = loader.load();
            
            FXMLUnidadAsignacionController controller = loader.getController();
            controller.inicializarDatos(unidad, this, esReasignacion);
            
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle(esReasignacion ? "Reasignar Conductor" : "Asignar Conductor");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void clicMostrarTodos(ActionEvent event) {
        tfBarraBusqueda.setText(""); 
        cargarInformacionUnidades(); 
        
        if(btnMostrarTodos != null) {
            btnMostrarTodos.setVisible(false);
        }
    }

    @FXML
    private void clicReasignar(ActionEvent event) {
        Unidad unidadSeleccionada = tvUnidades.getSelectionModel().getSelectedItem();
        
        if (unidadSeleccionada != null) {
            if ("Baja".equalsIgnoreCase(unidadSeleccionada.getEstatus())) return;
            if (unidadSeleccionada.getIdConductor() == null || unidadSeleccionada.getIdConductor() == 0) return;
            
            irPantallaSeleccionConductor(unidadSeleccionada, true);
        } 
    }
}