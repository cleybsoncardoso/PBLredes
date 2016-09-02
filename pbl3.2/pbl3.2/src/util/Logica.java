/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import controller.Controller;
import controller.ControllerCarro;
import java.util.ArrayList;

/**
 *
 * @author paiva
 */
public class Logica {

    private Controller controller;
    private ControllerCarro meuCarro;
    
    public boolean conflito(){
        
        return false;
    }
    
    public ArrayList<Quadrante> calcularTrajeto(String origem, String destino) {
        ArrayList<Quadrante> trajeto = new ArrayList<Quadrante>();
        if (origem.equals("A")) {
            trajeto.add(new Quadrante("d", 0, 0, 1));
            if (destino.equals("B")) {
                return trajeto;
            } else if (destino.equals("C")) {
                trajeto.add(new Quadrante("b", 0, 0, 2));
                return trajeto;
            } else {
                trajeto.add(new Quadrante("b", 0, 0, 2));
                trajeto.add(new Quadrante("a", 0, 0, 3));
                return trajeto;
            }
        } else if (origem.equals("B")) {
            trajeto.add(new Quadrante("b", 0, 0, 1));
            if (destino.equals("C")) {
                return trajeto;
            } else if (destino.equals("D")) {
                trajeto.add(new Quadrante("a", 0, 0, 2));
                return trajeto;
            } else {
                trajeto.add(new Quadrante("a", 0, 0, 2));
                trajeto.add(new Quadrante("c", 0, 0, 3));
                return trajeto;
            }
        } else if (origem.equals("C")) {
            trajeto.add(new Quadrante("a", 0, 0, 1));
            if (destino.equals("D")) {
                return trajeto;
            } else if (destino.equals("A")) {
                trajeto.add(new Quadrante("c", 0, 0, 2));
                return trajeto;
            } else {
                trajeto.add(new Quadrante("c", 0, 0, 2));
                trajeto.add(new Quadrante("d", 0, 0, 3));
                return trajeto;
            }
        } else {
            trajeto.add(new Quadrante("c", 0, 0, 1));
            if (destino.equals("A")) {
                return trajeto;
            } else if (destino.equals("B")) {
                trajeto.add(new Quadrante("d", 0, 0, 2));
                return trajeto;
            } else {
                trajeto.add(new Quadrante("d", 0, 0, 2));
                trajeto.add(new Quadrante("b", 0, 0, 3));
                return trajeto;
            }
        }
    }
    
}
