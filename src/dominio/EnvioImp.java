
package dominio;


import com.google.gson.reflect.TypeToken;
import conexion.ConexionAPI;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import pojo.Envio;
import pojo.RespuestaHTTP;
import utilidad.Constantes;
import utilidad.GsonUtil;


public class EnvioImp {
    
    public static HashMap<String, Object> obtenerEnvio(String noGuia) {
        HashMap<String, Object> respuesta = new LinkedHashMap();
        
        String URL = Constantes.URL_WS + "envio/obtener-envio/" + noGuia;
        RespuestaHTTP respuestaAPI = ConexionAPI.peticionGET(URL);
        
        if( respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK ){
            Type tipoLista = new TypeToken<List<Envio>>(){}.getType();
            List<Envio> envios = GsonUtil.GSON.fromJson(respuestaAPI.getContenido(), tipoLista);
            
            respuesta.put(Constantes.KEY_ERROR, false);
            respuesta.put(Constantes.KEY_LISTA, envios);
            
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
