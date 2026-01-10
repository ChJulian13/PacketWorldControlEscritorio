
package dominio;


import com.google.gson.reflect.TypeToken;
import conexion.ConexionAPI;
import dto.EnvioCompletoDTO;
import dto.Respuesta;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import pojo.Envio;
import pojo.EnvioHistorialEstatus;
import pojo.EstatusEnvio;
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
    
    public static Respuesta crearEnvioCompleto(EnvioCompletoDTO envioCompleto){
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);
        String URL = Constantes.URL_WS + "envio/crear-envio-completo";
        String parametrosJSON = GsonUtil.GSON.toJson(envioCompleto);
        
        RespuestaHTTP respuestaAPI = ConexionAPI.peticionBody(URL, Constantes.PETICION_POST, parametrosJSON, Constantes.APPLICATION_JSON);
        if( respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK){
            respuesta = GsonUtil.GSON.fromJson(respuestaAPI.getContenido(), Respuesta.class);
        } else {
            switch( respuestaAPI.getCodigo() ) {
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
    
    public static Respuesta editar(Envio envio){
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);
        String URL = Constantes.URL_WS + "envio/editar";
        String parametrosJSON = GsonUtil.GSON.toJson(envio);
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
    
    public static HashMap<String, Object> obtenerCatalogoEstatusEnvio(){
        HashMap<String, Object> respuesta = new LinkedHashMap();
        String URL = Constantes.URL_WS + "catalogo/obtener-estatus-envios";
        RespuestaHTTP respuestaAPI = ConexionAPI.peticionGET(URL);
        
        if( respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK ){
            Type tipoLista = new TypeToken<List<EstatusEnvio>>(){}.getType();
            List<EstatusEnvio> estatus = GsonUtil.GSON.fromJson(respuestaAPI.getContenido(), tipoLista);
            respuesta.put(Constantes.KEY_ERROR, false);
            respuesta.put(Constantes.KEY_LISTA, estatus);
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

    public static Respuesta actualizarEstatus(EnvioHistorialEstatus estatus){
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);
        String URL = Constantes.URL_WS + "envio/actualizar-estatus";
        String parametrosJSON = GsonUtil.GSON.toJson(estatus);
        RespuestaHTTP respuestaAPI = ConexionAPI.peticionBody(URL, Constantes.PETICION_POST, parametrosJSON, Constantes.APPLICATION_JSON);
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
    
    public static Respuesta actualizarCosto(Envio envio){
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);
        String URL = Constantes.URL_WS + "envio/actualizar-costo";
        String parametrosJSON = GsonUtil.GSON.toJson(envio);
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

   public static Respuesta calcularCosto(double distanciaKM, int numeroPaquetes){
        Respuesta respuesta = new Respuesta();
        double costoKM;
        double costoAdicional;
        double costoEnvio;
        if ( numeroPaquetes > 0 ){
            // Calcular el costo por kilometro
            if (distanciaKM <= 200) { 
                costoKM = 4.00;
            } else if (distanciaKM <= 500) {
                costoKM = 3.00;
            } else if (distanciaKM <= 1000) {
                costoKM = 2.00;
            } else if (distanciaKM <= 2000) {
                costoKM = 1.00;
            } else {
                costoKM = 0.50;
            }
            // Calcular el costo adicional
            switch( numeroPaquetes ){
                case 1:
                    costoAdicional = 0.00;
                    break;
                case 2:
                    costoAdicional = 50.00;
                    break;
                case 3:
                    costoAdicional = 80.00;
                    break;
                case 4:
                    costoAdicional = 110.00;
                    break;
                default: // 5 paquetes o más
                    costoAdicional = 150.00;
            }
            costoEnvio = ( distanciaKM * costoKM ) + costoAdicional;
        } else {
            costoEnvio = 0.0;
        }

        respuesta.setError(false);
        BigDecimal costoRedondeado = BigDecimal.valueOf(costoEnvio).setScale(2, RoundingMode.HALF_UP);
        respuesta.setValor(String.valueOf(costoRedondeado.doubleValue()));
        respuesta.setMensaje("Costo calculado correctamente.");

       return respuesta;
   }    

    public static Respuesta obtenerCosto(double distanciaKM, int numeroPaquetes){
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);
        String URL = Constantes.URL_WS + "envio/calcular-costo/"+distanciaKM+"/"+numeroPaquetes;

        RespuestaHTTP respuestaAPI = ConexionAPI.peticionGET(URL);
        if ( respuestaAPI.getCodigo() == HttpURLConnection.HTTP_OK ){
            respuesta = utilidad.GsonUtil.GSON.fromJson(respuestaAPI.getContenido(), Respuesta.class);
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
}
