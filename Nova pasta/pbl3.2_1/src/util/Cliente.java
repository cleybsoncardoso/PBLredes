/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import static java.lang.Thread.sleep;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author paiva
 */
public class Cliente implements Runnable {

    private int porta;
    private String ip;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Socket cliente;
    private boolean enviarMsg;
    private Object msg;

    public Cliente(int porta, String ip) {
        this.porta = porta;
        this.ip = ip;
        try {
            cliente = new Socket(ip, porta);
            output = new ObjectOutputStream(cliente.getOutputStream());
            input = new ObjectInputStream(cliente.getInputStream());
            System.out.println("Conexao realizada");
        } catch (IOException ex) {//Caso ocorra um erro na comunicação
            System.out.println("Servidor esta offline");
        }

    }

    public String getIp() {
        return ip;
    }

    @Override
    public void run() {
        while (true) {
            try {
                sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (enviarMsg) {
                try {
                    output.writeObject(msg);
                    enviarMsg = false;
                } catch (IOException ex) {
                    Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public ObjectInputStream getInput() {
        return input;
    }

    public void enviarMsg(Object msg) {
        this.msg = msg;
        this.enviarMsg = true;
    }

}
