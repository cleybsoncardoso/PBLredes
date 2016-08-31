/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cleyb
 */
public class Auxiliar {

    private ArrayList<Socket> clientes;

    public Auxiliar() {
        this.clientes = new ArrayList<Socket>();
    }

    public void primeiraConexao(String ip, int porta) {
        try {
            Socket cliente = new Socket(ip, porta);
            clientes.add(cliente);
            ArrayList<String> ips = (ArrayList<String>) new ObjectInputStream(cliente.getInputStream()).readObject();
            for (String ipCliente : ips) {
                Socket clienteAtual = new Socket(ipCliente, 8080);
                clientes.add(clienteAtual);
            }
        } catch (IOException ex) {
            System.out.println("não foi possivel estabelecer conexao");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Auxiliar.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addSocket(String ip, int porta) {
        try {
            Socket cliente = new Socket(ip, porta);
            clientes.add(cliente);
        } catch (IOException ex) {
            System.out.println("não foi possivel estabelecer conexao");

        }
    }
}
