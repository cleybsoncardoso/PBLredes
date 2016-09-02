/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import controller.Controller;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLSocket;
import util.Cliente;
import util.Quadrante;

/**
 *
 * @author cleyse
 */
public class Auxiliar {

    private ArrayList<Cliente> clientes;
    private Controller controller;

    public Auxiliar(Controller controller) {
        this.clientes = new ArrayList<Cliente>();
        this.controller=controller;
    }

    public void iniciarConexao(String ip) {
        Cliente client = new Cliente(8080, ip);
        new Thread(client).start();
        client.enviarMsg("segundo");
        clientes.add(client);
    }

    public void replicarMsg(String msg) {
        for (Cliente c : clientes) {
            c.enviarMsg(msg);
        }
    }

    public void removerCliente(String ip) {
        for (Cliente c : clientes) {
            if (c.getIp().equals(ip)) {
                clientes.remove(c);
            }
        }
    }

    public void primeiraConexao(String ip) {
        Cliente client = new Cliente(8080, ip);
        new Thread(client).start();
        client.enviarMsg("primeiro");
        try {
            ArrayList<String> ips = (ArrayList<String>) client.getInput().readObject();
            for (String ipAtual : ips) {
                System.out.println(ipAtual);
                controller.iniciarConexao(ipAtual);
            }
        } catch (IOException ex) {
            Logger.getLogger(Auxiliar.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Auxiliar.class.getName()).log(Level.SEVERE, null, ex);
        }
        clientes.add(client);
    }
    
    public ArrayList<Quadrante> calcularTrajeto(String origem, String destino) {
        ArrayList<Quadrante> trajeto = new ArrayList<Quadrante>();
        if (origem.equals("A")) {
            trajeto.add(new Quadrante("d", 0, 0, 1));
            if (destino.equals("B")) {
                return trajeto;
            } else if (destino.equals("C")) {
                trajeto.add(new Quadrante("b", 0, 0, 2));
                return trajeto;
            } else {
                trajeto.add(new Quadrante("b", 0, 0, 2));
                trajeto.add(new Quadrante("a", 0, 0, 3));
                return trajeto;
            }
        } else if (origem.equals("B")) {
            trajeto.add(new Quadrante("b", 0, 0, 1));
            if (destino.equals("C")) {
                return trajeto;
            } else if (destino.equals("D")) {
                trajeto.add(new Quadrante("a", 0, 0, 2));
                return trajeto;
            } else {
                trajeto.add(new Quadrante("a", 0, 0, 2));
                trajeto.add(new Quadrante("c", 0, 0, 3));
                return trajeto;
            }
        } else if (origem.equals("C")) {
            trajeto.add(new Quadrante("a", 0, 0, 1));
            if (destino.equals("D")) {
                return trajeto;
            } else if (destino.equals("A")) {
                trajeto.add(new Quadrante("c", 0, 0, 2));
                return trajeto;
            } else {
                trajeto.add(new Quadrante("c", 0, 0, 2));
                trajeto.add(new Quadrante("d", 0, 0, 3));
                return trajeto;
            }
        } else {
            trajeto.add(new Quadrante("c", 0, 0, 1));
            if (destino.equals("A")) {
                return trajeto;
            } else if (destino.equals("B")) {
                trajeto.add(new Quadrante("d", 0, 0, 2));
                return trajeto;
            } else {
                trajeto.add(new Quadrante("d", 0, 0, 2));
                trajeto.add(new Quadrante("b", 0, 0, 3));
                return trajeto;
            }
        }
    }
}
