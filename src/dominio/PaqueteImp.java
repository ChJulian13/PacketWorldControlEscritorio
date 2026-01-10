
package dominio;

import com.google.gson.reflect.TypeToken;
import conexion.ConexionAPI;
import dto.RSDistanciaKM;
import dto.Respuesta;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;
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

    public static Respuesta registrar(Paquete paquete){
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);
        String URL = Constantes.URL_WS + "paquete/registrar";
        String parametrosJSON = GsonUtil.GSON.toJson(paquete);
        RespuestaHTTP respuestaAPI = ConexionAPI.peticionBody(URL, Constantes.PETICION_POST, parametrosJSON, Constantes.APPLICATION_JSON);
        if ( respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK ){
            respuesta = GsonUtil.GSON.fromJson(respuestaAPI.getContenido(), Respuesta.class);
        } else {
            switch (respuestaAPI.getCodigo()){
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
    public static Respuesta editar(Paquete paquete){
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);
        String URL = Constantes.URL_WS + "paquete/editar";
        String parametrosJSON = GsonUtil.GSON.toJson(paquete);
        RespuestaHTTP respuestaAPI = ConexionAPI.peticionBody(URL, Constantes.PETICION_PUT, parametrosJSON, Constantes.APPLICATION_JSON);
        if ( respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK ){
            respuesta = GsonUtil.GSON.fromJson(respuestaAPI.getContenido(), Respuesta.class);
        } else {
            switch (respuestaAPI.getCodigo()){
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
    public static Respuesta eliminar(Integer idPaquete){
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);
        String URL = Constantes.URL_WS + "paquete/eliminar/" + idPaquete;

        RespuestaHTTP respuestaAPI = ConexionAPI.peticionSinBody(URL, Constantes.PETICION_DELETE);
        if ( respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK ){
            respuesta = GsonUtil.GSON.fromJson(respuestaAPI.getContenido(), Respuesta.class);
        } else {
            switch (respuestaAPI.getCodigo()){
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
}
