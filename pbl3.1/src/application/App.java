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
import util.TrataCliente;
import view.Inicio;

/**
 *
 * @author paiva
 */
public class App {

    public static void main(String[] args) {

        Controller controller = Controller.novoController();
        Inicio telaInicial = Inicio.novoController(controller); 
        new Thread(new TrataCliente()).start();

        }
    }
