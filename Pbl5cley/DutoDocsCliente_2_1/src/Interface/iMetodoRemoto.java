/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interface;

import Model.Modificacao;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 *
 * @author cleyb
 */
public interface iMetodoRemoto extends Remote {

    public Boolean logar(String nome, String senha) throws RemoteException;

    public ArrayList<String> listarArquivos() throws RemoteException;

    public void criarArquivo(String nome) throws RemoteException;

    public String abrirArquivo(String nomeArquivo) throws RemoteException;

    public String refresh(String nomeArquivo) throws RemoteException;

    public void modifica(String user, String nome, char conteudo, int carent) throws RemoteException;

    public void fecha(String nome, String conteudo) throws RemoteException;

    public void del(String user, String nome, int pos) throws RemoteException;

    public void del(String user, String nome, int posBegin, int posEnd) throws RemoteException;

    public Modificacao requisicao(String nome, String titulo) throws RemoteException;

}
