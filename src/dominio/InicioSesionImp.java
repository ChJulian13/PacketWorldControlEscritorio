/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dominio;

import com.google.gson.Gson;
import conexion.ConexionAPI;
import dto.RSAutenticacionAdmin;
import java.net.HttpURLConnection;
import pojo.RespuestaHTTP;
import utilidad.Constantes;

/**
 *
 * @author julia
 */
public class InicioSesionImp {
    public static RSAutenticacionAdmin verificarCredenciales(String noPersonal, String password) {
        RSAutenticacionAdmin respuesta = new RSAutenticacionAdmin();
        String parametros = "noPersonal=" + noPersonal + "&password=" + password; 
        String URL = Constantes.URL_WS + "autenticacion/escritorio";
        RespuestaHTTP respuestaApi = ConexionAPI.peticionBody(URL,"POST", parametros, "application/x-www-form-urlencoded");
        
        if(respuestaApi.getCodigo() == HttpURLConnection.HTTP_OK) {
            try {
                Gson gson = new Gson();
                respuesta = gson.fromJson(respuestaApi.getContenido(), RSAutenticacionAdmin.class);
            } catch (Exception e) {
                respuesta.setError(true);
                respuesta.setMensaje("Lo sentimos, hubo un error al obtener la información, intente de nuevo más tarde.");
            }
        } else {
            respuesta.setError(true);
            switch (respuestaApi.getCodigo()) {
                case Constantes.ERROR_MALFORMED_URL:
                    respuesta.setMensaje(String.valueOf(Constantes.ERROR_MALFORMED_URL));
                    break;
                case Constantes.ERROR_PETICION:
                    respuesta.setMensaje(String.valueOf(Constantes.MSJ_ERROR_PETICION));
                    break;
                case HttpURLConnection.HTTP_BAD_REQUEST:
                    respuesta.setMensaje("Datos requetidos para poder realizar la operación solicitada");
                    break;
                default:
                    respuesta.setMensaje("Lo sentimos, hay problemas para verificar sus credenciales en este momento, por favor inténtelo más tarde.");
            }
        }
        return respuesta;
    }
}
