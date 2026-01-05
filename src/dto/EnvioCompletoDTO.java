
package dto;

import java.util.List;
import pojo.Direccion;
import pojo.Envio;
import pojo.Paquete;

public class EnvioCompletoDTO {
    Envio envio;
    Direccion direccion;
    List<Paquete> paquetes;

    public EnvioCompletoDTO() {
    }

    public EnvioCompletoDTO(Envio envio, Direccion direccion, List<Paquete> paquetes) {
        this.envio = envio;
        this.direccion = direccion;
        this.paquetes = paquetes;
    }

    public Envio getEnvio() {
        return envio;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public List<Paquete> getPaquetes() {
        return paquetes;
    }

    public void setEnvio(Envio envio) {
        this.envio = envio;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public void setPaquetes(List<Paquete> paquetes) {
        this.paquetes = paquetes;
    }
}
