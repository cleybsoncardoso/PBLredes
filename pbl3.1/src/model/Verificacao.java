/*
 * Classe que verifica o timeout dos carros
 */
package model;

import controller.Controller;
import java.net.InetAddress;
import java.net.UnknownHostException;
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

    private HashMap carrosOnline; //hashmap que armazena o ultimo momento da mensagem de cada carro
    private TrataCliente trataCliente;
    private Controller controller;
    private String meuIP;

    
    public Verificacao(TrataCliente trataCliente) {
        this.trataCliente = trataCliente;
        carrosOnline = new HashMap();
        controller = Controller.getInstance();
        try {
            this.meuIP = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException ex) {
            Logger.getLogger(Verificacao.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Atualiza o ultimo momento da mensagem do carro com a determinada chave
     * @param chave 
     */
    public void atualiza(String chave) {
        Date time = new Date();
        carrosOnline.put(chave, time.getTime());
    }

    @Override
    public void run() {
        while (true) {
            try {
                Set<String> chaves = carrosOnline.keySet();
                Date timeAtual = new Date();
                if (chaves.size() > 0) {
                    for (String chave : chaves) {
                        long ultimaAtualizacao = (long) carrosOnline.get(chave);
                        //verifica se o tempo ja é maior do que o determinado
                        if (timeAtual.getTime() - ultimaAtualizacao > 5000) {
                            System.out.println("Tempo de comunicação excedido");
                            carrosOnline.remove(chave);
                            controller.removerCarro(chave);
                            trataCliente.getChavesips().remove(chave);
                        }
                    }
                }

                if (!meuIP.equals(InetAddress.getLocalHost().getHostAddress())) {
                    System.out.println("Conexão perdida, o sistema não pode ser utilizado");
                    System.exit(0);
                }

            } catch (ConcurrentModificationException e) {

            } catch (UnknownHostException ex) {
                Logger.getLogger(Verificacao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
