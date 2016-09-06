/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testandoupd;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cleyb
 */
public class Cliente {

    public Cliente() {
    }

    public static void main(String[] args) {
        new Cliente().begin();
    }

    public void begin() {
        try {
            Socket socket = new Socket("192.168.0.4", 8080);
            System.out.println("conectado");
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            //ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            String ip;
            while (true) {
                System.out.println("entrou");
                ip = input.readObject().toString();
                System.err.println(ip);
                ClienteMulticast clienteM  = new ClienteMulticast(ip);
                Thread t = new Thread(clienteM);
                t.start();
                if(input.readObject().toString().equals("fechar")){
                    t.suspend();
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
