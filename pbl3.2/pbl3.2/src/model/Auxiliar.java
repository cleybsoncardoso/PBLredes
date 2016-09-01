/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLSocket;
import util.Cliente;

/**
 *
 * @author cleyse
 */
public class Auxiliar {

    private ArrayList <Cliente> clientes;

    public Auxiliar() {
        this.clientes = new ArrayList<Cliente>();
    }
    
    
    public void iniciarConexao(String ip) {
        Cliente client = new Cliente(8080, ip);
        new Thread (client).start();
        clientes.add(client);
    }

    public void replicarMsg(String msg) {
        for(Cliente c: clientes){
            c.enviarMsg(msg);
        }
    }
    
}