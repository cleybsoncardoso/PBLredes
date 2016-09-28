/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cleyse
 */
public class Multicast {

    private static Multicast multicast;
    private String grupo, meuIp;
    private int porta;

    public Multicast() {
        try {
            grupo = "224.0.0.0";
            porta = 12345;
            meuIp = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException ex) {
            Logger.getLogger(Multicast.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static Multicast novaInstancia() {
        multicast = new Multicast();
        return multicast;
    }

    public static Multicast getInstancia() {
        return multicast;
    }

    public String receberMensagem() {
        try {
            MulticastSocket mcs = new MulticastSocket(porta);
            InetAddress grp = InetAddress.getByName(grupo);//grupo multicast
            mcs.joinGroup(grp);
            byte rec[] = new byte[256];
            DatagramPacket pkg = new DatagramPacket(rec, rec.length);
            mcs.receive(pkg);
            String data = new String(pkg.getData());
            return data;
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
        return null;
    }

    public void enviarMensagem(String msg) {
        String msgCompleta = meuIp + ";" + msg;
        try {
            byte[] b = msgCompleta.getBytes();
            InetAddress addr = InetAddress.getByName(grupo);
            DatagramSocket ds = new DatagramSocket();
            DatagramPacket pkg = new DatagramPacket(b, b.length, addr, porta);
            ds.send(pkg);
        } catch (Exception e) {
            System.out.println("Nao foi possivel enviar a mensagem");
        }
    }

}
