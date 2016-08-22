package servidor;

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
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.Arquivo;
import util.InformacoesCliente;

/**
 * Classe Servidor Principal responsável por administrar as conexões p2p entre
 * os usuários. Nela contém uma lista de arquivos que serão compartilhados.
 *
 * @see Usuario
 * @see Arquivo
 * @see InformacoesCliente
 * @see Serializable
 * @see ServerSocket
 * @see Socket
 */
public class Servidor {

    private int porta; //porta que o servidor irá abrir
    private ServerSocket server;
    private ArrayList<Usuario> usuarios; //lista de usuarios cadastrados
    private ArrayList<Integer> portas; //lista de clientes conectados ao servidor
    private ArrayList<InformacoesCliente> informacoesClientes = new ArrayList<>(); //lista de arquivos/informacoes dos clientes conectados

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Servidor servidor = new Servidor(8080);//inicia o servidor
        servidor.levantarServidor();
        servidor.esperarCliente();
    }

    /**
     * Construtor que recebe como parâmetro a porta em que o servidor será
     * mantido.
     *
     * @param porta
     */
    public Servidor(int porta) {
        this.porta = porta;
        this.carregarUsuarios();
    }

    /**
     * Método responsável por abrir o servidor na porta indicada.
     */
    private void levantarServidor() {
        try {
            server = new ServerSocket(porta);//abre o servidor na porta indicada
            System.out.println("Servidor aberto");
        } catch (IOException ex) {
            System.out.println("Erro ao iniciar servidor");
        }
    }

    /**
     * Método responsável por esperar um cliente e direcioná-lo a uma thread
     * capaz de ouvi-lo.
     */
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

    /**
     * Método que retorna a lista de usuários.
     *
     * @return
     * @see Usuario
     */
    public ArrayList<Usuario> getUsuarios() {
        return usuarios;
    }

    /**
     * Método que retorna a lista contendo informações dos arquivos disponíveis.
     *
     * @return
     * @see InformacoesCliente
     * @see Usuario
     */
    public ArrayList<InformacoesCliente> getInformacoesClientes() {
        return informacoesClientes;
    }

    /**
     * Método que salva a lista de usuarios em arquivo serializado.
     *
     * @see Serializable
     * @see Usuario
     */
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

    /**
     * Método que lê lista de usuários da lista serializada.
     *
     * @see Serializable
     * @see Usuario
     */
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

        deslogarClientes();

    }

    /**
     * Método que desloga todos os usuarios caso o servidor feche de forma
     * inesperada.
     *
     * @see Usuarios
     */
    private void deslogarClientes() {
        Iterator i = usuarios.iterator();
        while (i.hasNext()) {
            Usuario atual = (Usuario) i.next();
            atual.setOnline(false);
        }
    }

}
