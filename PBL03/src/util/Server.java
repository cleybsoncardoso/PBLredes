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
 * classe com o serversocket
 * @author Cleybson e Lucas
 */
public class Server implements Runnable {

    private int porta;
    private Controller controller;
    private ArrayList<Socket> clientes;

    public Server(int porta) {
        this.controller = Controller.getInstance();
        this.porta = porta;
        clientes = new ArrayList<>();
    }

    @Override
    public void run() {
        try {
            ServerSocket servidor = new ServerSocket(this.porta);
            while (true) {
                Socket cliente = servidor.accept();
                String ip = cliente.getInetAddress().getHostAddress();
                System.out.println(ip + " se conectou comigo");
                TrataCliente tc = new TrataCliente(cliente);
                new Thread(tc).start();
                controller.iniciarConexao(ip);

            }
        } catch (IOException ex) {

        }
    }
}
