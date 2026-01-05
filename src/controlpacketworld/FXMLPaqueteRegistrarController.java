
package controlpacketworld;

import controlpacketworld.interfaz.INotificador;
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
    
    private boolean esModoEdicion;
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
        esModoEdicion = false;
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
    
    // Modo crear
    public void cargarInformacion(INotificador observador){
        this.observador = observador;
    }
    // Modo edicion
    public void cargarModoEdicion(INotificador observador, Paquete paquete){
        this.observador = observador;
        this.paquete = paquete;
        cargarInformacionPaquete();
        btAgregar.setText("Guardar");
    }
    public void cargarModoEdicionPaqueteRegistrado(INotificador observador, Paquete paquete){
        esModoEdicion = true;
        this.paquete = paquete;
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
        if( esInformacionValida() ){
            try {
                paquete.setDescripcion(taDescripcion.getText());
                paquete.setPeso(new BigDecimal(tfPeso.getText()).setScale(2, RoundingMode.HALF_UP));
                paquete.setAlto(new BigDecimal(tfAlto.getText()).setScale(2, RoundingMode.HALF_UP));
                paquete.setAncho(new BigDecimal(tfAncho.getText()).setScale(2, RoundingMode.HALF_UP));
                paquete.setProfundidad(new BigDecimal(tfProfundidad.getText()).setScale(2, RoundingMode.HALF_UP));
            } catch (NumberFormatException e) {
                Utilidades.mostrarAlertaSimple("Error", "Ocurrió un error al convertir los números, verifique la información.", Alert.AlertType.ERROR);
                e.printStackTrace();
            }
            
            if( !esModoEdicion ){
                observador.enviarObjeto(paquete);
                Utilidades.mostrarAlertaSimple("Paquete", "El paquete se ha añadido al envío.", Alert.AlertType.INFORMATION);
                cerrarVentana();
            } else {
                // Modificar objeto
                // TODO:
                
                // Notificar edicion a observador
                observador.notificarOperacionExitosa("editar", this.paquete.getIdPaquete().toString());
                Utilidades.mostrarAlertaSimple("Paquete", "La información del paquete se ha actualizado.", Alert.AlertType.INFORMATION);
                cerrarVentana();
            }
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

    @FXML
    private void clicCancelar(ActionEvent event) {
        cerrarVentana();
    }
    
    private void cerrarVentana(){
            Stage escenario = (Stage) tfAlto.getScene().getWindow();
            escenario.close();
    }
}
