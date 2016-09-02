/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.ArrayList;
import java.util.HashMap;
import model.Auxiliar;
import model.Carro2;

/**
 *
 * @author paiva
 */
public class Controller {

    private Auxiliar auxiliar;
    private ArrayList<String> ips;
    private HashMap<Integer, Carro2> carros;

    public Controller() {
        auxiliar = new Auxiliar(this);
        ips = new ArrayList<>();
    }

    public void primeiraConexao(String ip){
        auxiliar.primeiraConexao(ip);
        ips.add(ip);
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
    
    public ArrayList<String> getIps(){
        return this.ips;
    }

    public void replicarMsg(String msg) {
        auxiliar.replicarMsg(msg);
    }
    
    public void adicionarCarro(int id){
    }
}
