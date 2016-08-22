/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
    private String ipPrincipal = "25.6.169.148";
    ClienteOnline nomeCliente;

    public Cliente(Servidor servidorCliente, ClienteOnline nomeCliente) {
        this.nomeCliente = nomeCliente;
        this.servidorCliente = servidorCliente;

        teclado = new Scanner(System.in);
        try {
            cliente = new Socket(ipPrincipal, 8080);
            System.out.println("seu ip é " + this.cliente.getInetAddress().getHostAddress());
            output = new ObjectOutputStream(cliente.getOutputStream());
            input = new ObjectInputStream(cliente.getInputStream());
            output.writeObject(this.nomeCliente.getNomeCLiente());

        } catch (IOException ex) {
            System.out.println("Servidor esta offline");
            System.exit(0);
        }
    }

    private ArrayList<Arquivo> arquivoPessoal() {
        System.out.println("entrou");
        ArrayList<Arquivo> repassarArquivos = new ArrayList();
        List endereco = new ArrayList();
        endereco.add("programa lava duto upload");
        ArrayList<Arquivo> arquivoPessoalLista = precorrePastas(endereco, repassarArquivos);

        System.out.println("\nSeus arquvios compartilhados:");

        for (Arquivo fileEntry : arquivoPessoalLista) {
            System.out.println("-> " + fileEntry.getNome());
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
        try {
            for (File fileEntry : local.listFiles()) {//informa quais arquivos e pastas estão no diretorio atual
                if (fileEntry.isDirectory()) {
                    endereco.add("/" + fileEntry.getName());
                    precorrePastas(endereco, repassarArquivos);
                    endereco.remove("/" + fileEntry.getName());
                } else {
                    repassarArquivos.add(new Arquivo(fileEntry.getName(), fileEntry.length(), enderecoAtual));
                }

            }
        } catch (NullPointerException e) {
            System.out.println("criando pasta de compartilhamento");
            local.mkdir();
        }
        return repassarArquivos;
    }

    private void cadastro() {
        try {
            String dado = "";
            System.out.println("\n\n________________________________________________");
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
                System.out.println("\n\n________________________________________________");
                System.out.println("----------CADASTRO EFETUADO COM SUCESSO----------");
            } else {
                System.out.println("\n\n________________________________________________");
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
            String dadoLogin = "";
            String dadoSenha = "";
            System.out.println("\n\n________________________________________________");
            output.writeObject("logar");
            do {//login
                System.out.println("Login:");
                dadoLogin = teclado.nextLine();
            } while (dadoLogin.equals(""));
            output.writeObject(dadoLogin);
            do {//senha
                System.out.println("Senha:");
                dadoSenha = teclado.nextLine();
            } while (dadoSenha.equals(""));
            output.writeObject(dadoSenha);
            String resposta = input.readObject().toString();
            if (resposta.equals("online")) {//caso a conta ja esteja online
                System.out.println("\n\n________________________________________________");
                System.out.println("----------USUARIO JA ESTA LOGADO----------");
            } else if (resposta.equals("logado")) {//caso nao ocorra erro
                System.out.println("\n\n________________________________________________");
                System.out.println("----------LOGADO COM SUCESSO----------");
                nomeCliente.setNomeCLiente(dadoLogin);
                logado();
            } else if (resposta.equals("senha")) {//caso senha esteja incorreta
                System.out.println("\n\n________________________________________________");
                System.out.println("----------SENHA INCORRETA----------");
            } else if (resposta.equals("inexistente")) {//caso conta nao exista
                System.out.println("\n\n________________________________________________");
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
            System.out.println("\n\nArquivos disponiveis:");
            System.out.println("[ID] NOME \tTAMANHO\n");
            for (Arquivo nome : arquivosCliente) {
                System.out.println("[" + i + "]" + " " + nome.getNome() + "\t (" + nome.getTamanho() + " Kb)");
                i++;
            }
            System.out.println("__________________________________________________");
            navegar = teclado.nextLine();
            if (navegar.contains("download")) {
                String[] comandos = new String[2];
                comandos = navegar.split(" ");
                navegar = comandos[0];
                try {
                    indexArquivo = Integer.parseInt(comandos[1]);
                } catch (NumberFormatException e) {
                    System.out.println("Digite um numero");
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Digite um ID valido");;
                }
            }
            if (navegar.equals("deslogar")) {
                try {
                    output.writeObject("deslogar");
                    nomeCliente.setNomeCLiente("deslogado");
                } catch (IOException ex) {
                    System.out.println("Servidor ficou offline");
                }
                break;
            }
            switch (navegar) {
                case "help":
                    System.out.println("atualizar: Atualiza a sua lista de arquivos, caso faca alguma alteracao");
                    System.out.println("download <ID do arquivo>: Baixa o arquivo referente aquele numero");
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
                    try {
                        Arquivo baixando = arquivosCliente.get(indexArquivo);
                        System.out.println("Fazendo conexão com o clienteServidor " + baixando.getIp());
                        System.out.println("Arquivo selecionado: " + baixando.getNome() + " (" + baixando.getTamanho() + " Kb)");
                        this.download(baixando.getIp(), baixando.getPorta(), baixando);

                    } catch (ArrayIndexOutOfBoundsException e) {
                        System.out.println("Digite um ID existente ");
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println("Digite um ID existente ");
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

    private void download(String ip, int porta, Arquivo baixando) {
        try {
            cliente.close();
            cliente = new Socket(ip, porta);
            System.out.println("Conectou com o servidor Download");
            ObjectOutputStream output = new ObjectOutputStream(cliente.getOutputStream());
            output.writeObject(baixando);

        } catch (IOException ex) {
            System.err.println("Download nao concluido");
        }

        FileOutputStream fos = null;
        try {
            InputStream in = cliente.getInputStream();
            long tamanho = baixando.getTamanho();
            long tamanhoParcial = 0;

            File testePasta = new File("./programa lava duto download");
            if (!testePasta.exists()) {
                System.out.println("criando pasta de download");
                testePasta.mkdir();
            }
            fos = new FileOutputStream("./programa lava duto download/" + baixando.getNome());
            int tamanhoBuffer = 1024;
            byte[] buffer = new byte[tamanhoBuffer];
            int lidos;

            System.out.println("Recebendo.");
            while (tamanhoParcial < tamanho) {
                lidos = in.read(buffer, 0, tamanhoBuffer);
                tamanhoParcial += lidos;
                fos.write(buffer, 0, lidos);
            }
            System.out.println("Recebido.");

            fos.flush();
            fos.close();

        } catch (FileNotFoundException ex) {
            System.out.println("Diretório não enconstrado.");
        } catch (IOException ex) {
            try {
                fos.close();
                File arq = new File("./programa lava duto" + baixando.getNome());
                arq.delete();
                System.out.println("Erro de comunicação");
            } catch (IOException ex1) {
                //É lógicamente impossível chegar aqui.
                System.out.println("Erro inesperado");
            }

        } catch (NullPointerException ex) {
            System.out.println("Usuario se desconectou");
        }
        try {
            cliente.close();
            cliente = new Socket(ipPrincipal, 8080);
            output = new ObjectOutputStream(cliente.getOutputStream());
            input = new ObjectInputStream(cliente.getInputStream());
            output.writeObject(this.nomeCliente.getNomeCLiente());
            try {
                //enviado para o servidor os arquivos que tem no computador ao fazer o login e informando a porta do servido cliente
                output.writeObject(this.arquivoPessoal());
                output.writeObject(this.servidorCliente.getServidorCliente().getLocalPort());
            } catch (IOException ex) {
                System.err.println("Servidor ficou offline");
                System.exit(0);
            }
        } catch (IOException ex) {
            System.out.println("Conexao com usuario de download foi perdida");
        } catch (NullPointerException e) {
            System.out.println("Cliente de download se desconectou");
        }
    }

}
