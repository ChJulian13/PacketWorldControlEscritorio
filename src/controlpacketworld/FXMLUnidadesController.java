/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controlpacketworld;

import dominio.UnidadImp;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import pojo.Unidad;
import utilidad.Utilidades;

/**
 * FXML Controller class
 *
 * @author julia
 */
public class FXMLUnidadesController implements Initializable {

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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        configurarTabla();
        cargarInformacionUnidades();
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
    }

    @FXML
    private void clicEditar(ActionEvent event) {
    }

    @FXML
    private void clicDarBaja(ActionEvent event) {
    }
    
}
