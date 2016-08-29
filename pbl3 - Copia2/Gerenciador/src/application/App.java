/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import controller.Controller;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author paiva
 */
public class App {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            ServerSocket servidor = new ServerSocket(8080);
            while(true){
                Socket cliente = servidor.accept();
                Controller controller = new Controller(cliente, servidor);
                new Thread(controller).start();
            }
        } catch (IOException ex) {
            System.out.println("Não foi possivel abrir porta");
        }
    }
    
}
