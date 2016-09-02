/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Auxiliar;

/**
 *
 * @author paiva
 */
public class Controller {

    private Auxiliar auxiliar;
    private ArrayList<String> ips;
    private ArrayList<ControllerCarro> carros;
    private String meuIp;

    public Controller(String meuIp) {
        auxiliar = new Auxiliar(this);
        ips = new ArrayList<>();
        this.meuIp=meuIp;
    }

    public void primeiraConexao(String ip) {
        auxiliar.primeiraConexao(ip);
        ips.add(ip);
    }

    public void iniciarConexao(String ip) {
        System.out.println("recebi: " + ip);
        if (verificaIp(ip)) {
            System.err.println("acho q agora foi");
            auxiliar.iniciarConexao(ip);
            ips.add(ip);
        }
    }

    public void removerIp(String ip) {
        ips.remove(ip);
        auxiliar.removerCliente(ip);
    }

    private boolean verificaIp(String ip) {

        for (String ipAtual : ips) {
            if (ipAtual.equals(ip)) {
                return false;
            }
        }
        return true;
    }

    public ArrayList<String> getIps() {
        return this.ips;
    }

    public void replicarMsg(String msg) {
        auxiliar.replicarMsg(msg);
    }

    public void adicionarCarro(int id) {
        ControllerCarro c = new ControllerCarro(id, 480, 482);
        carros.add(id, c);
    }

    public ArrayList<ControllerCarro> getCarros() {
        return this.carros;
    }
}
