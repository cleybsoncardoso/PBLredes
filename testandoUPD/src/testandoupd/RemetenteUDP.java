/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testandoupd;

import java.io.IOException;
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
public class RemetenteUDP {

  public static void main(String[] args) {

    String[] teste = new String[3];
        String ip = null;
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
            System.out.println(ip);
        } catch (UnknownHostException ex) {
            //Logger.getLogger(ServidorMulticast.class.getName()).log(Level.SEVERE, null, ex);
        }
        teste[0] = "239.0.0.1";
        teste[1] = "12347";
        teste[2] = ip;

    try {
      //Primeiro argumento é o nome do host destino
      InetAddress addr = InetAddress.getByName(teste[0]);
      int port = Integer.parseInt(teste[1]);
      byte[] msg = teste[2].getBytes();
      //Monta o pacote a ser enviado
      DatagramPacket pkg = new DatagramPacket(msg,msg.length, addr, port);
      // Cria o DatagramSocket que será responsável por enviar a mensagem
      DatagramSocket ds = new DatagramSocket();
      //Envia a mensagem
      ds.send(pkg);
      System.out.println("Mensagem enviada para: " + addr.getHostAddress() + "\n" +
	      "Porta: " + port + "\n" + "Mensagem: " + teste[2]);

      //Fecha o DatagramSocket
      ds.close();     
    }

    catch(IOException ioe) {}
  }
}
