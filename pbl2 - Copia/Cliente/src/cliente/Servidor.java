package cliente;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe que representa um servidor de conexão p2p. Ela se conecta com o Socket
 * de outros clientes dentro de uma Thread que é executada pelo App.
 *
 * @see ServerSocket
 * @see Socket
 * @see Runnable
 */
public class Servidor implements Runnable {

    ServerSocket servidorCliente;
    ClienteOnline nomeCliente;

    /**
     * Construtor que recebe como parâmetro a porta em que o servidor será
     * aberto e informações do cliente.
     *
     * @param porta
     * @param nomeCliente
     */
    public Servidor(int porta, ClienteOnline nomeCliente) {
        this.nomeCliente = nomeCliente;
        try {
            servidorCliente = new ServerSocket(porta);

        } catch (IOException ex) {
            System.out.println("Porta existente");
        }
    }

    /**
     * Método que retorna o nome da classe ClienteOnline.
     * @return 
     */
    public String getNomeCliente() {
        return nomeCliente.getNomeCLiente();
    }

    /**
     * Método que retorna a referencia do ServerSocket.
     * @return 
     */
    public ServerSocket getServidorCliente() {
        return servidorCliente;
    }

    /**
     * Método que é chamado quando a Thread que a classe está é executada.
     */
    @Override
    public void run() {
        while (true) {
            Socket cliente;
            try {
                //Servidor espera por um cliente:
                cliente = servidorCliente.accept();
                System.out.println("Cliente " + cliente.getInetAddress().getHostAddress() + " se conectou");
                TratarCliente tc = new TratarCliente(this, cliente);
                new Thread(tc).start();
            } catch (IOException ex) {
                Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

}
