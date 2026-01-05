
package dto;

public class RespuestaGenerica<T> {
    private boolean error;
    private String mensaje;
    private T valor;

    public RespuestaGenerica() {
    }
    
    public RespuestaGenerica(boolean error, String mensaje, T valor) {
        this.error = error;
        this.mensaje = mensaje;
        this.valor = valor;
    }

    public boolean isError() {
        return error;
    }

    public String getMensaje() {
        return mensaje;
    }

    public T getValor() {
        return valor;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public void setValor(T valor) {
        this.valor = valor;
    }
    
}
