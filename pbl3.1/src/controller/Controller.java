/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
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
    private ArrayList<ControllerCarro> carros;
    private int counter;
    private AtomicBoolean value;

    public Controller() {
        auxiliar = new Auxiliar(this);
        carros = new ArrayList<>();
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


    public void adicionarCarro(int id, String origem, String destino) {
        ControllerCarro c = new ControllerCarro(id, origem, destino);
        carros.add(c);
        counter++;
    }

    public void adicionarCarro(int id, float x, float y, int direcao, ArrayList<Quadrante> trajeto) {
        ControllerCarro c = new ControllerCarro(id, x, y, direcao, trajeto);
        carros.add(c);
        counter++;
    }

    public void removerCarro(int id) {
        try {
            for (ControllerCarro c : carros) {
                if (c.getId() == id) {
                    carros.remove(c);
                    counter--;
                }
            }
        } catch (ConcurrentModificationException ex) {
            this.removerCarro(id);
        }
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
        try {
            for (ControllerCarro c : carros) {
                if (c.getId() == id) {
                    return c;
                }
            }
            return null;
        } catch (Exception e) {
            this.getCarro(id);
        }
        return null;
    }
}
