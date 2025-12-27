/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controlpacketworld;

import dominio.ColaboradorImp;
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
public class FXMLColaboradoresController implements Initializable {

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
 
    @FXML
    private void tfBuscador(ActionEvent event) {
    }

    @FXML
    private void clicRegistrar(ActionEvent event) {
        try {
            Parent vista = FXMLLoader.load(getClass().getResource("FXMLColaboradorRegistrar.fxml"));
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
    }

    @FXML
    private void clicEliminar(ActionEvent event) {
    }

    @FXML
    private void clicBuscar(ActionEvent event) {
    }
    
}
