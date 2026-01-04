
package dominio;

import com.google.gson.reflect.TypeToken;
import conexion.ConexionAPI;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import pojo.Direccion;
import pojo.RespuestaHTTP;
import utilidad.Constantes;
import utilidad.GsonUtil;

public class DireccionImp {
    
    public static HashMap<String, Object> obtenerDireccion(String codigoPostal){
        HashMap<String, Object> respuesta = new LinkedHashMap();
        String URL = Constantes.URL_WS + "direccion/obtener-direccion-codigo-postal/" + codigoPostal;
        RespuestaHTTP respuestaAPI = ConexionAPI.peticionGET(URL);
        
        if( respuestaAPI .getCodigo() == HttpURLConnection.HTTP_OK ) {
            Type tipoLista = new TypeToken<List<Direccion>>(){}.getType();
            List<Direccion> direcciones = GsonUtil.GSON.fromJson(respuestaAPI.getContenido(), tipoLista);
            respuesta.put(Constantes.KEY_ERROR, false);
            respuesta.put(Constantes.KEY_LISTA, direcciones);
        } else {
            respuesta.put(Constantes.KEY_ERROR, true);
            switch ( respuestaAPI.getCodigo() ){
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
    public static HashMap<String, Object> obtenerDireccionPorId(Integer idDireccion){
        HashMap<String, Object> respuesta = new LinkedHashMap();
        String URL = Constantes.URL_WS + "direccion/obtener-direccion-id/" + idDireccion;
        RespuestaHTTP respuestaAPI = ConexionAPI.peticionGET(URL);
        
        if( respuestaAPI .getCodigo() == HttpURLConnection.HTTP_OK ) {
            Direccion direccion = GsonUtil.GSON.fromJson(respuestaAPI.getContenido(), Direccion.class);
            respuesta.put(Constantes.KEY_ERROR, false);
            respuesta.put(Constantes.KEY_OBJETO, direccion);
        } else {
            respuesta.put(Constantes.KEY_ERROR, true);
            switch ( respuestaAPI.getCodigo() ){
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
