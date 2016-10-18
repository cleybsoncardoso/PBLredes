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
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cleyb
 */
public class MetodoRemoto extends UnicastRemoteObject implements iMetodoRemoto {

    private ArrayList<Usuario> users;

    public MetodoRemoto() throws RemoteException {
        super();
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
        return arquivo;
    }

    @Override
    public void atualiza(String nome, String conteudo) throws RemoteException {
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
}
