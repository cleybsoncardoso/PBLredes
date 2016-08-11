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
    private ArrayList<String> arquivos;

    public TratarCliente(Servidor servidor, Socket cliente) {
        arquivos = new ArrayList();
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

        try {
            arquivos = (ArrayList<String>) input.readObject();
        } catch (IOException ex) {
            Logger.getLogger(TratarCliente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TratarCliente.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (String nome : arquivos) {
            System.out.println(nome);
        }

        while (true) {

            System.out.println("esperando opção do cliente " + cliente.getInetAddress().getHostAddress());
            String opcaoCliente = null;
            try {
                opcaoCliente = input.readObject().toString();
                switch (opcaoCliente) {
                    case "cadastro":
                        this.cadastro();
                        break;
                    case "logar":
                        this.login();
                        this.logado();
                        break;

                }
            } catch (IOException ex) {
                System.err.println("Cliente " + cliente.getInetAddress().getHostAddress() + " se desconectou.");
                logado.setOnline(false);
                return;
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(TratarCliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Método responsável por efetuar o cadastro de usuários.
     */
    private void cadastro() throws IOException, ClassNotFoundException {
        System.out.println("Opcao escolhida por " + cliente + " foi cadastro");

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
    private void login() throws IOException, ClassNotFoundException {
        System.out.println("Opcao escolhida por " + cliente + " foi cadastro");

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
                        return;
                    } else {
                        //informa que usuario foi logado com sucesso
                        atual.setOnline(true);
                        logado = atual;
                        output.writeObject("logado");
                        return;
                    }
                } else {
                    //informa que senha é inválida
                    output.writeObject("senha");
                    return;
                }

            }
        }

        //informa que nenhum usuario foi encontrado
        output.writeObject("inexistente");
    }

    /**
     * Método chamado quando o usuário já está logado. Responsável pela
     * comunicação do usuário já logado.
     */
    private void logado() {
        try {
            output.writeObject(arquivos);
        } catch (IOException ex) {
            Logger.getLogger(TratarCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
