/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
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
    private ArrayList<Usuario> usuarios;
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
        this.carregarUsuarios();
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

    public void salvarUsuarios() {
        try {
            //serializa e salva lista dos usuarios
            FileOutputStream fileOut = new FileOutputStream("lista.dat");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            if (usuarios == null) {
                usuarios = new ArrayList<>();
                System.out.println("\n\n\nLista de usuarios: " + usuarios.size());
            }
            out.writeObject(usuarios);
            out.close();
            fileOut.close();
            System.out.println("Servidor gravou lista de usuários.");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Não foi possível salvar usuários. Erro: " + ex.getMessage());
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Não foi possível salvar usuários. Erro: " + ex.getMessage());
        }

    }

    private void carregarUsuarios() {
        try {//pega a serialização dos usuarios já cadastrados.
            FileInputStream fileIn = new FileInputStream("lista.dat");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            usuarios = (ArrayList<Usuario>) in.readObject();
            in.close();
            fileIn.close();
            System.out.println("\nServidor leu lista de usuários");
        } catch (FileNotFoundException ex) {
            
            this.salvarUsuarios();
            
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
