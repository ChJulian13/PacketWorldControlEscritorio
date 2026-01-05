
package controlpacketworld;

import controlpacketworld.interfaz.INotificador;
import dominio.DireccionImp;
import dominio.EnvioImp;
import dominio.PaqueteImp;
import dto.EnvioCompletoDTO;
import dto.RSDistanciaKM;
import dto.Respuesta;
import dto.RespuestaGenerica;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pojo.Direccion;
import pojo.Envio;
import pojo.Paquete;
import utilidad.Constantes;
import utilidad.Utilidades;


public class FXMLPaquetesController implements Initializable, INotificador{
    
    private ObservableList<Paquete> paquetes;
    private boolean esModoEdicion;
    private Envio envio;
    private Direccion direccion;
    private Integer cpOrigen;
    private Integer cpDestino;
    private double distanciaKM;
    private double costo;
    private INotificador observadorEnvio;
    @FXML
    private TableView<Paquete> tvPaquetes;
    @FXML
    private TableColumn colDescripcion;
    @FXML
    private TableColumn colPeso;
    @FXML
    private TableColumn colAlto;
    @FXML
    private TableColumn colAncho;
    @FXML
    private TableColumn colProfundidad;
    @FXML
    private Label lbCosto;
    @FXML
    private Button btConfirmar;
    @FXML
    private Button btCancelar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        esModoEdicion = false;
        configurarTabla();
        paquetes = FXCollections.observableArrayList();
        tvPaquetes.setItems(paquetes);
    }

    @Override
    public void notificarOperacionExitosa(String operacion, String nombre) {
        paquetes.removeAll();
        cargarPaquetesEnvio();
    }
    @Override
    public void enviarObjeto(Object objetoPaquete) {
        Paquete paqueteRecibido = (Paquete) objetoPaquete;
        if (paquetes.contains(paqueteRecibido)) {
            // Caso edicion
            // El objeto ya existe y fue modificado en la otra ventana porque se pasó por referencia.
            // Solo necesitamos refrescar la tabla visualmente.
            tvPaquetes.refresh();
        } else {
            // El objeto no está en la lista, así que lo agregamos.
            paquetes.add(paqueteRecibido);
            calcularCostoEnvio();
        }
        
    }
    
    // Inicialización con loader
    public void cargarInformacion(INotificador observadorEnvio, Envio envio, Direccion direccion, Integer cpSucursal, Integer cpDestino){
        this.observadorEnvio = observadorEnvio;
        this.envio = envio;
        this.direccion = direccion;
        this.cpOrigen = cpSucursal;
        this.cpDestino = cpDestino;
        obtenerDistanciaKM();
    }
    // Inicialización con loader modo edicion
    public void cargarInformacion(INotificador observadorEnvio, Envio envio){
        btCancelar.setVisible(false);
        btConfirmar.setVisible(false);
        esModoEdicion = true;
        this.observadorEnvio = observadorEnvio;
        this.envio = envio;
        cargarCodigosPostales();
        obtenerDistanciaKM();
        cargarPaquetesEnvio();
        tvPaquetes.refresh();
    }
    
    private void configurarTabla(){
        colDescripcion.setCellValueFactory(new PropertyValueFactory("descripcion"));
        colPeso.setCellValueFactory(new PropertyValueFactory("peso"));
        colAlto.setCellValueFactory(new PropertyValueFactory("alto"));
        colAncho.setCellValueFactory(new PropertyValueFactory("ancho"));
        colProfundidad.setCellValueFactory(new PropertyValueFactory("profundidad"));
    }
    
    private void obtenerDistanciaKM(){
        RSDistanciaKM respuesta = PaqueteImp.obtenerDistanciaKM(this.cpOrigen, this.cpDestino);
        if ( !respuesta.isError() ){
            this.distanciaKM = respuesta.getDistanciaKM();
        } else {
            Utilidades.mostrarAlertaSimple("Error al calcular distancia", respuesta.getMensaje(), Alert.AlertType.ERROR);  
        }
    }
    
    private void calcularCostoEnvio(){
        //###########
        Utilidades.mostrarAlertaSimple("Calculo", "Distancia: "+this.distanciaKM + "Numero paquetes: "+ paquetes.size(), Alert.AlertType.INFORMATION);
        RespuestaGenerica<Double> respuestaCosto = PaqueteImp.CalcularCosto(this.distanciaKM, paquetes.size());
        if ( !respuestaCosto.isError() && respuestaCosto.getValor()!= null){
            this.costo = respuestaCosto.getValor();
            //###########
            Utilidades.mostrarAlertaSimple("Costo actualizado", "Distancia: "+respuestaCosto.getValor().toString(), Alert.AlertType.INFORMATION);
            actualizarCosto();
        } else {
            Utilidades.mostrarAlertaSimple("Error al calcular el costo, intentelo más tarde.", respuestaCosto.getMensaje(), Alert.AlertType.ERROR);  
        }
    }
    
    private void actualizarCosto(){
        lbCosto.setText(String.valueOf(this.costo));
    }

    @FXML
    private void clicAgregar(ActionEvent event) {
        try {
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("FXMLPaqueteRegistrar.fxml"));
            Parent vista = cargador.load();
            FXMLPaqueteRegistrarController controlador = cargador.getController();
            controlador.cargarInformacion(this);
            Scene scRegistrarPaquete = new Scene(vista);
            Stage stRegistrarPaquete = new Stage();
            stRegistrarPaquete.setScene(scRegistrarPaquete);
            stRegistrarPaquete.setTitle("Registrar paquete");
            stRegistrarPaquete.initModality(Modality.APPLICATION_MODAL);
            stRegistrarPaquete.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void clicModificar(ActionEvent event) {
        if( tvPaquetes.getSelectionModel().getSelectedItem() != null ){
            try {
                FXMLLoader cargador = new FXMLLoader(getClass().getResource("FXMLPaqueteRegistrar.fxml"));
                Parent vista = cargador.load();
                FXMLPaqueteRegistrarController controlador = cargador.getController();
                controlador.cargarModoEdicion(this, tvPaquetes.getSelectionModel().getSelectedItem());
                Scene scRegistrarPaquete = new Scene(vista);
                Stage stRegistrarPaquete = new Stage();
                stRegistrarPaquete.setScene(scRegistrarPaquete);
                stRegistrarPaquete.setTitle("Modificar paquete");
                stRegistrarPaquete.initModality(Modality.APPLICATION_MODAL);
                stRegistrarPaquete.showAndWait();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Utilidades.mostrarAlertaSimple("Seleccion", "Debe de seleccionar un paquete.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void clicEliminar(ActionEvent event) {
        if( tvPaquetes.getSelectionModel().getSelectedItem() != null ){
            paquetes.remove(tvPaquetes.getSelectionModel().getSelectedItem());
            calcularCostoEnvio();
        } else {
            Utilidades.mostrarAlertaSimple("Seleccion", "Debe de seleccionar un paquete.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void clicConfirmar(ActionEvent event) {
        if ( paquetes.size() > 0 ){
            if ( !esModoEdicion ){
                crearEnvio();
            }
        } else {
            Utilidades.mostrarAlertaSimple("Sin paquetes", "Debe de añadir por lo menos un paquete.", Alert.AlertType.WARNING);
        }
    }
    private void crearEnvio(){
        envio.setCosto(costo);
        EnvioCompletoDTO envioCompleto = new EnvioCompletoDTO(envio, direccion, paquetes);
        Respuesta respuesta = EnvioImp.crearEnvioCompleto(envioCompleto);
        if ( !respuesta.isError() ){
            Utilidades.mostrarAlertaSimple("Envio registrado", "Envio "+ respuesta.getValor() +" guardado.", Alert.AlertType.INFORMATION);
        } else {
            Utilidades.mostrarAlertaSimple("Error al registrar", respuesta.getMensaje(), Alert.AlertType.INFORMATION);
        }
    }

    @FXML
    private void clicCancelar(ActionEvent event) {
        CerrarVentana();
    }
    
    private void CerrarVentana(){
        Stage escenario = (Stage) tvPaquetes.getScene().getWindow();
        escenario.close();
    }
    
    // #########################################################################
    private void cargarCodigosPostales(){
        Integer origen = obtenerCodigoPostalSucursal(envio.getIdSucursalOrigen());
        Integer destino = obtenerCodipoPostalIdDireccion(envio.getDestinatarioIdDireccion());
        if ( origen!= null && destino != null){
            this.cpOrigen = origen;
            this.cpDestino = destino;
        } else {
            Utilidades.mostrarAlertaSimple("Error", "Error al cargar codigos postales de origen y destino.", Alert.AlertType.ERROR);
            CerrarVentana();
        }
    }
    private Integer obtenerCodigoPostalSucursal(Integer idSucursal){
        Respuesta respuesta = DireccionImp.obtenerCodigoPostalSucursal(idSucursal);
        if( !respuesta.isError() ){
            return Integer.valueOf( respuesta.getValor() );
        }
        return null;
    }
    private Integer obtenerCodipoPostalIdDireccion(Integer idDireccion) {
        HashMap<String, Object> respuesta = DireccionImp.obtenerDireccionPorId(idDireccion);
        if( respuesta != null ){
            boolean esError = (boolean) respuesta.get(Constantes.KEY_ERROR);

            if( !esError ) {
                Direccion direccion = (Direccion) respuesta.get(Constantes.KEY_OBJETO);
                if (direccion != null) {
                    return direccion.getCodigoPostal();
                }
            }
        }
        return null;
    }
    
    
    private void cargarPaquetesEnvio(){
        HashMap<String, Object> respuesta = PaqueteImp.obtenerPaquetesEnvio(this.envio.getIdEnvio());
        boolean esError = (boolean) respuesta.get(Constantes.KEY_ERROR);
        if( !esError ){
            List<Paquete> paquetes = (List<Paquete>) respuesta.get(Constantes.KEY_LISTA);
            if( !paquetes.isEmpty() ){
                this.paquetes.addAll(paquetes);
            } else {
                // Caso raro
                Utilidades.mostrarAlertaSimple("Sin paquetes", "Este pedido no tiene paquetes", Alert.AlertType.INFORMATION);
            }
        } else {
            Utilidades.mostrarAlertaSimple("Error al cargar paquetes", respuesta.get(Constantes.KEY_MENSAJE).toString(), Alert.AlertType.ERROR);
        }
    }
}
