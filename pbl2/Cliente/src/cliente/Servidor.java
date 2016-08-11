/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cleyb
 */
public class Servidor {

    public Servidor(int porta) {
        try {
            ServerSocket servidorCliente = new ServerSocket(porta);
            while (true) {
                Socket cliente = servidorCliente.accept();
                System.out.println("Cliente " + cliente.getInetAddress().getHostAddress() + " se conectou");
                TratarCliente tc = new TratarCliente(this, cliente);
                new Thread(tc).start();
            }

        } catch (IOException ex) {
            System.out.println("Porta existente");
        }
    }

}
