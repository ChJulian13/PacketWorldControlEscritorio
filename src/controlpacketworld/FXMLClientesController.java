/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controlpacketworld;

import dominio.ClienteImp;
import java.io.IOException;
import java.net.URL;
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
import pojo.Cliente;
import utilidad.Utilidades;

/**
 * FXML Controller class
 *
 * @author julia
 */
public class FXMLClientesController implements Initializable {

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
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellidoPaterno.setCellValueFactory(new PropertyValueFactory<>("apellidoPaterno"));
        colApellidoMaterno.setCellValueFactory(new PropertyValueFactory<>("apellidoMaterno"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
    }

    private void cargarDatosTabla() {
        listaClientes = FXCollections.observableArrayList();
        List<Cliente> clientesWS = ClienteImp.obtenerClientes(); 
        if (clientesWS != null) {
            listaClientes.addAll(clientesWS);
            tvClientes.setItems(listaClientes);
        } else {
            Utilidades.mostrarAlertaSimple("Error de conexión", "No se pudo cargar la lista de clientes.", Alert.AlertType.ERROR);
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
            Utilidades.mostrarAlertaSimple("Selección requerida", "Debes seleccionar un cliente de la tabla para editarlo.", Alert.AlertType.WARNING);
        }
    }

    private void irFormulario(boolean esEdicion, Cliente cliente) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLClienteRegistrar.fxml"));
            Parent root = loader.load();
            
            FXMLClienteRegistrarController controlador = loader.getController();
            // Ahora 'this' es válido porque la clase implementa INotificador
            controlador.inicializarValores(this, cliente); 
            
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(esEdicion ? "Editar Cliente" : "Registrar Cliente");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();
            Utilidades.mostrarAlertaSimple("Error", "No se pudo cargar la ventana del formulario.", Alert.AlertType.ERROR);
        }
    }

    @Override
    public void notificarOperacion(String mensaje, boolean exito) {
        cargarDatosTabla();
        if(exito){
             Utilidades.mostrarAlertaSimple("Operación exitosa", mensaje, Alert.AlertType.INFORMATION);
        }
    }
}