/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dominio;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import conexion.ConexionAPI;
import dto.Respuesta;
import java.io.File;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import pojo.Colaborador;
import pojo.RespuestaHTTP;
import utilidad.Constantes;
import utilidad.GsonUtil;

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
    
    public static Respuesta cambiarPassword(int idColaborador, String passwordActual, String passwordNueva) {
        Respuesta respuesta = new Respuesta();
        String URL = Constantes.URL_WS + "colaborador/cambiar-password";

        try {
            String parametros = "idColaborador=" + idColaborador +
                                "&passwordActual=" + URLEncoder.encode(passwordActual, StandardCharsets.UTF_8.name()) +
                                "&passwordNueva=" + URLEncoder.encode(passwordNueva, StandardCharsets.UTF_8.name());

            RespuestaHTTP respuestaAPI = ConexionAPI.peticionBody(URL, "PUT", parametros, "application/x-www-form-urlencoded");

            if (respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK) {
                Gson gson = new Gson();
                respuesta = gson.fromJson(respuestaAPI.getContenido(), Respuesta.class);
            } else {
                respuesta.setError(true);
                respuesta.setMensaje("Error al cambiar contraseña: " + respuestaAPI.getCodigo());
            }
        } catch (Exception e) {
            respuesta.setError(true);
            respuesta.setMensaje("Error al procesar la solicitud: " + e.getMessage());
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
        return ejecutarBusqueda("nombre", nombre);
    }
    
    public static HashMap<String, Object> obtenerPorRol(String rol) {
        return ejecutarBusqueda("rol", rol);
    }
    
    public static HashMap<String, Object> obtenerPorNoPersonal(String noPersonal) {
        return ejecutarBusqueda("nopersonal", noPersonal);
    }

    private static HashMap<String, Object> ejecutarBusqueda(String criterio, String valor) {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        try {
            String valorCodificado = URLEncoder.encode(valor, StandardCharsets.UTF_8.name());
            
            String URL = Constantes.URL_WS + "colaborador/buscar?criterio=" + criterio + "&valor=" + valorCodificado;

            RespuestaHTTP respuestaAPI = ConexionAPI.peticionGET(URL);

            if (respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK) {
                Gson gson = new Gson();
                Type tipoLista = new TypeToken<List<Colaborador>>(){}.getType();
                List<Colaborador> colaboradores = gson.fromJson(respuestaAPI.getContenido(), tipoLista);

                if (colaboradores != null && !colaboradores.isEmpty()) {
                    respuesta.put("error", false);
                    respuesta.put("colaboradores", colaboradores);
                } else {
                    respuesta.put("error", true);
                    respuesta.put("mensaje", "No se encontraron coincidencias.");
                }
            } else {
                respuesta.put("error", true);
                respuesta.put("mensaje", "No se encontraron coincidencias o hubo un error en la búsqueda.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            respuesta.put("error", true);
            respuesta.put("mensaje", "Error al procesar la búsqueda: " + e.getMessage());
        }
        return respuesta;
    }
 
    public static Respuesta subirFoto(int idColaborador, File fotoFile) {
        Respuesta respuesta = new Respuesta();
        String URL = Constantes.URL_WS + "colaborador/subir-fotografia/" + idColaborador;

        try {
            byte[] fotoBytes = Files.readAllBytes(fotoFile.toPath());

            RespuestaHTTP respuestaAPI = ConexionAPI.peticionPUTImagen(URL, fotoBytes); 

            if (respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK) {
                Gson gson = new Gson();
                respuesta = gson.fromJson(respuestaAPI.getContenido(), Respuesta.class);
            } else {
                respuesta.setError(true);
                respuesta.setMensaje("Error al subir la imagen.");
            }
        } catch (Exception e) {
            respuesta.setError(true);
            respuesta.setMensaje("Error al procesar el archivo: " + e.getMessage());
        }

        return respuesta;
    }
    
    public static HashMap<String, Object> obtenerConductoresDisponibles() {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        String URL = Constantes.URL_WS + "colaborador/obtener-conductores-disponibles"; 
        
        RespuestaHTTP respuestaAPI = ConexionAPI.peticionGET(URL);
        
        if (respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK) {
            Gson gson = new Gson();
            Type tipoLista = new TypeToken<List<Colaborador>>(){}.getType();
            List<Colaborador> colaboradores = gson.fromJson(respuestaAPI.getContenido(), tipoLista);
            
            respuesta.put(Constantes.KEY_ERROR, false);
            respuesta.put("colaboradores", colaboradores);
        } else {
            respuesta.put(Constantes.KEY_ERROR, true);
            respuesta.put(Constantes.KEY_MENSAJE, "Error al cargar conductores disponibles: " + respuestaAPI.getCodigo());
        }
        
        return respuesta;
    }
        
    public static HashMap<String, Object> obtenerTodosLosConductores() {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        String URL = Constantes.URL_WS + "colaborador/obtener-conductores-todos"; 
        
        RespuestaHTTP respuestaAPI = ConexionAPI.peticionGET(URL);
        
        if (respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK) {
            Gson gson = new Gson();
            Type tipoLista = new TypeToken<List<Colaborador>>(){}.getType();
            List<Colaborador> colaboradores = gson.fromJson(respuestaAPI.getContenido(), tipoLista);
            
            respuesta.put(Constantes.KEY_ERROR, false);
            respuesta.put("colaboradores", colaboradores);
        } else {
            respuesta.put(Constantes.KEY_ERROR, true);
            respuesta.put(Constantes.KEY_MENSAJE, "Error al cargar conductores.");
        }
        return respuesta;
    }

    public static HashMap<String, Object> obtenerColaboradorPorId(int idColaborador){
        HashMap<String, Object> respuesta = new LinkedHashMap();
        String URL = Constantes.URL_WS + "colaborador/obtener/" + idColaborador;
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
    
    public static HashMap<String, Object> obtenerConductoresPorSucursal(int idSucursal) {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        
        String URL = Constantes.URL_WS + "colaborador/obtener-conductores-sucursal/" + idSucursal;
        
        RespuestaHTTP respuestaAPI = ConexionAPI.peticionGET(URL);
        
        if (respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK) {
            Gson gson = new Gson();
            Type tipoLista = new TypeToken<List<Colaborador>>(){}.getType();
            List<Colaborador> colaboradores = gson.fromJson(respuestaAPI.getContenido(), tipoLista);
            
            respuesta.put("error", false);
            respuesta.put("colaboradores", colaboradores);
        } else {
            respuesta.put("error", true);
            respuesta.put("mensaje", "Error al cargar los conductores de la sucursal.");
        }
        return respuesta;
    }
}
