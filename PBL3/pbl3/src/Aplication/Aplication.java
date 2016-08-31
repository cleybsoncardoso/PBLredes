/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Aplication;

import Controller.Controller;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.TrataCliente;

/**
 *
 * @author cleyb
 */
public class Aplication {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Controller controler = new Controller();
        try {
            ServerSocket servidor = new ServerSocket(8080);
            while(true){
                Socket cliente = servidor.accept();
                TrataCliente tc = new TrataCliente(cliente);
                new Thread(tc).start();
            }
        } catch (IOException ex) {
            System.out.println("não foi possivel iniciar servidor");
        }
    }
    
}
