package utilidad;

public class Validaciones {
    
    private static final String REGEX_SOLO_LETRAS = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$";
    private static final String REGEX_NUM_PERSONAL = "^PW(?![0]+$)[0-9]{3,10}$";
    private static final String REGEX_EMAIL = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    private static final String REGEX_CURP = "^[A-Z]{1}[AEIOU]{1}[A-Z]{2}[0-9]{2}(0[1-9]|1[0-2])(0[1-9]|1[0-9]|2[0-9]|3[0-1])[HM]{1}(AS|BC|BS|CC|CL|CM|CS|CH|DF|DG|GT|GR|HG|JC|MC|MN|MS|NT|NL|OC|PL|QT|QR|SP|SL|SR|TC|TS|TL|VZ|YN|ZS|NE)[B-DF-HJ-NP-TV-Z]{3}[0-9A-Z]{1}[0-9]{1}$";
    private static final String REGEX_PASSWORD_SEGURA = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
    private static final String REGEX_NUM_LICENCIA = "^[A-Z0-9\\-]{5,20}$";

    public static boolean esVacio(String campo){
        return campo == null || campo.trim().isEmpty();
    }
    
    public static boolean esNumerico(String campo) {
        return campo != null && campo.matches("\\d+");
    }
    
    public static boolean esNumericoConLongitud(String campo, int longitud) {
        return campo != null && campo.matches("\\d{" + longitud + "}");
    }

    public static boolean esCorreoValido(String correo) {
        return correo != null && correo.matches(REGEX_EMAIL);
    }

    public static boolean esSoloTexto(String texto) {
        return texto != null && texto.matches(REGEX_SOLO_LETRAS);
    }
    
    public static boolean esNumPersonalValido(String numPersonal) {
        return numPersonal != null && numPersonal.matches(REGEX_NUM_PERSONAL);
    }
    
    public static boolean esCurpValida(String curp) {
        return curp != null && curp.toUpperCase().matches(REGEX_CURP);
    }
    
    public static boolean esPasswordSegura(String password) {
        return password != null && password.matches(REGEX_PASSWORD_SEGURA);
    }
    
    public static boolean esLicenciaValida(String licencia) {
        return licencia != null && licencia.toUpperCase().matches(REGEX_NUM_LICENCIA);
    }
}