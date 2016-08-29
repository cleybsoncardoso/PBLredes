/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

<<<<<<< HEAD
=======
import java.io.IOException;
>>>>>>> 85c279a371044bb662fc6867e93ba00cc8b92925
import java.net.Socket;

/**
 *
 * @author paiva
 */
public class Controller {
    private Socket cliente;

    public Controller(Socket cliente) {
        this.cliente = cliente;
    }
    
    
    private final String ipServidor = "127.0.0.1";
    private final int portaServidor = 12345;
    

    public void conectaServidor() {
        try {
            Socket socket = new Socket(ipServidor, portaServidor);
        } catch (IOException ex) {
            System.out.println("Não foi possível se conectar com o servidor");
        }
    }
    
}
