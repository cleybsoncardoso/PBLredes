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
 * Classe responsavel por tratar as logicas relacionadas a imagem, em que traça o trajeto do carro e verifica se tem conflito
 * @author cleybson e lucas
 */
public class Logica {

    private Controller controller;
    private ControllerCarro meuCarro;
    private boolean conflito = false;

    public Logica(ControllerCarro meuCarro) {
        this.meuCarro = meuCarro;
        this.controller = Controller.getInstance();

    }

    /**
     * Classe que verifica se tem ou não conflito
     * @return 
     */
    public boolean conflito() {
        if (controller.getCarros().size() > 1) {//verifica se tem carro conectado
            for (int l = 0; l < controller.getCarros().size(); l++) {//caso tenha, 
                ControllerCarro carroAtual = Controller.getInstance().getCarros().get(l);
                int j = 0;
                //verifica qual o maior trajeto, para basear se vai ter conflito
                if (meuCarro.getTrajeto().size() > carroAtual.getTrajeto().size()) {
                    j = carroAtual.getTrajeto().size();
                } else {
                    j = meuCarro.getTrajeto().size();
                }
                //busca direta, para verificar se vai ter conflito
                if (carroAtual.getId() != meuCarro.getId()) {//verifica se o carro não é o meu
                    conflito = false;
                    for (int i = 0; i < j; i++){//percorre carro por carro, analisando se o carro ja esta no cruzamento ou se ta na via principal
                        if (carroAtual.getTrajeto().get(i).getNome().equals(meuCarro.getTrajeto().get(i).getNome())) {
                            if (carroAtual.getTrajeto().get(0).getNome().equals("A") || carroAtual.getTrajeto().get(0).getNome().equals("B") || carroAtual.getTrajeto().get(0).getNome().equals("C") || carroAtual.getTrajeto().get(0).getNome().equals("D")) {
                            } else if (carroAtual.noCruzamento() == true) {
                                conflito = true;
                            }
                        }
                    }

                    return conflito;

                }
            }
        }
        return false;
    }

    /**
     * Calcula apartir da origem e do destino, qual o trajeto a ser feito
     * @param origem
     * @param destino
     * @return 
     */
    public ArrayList<Quadrante> calcularTrajeto(String origem, String destino) {
        ArrayList<Quadrante> trajeto = new ArrayList<Quadrante>();
        switch (origem) {
            case "A":
                trajeto.add(new Quadrante("A"));
                trajeto.add(new Quadrante("d"));
                if (destino.equals("B")) {
                    trajeto.add(new Quadrante("B"));
                    return trajeto;
                } else if (destino.equals("C")) {
                    trajeto.add(new Quadrante("b"));
                    trajeto.add(new Quadrante("C"));
                    return trajeto;
                } else {
                    trajeto.add(new Quadrante("b"));
                    trajeto.add(new Quadrante("a"));
                    trajeto.add(new Quadrante("D"));
                    return trajeto;
                }
            case "B":
                trajeto.add(new Quadrante("B"));
                trajeto.add(new Quadrante("b"));
                if (destino.equals("C")) {
                    trajeto.add(new Quadrante("C"));
                    return trajeto;
                } else if (destino.equals("D")) {
                    trajeto.add(new Quadrante("a"));
                    trajeto.add(new Quadrante("D"));
                    return trajeto;
                } else {
                    trajeto.add(new Quadrante("a"));
                    trajeto.add(new Quadrante("c"));
                    trajeto.add(new Quadrante("A"));
                    return trajeto;
                }
            case "C":
                trajeto.add(new Quadrante("C"));
                trajeto.add(new Quadrante("a"));
                if (destino.equals("D")) {
                    trajeto.add(new Quadrante("D"));
                    return trajeto;
                } else if (destino.equals("A")) {
                    trajeto.add(new Quadrante("c"));
                    trajeto.add(new Quadrante("A"));
                    return trajeto;
                } else {
                    trajeto.add(new Quadrante("c"));
                    trajeto.add(new Quadrante("d"));
                    trajeto.add(new Quadrante("B"));
                    return trajeto;
                }
            default:
                trajeto.add(new Quadrante("D"));
                trajeto.add(new Quadrante("c"));
                if (destino.equals("A")) {
                    trajeto.add(new Quadrante("A"));
                    return trajeto;
                } else if (destino.equals("B")) {
                    trajeto.add(new Quadrante("d"));
                    trajeto.add(new Quadrante("B"));
                    return trajeto;
                } else {
                    trajeto.add(new Quadrante("d"));
                    trajeto.add(new Quadrante("b"));
                    trajeto.add(new Quadrante("C"));
                    return trajeto;
                }
        }
    }

}
