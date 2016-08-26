/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cleyb
 */
public class Servidor implements Runnable {

    private int porta;
    ServerSocket servidor;

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
            }
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
