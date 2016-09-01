/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import model.Auxiliar;

/**
 *
 * @author paiva
 */
public class Controller {

    Auxiliar auxiliar;

    public Controller() {
        auxiliar = new Auxiliar();
    }

    public void iniciarConexao(String string) {
        auxiliar.iniciarConexao(string);
    }

    public void replicarMsg(String msg) {
        auxiliar.replicarMsg(msg);
    }
}
