/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dominio;

import com.google.gson.Gson;
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
import pojo.RespuestaHTTP;
import pojo.Unidad;
import pojo.UnidadBaja;
import utilidad.Constantes;

/**
 *
 * @author julia
 */
public class UnidadImp {
    public static HashMap<String, Object> obtenerTodos() {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        String URL = Constantes.URL_WS + "unidades/obtener-todas";
        RespuestaHTTP respuestaAPI = ConexionAPI.peticionGET(URL);
        if (respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK) {
            Gson gson = new Gson();
            Type tipoLista = new TypeToken<List<Unidad>>(){}.getType();
            List<Unidad> unidades = gson.fromJson(respuestaAPI.getContenido(), tipoLista);
            respuesta.put("error", false);
            respuesta.put("unidades",unidades);
        } else {
           respuesta.put("error", true);
            switch(respuestaAPI.getCodigo()){
                    case Constantes.ERROR_MALFORMED_URL:
                        respuesta.put("mensaje",(String.valueOf(Constantes.MSJ_ERROR_PETICION)));
                        break;

                    case Constantes.ERROR_PETICION:
                        respuesta.put("mensaje",((String.valueOf(Constantes.MSJ_ERROR_PETICION))));
                        break;
                    default:
                        respuesta.put("mensaje",("Lo sentimos, hay problemas para verificar sus credenciales en este momento, por favor intentelo más tarde."));
                } 
        }
        return respuesta;
    }
    
    public static Respuesta registrar(Unidad unidad) {
        Respuesta respuesta = new Respuesta();
        String URL = Constantes.URL_WS + "unidades/registrar";
        Gson gson = new Gson();
        String ParametrosJSON = gson.toJson(unidad);
        RespuestaHTTP respuestaAPI = ConexionAPI.peticionBody(URL, Constantes.PETICION_POST, ParametrosJSON, Constantes.APPLICATION_JSON);
        if (respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK) {
            respuesta = gson.fromJson(respuestaAPI.getContenido(), Respuesta.class);
        } else {
            respuesta.setError(true);
                switch(respuestaAPI.getCodigo()){
                       case Constantes.ERROR_MALFORMED_URL:

                           respuesta.setMensaje(String.valueOf(Constantes.MSJ_ERROR_PETICION));
                           break;

                       case Constantes.ERROR_PETICION:
                           respuesta.setMensaje((String.valueOf(Constantes.MSJ_ERROR_PETICION)));
                           break;
                       default:
                           respuesta.setMensaje("Lo sentimos, hay problemas para verificar sus credenciales en este momento, por favor intentelo más tarde.");
                }
        }
        
        return respuesta;
    }
    
    public static Respuesta editar (Unidad unidad) {
        Respuesta respuesta = new Respuesta();
        String URL = Constantes.URL_WS + "unidades/editar";
        Gson gson = new Gson();
        String parametrosJSON = gson.toJson(unidad);
        RespuestaHTTP respuestaAPI = ConexionAPI.peticionBody(URL, "PUT", parametrosJSON, Constantes.APPLICATION_JSON);
        
        if(respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK) {
            respuesta = gson.fromJson(respuestaAPI.getContenido(), Respuesta.class);
        } else {
            respuesta.setError(true);
            switch(respuestaAPI.getCodigo()) {
                case Constantes.ERROR_MALFORMED_URL:
                    respuesta.setMensaje(String.valueOf(Constantes.MSJ_ERROR_PETICION));
                    break;
                case Constantes.ERROR_PETICION:
                    respuesta.setMensaje((String.valueOf(Constantes.MSJ_ERROR_PETICION)));
                    break;
                default:
                    respuesta.setMensaje("Lo sentimos, hay problemas para editar la información en este momento, por favor intentelo más tarde");
            }
        }
        
        return respuesta;
    }
    
    public static Respuesta darBaja (UnidadBaja unidadBaja) {
        Respuesta respuesta = new Respuesta();
        String URL = Constantes.URL_WS + "unidades/dar-baja";
        Gson gson = new Gson();
        String parametrosJSON = gson.toJson(unidadBaja);
        RespuestaHTTP respuestaAPI = ConexionAPI.peticionBody(URL, "PUT", parametrosJSON, Constantes.APPLICATION_JSON);
        
        if(respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK) {
            respuesta = gson.fromJson(respuestaAPI.getContenido(), Respuesta.class);
        } else {
            respuesta.setError(true);
            switch(respuestaAPI.getCodigo()) {
                case Constantes.ERROR_MALFORMED_URL:
                    respuesta.setMensaje(String.valueOf(Constantes.MSJ_ERROR_PETICION));
                    break;
                case Constantes.ERROR_PETICION:
                    respuesta.setMensaje((String.valueOf(Constantes.MSJ_ERROR_PETICION)));
                    break;
                default:
                    respuesta.setMensaje("Lo sentimos, hay problemas para editar la información en este momento, por favor intentelo más tarde");
            }
        }
        
        return respuesta;
    }
    
   public static HashMap<String, Object> buscar(String textoBusqueda) {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("error", true); // Asumimos error por defecto
        
        try {
            String busquedaCodificada = URLEncoder.encode(textoBusqueda, StandardCharsets.UTF_8.toString());
            
            String URL = Constantes.URL_WS + "unidades/buscar?texto=" + busquedaCodificada;
            
            RespuestaHTTP respuestaAPI = ConexionAPI.peticionGET(URL);
            
            if (respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK) {
                Gson gson = new Gson();
                Type tipoLista = new TypeToken<List<Unidad>>(){}.getType();
                List<Unidad> unidades = gson.fromJson(respuestaAPI.getContenido(), tipoLista);
                
                respuesta.put("error", false);
                respuesta.put("unidades", unidades);
            } else {
                respuesta.put("mensaje", "Error en la petición: " + respuestaAPI.getCodigo());
            }
        } catch (UnsupportedEncodingException e) {
            respuesta.put("mensaje", "Error al codificar el texto de búsqueda.");
            e.printStackTrace();
        }
        
        return respuesta;
    }
}
