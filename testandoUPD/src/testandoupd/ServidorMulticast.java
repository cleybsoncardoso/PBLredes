/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testandoupd;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import static java.lang.Thread.sleep;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cleyb
 */
public class ServidorMulticast {

    public static void main(String[] args) {

        String[] teste = new String[3];
        String ip = null, ipAtual;
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
            System.out.println(ip);
        } catch (UnknownHostException ex) {
            Logger.getLogger(ServidorMulticast.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            ServerSocket servidor = new ServerSocket(8080);
            Socket clienteM = servidor.accept();
            ObjectInputStream input = new ObjectInputStream(clienteM.getInputStream());
            ObjectOutputStream output = new ObjectOutputStream(clienteM.getOutputStream());
            for (int i = 224; i < 240; i++) {
                for (int j = 0; j < 256; j++) {
                    for (int k = 0; k < 256; k++) {
                        for (int l = 0; l < 256; l++) {
                           
                            String ipEnviar = i+"."+j+"."+k+"."+l;
                            output.writeObject(ipEnviar);
                            try {
                                sleep(100);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(ServidorMulticast.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            teste[0] = ipEnviar;
                            teste[1] = "12347";
                            teste[2] = ipEnviar;

                            try {
                                byte[] b = teste[2].getBytes();
                                InetAddress addr = InetAddress.getByName(teste[0]);
                                DatagramSocket ds = new DatagramSocket();
                                DatagramPacket pkg = new DatagramPacket(b, b.length, addr, Integer.parseInt(teste[1]));
                                ds.send(pkg);
                            } catch (Exception e) {
                                System.out.println("Nao foi possivel enviar a mensagem");
                            }
                            try {
                                sleep(100);
                                output.writeObject("fechar");
                            } catch (InterruptedException ex) {
                                Logger.getLogger(ServidorMulticast.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ServidorMulticast.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
