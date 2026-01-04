/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package controlpacketworld.interfaz;

/**
 *
 * @author julia
 */
public interface INotificador {
    public void notificarOperacionExitosa(String operacion, String nombre);
    public void enviarObjeto(Object object);
}
