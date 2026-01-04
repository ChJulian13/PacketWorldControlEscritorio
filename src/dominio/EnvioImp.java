
package dominio;


import com.google.gson.reflect.TypeToken;
import conexion.ConexionAPI;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import pojo.Cliente;
import pojo.Colaborador;
import pojo.Envio;
import pojo.RespuestaHTTP;
import pojo.Sucursal;
import utilidad.Constantes;
import utilidad.GsonUtil;


public class EnvioImp {
    
    public static HashMap<String, Object> obtenerEnvio(String noGuia) {
        HashMap<String, Object> respuesta = new LinkedHashMap();
        String URL;
        if ( noGuia == null) {
            URL = Constantes.URL_WS + "envio/obtener-envios";
        } else {
            URL = Constantes.URL_WS + "envio/obtener-envio/" + noGuia;
        }
                
        RespuestaHTTP respuestaAPI = ConexionAPI.peticionGET(URL);
        
        if( respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK ){
            // Java pierde la información de los genéricos en tiempo de ejecución
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
    
    public static HashMap<String, Object> obtenerSucursales(){
        HashMap<String, Object> respuesta = new LinkedHashMap();
        String URL = Constantes.URL_WS + "sucursal/obtener-activas";
        RespuestaHTTP respuestaAPI = ConexionAPI.peticionGET(URL);
        
        if( respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK ){
            Type tipoLista = new TypeToken<List<Sucursal>>(){}.getType();
            List<Sucursal> sucursales = GsonUtil.GSON.fromJson(respuestaAPI.getContenido(), tipoLista);
            respuesta.put(Constantes.KEY_ERROR, false);
            respuesta.put(Constantes.KEY_LISTA, sucursales);
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
    
    public static HashMap<String, Object> obtenerClientePorId(int idCliente){
        HashMap<String, Object> respuesta = new LinkedHashMap();
        String URL = Constantes.URL_WS + "cliente/buscar/id/" + idCliente;
        RespuestaHTTP respuestaAPI = ConexionAPI.peticionGET(URL);
        
        if(respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK && respuestaAPI.getContenido() != null && !respuestaAPI.getContenido().trim().isEmpty()){
            System.out.println("\n\nobtenerClientePorId" + respuestaAPI.getContenido() +"\n\n");
            Cliente cliente = GsonUtil.GSON.fromJson(respuestaAPI.getContenido(), Cliente.class);
            respuesta.put(Constantes.KEY_ERROR, false);
            respuesta.put(Constantes.KEY_OBJETO, cliente);
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
    
    public static HashMap<String, Object> obtenerColaboradorPorId(int idColaborador){
        HashMap<String, Object> respuesta = new LinkedHashMap();
        String URL = Constantes.URL_WS + "colaborador/buscar-id/" + idColaborador;
        RespuestaHTTP respuestaAPI = ConexionAPI.peticionGET(URL);
        
        if(respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK && respuestaAPI.getContenido() != null && !respuestaAPI.getContenido().trim().isEmpty()){
            Colaborador colaborador = GsonUtil.GSON.fromJson(respuestaAPI.getContenido(), Colaborador.class);
            respuesta.put(Constantes.KEY_ERROR, false);
            respuesta.put(Constantes.KEY_OBJETO, colaborador);
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
