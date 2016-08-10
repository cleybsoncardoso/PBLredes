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
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

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
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
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

            }
            if (navegacao.equals("3")) {
                break;
            }

        }
    }

    private void cadastro() {
        try {
            System.out.println("\n\n\n________________________________________________");
            output.writeObject("cadastro");
            System.out.println("________________________________________________");
            System.out.println("Digite o login a ser cadastrado");
            output.writeObject(teclado.nextLine());
            System.out.println("Digite a senha a ser cadastrado");
            System.out.println("________________________________________________");
            output.writeObject(teclado.nextLine());
            if (input.readObject().toString().equals("cadastrado")) {
                System.out.println("\n\n\n________________________________________________");
                System.out.println("----------CADASTRO EFETUADO COM SUCESSO----------");
            } else {
                System.out.println("\n\n\n________________________________________________");
                System.err.println("CADASTRO NÃO FOI EFETUADO, TENTE NOVAMENTE");
            }
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
