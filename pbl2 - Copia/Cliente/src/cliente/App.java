package cliente;

import java.io.IOException;
import java.net.UnknownHostException;

/**
 *
 * @author cleyb
 */
public class App {

    public static void main(String[] args) throws UnknownHostException, IOException {
        //classe "ClienteOnline" criada para ser um objeto em comum e poder ser modificado tanto pela Classe do ServeSocket criado pelo cliente, quanto para a classe cleinte 
        ClienteOnline nomeCliente = new ClienteOnline();
        Servidor servidorCliente = new Servidor(12345, nomeCliente);//Servidor tem como parametro a porta que o ServeSocket será criado e o Nome do usuario logado
        Cliente atual = new Cliente(servidorCliente, nomeCliente);//passa para o cliente o servesocket que será criado e o nome do usuario
        new Thread(atual).start();//inicia thread o cliente
        new Thread(servidorCliente).start();//inicia thread do servidor
    }
}
