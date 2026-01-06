/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controlpacketworld;

import controlpacketworld.interfaz.INotificador;
import dominio.ClienteImp;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pojo.Cliente;
import utilidad.Constantes;
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
    @FXML
    private TableColumn<Cliente, String> colCalle;
    @FXML
    private TableColumn<Cliente, String> colNumero;
    @FXML
    private TableColumn<Cliente, String> colColonia;
    @FXML
    private TableColumn<Cliente, String> colCP;
    
    private ObservableList<Cliente> listaClientes;
    
    @FXML
    private TextField tfBuscar;
    @FXML
    private ComboBox<String> cbBuscar; 
    @FXML
    private Button btnMostrarTodos;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        configurarBusqueda(); 
        cargarDatosTabla();
    }

    private void configurarTabla() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellidoPaterno.setCellValueFactory(new PropertyValueFactory<>("apellidoPaterno"));
        colApellidoMaterno.setCellValueFactory(new PropertyValueFactory<>("apellidoMaterno"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        colCalle.setCellValueFactory(new PropertyValueFactory<>("calle"));
        colNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));
        colColonia.setCellValueFactory(new PropertyValueFactory<>("nombreColonia"));
        colCP.setCellValueFactory(new PropertyValueFactory<>("codigoPostal"));
    }
    
    private void configurarBusqueda() {
        ObservableList<String> criterios = FXCollections.observableArrayList("Nombre", "Correo", "Teléfono");
        cbBuscar.setItems(criterios);
        
        if (btnMostrarTodos != null) {
            btnMostrarTodos.setVisible(false);
        }
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
    
    @FXML
    private void clicEliminar(ActionEvent event) {
        Cliente clienteSeleccionado = tvClientes.getSelectionModel().getSelectedItem();
        if (clienteSeleccionado != null) {
            boolean confirmar = Utilidades.mostrarAlertaConfirmacion("Eliminar Cliente", 
                    "¿Estás seguro de que deseas eliminar al cliente " + clienteSeleccionado.getNombre() + "?");
            
            if (confirmar) {
                HashMap<String, Object> respuesta = ClienteImp.eliminarCliente(clienteSeleccionado.getIdCliente());
                if (!(boolean) respuesta.get(Constantes.KEY_ERROR)) {
                    Utilidades.mostrarAlertaSimple("Cliente eliminado", 
                            (String) respuesta.get(Constantes.KEY_MENSAJE), 
                            Alert.AlertType.INFORMATION);
                    cargarDatosTabla(); // Recargamos la tabla para ver que se eliminó
                } else {
                    Utilidades.mostrarAlertaSimple("Error al eliminar", 
                            (String) respuesta.get(Constantes.KEY_MENSAJE), 
                            Alert.AlertType.ERROR);
                }
            }
        } else {
            Utilidades.mostrarAlertaSimple("Selección requerida", 
                    "Para eliminar un cliente debes seleccionarlo previamente de la tabla.", 
                    Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void clicBuscar(ActionEvent event) {
        String criterio = cbBuscar.getSelectionModel().getSelectedItem();
        String busqueda = tfBuscar.getText();
        
        if (criterio != null && !busqueda.isEmpty()) {
            HashMap<String, Object> respuesta = ClienteImp.buscarCliente2(busqueda, criterio);
            if (!(boolean) respuesta.get(Constantes.KEY_ERROR)) {
                List<Cliente> resultados = (List<Cliente>) respuesta.get(Constantes.KEY_LISTA);
                listaClientes.clear();
                listaClientes.addAll(resultados);
                tvClientes.setItems(listaClientes);
                
                if (btnMostrarTodos != null) {
                    btnMostrarTodos.setVisible(true); 
                }
            } else {
                listaClientes.clear(); 
                Utilidades.mostrarAlertaSimple("Sin resultados", 
                        (String) respuesta.get(Constantes.KEY_MENSAJE), 
                        Alert.AlertType.INFORMATION);
            }
        } else {
            Utilidades.mostrarAlertaSimple("Campos requeridos", 
                    "Selecciona un criterio de búsqueda e ingresa un valor.", 
                    Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void clicMostrarTodos(ActionEvent event) {
        tfBuscar.clear();
        cbBuscar.getSelectionModel().clearSelection();
        cargarDatosTabla();
        
        if (btnMostrarTodos != null) {
            btnMostrarTodos.setVisible(false);
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

    @Override
    public void notificarOperacionExitosa(String operacion, String nombre) {
        tfBuscar.clear();
        if (btnMostrarTodos != null) {
            btnMostrarTodos.setVisible(false);
        }
        cargarDatosTabla();
        Utilidades.mostrarAlertaSimple("Operación exitosa", 
            "El cliente " + nombre + " se ha " + operacion + " correctamente.", 
            Alert.AlertType.INFORMATION);
    }

    @Override
    public void enviarObjeto(Object object) {
        // Implementación opcional
    }
}