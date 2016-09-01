/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
    private String msg;

    public Cliente(int porta, String ip) {
        this.porta = porta;
        this.ip = ip;

    }

    @Override
    public void run() {
        realizarConexao();
        while (true) {
            if(enviarMsg){
                try {
                    output.writeObject(msg);
                    enviarMsg = false;
                } catch (IOException ex) {
                    Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private void realizarConexao() {
        try {
            cliente = new Socket(ip, porta);
            output = new ObjectOutputStream(cliente.getOutputStream());
            input = new ObjectInputStream(cliente.getInputStream());

        } catch (IOException ex) {//Caso ocorra um erro na comunicação
            System.out.println("Servidor esta offline");
        }
    }
    
    public void enviarMsg(String msg){
        this.msg = msg;
        this.enviarMsg = true;
    }

}
