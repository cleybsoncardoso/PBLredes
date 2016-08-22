/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.UnknownHostException;

/**
 *
 * @author cleyb
 */
public class App {

    public static void main(String[] args) throws UnknownHostException, IOException {
        ClienteOnline nomeCliente = new ClienteOnline();
        Servidor servidorCliente = new Servidor(12345, nomeCliente);
        Cliente atual = new Cliente(servidorCliente, nomeCliente);
        new Thread(atual).start();
        new Thread(servidorCliente).start();
    }
}
