/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.Cliente;
import util.Quadrante;
import util.Replicador;

/**
 *
 * @author cleyb
 */
public class Auxiliar {

    private ArrayList<Cliente> clientes;

    public Auxiliar() {
        this.clientes = new ArrayList<Cliente>();
    }

    public void primeiraConexao(String ip, int porta) {
        try {
            
            Cliente cliente = new Cliente(ip, porta);
            clientes.add(cliente);
            
            ArrayList<String> ips = (ArrayList<String>) new ObjectInputStream(cliente.getInputStream()).readObject();
            for (String ipCliente : ips) {
                Socket clienteAtual = new Socket(ipCliente, 8080);
                new ObjectOutputStream(clienteAtual.getOutputStream()).writeObject("segundo");
                clientes.add(clienteAtual);
            }
        } catch (IOException ex) {
            System.out.println("não foi possivel estabelecer conexao");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Auxiliar.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addSocket(String ip, int porta) {
        try {
            Socket cliente = new Socket(ip, porta);
            new ObjectOutputStream(cliente.getOutputStream()).writeObject("segundo");
            clientes.add(cliente);
        } catch (IOException ex) {
            System.out.println("não foi possivel estabelecer conexao");

        }
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

    public void replica(String msg) {
        for (Socket clienteAtual : clientes) {
            new Thread(new Replicador(clienteAtual, clientes, msg)).start();
        }
    }
}
