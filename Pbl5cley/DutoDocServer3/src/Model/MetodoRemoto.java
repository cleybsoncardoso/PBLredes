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
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import static java.lang.Thread.sleep;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
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
    private ArrayList<Usuario> logados;
    private HashMap<String, Documento> documentos;
    private HashMap<String, LinkedList<Modificacao>> requisicoes;//key: nometitulo - value: fila de modificacao
    private ArrayList<Quantidade> documentosAbertos;
    private Queue<Modificacao> fila;

    public MetodoRemoto() throws RemoteException {
        super();
        documentos = new HashMap<>();
        documentosAbertos = new ArrayList<>();
        fila = new LinkedList<>();
        logados = new ArrayList<>();
        requisicoes = new HashMap<>();
        users = this.leituraLogin();
    }

    @Override
    public Boolean logar(String nome, String senha) throws RemoteException {
        System.out.println(nome + " tentou se conectar");
        for (Usuario user : users) {
            if (user.getNome().equals(nome) && user.getSenha().equals(senha)) {
                logados.add(user);
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
    public String abrirArquivo(String nomeArquivo, String nome) throws RemoteException {

        Documento documento = documentos.get(nomeArquivo);
        requisicoes.put(nome + nomeArquivo, new LinkedList<>());
        if (documento != null) {
            documento.increment();
            return documento.getConteudo();
        } else {
            String conteudo = "";
            try {
                File fnome = new File(nomeArquivo);
                FileInputStream stream;
                stream = new FileInputStream(fnome);
                InputStreamReader reader = new InputStreamReader(stream);
                BufferedReader br = new BufferedReader(reader);
                String texto = br.readLine();
                while (texto != null) {
                    conteudo += texto + "\n";
                    texto = br.readLine();
                }
                br.close();
                reader.close();
                stream.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(MetodoRemoto.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            } catch (IOException ex) {
                Logger.getLogger(MetodoRemoto.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
            documentos.put(nomeArquivo, new Documento(nomeArquivo, conteudo));
            return conteudo;
        }

    }

    private void gravarArquivo(String nome, String conteudo) throws RemoteException {
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

    /**
     * Atualiza uma remoção.
     *
     * @param nome
     * @param position
     */
    private void atualiza(String nome, int position) {
        Documento atual = documentos.get(nome);
        if (atual.getNome().equals(nome)) {
            System.out.println("removendo posicao: " + position);
            StringBuilder texto = new StringBuilder(atual.getConteudo());
            texto.deleteCharAt(position);
            atual.setConteudo(texto.toString());
            System.out.println("\ntexto atual:\n" + atual.getConteudo());
        }
    }

    /**
     * Atualiza uma remoção por seleção.
     *
     * @param nome
     * @param position
     */
    private void atualiza(String nome, int posBegin, int posEnd) {
        Documento atual = documentos.get(nome);
        if (atual.getNome().equals(nome)) {
            System.out.println("removendo da posicao: " + posBegin + " atéa posição: " + posEnd);
            StringBuilder texto = new StringBuilder(atual.getConteudo());
            texto.delete(posBegin, posEnd);
            atual.setConteudo(texto.toString());
            System.out.println("\ntexto atual:\n" + atual.getConteudo());
        }
    }

    /**
     * Atualiza uma adição.
     *
     * @param nome
     * @param conteudo
     * @param position
     */
    private void atualiza(String nome, char conteudo, int position) {
        File fnome = new File(nome);
        Documento atual = documentos.get(nome);
        if (atual.getNome().equals(nome)) {
            System.out.println("caractere: " + conteudo + " caret: " + position);
            if (position <= 0) {
                atual.setConteudo(conteudo + atual.getConteudo());
                System.out.println("no inicio");
            } else if (position >= atual.length()) {
                atual.setConteudo(atual.getConteudo() + conteudo);
                System.out.println("no final");
            } else {
                StringBuilder t = new StringBuilder(atual.getConteudo());
                t.insert(position, conteudo);
                atual.setConteudo(t.toString());
                System.out.println("no meio");
            }
            System.out.println("\ntexto atual:\n" + atual.getConteudo());
        }
    }

    @Override
    public void fechar(String user, String titulo) throws RemoteException {
        Documento documento = documentos.get(titulo);
        if (documento != null) {
            requisicoes.remove(user + titulo);
            int valor = documento.decrement();
            System.out.println("numero de usuarios" + valor);
            if (valor == 0) {
                documentos.remove(titulo);
            }
            gravarArquivo(titulo, documento.getConteudo());
        }
    }


    @Override
    public void modifica(String user, String nome, char conteudo, int carent) throws RemoteException {
        System.out.println("usuario " + user + " adicionou: " + conteudo);
        fila.add(new Adicao(nome, conteudo, carent));
        for (Usuario usuario : logados) {
            if (!usuario.getNome().equals(user)) {
                LinkedList<Modificacao> lista = requisicoes.get(usuario.getNome() + nome);//add(new Adicao(nome, conteudo, carent));
                if (lista != null) {
                    lista.add(new Adicao(nome, conteudo, carent));
                }
            }
        }
    }

    @Override
    public void del(String user, String nome, int pos) throws RemoteException {
        fila.add(new Remocao(nome, pos));
        for (Usuario usuario : logados) {
            if (!usuario.getNome().equals(user)) {
                LinkedList<Modificacao> lista = requisicoes.get(usuario.getNome() + nome);//add(new Adicao(nome, conteudo, carent));
                if (lista != null) {
                    lista.add(new Remocao(nome, pos));
                }
            }
        }
    }

    @Override
    public void del(String user, String nome, int posBegin, int posEnd) throws RemoteException {
        fila.add(new RemocaoSelecao(nome, posBegin, posEnd));
        for (Usuario usuario : logados) {
            if (!usuario.getNome().equals(user)) {
                LinkedList<Modificacao> lista = requisicoes.get(usuario.getNome() + nome);//add(new Adicao(nome, conteudo, carent));
                if (lista != null) {
                    lista.add(new RemocaoSelecao(nome, posBegin, posEnd));
                }
            }
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                sleep(0, 1);
                if (!fila.isEmpty()) {
                    Modificacao atual = fila.poll();
                    if (atual instanceof Adicao) {
                        Adicao add = (Adicao) atual;
                        this.atualiza(add.getNome(), add.getConteudo(), add.getPosition());
                        System.out.println("é uma adicao");
                    } else if (atual instanceof Remocao) {
                        Remocao del = (Remocao) atual;
                        this.atualiza(del.getNome(), del.getPosition());
                        System.out.println("é uma remocao");
                    } else {
                        RemocaoSelecao del = (RemocaoSelecao) atual;
                        this.atualiza(del.getNome(), del.getPosBegin(), del.getPosEnd());
                        System.out.println("é uma remocao de seleção");
                    }
                    //atual = fila.poll();
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(MetodoRemoto.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public Modificacao requisicao(String nome, String nomeTitulo) throws RemoteException {
        System.out.println("usuario " + nome + " pediu uma requisicao");
        LinkedList<Modificacao> modificacao = requisicoes.get(nome + nomeTitulo);
        if (modificacao != null) {
            return modificacao.poll();
        } else {
            return null;
        }
    }
}
