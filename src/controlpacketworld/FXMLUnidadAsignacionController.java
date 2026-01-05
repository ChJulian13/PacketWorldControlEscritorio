/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import pojo.Colaborador;
import pojo.Unidad;
import utilidad.Constantes;
import utilidad.Utilidades;

/**
 * FXML Controller class
 *
 * @author julia
 */
public class FXMLUnidadAsignacionController implements Initializable {

    @FXML
    private Label lbUnidad;
    @FXML
    private ComboBox<Colaborador> cbConductores;
    
    private Unidad unidadSeleccionada;
    private INotificador observador;
    private ObservableList<Colaborador> conductoresDisponibles;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbConductores.setConverter(new StringConverter<Colaborador>() {
            @Override
            public String toString(Colaborador c) {
                return (c != null) ? c.getNombre() + " " + c.getApellidoPaterno() +  " " + c.getApellidoMaterno() : null;
            }

            @Override
            public Colaborador fromString(String string) {
                return null; 
            }
        });
    }

    public void inicializarDatos(Unidad unidad, INotificador observador) {
        this.unidadSeleccionada = unidad;
        this.observador = observador;
        
        if (unidad != null) {
            lbUnidad.setText(unidad.getMarca() + " " + unidad.getModelo() + " (" + unidad.getVin() + ")");
            cargarConductoresDisponibles();
        }
    }
    
    private void cargarConductoresDisponibles() {
        HashMap<String, Object> respuesta = ColaboradorImp.obtenerTodosLosConductores();
        
        if (!(boolean) respuesta.get(Constantes.KEY_ERROR)) {
            List<Colaborador> lista = (List<Colaborador>) respuesta.get("colaboradores");
            conductoresDisponibles = FXCollections.observableArrayList(lista);
            cbConductores.setItems(conductoresDisponibles);
        } else {
            Utilidades.mostrarAlertaSimple("Error", "No se pudieron cargar los conductores.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void clicAsignar(ActionEvent event) {
        Colaborador conductorSeleccionado = cbConductores.getSelectionModel().getSelectedItem();
        
        if (conductorSeleccionado == null) {
            Utilidades.mostrarAlertaSimple("Selección requerida", "Por favor selecciona un conductor de la lista.", Alert.AlertType.WARNING);
            return;
        }

        Respuesta respuesta = UnidadImp.cambiarConductor(unidadSeleccionada.getIdUnidad(), conductorSeleccionado.getIdColaborador());

        if (!respuesta.isError()) {
            Utilidades.mostrarAlertaSimple("Operación Exitosa", respuesta.getMensaje(), Alert.AlertType.INFORMATION);
            if (observador != null) {
                observador.notificarOperacionExitosa("asignacion", unidadSeleccionada.getModelo());
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
}