/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pojo;

/**
 *
 * @author julia
 */
public class UnidadBaja {
    private int idUnidad;
    private String motivoBaja;
    private int idColaborador;

    public UnidadBaja() {
    }

    public UnidadBaja(int idUnidad, String motivoBaja, int idColaborador) {
        this.idUnidad = idUnidad;
        this.motivoBaja = motivoBaja;
        this.idColaborador = idColaborador;
    }

    public int getIdUnidad() {
        return idUnidad;
    }

    public void setIdUnidad(int idUnidad) {
        this.idUnidad = idUnidad;
    }

    public String getMotivoBaja() {
        return motivoBaja;
    }

    public void setMotivoBaja(String motivoBaja) {
        this.motivoBaja = motivoBaja;
    }

    public int getIdColaborador() {
        return idColaborador;
    }

    public void setIdColaborador(int idColaborador) {
        this.idColaborador = idColaborador;
    }
    
    
}
