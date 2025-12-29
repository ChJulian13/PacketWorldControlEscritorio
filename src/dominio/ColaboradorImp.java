/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dominio;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import conexion.ConexionAPI;
import dto.Respuesta;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import pojo.Colaborador;
import pojo.RespuestaHTTP;
import utilidad.Constantes;

/**
 *
 * @author julia
 */
public class ColaboradorImp {
    public static HashMap<String, Object> obtenerTodos() {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        String URL = Constantes.URL_WS + "colaborador/obtener-todos";
        RespuestaHTTP respuestaAPI = ConexionAPI.peticionGET(URL);
        if (respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK) {
            Gson gson = new Gson();
            Type tipoLista = new TypeToken<List<Colaborador>>(){}.getType();
            List<Colaborador> colaboradores = gson.fromJson(respuestaAPI.getContenido(), tipoLista);
            respuesta.put("error", false);
            respuesta.put("colaboradores",colaboradores);
        } else {
           respuesta.put("Error", true);
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
    
    
    public static Respuesta registrar(Colaborador colaborador) {
        Respuesta respuesta = new Respuesta();
        String URL = Constantes.URL_WS + "colaborador/registrar";
        Gson gson = new Gson();
        String ParametrosJSON = gson.toJson(colaborador);
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
    
    public static Respuesta editar (Colaborador colaborador) {
        Respuesta respuesta = new Respuesta();
        String URL = Constantes.URL_WS + "colaborador/editar";
        Gson gson = new Gson();
        String parametrosJSON = gson.toJson(colaborador);
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
    
    public static Respuesta eliminar(int idColaborador) {
        Respuesta respuesta = new Respuesta();
        Gson gson = new Gson();
        String URL = Constantes.URL_WS + "colaborador/eliminar/" + idColaborador;
        RespuestaHTTP respuestaAPI = ConexionAPI.peticionSinBody(URL, Constantes.PETICION_DELETE);
        if (respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK) {
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
                    respuesta.setMensaje("Lo sentimos, hay problemas para eliminar la información en este momento, por favor intentelo más tarde");
            }
        }
        
        return respuesta;
    }
    
    public static HashMap<String, Object> obtenerPorNombre(String nombre) {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        try {
            String nombreCodificado = URLEncoder.encode(nombre, StandardCharsets.UTF_8.name());
            nombreCodificado = nombreCodificado.replace("+", "%20");
            String URL = Constantes.URL_WS + "colaborador/buscar-nombre/" + nombreCodificado;

            RespuestaHTTP respuestaAPI = ConexionAPI.peticionGET(URL);

            if (respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK) {
                Gson gson = new Gson();
                Type tipoLista = new TypeToken<List<Colaborador>>(){}.getType();
                List<Colaborador> colaboradores = gson.fromJson(respuestaAPI.getContenido(), tipoLista);

                respuesta.put("error", false);
                respuesta.put("colaboradores", colaboradores);
            } else {
                respuesta.put("error", true);
                respuesta.put("mensaje", "No se encontraron colaboradores con ese nombre.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            respuesta.put("error", true);
            respuesta.put("mensaje", "Error al procesar la búsqueda por nombre.");
        }
        return respuesta;
    }
    
    public static HashMap<String, Object> obtenerPorRol(String rol) {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        try {
            String rolCodificado = URLEncoder.encode(rol, StandardCharsets.UTF_8.name());
            rolCodificado = rolCodificado.replace("+", "%20"); 
            String URL = Constantes.URL_WS + "colaborador/buscar-rol/" + rolCodificado;

            RespuestaHTTP respuestaAPI = ConexionAPI.peticionGET(URL);

            if (respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK) {
                Gson gson = new Gson();
                Type tipoLista = new TypeToken<List<Colaborador>>(){}.getType();
                List<Colaborador> colaboradores = gson.fromJson(respuestaAPI.getContenido(), tipoLista);
                respuesta.put("error", false);
                respuesta.put("colaboradores", colaboradores);
            } else {
                respuesta.put("error", true);
                respuesta.put("mensaje", "No se encontraron coincidencias.");
            }
        } catch (Exception e) {
            respuesta.put("error", true);
            respuesta.put("mensaje", "Error al codificar la URL.");
        }

        return respuesta;
    }
    
    public static HashMap<String, Object> obtenerPorNoPersonal(String noPersonal) {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        String URL = Constantes.URL_WS + "colaborador/buscar-nopersonal/" + noPersonal;
        RespuestaHTTP respuestaAPI = ConexionAPI.peticionGET(URL);
        if (respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK) {
            Gson gson = new Gson();
            Type tipoLista = new TypeToken<List<Colaborador>>(){}.getType();
            List<Colaborador> colaboradores = gson.fromJson(respuestaAPI.getContenido(), tipoLista);
            respuesta.put("error", false);
            respuesta.put("colaboradores",colaboradores);
        } else {
           respuesta.put("Error", true);
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
    
}
