/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author paiva
 */
public class Replicador implements Runnable {
    
    private Cliente cliente;
    private ArrayList<Cliente> lista;
    private String msg;

    public Replicador(Cliente cliente, ArrayList<Cliente> lista, String msg){
        this.cliente = cliente;
        this.lista = lista;
        this.msg = msg;
    }
    
    @Override
    public void run() {
        cliente.enviarMsg(msg);
    }

}
