/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pojo.Colaborador;
import pojo.Unidad;
import pojo.UnidadBaja;
import utilidad.Utilidades;

/**
 * FXML Controller class
 *
 * @author julia
 */
public class FXMLUnidadesController implements Initializable, INotificador{

    @FXML
    private TableView<Unidad> tvUnidades;
    @FXML
    private TableColumn colMarca;
    @FXML
    private TableColumn colModelo;
    @FXML
    private TableColumn colVin;
    @FXML
    private TableColumn colTipo;
    @FXML
    private TableColumn colNii;
    @FXML
    private TableColumn colAnio;
    @FXML
    private TableColumn colEstatus;
    
    private ObservableList<Unidad> unidades;
    
    private Colaborador colaboradorSesion;
    @FXML
    private TextField tfBarraBusqueda;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        configurarTabla();
        cargarInformacionUnidades();
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
    }
    
    private void cargarInformacionUnidades() {
        HashMap<String, Object> respuesta = UnidadImp.obtenerTodos();
        boolean esError = (boolean) respuesta.get("error");
        if (!esError) {
            List<Unidad> unidadAPI = (List<Unidad>) respuesta.get("unidades");
            unidades = FXCollections.observableArrayList();
            unidades.addAll(unidadAPI);
            tvUnidades.setItems(unidades);
        } else {
            Utilidades.mostrarAlertaSimple("Error al cargar", "" + respuesta.get("mensaje"), Alert.AlertType.NONE);
        }
    }

    @FXML
    private void clicRegistrar(ActionEvent event) {
        irFormulario(null);
    }

    @FXML
    private void clicEditar(ActionEvent event) {
        Unidad unidad = tvUnidades.getSelectionModel().getSelectedItem();
        if (unidad != null) {
            irFormulario(unidad);
        } else {
            Utilidades.mostrarAlertaSimple("Selecciona un colaborador", "Para editar la información de un colaborador, debe seleccionarlo.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void clicDarBaja(ActionEvent event) {
        Unidad unidadSeleccionada = tvUnidades.getSelectionModel().getSelectedItem();
        
        if (unidadSeleccionada != null) {
            javafx.scene.control.TextInputDialog dialogo = new javafx.scene.control.TextInputDialog();
            dialogo.setTitle("Dar de baja unidad");
            dialogo.setHeaderText("Baja de unidad: " + unidadSeleccionada.getMarca() + " " + unidadSeleccionada.getModelo());
            dialogo.setContentText("Motivo de la baja:");

            java.util.Optional<String> resultado = dialogo.showAndWait();
            
            // 3. Si el usuario escribió algo y dio Aceptar
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
        } else {
            cargarInformacionUnidades();
        }
    }

    @Override
    public void enviarObjeto(Object object) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
}
