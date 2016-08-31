/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import Controller.Controller;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author paiva
 */
public class Server implements Runnable{
    
    private Controller controller;
    
    public Server(Controller controller){
        this.controller = controller;
    }

    @Override
    public void run() {
        try {
            ServerSocket servidor = new ServerSocket(8080);
            while(true){
                Socket cliente = servidor.accept();
                controller.addIp(cliente.getInetAddress().getHostAddress());
                TrataCliente tc = new TrataCliente(controller,cliente);
                new Thread(tc).start();
                controller.addConexao(cliente.getInetAddress().getHostAddress());
            }
        } catch (IOException ex) {
            System.out.println("não foi possivel iniciar servidor");
        }
    }   
}
