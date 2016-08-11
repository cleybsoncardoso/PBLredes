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
    private ObjectInputStream input;
    private ObjectOutputStream output;

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
        System.out.println("esperando opção do cliente " + cliente.getInetAddress().getHostAddress());
        String opcaoCliente = null;
        try {
            opcaoCliente = input.readObject().toString();
            switch (opcaoCliente) {
                case "cadastro":
                    cadastro();
            }
        } catch (IOException ex) {
            Logger.getLogger(TratarCliente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TratarCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

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

}
