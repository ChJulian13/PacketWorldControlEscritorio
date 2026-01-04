
package controlpacketworld;

import dominio.ClienteImp;
import dominio.ColaboradorImp;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
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
    private Envio envio;
    private Direccion direccion;
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
    @FXML
    private Button btContinuar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        envio = new Envio();
        direccion = new Direccion();
        cbBuscarClientePor.getItems().addAll("Nombre", "Teléfono", "Correo");
        // Seleccionar un valor por defecto
        cbBuscarClientePor.getSelectionModel().selectFirst();
        
        configurarComboBoxColoniaSucursalConductor();
        cargarInformacionConductores();
        inicializarTextFieldConTextFormatter();
    }
    
    public void cargarInformacion(Integer idSucursal){
        this.idSucursal = idSucursal;
        this.esModoEdicion = false;
        cargarInformaciónSucursales();
    }
    public void cargarInformacion(Envio envio){
        this.esModoEdicion = true;
        btContinuar.setText("Guardar");
        this.idSucursal = envio.getIdSucursalOrigen();
        obtenerInfoClienteId(envio.getIdCliente());
        cargarInformacionCliente();
        tfDestinatarioNombre.setText(envio.getDestinatarioNombre());
        tfDestinatarioApellidoPaterno.setText(envio.getDestinatarioApellidoPaterno());
        tfDestinatarioApellidoMaterno.setText(envio.getDestinatarioApellidoMaterno());
        cargarDireccionEnvio(envio.getDestinatarioIdDireccion());
        cargarInformaciónSucursales();
        seleccionarConductorEnvio(envio.getIdConductor());
        this.envio = envio;
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
            
            // Si no se encuentra la sucursal actual (caso extraño)
            if(sucursalActual == null){
                Utilidades.mostrarAlertaSimple("Advertencia", 
                    "La sucursal asociada no fue encontrada. Se mostrarán todas las sucursales disponibles.", 
                    Alert.AlertType.WARNING);

                // Cargar todas las sucursales y seleccionar la primera
                sucursales.setAll(sucursalesAPI);
                cbSucursal.setItems(sucursales);
                cbSucursal.setValue(sucursalesAPI.get(0)); // Selecciona la primera por defecto
                return;
            }
            
            // Si el modo es edición, se cargan todas las sucursales, caso contrario solo la sucursal actual
            if (esModoEdicion) {
                sucursales.setAll(sucursalesAPI);
            } else {
                sucursales.add(sucursalActual);
            }
            cbSucursal.setItems(sucursales);
            cbSucursal.setValue(sucursalActual);
        }
    }
    public Sucursal buscarSucursalPorId(Integer id, List<Sucursal> sucursales) {
        for (Sucursal s : sucursales) {
            if (s.getIdSucursal() != null && s.getIdSucursal().equals(id)) {
                return s;
            }
        }
        return null;
    }
    
    public void cargarInformacionConductores(){
        HashMap<String, Object> respuesta = ColaboradorImp.obtenerPorRol("Conductor");
        boolean esError = (boolean) respuesta.get(Constantes.KEY_ERROR);
        if( !esError ){
            List<Colaborador> conductoresAPI = (List<Colaborador>) respuesta.get("colaboradores");
            if( conductoresAPI.isEmpty() ) {
                Utilidades.mostrarAlertaSimple("Conductores", "No hay conductores disponibles.", Alert.AlertType.WARNING);
                return;
            }
            conductores = FXCollections.observableArrayList();
            conductores.addAll(conductoresAPI);
            cbConductor.setItems(conductores);
        }
    }
    
    public void inicializarTextFieldConTextFormatter(){
        tfNumero.setTextFormatter(crearFormatterConLimite(20));
        tfCalle.setTextFormatter(crearFormatterConLimite(100));
        tfDestinatarioNombre.setTextFormatter(crearFormatterConLimite(100));
        tfDestinatarioApellidoPaterno.setTextFormatter(crearFormatterConLimite(100));
        tfDestinatarioApellidoMaterno.setTextFormatter(crearFormatterConLimite(100));
        tfCodigoPostal.setTextFormatter(crearFormatterNumerico(5));
    }
    private TextFormatter<String> crearFormatterConLimite(int longitudMaxima) {
        return new TextFormatter<>(cambio -> {
            if (cambio.getControlNewText().length() <= longitudMaxima) {
                return cambio;
            }
            return null;
        });
    }
    private TextFormatter<String> crearFormatterNumerico(int longitudMaxima) {
        return new TextFormatter<>(cambio -> {
            String nuevoTexto = cambio.getControlNewText();
            if (nuevoTexto.matches("\\d*") && nuevoTexto.length() <= longitudMaxima) {
                return cambio;
            }
            return null;
        });
    }

    public void cargarDireccionEnvio(Integer idDireccion) {
        Direccion direccionAPI = obtenerDireccionPorId(idDireccion);

        if (direccionAPI != null) {
            tfCodigoPostal.setText(direccionAPI.getCodigoPostal().toString());
            cargarInformacionCodigoPostal(direccionAPI.getCodigoPostal().toString());
            cbColonia.setValue(direccionAPI);
            tfCalle.setText(direccionAPI.getCalle());
            tfNumero.setText(direccionAPI.getNumero());
        }
    }
    private Direccion obtenerDireccionPorId(Integer idDireccion) {
        HashMap<String, Object> respuesta = DireccionImp.obtenerDireccionPorId(idDireccion);
        boolean esError = (boolean) respuesta.get(Constantes.KEY_ERROR);
        if ( !esError ) {
            return (Direccion) respuesta.get(Constantes.KEY_OBJETO);
        }
        return null;
    }
    
    public void seleccionarConductorEnvio(Integer idConductor){
        HashMap<String, Object> respuesta = EnvioImp.obtenerColaboradorPorId(idConductor);
        boolean esError = (boolean) respuesta.get(Constantes.KEY_ERROR);
        if( !esError ){
            Colaborador colaboradorAPI = (Colaborador) respuesta.get(Constantes.KEY_OBJETO);
            if( colaboradorAPI != null ) {
                cbConductor.setValue(colaboradorAPI);
            }
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

    @FXML
    private void clicBuscarCliente(ActionEvent event) {
        HashMap<String, Object> respuesta = null;
        boolean esError;
        String buscarPor = cbBuscarClientePor.getSelectionModel().getSelectedItem();
        
        if ( !esBusquedaClienteValida(buscarPor) ) return;
        
        try {
            respuesta = ClienteImp.buscarCliente(tfBuscarCliente.getText(), buscarPor);
        } catch (UnsupportedEncodingException e) {
            Utilidades.mostrarAlertaSimple("Busqueda cliente", "Introduzca información de busqueda valida", Alert.AlertType.WARNING);
        }

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
    public void obtenerInfoClienteId(int idCliente){
        HashMap<String, Object> respuesta = EnvioImp.obtenerClientePorId(idCliente);
        boolean esError = (boolean) respuesta.get(Constantes.KEY_ERROR);
        if( !esError ){
            Cliente clienteAPI = (Cliente) respuesta.get(Constantes.KEY_OBJETO);
            if( clienteAPI != null) {
                this.cliente = clienteAPI;
            }
        }
    }

    @FXML
    private void clicContinuar(ActionEvent event) {
        if( esInformacionEnvioValida() ){
            // Objeto Direccion
            direccion.setCalle(tfCalle.getText());
            direccion.setNumero(tfNumero.getText());
            direccion.setIdColonia(cbColonia.getSelectionModel().getSelectedItem().getIdColonia());
            System.out.println("Dirección Envío:\n" + "Calle: " + direccion.getCalle() + "\n" + "Número: " + direccion.getNumero() + "\n" + "IdColonia: " + direccion.getIdColonia() );
            // Objeto envio
            envio.setIdCliente(this.cliente.getIdCliente());
            envio.setIdSucursalOrigen(cbSucursal.getSelectionModel().getSelectedItem().getIdSucursal());
            envio.setIdConductor(cbConductor.getSelectionModel().getSelectedItem().getIdColaborador());
            envio.setDestinatarioNombre(tfDestinatarioNombre.getText());
            envio.setDestinatarioApellidoPaterno(tfDestinatarioApellidoPaterno.getText());
            envio.setDestinatarioApellidoMaterno(tfDestinatarioApellidoMaterno.getText());
            
            if ( !esModoEdicion ){
                // Mandar a loader de paquetes
                envio.setIdEstatusEnvio(1);
                Integer cpSucursal = obtenerCodipoPostalIdDireccion(cbSucursal.getSelectionModel().getSelectedItem().getIdDireccion());
                Utilidades.mostrarAlertaSimple("Exito", "Exito", Alert.AlertType.INFORMATION);
                irPantallaPaquetes(envio, direccion, cpSucursal, Integer.valueOf(tfCodigoPostal.getText()));
            } else{
                //TODO: enviar a modificar envío
            }
            
        }
    }
    public Integer obtenerCodipoPostalIdDireccion(Integer idDireccion){
        Integer codigoPostal = null;
        Direccion direccionAPI = obtenerDireccionPorId(idDireccion);
        if ( direccionAPI != null ){
            return direccionAPI.getCodigoPostal();
        }
        return codigoPostal;
    }
    
    public boolean esInformacionEnvioValida(){
        // ComboBox y Cliente
        if ( this.cliente == null) {
            Utilidades.mostrarAlertaSimple("Cliente", "Debe de seleccionar un cliente para poder realizar el envío.", Alert.AlertType.INFORMATION);
            return false;
        }
        if ( !esModoEdicion && Validaciones.esVacio(tfCodigoPostal.getText())){
            Utilidades.mostrarAlertaSimple("Código postal", "Debe de introducir el código postal.", Alert.AlertType.INFORMATION);
            return false;
        }
        if ( cbColonia.getSelectionModel().getSelectedItem() == null ){
            Utilidades.mostrarAlertaSimple("Colonia", "Tras introducir el código postal, debe de seleccionar una colonia.", Alert.AlertType.INFORMATION);
            return false;
        }
        if ( cbSucursal.getSelectionModel().getSelectedItem() == null ){
            Utilidades.mostrarAlertaSimple("Sucursal", "Debe de seleccionar la sucursal.", Alert.AlertType.INFORMATION);
            return false;
        }
        if ( cbConductor.getSelectionModel().getSelectedItem() == null ){
            Utilidades.mostrarAlertaSimple("Condictor", "Debe de seleccionar un conductor.", Alert.AlertType.INFORMATION);
            return false;
        }
        // Vacios
        if ( Validaciones.esVacio(tfCalle.getText()) || Validaciones.esVacio(tfNumero.getText()) ){
            Utilidades.mostrarAlertaSimple("Dirección", "Complete la información de la dirección de envío", Alert.AlertType.INFORMATION);
            return false;
        }
        if ( Validaciones.esVacio(tfDestinatarioNombre.getText()) || Validaciones.esVacio(tfDestinatarioApellidoPaterno.getText()) || Validaciones.esVacio(tfDestinatarioApellidoMaterno.getText()) ){
            Utilidades.mostrarAlertaSimple("Dirección", "Introduzca el nombre completo del destinatario.", Alert.AlertType.INFORMATION);
            return false;
        }
        return true;
    }
    
    @FXML
    private void clicCancelar(ActionEvent event) {
        Stage escenario = (Stage) cbBuscarClientePor.getScene().getWindow();
        escenario.close();
    }
    
    private void irPantallaPaquetes(Envio envio, Direccion direccion, Integer cpSucursal, Integer cpEnvio){
        try {
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("FXMLPaquetes.fxml"));
            Parent vista = cargador.load();
            FXMLPaquetesController controlador = cargador.getController();
            controlador.cargarInformacion(envio, direccion, cpSucursal, cpEnvio);
            
            Scene scPaquetes = new Scene(vista);
            Stage stPaquetes = (Stage) tfCodigoPostal.getScene().getWindow();
            stPaquetes.setScene(scPaquetes);
            stPaquetes.setTitle("Paquetes");
            stPaquetes.show();
        } catch (Exception e) {
            e.printStackTrace();
            Utilidades.mostrarAlertaSimple("Paquetes", "Ocurrió un error al cargar el modulo de paquetes, por favor intento más tarde.", Alert.AlertType.ERROR);
        }

    }
    
}
