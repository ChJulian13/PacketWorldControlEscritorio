
package pojo;

public class Envio {
    private Integer idEnvio;
    private String noGuia;
    private Double costo;
    private String destinatarioNombre;
    private String destinatarioApellidoPaterno;
    private String destinatarioApellidoMaterno;
    
    // Llaves foráneas (IDs)
    private Integer idEstatusEnvio;
    private Integer idSucursalOrigen;
    private Integer idConductor;
    private Integer idCliente;
    private Integer destinatarioIdDireccion;
    
    // Informacion recolectada de FK
    private String destinatarioDireccion;
    private String sucursal;
    private String sucursalDireccion;
    private String conductor;
    private String cliente;
    private String clienteTelefono;
    private String clienteCorreo;
    private String estatus;
        
    public Envio() {
    }

    public Envio(Integer idEnvio, String noGuia, Double costo, String destinatarioNombre, String destinatarioApellidoPaterno, String destinatarioApellidoMaterno, Integer idEstatusEnvio, Integer idSucursalOrigen, Integer idConductor, Integer idCliente, Integer destinatarioIdDireccion, String destinatarioDireccion, String sucursal, String sucursalDireccion, String conductor, String cliente, String clienteTelefono, String clienteCorreo, String estatus) {
        this.idEnvio = idEnvio;
        this.noGuia = noGuia;
        this.costo = costo;
        this.destinatarioNombre = destinatarioNombre;
        this.destinatarioApellidoPaterno = destinatarioApellidoPaterno;
        this.destinatarioApellidoMaterno = destinatarioApellidoMaterno;
        this.idEstatusEnvio = idEstatusEnvio;
        this.idSucursalOrigen = idSucursalOrigen;
        this.idConductor = idConductor;
        this.idCliente = idCliente;
        this.destinatarioIdDireccion = destinatarioIdDireccion;
        this.destinatarioDireccion = destinatarioDireccion;
        this.sucursal = sucursal;
        this.sucursalDireccion = sucursalDireccion;
        this.conductor = conductor;
        this.cliente = cliente;
        this.clienteTelefono = clienteTelefono;
        this.clienteCorreo = clienteCorreo;
        this.estatus = estatus;
    }

    public void setIdEnvio(Integer idEnvio) {
        this.idEnvio = idEnvio;
    }

    public void setNoGuia(String noGuia) {
        this.noGuia = noGuia;
    }

    public void setCosto(Double costo) {
        this.costo = costo;
    }

    public void setDestinatarioNombre(String destinatarioNombre) {
        this.destinatarioNombre = destinatarioNombre;
    }

    public void setDestinatarioApellidoPaterno(String destinatarioApellidoPaterno) {
        this.destinatarioApellidoPaterno = destinatarioApellidoPaterno;
    }

    public void setDestinatarioApellidoMaterno(String destinatarioApellidoMaterno) {
        this.destinatarioApellidoMaterno = destinatarioApellidoMaterno;
    }

    public void setIdEstatusEnvio(Integer idEstatusEnvio) {
        this.idEstatusEnvio = idEstatusEnvio;
    }

    public void setIdSucursalOrigen(Integer idSucursalOrigen) {
        this.idSucursalOrigen = idSucursalOrigen;
    }

    public void setIdConductor(Integer idConductor) {
        this.idConductor = idConductor;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public void setDestinatarioIdDireccion(Integer destinatarioIdDireccion) {
        this.destinatarioIdDireccion = destinatarioIdDireccion;
    }

    public void setDestinatarioDireccion(String destinatarioDireccion) {
        this.destinatarioDireccion = destinatarioDireccion;
    }

    public void setSucursal(String sucursal) {
        this.sucursal = sucursal;
    }

    public void setSucursalDireccion(String sucursalDireccion) {
        this.sucursalDireccion = sucursalDireccion;
    }

    public void setConductor(String conductor) {
        this.conductor = conductor;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public void setClienteTelefono(String clienteTelefono) {
        this.clienteTelefono = clienteTelefono;
    }

    public void setClienteCorreo(String clienteCorreo) {
        this.clienteCorreo = clienteCorreo;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public Integer getIdEnvio() {
        return idEnvio;
    }

    public String getNoGuia() {
        return noGuia;
    }

    public Double getCosto() {
        return costo;
    }

    public String getDestinatarioNombre() {
        return destinatarioNombre;
    }

    public String getDestinatarioApellidoPaterno() {
        return destinatarioApellidoPaterno;
    }

    public String getDestinatarioApellidoMaterno() {
        return destinatarioApellidoMaterno;
    }

    public Integer getIdEstatusEnvio() {
        return idEstatusEnvio;
    }

    public Integer getIdSucursalOrigen() {
        return idSucursalOrigen;
    }

    public Integer getIdConductor() {
        return idConductor;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public Integer getDestinatarioIdDireccion() {
        return destinatarioIdDireccion;
    }

    public String getDestinatarioDireccion() {
        return destinatarioDireccion;
    }

    public String getSucursal() {
        return sucursal;
    }

    public String getSucursalDireccion() {
        return sucursalDireccion;
    }

    public String getConductor() {
        return conductor;
    }

    public String getCliente() {
        return cliente;
    }

    public String getClienteTelefono() {
        return clienteTelefono;
    }

    public String getClienteCorreo() {
        return clienteCorreo;
    }

    public String getEstatus() {
        return estatus;
    }
}
