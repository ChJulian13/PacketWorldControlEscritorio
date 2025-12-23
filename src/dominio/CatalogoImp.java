/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dominio;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import conexion.ConexionAPI;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import pojo.RespuestaHTTP;
import pojo.Rol;
import utilidad.Constantes;

/**
 *
 * @author julia
 */
public class CatalogoImp {
    public static HashMap<String, Object> obtenerRolesSistema() {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        String URL = Constantes.URL_WS + "catalogo/obtener-roles";
        RespuestaHTTP respuestaAPI = ConexionAPI.peticionGET(URL);
        if (respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK) {
            Gson gson = new Gson();
            Type tipoLista = new TypeToken<List<Rol>>(){}.getType();
            List<Rol> roles = gson.fromJson(respuestaAPI.getContenido(), tipoLista);
            respuesta.put(Constantes.KEY_ERROR, false);
            respuesta.put(Constantes.KEY_LISTA, roles);
        } else {
            respuesta.put(Constantes.KEY_ERROR, true);
            switch (respuestaAPI.getCodigo()) {
                case Constantes.ERROR_MALFORMED_URL:
                    respuesta.put(Constantes.KEY_MENSAJE, (String.valueOf(Constantes.ERROR_MALFORMED_URL)));
                    break;
                case Constantes.ERROR_PETICION:
                    respuesta.put(Constantes.KEY_MENSAJE, (String.valueOf(Constantes.MSJ_ERROR_PETICION)));
                    break;
                default:
                    respuesta.put(Constantes.KEY_MENSAJE, ("Lo sentimos, hay problemas para la conexión a la base de datos"));
            }  
        }
        
        return respuesta;
    }
}
