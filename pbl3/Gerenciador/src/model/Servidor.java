/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.TrataCliente;

/**
 *
 * @author cleyb
 */
public class Servidor implements Runnable {

    private int porta;
    private ServerSocket servidor;
    private ArrayList<Carro> carros;

    public Servidor(int porta) {
        this.porta = porta;
        try {
            servidor = new ServerSocket(porta);
        } catch (IOException ex) {
            System.err.println("Porta não pode ser aberta");
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                Socket cliente = servidor.accept();
                Carro car = new Carro(cliente);
                carros.add(car);
                TrataCliente trataCliente = new TrataCliente(cliente, car);
                new Thread(trataCliente).start();
            }
        } catch (IOException ex) {
            System.out.println("Servidor ficou offline");
        }
    }

}
