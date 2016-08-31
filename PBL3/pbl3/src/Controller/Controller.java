/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.net.ServerSocket;
import java.util.ArrayList;
import model.Carro;

/**
 *
 * @author cleyb
 */
public class Controller {
    private ArrayList<Carro> carros;

    public Controller() {
        carros = new ArrayList<Carro>();
    }
    
    

    public ArrayList<Carro> getCarros() {
        return carros;
    }
    

    
    
}
