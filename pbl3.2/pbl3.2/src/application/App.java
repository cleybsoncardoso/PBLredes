/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import controller.Controller;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.EstouNaRede;
import util.Server;
import util.TrataCliente;

/**
 *
 * @author paiva
 */
public class App {

    public static void main(String[] args) {
        Controller controller = new Controller();
        //controller.iniciarConexao("25.12.22.120");
        //controller.primeiraConexao("25.12.22.120");
        Server serverSocket = new Server(controller, 8080);
        new Thread(serverSocket).start();
        Scanner teclado = new Scanner(System.in);
        EstouNaRede enr = new EstouNaRede("239.0.0.1", 12347, controller);
        new Thread(enr).start();

        String[] dados = new String[3];
        String ip = null;
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
            System.out.println(ip);
        } catch (UnknownHostException ex) {
            System.out.println("não foi possivel verificar ip");
        }
        dados[0] = "238.128.0.2";
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
        while (true) {
            controller.replicarMsg(teclado.nextLine());
        }
    }
}
