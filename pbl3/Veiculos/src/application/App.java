/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import controller.Controller;
<<<<<<< HEAD
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
=======
>>>>>>> 85c279a371044bb662fc6867e93ba00cc8b92925

/**
 *
 * @author paiva
 */
public class App {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
<<<<<<< HEAD
        try {
            Socket cliente = new Socket("127.0.0.1", 8080);
            Controller controller = new Controller(cliente);
        } catch (IOException ex) {
            System.out.println("Erro na conexão");
        }
=======
        Controller controller = new Controller();
        controller.conectaServidor();
>>>>>>> 85c279a371044bb662fc6867e93ba00cc8b92925
    }
}
