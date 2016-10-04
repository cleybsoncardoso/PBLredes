/*
 * Classe responsavel pela comunicação com o grupo multicast
 * tanto para receber, quanto para enviar mensagem
 */
package model;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cleybson e lucas
 */
public class Multicast {

    private static Multicast multicast;
    private String grupo, meuIp;
    private int porta;
    private String myKey;

    public Multicast() {
        try {
            grupo = "224.0.0.0";//ip do grupo
            porta = 12345;//porta do grupo
            meuIp = InetAddress.getLocalHost().getHostAddress(); //pega o seu ip que esta conectado a rede
            myKey = this.geraKey();//minha chave
        } catch (UnknownHostException ex) {
            Logger.getLogger(Multicast.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Cria instancia da classe multicast
     * @return 
     */
    public static Multicast novaInstancia() {
        multicast = new Multicast();
        return multicast;
    }

    /**
     * Retorna a instancia da classe multicast
     * @return 
     */
    public static Multicast getInstancia() {
        return multicast;
    }

    /**
     * gera a sua chave do hashmap
     * @return 
     */
    private String geraKey() {
        StringTokenizer tokenIp = new StringTokenizer(meuIp, ".");
        tokenIp.nextToken();
        tokenIp.nextToken();
        String chaveHash = tokenIp.nextToken() + tokenIp.nextToken();
        return chaveHash;
    }

    /**
     * metodo que recebe todas as mensagem transmitidas no grupo
     * @return 
     */
    public String receberMensagem() {
        try {
            MulticastSocket mcs = new MulticastSocket(porta);
            InetAddress grp = InetAddress.getByName(grupo);
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

    /**
     * Prepara a mensagem e transmite no grupo
     * @param msg 
     */
    public void enviarMensagem(String msg) {
        String msgCompleta = meuIp + ";" + myKey + ";" + msg; 
        /*coloca o seu ip e a sua chave na transmissão para que todos do grupo 
        saiba quem esta enviando a mensagem*/
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
