/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Aplication;

import Controller.Controller;
import java.util.Scanner;
import util.Server;

/**
 *
 * @author cleyb
 */
public class Aplication {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Controller controller = new Controller();
        Server servidor = new Server(controller);
        new Thread(servidor).start();
        Scanner teste = new Scanner(System.in);
        while(true){
            String t = teste.nextLine();
            controller.replica(t);
        }
    }
    
}