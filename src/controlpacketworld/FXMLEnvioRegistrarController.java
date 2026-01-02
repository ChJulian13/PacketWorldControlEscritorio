
package controlpacketworld;

import dominio.ClienteImp;
import dominio.DireccionImp;
import dominio.EnvioImp;
import java.io.UnsupportedEncodingException;
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
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import pojo.Cliente;
import pojo.Colaborador;
import pojo.Direccion;
import pojo.Envio;
import pojo.Sucursal;
import utilidad.Constantes;
import utilidad.Utilidades;
import utilidad.Validaciones;


public class FXMLEnvioRegistrarController implements Initializable {
    
    private Cliente cliente;
    private boolean esModoEdicion;
    private Integer idSucursal;
    private ObservableList<Colaborador> conductores;
    private ObservableList<Sucursal> sucursales;
    private ObservableList<Direccion> direcciones;
    @FXML
    private ComboBox<String> cbBuscarClientePor;
    @FXML
    private ComboBox<Colaborador> cbConductor;
    @FXML
    private ComboBox<Sucursal> cbSucursal;
    @FXML
    private TextField tfCodigoPostal;
    @FXML
    private ComboBox<Direccion> cbColonia;
    @FXML
    private TextField tfEstado;
    @FXML
    private TextField tfMunicipio;
    @FXML
    private TextField tfCiudad;
    @FXML
    private TextField tfDestinatarioApellidoPaterno;
    @FXML
    private TextField tfDestinatarioNombre;
    @FXML
    private TextField tfDestinatarioApellidoMaterno;
    @FXML
    private TextField tfCalle;
    @FXML
    private TextField tfNumero;
    @FXML
    private TextField tfBuscarCliente;
    @FXML
    private Label lbClienteNombre;
    @FXML
    private Label lbClienteCorreo;
    @FXML
    private Label lbClienteTelefono;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbBuscarClientePor.getItems().addAll("Nombre", "Teléfono", "Correo");
        // Seleccionar un valor por defecto
        cbBuscarClientePor.getSelectionModel().selectFirst();
        
        configurarComboBoxColoniaSucursalConductor();
    }
    
    public void cargarInformacion(Integer idSucursal){
        this.idSucursal = idSucursal;
        this.esModoEdicion = false;
        cargarInformaciónSucursales();
    }
    public void cargarInformacion(Envio envio){
        // TODO: cargar campos;
        this.esModoEdicion = true;
        cargarInformaciónSucursales();
    }
    
    public void configurarComboBoxColoniaSucursalConductor(){
        // ComboBox no tiene el mismo mecanisco que TableView con PropertyValueFactory
        // Se trabajan con StringConverter o cellFactory
        cbColonia.setConverter(new StringConverter<Direccion>() {
            @Override
            public String toString(Direccion direccion) {
                return direccion == null ? "" : direccion.getColonia();
            }
            @Override
            public Direccion fromString(String string) {
                return null;
            }
        });
        cbSucursal.setConverter(new StringConverter<Sucursal>() {
            @Override
            public String toString(Sucursal sucursal) {
                return sucursal == null ? "" : sucursal.getNombre();
            }
            @Override
            public Sucursal fromString(String string) {
                return null;
            }
        });
        cbConductor.setConverter(new StringConverter<Colaborador>(){
            @Override
            public String toString(Colaborador colaborador) {
                return colaborador == null ? "" : colaborador.getNombre()+" "+colaborador.getApellidoPaterno()+" "+colaborador.getApellidoMaterno();
            }
            @Override
            public Colaborador fromString(String string) {
                return null;
            }
        });
    }
    
    public void cargarInformaciónSucursales(){
        HashMap<String, Object> respuesta = EnvioImp.obtenerSucursales();
        boolean esError = (boolean) respuesta.get(Constantes.KEY_ERROR);
        if( !esError ){
            List<Sucursal> sucursalesAPI = (List<Sucursal>) respuesta.get(Constantes.KEY_LISTA);
            if( sucursalesAPI.isEmpty() ){
                Utilidades.mostrarAlertaSimple("Sucursales", "No se encontraron sucursales activas.", Alert.AlertType.INFORMATION);
                return;
            }
            sucursales = FXCollections.observableArrayList();
            Sucursal sucursalActual = buscarSucursalPorId(this.idSucursal, sucursalesAPI);
            // Si el modo es edición, se cargan todas las sucursales, caso contrario solo la sucursal actual
            if( esModoEdicion ){
                sucursales.addAll(sucursalesAPI);
                cbSucursal.setItems(sucursales);
            } else {
                sucursales.add(sucursalActual);
                cbSucursal.setItems(sucursales);
            }
            cbSucursal.getSelectionModel().select(sucursalActual);
        }
    }
    public Sucursal buscarSucursalPorId(Integer id, List<Sucursal> sucursales){
        Sucursal sucursal = null;
        for (Sucursal s : sucursales) {
            if (s.getIdSucursal().equals(this.idSucursal)) {
                sucursal = s;
                break;
            }
        }
        return sucursal;
    }
    
    public void cargarInformacionConductores(){
        HashMap<String, Object> respuesta = null;
        boolean esError = (boolean) respuesta.get(Constantes.KEY_ERROR);
        if( !esError ){
            List<Colaborador> conductoresAPI = (List<Colaborador>) respuesta.get(Constantes.KEY_LISTA);
            if( conductoresAPI.isEmpty() ) {
                Utilidades.mostrarAlertaSimple("Conductores", "No hay conductores disponibles.", Alert.AlertType.WARNING);
                return;
            }
            conductores = FXCollections.observableArrayList();
            conductores.addAll(conductoresAPI);
            cbConductor.setItems(conductores);
        }
    }

    @FXML
    private void clicBuscarCodigoPostal(ActionEvent event) {
        cbColonia.getSelectionModel().clearSelection();
        cbColonia.getItems().clear();
        tfEstado.clear();
        tfMunicipio.clear();
        tfCiudad.clear();
        cargarInformacionCodigoPostal(tfCodigoPostal.getText());
    }
    
    public void cargarInformacionCodigoPostal(String codigoPostal){
        if( Validaciones.esVacio(codigoPostal) ){
            Utilidades.mostrarAlertaSimple("Codigo postal", "Debe de introducir un código postal.", Alert.AlertType.ERROR);
            return;
        }
        if( !Validaciones.esNumericoConLongitud(codigoPostal, 5) ){
            Utilidades.mostrarAlertaSimple("Codigo postal", "Introduzca un código postal valido.", Alert.AlertType.WARNING);
            return;
        }
        
        HashMap<String, Object> respuesta = DireccionImp.obtenerDireccion(codigoPostal);
        boolean esError = (boolean) respuesta.get(Constantes.KEY_ERROR);
        if( !esError ){
            List<Direccion> direccionesAPI = (List<Direccion>) respuesta.get(Constantes.KEY_LISTA);
            
            if( direccionesAPI.isEmpty() ){
                Utilidades.mostrarAlertaSimple("Sin coincidencias", "No se encontraron resultados para el código postal introducido.", Alert.AlertType.INFORMATION);
                return;
            }
            
            direcciones = FXCollections.observableArrayList();
            direcciones.addAll(direccionesAPI);
            cbColonia.setItems(direcciones);
            
            // Estado, municipio y ciudad se repite, solo varia la colonia
            Direccion direccion = direcciones.get(0);
            tfEstado.setText(direccion.getEstado());
            tfMunicipio.setText(direccion.getMunicipio());
            tfCiudad.setText(direccion.getCiudad());
        }
    }
    
    public boolean esInformacionEnvioValida(){
        boolean estado = true;
        // combobox
        // vacios
        if ( tfCalle.getText().isEmpty() || tfCalle.getText().trim().isEmpty() ){
            
        }
        // longitudes
        return estado;
    }

    @FXML
    private void clicBuscarCliente(ActionEvent event) throws UnsupportedEncodingException {
        HashMap<String, Object> respuesta = null;
        boolean esError;
        String buscarPor = cbBuscarClientePor.getSelectionModel().getSelectedItem();
        
        if ( !esBusquedaClienteValida(buscarPor) ) return;
        
        respuesta = ClienteImp.buscarCliente(tfBuscarCliente.getText(), buscarPor);
        
        esError = (boolean) respuesta.get(Constantes.KEY_ERROR);
        if( !esError ){
            List<Cliente> clienteAPI = (List<Cliente>) respuesta.get(Constantes.KEY_LISTA);
            if( !clienteAPI.isEmpty() ){
                this.cliente = clienteAPI.get(0);
                cargarInformacionCliente();
            } else {
                Utilidades.mostrarAlertaSimple("Cliente", "No se encontró información del cliente, verifique la información.", Alert.AlertType.INFORMATION);
            }
        } else {
            Utilidades.mostrarAlertaSimple("Error al cargar", respuesta.get(Constantes.KEY_MENSAJE).toString(), Alert.AlertType.ERROR);
        }
    }
    public boolean esBusquedaClienteValida(String buscarPor){
        String cadenaBusqueda = tfBuscarCliente.getText();
        if (Validaciones.esVacio(cadenaBusqueda)) {
            Utilidades.mostrarAlertaSimple("Buscar cliente", "Introduzca información del cliente para poder realizar la busqueda.", Alert.AlertType.INFORMATION);
            return false;
        }
        if (buscarPor.contains("Nombre") && !Validaciones.esSoloTexto(cadenaBusqueda)) {
            Utilidades.mostrarAlertaSimple("Buscar cliente", "Introduzca un numbre valido.", Alert.AlertType.INFORMATION);
            return false;
        }
        if (buscarPor.equals("Correo") && !Validaciones.esCorreoValido(cadenaBusqueda)) {
            Utilidades.mostrarAlertaSimple("Buscar cliente", "Introduzca un correo electrónico valido.", Alert.AlertType.INFORMATION);
            return false;
        }
        if (buscarPor.equals("Teléfono") && !Validaciones.esNumericoConLongitud(cadenaBusqueda, 10)) {
            Utilidades.mostrarAlertaSimple("Buscar cliente", "Introduzca número de teléfono valido.", Alert.AlertType.INFORMATION);
            return false;
        }
        return true;
    }
    public void cargarInformacionCliente(){
        lbClienteNombre.setText( cliente.getNombre()+" "+cliente.getApellidoPaterno()+" "+cliente.getApellidoMaterno() );
        lbClienteCorreo.setText( cliente.getCorreo() );
        lbClienteTelefono.setText( cliente.getTelefono() );
    }

    @FXML
    private void clicContinuar(ActionEvent event) {
    }

    @FXML
    private void clicCancelar(ActionEvent event) {
    }
}
