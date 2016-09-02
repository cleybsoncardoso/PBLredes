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
public class ReceptorUDP {

    public static void main(String[] args) {

        try {
            //Converte o argumento recebido para inteiro (numero da porta)      
            int port = 12347;
            //Cria o DatagramSocket para aguardar mensagens, neste momento o método fica bloqueando
            //até o recebimente de uma mensagem
            DatagramSocket ds = new DatagramSocket(port);
            System.out.println("Ouvindo a porta: " + port);
            //Preparando o buffer de recebimento da mensagem
            byte[] msg = new byte[256];
            //Prepara o pacote de dados
            DatagramPacket pkg = new DatagramPacket(msg, msg.length);
            //Recebimento da mensagem
            ds.receive(pkg);
            JOptionPane.showMessageDialog(null, new String(pkg.getData()).trim(), "Mensagem recebida", 1);
            ds.close();
            while (true) {
                System.out.println(pkg.getData());
            }
        } catch (IOException ioe) {
        }
    }
}
