
package dto;

public class Respuesta {
    private boolean error;
    private String mensaje;
    private String valor;

    public Respuesta() {
    }

    public Respuesta(boolean error, String mensaje) {
        this.error = error;
        this.mensaje = mensaje;
    }
    public Respuesta(boolean error, String mensaje, String valor) {
        this.error = error;
        this.mensaje = mensaje;
        this.valor = valor;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
    
    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}
