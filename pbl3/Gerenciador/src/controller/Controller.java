/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import model.Servidor;

/**
 *
 * @author paiva
 */
public class Controller {
    
    private Servidor servidor;
    private final int porta = 12345;

    public void iniciarServidor() {
        servidor = new Servidor(porta);
        new Thread(servidor).start();
    }
    
}
