/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import controller.Controller;
import model.Multicast;
import util.TrataCliente;
import view.Inicio;

/**
 *
 * @author paiva
 */
public class App {

    public static void main(String[] args) {

        Controller controller = Controller.novoController();
        Inicio telaInicial = Inicio.novoController(controller);
        Multicast.novaInstancia();
        new Thread(new TrataCliente()).start();

    }
}
