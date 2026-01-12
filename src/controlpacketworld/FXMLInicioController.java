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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pojo.Colaborador;
import utilidad.Utilidades;

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
    @FXML
    private Label lbRol;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }

    public void cargarInformacion(Colaborador colaborador) {
        colaboradorSesion = colaborador;
        lbNombreCompleto.setText(colaborador.getNombre() + " " + colaborador.getApellidoPaterno() + " " + colaborador.getApellidoMaterno());
        lbNoPersonal.setText("NP: " + colaborador.getNoPersonal());
        lbRol.setText(colaborador.getRol());
                
    }

    @FXML
    private void cliclColaboradores(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLColaboradores.fxml"));
            Parent vista = loader.load(); 
            
            FXMLColaboradoresController controlador = loader.getController();
            controlador.inicializar(this.colaboradorSesion); 

            Scene sceneColaboradores = new Scene(vista);
            Stage stageColaboradores = new Stage();
            
            stageColaboradores.setScene(sceneColaboradores);
            stageColaboradores.setTitle("Gestión de Colaboradores");
            stageColaboradores.initModality(Modality.APPLICATION_MODAL);
            stageColaboradores.showAndWait();
        } catch (IOException ex) {
           ex.printStackTrace();
        }
    }

    @FXML
    private void clicUnidades(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLUnidades.fxml"));
            Parent vista = loader.load();
            FXMLUnidadesController controlador = loader.getController();
            controlador.inicializarColaborador(this.colaboradorSesion);

            Scene sceneUnidades = new Scene(vista);
            Stage stageUnidades = new Stage();
            
            stageUnidades.setScene(sceneUnidades);
            stageUnidades.setTitle("Gestión de Unidades");
            stageUnidades.initModality(Modality.APPLICATION_MODAL);
            stageUnidades.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void clicSucursales(ActionEvent event) {
        try {
            Parent vista = FXMLLoader.load(getClass().getResource("FXMLSucursales.fxml"));
            
            Scene sceneSucursales = new Scene(vista);
            Stage stageSucursales = new Stage();
            
            stageSucursales.setScene(sceneSucursales);
            stageSucursales.setTitle("Gestión de Sucursales");
            stageSucursales.initModality(Modality.APPLICATION_MODAL);
            stageSucursales.showAndWait();
        } catch (IOException ex) {
           ex.printStackTrace();
        }
    }

    @FXML
    private void clicClientes(ActionEvent event) {
        try {
            Parent vista = FXMLLoader.load(getClass().getResource("FXMLClientes.fxml"));
            
            Scene sceneClientes = new Scene(vista);
            Stage stageClientes = new Stage();
            
            stageClientes.setScene(sceneClientes);
            stageClientes.setTitle("Gestión de Clientes");
            stageClientes.initModality(Modality.APPLICATION_MODAL);
            stageClientes.showAndWait();
        } catch (IOException ex) {
           ex.printStackTrace();
        }
    }

    @FXML
    private void clicCerrarSesion(ActionEvent event) {
         try {
            Parent vista = FXMLLoader.load(getClass().getResource("FXMLInicioSesion.fxml"));
            
            Scene sceneInicioSesion = new Scene(vista);
            Stage stageInicioSesion = (Stage) ((Button) event.getSource()).getScene().getWindow();
            
            stageInicioSesion.setScene(sceneInicioSesion);
            stageInicioSesion.setTitle("Inicio de Sesión"); 
            stageInicioSesion.show();
            
         } catch (IOException ex) {
            ex.printStackTrace();
         }
    }

    @FXML
    private void clicEnvios(ActionEvent event) {
        try {
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("FXMLEnvio.fxml"));
            Parent vista = cargador.load();
            FXMLEnvioController controlador = cargador.getController();
            controlador.cargarInformacion(colaboradorSesion.getIdSucursal(), colaboradorSesion.getIdColaborador());
            
            Scene sceneEnvios = new Scene(vista);
            Stage stageEnvios = new Stage();
            
            stageEnvios.setScene(sceneEnvios);
            stageEnvios.setTitle("Gestión de Envíos");
            stageEnvios.initModality(Modality.APPLICATION_MODAL);
            stageEnvios.showAndWait();
        } catch (IOException ex) {
           ex.printStackTrace();
        }
    } 

    @FXML
    private void clicManualUsuario(ActionEvent event) {
        try {
            String url = "https://uvmx-my.sharepoint.com/:w:/g/personal/zs22016079_estudiantes_uv_mx/IQC68Wms34EySpoxdc4TG12mAdZjtjT6QPFHKHw7SsLar4U?e=6ETUXs"; 
            java.awt.Desktop.getDesktop().browse(new java.net.URI(url));
        } catch (Exception e) {
            e.printStackTrace();
            Utilidades.mostrarAlertaSimple("Error", "No se pudo abrir el enlace.", Alert.AlertType.ERROR);
        }
    }
}