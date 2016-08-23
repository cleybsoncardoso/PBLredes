/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import util.Arquivo;

/**
 *
 * @author cleyb
 */
public class Download implements Runnable {

    private String ip;
    private int porta;
    private Arquivo baixando;
    Socket baixaServidor = null;

    public Download(String ip, int porta, Arquivo baixando) {
        this.ip = ip;
        this.porta = porta;
        this.baixando = baixando;
    }

    @Override
    public void run() {

        try {
            baixaServidor = new Socket(ip, porta);
            System.out.println("Conectou com o servidor Download");
            ObjectOutputStream output = new ObjectOutputStream(baixaServidor.getOutputStream());
            output.writeObject(baixando);

        } catch (IOException ex) {
            System.err.println("Download nao concluido");
        }

        FileOutputStream fos = null;
        try {
            InputStream in = baixaServidor.getInputStream();
            long tamanho = baixando.getTamanho();
            long tamanhoParcial = 0;

            File testePasta = new File("./programa lava duto download");
            if(!testePasta.exists()){
                System.out.println("criando pasta de download");
                testePasta.mkdir();
            }
            fos = new FileOutputStream("./programa lava duto download/" + baixando.getNome());
            int tamanhoBuffer = 1024;
            byte[] buffer = new byte[tamanhoBuffer];
            int lidos;

            System.out.println("Recebendo...");
            while (tamanhoParcial < tamanho) {
                lidos = in.read(buffer, 0, tamanhoBuffer);
                tamanhoParcial += lidos;
                fos.write(buffer, 0, lidos);
            }
            System.out.println("Recebido.");

            fos.flush();
            fos.close();

        } catch (FileNotFoundException ex) {
            System.out.println("Diretório não enconstrado.");
        } catch (IOException ex) {
            try {
                fos.close();
                File arq = new File("./programa lava duto" + baixando.getNome());
                arq.delete();
                System.out.println("Erro de comunicação");
            } catch (IOException ex1) {
                //É lógicamente impossível chegar aqui.
                System.out.println("Erro inesperado");
            }

        }catch(NullPointerException ex){
            System.out.println("Usuario se desconectou");
        }
        try {
            baixaServidor.close();
        } catch (IOException ex) {
            System.out.println("Conexao com usuario de download foi perdida");
        }catch(NullPointerException e){
            System.out.println("Cliente de download se desconectou");
        }
    }
}
