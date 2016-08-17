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
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.Arquivo;

/**
 *
 * @author cleyb
 */
class TratarCliente implements Runnable {

    private Servidor servidor;
    private Socket cliente;
    private Arquivo upload;

    public TratarCliente(Servidor servidor, Socket cliente) {
        this.servidor = servidor;
        this.cliente = cliente;
    }

    @Override
    public void run() {
        try {
            ObjectInputStream input = new ObjectInputStream(cliente.getInputStream());
            upload = (Arquivo) input.readObject();
            System.out.println("Arquivo enviado: " + upload.getNome());
        } catch (IOException ex) {
            System.err.println("Cliente se desconectou");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TratarCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        FileInputStream fis;
        File arq;
        
        try {
            arq = new File(upload.getNome());

            fis = new FileInputStream(arq);
            OutputStream os = cliente.getOutputStream();

            long tamanhoTotal = arq.length();
            long tamanhoParcial = 0;
            int tamanhoBuffer = 1024;
            byte[] buffer = new byte[tamanhoBuffer];
            int lidos;
            System.out.println("Enviando.");
            while (tamanhoParcial < tamanhoTotal) {
                lidos = fis.read(buffer, 0, tamanhoBuffer);
                tamanhoParcial += lidos;
                os.write(buffer, 0, lidos);
            }
            System.out.println("Enviado.");
            fis.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Arquivo não encontrado.");
        } catch (IOException ex) {
            System.out.println("Erro na comunicação.");

        }
    }

}
