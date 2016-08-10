/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorftp;

import java.awt.HeadlessException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import static java.lang.Thread.sleep;
import java.net.Socket;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cleybson e lucas
 */
class TrataCliente implements Runnable, Serializable {
    
    private InputStream teclado; //variavel responsavel por receber as strings enviados pelos clientes
    private OutputStream saida; //veriavel responsavel por enviar as strings para os clientes
    private Servidor servidor; //instancia do servidor
    private Socket cliente; //instancia do cliente que se conectou ao servidor
    private Usuario logado = null; //variavel que guarda os dados do usuario ao logar na conta

    public TrataCliente(Socket cliente, Servidor servidor) throws IOException {
        
        this.cliente = cliente;
        this.teclado = cliente.getInputStream();//pega a input do cliente na rede
        this.saida = cliente.getOutputStream();//pega o output do cliente na rede
        this.servidor = servidor;
        
    }
  
    @Override
    public void run() {
        //this.especiais();
        String navegacao = null;//string para receber os dados do scanner, que iria receber todos os dados que o cliente enviar para o servidor
        Scanner getTeclado = new Scanner(teclado);//recebe todos os dados que o cliente envia ao servidor, através do inputStream
        PrintStream getSaida = new PrintStream(saida);//envia os dados para o cliente, em forma de um objeto, através do outputStream
        //menu dos usuarios nao logados no sistema
        while (true) {
            getSaida.println("-------------------Lava Duto-------------------");
            getSaida.println("Digite a opcao desejada:\n" + "1 - cadastro\n" + "2 - login\n" + "3 - sair");
            getSaida.println("-----------------------------------------------");
            getSaida.println("EOT");
            /*por ter problemas ao imprimir na tela do cliente um objeto como o acima, em que existem quebras de linha
                                        ao longo do objeto, no momento de leitura dos dados que foram recebidos, o programa so lia ate a primeira 
                                        quebra de linha e passava para a proxima instrução (enviar para o servidor), e nisso acarretava no problema
                                        em que o codigo perdia a sincronia e ficava travado, os 2 no modo de envio, e nenhum aguardado receber dados
                                        então foi criado o mecanismo de avisar ao cliente que o servidor terminou de enviar todos os dados e ele pode 
                                        prosseguir no codigo. Por isso a sigla EOT(end of transmission).*/
            try {
                navegacao = getTeclado.nextLine();//o scanner lê a linha e passa para a variavel navegação
            } catch (Exception ex) {//erro caso o cliente se desconecta
                return;
            }

            //analiza se a entrada de dados do cliente, é valida. Enquanto for invalida, ficará nesse loop
            while (!navegacao.equals("1") && !navegacao.equals("2") && !navegacao.equals("3")) {
                getSaida.print("-----------------------------------------------\n");
                getSaida.print("Opção invalida\n");
                getSaida.print("Digite a opcao desejada:\n" + "1 - cadastro\n" + "2 - login\n" + "3 - sair\n");
                getSaida.print("-----------------------------------------------\n");
                getSaida.println("EOT");
                try {
                    navegacao = getTeclado.nextLine();//o scanner lê a linha e passa para a variavel navegação
                } catch (Exception ex) {
                    System.out.println("error: " + ex.getMessage());
                    break;
                }
            }
            //quando for digitado uma opção valida, o cliente será direcionado a sua respectiva opção escolhida
            switch (navegacao) {
                case "1":
                    this.cadastro(getTeclado, getSaida);
                    break;
                
                case "2":
                    this.logar(getTeclado, getSaida);
                    break;
                
                case "3":
                    this.servidor.write();
                    try {
                        this.cliente.close();
                        System.out.println("\n" + cliente.getInetAddress().getHostAddress() + " se desconectou do servidor");
                    } catch (IOException ex) {
                        System.out.println("error: " + ex.getMessage());
                    }
                    break;
            }
        }
    }

    //função responsavel pelo cadastro
    private void cadastro(Scanner getTeclado, PrintStream getSaida) {
        getSaida.println("--------------------Cadastro-------------------");
        getSaida.println("Digite o login a ser cadastrado:");
        getSaida.println("EOT");
        String login = null;
        try {
            login = getTeclado.nextLine();
        } catch (Exception e) {
            System.out.println("error: " + e.getMessage());
        }
        Iterator iteradorUsuarios = servidor.getUsuarios().iterator();
        while (iteradorUsuarios.hasNext()) {
            Usuario usuarioAtual = (Usuario) iteradorUsuarios.next();
            if (usuarioAtual.getLogin().equals(login)) {
                getSaida.println("Conta Já Existente, Escolha Outro Login");
                return;
            }
        }
        getSaida.println("Digite a senha a ser cadastrada:");
        getSaida.println("EOT");
        String senha = null;
        try {
            senha = getTeclado.nextLine();
        } catch (Exception e) {
            System.out.println("error: " + e.getMessage());
        }
        getSaida.println("\n\n\n***********CADASTRO EFETUADO COM SUCESSO***********\n");
        //cria um novo usuario comum
        Usuario novo = new Usuario(login, senha, false);
        //salva ele na lista de usuarios do servidor
        servidor.getUsuarios().add(novo);
        servidor.write();
        System.out.println("\nNovo usuario criado: " + login);
    }

    //função responsavel por logar o usuario
    private void logar(Scanner getTeclado, PrintStream getSaida) {
        logado = null;
        getSaida.println("---------------------Login---------------------");
        getSaida.println("Login:");
        getSaida.println("EOT");
        String login = null;
        try {
            login = getTeclado.nextLine();
        } catch (Exception e) {
            System.out.println("error: " + e.getMessage());
        }
        getSaida.println("Senha:");
        getSaida.println("EOT");
        String senha = null;
        try {
            senha = getTeclado.nextLine();
        } catch (Exception e) {
            System.out.println("error: " + e.getMessage());
        }
        //pega o iterador da lista de usuarios do servidor, pra procurar o usuario
        Iterator iteradorUsuarios = servidor.getUsuarios().iterator();
        Boolean existe = false;//booleano responsavel por dizer se a conta existe
        while (iteradorUsuarios.hasNext()) {
            Usuario usuarioAtual = (Usuario) iteradorUsuarios.next();
            //verifica se o login é igual
            if (usuarioAtual.getLogin().equals(login)) {
                //se for igual, verifica se a senha é a mesma
                if (usuarioAtual.getSenha().equals(senha)) {
                    //se a senha for igual, a conta existe
                    existe = true;
                    if (!usuarioAtual.getOnline()) {
                        
                        //então verifica se a conta ja esta sendo utilizada
                        usuarioAtual.setCliente(true);//se não estiver, o usuario loga e altera o valor da variavel online para true
                        logado = usuarioAtual;//variavel logado recebe o usuario que esta logando
                        getSaida.println("\n***********LOGADO COM SUCESSO***********");
                        System.out.println("\nUsuário " + usuarioAtual.getLogin() + " se conectou ao servidor");
                        this.logado(getTeclado, getSaida);
                    } else {
                        getSaida.println("\n***********Conta já esta logada***********");
                    }
                    return;
                }
            }
        }
        if (!existe) {//caso nao encontre o cadastro, o sistema avisa e o usuario volta para o menu para escolher entre cadastro, logar ou sair
            getSaida.println("\nNão Foi Possível Se Conectar: Dados Inválidos");
        }
    }

    //apos o cliente logar
    private void logado(Scanner getTeclado, PrintStream getSaida) {
        List endereco = new ArrayList(); //lista com endereço da navegação
        String naver = null;//navegar pelo sistema
        endereco.add("programa lava duto");//pasta inicial do programa
        while (true) {
            int local = 0;//guarda o local onde esta a variavel que percorre a string do comando de navegação dado pelo usuario
            Iterator it = endereco.iterator();//iterador que percorre a lista de endereços, para ter o endereço atual
            String enderecoAtual = "";//variavel onde é passada o caminho atual
            try {
                sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(TrataCliente.class.getName()).log(Level.SEVERE, null, ex);
            }
            while (it.hasNext()) {//passando o endereço da lista com o local atual, para a variavel
                enderecoAtual = enderecoAtual + (String) it.next();
            }
            File localAtual = new File(enderecoAtual);
            getSaida.println("\n********************************************************************");
            getSaida.println("pasta " + endereco.get(endereco.size() - 1) + ":");//informa em qual pasta esta
            getSaida.println("********************************************************************");
            for (File fileEntry : localAtual.listFiles()) {//informa quais arquivos e pastas estão no diretorio atual
                getSaida.println(fileEntry.getName());
            }
            getSaida.println("EOT");
            try {
                naver = getTeclado.nextLine();
            } catch (Exception e) {
                System.out.println("\n" + logado.getLogin() + " se desconectou do servidor");
                logado.setCliente(false);
                logado = null;
                return;
            }
            String comando = "";//verifica qual foi o comando
            if (naver.contains(" ")) {
                //se for um comando com mais de uma palavra(comando e pasta ou arquivo)
                for (int i = 0; naver.charAt(i) != ' ' && naver.length() > i; i++) {
                    comando = comando + naver.charAt(i);//passa letra por letra do comando para a variavel comando
                    local++;//salva em qual posição da string está
                }
                local++;//pula o espaço
            } else {//se foi um comando puro, como "voltar", somente pega o comando
                comando = naver;
            }
            
            switch (comando) {//escolhe as instruções apartir do comando
                case "abrir":
                    String proximoEndereco = "/";//
                    for (int i = local; i < naver.length(); i++) {//passa o nome da pasta para a variavel "proximoEndereco"
                        proximoEndereco = proximoEndereco + naver.charAt(i);
                        local++;
                    }
                    File aux = new File(enderecoAtual + proximoEndereco);
                    if (aux.exists()) {//verifica se o diretorio existe
                        endereco.add(proximoEndereco);//se existir, coloca na lista de endereços
                    } else {
                        getSaida.println("\n*******************************pasta nao encontrada*************************************");
                    }
                    break;
                case "voltar":
                    if (endereco.size() > 1) {
                        endereco.remove(endereco.size() - 1);
                    } else {//o usuario so vai ter acesso a pasta que foi destinada a ele, ele nao pode navegar por todo sistema
                        getSaida.println("\n*******************************nao pode voltar mais*************************************");
                    }
                    break;
                case "help"://comando que informa os comando que podem ser feitos no sistema
                    getSaida.println("\n\n********************************************************************");
                    getSaida.println("\n---------------------------------------------------------------------------");
                    getSaida.println("Comandos para navegacao");
                    getSaida.println("---------------------------------------------------------------------------");
                    getSaida.println("abrir <pasta>: abre pasta indicada \tvoltar: volta para pasta anterior");
                    if (logado.getEspecial()) {//comando especificos para usuarios especiais, logo so aparecem para eles
                        getSaida.println("add : adiciona ao sistema\tremover <nome do arquivo>: remove do sistema");
                    }
                    getSaida.println("listar: lista o diretorio atual \tdeslogar: sair da conta logada");
                    getSaida.println("download <arquivo>: baixa o arquivo \tsair: sair do sistema");
                    getSaida.println("********************************************************************");
                    getSaida.println("EOT");
                    
                    try {
                        naver = getTeclado.nextLine();
                    } catch (Exception e) {
                        System.out.println("error: " + e.getMessage());
                        logado.setCliente(false);
                        logado = null;
                        break;
                    }
                    break;
                
                case "listar":
                    getSaida.println("\n");
                    break;
                case "sair":
                    logado.setCliente(false);
                     {
                        try {
                            cliente.close();
                            System.out.println("\nCliente " + cliente.getInetAddress().getHostAddress() + " desconectou do servidor");
                        } catch (IOException ex) {
                            System.out.println(ex.getMessage());
                        }
                    }
                    break;
                case "deslogar":
                    logado.setCliente(false);
                    logado = null;
                    getSaida.println("\n\n\n");
                    this.run();
                    System.out.println("\nUsuário " + logado.getLogin() + " deslogou do servidor");
                    break;
                
                case "download":
                    
                    String localArquivo = "/";//
                    for (int i = local; i < naver.length(); i++) {//passa o nome da pasta para a variavel "proximoEndereco"
                        localArquivo = localArquivo + naver.charAt(i);
                        local++;
                    }
                    File arquivoExiste = new File(enderecoAtual + localArquivo);
                    if (arquivoExiste.exists() && arquivoExiste.isFile()) {//verifica se o diretorio existe
                        getSaida.println(true);
                        System.out.println("\nEnviando arquivo para " + logado.getLogin());
                        this.enviarArquivo(arquivoExiste);
                        System.out.println("\nArquivo enviado com sucesso para " + logado.getLogin());
                    } else if (arquivoExiste.exists() && !arquivoExiste.isFile()) {
                        getSaida.println(false);
                    } else {
                        getSaida.println(false);
                    }
                    System.out.println("\nUsuário " + logado.getLogin() + " fez download de arquivo");
                    break;
                
                case "deletar":
                    
                    String localArquivoExcluir = "/";//
                    for (int i = local; i < naver.length(); i++) {//passa o nome da pasta para a variavel "proximoEndereco"
                        localArquivoExcluir = localArquivoExcluir + naver.charAt(i);
                        local++;
                    }
                    File arquivoDeletarExiste = new File(enderecoAtual + localArquivoExcluir);
                    if (logado.getEspecial()) {
                        if (arquivoDeletarExiste.exists() && arquivoDeletarExiste.isFile()) {//verifica se o diretorio existe
                            getSaida.println("\nDeletando arquivo " + logado.getLogin());
                            arquivoDeletarExiste.delete();
                            getSaida.println("\nArquivo deletado\n\n");
                            
                        } else {
                            getSaida.println("Selecione um arquivo");
                        }
                    } else {
                        getSaida.println("Tem que ser usuario especial para poder excluir");
                    }
                    break;
                
                case "add":
                    if (logado.getEspecial()) {
                        getSaida.println(logado.getEspecial());
                        String diretorio = "";
                        for (int i = 0; i < endereco.size(); i++) {
                            diretorio = diretorio + endereco.get(i);
                        }
                        recebeArquivo(diretorio);
                        System.out.println("\nUsuário " + logado.getLogin() + " fez upload de arquivo");
                    }
                    break;
                
                default:
                    getSaida.println("\n***************************Comando não foi reconhecido***************************");
                    break;
            }
            
        }
    }
    
    private void especiais() {//lista para criar usuarios especiais
        Usuario novo = new Usuario("a", "a", true);
        servidor.getUsuarios().add(novo);
    }
    
    private void enviarArquivo(File arquivo) {
        PrintStream getSaida = new PrintStream(saida);
        getSaida.println(arquivo.getName());
        getSaida.println(arquivo.length());
        
        try {
            File f = new File(arquivo.getAbsolutePath());
            FileInputStream in = new FileInputStream(f);
            OutputStream out = cliente.getOutputStream();
            PrintStream ps = new PrintStream(cliente.getOutputStream());
            Scanner input = new Scanner(cliente.getInputStream());
            
            int tamanho = 1048576; // buffer de 4KB
            byte[] buffer = new byte[tamanho];
            long tamanhoParcial = 0;
            long tamanhoTotal = f.length();
            int lidos = -1;
            sleep(100);
            while (tamanhoParcial < tamanhoTotal) {
                lidos = in.read(buffer, 0, tamanho);
                System.out.println((tamanhoParcial * 100) / tamanhoTotal + " %");
                tamanhoParcial += lidos;
                out.write(buffer, 0, lidos);
            }
            System.out.println("saiu do loop");
            String confirma = input.nextLine();
            if (confirma.equals("erro")) {
                System.out.println("deu errado");
                out.flush();
                ps.flush();
            } else if (confirma.equals("terminou")) {
                System.out.println("terminou");
            }
            
            out.flush();
            ps.flush();
            
        } catch (FileNotFoundException ex) {
            System.out.println("FileNotFoundException");
        } catch (IOException ex) {
            System.out.println("Io");
        } catch (Exception e) {
            System.out.println("deu ruim");
        }
        
        System.out.println("Arquivo enviado!");
        
    }
    
    private void recebeArquivo(String diretorio) {
        
        System.out.println("Destino do arquivo: " + diretorio);
        
        Scanner scanner = new Scanner(teclado);
        String fName = scanner.nextLine();
        System.out.println("\nfname: " + fName);
        String n = scanner.nextLine();
        System.out.println("\ntamanho: " + n);
        long tamanhoTotal = Long.parseLong(n);
        System.out.println("começou");
        
        try {
            //download de arquivo
            File f = new File(diretorio + File.separator + fName);
            Scanner input = new Scanner(cliente.getInputStream());
            InputStream in = cliente.getInputStream();
            FileOutputStream out = new FileOutputStream(f);
            PrintStream ps = new PrintStream(cliente.getOutputStream());
            
            int tamanho = 1048576; // buffer de 4KB
            int tamanhoParcial = 0;
            byte[] buffer = new byte[tamanho];
            int lidos = -1;
            while (tamanhoParcial < tamanhoTotal) {
                lidos = in.read(buffer, 0, tamanho);
                tamanhoParcial += lidos;
                System.out.println((tamanhoParcial * 100) / tamanhoTotal + " %");
                out.write(buffer, 0, lidos);
            }
            if (tamanhoParcial < tamanhoTotal) {
                ps.println("erro");
                System.out.println("Erro no download");
                f.delete();
            } else {
                ps.println("confirma");
                System.out.println("download terminado");
            }
            
            out.flush();
            out.close();
            
        } catch (FileNotFoundException ex) {
            System.out.println("FileNotFound");
        } catch (IOException ex) {
            System.out.println("IO");
        }
    }
}
