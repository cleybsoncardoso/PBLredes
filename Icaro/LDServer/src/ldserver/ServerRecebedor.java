package ldserver;

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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Thread chamada para segurar o client e receber o arquivo um Objeto desta
 * classe é criado sempre que alguém tenta se conectar ao servidor devido a esta
 * thread, é possivel que vários usuarios se conectem ao servidor pois cada um
 * recebe uma porta diferente devido a thread.
 *
 * @author Ícaro Rios
 */
public class ServerRecebedor implements Runnable {

    private Socket clientSocket;
    private List<Usuario> usuarios;
    private static ObjectInputStream input;
    private static ObjectOutputStream output;
    private Usuario usuario;
    private String caminho;
    private final List<Usuario> usuariosEspeciais;

    /**
     * Construtor do recebedor
     *
     * @param clientSocket
     * @param usuarios
     * @param usuariosEspeciais
     */
    public ServerRecebedor(Socket clientSocket, List<Usuario> usuarios, List<Usuario> usuariosEspeciais) {
        this.usuarios = usuarios;
        this.usuariosEspeciais = usuariosEspeciais;
        this.clientSocket = clientSocket;
        try {
            input = new ObjectInputStream(clientSocket.getInputStream());
            output = new ObjectOutputStream(clientSocket.getOutputStream());
            caminho = File.listRoots()[0].getAbsolutePath();
        } catch (IOException ex) {
            System.out.println("Erro na comunicação.");
            logout();
        }

    }

    /**
     * Método responsável por enviar um arquivo pela rede.
     *
     * @param file nome do arquivo que quer baixar da pasta atual
     */
    public void enviarArquivo(String file) {

        File arq;
        FileInputStream fis;

        try {
            arq = new File(caminho + file);
            fis = new FileInputStream(arq);
            OutputStream os = clientSocket.getOutputStream();
            long tamanhoTotal = arq.length();
            long tamanhoParcial = 0;
            enviarMensagem(String.valueOf(tamanhoTotal));
            output.flush();

            int tamanhoBuffer = 1024;
            byte[] buffer = new byte[tamanhoBuffer];
            int lidos;

            while (tamanhoParcial < tamanhoTotal) {
                lidos = fis.read(buffer, 0, tamanhoBuffer);
                tamanhoParcial += lidos;
                os.write(buffer, 0, lidos);
            }
            fis.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Arquivo não encontrado");
        } catch (IOException ex) {
            System.out.println("Erro na comunicação");
        }
        /*
         File arq = new File("teste.mp4");
         enviarMensagem(String.valueOf(arq.length()));
         output.flush();
         byte[] buffer = new byte[1024];
         FileInputStream fis = new FileInputStream(arq);

         int count;
         System.out.println(String.valueOf(arq.length()));
         long tamanho = arq.length();
         while (tamanho > 0) {
         count = fis.read(buffer,0,1024);
         tamanho -=count;
         output.write(buffer, 0, count);
         System.out.println(count);
         }
        
         System.out.println("Arquivo enviado: " + file);
        
         fis.close();
         /*
        
         /*
         OutputStream os = clientSocket.getOutputStream();        
         //File arq = new File(origem + file);
         File arq = new File("teste.mp4");
         FileInputStream fis = new FileInputStream(arq);
        
         byte[] buffer = new byte[1024];
         int count;
         System.out.println("enviando arquivo");

         while ((count = fis.read(buffer)) > 0) {
         os.write(buffer, 0, count);

         }
         System.out.println("Arquivo enviado: " + file);
         fis.close();
         */
    }

    public void receberArquivo(String nome) {
        FileOutputStream fos = null;
        try {
            InputStream in = clientSocket.getInputStream();
            long tamanho = Long.valueOf(receberMensagem());
            long tamanhoParcial = 0;

            fos = new FileOutputStream(caminho + nome);
            int tamanhoBuffer = 1024;
            byte[] buffer = new byte[tamanhoBuffer];
            int lidos;

            while (tamanhoParcial < tamanho) {
                lidos = in.read(buffer, 0, tamanhoBuffer);
                tamanhoParcial += lidos;
                fos.write(buffer, 0, lidos);
            }
            System.out.println("recebido");
            fos.flush();
            fos.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Diretório não enconstrado.");
        } catch (IOException ex) {
            try {
                fos.close();
                File arq = new File(caminho + nome);
                arq.delete();
                System.out.println("Erro de comunicação");
            } catch (IOException ex1) {
                //É lógicamente impossível chegar aqui.
                System.out.println("Erro inesperado");
            }
        }
    }

    /**
     * Método que enviar uma mensagem através da rede.
     *
     * @param mensagem
     */
    public void enviarMensagem(String mensagem) {
        try {
            output.writeObject(mensagem);
        } catch (IOException ex) {
            System.out.println("Erro na comunicação");
            if (usuario != null) {
                logout();
            }
        }

    }

    /**
     * Método que recebe uma mensagem através da rede.
     *
     * @return string da mensagem
     */
    public String receberMensagem() {
        try {
            return input.readObject().toString();
        } catch (IOException ex) {
            System.out.println("Erro na comunicação");
            if (usuario != null) {
                logout();
            }
        } catch (ClassNotFoundException ex) {
            System.out.println("Erro na comunicação");
            logout();
        }
        return null;
    }

    /**
     * Método que remove um arquivo do sistema.
     *
     * @param arquivo
     */
    private void removerArquivo(String arquivo) {
        File arq = new File(caminho + arquivo);
        arq.delete();
        

    }

    /**
     * Método que volta um diretório.
     */
    private void voltarDiretorio() {
        caminho = new File(caminho).getParent();// pega a pasta pai
        if (caminho == null) // a string fica assim quando está na pasta C e tenta voltar
        {
            caminho = File.listRoots()[0].getAbsolutePath();// se já estivesse no root, pega o root novamente
        }
        /*
         String[] pastas = caminho.split("\\\\");// expressão regular para a \ ou melhor "\\"
        
         caminho = "";
         for (int i = 0; i < pastas.length - 1; i++) {// pega todas as strings que ficaram menos a ultima
         caminho += pastas[i] + File.separator;
         }
         if (caminho.equals("")) {// a string fica assim quando está na pasta C e tenta voltar
         caminho = File.listRoots()[0].getAbsolutePath();
         }
         */
    }

    /**
     * Método que da a sencação ao usuário que ele entrou em um diretório do
     * servidor
     *
     * @param diretorio
     * @return string com informação de pastas e arquivos do servidor.
     */
    private String entrarDiretorio(String diretorio) {
        String novoCaminho = caminho + diretorio + File.separator;
        File folder = new File(novoCaminho);
        caminho = novoCaminho;

        return pegarDiretorio();
    }

    /**
     * Método que lista os arquivos e pastas do diretório atual.
     *
     * @return
     */
    private String pegarDiretorio() {
        String informacao = "";
        try{
        File folder = new File(caminho);
        
        File[] listOfFiles = folder.listFiles();
        

        for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile()) {
                informacao += "Arquivo " + listOfFile.getName() + "\n";
            } else if (listOfFile.isDirectory()) {
                informacao += "Pasta " + listOfFile.getName() + "\n";
            }
        }
        }catch( Exception ex){
            return "Diretório não existente.";
        }
        return informacao;
    }

    /**
     * Método que cadastra um usuário.
     *
     * @param login
     * @param senha
     * @return True se foi bem sucedido
     */
    private boolean cadastrarUsuario(String login, String senha) {

        Usuario novo = new Usuario(login, senha);
        for (Usuario usuario1 : usuarios) {
            if (novo.getLogin().equals(usuario1.getLogin())) {
                return false;
            }
        }
        for (Usuario usuario1 : usuariosEspeciais) {
            if (novo.getLogin().equals(usuario1.getLogin())) {
                return false;
            }
        }
        usuarios.add(novo);
        return true;
    }

    /**
     * Método que faz o login de um usuário no sistema
     *
     * @param login
     * @param senha
     * @return true se foi bem sucedido
     */
    private boolean login(String login, String senha) {
        usuario = new Usuario(login, senha);
        for (Usuario usuario1 : usuarios) {
            if (usuario1.equals(usuario) && !usuario1.isLogado()) {
                usuario1.setLogado(true);
                usuario = usuario1;
                return true;
            }
        }
        for (Usuario usuarioEspecial : usuariosEspeciais) {
            if (usuarioEspecial.equals(usuario) && !usuarioEspecial.isLogado()) {
                usuarioEspecial.setLogado(true);
                usuario = usuarioEspecial;
                return true;
            }
        }
        return false;
    }

    /**
     * Método de logout de usuário.
     */
    private void logout() {
        usuario.setLogado(false);
        usuario = null;
        caminho = File.listRoots()[0].getAbsolutePath();

    }

    /**
     * Método que responde ao usuario se ele fez o login.
     */
    private void login() {
        String login = receberMensagem();
        String senha = receberMensagem();

        boolean log = login(login, senha);
        if (log) {
            enviarMensagem(pegarDiretorio());
        } else {
            enviarMensagem("Erro de login");
        }
    }

    /**
     * Método que responde ao usuário se ele conseguiu fazer o login no sistema.
     */
    private void cadastrar() {
        String login = receberMensagem();
        String senha = receberMensagem();

        boolean cad = cadastrarUsuario(login, senha);
        if (cad) {
            enviarMensagem("Cadastrado\n");
        } else {
            enviarMensagem("Usuario inválido");
        }
    }

    /**
     * Sobrescrita do método run implementado pelo runnable este método é
     * chamado no momento em que a thread é iniciada.
     */
    @Override
    public void run() {
        try {
            carregarUsuarios();
            String resposta;
            do {

                resposta = receberMensagem();
                if (resposta.equalsIgnoreCase("lin")) {
                    login();
                } else if (resposta.equalsIgnoreCase("cad")) {
                    cadastrar();
                    salvarUsuarios();
                } else if (resposta.equalsIgnoreCase("en")) {
                    String novoDir = entrarDiretorio(receberMensagem());
                    enviarMensagem(novoDir);
                } else if (resposta.equalsIgnoreCase("vol")) {
                    voltarDiretorio();
                    enviarMensagem(pegarDiretorio());
                } else if (resposta.equalsIgnoreCase("down")) {
                    String arquivo = receberMensagem();
                    enviarArquivo(arquivo);
                } else if (resposta.equalsIgnoreCase("up")) {
                    if (usuario.isUpload()) {
                        enviarMensagem("Permitido");
                        String arquivo = receberMensagem();
                        receberArquivo(arquivo);
                    } else {
                        enviarMensagem("Negado");
                    }
                } else if (resposta.equalsIgnoreCase("logout")) {
                    logout();
                } else if (resposta.equalsIgnoreCase("del")) {
                    if (usuario.isUpload()) {
                        enviarMensagem("Permitido");
                        removerArquivo(receberMensagem());
                    } else {
                        enviarMensagem("Negado");
                    }
                }

            } while (!resposta.equalsIgnoreCase("exit"));
            System.out.println("Desconectado");
            input.close();
            output.close();
            clientSocket.close();

        } catch (IOException ex) {
            System.out.println("erro de comunicação");
            if (usuario != null) {
                logout();
            }
        }

    }

    public void carregarUsuarios() {

        try {
            FileInputStream arquivoLeitura = new FileInputStream("Backup.ser");
            ObjectInputStream objLeitura = new ObjectInputStream(arquivoLeitura);
            usuarios = (List<Usuario>) objLeitura.readObject();

            arquivoLeitura.close();
            objLeitura.close();
            arquivoLeitura.close();

        } catch (FileNotFoundException ex) {
            System.out.println("Não tem arquivo de backup.");
        } catch (ClassNotFoundException ex) {
            System.out.println("Formato do arquivo diferente.");
        } catch (IOException ex) {
            if (usuario != null) {
                logout();
            }
            System.out.println("Erro na comunicação.");
        }

    }

    public void salvarUsuarios() {
        try {
            FileOutputStream fs = new FileOutputStream("Backup.ser");
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(usuarios);

            fs.flush();
            fs.close();
            os.flush();
            os.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Não tem arquivo de backup.");
        } catch (IOException ex) {
            System.out.println("Erro na comunicação.");
            if (usuario != null) {
                logout();
            }
        }
    }
}
