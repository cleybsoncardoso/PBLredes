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

/**
 *
 * @author paiva
 */
public class TrataCliente implements Runnable {

    private static int id_counter = 1;
    private Controller controller;
    private Socket cliente;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Server servidor;
    private String ip;
    private int id;
    private int modifier = 0;

    TrataCliente(Controller controller, Socket cliente) {
        this.id = id_counter;
        this.id_counter++;
        System.out.println(id);
        this.controller = controller;
        this.cliente = cliente;
        this.ip = this.cliente.getInetAddress().getHostAddress();
        this.servidor = servidor;
        try {
            output = new ObjectOutputStream(cliente.getOutputStream());
            input = new ObjectInputStream(cliente.getInputStream());
        } catch (IOException ex) {
//            controller.removerIp(this.ip);
            System.out.println("Conexão perdida com " + ip);
        }
    }

    @Override
    public void run() {

        try {
            enviaIps();
        } catch (IOException ex) {
            //           controller.removerIp(this.ip);
            System.out.println("Conexão perdida com " + ip);
            return;
        } catch (ClassNotFoundException ex) {
            return;
        }

        while (true) {
            try {
//                String msg = (String) input.readObject();
//                System.out.println(msg);
                
                ArrayList<Object> mensagem = (ArrayList<Object>) input.readObject();
                float x = (float) mensagem.get(0);
                float y = (float) mensagem.get(1);
                int direcao = (int) mensagem.get(2);
                ArrayList<Quadrante> trajeto = (ArrayList<Quadrante>) mensagem.get(3);
                //System.out.println("mensagem recebida");
                if (modifier == 0) {
                    controller.adicionarCarro(id, x, y, direcao, trajeto);
                    modifier = 1;
                } else {
                    controller.getCarro(this.id).setXY(x, y, direcao);
                    controller.getCarro(this.id).setTrajeto(trajeto);
                }

            } catch (IOException ex) {
//                controller.removerIp(this.ip);
                System.out.println("Conexão perdida com " + ip);
                return;
            } catch (ClassNotFoundException ex) {
                return;
            }
        }
    }

    private void enviaIps() throws IOException, ClassNotFoundException {
        String msg = (String) input.readObject();
        System.out.println(msg);
        if (msg.equals("primeiro")) {
            ArrayList<String> aux = new ArrayList<>();
            aux.addAll(controller.getIps());
            aux.remove(this.ip);
            output.writeObject(aux);
            System.out.println("saiu");
        }
    }

}
