/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.Coordenada;

/**
 *
 * @author paiva
 */
public class Carro {

    private Socket cliente;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Coordenada coordenada;
    private String origem;
    private String destino;

    public Carro(Socket cliente) {
        try {
            output = new ObjectOutputStream(cliente.getOutputStream());
            input = new ObjectInputStream(cliente.getInputStream());
        } catch (IOException ex) {
            System.out.println("Conexão perdida");
        }

    }

}
