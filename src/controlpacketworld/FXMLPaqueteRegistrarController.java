
package controlpacketworld;

import controlpacketworld.interfaz.INotificador;
import dominio.PaqueteImp;
import dto.Respuesta;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import pojo.Paquete;
import utilidad.Utilidades;
import utilidad.Validaciones;

public class FXMLPaqueteRegistrarController implements Initializable{
    
    private boolean esModoEnvioNuevo;
    //private boolean esModoRegistroEnvioNuevo;
    private boolean esModoRegistroEnvioRegistrado;
    //private boolean esModoEdicionEnvioNuevo;
    private boolean esModoEdicionEnvioRegistrado;
    private Paquete paquete;
    private INotificador observador;
    @FXML
    private TextField tfPeso;
    @FXML
    private TextArea taDescripcion;
    @FXML
    private TextField tfAncho;
    @FXML
    private TextField tfProfundidad;
    @FXML
    private TextField tfAlto;
    @FXML
    private Button btAgregar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.paquete = new Paquete();
        configurarFormaterTextField();
        esModoEnvioNuevo = true;
        esModoEdicionEnvioRegistrado = false;
        esModoRegistroEnvioRegistrado = false;
    }
    
    private void configurarFormaterTextField(){
        tfAlto.setTextFormatter(crearFormatterDecimal());
        tfAncho.setTextFormatter(crearFormatterDecimal());
        tfProfundidad.setTextFormatter(crearFormatterDecimal());
        tfPeso.setTextFormatter(crearFormatterDecimal());
    }
    private TextFormatter<String> crearFormatterDecimal() {
        return new TextFormatter<>(cambio -> {
            String nuevoTexto = cambio.getControlNewText();
            if (nuevoTexto.isEmpty()) {
                return cambio;
            }
            if (!nuevoTexto.matches("\\d+(\\.\\d*)?")) {
                return null;
            }
            // Solo números y un punto decimal
            if (!nuevoTexto.matches("\\d*(\\.\\d*)?")) {
                return null;
            }
            // Separar parte entera y decimal
            String[] partes = nuevoTexto.split("\\.");
            String parteEntera = partes[0];
            String parteDecimal = partes.length > 1 ? partes[1] : "";
            // Máximo 8 dígitos enteros
            if (parteEntera.length() > 8) {
                return null;
            }
            // Máximo 2 decimales
            if (parteDecimal.length() > 2) {
                return null;
            }
            return cambio;
        });
    }
    
    public void cargarModoRegistroEnvioNuevo(INotificador observador){
        this.observador = observador;
    }
    public void cargarModoEdicionEnvioNuevo(INotificador observador, Paquete paquete){
        this.observador = observador;
        this.paquete = paquete;
        cargarInformacionPaquete();
        btAgregar.setText("Guardar");
    }
    
    public void cargarModoRegistroEnvioRegistrado(INotificador observador, Integer idEnvio){
        esModoEnvioNuevo = false;
        esModoRegistroEnvioRegistrado = true;
        this.observador = observador;
        paquete.setIdEnvio(idEnvio);
    }
    public void cargarModoEdicionPaqueteRegistrado(INotificador observador, Paquete paquete){
        esModoEnvioNuevo = false;
        esModoEdicionEnvioRegistrado = true;
        this.observador = observador;
        esModoEnvioNuevo = false;
        // Paquete ya contienen idEnvio
        this.paquete = paquete;
        cargarInformacionPaqueteModoEdicion();
        btAgregar.setText("Actualizar");
    }
    
    private void cargarInformacionPaquete(){
        taDescripcion.setText(paquete.getDescripcion());
        tfAlto.setText(paquete.getAlto().toString());
        tfAncho.setText(paquete.getAncho().toString());
        tfPeso.setText(paquete.getPeso().toString());
        tfProfundidad.setText(paquete.getProfundidad().toString());
    }

    @FXML
    private void clicAgregar(ActionEvent event) {
        
        if (!esInformacionValida()) {
            return; 
        }
        
        boolean datosConvertidos = llenarDatosPaquete();
        if (!datosConvertidos) {
            return;
        }
        if (esModoEnvioNuevo) {
            manejarModoEnvioNuevo();
        } else {
            manejarModoEnvioRegistrado();
        }
        
    }
    
    private void manejarModoEnvioNuevo() {
        this.observador.enviarObjeto(this.paquete);
        Utilidades.mostrarAlertaSimple("Paquete agregado", "El paquete se ha añadido al envío.", Alert.AlertType.INFORMATION);
        cerrarVentana();
    }
    private void manejarModoEnvioRegistrado() {
        
        if ( esModoRegistroEnvioRegistrado ) {
            // INSERTAR
            registrarPaquete();
           
        } else if (esModoEdicionEnvioRegistrado) {
            // EDITAR
            editarPaquete();
        }
    }
    
    private void registrarPaquete(){
        Respuesta respuesta = PaqueteImp.registrar(paquete);
        if ( !respuesta.isError() ){
            Utilidades.mostrarAlertaSimple("Registrado", "El paquete se ha añadido.", Alert.AlertType.INFORMATION);
            observador.notificarOperacionExitosa("agregar", "exitoso");
            cerrarVentana();
        } else {
            Utilidades.mostrarAlertaSimple("Error al registrar", respuesta.getMensaje(), Alert.AlertType.ERROR);
        }
    }
    private void editarPaquete(){
        Respuesta respuesta = PaqueteImp.editar(paquete);
        if ( !respuesta.isError() ){
            Utilidades.mostrarAlertaSimple("Actualizado", "La información del paquete ha sido actualizada.", Alert.AlertType.INFORMATION);
            observador.notificarOperacionExitosa("editar", "exitoso");
            cerrarVentana();
        } else {
            Utilidades.mostrarAlertaSimple("Error al actualizar", respuesta.getMensaje(), Alert.AlertType.ERROR);
        }
    }
    
    private boolean esInformacionValida() {
        // Descripción
        if (Validaciones.esVacio(taDescripcion.getText())) {
            Utilidades.mostrarAlertaSimple("Información inválida", "Por favor, ingrese una descripción para el paquete.", Alert.AlertType.WARNING);
            taDescripcion.requestFocus();
            return false;
        }
        // Peso
        if (Validaciones.esVacio(tfPeso.getText())) {
            Utilidades.mostrarAlertaSimple("Información inválida", "Por favor, ingrese el peso del paquete.", Alert.AlertType.WARNING);
            tfPeso.requestFocus();
            return false;
        }
        if (!esMayorACero(tfPeso.getText())) {
            Utilidades.mostrarAlertaSimple("Información inválida", "El peso debe ser mayor a 0.", Alert.AlertType.WARNING);
            tfPeso.requestFocus();
            return false;
        }
        // Altura
        if (Validaciones.esVacio(tfAlto.getText())) {
            Utilidades.mostrarAlertaSimple("Información inválida", "Por favor, ingrese la altura del paquete.", Alert.AlertType.WARNING);
            tfAlto.requestFocus(); // Pone el cursor en el campo con error
            return false;
        }
        if (!esMayorACero(tfAlto.getText())) {
            Utilidades.mostrarAlertaSimple("Información inválida", "La altura debe ser mayor a 0.", Alert.AlertType.WARNING);
            tfAlto.requestFocus();
            return false;
        }
        // Ancho
        if (Validaciones.esVacio(tfAncho.getText())) {
            Utilidades.mostrarAlertaSimple("Información inválida", "Por favor, ingrese el ancho del paquete.", Alert.AlertType.WARNING);
            tfAncho.requestFocus();
            return false;
        }
        if (!esMayorACero(tfAncho.getText())) {
            Utilidades.mostrarAlertaSimple("Información inválida", "El ancho debe ser mayor a 0.", Alert.AlertType.WARNING);
            tfAncho.requestFocus();
            return false;
        }
        // Profundidad
        if (Validaciones.esVacio(tfProfundidad.getText())) {
            Utilidades.mostrarAlertaSimple("Información inválida", "Por favor, ingrese la profundidad del paquete.", Alert.AlertType.WARNING);
            tfProfundidad.requestFocus();
            return false;
        }
        if (!esMayorACero(tfProfundidad.getText())) {
            Utilidades.mostrarAlertaSimple("Información inválida", "La profundidad debe ser mayor a 0.", Alert.AlertType.WARNING);
            tfProfundidad.requestFocus();
            return false;
        }
        return true;
    }
    private boolean esMayorACero(String numero) {
        try {
            float valor = Float.parseFloat(numero);
            return valor > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    private void cerrarVentana(){
        Stage escenario = (Stage) tfAlto.getScene().getWindow();
        escenario.close();
    }
    
    private boolean llenarDatosPaquete() {
        try {
            paquete.setDescripcion(taDescripcion.getText());
            paquete.setPeso(new BigDecimal(tfPeso.getText()).setScale(2, RoundingMode.HALF_UP));
            paquete.setAlto(new BigDecimal(tfAlto.getText()).setScale(2, RoundingMode.HALF_UP));
            paquete.setAncho(new BigDecimal(tfAncho.getText()).setScale(2, RoundingMode.HALF_UP));
            paquete.setProfundidad(new BigDecimal(tfProfundidad.getText()).setScale(2, RoundingMode.HALF_UP));
            return true;
        } catch (NumberFormatException e) {
            Utilidades.mostrarAlertaSimple("Error de formato", "Verifique que no haya caracteres inválidos.", Alert.AlertType.ERROR);
            e.printStackTrace();
            return false;
        }
    }

    private void cargarInformacionPaqueteModoEdicion() {
        taDescripcion.setText(paquete.getDescripcion());
        tfAlto.setText(paquete.getAlto().toString());
        tfAncho.setText(paquete.getAncho().toString());
        tfPeso.setText(paquete.getPeso().toString());
        tfProfundidad.setText(paquete.getProfundidad().toString());
    }
    
    @FXML
    private void clicCancelar(ActionEvent event) {
        cerrarVentana();
    }
}
