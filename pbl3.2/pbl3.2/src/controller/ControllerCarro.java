/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import util.Carro;
import util.Logica;
import util.Quadrante;

/**
 *
 * @author paiva
 */
public class ControllerCarro {

    private float x;
    private float y;
    public Carro carro;
    private int alturaTela;
    private int larguraTela;
    private int direcao;
    private float v = 0.3f;
    private ArrayList<Quadrante> trajeto;
    private int id;
    private String origem;
    private String destino;
    private Logica logica;

    public ControllerCarro(int id, int screenWidth, int screenHeight, String origem, String destino) {
        this.id = id;
        this.origem = origem;
        this.destino = destino;
        this.alturaTela = screenHeight;
        this.larguraTela = screenWidth;
        this.logica = new Logica(this);
        this.trajeto = logica.calcularTrajeto(origem, destino);
        this.setup();
    }

    private void setup() {
        switch (origem) {
            case "A":
                this.x = larguraTela / 2 + 18;
                this.y = alturaTela;
                this.direcao = 0;
                carro = new Carro(this.x, this.y, direcao);
                break;
            case "B":
                this.x = larguraTela;
                this.y = alturaTela / 2 - 35;
                this.direcao = 9;
                carro = new Carro(this.x, this.y, direcao);
                break;
            case "C":
                this.x = larguraTela / 2 - 35;
                this.y = 0;
                this.direcao = 6;
                carro = new Carro(this.x, this.y, direcao);
                break;
            case "D":
                this.x = 0;
                this.y = alturaTela / 2 + 15;
                this.direcao = 3;
                carro = new Carro(this.x, this.y, direcao);
                break;
        }
    }

    public void desenhar(Graphics2D g2d) {
        g2d.setColor(Color.RED);
        g2d.fill(carro.draw());

    }

    public ArrayList<Quadrante> getTrajeto() {
        return trajeto;
    }

    public void andar() {
        switch (direcao) {
            case 0:
                if (!trajeto.get(0).aindaQuadranteY(y)) {
                    if (trajeto.size() > 1) {
                        trajeto.remove(0);
                    }
                }
                y = y - v;
                carro.setXY(x, y);
                break;
            case 3:
                if (!trajeto.get(0).aindaQuadranteX(x)) {
                    if (trajeto.size() > 1) {
                        trajeto.remove(0);
                    }
                }
                x = x + v;
                carro.setXY(x, y);
                break;
            case 6:
                if (!trajeto.get(0).aindaQuadranteY(y)) {
                    if (trajeto.size() > 1) {
                        trajeto.remove(0);
                    }
                }
                y = y + v;
                carro.setXY(x, y);
                break;
            case 9:
                if (!trajeto.get(0).aindaQuadranteX(x)) {
                    if (trajeto.size() > 1) {
                        trajeto.remove(0);
                    }
                }
                x = x - v;
                carro.setXY(x, y);
                break;
        }

        System.out.println(trajeto.get(0).getNome());

    }

    public void virarEsquerda() {
        direcao = direcao - 3;
        if (direcao == -3) {
            direcao = 9;
        }
        carro.virar();
    }

    public void virarDireita() {
        direcao = direcao + 3;
        if (direcao == 12) {
            direcao = 0;
        }
        carro.virar();
    }

    public void acao() {

        if (origem.equals("A") && this.y > 287) {
            andar();
        } else if (origem.equals("C") && this.y < 161) {
            andar();
        } else if (origem.equals("B") && this.x > 288) {
            andar();
        } else if (origem.equals("D") && this.x < 163) {
            andar();
        } else if (!logica.conflito()) {
            if (origem.equals("A") && destino.equals("D")) {
                if ((y <= alturaTela / 2 - 35) && (direcao == 0)) {
                    virarEsquerda();
                } else {
                    andar();
                }
            } else if (origem.equals("A") && destino.equals("B")) {
                if ((y <= alturaTela / 2 + 15) && (direcao == 0)) {
                    virarDireita();
                } else {
                    andar();
                }
            } else if (origem.equals("A") && destino.equals("C")) {
                andar();
            }

            if (origem.equals("C") && destino.equals("D")) {
                if ((y >= alturaTela / 2 - 35) && (direcao == 6)) {
                    virarDireita();
                } else {
                    andar();
                }
            } else if (origem.equals("C") && destino.equals("B")) {
                if ((y >= alturaTela / 2 + 15) && (direcao == 6)) {
                    virarEsquerda();
                } else {
                    andar();
                }
            } else if (origem.equals("C") && destino.equals("A")) {
                andar();
            }

            if (origem.equals("B") && destino.equals("C")) {
                if ((x <= larguraTela / 2 + 18) && (direcao == 9)) {
                    virarDireita();
                } else {
                    andar();
                }
            } else if (origem.equals("B") && destino.equals("A")) {
                if ((x <= larguraTela / 2 - 35) && (direcao == 9)) {
                    virarEsquerda();
                } else {
                    andar();
                }
            } else if (origem.equals("B") && destino.equals("D")) {
                andar();
            }

            if (origem.equals("D") && destino.equals("C")) {
                if ((x >= larguraTela / 2 + 18) && (direcao == 3)) {
                    virarEsquerda();
                } else {
                    andar();
                }
            } else if (origem.equals("D") && destino.equals("A")) {
                if ((x >= larguraTela / 2 - 35) && (direcao == 3)) {
                    virarDireita();
                } else {
                    andar();
                }
            } else if (origem.equals("D") && destino.equals("B")) {
                andar();
            }
        }

    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

}
