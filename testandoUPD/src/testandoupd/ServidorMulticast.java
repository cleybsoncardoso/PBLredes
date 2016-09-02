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
public class ServidorMulticast {

    public static void main(String[] args) {

        args = new String[3];

        args[0] = "224.0.0.0";
        args[1] = "12347";
        args[2] = "teste";
        if (args.length != 3) {
            System.out.println("Digite <endereco multicast> <porta> <mensagem>");
            System.exit(0);
        }

        try {
            byte[] b = args[2].getBytes();
            InetAddress addr = InetAddress.getByName(args[0]);
            DatagramSocket ds = new DatagramSocket();
            DatagramPacket pkg = new DatagramPacket(b, b.length, addr, Integer.parseInt(args[1]));
            ds.send(pkg);
        } catch (Exception e) {
            System.out.println("Nao foi possivel enviar a mensagem");
        }
    }
}
