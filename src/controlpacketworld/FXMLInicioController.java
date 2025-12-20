/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controlpacketworld;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pojo.Colaborador;

/**
 * FXML Controller class
 *
 * @author julia
 */
public class FXMLInicioController implements Initializable {
    @FXML
    private Label lbNombreCompleto;
    @FXML
    private Label lbNoPersonal;
    private Colaborador colaboradorSesion;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }

    public void cargarInformacion(Colaborador colaborador) {
        colaboradorSesion = colaborador;
        lbNombreCompleto.setText(colaborador.getNombre() + " " + colaborador.getApellidoPaterno() + " " + colaborador.getApellidoMaterno());
        lbNoPersonal.setText("Num Personal: " + colaborador.getNoPersonal());
                
    }

    @FXML
    private void cliclColaboradores(ActionEvent event) {
        try {
            Parent vista = FXMLLoader.load(getClass().getResource("FXMLColaboradores.fxml"));
            Scene scAdminUsuarios = new Scene(vista);
            
            //generar un nuevo stage
            Stage stAdmin = new Stage();
            stAdmin.setScene(scAdminUsuarios);
            stAdmin.setTitle("Colaboradores");
            stAdmin.initModality(Modality.APPLICATION_MODAL);
            stAdmin.showAndWait();
        } catch (IOException ex) {
           ex.printStackTrace();
        }
    }

    @FXML
    private void clicUnidades(ActionEvent event) {
        try {
            Parent vista = FXMLLoader.load(getClass().getResource("FXMLUnidades.fxml"));
            Scene scAdminUsuarios = new Scene(vista);
            
            //generar un nuevo stage
            Stage stAdmin = new Stage();
            stAdmin.setScene(scAdminUsuarios);
            stAdmin.setTitle("Unidades");
            stAdmin.initModality(Modality.APPLICATION_MODAL);
            stAdmin.showAndWait();
        } catch (IOException ex) {
           ex.printStackTrace();
        }
    }

    @FXML
    private void clicSucursales(ActionEvent event) {
        try {
            Parent vista = FXMLLoader.load(getClass().getResource("FXMLSucursales.fxml"));
            Scene scAdminUsuarios = new Scene(vista);
            
            //generar un nuevo stage
            Stage stAdmin = new Stage();
            stAdmin.setScene(scAdminUsuarios);
            stAdmin.setTitle("Sucursales");
            stAdmin.initModality(Modality.APPLICATION_MODAL);
            stAdmin.showAndWait();
        } catch (IOException ex) {
           ex.printStackTrace();
        }
    }

    @FXML
    private void clicClientes(ActionEvent event) {
        try {
            Parent vista = FXMLLoader.load(getClass().getResource("FXMLClientes.fxml"));
            Scene scAdminUsuarios = new Scene(vista);
            
            //generar un nuevo stage
            Stage stAdmin = new Stage();
            stAdmin.setScene(scAdminUsuarios);
            stAdmin.setTitle("Clientes");
            stAdmin.initModality(Modality.APPLICATION_MODAL);
            stAdmin.showAndWait();
        } catch (IOException ex) {
           ex.printStackTrace();
        }
    }

    @FXML
    private void clicCerrarSesion(ActionEvent event) {
         try {
            Parent vista = FXMLLoader.load(getClass().getResource("FXMLInicioSesion.fxml"));
            
            //Stage -> Scena -> Parent -> FXML
            Scene escenaInicioSesion = new Scene(vista);
            
            //Stage stInicioSesion = (Stage)btnCerrarSesion.getScene().getWindow();
            //Otra forma utilizando el objeto que generó el listener, hay que recordar que viene de una interfaz 
            //Se tiene que castear porque proviene de objeto, la superpadre y java no sabe qué proviene de un boton
           
            Stage stInicioSesion = (Stage) ((Button) event.getSource()).getScene().getWindow();
            
            stInicioSesion.setScene(escenaInicioSesion);
            
            stInicioSesion.show();
            
           
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void clicEnvios(ActionEvent event) {
        try {
            Parent vista = FXMLLoader.load(getClass().getResource("FXMLEnvio.fxml"));
            Scene scEnvio = new Scene(vista);
            Stage stEnvio = new Stage();
            stEnvio.setScene(scEnvio);
            stEnvio.setTitle("Envios");
            stEnvio.initModality(Modality.APPLICATION_MODAL);
            stEnvio.showAndWait();
        } catch (IOException ex) {
           ex.printStackTrace();
        }
    }
    
}
