
package pojo;

public class EstatusEnvio {
    Integer idEstatusEnvio;
    String nombre;

    public EstatusEnvio() {
    }

    public EstatusEnvio(Integer idEstatusEnvio, String nombre) {
        this.idEstatusEnvio = idEstatusEnvio;
        this.nombre = nombre;
    }

    public Integer getIdEstatusEnvio() {
        return idEstatusEnvio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setIdEstatusEnvio(Integer idEstatusEnvio) {
        this.idEstatusEnvio = idEstatusEnvio;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
}
