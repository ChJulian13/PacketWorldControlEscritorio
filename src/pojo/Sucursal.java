/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pojo;

/**
 *
 * @author julia
 */
public class Sucursal {
    private Integer idSucursal;
    private String codigo;
    private String nombre;
    private String estatus;
    private Integer idDireccion;
    
    private String direccionCompleta;

    private String calle;
    private String numero;
    private Integer idColonia;
    private String nombreColonia; 

    public Sucursal() {
    }

    public Sucursal(Integer idSucursal, String codigo, String nombre, String estatus, Integer idDireccion, String direccionCompleta, String calle, String numero, Integer idColonia, String nombreColonia) {
        this.idSucursal = idSucursal;
        this.codigo = codigo;
        this.nombre = nombre;
        this.estatus = estatus;
        this.idDireccion = idDireccion;
        this.direccionCompleta = direccionCompleta;
        this.calle = calle;
        this.numero = numero;
        this.idColonia = idColonia;
        this.nombreColonia = nombreColonia;
    }

    public Integer getIdSucursal() {
        return idSucursal;
    }

    public void setIdSucursal(Integer idSucursal) {
        this.idSucursal = idSucursal;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public Integer getIdDireccion() {
        return idDireccion;
    }

    public void setIdDireccion(Integer idDireccion) {
        this.idDireccion = idDireccion;
    }

    public String getDireccionCompleta() {
        return direccionCompleta;
    }

    public void setDireccionCompleta(String direccionCompleta) {
        this.direccionCompleta = direccionCompleta;
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

    public Integer getIdColonia() {
        return idColonia;
    }

    public void setIdColonia(Integer idColonia) {
        this.idColonia = idColonia;
    }

    public String getNombreColonia() {
        return nombreColonia;
    }

    public void setNombreColonia(String nombreColonia) {
        this.nombreColonia = nombreColonia;
    }
    
    public String getCp() {
        return extraerDato("CP");
    }

    public String getCiudad() {
        return extraerDato("CIUDAD");
    }

    public String getEstado() {
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
    
    @Override
    public String toString() {
        return nombre;
    }
}