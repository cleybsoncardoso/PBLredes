/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.*;

/**
 *
 * @author paiva
 */
public class App {

    Servidor servidor;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        App Controller = new App();
    }

    public App() {
        servidor = new Servidor(8080);
        new Thread (servidor).start();
    }
    
    
}
