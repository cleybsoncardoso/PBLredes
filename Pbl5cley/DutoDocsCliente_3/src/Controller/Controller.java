/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Interface.iMetodoRemoto;
import Model.Modificacao;
import java.net.MalformedURLException;
import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controller {

    private iMetodoRemoto metodos; //interface com os metodos remote
    private String nome, titulo; //nome do usuario logado e titulo do texto que está aberto

    /**
     * construtor do controller
     * @param ip ip do servidor que deseja se conectar
     * @throws NotBoundException
     * @throws MalformedURLException
     * @throws RemoteException 
     */
    public Controller(String ip) throws NotBoundException, MalformedURLException, RemoteException {
        try {
            metodos = (iMetodoRemoto) Naming.lookup("//" + ip + ":3322/metodos"); //obtendo referencias do remote
            this.nome = null;
            this.titulo = null;
        } catch (ConnectException e) {
            System.err.println("Falha na conexao");
        }
    }

    /**
     * Metodo que pede ao remote para fazer o login
     * @param nome nome cadastrado no login
     * @param senha senha cadastrada
     * @return true se o login for aceito
     */
    public Boolean login(String nome, String senha) {
        try {
            if (metodos.logar(nome, senha)) {
                this.nome = nome;
                return true;
            }
        } catch (RemoteException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }

    /**
     * metodo que pede ao servidor o nome de todos os arquivos txt
     * @return lista com o nome dos arquivos
     */
    public ArrayList<String> listarArquivosTxt() {
        try {
            return metodos.listarArquivos();
        } catch (RemoteException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Metodo que cria arquivos
     * @param nomeArquivo nome do arquivo que deseja criar 
     */
    public void criarArquivo(String nomeArquivo) {
        try {
            metodos.criarArquivo(nomeArquivo);
        } catch (RemoteException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Metodo que pede ao servidor as modificações feitas por outros usuarios que estão usando o mesmo documento
     * @return retorna a modificação
     */
    public Modificacao requisicao() {
        if (titulo != null) {
            try {
                return metodos.requisicao(this.nome, this.titulo);
            } catch (RemoteException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    /**
     * Metodo que pede ao servidor para abrir um arquivo
     * @param nome nome do arquivo que deseja abrir
     * @return retorna o texto em forma de string
     */
    public String abrirArquivo(String nome) {
        this.titulo = nome;
        try {
            return metodos.abrirArquivo(nome, this.nome);
        } catch (RemoteException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * metodo que é utilizado para avisar ao servidor, que o usuario fez alguma alteração
     * @param nome nome do arquivo
     * @param conteudo char que foi alterado
     * @param carent posição que foi inserido ou retirado
     */
    public void escreveArquivo(String nome, char conteudo, int carent) {
        try {
            metodos.modifica(this.nome, nome, conteudo, carent);
        } catch (RemoteException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getNome() {
        return nome;
    }

    public String getTitulo() {
        return titulo;
    }

/**
 *´Apagar algum caracter
 * @param nome nome do arquivo
 * @param pos posição da letra apagada
 */
    public void del(String nome, int pos) {
        try {
            metodos.del(this.nome, nome, pos);
        } catch (RemoteException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
/**
 * apagar caracter por seleção
 * @param nome nome do arquivo
 * @param selectionStart posição da primeira letra a ser apagada
 * @param selectionEnd  posição da ultima letra a ser apagada
 */
    public void del(String nome, int selectionStart, int selectionEnd) {
        try {
            metodos.del(this.nome, nome, selectionStart, selectionEnd);
        } catch (RemoteException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * fechar arquivo
     */
    public void fechar() {
        try {
            metodos.fechar(nome, titulo);
            titulo=null;
        } catch (RemoteException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
