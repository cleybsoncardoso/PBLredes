/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author paiva
 */
public class Controller {
    private Socket cliente;

    public Controller(String ip, int porta) {
        try {
            cliente=new Socket(ip, porta);
        } catch (IOException ex) {
            System.err.println("não foi possivel abrir conexão");
        }
    }
}
