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
    private ArrayList<ControllerCarro> carrosList;
    private int counter;
    private AtomicBoolean value;
    private ControllerCarro meuCarro;

    public Controller() {
        auxiliar = new Auxiliar(this);
        carros = new HashMap<>();
        counter = 0;
        value = new AtomicBoolean();
        carrosList = new ArrayList<>();
    }

    /**
     * Método que cria uma nova instancia da classe controller.
     *
     * @return
     */
    public static Controller novoController() {
        controller = new Controller();
        return controller;
    }

    /**
     * Método que retorna a instancia atual da classe controller.
     *
     * @return
     */
    public static Controller getInstance() {
        return controller;
    }

    /**
     * Método que envia mensagem para grupo de multicast.
     *
     * @param msg
     */
    public void replicarMsg(String msg) {
        auxiliar.replicarMsg(msg);
    }

    /**
     * Método que adiciona carro principal, ou seja, carro do usuário desse
     * host.
     *
     * @param origem
     * @param destino
     */
    public void adicionarCarro(String origem, String destino) {
        ControllerCarro carro = new ControllerCarro(origem, destino);
        meuCarro = carro;
        carrosList.add(meuCarro);
        //carros.put(key, carro);
        counter++;
    }

    /**
     * Método que adiciona veículos de outros usuários (outros hosts) na tela do
     * atual usuário
     *
     * @param key
     * @param x
     * @param y
     * @param direcao
     * @param trajeto
     */
    public void adicionarCarro(String key, float x, float y, int direcao, ArrayList<Quadrante> trajeto) {
        ControllerCarro carro = new ControllerCarro(x, y, direcao, trajeto);
        carros.put(key, carro);
        carrosList.add(carro);
        counter++;
    }

    /**
     * Método que remove veículos da tela utilizando a key.
     *
     * @param key
     */
    public void removerCarro(String key) {

        try {
            carrosList.remove(carros.remove(key));
            counter--;
        } catch (ConcurrentModificationException ex) {
            this.removerCarro(key);
        }
    }

    /**
     * Método que remover o carro da tela através do controllerCarro.
     *
     * @param carro
     */
    public void removerCarro(ControllerCarro carro) {
        try {
            carrosList.remove(carros.remove(carro));
            counter--;
        } catch (ConcurrentModificationException ex) {
            this.removerCarro(carro);
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

    /**
     * Método que retorna um ArrayList contendo todos os carros. Os carros são
     * passados do HashMap para um ArrayList.
     *
     * @return
     */
    public ArrayList<ControllerCarro> getCarros() {
//        ArrayList<ControllerCarro> aux = new ArrayList<>();
//        aux.add(meuCarro);
//        Set<String> chaves = carros.keySet();
//        for (String chave : chaves) {
//            if (chave != null) {
//                aux.add(carros.get(chave));
//            }
//        }
//        return aux;
        ArrayList<ControllerCarro> aux = new ArrayList<>();
        for (int i = 0; i < this.counter; i++) {
            aux.add(this.carrosList.get(i));
        }
        return aux;
    }

    /**
     * Método que retorna um carro de acordo com sua key.
     */
    public ControllerCarro getCarro(String key) {
        try {
            return carros.get(key);
        } catch (Exception e) {
            this.getCarro(key);
        }
        return null;
    }

    /**
     * Método que retorna o carro do usuário.
     *
     * @return
     */
    public ControllerCarro getMeuCarro() {
        return meuCarro;
    }
}
