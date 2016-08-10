/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ldclient;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Neida
 */
public class ReceberArquivo implements Runnable {

    Socket clientSocket;
    String diretorio;
    String nome;
    private final InputStream input;

    public ReceberArquivo(Socket clientSocket, String diretorio, String nome) throws IOException {
        this.clientSocket = clientSocket;
        this.diretorio = diretorio;
        this.nome = nome;
        this.input = clientSocket.getInputStream();
    }

    @Override
    public void run() {

        FileOutputStream fos;
        try {
            fos = new FileOutputStream(nome); // crio o arquivo e abro a stream

            byte[] buffer = new byte[1024];// informo o tamanho do pacote que será enviado por vez
            int count;
            while ((count = input.read(buffer)) > 0) {
                fos.write(buffer, 0, count); // preencho o arquivo com os pacotes recebidos
            }

            System.out.println("Arquivo recebido: " + nome);
            fos.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ReceberArquivo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ReceberArquivo.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            input.close();
        } catch (IOException ex) {
            Logger.getLogger(ReceberArquivo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

}
