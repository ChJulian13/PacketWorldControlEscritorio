/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controlpacketworld;

import controlpacketworld.interfaz.INotificador;
import dominio.ClienteImp;
import java.io.IOException;
import java.net.URL;
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
import pojo.Cliente;
import utilidad.Utilidades;

/**
 * FXML Controller class
 *
 * @author julia
 */
public class FXMLClientesController implements Initializable, INotificador {

    @FXML
    private TableView<Cliente> tvClientes;
    @FXML
    private TableColumn<Cliente, String> colNombre;
    @FXML
    private TableColumn<Cliente, String> colApellidoPaterno;
    @FXML
    private TableColumn<Cliente, String> colApellidoMaterno;
    @FXML
    private TableColumn<Cliente, String> colCorreo;
    @FXML
    private TableColumn<Cliente, String> colTelefono;
    
    private ObservableList<Cliente> listaClientes;
    @FXML
    private TableColumn<Cliente, String> colCalle;
    @FXML
    private TableColumn<Cliente, String> colNumero;
    @FXML
    private TableColumn<Cliente, String> colColonia;
    @FXML
    private TableColumn<Cliente, String> colCP;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        configurarTabla();
        cargarDatosTabla();
    }

    private void configurarTabla() {
        // Datos Personales
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellidoPaterno.setCellValueFactory(new PropertyValueFactory<>("apellidoPaterno"));
        colApellidoMaterno.setCellValueFactory(new PropertyValueFactory<>("apellidoMaterno"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));

        // Datos de Dirección (Directos de Cliente, sin objeto anidado)
        // Esto coincide con tu Mapper SQL que devuelve "calle", "numero", etc. como columnas.
        colCalle.setCellValueFactory(new PropertyValueFactory<>("calle"));
        colNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));
        colColonia.setCellValueFactory(new PropertyValueFactory<>("nombreColonia"));
        colCP.setCellValueFactory(new PropertyValueFactory<>("codigoPostal"));
    }

    private void cargarDatosTabla() {
        listaClientes = FXCollections.observableArrayList();
        List<Cliente> clientesWS = ClienteImp.obtenerClientes(); 
        if (clientesWS != null) {
            listaClientes.addAll(clientesWS);
            tvClientes.setItems(listaClientes);
        } else {
            Utilidades.mostrarAlertaSimple("Error de conexión", 
                "No se pudo cargar la lista de clientes. Por favor intente más tarde.", 
                Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void clicRegistrar(ActionEvent event) {
        irFormulario(false, null);
    }

    @FXML
    private void clicEditar(ActionEvent event) {
        Cliente clienteSeleccionado = tvClientes.getSelectionModel().getSelectedItem();
        if(clienteSeleccionado != null){
            irFormulario(true, clienteSeleccionado);
        } else {
            Utilidades.mostrarAlertaSimple("Selección requerida", 
                "Debes seleccionar un cliente de la tabla para editarlo.", 
                Alert.AlertType.WARNING);
        }
    }

    private void irFormulario(boolean esEdicion, Cliente cliente) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLClienteRegistrar.fxml"));
            Parent root = loader.load();
            
            FXMLClienteRegistrarController controlador = loader.getController();
            controlador.inicializarValores(this, cliente); 
            
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(esEdicion ? "Editar Cliente" : "Registrar Cliente");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();
            Utilidades.mostrarAlertaSimple("Error", 
                "No se pudo cargar la ventana del formulario.", 
                Alert.AlertType.ERROR);
        }
    }
    public void notificarOperacion(String mensaje, boolean exito) {
        if(exito){
             Utilidades.mostrarAlertaSimple("Operación exitosa", mensaje, Alert.AlertType.INFORMATION);
             cargarDatosTabla(); 
        }
    }
    
    // Métodos no usados de la interfaz pero requeridos por contrato
    @Override
    public void notificarOperacionExitosa(String operacion, String nombre) {
        // Implementación opcional si se requiere en el futuro
    }

    @Override
    public void enviarObjeto(Object object) {
        // Implementación opcional
    }

    @FXML
    private void clicEliminar(ActionEvent event) {
    }
}