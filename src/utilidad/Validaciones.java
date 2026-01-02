
package utilidad;

public class Validaciones {
    public static boolean esVacio(String campo){
        return campo == null || campo.trim().isEmpty();
    }
    
    public static boolean esNumerico(String campo) {
        return campo.matches("\\d+");
    }
    
    public static boolean esNumericoConLongitud(String campo, int longitud) {
        return campo.matches("\\d{" + longitud + "}");
    }
    public static boolean esCorreoValido(String correo) {
        return correo.matches("^[\\w.-]+@([\\w-]+\\.)+[\\w-]{2,}$");
    }
    public static boolean esSoloTexto(String texto) {
        return texto.matches("[\\p{L} ]+");
    }
}
