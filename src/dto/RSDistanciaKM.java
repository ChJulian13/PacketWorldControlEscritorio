
package dto;


public class RSDistanciaKM {
    boolean error;
    double distanciaKM;
    String mensaje;

    public RSDistanciaKM() {
    }

    public RSDistanciaKM(boolean error, double distanciaKM, String mensaje) {
        this.error = error;
        this.distanciaKM = distanciaKM;
        this.mensaje = mensaje;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public void setDistanciaKM(double distanciaKM) {
        this.distanciaKM = distanciaKM;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public boolean isError() {
        return error;
    }

    public double getDistanciaKM() {
        return distanciaKM;
    }

    public String getMensaje() {
        return mensaje;
    }
    
}
