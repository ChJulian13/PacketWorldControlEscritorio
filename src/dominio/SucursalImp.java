package dominio;

import com.google.gson.reflect.TypeToken;
import conexion.ConexionAPI;
import dto.Respuesta;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import pojo.RespuestaHTTP;
import pojo.Sucursal;
import utilidad.Constantes;
import utilidad.GsonUtil;

public class SucursalImp {

    public static List<Sucursal> obtenerSucursales() {
        List<Sucursal> lista = null;
        String url = Constantes.URL_WS + "sucursal/obtener-todos";
        RespuestaHTTP respuesta = ConexionAPI.peticionGET(url);
        
        if (respuesta.getCodigo() == HttpURLConnection.HTTP_OK) {
            Type tipoLista = new TypeToken<List<Sucursal>>(){}.getType();
            lista = GsonUtil.GSON.fromJson(respuesta.getContenido(), tipoLista);
        }
        return lista;
    }
    
    public static HashMap<String, Object> registrarSucursal(Sucursal sucursal) {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put(Constantes.KEY_ERROR, true);
        
        String url = Constantes.URL_WS + "sucursal/registrar";
        String json = GsonUtil.GSON.toJson(sucursal);
        
        RespuestaHTTP respuestaAPI = ConexionAPI.peticionBody(url, "POST", json, "application/json");
        
        if (respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK) {
            Respuesta respuestaServidor = GsonUtil.GSON.fromJson(respuestaAPI.getContenido(), Respuesta.class);
            respuesta.put(Constantes.KEY_ERROR, respuestaServidor.isError());
            respuesta.put(Constantes.KEY_MENSAJE, respuestaServidor.getMensaje());
        } else {
            respuesta.put(Constantes.KEY_MENSAJE, "Error de conexión: " + respuestaAPI.getCodigo());
        }
        return respuesta;
    }
    
    public static HashMap<String, Object> editarSucursal(Sucursal sucursal) {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put(Constantes.KEY_ERROR, true);
        
        String url = Constantes.URL_WS + "sucursal/editar";
        String json = GsonUtil.GSON.toJson(sucursal);
        
        RespuestaHTTP respuestaAPI = ConexionAPI.peticionBody(url, "PUT", json, "application/json");
        
        if (respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK) {
            Respuesta respuestaServidor = GsonUtil.GSON.fromJson(respuestaAPI.getContenido(), Respuesta.class);
            respuesta.put(Constantes.KEY_ERROR, respuestaServidor.isError());
            respuesta.put(Constantes.KEY_MENSAJE, respuestaServidor.getMensaje());
        } else {
            respuesta.put(Constantes.KEY_MENSAJE, "Error de conexión: " + respuestaAPI.getCodigo());
        }
        return respuesta;
    }
    
    public static HashMap<String, Object> eliminarSucursal(Integer idSucursal) {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put(Constantes.KEY_ERROR, true);
        
        String url = Constantes.URL_WS + "sucursal/bajar/" + idSucursal;
        String emptyJson = ""; 
        
        RespuestaHTTP respuestaAPI = ConexionAPI.peticionBody(url, "PUT", emptyJson, "application/json");
        
        
        if (respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK) {
            Respuesta respuestaServidor = GsonUtil.GSON.fromJson(respuestaAPI.getContenido(), Respuesta.class);
            respuesta.put(Constantes.KEY_ERROR, respuestaServidor.isError());
            respuesta.put(Constantes.KEY_MENSAJE, respuestaServidor.getMensaje());
        } else {
            respuesta.put(Constantes.KEY_MENSAJE, "Error de conexión: " + respuestaAPI.getCodigo());
        }
        return respuesta;
    }

    public static HashMap<String, Object> obtenerSucursalesActivasSistema() {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        String URL = Constantes.URL_WS + "sucursal/obtener-activas";
        RespuestaHTTP respuestaAPI = ConexionAPI.peticionGET(URL);
        if (respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK) {
            Type tipoLista = new TypeToken<List<Sucursal>>(){}.getType();
            List<Sucursal> sucursales = GsonUtil.GSON.fromJson(respuestaAPI.getContenido(), tipoLista);
            respuesta.put(Constantes.KEY_ERROR, false);
            respuesta.put(Constantes.KEY_LISTA, sucursales);
        } else {
            respuesta.put(Constantes.KEY_ERROR, true);
            respuesta.put(Constantes.KEY_MENSAJE, "Error al obtener sucursales activas.");
        }
        return respuesta;
    }
}