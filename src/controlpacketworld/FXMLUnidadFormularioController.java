/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controlpacketworld;

import controlpacketworld.interfaz.INotificador;
import dominio.CatalogoImp;
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
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import pojo.TipoUnidad;
import pojo.Unidad;
import utilidad.Constantes;
import utilidad.Utilidades;
import utilidad.Validaciones;

/**
 * FXML Controller class
 *
 * @author julia
 */
public class FXMLUnidadFormularioController implements Initializable {

    @FXML
    private TextField tfMarca;
    @FXML
    private TextField tfModelo;
    @FXML
    private TextField tfAnio;
    @FXML
    private TextField tfVin;
    @FXML
    private ComboBox<TipoUnidad> cbTipo;
    private INotificador observador;
    private Unidad unidadEdicion;
    private ObservableList<TipoUnidad> tipoUnidad;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        cargarTipoUnidad();
    }   
    
    private boolean sonCamposValidos() {
        if (Validaciones.esVacio(tfMarca.getText()) || 
            Validaciones.esVacio(tfModelo.getText()) || 
            Validaciones.esVacio(tfAnio.getText()) || 
            Validaciones.esVacio(tfVin.getText())) {
            
            Utilidades.mostrarAlertaSimple("Campos vacíos", "Por favor llena todos los campos.", Alert.AlertType.WARNING);
            return false;
        }
        
        if (cbTipo.getSelectionModel().getSelectedItem() == null) {
            Utilidades.mostrarAlertaSimple("Selección requerida", "Debes seleccionar un Tipo de Unidad.", Alert.AlertType.WARNING);
            return false;
        }

        if (!Validaciones.esNumericoConLongitud(tfAnio.getText(), 4)) {
            Utilidades.mostrarAlertaSimple("Año inválido", "El año debe ser un número de 4 dígitos.", Alert.AlertType.WARNING);
            return false;
        }
        int anio = Integer.parseInt(tfAnio.getText());
        int anioActual = java.time.Year.now().getValue();
        if (anio < 1980 || anio > (anioActual + 1)) {
            Utilidades.mostrarAlertaSimple("Año fuera de rango", "Ingresa un año válido (1980 - " + (anioActual + 1) + ").", Alert.AlertType.WARNING);
            return false;
        }

        String vin = tfVin.getText().trim();
        if (vin.length() != 17 || !vin.matches("[A-Z0-9]+")) { 
            Utilidades.mostrarAlertaSimple("VIN inválido", "El VIN debe tener exactamente 17 caracteres alfanuméricos.", Alert.AlertType.WARNING);
            return false;
        }

        return true;
    }
    
    @FXML
    private void clicGuardar(ActionEvent event) {
        if (sonCamposValidos()) {
            Unidad unidad = new Unidad();
            unidad.setMarca(tfMarca.getText());
            unidad.setModelo(tfModelo.getText());
            unidad.setAnio(Integer.parseInt(tfAnio.getText()));
            unidad.setVin(tfVin.getText());
            TipoUnidad tipoSeleccionado = cbTipo.getSelectionModel().getSelectedItem();
            unidad.setIdTipoUnidad(tipoSeleccionado.getIdTipoUnidad());
            if(unidadEdicion == null) {
                registrarUnidad(unidad);
            } else {
                editarUnidad(unidad);
            }
            
        }
    }
    
    public void inicializarDatos(Unidad unidadEdicion, INotificador observador) {
        this.unidadEdicion = unidadEdicion;
        this.observador = observador;
        if (unidadEdicion != null) {
            tfMarca.setText(unidadEdicion.getMarca());
            tfModelo.setText(unidadEdicion.getModelo());
            tfAnio.setText(String.valueOf(unidadEdicion.getAnio()));
            tfVin.setText(unidadEdicion.getVin());
            int posicionTipoUnidad = obtenerPosicionTipo(unidadEdicion.getIdTipoUnidad());
            cbTipo.getSelectionModel().select(posicionTipoUnidad);
            tfVin.setEditable(false);
            tfVin.setDisable(true);
        }
    
    }
    
    private int obtenerPosicionTipo(int idTipoUnidad) {
        for (int i = 0; i < tipoUnidad.size(); i++) {
            if (tipoUnidad.get(i).getIdTipoUnidad() == idTipoUnidad)
                return i;
        }
        return -1;
    }
    
    private void cargarTipoUnidad() {
        HashMap<String, Object> respuesta = CatalogoImp.obtenerTipoUnidades();
        if (!(boolean) respuesta.get(Constantes.KEY_ERROR)) {
            List<TipoUnidad> unidadesAPI = (List<TipoUnidad>)respuesta.get(Constantes.KEY_LISTA);
            tipoUnidad = FXCollections.observableArrayList();
            tipoUnidad.addAll(unidadesAPI);
            cbTipo.setItems(tipoUnidad);
        } else {
            Utilidades.mostrarAlertaSimple("ERROR", respuesta.get(Constantes.KEY_MENSAJE).toString(), Alert.AlertType.NONE);
        }
    }
    
    private void cerrarVentana() {
        ((Stage) tfModelo.getScene().getWindow()).close();
    }
    
    private void registrarUnidad(Unidad unidad) {
        Respuesta respuesta = UnidadImp.registrar(unidad);
        if(!respuesta.isError()) {
            Utilidades.mostrarAlertaSimple("Unidad registrada", respuesta.getMensaje(), Alert.AlertType.INFORMATION);
            observador.notificarOperacionExitosa("registro", unidad.getMarca() + " " + unidad.getModelo());
            cerrarVentana();
        } else {
            Utilidades.mostrarAlertaSimple("Error al registrar", respuesta.getMensaje(), Alert.AlertType.ERROR);
        }
    }
    
    private void editarUnidad(Unidad unidad) {
        unidad.setIdUnidad(unidadEdicion.getIdUnidad());
        Respuesta respuesta = UnidadImp.editar(unidad);
        if (!respuesta.isError()) {
            Utilidades.mostrarAlertaSimple("Unidad editada", respuesta.getMensaje(), Alert.AlertType.INFORMATION);
            observador.notificarOperacionExitosa("editar", unidad.getMarca() + " " + unidad.getModelo());
            cerrarVentana();
        } else {
            Utilidades.mostrarAlertaSimple("Error al editar", respuesta.getMensaje(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void clicCancelar(ActionEvent event) {
        cerrarVentana();
    }
    
    
}
