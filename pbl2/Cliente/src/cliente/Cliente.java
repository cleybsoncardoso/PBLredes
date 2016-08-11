/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javafx.application.Platform.exit;

/**
 *
 * @author cleyb
 */
public class Cliente {

    private Socket cliente;
    private Scanner teclado;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new Cliente().executa();
    }

    public Cliente() {
        teclado = new Scanner(System.in);

        try {
            cliente = new Socket("127.0.0.1", 8080);
            output = new ObjectOutputStream(cliente.getOutputStream());
            input = new ObjectInputStream(cliente.getInputStream());
        } catch (IOException ex) {
            System.out.println("Servidor esta offline");
            System.exit(0);
        }
    }

    private void executa() {
        String navegacao = null;
        while (true) {
            System.out.println("\n\n\n________________________________________________");
            System.out.println("Bem vindo ao Sistema Lava Duto\n");
            System.out.println("1 - Cadastro");
            System.out.println("2 - Logar");
            System.out.println("3 - Sair");
            System.out.println("________________________________________________");
            navegacao = teclado.nextLine();
            switch (navegacao) {
                case "1":
                    cadastro();
                    break;

                case "2":
                    logar();
                    break;

            }
            if (navegacao.equals("3")) {
                break;
            }

        }
    }

    private void cadastro() {
        try {
            String dado = "";
            System.out.println("\n\n\n________________________________________________");
            output.writeObject("cadastro");
            do {//login
                System.out.println("Digite o login a ser cadastrado");
                dado = teclado.nextLine();
            } while (dado.equals(""));
            output.writeObject(dado);
            do {//senha
                System.out.println("Digite a senha a ser cadastrado");
                dado = teclado.nextLine();
            } while (dado.equals(""));
            output.writeObject(dado);
            //após enviar os dados de login e senha, o servidor informa se foi cadastrado ou não a conta
            if (input.readObject().toString().equals("cadastrado")) {
                System.out.println("\n\n\n________________________________________________");
                System.out.println("----------CADASTRO EFETUADO COM SUCESSO----------");
            } else {
                System.out.println("\n\n\n________________________________________________");
                System.err.println("CADASTRO NÃO FOI EFETUADO, TENTE NOVAMENTE");
            }
        } catch (IOException ex) {
            System.err.println("Servidor ficou offline");
            System.exit(0);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void logar() {
        try {
            String dado = "";
            System.out.println("\n\n\n________________________________________________");
            output.writeObject("logar");
            do {//login
                System.out.println("Login:");
                dado = teclado.nextLine();
            } while (dado.equals(""));
            output.writeObject(dado);
            do {//senha
                System.out.println("Senha:");
                dado = teclado.nextLine();
            } while (dado.equals(""));
            output.writeObject(dado);
            String resposta = input.readObject().toString();
            if (resposta.equals("online")) {//caso a conta ja esteja online
                System.out.println("\n\n\n________________________________________________");
                System.out.println("----------USUARIO JA ESTA LOGADO----------");
            } else if (resposta.equals("logado")) {//caso nao ocorra erro
                System.out.println("\n\n\n________________________________________________");
                System.out.println("----------LOGADO COM SUCESSO----------");
            }else if (resposta.equals("senha")) {//caso senha esteja incorreta
                System.out.println("\n\n\n________________________________________________");
                System.out.println("----------LOGADO COM SUCESSO----------");
            }else if (resposta.equals("inexistente")) {//caso conta nao exista
                System.out.println("\n\n\n________________________________________________");
                System.out.println("----------ESTA CONTA NAO EXISTE----------");
            }

        } catch (IOException ex) {
            System.err.println("Servidor ficou offline");
            System.exit(0);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
