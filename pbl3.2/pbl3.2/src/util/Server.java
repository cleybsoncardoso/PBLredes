/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import controller.Controller;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author paiva
 */
public class Server implements Runnable {

    private int porta;
    private Controller controller;
    private ArrayList<Socket> clientes;

    public Server(Controller controller, int porta) {
        this.controller = controller;
        this.porta = porta;
    }

    @Override
    public void run() {
        try {
            ServerSocket servidor = new ServerSocket(this.porta);
            System.out.println("Servidor Aberto..");
            while (true) {
                System.out.println("Esperando Cliente...");
                Socket cliente = servidor.accept();
                System.out.println("Conexao criada com " + cliente.getInetAddress().getHostAddress());
                TrataCliente tc = new TrataCliente(controller, cliente);
                new Thread(tc).start();
                controller.iniciarConexao(cliente.getInetAddress().getHostAddress());
            }
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
