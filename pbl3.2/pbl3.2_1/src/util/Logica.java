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

    public Logica(ControllerCarro meuCarro) {
        this.meuCarro = meuCarro;
        this.controller = Controller.getInstance();

    }

    public boolean conflito() {
        if (controller.getCarros().size() > 1) {
            for (int l = 0; l < controller.getCarros().size(); l++) {
                ControllerCarro carroAtual = Controller.getInstance().getCarros().get(l);
                int j = 0;
                //verifica qual o maior trajeto, para basear se vai ter conflito
                if (meuCarro.getTrajeto().size() > carroAtual.getTrajeto().size()) {
                    j = carroAtual.getTrajeto().size();
                } else {
                    j = meuCarro.getTrajeto().size();
                }
                //busca direta, para verificar se vai ter conflito
                if (carroAtual.getId() != meuCarro.getId()) {
                    for (int i = 0; i < j; i++) {
                        System.err.println("Comparando Meu: " + meuCarro.getId() + " " + meuCarro.getTrajeto().get(i).getNome() + "teu: " + carroAtual.getId() + " " + carroAtual.getTrajeto().get(i).getNome());
                        if (carroAtual.getTrajeto().get(i).getNome().equals(meuCarro.getTrajeto().get(i).getNome())) {
                            if (!carroAtual.getTrajeto().get(0).getNome().equals("A") || !carroAtual.getTrajeto().get(0).getNome().equals("B") || !carroAtual.getTrajeto().get(0).getNome().equals("C") || !carroAtual.getTrajeto().get(0).getNome().equals("D")) {
                                System.out.println("Deu true");
                                if (carroAtual.isParado() == true) {
                                    meuCarro.setParado(false);
                                    return false;
                                } else {
                                    meuCarro.setParado(true);
                                    return true;
                                }
                            }
                        }
                    }

                    System.out.println("Rota de tua: ");
                    for (Quadrante q : carroAtual.getTrajeto()) {
                        System.out.println(q.getNome());
                    }
                    System.out.println("Rota de minha: ");
                    for (Quadrante q : meuCarro.getTrajeto()) {
                        System.out.println(q.getNome());
                    }

                }
            }
        }
        return false;
    }

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
