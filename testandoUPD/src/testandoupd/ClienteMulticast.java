/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testandoupd;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 *
 * @author cleyb
 */
public class ClienteMulticast implements Runnable {

    private String ip;

    public ClienteMulticast(String ip) {
        this.ip = ip;
    }
    

    @Override
    public void run() {
        try {
            MulticastSocket mcs = new MulticastSocket(12347);
            InetAddress grp = InetAddress.getByName(ip);
            mcs.joinGroup(grp);
            byte rec[] = new byte[256];
            DatagramPacket pkg = new DatagramPacket(rec, rec.length);
            mcs.receive(pkg);
            String data = new String(pkg.getData());
            System.out.println("Dados recebidos:" + data);
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}
