
package dominio;

import com.google.gson.reflect.TypeToken;
import conexion.ConexionAPI;
import dto.RSDistanciaKM;
import dto.RespuestaGenerica;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import pojo.Paquete;
import pojo.RespuestaHTTP;
import utilidad.Constantes;
import utilidad.GsonUtil;


public class PaqueteImp {
    
   public static RSDistanciaKM obtenerDistanciaKM(Integer cpOrigen, Integer cpDestino){
       RSDistanciaKM respuesta = new RSDistanciaKM();
       String URL = "http://sublimas.com.mx:8080/calculadora/api/envios/distancia/"+cpOrigen+","+cpDestino;
       RespuestaHTTP respuestaAPI = ConexionAPI.peticionGET(URL);
       if ( respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK ){
           respuesta = GsonUtil.GSON.fromJson(respuestaAPI.getContenido(), RSDistanciaKM.class);
       }  else {
            respuesta.setError(true);
            switch( respuestaAPI.getCodigo() ) {
                case Constantes.ERROR_MALFORMED_URL:
                    respuesta.setMensaje(Constantes.MSJ_ERROR_URL);
                    break;
                case Constantes.ERROR_PETICION:
                    respuesta.setMensaje(Constantes.MSJ_ERROR_PETICION);
                    break;
                default:
                    respuesta.setMensaje(Constantes.MSJ_DEFAULT);
            }
        }
       return respuesta;
   }
   
   public static RespuestaGenerica<Double> CalcularCosto(double distanciaKM, int numeroPaquetes){
        RespuestaGenerica<Double> respuesta = new RespuestaGenerica<>();
        double costoKM;
        double costoAdicional;
        double costoEnvio;
        if ( numeroPaquetes > 0 ){
            // Calcular el costo por kilometro
            if (distanciaKM <= 200) { 
                costoKM = 4.00;
            } else if (distanciaKM <= 500) {
                costoKM = 3.00;
            } else if (distanciaKM <= 1000) {
                costoKM = 2.00;
            } else if (distanciaKM <= 2000) {
                costoKM = 1.00;
            } else {
                costoKM = 0.50;
            }
            // Calcular el costo adicional
            switch( numeroPaquetes ){
                case 1:
                    costoAdicional = 0.00;
                    break;
                case 2:
                    costoAdicional = 50.00;
                    break;
                case 3:
                    costoAdicional = 80.00;
                    break;
                case 4:
                    costoAdicional = 110.00;
                    break;
                default: // 5 paquetes o más
                    costoAdicional = 150.00;
            }
            costoEnvio = ( distanciaKM * costoKM ) + costoAdicional;
        } else {
            costoEnvio = 0.0;
        }

        respuesta.setError(false);
        respuesta.setValor(costoEnvio);
        respuesta.setMensaje("Costo calculado correctamente.");

       return respuesta;
   }
   
   public static HashMap<String, Object> obtenerPaquetesEnvio(Integer idEnvio){
       HashMap<String, Object> respuesta = new LinkedHashMap();
       String URL = Constantes.URL_WS + "paquete/obtener-paquetes-envio/" + idEnvio;
       RespuestaHTTP respuestaAPI = ConexionAPI.peticionGET(URL);
       
       if( respuestaAPI.getCodigo() ==  HttpURLConnection.HTTP_OK ){
           Type tipoLista = new TypeToken<List<Paquete>>(){}.getType();
           List<Paquete> paquetes = GsonUtil.GSON.fromJson(respuestaAPI.getContenido(), tipoLista);
           respuesta.put(Constantes.KEY_ERROR, false);
           respuesta.put(Constantes.KEY_LISTA, paquetes);
       } else {
            respuesta.put(Constantes.KEY_ERROR, true);
            switch( respuestaAPI.getCodigo() ) {
                case Constantes.ERROR_MALFORMED_URL:
                    respuesta.put(Constantes.KEY_MENSAJE, Constantes.MSJ_ERROR_URL);
                    break;
                case Constantes.ERROR_PETICION:
                    respuesta.put(Constantes.KEY_MENSAJE, Constantes.MSJ_ERROR_PETICION);
                    break;
                default:
                    respuesta.put(Constantes.KEY_MENSAJE, Constantes.MSJ_DEFAULT);
            }
        }
       return respuesta;
   }
}
