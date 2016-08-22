/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.Arquivo;

/**
 * Classe responsavel por tratar os cliente que se conectam ao servidor
 * @author cleyb
 */
class TratarCliente implements Runnable {

    private Servidor servidor; //servidor, com os dados do usuario
    private Socket cliente; //socket que se conectou
    private Arquivo upload; //referencia do arquivo a ser baixado

    public TratarCliente(Servidor servidor, Socket cliente) {
        this.servidor = servidor;
        this.cliente = cliente;
    }

    @Override
    public void run() {

        try {
            ObjectInputStream input = new ObjectInputStream(cliente.getInputStream());
            ObjectOutputStream output = new ObjectOutputStream(cliente.getOutputStream());
            
            Object a = input.readObject();//verifica se é o servidor principal ou um usuario que se conectou
            //se for o servidor principal, o programa sabe que ele só quer saber qual o usuario que esta logado no momento com este serveSocket aberto
            if (a.toString().equals("servidor")) {
                
                output.writeObject(this.servidor.getNomeCliente());
            } else {
                //se for um usuario, o programa sabe que o usuario só quer fazer o download
                upload = (Arquivo) input.readObject();
                System.out.println("Arquivo enviado: " + upload.getNome());
                FileInputStream fis;
                File arq;

                try {
                    arq = new File(upload.getEndereco() + "/" + upload.getNome());
                    //verifica se o arquivo ainda existe no repositório
                    if(arq.exists()){
                        //envia resposta positiva ao cliente
                        output.writeObject("sim");
                    }else{
                        //envia resposta negativa ao cliente
                        output.writeObject("nao");                        
                    }
                    System.out.println("diretorio: " + arq.getAbsolutePath());
                    fis = new FileInputStream(arq);
                    OutputStream os = cliente.getOutputStream();

                    long tamanhoTotal = arq.length();
                    long tamanhoParcial = 0;
                    int tamanhoBuffer = 1024;
                    byte[] buffer = new byte[tamanhoBuffer];
                    int lidos;
                    System.out.println("Enviando...");
                    while (tamanhoParcial < tamanhoTotal) {
                        lidos = fis.read(buffer, 0, tamanhoBuffer);
                        tamanhoParcial += lidos;
                        os.write(buffer, 0, lidos);
                    }
                    System.out.println("Enviado.");
                    fis.close();
                } catch (FileNotFoundException ex) {
                    System.out.println("\n\n\nArquivo não encontrado.");
                } catch (IOException ex) {
                    System.out.println("Erro na comunicação.");

                }
            }
        } catch (IOException ex) {
            System.err.println("Cliente se desconectou");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TratarCliente.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
