/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controlpacketworld;

import controlpacketworld.interfaz.INotificador;
import dominio.CatalogoImp;
import dominio.ColaboradorImp;
import dto.Respuesta;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
import pojo.Colaborador;
import pojo.Rol;
import utilidad.Constantes;
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
    @FXML
    private TextField tfBarraBusqueda;
    @FXML
    private ComboBox<String> cbBuscar;
    @FXML
    private Button btnBuscar;
    @FXML
    private Button btnMostrarTodos;
    @FXML
    private ComboBox<Rol> cbBusquedaRol;
    private ObservableList<Rol> listaRolesBusqueda;
    @FXML
    private Button btnMostrarTodos1;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarInformacionColaboradores();
        cargarRolesParaBusqueda();
        ObservableList<String> criterios = FXCollections.observableArrayList("Nombre", "Rol", "No. Personal");
        cbBuscar.setItems(criterios);
        cbBuscar.getSelectionModel().selectFirst();
        btnMostrarTodos.setVisible(false);
        cbBuscar.valueProperty().addListener((obs, oldVal, newVal) -> {
            configurarVisibilidadBusqueda(newVal);
        });
        cbBusquedaRol.setVisible(false);
    }
    
    private void configurarVisibilidadBusqueda(String criterio) {
        if ("Rol".equals(criterio)) {
            tfBarraBusqueda.setVisible(false);
            tfBarraBusqueda.setManaged(false); 
            
            cbBusquedaRol.setVisible(true);
            cbBusquedaRol.setManaged(true);
        } else {
            tfBarraBusqueda.setVisible(true);
            tfBarraBusqueda.setManaged(true);
            
            cbBusquedaRol.setVisible(false);
            cbBusquedaRol.setManaged(false);
            
            tfBarraBusqueda.setText("");
        }
    }
    
    private void cargarRolesParaBusqueda() {
        HashMap<String, Object> respuesta = CatalogoImp.obtenerRolesSistema();
        if (!(boolean) respuesta.get(Constantes.KEY_ERROR)) {
            List<Rol> roles = (List<Rol>) respuesta.get(Constantes.KEY_LISTA);
            listaRolesBusqueda = FXCollections.observableArrayList(roles);
            cbBusquedaRol.setItems(listaRolesBusqueda);
        } else {
            Utilidades.mostrarAlertaSimple("ERROR", respuesta.get(Constantes.KEY_MENSAJE).toString(), Alert.AlertType.NONE);
        }
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
        irFormulario(null);
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
        String criterio = cbBuscar.getValue();
        String textoBusqueda = "";

        if ("Rol".equals(criterio)) {
            Rol rolSeleccionado = cbBusquedaRol.getValue();
            if (rolSeleccionado == null) {
                Utilidades.mostrarAlertaSimple("Selección requerida", "Selecciona un rol de la lista.", Alert.AlertType.WARNING);
                return;
            }
            textoBusqueda = rolSeleccionado.getRol();
        } else {
            textoBusqueda = tfBarraBusqueda.getText();
            if (textoBusqueda == null || textoBusqueda.trim().isEmpty()) {
                clicMostrarTodos(null); 
                return;
            }
        }
        
        HashMap<String, Object> respuesta = null;
        switch (criterio) {
            case "Nombre":
                respuesta = ColaboradorImp.obtenerPorNombre(textoBusqueda);
                break;
            case "Rol":
                respuesta = ColaboradorImp.obtenerPorRol(textoBusqueda);
                break;
            case "No. Personal":
                respuesta = ColaboradorImp.obtenerPorNoPersonal(textoBusqueda);
                break;
        }

        procesarRespuestaBusqueda(respuesta);
        btnMostrarTodos.setVisible(true);
    }

    private void procesarRespuestaBusqueda(HashMap<String, Object> respuesta) {
        boolean esError = (boolean) respuesta.get("error");
        
        if (!esError) {
            List<Colaborador> lista = (List<Colaborador>) respuesta.get("colaboradores");
            colaboradores.clear();
            if (lista != null && !lista.isEmpty()) {
                colaboradores.addAll(lista);
            } else {
                Utilidades.mostrarAlertaSimple("Sin coincidencias", "No se encontraron colaboradores.", Alert.AlertType.INFORMATION);
            }
        } else {
            String mensaje = (String) respuesta.get("mensaje");
            Utilidades.mostrarAlertaSimple("Error", mensaje, Alert.AlertType.ERROR);
        }
    }

    @Override
    public void notificarOperacionExitosa(String operacion, String nombre) {
        System.out.print("Operación: " + operacion + "nombre colaborador: " + nombre);
        cargarInformacionColaboradores();
    }

    @FXML
    private void clicMostrarTodos(ActionEvent event) {
        cbBuscar.getSelectionModel().selectFirst();
        if (cbBusquedaRol != null) {
            cbBusquedaRol.getSelectionModel().clearSelection();
            cbBusquedaRol.setButtonCell(null); 
        }
        tfBarraBusqueda.setText(""); 
        cargarInformacionColaboradores();
        btnMostrarTodos.setVisible(false);
    }
    
    private void irSeleccionarFoto(Colaborador colaborador) {
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("FXMLColaboradorFoto.fxml"));
        try {
            Parent vista = cargador.load();
            FXMLColaboradorFotoController controlador = cargador.getController();
            controlador.inicializarValores(colaborador.getIdColaborador());
            Scene escena = new Scene(vista);
            Stage escenario = new Stage();
            escenario.setScene(escena);
            escenario.setTitle("Selección de foto colaborador");
            escenario.initModality(Modality.APPLICATION_MODAL);
            escenario.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void clicFoto(ActionEvent event) {
        Colaborador colaborador = tvColaboradores.getSelectionModel().getSelectedItem();
        if (colaborador != null) {
            irSeleccionarFoto(colaborador);
        } else {
            Utilidades.mostrarAlertaSimple("Selecciona colaborador", "Para seleccionar la foto de un colaborador, debes seleccionarlo.", Alert.AlertType.WARNING);
        }
    }
    
}
