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

/**
 * classe que auxilia o controller
 * @author cleybson e lucas
 */
public class Auxiliar {

    private Controller controller;

    public Auxiliar(Controller controller) {
        this.controller = controller;
    }



    /**
     * manda msg para todos os carros conectados
     * @param msg objeto
     */
    public void replicarMsg(String msg) {
        Multicast.getInstancia().enviarMensagem(msg);
    }


}
