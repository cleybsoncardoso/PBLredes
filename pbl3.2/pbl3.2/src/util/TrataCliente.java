/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import controller.Controller;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author paiva
 */
public class TrataCliente implements Runnable {

    private Controller controller;
    private Socket cliente;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Server servidor;

    TrataCliente(Controller controller, Socket cliente, Server servidor) {
        this.controller = controller;
        this.cliente = cliente;
        this.servidor = servidor;
        try {
            output = new ObjectOutputStream(cliente.getOutputStream());
            input = new ObjectInputStream(cliente.getInputStream());
        } catch (IOException ex) {
            servidor.removerCliente(this.cliente);
            Logger.getLogger(TrataCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        while(true){
            try {
                String msg = (String) input.readObject();
                System.out.println(msg);
            } catch (IOException ex) {
                servidor.removerCliente(this.cliente);
                Logger.getLogger(TrataCliente.class.getName()).log(Level.SEVERE, null, ex);
                return;
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(TrataCliente.class.getName()).log(Level.SEVERE, null, ex);
                return;
            }
        }
    }

}