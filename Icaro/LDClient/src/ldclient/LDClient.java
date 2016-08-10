package ldclient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Esta classe é responsável por gerar o programa o qual o usuário terá contato
 * para manipular o servidor.
 *
 * @author Icaro Rios
 */
public class LDClient {

    private static ObjectInputStream input;
    private static ObjectOutputStream output;
    private static Socket socket;
    private static Scanner teclado;

    /**
     * Este método é responsável por enviar uma menságem atravéz da comunicação.
     *
     * @param mensagem
     */
    public static void enviarMensagem(String mensagem) {
        try {
            output.writeObject(mensagem);
        } catch (IOException ex) {
            System.out.println("Erro na comunicação");
        }

    }

    /**
     * Método responsável por receber uma mensagem.
     *
     * @return mensagem que foi recebida.
     */
    public static String receberMensagem() {
        try {
            return input.readObject().toString();
        } catch (IOException ex) {
            System.out.println("Erro na comunicação");

        } catch (ClassNotFoundException ex) {
            System.out.println("Erro interno do Sistema Operacilnal.");
        }
        return "";

    }

    /**
     * Método responsável por eniar um arquivo através da comunicação
     *
     * @param origem string que mostra de ondeo arquivo virá
     * @param file nome do arquivo
     */
    public static void enviarArquivo(String origem, String file) {
        FileInputStream fis;
        File arq;
        try {
            arq = new File(origem + File.separator + file);

            fis = new FileInputStream(arq);
            OutputStream os = socket.getOutputStream();

            long tamanhoTotal = arq.length();
            long tamanhoParcial = 0;
            enviarMensagem(String.valueOf(tamanhoTotal));
            output.flush();

            int tamanhoBuffer = 1024;
            byte[] buffer = new byte[tamanhoBuffer];
            int lidos;
            System.out.println("Enviando.");
            while (tamanhoParcial < tamanhoTotal) {
                lidos = fis.read(buffer, 0, tamanhoBuffer);
                tamanhoParcial += lidos;
                os.write(buffer, 0, lidos);
            }
            System.out.println("Enviado.");
            fis.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Arquivo não encontrado.");
        } catch (IOException ex) {
            System.out.println("Erro na comunicação.");

        }

    }

    /**
     * Método responsável por receber um arquivo atravez da comunicação.
     *
     * @param diretorio diretório onde o arquivo irá
     * @param nome nome que o arquivo irá receber, cuidado com o formato
     */
    public static void receberArquivo(String diretorio, String nome) {
        FileOutputStream fos = null;
        try {
            InputStream in = socket.getInputStream();
            long tamanho = Long.valueOf(receberMensagem());
            long tamanhoParcial = 0;

            fos = new FileOutputStream(diretorio + File.separator + nome);
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
                File arq = new File(diretorio + File.separator + nome);
                arq.delete();
                System.out.println("Erro de comunicação");
            } catch (IOException ex1) {
                //É lógicamente impossível chegar aqui.
                System.out.println("Erro inesperado");
            }

        }

        /**
         * Algumas tentativas que deram errado...
         */
        /*
         FileOutputStream fos = new FileOutputStream("teste.mp4");// crio o arquivo e abro a stream
         byte[] buffer = new byte[1024];// informo o tamanho do pacote que será enviado por vez
         int count;
         long tamanho = Long.valueOf(receberMensagem());
         System.out.println(tamanho);
         while (tamanho > 0) {
         count = input.read(buffer,0,1024);
         tamanho -=count;
         System.out.println(tamanho);
         if(tamanho<1024){
         int i = (int) tamanho;
         fos.write(buffer,0,i);
         break;
         }
         fos.write(buffer, 0, count); // preencho o arquivo com os pacotes recebidos            
         }

         System.out.println("Arquivo " + nome + " recebido");
         fos.close();
         */

        /*
         InputStream is = socket.getInputStream();

         //File arq = new File(diretorio + nome);
         File arq = new File("teste.mp4");
         FileOutputStream fos = new FileOutputStream(arq);// crio o arquivo e abro a stream
         //FileOutputStream fos = new FileOutputStream(diretorio + nome);// crio o arquivo e abro a stream

         byte[] buffer = new byte[1024];// informo o tamanho do pacote que será enviado por vez
         int count;
         count = is.read(buffer);
         fos.write(buffer, 0, count); // esta leitura e escrita adicional é feita pois ainda não tem nada no available
         fos.flush();

         while (is.available() > 0) {
         count = is.read(buffer);
         fos.write(buffer, 0, count); // preencho o arquivo com os pacotes recebidos

         fos.flush();
         }

         System.out.println("Arquivo recebido: " + nome);
         fos.close();
         */
    }

    /**
     * Método responsável por estabelecer a comunicação com o servidor. Este
     * método é recursivo através de exception.
     */
    public static void conectarServidor() {
        try {
            teclado = new Scanner(System.in);
            System.out.println("Digite o IP:");
            String host = teclado.nextLine();
            System.out.println("Digite a porta:");
            int porta = teclado.nextInt();
            teclado.nextLine();
            socket = new Socket(host, porta);
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());

        } catch (Exception ex) {
            System.out.println("Informações erradas.");
            conectarServidor();
            //teclado.remove();
        }

    }

    /**
     * Main do sistema.
     *
     * @param args
     */
    public static void main(String[] args) {

        /**
         * tentar conexão
         */
        //int porta = Integer.getInteger(port);
        conectarServidor();

        //socket = new Socket(host, port);
        //output = new ObjectOutputStream(socket.getOutputStream());
        //input = new ObjectInputStream(socket.getInputStream());
        String leitura;

        do {
            //System.out.println(socket.getLocalPort());
            System.out.println("1 - Fazer Login \n2 - Cadastrar Usuario\n3 - sair");
            leitura = teclado.nextLine();
            if (leitura.equalsIgnoreCase("1")) {
                enviarMensagem("lin");
                System.out.println("Digite seu login");
                enviarMensagem(teclado.nextLine());
                System.out.println("Digite sua senha");
                enviarMensagem(teclado.nextLine());
                String recebido = receberMensagem();
                System.out.println(recebido);
                if (!recebido.equalsIgnoreCase("Erro de login")) {
                    do {
                        System.out.println("1 - Para entrar em pastas\n2 - Para voltar de pasta"
                                + "\n3 - Para Download de arquivo\n4 - Para upload de arquivo\n5 - Para remover Arquivo"
                                + "\n6 - Para logout");
                        leitura = teclado.nextLine();
                        if (leitura.equalsIgnoreCase("1")) {// entrar em pastas
                            enviarMensagem("en");
                            System.out.println("Digite o nome da pasta.");
                            enviarMensagem(teclado.nextLine());
                            System.out.println(receberMensagem());
                        } else if (leitura.equalsIgnoreCase("2")) {//voltar de pasta
                            enviarMensagem("vol");
                            System.out.println(receberMensagem());
                        } else if (leitura.equalsIgnoreCase("3")) {//download de arquivo
                            enviarMensagem("down");
                            System.out.println("Informe o nome do diretório de destino.");
                            String diretorio = teclado.nextLine();
                            System.out.println("Informe o nome do arquivo.");
                            String arquivo = teclado.nextLine();
                            enviarMensagem(arquivo);// envia o nome do arquivo
                            receberArquivo(diretorio, arquivo);// depois pede o download
                        } else if (leitura.equalsIgnoreCase("4")) {
                            enviarMensagem("up");
                            if (receberMensagem().equals("Permitido")) {
                                System.out.println("Informe o nome do diretório de origem.");
                                String diretorio = teclado.nextLine();
                                System.out.println("Informe o nome do arquivo.");
                                String arquivo = teclado.nextLine();
                                enviarMensagem(arquivo);
                                enviarArquivo(diretorio, arquivo);
                            } else {
                                System.out.println("Usuario sem autorização");
                            }

                        } else if (leitura.equalsIgnoreCase("5")) {
                            enviarMensagem("del");
                            if (receberMensagem().equals("Permitido")) {
                                System.out.println("Informe o nome do arquivo.");
                                enviarMensagem(teclado.nextLine());
                            }
                        }

                    } while (!leitura.equalsIgnoreCase("6"));
                    enviarMensagem("logout");
                }
            } else if (leitura.equalsIgnoreCase("2")) {
                enviarMensagem("cad");
                System.out.println("Digite um login.");
                String login = teclado.nextLine();
                System.out.println("Digite uma senha");
                String senha = teclado.nextLine();
                enviarMensagem(login);
                enviarMensagem(senha);
                System.out.println(receberMensagem());
            }

        } while (!leitura.equalsIgnoreCase("3"));
        enviarMensagem("exit");
        try {
            /*
             do {
             System.out.println(receberMensagem);//recebe opcoes de login
             teclado.nextLine();// não sei porque, más ainda fica algo na entrada, então faço ele sair de vez
             leitura = teclado.nextLine();//cadastrar, fazer login ou sair?
            
             if (leitura.equalsIgnoreCase("1")) {// login
             enviarMensagem("1");
             login();
             do {
             System.out.println(receberMensagem());
             leitura = teclado.nextLine();
             enviarMensagem(leitura);
             System.out.println(receberMensagem());
             enviarMensagem(teclado.nextLine());
             System.out.println(receberMensagem());
             } while (!leitura.equalsIgnoreCase("5"));
            
             } else if (leitura.equalsIgnoreCase("2")) {
             enviarMensagem("2");
             cadastrarUsuario();
             System.out.println(receberMensagem());
             enviarMensagem(teclado.nextLine());
             }
            
             } while (leitura.equalsIgnoreCase("10"));
             */
            /*
             client.enviarMensagem(teclado.nextLine());
             System.out.println(client.receberMensagem());
             client.enviarMensagem(teclado.nextLine());
             System.out.println(client.receberMensagem());
            
             //client.enviarMensagem("C:\\Users\\Neida\\Dropbox\\UEFS\\4 SEMESTRE\\redes testes\\LDServer");
             //client.enviarMensagem("teste.mp3");
             //client.enviarArquivo("", "teste.mp3");
             String diretorio = client.receberMensagem();
             String nome = client.receberMensagem();
             client.receberArquivo(diretorio, nome);
             */
            input.close();
            output.close();
            teclado.close();
            socket.close();
        } catch (IOException ex) {
            System.out.println("Comunicação encerrada.");
        }

    }

}
