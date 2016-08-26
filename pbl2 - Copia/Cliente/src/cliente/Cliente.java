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

    private Socket cliente; //socket criado para se conectar tanto no servidor principal, quanto no servesocket dos usuarios
    private Scanner teclado; //Scanner para obter as informações digitadas pelo usuario
    private ObjectInputStream input; //input do servesocket que o usuario estiver logado no momento
    private ObjectOutputStream output; //output do servesocket que o usuario estiver logado no momento
    private Servidor servidorCliente;
    private String ipPrincipal = "192.168.0.125"; //ip do servidor principal
    private ClienteOnline nomeCliente;

    /**
     * Construtor da classe cliente
     *
     * @param servidorCliente //classe que possue o ServeSocket
     * @param nomeCliente //nome do usuario no momento
     */
    public Cliente(Servidor servidorCliente, ClienteOnline nomeCliente) {
        this.nomeCliente = nomeCliente;
        this.servidorCliente = servidorCliente;

        teclado = new Scanner(System.in);
        try {
            cliente = new Socket(ipPrincipal, 8080);
            System.out.println("seu ip é " + this.cliente.getInetAddress().getHostAddress());
            this.conexaoNova(); //chama o metodo responsavel por atribuir os valores das variaveis "input" e "output" do servidor atual
            output.writeObject(this.nomeCliente.getNomeCLiente());/*ao conectar com o servidor principal, toda vez o usuario envia o nome do usuario, 
                                                                    para saber se o usuario ja estava logado ou não*/

        } catch (IOException ex) {//Caso ocorra um erro na comunicação
            System.out.println("Servidor esta offline");
            System.exit(0);
        }
    }

    /**
     * Metodo que obtem os nomes dos arquivos presentes dentro da pasta de
     * compartilhamento
     *
     * @return Lista com o nome dos arquivos presentes na pasta de
     * compartilhamento
     */
    private ArrayList<Arquivo> arquivoPessoal() {
        System.out.println("entrou");
        ArrayList<Arquivo> repassarArquivos = new ArrayList(); //lista com o nome dos arquivos que vão ser compartilhados
        List endereco = new ArrayList();//lista com o nome do endereço atual
        endereco.add("programa lava duto upload");//pasta de compartilhamento
        ArrayList<Arquivo> arquivoPessoalLista = precorrePastas(endereco, repassarArquivos);//função que retorna o nome dos arquivos ques estão dentro de outras pastas

        System.out.println("\nSeus arquvios compartilhados:");

        for (Arquivo fileEntry : arquivoPessoalLista) {//mostra o nome dos arquivos que o usuario está compartilhando
            System.out.println("-> " + fileEntry.getNome());
        }
        return arquivoPessoalLista;
    }

    /**
     * função recursiva, que percorre cada elemento presente na pasta
     * compartilhada, um por um, se for uma pasta ele entra, adiciona todos os
     * arquivos presentes dentro dela, e volta, e assim é feito com todas
     * elementos dentro da pasta compartilhada
     *
     * @param endereco endereço da pasta atual
     * @param repassarArquivos lista atualizada com o nome dos arquivos
     * @return
     */
    private ArrayList<Arquivo> precorrePastas(List endereco, ArrayList<Arquivo> repassarArquivos) {
        Iterator it = endereco.iterator();//iterador que percorre a lista de endereços, para ter o endereço atual
        String enderecoAtual = "";
        while (it.hasNext()) {//passando o endereço da lista com o local atual, para a variavel
            enderecoAtual = enderecoAtual + (String) it.next();
        }
        File local = new File(enderecoAtual);
        try {
            for (File fileEntry : local.listFiles()) {//informa quais arquivos e pastas estão no diretorio atual
                if (fileEntry.isDirectory()) {//recursividade enquanto encontra pastas novas
                    endereco.add("/" + fileEntry.getName());
                    precorrePastas(endereco, repassarArquivos);
                    endereco.remove("/" + fileEntry.getName());
                } else {
                    repassarArquivos.add(new Arquivo(fileEntry.getName(), fileEntry.length(), enderecoAtual));//caso seja uma arquivo, ele é inserido na lista
                }

            }
        } catch (NullPointerException e) {//caso a pasta de compartilhamento não exista, o programa cria
            System.out.println("criando pasta de compartilhamento");
            local.mkdir();
        }
        return repassarArquivos;
    }

    /**
     * Método para fazer o cadastro do cliente
     */
    private void cadastro() {
        try {
            String dado = "";
            System.out.println("\n\n________________________________________________");
            output.writeObject("cadastro");
            do {//login
                System.out.println("Digite o login a ser cadastrado");
                dado = teclado.nextLine();
            } while (dado.equals(""));//checa se o login é válido
            output.writeObject(dado); //envia o login para o servidor
            do {//senha
                System.out.println("Digite a senha a ser cadastrado");
                dado = teclado.nextLine();
            } while (dado.equals(""));//verifica se a senha é valida
            output.writeObject(dado);//envia a senha para o servidor
            //após enviar os dados de login e senha, o servidor informa se a conta foi cadastrada ou não
            if (input.readObject().toString().equals("cadastrado")) {
                System.out.println("\n\n________________________________________________");
                System.out.println("----------CADASTRO EFETUADO COM SUCESSO----------");
            } else {
                System.out.println("\n\n________________________________________________");
                System.err.println("CADASTRO NÃO FOI EFETUADO, TENTE NOVAMENTE");
            }
        } catch (IOException ex) {//caso perca a conexão com o servidor
            System.err.println("Servidor ficou offline");
            System.exit(0);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Método responsavel pelo login do usuario
     */
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

    /**
     * Após o login ser efetuado com sucesso, metodo reponsavel por apresentar
     * os arquivos disponiveis
     */
    private void logado() {

        try {
            //enviado para o servidor os arquivos que tem no computador ao fazer o login
            output.writeObject(this.arquivoPessoal());
            output.writeObject(this.servidorCliente.getServidorCliente().getLocalPort());//informa a porta do serveSocket do cliente
        } catch (IOException ex) {//erro na comunicação
            System.err.println("Servidor ficou offline");
            System.exit(0);
        }

        //recebe lista de arquivo que estão disponiveis no servidor
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

        //o programa entra em loop, apos cada operação feita, o software mostra novamente os arquivos, e isso se repete ate o cliente deslogar
        while (true) {
            int i = 0, indexArquivo = -1;
            System.out.println("\n\nArquivos disponiveis:");
            System.out.println("[ID] NOME \tTAMANHO\n");
            //mostra o nome e o tamanho de cada arquivo para fazer o download
            for (Arquivo nome : arquivosCliente) {
                System.out.println("[" + i + "]" + " " + nome.getNome() + "\t (" + nome.getTamanho() + " Kb)");
                i++;
            }
            System.out.println("__________________________________________________");
            navegar = teclado.nextLine();

            //caso seja um download, é o unico caso especial que possue 2 palavras
            if (navegar.contains("download")) {
                String[] comandos = new String[2];
                comandos = navegar.split(" ");//o software separa a palavra download e o ID do arquivo referente
                navegar = comandos[0];
                try {
                    indexArquivo = Integer.parseInt(comandos[1]);//transforma o ID digitado, em um numero inteiro para usar como index da lista
                } catch (NumberFormatException e) {//caso não seja um numero
                    System.out.println("Digite um numero");
                } catch (ArrayIndexOutOfBoundsException e) {//caso não tenha nenhum numero
                    System.out.println("Digite um ID valido");;
                }
            }

            //caso o cliente deslogue, o sistema informa ao servidor, coloca o nome do usuario como "deslogado" para futuras utilizações com o servidor e sae do loop do nome dos arquivos
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
                //caso o cliente digite help, é listado os comando disponiveis para utilizar no sistema
                case "help":
                    System.out.println("atualizar: Atualiza a sua lista de arquivos, caso faca alguma alteracao");
                    System.out.println("download <ID do arquivo>: Baixa o arquivo referente aquele numero");
                    System.out.println("deslogar: Sai da conta");
                    teclado.nextLine();//aguarda o cliente digitar alguma coisa, para voltar ao loop
                    break;

                case "atualizar":
                    //atualiza a lista dos arquivos disponiveis e seus arquivos da pasta upload
                    try {
                        output.writeObject("atualiza");//envia para o servidor, que  o usuario quer atualizar
                        System.out.println("Solicitando do servidor...");
                        output.writeObject(this.arquivoPessoal());//envia uma lista atualizada dos arquivos atuais presentes na pasta compartilhada
                        System.out.println("ATUALIZANDO SERVIDOR");
                        arquivosCliente = (ArrayList<Arquivo>) input.readObject();//recebe a lista de arquivos atualizada do servidor, de arquivos para fazer o download
                        System.out.println("\n\nSERVIDOR ATUALIZADO!!!!!!!\n\n");
                        break;
                    } catch (IOException ex) {
                        System.err.println("Servidor ficou offline");
                        System.exit(0);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                    }
                case "download":
                    //opção de download
                    try {
                        Arquivo baixando = arquivosCliente.get(indexArquivo);//obtem o arquivo com o ID escolhido pelo usuario
                        System.out.println("Fazendo conexão com o clienteServidor " + baixando.getIp());
                        System.out.println("Arquivo selecionado: " + baixando.getNome() + " (" + baixando.getTamanho() + " Kb)");
                        //envia o arquivo solicitado para fazer o download, a porta e o ip do serveSocket que possue o arquivo; Para o metodo fazer o download
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
        //menu principal
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

    /**
     * Esta função basicamente faz todo o trabalho de fazer o download, ela faz
     * o usuario se desconectar do servidor principal e se conectar com o
     * serveSocket do cliente que possue o arquivo, após terminar o download,
     * ela faz o usuario se desconectar do serveSocket do cliente que possue o
     * arquivo e retorna a conexão com o servidor principal.
     *
     * @param ip ip do serveSocket
     * @param porta porta do ServeSocket
     * @param baixando referencias do arquivo que quer baixar
     */
    private void download(String ip, int porta, Arquivo baixando) {
        try {
            cliente.close();//desconecta do servidor principal
            cliente = new Socket(ip, porta);//se conecta com o serveSocket do cliente que possue o arquivo
            this.conexaoNova();//coloca os valores do output e input da nova conexão, referentes ao serveSocket do cliente
            System.out.println("Conectou com o servidor Download");
            output.writeObject("cliente");//envia para o serveSocket do cliente, dizendo que ele é um cliente(so quer baixar).
            output.writeObject(baixando);//envia as referencias do arquivo que quer baixar

        } catch (IOException ex) {
            System.err.println("Download nao concluido");
        }
        try {
            //espera a resposta o serveSocket do cliente, falando se o arquivo ainda existe ou se ja foi deletado
            String existencia = input.readObject().toString();
            //se existir, o cliente faz o download
            if (existencia.equals("sim")) {
                FileOutputStream fos = null;
                try {
                    InputStream in = cliente.getInputStream();
                    long tamanho = baixando.getTamanho();
                    long tamanhoParcial = 0;

                    //verifica se a pasta de download existe, se nao existe o software cria
                    File testePasta = new File("./programa lava duto download");
                    if (!testePasta.exists()) {
                        System.out.println("criando pasta de download");
                        testePasta.mkdir();
                    }
                    //cria o file para iniciar o download
                    fos = new FileOutputStream("./programa lava duto download/" + baixando.getNome());
                    int tamanhoBuffer = 1024;
                    byte[] buffer = new byte[tamanhoBuffer];
                    int lidos;
                    
                    System.out.println("Iniciando Download ...");
                    while (tamanhoParcial < tamanho) {
                        System.out.println((tamanhoParcial * 100) / tamanho + " %");//porcentagem do processo
                        lidos = in.read(buffer, 0, tamanhoBuffer);
                        tamanhoParcial += lidos;
                        fos.write(buffer, 0, lidos);
                    }
                    System.out.println("Download concluido");

                    fos.flush();
                    fos.close();

                } catch (FileNotFoundException ex) {
                    System.out.println("Diretório não enconstrado.");
                } catch (IOException ex) {//caso ocorra um problema na comunicação
                    try {
                        fos.close();
                        File arq = new File("./programa lava duto" + baixando.getNome());
                        arq.delete();//apaga o file corrompido
                        System.out.println("Erro de comunicação");
                    } catch (IOException ex1) {
                        //erro impossivel de acontecer
                        System.out.println("Erro inesperado");
                    }

                } catch (NullPointerException ex) {
                    System.out.println("Usuario se desconectou");
                }
                
                //voltando para o servidor principal
                try {
                    cliente.close();// ao finalizar download
                    System.out.println("Reestabelecando conexao com o servidor principal");
                    cliente = new Socket(ipPrincipal, 8080);//retorna ao servidor principal
                    this.conexaoNova();//coloca os valores do output e input da nova conexão, referentes ao servidor principal
                    output.writeObject(this.nomeCliente.getNomeCLiente());//envia para o servidor principal o nome do usuario, para reestabelecer a conexao de onde parou
                    try {
                        //enviado para o servidor os arquivos que tem no computador ao fazer o login e informando a porta do servido cliente
                        output.writeObject(this.arquivoPessoal());//envia para o servidor os arquivos que estão na pasta compartilhada para download, para reestabelecer a conexão
                        output.writeObject(this.servidorCliente.getServidorCliente().getLocalPort());//envia a porta do seu ServeSocket
                    } catch (IOException ex) {
                        System.err.println("Servidor ficou offline");
                        System.exit(0);
                    }
                } catch (IOException ex) {
                    System.out.println("Conexao com usuario de download foi perdida");
                } catch (NullPointerException e) {
                    System.out.println("Cliente de download se desconectou");
                }
            } else {
                System.out.println("Arquivo deletado pelo usuario fornecedor");
            }
        } catch (IOException ex) {
            System.out.println("erro em comunicacao");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Metodo criado com o intuito de atualizar sempre que efetuada uma nova
     * conexão os valores das variaveis de comunicação "input" e "output".
     */
    private void conexaoNova() {
        try {
            output = new ObjectOutputStream(cliente.getOutputStream());
            input = new ObjectInputStream(cliente.getInputStream());
        } catch (IOException ex) {
            System.out.println("conexao perdida");
        }

    }

}
