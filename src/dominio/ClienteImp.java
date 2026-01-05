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

    public static List<Cliente> obtenerClientes() {
        List<Cliente> lista = null;
        String url = Constantes.URL_WS + "cliente/obtener-todos";
        RespuestaHTTP respuesta = ConexionAPI.peticionGET(url);
        
        if (respuesta.getCodigo() == HttpURLConnection.HTTP_OK) {
            Type tipoLista = new TypeToken<List<Cliente>>(){}.getType();
            lista = GsonUtil.GSON.fromJson(respuesta.getContenido(), tipoLista);
        }
        return lista;
    }

    public static HashMap<String, Object> registrarCliente(Cliente cliente) {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put(Constantes.KEY_ERROR, true);
        
        String url = Constantes.URL_WS + "cliente/registrar";
        String json = GsonUtil.GSON.toJson(cliente);
        
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
    
    public static HashMap<String, Object> editarCliente(Cliente cliente) {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put(Constantes.KEY_ERROR, true);
        
        String url = Constantes.URL_WS + "cliente/editar";
        String json = GsonUtil.GSON.toJson(cliente);
        
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
    
    public static HashMap<String, Object> eliminarCliente(Integer idCliente) {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put(Constantes.KEY_ERROR, true);
        
        // Endpoint para eliminar
        String url = Constantes.URL_WS + "cliente/eliminar/" + idCliente;
        
        // Usamos peticionSinBody con DELETE
        RespuestaHTTP respuestaAPI = ConexionAPI.peticionSinBody(url, "DELETE");
        
        if (respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK) {
            Respuesta respuestaServidor = GsonUtil.GSON.fromJson(respuestaAPI.getContenido(), Respuesta.class);
            respuesta.put(Constantes.KEY_ERROR, respuestaServidor.isError());
            respuesta.put(Constantes.KEY_MENSAJE, respuestaServidor.getMensaje());
        } else {
            respuesta.put(Constantes.KEY_MENSAJE, "Error de conexión: " + respuestaAPI.getCodigo());
        }
        return respuesta;
    }

    public static HashMap<String, Object> buscarCliente(String cadena, String modoBusqueda) {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put(Constantes.KEY_ERROR, true);
        String URL = "";
        
        try {
            switch (modoBusqueda) {
                case "Correo":
                    URL = Constantes.URL_WS + "cliente/buscar/correo/" + cadena;
                    break;
                case "Teléfono":
                    URL = Constantes.URL_WS + "cliente/buscar/telefono/" + cadena;
                    break;
                default: // Por defecto busca por Nombre
                    String cadenaEncoded = URLEncoder.encode(cadena, StandardCharsets.UTF_8.toString()).replace("+", "%20");
                    URL = Constantes.URL_WS + "cliente/buscar/nombre/" + cadenaEncoded;
            }
            
            RespuestaHTTP respuestaAPI = ConexionAPI.peticionGET(URL);
            
            if (respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK) {
                Type tipoLista = new TypeToken<List<Cliente>>(){}.getType();
                List<Cliente> clientes = GsonUtil.GSON.fromJson(respuestaAPI.getContenido(), tipoLista);
                respuesta.put(Constantes.KEY_ERROR, false);
                respuesta.put(Constantes.KEY_LISTA, clientes);
            } else {
                respuesta.put(Constantes.KEY_MENSAJE, "No se encontraron resultados para la búsqueda.");
            }
        } catch (UnsupportedEncodingException e) {
            respuesta.put(Constantes.KEY_MENSAJE, "Error al procesar la búsqueda: " + e.getMessage());
        }
        return respuesta;
    }
}