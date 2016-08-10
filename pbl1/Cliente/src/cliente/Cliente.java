/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import static java.lang.Thread.sleep;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cleyb
 */
public class Cliente {

    private Socket cliente;
    private Scanner teclado;
    private Scanner scanner;
    private PrintStream saida;

    public Cliente() throws IOException {
        cliente = new Socket("127.0.0.1", 12345);
        System.out.println("Conexão com o servidor bem sucedida!");
        teclado = new Scanner(System.in);
        scanner = new Scanner(cliente.getInputStream());
        saida = new PrintStream(cliente.getOutputStream());
        try {
            executa();
        } catch (NoSuchElementException ex) {
            System.out.println("\nDesconectado do Servidor!");
            cliente.close();
        }
    }

    private void executa() {
        
        boolean estadoHelp = false;
        String comando = null;
        while (true) {
            try {
                scanner = new Scanner(cliente.getInputStream());
            } catch (IOException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            }
            String msg = scanner.nextLine();
            System.out.println(msg);
            while (!msg.equals("EOT")) {
                msg = scanner.nextLine();
                if (!msg.equals("EOT")) {
                    System.out.println(msg);
                }
            }
            if (comando != null && comando.equals("help")) {
                estadoHelp = true;
            } else {
                estadoHelp = false;
            }
            comando = teclado.nextLine();
            saida.println(comando);
            if (!estadoHelp) {
                String instr[] = comando.split(" ");
                if ("download".equals(instr[0])) {
                    if (scanner.nextBoolean()) {
                        File arquivo = navergar(true);
                        recebeArquivo(arquivo.getAbsolutePath());
                    } else {
                        System.out.println("**Não foi possível efetuar download.");
                    }
                } else if ("add".equals(instr[0])) {
                    if (scanner.nextBoolean()) {
                        System.out.println("\n**Selecione o arquivo**");
                        File arquivo = navergar(false);
                        System.out.println("\n**ARQUIVO SELECIONADO: " + arquivo.getName());
                        System.out.println("**Enviando arquivo..");
                        enviarArquivo(arquivo);
                        System.out.println("**Arquivo enviado!");

                    } else {
                        System.out.println("Comando não permitido");
                    }

                }
            }
        }
    }

    private File navergar(Boolean download) {
        //pilha de navegacao. Contem todas as pastas e o arquivo selecionado
        Stack<File> pilha = new Stack();

        //diretorio que o usuario vai digitando
        String dir = null;
        while (true) {
            if (pilha.isEmpty()) {//se a pilha estiver vazia
                File[] root = File.listRoots();//lista os roots do S.O.
                System.out.println("**Selecione um root:");
                for (int i = 0; i < root.length; i++) {
                    System.out.println(root[i].getPath());//printa os nomes dos roots.
                }
                dir = teclado.nextLine();//usuario escolhe
                for (int i = 0; i < root.length; i++) {//compara se é uma opcao valida
                    if ((root[i].getPath().toLowerCase()).equals(dir.toLowerCase())) {
                        pilha.add(root[i]);//adiciona o root na pilha
                    }
                }

            } else {//se ja estiver no root

                System.out.println("\n**Selecione um arquivo ou pasta:");

                File[] arquivos = pilha.peek().listFiles();//lista os arquivos do root

                System.out.println("../");//opcao de voltar
                for (int i = 0; i < arquivos.length; i++) {//printa os arquivos da pasta selecionada
                    System.out.println(arquivos[i].getName());
                }

                dir = teclado.nextLine();//usuario escolhe o arquivo ou pasta
                if (dir.equals("../")) {//se usuario escolher voltar elimina o topo da pilha
                    pilha.pop();
                } else {
                    for (int i = 0; i < arquivos.length; i++) {//percorre os arquivos e compara com a escolha do usuario
                        if ((arquivos[i].getName().toLowerCase()).equals(dir.toLowerCase())) {
                            pilha.add(arquivos[i]);// se for valido adiciona na pilha
                            break;
                        }
                    }
                    if (download && dir.equals("salvar")) {
                        return pilha.peek();//se for um download, ao identificar o comando salvar, o software identifica que a pasta atual é o diretorio em que o usuario deseja salvar o arquivo
                    }
                    if (pilha.peek().isFile()) {
                        if (!download) {
                            return pilha.peek();//se for arquivo retorna o file.
                        } else {
                            System.out.println("não é possivel salvar dentro de arquivo, somente em pastas");
                        }
                    }
                }
            }
        }
    }

    private void enviarArquivo(File arquivo) {
        PrintStream getSaida = new PrintStream(saida);

        String fName = arquivo.getName();

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

    public static void main(String[] args) throws UnknownHostException, IOException {
        new Cliente();
    }

    private void recebeArquivo(String caminho) {

        String s = scanner.nextLine();
        String fName = scanner.nextLine();
        System.out.println("\nfname: " + fName);
        String n = scanner.nextLine();
        System.out.println("\ntamanho: " + n);
        long tamanhoTotal = Long.parseLong(n);
        System.out.println("começou");

        try {
            //download de arquivo
            File f = new File(caminho + "\\" + fName);
            System.out.println("\n**DIRETÓRIO SELECIONADO: " + f.getAbsolutePath());
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
                System.out.println((tamanhoParcial * 100) / tamanhoTotal + " %");
                tamanhoParcial += lidos;
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
