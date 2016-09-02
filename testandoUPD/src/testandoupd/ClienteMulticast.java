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
import java.net.MulticastSocket;
import javax.swing.JOptionPane;

/**
 *
 * @author cleyb
 */
public class ClienteMulticast {

  public static void main(String[] args) {

      while(true) {
        try {       
          MulticastSocket mcs = new MulticastSocket(12347);
          InetAddress grp = InetAddress.getByName("238.128.0.2");
          mcs.joinGroup(grp);
          byte rec[] = new byte[256];
          DatagramPacket pkg = new DatagramPacket(rec, rec.length);
          mcs.receive(pkg);
          String data = new String(pkg.getData());
          System.out.println("Dados recebidos:" + data);
      }
      catch(Exception e) {
        System.out.println("Erro: " + e.getMessage()); 
      } 
    }
  }
}