/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import controller.Controller;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author paiva
 */
public class TrataCliente implements Runnable {

    private Controller controller;
    private Socket cliente;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Server servidor;
    private String ip;

    TrataCliente(Controller controller, Socket cliente) {
        this.controller = controller;
        this.cliente = cliente;
        this.ip = this.cliente.getInetAddress().getHostAddress();
        this.servidor = servidor;
        try {
            output = new ObjectOutputStream(cliente.getOutputStream());
            input = new ObjectInputStream(cliente.getInputStream());
        } catch (IOException ex) {
            controller.removerIp(this.ip);
        }
    }

    @Override
    public void run() {

        try {
            enviaIps();
        } catch (IOException ex) {
            controller.removerIp(this.ip);
            System.out.println("Conexão perdida com " + ip);
            return;
        } catch (ClassNotFoundException ex) {
            return;
        }

        while (true) {
            try {
                String msg = (String) input.readObject();
                System.out.println(msg);
            } catch (IOException ex) {
<<<<<<< HEAD
                controller.removerIp(this.ip);
                System.out.println("Conexão perdida com " + ip);
=======
                controller.removerIp(this.cliente.getInetAddress().getHostAddress());
>>>>>>> 048622d7afe45bbc9a1d1f39de5c5c702991a6ed
                return;
            } catch (ClassNotFoundException ex) {
                return;
            }
        }
    }

    private void enviaIps() throws IOException, ClassNotFoundException {
        String msg = (String) input.readObject();
        if (msg.equals("primeiro")) {
            ArrayList<String> aux = new ArrayList<>();
            aux.addAll(controller.getIps());
            aux.remove(this.ip);
        }
    }

}
