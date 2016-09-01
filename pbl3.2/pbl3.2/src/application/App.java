/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import controller.Controller;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.Server;
import util.TrataCliente;

/**
 *
 * @author paiva
 */
public class App {

    public static void main(String[] args) {
        Controller controller = new Controller();
        controller.iniciarConexao("25.12.22.120");
        controller.iniciarConexao("25.14.184.173");
        Server serverSocket = new Server(controller, 8080);
        new Thread (serverSocket).start();
        Scanner teclado = new Scanner(System.in);
        while(true){
            controller.replicarMsg(teclado.nextLine());
        }
    }
}
