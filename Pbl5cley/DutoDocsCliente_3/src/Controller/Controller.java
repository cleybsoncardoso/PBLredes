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

    private iMetodoRemoto metodos;
    private String nome, titulo;

    public Controller(String ip) throws NotBoundException, MalformedURLException, RemoteException {
        try {
            metodos = (iMetodoRemoto) Naming.lookup("//" + ip + ":3322/metodos");
            this.nome = null;
            this.titulo = null;
        } catch (ConnectException e) {
            System.err.println("Falha na conexao");
        }
    }

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

    public ArrayList<String> listarArquivosTxt() {
        try {
            return metodos.listarArquivos();
        } catch (RemoteException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void criarArquivo(String nomeArquivo) {
        try {
            metodos.criarArquivo(nomeArquivo);
        } catch (RemoteException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Modificacao requisicao() {
        try {
            return metodos.requisicao(this.nome, this.titulo);
        } catch (RemoteException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String abrirArquivo(String nome) {
        this.titulo = nome;
        try {
            return metodos.abrirArquivo(nome, this.nome);
        } catch (RemoteException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

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

    public String refresh(String nome) {
        try {
            return metodos.refresh(nome);
        } catch (RemoteException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void del(String nome, int pos) {
        try {
            metodos.del(this.nome, nome, pos);
        } catch (RemoteException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void del(String nome, int selectionStart, int selectionEnd) {
        try {
            metodos.del(this.nome, nome, selectionStart, selectionEnd);
        } catch (RemoteException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void fechar() {
        try {
            metodos.fechar(nome, titulo);
        } catch (RemoteException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
