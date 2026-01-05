
package pojo;

import java.sql.Timestamp;

public class EnvioHistorialEstatus {
    private Integer idHistorial;
    private Integer idEnvio;
    private Integer idColaborador;
    private Integer idEstatusEnvio;
    private String comentario; 
    private Timestamp fechaHora;
    
    private String estatus;

    public EnvioHistorialEstatus() {
    }

    public EnvioHistorialEstatus(Integer idHistorial, Integer idEnvio, Integer idColaborador, Integer idEstatusEnvio, String comentario, Timestamp fechaHora, String estatus) {
        this.idHistorial = idHistorial;
        this.idEnvio = idEnvio;
        this.idColaborador = idColaborador;
        this.idEstatusEnvio = idEstatusEnvio;
        this.comentario = comentario;
        this.fechaHora = fechaHora;
        this.estatus = estatus;
    }

    public Integer getIdHistorial() {
        return idHistorial;
    }

    public void setIdHistorial(Integer idHistorial) {
        this.idHistorial = idHistorial;
    }

    public Integer getIdEnvio() {
        return idEnvio;
    }

    public void setIdEnvio(Integer idEnvio) {
        this.idEnvio = idEnvio;
    }

    public Integer getIdColaborador() {
        return idColaborador;
    }

    public void setIdColaborador(Integer idColaborador) {
        this.idColaborador = idColaborador;
    }

    public Integer getIdEstatusEnvio() {
        return idEstatusEnvio;
    }

    public void setIdEstatusEnvio(Integer idEstatusEnvio) {
        this.idEstatusEnvio = idEstatusEnvio;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Timestamp getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Timestamp fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }
}
