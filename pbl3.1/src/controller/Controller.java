/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import model.Auxiliar;
import util.Quadrante;

/**
 *
 * @author paiva
 */
public class Controller {

    private static Controller controller;
    private Auxiliar auxiliar;
    private HashMap<String, ControllerCarro> carros;
    private int counter;
    private AtomicBoolean value;
    private ControllerCarro meuCarro;

    public Controller() {
        auxiliar = new Auxiliar(this);
        carros = new HashMap<>();
        counter = 0;
        value = new AtomicBoolean();
    }

    public static Controller novoController() {
        controller = new Controller();
        return controller;
    }

    public static Controller getInstance() {
        return controller;
    }

    public void replicarMsg(String msg) {
        auxiliar.replicarMsg(msg);
    }

    public void adicionarCarro(String origem, String destino) {
        ControllerCarro carro = new ControllerCarro(origem, destino);
        meuCarro = carro;
        //carros.put(key, carro);
       // counter++;
    }

    public void adicionarCarro(String key, float x, float y, int direcao, ArrayList<Quadrante> trajeto) {
        ControllerCarro carro = new ControllerCarro(x, y, direcao, trajeto);
        carros.put(key, carro);
        counter++;
    }

    public void removerCarro(String key) {

        try {
            carros.remove(key);
            counter--;
        } catch (ConcurrentModificationException ex) {
            this.removerCarro(key);
        }
    }
//
//    public ArrayList<ControllerCarro> getCarros() {
//        ArrayList<ControllerCarro> aux = new ArrayList<>();
//        for (int i = 0; i < this.counter; i++) {
//            aux.add(this.carros.get(i));
//        }
//        return aux;
//    }

    public ArrayList<ControllerCarro> getCarros() {
        ArrayList<ControllerCarro> aux = new ArrayList<>();
        Set<String> chaves = carros.keySet();
        for (String chave : chaves) {
            if (chave != null) {
                aux.add(carros.get(chave));
            }
        }
        return aux;
    }

    public ControllerCarro getCarro(String key) {
        try {
            return carros.get(key);
        } catch (Exception e) {
            this.getCarro(key);
        }
        return null;
    }
    
    public ControllerCarro getMeuCarro(){
        return meuCarro;
    }
}
