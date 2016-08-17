/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
 * @author cleyb
 */
class TratarCliente implements Runnable {

    private Servidor servidor;
    private Socket cliente;
    private Usuario logado;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private InformacoesCliente informacoes;
    private ArrayList<InformacoesCliente> informacoesClientes;

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

    @Override
    public void run() {

        while (true) {

            System.out.println("Esperando opção do cliente " + cliente.getInetAddress().getHostAddress() + ".");

            try {
                //esperando opcao de menu do cliente
                String opcaoCliente = input.readObject().toString();
                switch (opcaoCliente) {
                    case "cadastro":
                        this.cadastro();
                        servidor.salvarUsuarios();
                        break;
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
                if (logado != null) {
                    logado.setOnline(false);
                }
                return;
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(TratarCliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Método responsável por receber as informações do servidor referente ao
     * cliente.
     *
     * @see InformacoesCliente.
     */
    private void receberInformacoes() {
        try {
            //cliente envia lista de arquivo do seu repositório com porta do servidor
            System.out.println("Recebendo informacoes do servidor " + cliente.getInetAddress().getHostAddress() + ".");
            ArrayList arquivos = (ArrayList<Arquivo>) input.readObject();

            //porta do servidor do cliente
            int porta = (Integer) input.readObject();

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
        Iterator iterador = usuarios.iterator();
        while (iterador.hasNext()) {
            Usuario atual = (Usuario) iterador.next();
            if (atual.getLogin().equals(login)) {
                //usuario correto
                if (atual.getSenha().equals(senha)) {
                    //senha correta
                    if (atual.isOnline()) {
                        //informa que usuario já está logado
                        output.writeObject("online");
                        return false;
                    } else {
                        //informa que usuario foi logado com sucesso
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
        this.informacoesClientes = servidor.getInformacoesClientes();
        output.writeObject(this.getListaArquivo());

        while (true) {
            System.out.println("Esperando opção do usuário " + logado.getLogin() + ".");

            try {
                //esperando opcao de menu do cliente
                String opcaoCliente = input.readObject().toString();
                switch (opcaoCliente) {
                    case "atualiza":
                        this.atualizar();
                        break;
                    case "logar":
                        break;
                    case "download":
                        //verificarArquivo();
                        break;
                    case "deslogar":
                        this.deslogar();
                        return;
                }
            } catch (IOException ex) {
                //caso a conexao seja perdida o usuario é deslogado e seus arquivos saem do sistema.
                System.err.println("Cliente " + cliente.getInetAddress().getHostAddress() + " se desconectou.");
                servidor.getInformacoesClientes().remove(informacoes);
                logado.setOnline(false);
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
        //atualiza a lista de arquivos desse cliente
        this.servidor.getInformacoesClientes().get(this.servidor.getInformacoesClientes().indexOf(informacoes)).setNomeArquivos(lista);
        //atualiza a lista de todos os arquivos disponiveis
        this.informacoesClientes = servidor.getInformacoesClientes();
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

    private void deslogar() {
        servidor.getInformacoesClientes().remove(informacoes);
        logado.setOnline(false);
        System.out.println("Usuário " + logado.getLogin() + " se desconectou do servidor.");
    }

}
