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
public class Cliente {

    private String ip;
    private int porta;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Socket cliente;

    public Cliente(String ip, int porta) {
        try {
            cliente = new Socket(ip, porta);
            output = new ObjectOutputStream(cliente.getOutputStream());
            input = new ObjectInputStream(cliente.getInputStream());

        } catch (IOException ex) {
            System.out.println("nao foi possivel se conectar com " + ip);
        }
    }
    
    public void enviarMsg(String msg){
        try {
            output.writeObject(msg);
        } catch (IOException ex) {
            System.out.println("perdeu conexao ao escrever para " + ip);
        }
    }
    
    public Object receberMsg(){
        try {
            return input.readObject();
        } catch (IOException ex) {
            System.out.println("perdeu conexao ao receber de " + ip);
            return null;
        } catch (ClassNotFoundException ex) {
            return null;
        }
    }

}
