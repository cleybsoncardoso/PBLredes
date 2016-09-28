/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import controller.Controller;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.TrataCliente;

/**
 *
 * @author cleyse
 */
public class Verificacao implements Runnable {

    private HashMap carrosOnline;
    private TrataCliente trataCliente;

    public Verificacao(TrataCliente aThis) {
        trataCliente = aThis;
        carrosOnline = new HashMap();

    }

    public void atualiza(String chave) {
        Date time = new Date();
        carrosOnline.put(chave, time.getTime());
    }

    @Override
    public void run() {
        while (true) {
            try {
                Set<String> chaves = carrosOnline.keySet();
                for (String chave : chaves) {
                    Date timeAtual = new Date();
                    long ultimaAtualizacao = (long) carrosOnline.get(chave);
                    System.out.println("tempo: " + (timeAtual.getTime() - ultimaAtualizacao));
//                if (timeAtual.getTime() - ultimaAtualizacao > 3000) {
//                    carrosOnline.remove(chave);
//                }

                }

            } catch (ConcurrentModificationException e) {

            }
        }
    }

}
