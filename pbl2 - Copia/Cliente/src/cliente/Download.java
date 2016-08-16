/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cleyb
 */
public class Download implements Runnable {

    private String ip;
    private int porta;

    public Download(String ip, int porta) {
        this.ip = ip;
        this.porta = porta;
    }

    @Override
    public void run() {
        Socket baixaServidor = null;
        
        try {
            baixaServidor = new Socket(ip, porta);
            System.out.println("Conectou");
            

        } catch (IOException ex) {
            System.err.println("Download nao concluido");
        }
    }

}
