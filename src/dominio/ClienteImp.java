
package dominio;

import com.google.gson.reflect.TypeToken;
import conexion.ConexionAPI;
import dto.Respuesta;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import pojo.Cliente;
import pojo.RespuestaHTTP;
import utilidad.Constantes;
import utilidad.GsonUtil;

public class ClienteImp {
    public static HashMap<String, Object> buscarCliente(String cadena, String modoBusqueda) throws UnsupportedEncodingException{
        HashMap<String, Object> respuesta = new LinkedHashMap();
        String URL;
        
        switch( modoBusqueda ) {
            case "Correo":
                URL = Constantes.URL_WS + "cliente/buscar/correo/" + cadena;
                break;
            case "Teléfono":
                URL = Constantes.URL_WS + "cliente/buscar/telefono/" + cadena;
                break;
            default:
                // Es necesario codificar la URL debido a los espacios
                String cadenaEncoded = URLEncoder.encode(cadena, StandardCharsets.UTF_8.toString()) .replace("+", "%20");
                URL = Constantes.URL_WS + "cliente/buscar/nombre/" + cadenaEncoded;
        }
        RespuestaHTTP respuestaAPI = ConexionAPI.peticionGET(URL);
        
        if( respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK ){
            Type tipoLista = new TypeToken<List<Cliente>>(){}.getType();
            List<Cliente> clientes = GsonUtil.GSON.fromJson(respuestaAPI.getContenido(), tipoLista);
            respuesta.put(Constantes.KEY_ERROR, false);
            respuesta.put(Constantes.KEY_LISTA, clientes);
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
    
    public static HashMap<String, Object> registrarCliente(Cliente cliente) {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put(Constantes.KEY_ERROR, true);
        
        String url = Constantes.URL_WS + "cliente/registrar";
        String json = GsonUtil.GSON.toJson(cliente);
        
        // Usamos peticionBody porque es un POST con datos JSON
        RespuestaHTTP respuestaAPI = ConexionAPI.peticionBody(url, "POST", json, "application/json");
        
        if (respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK) {
            // Deserializamos la respuesta genérica del servidor
            Respuesta respuestaServidor = GsonUtil.GSON.fromJson(respuestaAPI.getContenido(), Respuesta.class);
            
            respuesta.put(Constantes.KEY_ERROR, respuestaServidor.isError());
            respuesta.put(Constantes.KEY_MENSAJE, respuestaServidor.getMensaje());
        } else {
            respuesta.put(Constantes.KEY_MENSAJE, "Error de conexión o validación: Código " + respuestaAPI.getCodigo());
        }
        
        return respuesta;
    }
    
}
