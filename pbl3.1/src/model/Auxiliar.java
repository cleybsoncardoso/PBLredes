/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import controller.Controller;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.Cliente;

/**
 * classe que auxilia o controller
 * @author cleybson e lucas
 */
public class Auxiliar {

    private ArrayList<Cliente> clientes;//lista de todos os carros conectados
    private Controller controller;

    public Auxiliar(Controller controller) {
        this.clientes = new ArrayList<Cliente>();
        this.controller = controller;
    }

    /**
     * conecta com o carro do usuario com o com o serversocket
     * @param ip ip do server socket
     */
    public void iniciarConexao(String ip) {
        Cliente client = new Cliente(8080, ip);
        System.out.println("Me conectei com " + ip);
        new Thread(client).start();
        client.enviarMsg("segundo");
        clientes.add(client);
    }

    /**
     * manda msg para todos os carros conectados
     * @param msg string
     */
    public void replicarMsg(String msg) {
        for (Cliente c : clientes) {
            c.enviarMsg(msg);
        }
    }

    /**
     * manda msg para todos os carros conectados
     * @param msg objeto
     */
    public void replicarMsg(ArrayList<Object> msg) {
        for (Cliente c : clientes) {
            c.enviarMsg(msg);
        }
    }

    /**
     * Remove socket do carro apartir do ip
     * @param ip 
     */
    public void removerCliente(String ip) {
        try{
        for (Cliente c : clientes) {
            if (c.getIp().equals(ip)) {
                clientes.remove(c);
            }
        }
        }catch(ConcurrentModificationException e){
            this.removerCliente(ip);
        }
    }

    /**
     * metodo que pede uma lista de todos os ip que estão conectados na rede
     * @param ip 
     */
    public void primeiraConexao(String ip) {
        Cliente client = new Cliente(8080, ip);
        System.out.println("Me conectei com " + ip);
        new Thread(client).start();
        client.enviarMsg("primeiro");
        try {
            ArrayList<String> ips = (ArrayList<String>) client.getInput().readObject();
            for (String ipAtual : ips) {
                System.out.println(ipAtual);
                controller.iniciarConexao(ipAtual);
            }
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("Conexão perdida com o usuario");
        }
        clientes.add(client);//adiciona o usuario que passou a lista na lista de usuarios conectados
    }

}
