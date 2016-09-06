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
        
        Inicio telaInicial = new Inicio(controller);
        System.out.println("saiu");
        //controller.iniciarConexao("25.12.22.120");
        //controller.primeiraConexao("25.12.22.120");
        Server serverSocket = new Server(controller, 8080);


//
//        //adicionando carros no cruzamento
//        controller.adicionarCarro(0, "A", "B");
//        controller.adicionarCarro(1, "A", "C");
//        controller.adicionarCarro(2, "A", "D");
//        controller.adicionarCarro(3, "B", "A");
//        controller.adicionarCarro(4, "B", "C");
//        controller.adicionarCarro(5, "B", "D");
//        controller.adicionarCarro(6, "C", "A");
//        controller.adicionarCarro(7, "C", "B");
//        controller.adicionarCarro(8, "C", "D");
//        controller.adicionarCarro(9, "D", "A");
//        controller.adicionarCarro(10, "D", "B");
//        controller.adicionarCarro(11, "D", "C");
        
        new Thread(serverSocket).start();
        Scanner teclado = new Scanner(System.in);
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
        while (true) {
            controller.replicarMsg(teclado.nextLine());
        }
    }
}
