/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pojo;

/**
 *
 * @author julia
 */
public class Unidad {
    private Integer idUnidad;
    private String marca;
    private String modelo;
    private Integer anio;
    private String vin;
    private String nii;   
    private String estatus; 
    private Integer idTipoUnidad; 
    private String nombreTipoUnidad;
    
    private String nombreConductor; 
    private Integer idConductor;

    public Unidad() {
    }

    public Unidad(Integer idUnidad, String marca, String modelo, Integer anio, String vin, String nii, String estatus, Integer idTipoUnidad, String nombreTipoUnidad, String nombreConductor, Integer idConductor) {
        this.idUnidad = idUnidad;
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
        this.vin = vin;
        this.nii = nii;
        this.estatus = estatus;
        this.idTipoUnidad = idTipoUnidad;
        this.nombreTipoUnidad = nombreTipoUnidad;
        this.nombreConductor = nombreConductor;
        this.idConductor = idConductor;
    }

    public Integer getIdUnidad() {
        return idUnidad;
    }

    public void setIdUnidad(Integer idUnidad) {
        this.idUnidad = idUnidad;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getNii() {
        return nii;
    }

    public void setNii(String nii) {
        this.nii = nii;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public Integer getIdTipoUnidad() {
        return idTipoUnidad;
    }

    public void setIdTipoUnidad(Integer idTipoUnidad) {
        this.idTipoUnidad = idTipoUnidad;
    }

    public String getNombreTipoUnidad() {
        return nombreTipoUnidad;
    }

    public void setNombreTipoUnidad(String nombreTipoUnidad) {
        this.nombreTipoUnidad = nombreTipoUnidad;
    }

    public String getNombreConductor() {
        return nombreConductor;
    }

    public void setNombreConductor(String nombreConductor) {
        this.nombreConductor = nombreConductor;
    }
    
    public String getConductorLegible() {
        if (estatus != null && estatus.equalsIgnoreCase("baja")) {
            return "NO DISPONIBLE";
        }
        
        if (nombreConductor == null || nombreConductor.trim().isEmpty()) {
            return "SIN ASIGNAR";
        }
        
        return nombreConductor;
    }

    public Integer getIdConductor() {
        return idConductor;
    }

    public void setIdConductor(Integer idConductor) {
        this.idConductor = idConductor;
    }
}
