/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Interface.iMetodoRemoto;
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

    public Controller(String ip) throws NotBoundException, MalformedURLException, RemoteException {
        try {
            metodos = (iMetodoRemoto) Naming.lookup("//" + ip + ":3322/metodos");
        } catch (ConnectException e) {
            System.err.println("Falha na conexao");
        }
    }

    public Boolean login(String nome, String senha) {
        try {
            return metodos.logar(nome, senha);
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

    public String abrirArquivo(String nome) {
        try {
            return metodos.abrirArquivo(nome);
        } catch (RemoteException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void escreveArquivo(String nome, char conteudo, int carent) {
        try {
            metodos.modifica(nome, conteudo, carent);
        } catch (RemoteException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
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
            metodos.del(nome, pos);
        } catch (RemoteException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
