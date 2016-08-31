/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.net.ServerSocket;
import java.util.ArrayList;
import model.Auxiliar;
import model.Carro;

/**
 *
 * @author cleyb
 */
public class Controller {
    private ArrayList<Carro> carros;
    private Auxiliar auxiliar;
    private ArrayList<String> listaIps;

    public Controller() {
        carros = new ArrayList<Carro>();
        auxiliar = new Auxiliar();
        listaIps = new ArrayList<>();
    }
    
    public void iniciarConexão(){
        auxiliar.primeiraConexao("127.0.0.1",8080);
    }

    public ArrayList<Carro> getCarros() {
        return carros;
    }
    
    public void addConexao(String ip){
        auxiliar.addSocket(ip, 8080);
    }  
    
    public void addIp(String ip){
        listaIps.add(ip);
    }
    
    public ArrayList<String> getIpList(){
        return listaIps;
    }
    
    public void replica(String msg){
        auxiliar.replica(msg);
    }
    
}
