/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorftp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cleyb
 */
public class Servidor implements Serializable {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        new Servidor(12345).executa();//instancia a classe e salva o numero da porta e chama o metodo "executa"
    }

    private int porta;
    private ArrayList<Usuario> usuarios;//lista de usuarios cadastrados

    public Servidor(int porta) {
        this.porta = porta;
        this.usuarios = new ArrayList<>();
    }

    private void executa() throws IOException {
        this.read();//Salva os usuarios que ja foram cadastrados uma vez no sistema
        ServerSocket servidor = new ServerSocket(this.porta);//abre a porta da transmissão
        System.out.println("\nPorta 12345 aberta!");
        while (true) {
            Socket cliente = servidor.accept();//espera um cliente novo
            System.out.println("\nNova conexão com o cliente " + cliente.getInetAddress().getHostAddress());
            TrataCliente tc = new TrataCliente(cliente, this);//coloca ele no Runnable
            new Thread(tc).start();//coloca ele em uma nova thread
        }
    }

    public ArrayList<Usuario> getUsuarios() {
        return usuarios;//retorna lista de usuarios
    }

    public void write() {
        try {//serializa e salva os usuarios
            FileOutputStream fileOut = new FileOutputStream("lista.dat");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(usuarios);
            out.close();
            fileOut.close();
            System.out.println("\nServidor gravou lista de usuários");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Não foi possível salvar usuários. Erro: " + ex.getMessage());
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Não foi possível salvar usuários. Erro: " + ex.getMessage());
        }

    }

    public void read() {
        try {//pega a serialização dos usuarios já cadastrados.
            FileInputStream fileIn = new FileInputStream("lista.dat");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            usuarios = (ArrayList<Usuario>) in.readObject();
            in.close();
            fileIn.close();
            System.out.println("\nServidor leu lista de usuários");

        } catch (FileNotFoundException ex) {
            System.out.println("Erro na leitura de usuários: " + ex.getMessage());
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("Erro na leitura de usuários: " + ex.getMessage());
        }

    }
}
