
package dominio;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import conexion.ConexionAPI;
import dto.Respuesta;
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
    
    public static Respuesta obtenerCodigoPostalSucursal(Integer idSucursal){
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);
        String URL = Constantes.URL_WS + "direccion/obtener-cp-sucursal/" + idSucursal;
        RespuestaHTTP respuestaAPI = ConexionAPI.peticionGET(URL);
        
        if ( respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK){
            respuesta = GsonUtil.GSON.fromJson( respuestaAPI.getContenido(), Respuesta.class);
        }else {
            switch ( respuestaAPI.getCodigo() ){
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
    public static Respuesta editar(Direccion direccion){
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);
        String URL = Constantes.URL_WS + "direccion/editar";
        String parametrosJSON = GsonUtil.GSON.toJson(direccion);
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
    
    public static HashMap<String, Object> registrarDireccion(Direccion direccion) {
        HashMap<String, Object> respuesta = new HashMap<>();
        respuesta.put(Constantes.KEY_ERROR, true);
        
        try {
            Gson gson = new Gson();
            String parametros = gson.toJson(direccion);
            String url = Constantes.URL_WS + "direccion/crear-direccion";
            
            RespuestaHTTP respuestaPeticion = ConexionAPI.peticionBody(url, "POST", parametros, "application/json");
            
            if (respuestaPeticion.getCodigo() == HttpURLConnection.HTTP_OK) {
                Type tipoMapa = new TypeToken<HashMap<String, Object>>() {}.getType();
                HashMap<String, Object> respuestaApi = gson.fromJson(respuestaPeticion.getContenido(), tipoMapa);
                
                respuesta.put(Constantes.KEY_ERROR, respuestaApi.get("error"));
                respuesta.put(Constantes.KEY_MENSAJE, respuestaApi.get("mensaje"));
                
                if (respuestaApi.containsKey("idDireccion")) {
                    Number idNum = (Number) respuestaApi.get("idDireccion");
                    respuesta.put("idDireccion", idNum.intValue());
                }
            } else {
                respuesta.put(Constantes.KEY_MENSAJE, "Error en la solicitud: Código " + respuestaPeticion.getCodigo());
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            respuesta.put(Constantes.KEY_MENSAJE, "Error de conexión: " + e.getMessage());
        }
        
        return respuesta;
    }
    
}
