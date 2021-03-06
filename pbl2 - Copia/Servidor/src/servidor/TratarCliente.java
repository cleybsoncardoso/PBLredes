package servidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.Arquivo;
import util.InformacoesCliente;

/**
 *
 * Classe responsável por manter a conexão do cliente com o servidor através de
 * uma thread. Toda comunicação com o cliente acontece em TrataCliente, portanto
 * ela possui uma referencia do Socket do cliente e informações do usuário
 * conectado.
 *
 * @see Socket
 * @see ServerSocket
 * @see Usuario
 * @see Arquivo
 * @see InformacoesCliente
 *
 * @author paiva
 */
class TratarCliente implements Runnable {

    private Servidor servidor;
    private Socket cliente;
    private Usuario logado;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private InformacoesCliente informacoes;
    private ArrayList<InformacoesCliente> informacoesClientes;
    private int porta;

    /**
     * Construtor que recebe como parâmetro o socket do cliente que está se
     * comunicando e a referencia do servidor principal.
     *
     * @param servidor
     * @param cliente
     */
    public TratarCliente(Servidor servidor, Socket cliente) {
        this.cliente = cliente;
        this.servidor = servidor;
        try {
            input = new ObjectInputStream(cliente.getInputStream());
            output = new ObjectOutputStream(cliente.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(TratarCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Método que é chamado quando a Thread é executada. O cliente envia uma
     * string que indentifica qual o seu estado(logado/deslogado), apartir daí o
     * login de usuário é reconhecido e faz a verificação se é possível realizar
     * login.
     *
     */
    @Override
    public void run() {

        try {
            /*cliente enviar o nome de usuario do cliente. Se for deslogado é
            porque ainda não possui um usuario definido.
             */
            String nomeCliente = input.readObject().toString();
            if (!nomeCliente.equals("deslogado")) {
                for (Usuario atual : servidor.getUsuarios()) {
                    if (atual.getLogin().equals(nomeCliente)) {
                        logado = atual;
                        break;
                    }
                }
                //o cliente ja tinha logado anteriormente e agora nao precisa inserir informacoes de login e senha.
                this.logado();
            } else {

                /*Usuário enviar qual ação deseja executar e insere as informacoes necessarias*/
                while (true) {

                    System.out.println("Esperando opção do cliente " + cliente.getInetAddress().getHostAddress() + ".");

                    try {
                        //esperando opcao de menu do cliente
                        String opcaoCliente = input.readObject().toString();
                        switch (opcaoCliente) {
                            //caso cliente deseje cadastrar novo conta de usuario
                            case "cadastro":
                                this.cadastro(); //cadastro é realizado.
                                servidor.salvarUsuarios(); //servidor salva lista de usuarios em arquivo serializado.
                                break;
                            //enviando 'logar' o tratacliente verifica se é possível se logar.
                            case "logar":
                                if (this.login()) {
                                    logado();
                                }
                                break;
                        }
                    } catch (IOException ex) {
                        //caso a conexao seja perdida o usuario é deslogado e seus arquivos saem do sistema.
                        System.err.println("Cliente " + cliente.getInetAddress().getHostAddress() + " se desconectou.");
                        servidor.getInformacoesClientes().remove(informacoes);

                        return;
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(TratarCliente.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        } catch (IOException ex) {
            System.err.println("Cliente " + cliente.getInetAddress().getHostAddress() + " se desconectou.");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TratarCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Método responsável por receber as informações do servidor referente ao
     * cliente. O cliente enviar os arquivos disponíveis na pasta compartilhada
     * e a porta em que o seu ServerSocket está disponível.
     *
     * @see ServerSocket
     * @see Arquivo
     * @see InformacoesCliente.
     */
    private void receberInformacoes() {
        try {
            //cliente envia lista de arquivo do seu repositório com porta do servidor
            System.out.println("Recebendo informacoes do servidor " + cliente.getInetAddress().getHostAddress() + ".");
            ArrayList arquivos = (ArrayList<Arquivo>) input.readObject();

            //porta do servidor do cliente
            this.porta = (Integer) input.readObject();

            logado.setIp(cliente.getInetAddress().getHostAddress());
            logado.setPorta(porta);

            //informacoes sao gravadas no objeto InformacoesCliente
            informacoes = new InformacoesCliente(arquivos, porta, cliente.getInetAddress().getHostAddress());

            //informacoes sao adicionadas na lista de informacoes do servidor central
            servidor.getInformacoesClientes().add(informacoes);

        } catch (IOException ex) {
            System.err.println("Cliente " + cliente.getInetAddress().getHostAddress() + " se desconectou.");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TratarCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Método responsável por efetuar o cadastro de usuários.
     */
    private void cadastro() throws IOException, ClassNotFoundException {
        System.out.println("Opção escolhida por " + cliente.getInetAddress().getHostAddress() + " foi cadastro");

        //recebe login
        String login = input.readObject().toString();
        //recebe senha
        String senha = input.readObject().toString();

        //verifica se usuario já existe
        ArrayList<Usuario> usuarios = servidor.getUsuarios();
        Iterator iterador = usuarios.iterator();
        while (iterador.hasNext()) {
            Usuario atual = (Usuario) iterador.next();
            if (atual.getLogin().equals(login)) {
                //informa que nao foi possivel cadastrar usuario
                output.writeObject("invalido");
                return;
            }
        }

        //cria novo usuario
        Usuario novo = new Usuario(login, senha);

        //adiciona usuario na lista de usuarios
        usuarios.add(novo);

        //informa que usuario foi cadastrado com sucesso
        output.writeObject("cadastrado");
    }

    /**
     * Método responsável por efetuar o login dos usuários.
     */
    private boolean login() throws IOException, ClassNotFoundException {
        System.out.println("Opção escolhida por " + cliente.getInetAddress().getHostAddress() + " foi logar.");

        //recebe login
        String login = input.readObject().toString();
        //recebe senha
        String senha = input.readObject().toString();

        //verifica se login é válido
        ArrayList<Usuario> usuarios = servidor.getUsuarios();
        for (Usuario atual : usuarios) {
            if (atual.getLogin().equals(login)) {
                //usuario correto
                if (atual.getSenha().equals(senha)) {
                    //senha correta
                    //veirifica se o usuário está online
                    if (atual.isOnline()) {
                        try {
                            //cria uma comunicacao com o serversocket do usuario logado
                            Socket autentica = new Socket(atual.getIp(), atual.getPorta());

                            ObjectOutputStream outputAutentica = new ObjectOutputStream(autentica.getOutputStream());
                            outputAutentica.writeObject("servidor");
                            ObjectInputStream inputAutentica = new ObjectInputStream(autentica.getInputStream());

                            String loginAutentica = (String) inputAutentica.readObject();

                            //se o login retornado pelo serversocket do usuario for igual ao digitado
                            if (loginAutentica.equals(login)) {
                                //informa que usuario já está logado
                                output.writeObject("online");
                                autentica.close();
                                return false;
                            } else {
                                //usuario nao está mais logado, perdeu a conexao por algum motivo
                                atual.setOnline(true);
                                logado = atual;
                                output.writeObject("logado");
                                autentica.close();
                                return true;
                            }

                        } catch (IOException ex) {
                            //caso nao consiga conectar com o serversocket é porque o usuario estava offline
                            atual.setOnline(true);
                            logado = atual;
                            output.writeObject("logado");
                            return true;
                        }
                    } else {
                        atual.setOnline(true);
                        logado = atual;
                        output.writeObject("logado");
                        return true;
                    }

                } else {
                    //informa que senha é inválida
                    output.writeObject("senha");
                    return false;
                }

            }
        }

        //informa que nenhum usuario foi encontrado
        output.writeObject("inexistente");
        return false;
    }

    /**
     * Método chamado quando o usuário já está logado. Responsável pela
     * comunicação do usuário já logado.
     */
    private void logado() throws IOException {

        //servidor recebe lista de arquivos compartilhados do cliente
        //e informacoes do servidor do cliente.
        receberInformacoes();

        //é enviado ao usuário a lista contendo o nome de todos os arquivos disponíveis para download.
        System.out.println("Usuário " + logado.getLogin() + " foi logado com sucesso.");
        //this.informacoesClientes = servidor.getInformacoesClientes();
        this.informacoesClientes = new ArrayList<>();
        this.informacoesClientes.addAll(servidor.getInformacoesClientes());
        this.informacoesClientes.remove(this.informacoes);
        output.writeObject(this.getListaArquivo());
        while (true) {
            System.out.println("Esperando opção do usuário " + logado.getLogin() + ".");

            try {
                //esperando opcao de menu do cliente
                String opcaoCliente = input.readObject().toString();
                System.out.println("Cliente escolheu isso: " + opcaoCliente);
                switch (opcaoCliente) {
                    //atualiza a lista de arquivos compartilhados no servidor
                    case "atualiza":
                        this.atualizar();
                        break;
                    //desloga o cliente do servidor e atribui a variavel online de Usuario como false.
                    case "deslogar":
                        this.deslogar();
                        return;
                }
            } catch (IOException ex) {
                //caso a conexao seja perdida o usuario é deslogado e seus arquivos saem do sistema.
                System.err.println("Cliente " + cliente.getInetAddress().getHostAddress() + " se desconectou.");
                servidor.getInformacoesClientes().remove(informacoes);
                return;
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(TratarCliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Método responsável por atualizar a lista de arquivos disponíveis e
     * envia-la ao cliente.
     *
     * @throws IOException
     * @throws ClassNotFoundException
     *
     * @see Servidor
     */
    private void atualizar() throws IOException, ClassNotFoundException {
        System.out.println("Usuário " + logado.getLogin() + " escolheu atualizar.");
        //lê a lista de arquivos atual do cliente
        ArrayList<Arquivo> lista = (ArrayList<Arquivo>) input.readObject();

        //atualiza a lista de arquivos desse cliente no servidor
        InformacoesCliente aux = this.servidor.getInformacoesClientes().get(this.servidor.getInformacoesClientes().indexOf(informacoes));
        aux.setNomeArquivos(lista);
        aux.setInfo(cliente.getInetAddress().getHostAddress(), this.porta);

        //atualiza a lista de todos os arquivos disponiveis para download no cliente
        this.informacoesClientes = new ArrayList<>();
        this.informacoesClientes.addAll(servidor.getInformacoesClientes());
        this.informacoesClientes.remove(this.informacoes);
        output.writeObject(this.getListaArquivo());

        //envia lista atualizada de arquivos disponiveis
        output.writeObject(this.getListaArquivo());
    }

    /**
     * Método que retorna a lista contendo todos os arquivos disponíveis para
     * download.
     *
     * @return
     *
     * @see Arquivo
     */
    private ArrayList<Arquivo> getListaArquivo() {
        ArrayList<Arquivo> lista = new ArrayList<>();
        Iterator it = informacoesClientes.iterator();
        while (it.hasNext()) {
            InformacoesCliente atual = (InformacoesCliente) it.next();
            lista.addAll(atual.getNomeArquivos());
        }
        return lista;
    }

    /**
     * Método que desloga o cliente do servidor.
     */
    private void deslogar() {
        servidor.getInformacoesClientes().remove(informacoes);
        logado.setOnline(false);
        System.out.println("Usuário " + logado.getLogin() + " se desconectou do servidor.");
    }

}
