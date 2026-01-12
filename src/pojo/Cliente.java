package pojo;

public class Cliente {
private Integer idCliente;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String telefono;
    private String correo;
    
    private Integer idDireccion;
    private Integer idColonia;

 
    private String calle;
    private String numero;
    private String nombreColonia;
    private String codigoPostal;
    private String ciudad;
    private String estado;
    private String direccionCompleta;

    public Cliente() {
    }

    public Cliente(Integer idCliente, String nombre, String apellidoPaterno, String apellidoMaterno, String telefono, String correo, Integer idDireccion, Integer idColonia, String calle, String numero, String nombreColonia, String codigoPostal, String ciudad, String estado, String direccionCompleta) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.telefono = telefono;
        this.correo = correo;
        this.idDireccion = idDireccion;
        this.idColonia = idColonia;
        this.calle = calle;
        this.numero = numero;
        this.nombreColonia = nombreColonia;
        this.codigoPostal = codigoPostal;
        this.ciudad = ciudad;
        this.estado = estado;
        this.direccionCompleta = direccionCompleta;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Integer getIdDireccion() {
        return idDireccion;
    }

    public void setIdDireccion(Integer idDireccion) {
        this.idDireccion = idDireccion;
    }

    public Integer getIdColonia() {
        return idColonia;
    }

    public void setIdColonia(Integer idColonia) {
        this.idColonia = idColonia;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getNombreColonia() {
        return nombreColonia;
    }

    public void setNombreColonia(String nombreColonia) {
        this.nombreColonia = nombreColonia;
    }


    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDireccionCompleta() {
        return direccionCompleta;
    }

    public void setDireccionCompleta(String direccionCompleta) {
        this.direccionCompleta = direccionCompleta;
    }
    
    public String getCodigoPostal() {
        if (codigoPostal != null && !codigoPostal.isEmpty()) return codigoPostal;
        return extraerDato("CP");
    }

    public String getCiudad() {
        if (ciudad != null && !ciudad.isEmpty()) return ciudad;
        return extraerDato("CIUDAD");
    }

    public String getEstado() {
        if (estado != null && !estado.isEmpty()) return estado;
        return extraerDato("ESTADO");
    }

    private String extraerDato(String tipo) {
        if (direccionCompleta == null || direccionCompleta.isEmpty()) return "N/A";
        try {
            String[] partes = direccionCompleta.split(", ");
            if ("CP".equals(tipo)) {
                for (String parte : partes) {
                    if (parte.trim().startsWith("C.P.")) return parte.replace("C.P.", "").trim();
                }
            } else if ("CIUDAD".equals(tipo) && partes.length >= 2) {
                return partes[partes.length - 2].trim();
            } else if ("ESTADO".equals(tipo) && partes.length >= 1) {
                return partes[partes.length - 1].trim();
            }
        } catch (Exception e) { return ""; }
        return "";
    }
   
}

