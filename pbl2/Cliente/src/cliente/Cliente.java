/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javafx.application.Platform.exit;
import util.Arquivo;
import util.InformacoesCliente;

/**
 *
 * @author cleyb
 */
public class Cliente implements Runnable {

    private Socket cliente;
    private Scanner teclado;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Servidor servidorCliente;

    public Cliente(Servidor servidorCliente) {
        this.servidorCliente = servidorCliente;

        teclado = new Scanner(System.in);
        try {
            cliente = new Socket("25.15.175.182", 8080);
            System.out.println("seu ip é " + this.cliente.getInetAddress().getHostAddress());
            output = new ObjectOutputStream(cliente.getOutputStream());
            input = new ObjectInputStream(cliente.getInputStream());

        } catch (IOException ex) {
            System.out.println("Servidor esta offline");
            System.exit(0);
        }
    }

//    private ArrayList<Arquivo> arquivoPessoal() {
//        ArrayList<Arquivo> repassarArquivos = new ArrayList();
//        List endereco = new ArrayList();
//        endereco.add("programa lava duto");
//        Iterator it = endereco.iterator();//iterador que percorre a lista de endereços, para ter o endereço atual
//        String enderecoAtual = "";
//        while (it.hasNext()) {//passando o endereço da lista com o local atual, para a variavel
//            enderecoAtual = enderecoAtual + (String) it.next();
//        }
//        File local = new File(enderecoAtual);
//        for (File fileEntry : local.listFiles()) {//informa quais arquivos e pastas estão no diretorio atual
//            //   repassarArquivos.add(new Arquivo(fileEntry.getName(), fileEntry.length()));
//
//        }
//        return repassarArquivos;
//    }
    private ArrayList<Arquivo> arquivoPessoal() {
        System.out.println("entrou");
        ArrayList<Arquivo> repassarArquivos = new ArrayList();
        List endereco = new ArrayList();
        endereco.add("programa lava duto");
        ArrayList<Arquivo> arquivoPessoalLista = precorrePastas(endereco, repassarArquivos);
        for (Arquivo fileEntry : arquivoPessoalLista) {
            System.out.println(fileEntry.getNome());
        }
        return arquivoPessoalLista;
    }

    private ArrayList<Arquivo> precorrePastas(List endereco, ArrayList<Arquivo> repassarArquivos) {
        Iterator it = endereco.iterator();//iterador que percorre a lista de endereços, para ter o endereço atual
        String enderecoAtual = "";
        while (it.hasNext()) {//passando o endereço da lista com o local atual, para a variavel
            enderecoAtual = enderecoAtual + (String) it.next();
        }
        File local = new File(enderecoAtual);
        for (File fileEntry : local.listFiles()) {//informa quais arquivos e pastas estão no diretorio atual
            if (fileEntry.isDirectory()) {
                endereco.add("/" + fileEntry.getName());
                precorrePastas(endereco, repassarArquivos);
                endereco.remove("/" + fileEntry.getName());
            } else {
                repassarArquivos.add(new Arquivo(fileEntry.getName(), fileEntry.length(), enderecoAtual));
            }
        }
        return repassarArquivos;
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
                logado();
            } else if (resposta.equals("senha")) {//caso senha esteja incorreta
                System.out.println("\n\n\n________________________________________________");
                System.out.println("----------SENHA INCORRETA----------");
            } else if (resposta.equals("inexistente")) {//caso conta nao exista
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

    private void logado() {

        try {
            //enviado para o servidor os arquivos que tem no computador ao fazer o login e informando a porta do servido cliente
            output.writeObject(this.arquivoPessoal());
            output.writeObject(this.servidorCliente.getServidorCliente().getLocalPort());
        } catch (IOException ex) {
            System.err.println("Servidor ficou offline");
            System.exit(0);
        }

        String navegar = "";
        ArrayList<Arquivo> arquivosCliente = null;
        try {
            arquivosCliente = (ArrayList<Arquivo>) input.readObject();
        } catch (IOException ex) {
            System.err.println("Servidor ficou offline");
            System.exit(0);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }

        while (true) {
            int i = 0, indexArquivo = -1;
            System.out.println("\n\n\n\tArquivos disponiveis:\n");
            for (Arquivo nome : arquivosCliente) {
                System.out.println("[" + i + "]" + " " + nome.getNome() + " (" + nome.getTamanho() + " Kb)");
                i++;
            }
            System.out.println("__________________________________________________");
            navegar = teclado.nextLine();
            if (navegar.contains("download")) {
                String[] comandos = new String[2];
                comandos = navegar.split(" ");
                navegar = comandos[0];
                indexArquivo = Integer.parseInt(comandos[1]);
            }
            if (navegar.equals("deslogar")) {
                try {
                    output.writeObject("deslogar");
                } catch (IOException ex) {
                    System.out.println("Servidor ficou offline");
                }
                break;
            }
            switch (navegar) {
                case "help":
                    System.out.println("atualizar: Atualiza a sua lista de arquivos, caso faca alguma alteracao");
                    System.out.println("download <numero do arquivo>: Baixa o arquivo referente aquele numero");
                    System.out.println("deslogar: Sai da conta");
                    teclado.nextLine();
                    break;

                case "atualizar":
                    try {
                        output.writeObject("atualiza");
                        System.out.println("Solicitando do servidor...");
                        output.writeObject(this.arquivoPessoal());
                        System.out.println("ATUALIZANDO SERVIDOR");
                        arquivosCliente = (ArrayList<Arquivo>) input.readObject();
                        System.out.println("\n\nSERVIDOR ATUALIZADO!!!!!!!\n\n");
                        break;
                    } catch (IOException ex) {
                        System.err.println("Servidor ficou offline");
                        System.exit(0);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                    }
                case "download":
                    Arquivo baixando = arquivosCliente.get(indexArquivo);
                    System.out.println("Fazendo conexão com o clienteServidor " + baixando.getIp());
                    System.out.println("Arquivo selecionado: " + baixando.getNome() + " (" + baixando.getTamanho() + " Kb)");
                    Download downloadArquivo = new Download(baixando.getIp(), baixando.getPorta(), baixando);
                    Thread t = new Thread(downloadArquivo);
                    t.start();
                    try {
                        output.writeObject("download");
                        ArrayList<InformacoesCliente> clientesServidores = (ArrayList<InformacoesCliente>) input.readObject();
                    } catch (IOException ex) {
                        System.err.println("Servidor ficou offline");
                        System.exit(0);
                    } catch (ClassNotFoundException ex) {
                        System.err.println("nao existe");
                    }
                    break;
                case "sair":
                    System.exit(0);
                    break;
            }
        }
    }

    @Override
    public void run() {
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
                case "3":
                    System.exit(0);
                    break;

            }

        }
    }

}
//percorrer pastas
