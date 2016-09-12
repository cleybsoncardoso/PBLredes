/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import controller.Controller;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 *
 * @author cleyb
 */
public class EstouNaRede implements Runnable {

    private int porta;
    private String ip;
    private Controller controller;

    public EstouNaRede(String ip, int porta, Controller controller) {
        this.porta = porta;
        this.ip = ip;
        this.controller = controller;
    }

    @Override
    public void run() {
        while (true) {
            try {
                MulticastSocket mcs = new MulticastSocket(porta);
                InetAddress grp = InetAddress.getByName(ip);//grupo multicast
                mcs.joinGroup(grp);
                byte rec[] = new byte[256];
                DatagramPacket pkg = new DatagramPacket(rec, rec.length);
                mcs.receive(pkg);
                String data = new String(pkg.getData());
<<<<<<< HEAD
                System.out.println("Conectando com servidor: " + data);
=======
                //System.out.println("Conectando com servidor: " + data);
>>>>>>> ad056ddc786a6852de8463372da5e8a86c8c622c
                controller.primeiraConexao(data);
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
    }

}
