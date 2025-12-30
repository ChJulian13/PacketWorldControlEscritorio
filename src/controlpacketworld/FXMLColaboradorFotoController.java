/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controlpacketworld;

import dominio.ColaboradorImp;
import dto.Respuesta;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import utilidad.Utilidades;

/**
 * FXML Controller class
 *
 * @author julia
 */
public class FXMLColaboradorFotoController implements Initializable {
    private int idColaborador;
    @FXML
    private ImageView ivFotoColaborador;
    private File foto;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }  
    
    public void inicializarValores(int idColaborador) {
        this.idColaborador = idColaborador;
    }
    
    private void mostrarDialogoSeleccion() {
        FileChooser dialogo = new FileChooser();
        dialogo.setTitle("Selecciona una foto");
        FileChooser.ExtensionFilter filtroImg = new FileChooser.ExtensionFilter("Archivo de Imagen (.jpg, .png)", "*.jpg", "*.png");
        dialogo.getExtensionFilters().add(filtroImg);
        foto = dialogo.showOpenDialog(ivFotoColaborador.getScene().getWindow()); //No necesita casteo  
        if (foto != null) {
            mostrarFoto(foto);
        }
    }
    
    private void mostrarFoto(File file) {
        try {
            BufferedImage bufferImp = ImageIO.read(file);
            Image imagen = SwingFXUtils.toFXImage(bufferImp, null);
            ivFotoColaborador.setImage(imagen);
        } catch (IOException e) {
            Utilidades.mostrarAlertaSimple("Error", "Error al cargar la foto", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void clicSeleccionarFoto(ActionEvent event) {
        mostrarDialogoSeleccion();
    }
    
    private void cerrarVentana() {
        Stage escenario = (Stage) ivFotoColaborador.getScene().getWindow();
        escenario.close();
    }
    

    @FXML
    private void clicGuardar(ActionEvent event) {
        if (idColaborador <= 0) {
            Utilidades.mostrarAlertaSimple("Error", "No hay un profesor seleccionado para asignar la foto.", Alert.AlertType.ERROR);
            return;
        }
        
        if (foto == null) {
            Utilidades.mostrarAlertaSimple("Selecciona una imagen", "Debes seleccionar un archivo de imagen antes de guardar.", Alert.AlertType.WARNING);
            return;
        }
        Respuesta respuesta = ColaboradorImp.subirFoto(idColaborador, foto);

        if (!respuesta.isError()) {
            Utilidades.mostrarAlertaSimple("Foto guardada", respuesta.getMensaje(), Alert.AlertType.INFORMATION);
            cerrarVentana();
        } else {
            Utilidades.mostrarAlertaSimple("Error al guardar", respuesta.getMensaje(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void clicCancelar(ActionEvent event) {
        cerrarVentana();
    }
    
}
