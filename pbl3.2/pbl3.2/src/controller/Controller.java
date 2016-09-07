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

    private static Controller controller;
    private Auxiliar auxiliar;
    private ArrayList<String> ips;
    private ArrayList<ControllerCarro> carros;
    private int counter;
    private String meuIp;

    public Controller(String meuIp) {
        auxiliar = new Auxiliar(this);
        ips = new ArrayList<>();
        carros = new ArrayList<>();
        counter = 0;
        this.meuIp = meuIp;
    }

    public static Controller novoController(String ip) {
        controller = new Controller(ip);
        return controller;
    }

    public static Controller getInstance() {
        return controller;
    }

    public void primeiraConexao(String ip) {
        ips.add(ip);
        auxiliar.primeiraConexao(ip);
    }

    public void iniciarConexao(String ip) {
        if (verificaIp(ip)) {
            ips.add(ip);
            auxiliar.iniciarConexao(ip);
        }
    }

    public void removerIp(String ip) {
//        ips.remove(ip);
//        auxiliar.removerCliente(ip);
    }

    private boolean verificaIp(String ip) {
        System.out.println("tamanho = " + ips.size());
        for (String ipAtual : ips) {
            if (ipAtual.equals(ip) || ipAtual.equals(meuIp)) {
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

    public void replicarMsg(ArrayList<Object> msg) {
        auxiliar.replicarMsg(msg);
    }

    public void adicionarCarro(int id, String origem, String destino) {
        ControllerCarro c = new ControllerCarro(id, origem, destino);
        carros.add(c);
        counter++;
    }

    public void adicionarCarro(int id, float x, float y, int direcao) {
        ControllerCarro c = new ControllerCarro(id, x, y, direcao);
        carros.add(c);
        counter++;
    }

    public ArrayList<ControllerCarro> getCarros() {
        ArrayList<ControllerCarro> aux = new ArrayList<>();
        for (int i = 0; i < this.counter; i++) {
            aux.add(this.carros.get(i));
        }
        return aux;
    }

    public ArrayList<ControllerCarro> getCarros1() {
        ArrayList<ControllerCarro> aux = new ArrayList<>();
        for (int i = 1; i < this.counter; i++) {
            aux.add(this.carros.get(i));
        }
        return aux;
    }

    public ControllerCarro getCarro(int id) {
        
        for(ControllerCarro c : carros){
            if(c.getId()==id){
                return c;
            }
        }
        return null;
    }

}
