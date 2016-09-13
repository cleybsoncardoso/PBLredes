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
 * Classe que se conecta com os serversockets
 * @author Cleybson e Lucas
 */
public class Cliente implements Runnable {

    private int porta;//minha porta
    private String ip;//meu ip
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Socket cliente;
    private boolean enviarMsg;//boolean para controlar quando chega uma msg nova
    private Object msg;//msg a ser enviada

    
    public Cliente(int porta, String ip) {
        this.porta = porta;
        this.ip = ip;
        try {
            cliente = new Socket(ip, porta);
            output = new ObjectOutputStream(cliente.getOutputStream());
            input = new ObjectInputStream(cliente.getInputStream());
        } catch (IOException ex) {//Caso ocorra um erro na comunicação
            controller.Controller.getInstance().removerIp(ip);
            try {
                cliente.close();
                return;
            } catch (IOException ex1) {
            }
        }

    }

    public String getIp() {
        return ip;
    }

    @Override
    public void run() {
        while (true) {
            try {//sleep para dar o tempo de alterar o valor da variavel
                sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (enviarMsg) {//verifica se tem msg nova
                try {
                    output.writeObject(msg);//envia a msg para o serversocket
                    enviarMsg = false;
                } catch (IOException ex) {
                    controller.Controller.getInstance().removerIp(ip);
                    try {
                        cliente.close();
                        return;
                    } catch (IOException ex1) {
                        Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex1);
                    }
                }
            }
        }
    }

    public ObjectInputStream getInput() {
        return input;
    }

    /**
     * Método que altera o valor da variavel e altera o valor da variavel msg, para a msg atual
     * @param msg 
     */
    public void enviarMsg(Object msg) {
        this.msg = msg;
        this.enviarMsg = true;
    }

}
