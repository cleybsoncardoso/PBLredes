/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.InformacoesCliente;

/**
 *
 * @author cleyb
 */
public class Servidor {

    private int porta;
    private ServerSocket server;
    private ArrayList<Usuario> usuarios = new ArrayList<>();
    private ArrayList<Integer> portas;
    private ArrayList<InformacoesCliente> informacoesClientes = new ArrayList<>();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Servidor servidor = new Servidor(8080);
        servidor.levantarServidor();
        servidor.esperarCliente();
    }

    public Servidor(int porta) {
        this.porta = porta;
    }

    private void levantarServidor() {
        try {
            server = new ServerSocket(porta);
            System.out.println("Servidor aberto");
        } catch (IOException ex) {
            System.out.println("Erro ao iniciar servidor");
        }
    }

    private void esperarCliente() {
        while (true) {
            try {
                Socket cliente = server.accept();
                System.out.println("Cliente " + cliente.getInetAddress().getHostAddress() + " se conectou");
                TratarCliente tc = new TratarCliente(this, cliente);
                new Thread(tc).start();

            } catch (IOException ex) {
                Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public ArrayList<Usuario> getUsuarios() {
        return usuarios;
    }

    public ArrayList<InformacoesCliente> getInformacoesClientes() {
        return informacoesClientes;
    }
    
}
