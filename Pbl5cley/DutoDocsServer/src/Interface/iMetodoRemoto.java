/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interface;

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
    
    public void atualiza(String nome,String conteudo) throws RemoteException;

}
