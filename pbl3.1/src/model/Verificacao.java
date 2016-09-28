/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import controller.Controller;
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
                Thread.sleep(3000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Verificacao.class.getName()).log(Level.SEVERE, null, ex);
            }
            Set<String> chaves = carrosOnline.keySet();
            for (String chave : chaves) {
                Date timeAtual = new Date();
                long ultimaAtualizacao = (long) carrosOnline.get(chave);
                if (timeAtual.getTime() - ultimaAtualizacao > 3000) {
                    carrosOnline.remove(chave);
                }

            }
        }
    }

}
