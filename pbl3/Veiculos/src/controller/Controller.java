/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.Socket;

/**
 *
 * @author paiva
 */
public class Controller {
    private Socket cliente;

    public Controller(Socket cliente) {
        this.cliente = cliente;
    }
    
    
}
