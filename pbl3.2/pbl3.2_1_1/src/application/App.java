/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import controller.Controller;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;
import model.EstouNaRede;
import util.Server;
import view.Inicio;

/**
 *
 * @author paiva
 */
public class App {

    public static void main(String[] args) {
        String ip = null;
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
            System.out.println(ip);
        } catch (UnknownHostException ex) {
            System.out.println("não foi possivel verificar ip");
        }
        Controller controller = Controller.novoController(ip);
        
        Inicio telaInicial = Inicio.novoController(controller);
        //controller.iniciarConexao("25.12.22.120");
        //controller.primeiraConexao("192.168.0.6");
        //controller.iniciarConexao("25.12.22.120");
        Server serverSocket = new Server(8080);
        new Thread(serverSocket).start();
        
        EstouNaRede enr = new EstouNaRede("224.0.0.0", 12347, controller);
        new Thread(enr).start();

        String[] dados = new String[3];
        dados[0] = "224.0.0.0";
        dados[1] = "12347";
        dados[2] = ip;
        try {
            byte[] b = dados[2].getBytes();
            InetAddress addr = InetAddress.getByName(dados[0]);
            DatagramSocket ds = new DatagramSocket();
            DatagramPacket pkg = new DatagramPacket(b, b.length, addr, Integer.parseInt(dados[1]));
            ds.send(pkg);
        } catch (Exception e) {
            System.out.println("Nao foi possivel enviar a mensagem");
        }
        Scanner teclado = new Scanner(System.in);
        while (true) {
            controller.replicarMsg(teclado.nextLine());
        }
    }
}
