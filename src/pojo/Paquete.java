
package pojo;

import java.math.BigDecimal;


public class Paquete {
    Integer idPaquete;
    Integer idEnvio;
    String noGuia;
    String descripcion;
    BigDecimal peso;
    BigDecimal alto;
    BigDecimal ancho;
    BigDecimal profundidad;

    public Paquete() {
        
    }

    public Paquete(Integer idPaquete, Integer idEnvio, String noGuia, String descripcion, BigDecimal peso, BigDecimal alto, BigDecimal ancho, BigDecimal profundidad) {
        this.idPaquete = idPaquete;
        this.idEnvio = idEnvio;
        this.noGuia = noGuia;
        this.descripcion = descripcion;
        this.peso = peso;
        this.alto = alto;
        this.ancho = ancho;
        this.profundidad = profundidad;
    }

    public void setIdPaquete(Integer idPaquete) {
        this.idPaquete = idPaquete;
    }

    public void setIdEnvio(Integer idEnvio) {
        this.idEnvio = idEnvio;
    }

    public void setNoGuia(String noGuia) {
        this.noGuia = noGuia;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setPeso(BigDecimal peso) {
        this.peso = peso;
    }

    public void setAlto(BigDecimal alto) {
        this.alto = alto;
    }

    public void setAncho(BigDecimal ancho) {
        this.ancho = ancho;
    }

    public void setProfundidad(BigDecimal profundidad) {
        this.profundidad = profundidad;
    }

    public Integer getIdPaquete() {
        return idPaquete;
    }

    public Integer getIdEnvio() {
        return idEnvio;
    }

    public String getNoGuia() {
        return noGuia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public BigDecimal getPeso() {
        return peso;
    }

    public BigDecimal getAlto() {
        return alto;
    }

    public BigDecimal getAncho() {
        return ancho;
    }

    public BigDecimal getProfundidad() {
        return profundidad;
    }
    
}
