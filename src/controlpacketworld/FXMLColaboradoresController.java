/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controlpacketworld;

import controlpacketworld.interfaz.INotificador;
import dominio.ColaboradorImp;
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
import pojo.Colaborador;
import utilidad.Utilidades;

/**
 * FXML Controller class
 *
 * @author julia
 */
public class FXMLColaboradoresController implements Initializable, INotificador {

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
    private TableColumn colRol;
    @FXML
    private TableColumn colSucursal;
    
    private ObservableList<Colaborador> colaboradores;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarInformacionColaboradores();
    }

    private void configurarTabla() {
        colNumPersonal.setCellValueFactory(new PropertyValueFactory("noPersonal"));
        colNombre.setCellValueFactory(new PropertyValueFactory("nombre"));
        colApellidoPaterno.setCellValueFactory(new PropertyValueFactory("apellidoPaterno"));
        colApellidoMaterno.setCellValueFactory(new PropertyValueFactory("apellidoMaterno"));
        colCurp.setCellValueFactory(new PropertyValueFactory("curp"));
        colCorreo.setCellValueFactory(new PropertyValueFactory("correo"));
        colRol.setCellValueFactory(new PropertyValueFactory("rol"));
        colSucursal.setCellValueFactory(new PropertyValueFactory("sucursal"));
    }
    
    private void cargarInformacionColaboradores() {
        HashMap<String, Object> respuesta = ColaboradorImp.obtenerTodos();
        boolean esError = (boolean) respuesta.get("error");
        if (!esError) {
            List<Colaborador> colaboradoresAPI = (List<Colaborador>) respuesta.get("colaboradores");
            colaboradores = FXCollections.observableArrayList();
            colaboradores.addAll(colaboradoresAPI);
            tvColaboradores.setItems(colaboradores);
        } else {
            Utilidades.mostrarAlertaSimple("Error al cargar", "" + respuesta.get("mensaje"), Alert.AlertType.NONE);
        }
    }
    
    private void irFormulario(Colaborador colaborador) {
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("FXMLColaboradorFormulario.fxml"));
        try {
            Parent vista = cargador.load();
            FXMLColaboradorFormularioController controlador = cargador.getController();
            controlador.inicializarDatos(colaborador, this);
            Scene escena = new Scene(vista);
            Stage escenario = new Stage();
            escenario.setScene(escena);
            escenario.setTitle("Formulario colaborador");
            escenario.initModality(Modality.APPLICATION_MODAL);
            escenario.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void eliminarColaborador(int idColaborador) {
        Respuesta respuesta = ColaboradorImp.eliminar(idColaborador);
        if (!respuesta.isError()) {
            Utilidades.mostrarAlertaSimple("Registro eliminado", "El registro del colaborador (a) fue eliminado correctamente", Alert.AlertType.INFORMATION);
            cargarInformacionColaboradores();
        } else {
            Utilidades.mostrarAlertaSimple("Error al eliminar", respuesta.getMensaje(), Alert.AlertType.ERROR);
        }
    }
 
    @FXML
    private void tfBuscador(ActionEvent event) {
    }

    @FXML
    private void clicRegistrar(ActionEvent event) {
        try {
            Parent vista = FXMLLoader.load(getClass().getResource("FXMLColaboradorFormulario.fxml"));
            Scene scRegistrarColaborador = new Scene(vista);
            Stage stRegistrar = new Stage();
            stRegistrar.setScene(scRegistrarColaborador);
            stRegistrar.setTitle("Registrar colaborador");
            stRegistrar.initModality(Modality.APPLICATION_MODAL);
            stRegistrar.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void clicEditar(ActionEvent event) {
        Colaborador colaborador = tvColaboradores.getSelectionModel().getSelectedItem();
        if (colaborador != null) {
            irFormulario(colaborador);
        } else {
            Utilidades.mostrarAlertaSimple("Selecciona un colaborador", "Para editar la información de un colaborador, debe seleccionarlo.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void clicEliminar(ActionEvent event) {
        Colaborador colaborador = tvColaboradores.getSelectionModel().getSelectedItem();
        if (colaborador != null) {
            boolean confirmarOperacion = Utilidades.mostrarAlertaConfirmacion("Eliminar colaborador", "¿Estas seguro de eliminar al colaborador (a) " + colaborador.getNombre() + " " + colaborador.getApellidoPaterno() + " " + colaborador.getApellidoMaterno() + "?");
            if (confirmarOperacion) {
                eliminarColaborador(colaborador.getIdColaborador());
            } 
        } else {
            Utilidades.mostrarAlertaSimple("Selecciona un colaborador", "Para eliminar la información de un colaborador, debes seleccionarlo", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void clicBuscar(ActionEvent event) {
    }

    @Override
    public void notificarOperacionExitosa(String operacion, String nombre) {
        System.out.print("Operación: " + operacion + "nombre colaborador: " + nombre);
        cargarInformacionColaboradores();
    }
    
}
