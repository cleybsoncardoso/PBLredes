package ldserver;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe responsável por iniciar o sistema do servidor.
 *
 * @author Ícaro Rios
 */
public class LDServer {

    private final ServerSocket server;
    private final List<Usuario> usuarios;
    private List<Usuario> usuariosEspeciais;
    private static Scanner teclado;

    /**
     * Construtor do servidor, inicializa o server e as listas.
     *
     * @param port
     * @throws IOException
     */
    public LDServer(int port) throws IOException {
        this.usuarios = new ArrayList<>();
        this.usuariosEspeciais = new ArrayList<>();
        this.server = new ServerSocket(port);

    }

    /**
     * Método responsável por ler do arquivo e carregar usuários especiais.
     *
     */
    public void carregarrEspeciais() {
        try {
            FileInputStream arquivoLeitura = new FileInputStream("Especiais.ser");
            ObjectInputStream objLeitura = new ObjectInputStream(arquivoLeitura);
            usuariosEspeciais = (List<Usuario>) objLeitura.readObject();
            arquivoLeitura.close();
            objLeitura.close();
            arquivoLeitura.close();

        } catch (FileNotFoundException ex) {
            System.out.println("Arquivo de usuarios especiais não foi encontrado.");
        } catch (ClassNotFoundException ex) {
            System.out.println("Arquivo de usuarios especiais incorreto.");
        } catch (IOException ex) {
            System.out.println("Erro na comunicação.");
        }
    }

    /**
     * Método que salva os usuarios especiais da lista no arquivo.
     */
    public void salvarUsuariosEspeciais() {

        FileOutputStream fs;
        try {
            fs = new FileOutputStream("Especiais.ser");
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(usuariosEspeciais);

            fs.flush();
            fs.close();
            os.flush();
            os.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Arquivo não encontrado.");
        } catch (IOException ex) {
            System.out.println("Erro na comunicação.");
        }

    }

    /**
     * Método responsável por cadastrar um usuario especial
     *
     * @param login
     * @param senha
     * @return True se conseguiu cadasrar
     */
    private boolean cadastrar(String login, String senha) {
        Usuario novo = new Usuario(login, senha);

        for (Usuario usuario1 : usuarios) {
            if (novo.getLogin().equals(usuario1.getLogin())) {
                return false;
            }
        }
        for (Usuario usuario1 : usuariosEspeciais) {
            if (novo.getLogin().equals(usuario1.getLogin())) {
                return false;
            }
        }

        Usuario especial = new Usuario(novo.getLogin(), novo.getSenha(), true);
        usuariosEspeciais.add(especial);
        return true;
    }

    public void iniciar() {
        carregarrEspeciais();
        teclado = new Scanner(System.in);

        String resposta;
        do {
            System.out.println("Inserir usuarios especiais? Aperte 1. \nOu qualquer tecla para continuar.");
            resposta = teclado.nextLine();
            if (resposta.equalsIgnoreCase("1")) {
                System.out.println("Digite um login.");
                String login = teclado.nextLine();
                System.out.println("Digite uma senha.");
                String senha = teclado.nextLine();
                if (cadastrar(login, senha)) {
                    System.out.println("Usuario especial cadastrado com sucesso.");
                    salvarUsuariosEspeciais();
                } else {
                    System.out.println("Usuario não pode ser cadastrado.");
                }
            }
        } while (resposta.equalsIgnoreCase("1"));

        /**
         * Lista todos os IPs da máquina... O IPV4 provavelmente será o 3 a ser
         * listado.
         */
        
        Enumeration e;
        try {
            e = NetworkInterface.getNetworkInterfaces();
            while (e.hasMoreElements()) {
                NetworkInterface n = (NetworkInterface) e.nextElement();
                Enumeration ee = n.getInetAddresses();
                while (ee.hasMoreElements()) {
                    InetAddress i = (InetAddress) ee.nextElement();
                    System.out.println("IP = " + i.getHostAddress());
                }
            }
        } catch (SocketException ex) {
            System.out.println("Não foi possível listar os IPs da máquina.");
        }
        
        System.out.println("Esperando conexões...");
        /**
         * Colocar para pegar a lista de forma percistente substituir o while
         * true para enquanto não cebeber algum caractere especifico para fechar
         */
        while (true) {
            try {
                Socket clientSock = server.accept();
                System.out.println("conectado");
                ServerRecebedor r = new ServerRecebedor(clientSock, usuarios, usuariosEspeciais);
                new Thread(r).start(); // mandou pra thread e iniciou ela

            } catch (IOException ex) {
                System.out.println("Erro na comunicação.");
            }
        }
    }

    /**
     * Método que faz uma recurção com exception, 
     * só é possóvel sair até que consiga levantar o servidor
     * @return 
     */
    public static int levantarServidor() {
        try {
            teclado = new Scanner(System.in);
            System.out.println("Digite a porta do Servidor");
            return teclado.nextInt();
        } catch (Exception ex) {
            System.out.println("Digite um número.");
            levantarServidor();
        }
        return 0;
    }

    /**
     * Main do sistema.
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        /**
         * permitir setar a porta mannualmente
         */
        int porta = levantarServidor();
        LDServer fs;
        try {
            fs = new LDServer(porta);
            fs.iniciar();

        } catch (IOException ex) {
            Logger.getLogger(LDServer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
