/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.ArrayList;
import model.Auxiliar;

/**
 *
 * @author paiva
 */
public class Controller {

    private Auxiliar auxiliar;
    private ArrayList<String> ips;

    public Controller() {
        auxiliar = new Auxiliar();
        ips = new ArrayList<>();
    }

    public void iniciarConexao(String ip) {
        if (verificaIp(ip)) {
            auxiliar.iniciarConexao(ip);
            ips.add(ip);
        }
    }
    
    public void removerIp(String ip){
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

    public void replicarMsg(String msg) {
        auxiliar.replicarMsg(msg);
    }
}
