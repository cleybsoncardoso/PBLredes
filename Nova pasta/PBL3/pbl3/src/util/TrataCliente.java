/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import Controller.Controller;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cleyb
 */
public class TrataCliente implements Runnable {

    private Controller controller;
    private Socket cliente;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    public TrataCliente(Controller controler, Socket cliente) {
        this.controller = controler;
        this.cliente = cliente;
        try {
            this.output = new ObjectOutputStream(cliente.getOutputStream());
            this.input = new ObjectInputStream(cliente.getInputStream());
        } catch (IOException ex) {
            System.out.println("Conexão Perdida");
        }
    }

    @Override
    public void run() {

        try {
            System.out.println("entrou");
            inicio();
            controller.addIp(cliente.getInetAddress().getHostAddress());
            recebendo();
        } catch (IOException ex) {
            Logger.getLogger(TrataCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void inicio() {
        try {
            String msg = input.readObject().toString();
            if (msg.equals("primeiro")) {
                output.writeObject(controller.getIpList());
            }

        } catch (IOException ex) {
            conexaoPerdida();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TrataCliente.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void recebendo() throws IOException {
        while (true) {
            try {
                String msg = input.readObject().toString();
                System.out.println(msg);
            } catch (IOException ex) {
                conexaoPerdida();
                controller.removerCliente(cliente.getInetAddress().getHostAddress());
                cliente.close();
                return;
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(TrataCliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void conexaoPerdida() {
        System.out.println("Conexao perdida com o cliente " + cliente.getInetAddress().getHostAddress());
    }

}
