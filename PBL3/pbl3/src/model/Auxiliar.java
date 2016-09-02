/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import util.Cliente;
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
        Cliente cliente = new Cliente(ip, porta);
        new Thread(cliente).start();
        clientes.add(cliente);
        cliente.enviarMsg("primeiro");
        ArrayList<String> ips = (ArrayList<String>) cliente.receberMsg();
        for (String ipCliente : ips) {
            Cliente clienteAtual = new Cliente(ipCliente, 8080);
            clienteAtual.enviarMsg("segundo");
            clientes.add(clienteAtual);
        }
    }

    public void addSocket(String ip, int porta) {
        Cliente cliente = new Cliente(ip, porta);
        new Thread(cliente).start();
        cliente.enviarMsg("segundo");
        clientes.add(cliente);
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
        for (Cliente clienteAtual : clientes) {
            new Thread(new Replicador(clienteAtual, clientes, msg)).start();
        }
    }

    public void removerCliente(String ip) {
        for (Cliente cliente : clientes) {
            if (cliente.getIp().equals(ip)) {
                clientes.remove(cliente);
            }
        }
    }
}
