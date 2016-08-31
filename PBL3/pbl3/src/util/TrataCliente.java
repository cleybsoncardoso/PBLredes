/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import Controller.Controller;
import java.net.Socket;

/**
 *
 * @author cleyb
 */
public class TrataCliente implements Runnable{

    private Controller controller;
    private Socket cliente;
    
    public TrataCliente(Controller controler, Socket cliente) {
        this.controller = controler;
        this.cliente=cliente;
    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
