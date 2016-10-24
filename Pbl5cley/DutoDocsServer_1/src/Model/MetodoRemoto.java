/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Controller.Controller;
import Interface.iMetodoRemoto;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import static java.lang.Thread.sleep;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cleyb
 */
public class MetodoRemoto extends UnicastRemoteObject implements iMetodoRemoto, Runnable {

    private ArrayList<Usuario> users;
    private ArrayList<Documento> documentos;
    private ArrayList<Quantidade> documentosAbertos;
    private Queue<Modificacao> fila;

    public MetodoRemoto() throws RemoteException {
        super();
        documentos = new ArrayList<Documento>();
        documentosAbertos = new ArrayList<Quantidade>();
        fila = new LinkedList<Modificacao>();
        users = this.leituraLogin();
    }

    @Override
    public Boolean logar(String nome, String senha) throws RemoteException {
        for (Usuario user : users) {
            if (user.getNome().equals(nome) && user.getSenha().equals(senha)) {
                return true;
            }
        }
        return false;
    }

    private ArrayList<Usuario> leituraLogin() {
        ArrayList<Usuario> cadastros = new ArrayList<Usuario>();
        File fUser = new File("usuarios.txt");
        if (fUser.exists()) {
            try {
                FileInputStream stream = new FileInputStream(fUser);
                InputStreamReader reader = new InputStreamReader(stream);
                BufferedReader br = new BufferedReader(reader);
                String linha = br.readLine();
                while (linha != null) {
                    String[] user = linha.split(":");
                    Usuario novo = new Usuario(user[0], user[1]);
                    cadastros.add(novo);
                    System.out.println("Cadastrado usuario: " + user[0]);
                    linha = br.readLine();
                }
                br.close();
                reader.close();
                stream.close();

            } catch (FileNotFoundException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            System.out.println("Arquivo txt com os usuarios não existe, Será criado o arquivo pelo sistema, e então coloque os usuarios e as senhas dos seguinte modo: usuario:senha");
            try {
                fUser.createNewFile();
                System.exit(0);
            } catch (IOException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return cadastros;
    }

    @Override
    public ArrayList<String> listarArquivos() throws RemoteException {
        ArrayList<String> arquivos = new ArrayList<String>();
        File localAtual = new File(".");
        for (File fileEntry : localAtual.listFiles()) {//informa quais arquivos e pastas estão no diretorio atual
            if (fileEntry.isFile() && verificatxt(fileEntry.getName()) && !fileEntry.getName().equals("usuarios.txt")) {
                arquivos.add(fileEntry.getName());
            }
        }
        return arquivos;
    }

    private Boolean verificatxt(String nomeArquivo) {
        String extensao = nomeArquivo.substring(nomeArquivo.lastIndexOf("."), nomeArquivo.length());
        return extensao.equals(".txt");
    }

    @Override
    public void criarArquivo(String nomeArquivo) throws RemoteException {
        try {
            File fileCriado = new File(nomeArquivo);
            fileCriado.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(MetodoRemoto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String abrirArquivo(String nomeArquivo) throws RemoteException {
        String arquivo = "";
        try {
            File fnome = new File(nomeArquivo);
            FileInputStream stream;
            stream = new FileInputStream(fnome);
            InputStreamReader reader = new InputStreamReader(stream);
            BufferedReader br = new BufferedReader(reader);
            String linha = br.readLine();
            while (linha != null) {
                arquivo += linha + "\n";
                linha = br.readLine();
            }
            br.close();
            reader.close();
            stream.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MetodoRemoto.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MetodoRemoto.class.getName()).log(Level.SEVERE, null, ex);
        }
        Boolean existe = false;
        for (Quantidade qnt : documentosAbertos) {
            if (qnt.getNome().equals(nomeArquivo)) {
                qnt.increment();
                existe = true;
            }
        }
        if (!existe) {
            documentos.add(new Documento(nomeArquivo, arquivo));
            documentosAbertos.add(new Quantidade(nomeArquivo));
        }
        return arquivo;

    }

    private void atualiza(String nome, String conteudo) throws RemoteException {
        File fnome = new File(nome);
        try {
            FileWriter fileW = new FileWriter(fnome);//arquivo para escrita
            BufferedWriter buffW = new BufferedWriter(fileW);
            buffW.write(conteudo);
            buffW.close();
            fileW.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MetodoRemoto.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MetodoRemoto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void atualiza(String nome, char conteudo, int carent) throws RemoteException {
        System.out.println("carent :" + carent);
        File fnome = new File(nome);
        for (Documento atual : documentos) {
            if (atual.getNome().equals(nome)) {
                String todo = atual.getConteudo();
                if (carent <= 0) {
                    atual.setConteudo(conteudo + todo);
                } else if (carent >= todo.length()) {
                    atual.setConteudo(todo + conteudo);
                } else {
                    String aux = todo.substring(carent - 1);
                    atual.setConteudo(todo.replaceFirst(aux, conteudo + aux));
                }
//                try {
//                    String antes = "";
//                    String todo = atual.getConteudo();
//                    antes = todo.substring(0, carent-1);
//                    System.out.println(antes);
//                    antes += conteudo;
//                    System.out.println(antes);
//                    antes += todo.substring(carent-1);
//                    System.out.println(antes);
//                    atual.setConteudo(antes);
//                } catch (StringIndexOutOfBoundsException ex) {
//                    String antes = atual.getConteudo();
//                    antes += conteudo;
//                    atual.setConteudo(antes);
//
//                }
            }
        }

//        try {
//            FileWriter fileW = new FileWriter(fnome);//arquivo para escrita
//            BufferedWriter buffW = new BufferedWriter(fileW);
//            buffW.write(conteudo);
//            buffW.close();
//            fileW.close();
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(MetodoRemoto.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(MetodoRemoto.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    @Override
    public void fecha(String nome, String conteudo) throws RemoteException {
        Quantidade remover = null;
        for (Quantidade qtn : documentosAbertos) {
            if (qtn.getNome().equals(nome)) {
                qtn.decrement();
                if (qtn.getCont() == 0) {
                    remover = qtn;
                }
                break;
            }
        }
        if (remover != null) {
            documentosAbertos.remove(remover);
            this.atualiza(nome, conteudo);
            Documento remove = null;
            for (Documento fechar : documentos) {
                if (nome.equals(fechar.getNome())) {
                    remove = fechar;
                    break;
                }
            }
            documentos.remove(remove);
        }
    }

    @Override
    public void modifica(String nome, char conteudo, int carent) throws RemoteException {
        fila.add(new Modificacao(nome, conteudo, carent));
    }

    @Override
    public void run() {
        while (true) {
            try {
                sleep(0, 1);
                if (!fila.isEmpty()) {
                    System.out.println("modificou1");
                    Modificacao atual = fila.poll();
                    while (atual != null) {
                        System.out.println("modificou");
                        this.atualiza(atual.getNome(), atual.getConteudo(), atual.getCarent());
                        atual = fila.poll();
                    }
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(MetodoRemoto.class.getName()).log(Level.SEVERE, null, ex);
            } catch (RemoteException ex) {
                Logger.getLogger(MetodoRemoto.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public String refresh(String nomeArquivo) throws RemoteException {
        ArrayList<Documento> documentosAtual = documentos;
        for (Documento atual : documentosAtual) {
            if (atual.getNome().equals(nomeArquivo)) {
                //System.out.println("entrou");
                return atual.getConteudo();
            }
        }
        return null;
    }
}
