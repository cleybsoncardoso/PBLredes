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
    private int alturaTela = 480;
    private int larguraTela = 482;
    private int direcao;
    private float v = 0.3f;
    private ArrayList<Quadrante> trajeto;
    private int id;
    private String origem;
    private String destino;
    private Logica logica;

    public ControllerCarro(int id, String origem, String destino) {
        this.id = id;
        this.origem = origem;
        this.destino = destino;
        this.logica = new Logica(this);
        this.trajeto = logica.calcularTrajeto(origem, destino);
        this.setup();
    }

    public ControllerCarro(int id, int x, int y, int direcao) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.direcao = direcao;
        this.setup2();
    }

    private void setup2() {
        switch (direcao) {
            case 0:
                carro = new Carro(this.x, this.y, direcao);
                break;
            case 9:
                carro = new Carro(this.x, this.y, direcao);
                break;
            case 6:
                carro = new Carro(this.x, this.y, direcao);
                break;
            case 3:
                carro = new Carro(this.x, this.y, direcao);
                break;
        }
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
        ArrayList<Object> msg = new ArrayList<Object>();
        msg.add(x);
        msg.add(y);
        msg.add(direcao);
        msg.add(trajeto);
        Controller.getInstance().replicarMsg(msg);
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

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setXY(float x, float y, int direcao) {
        this.x = x;
        this.y = y;
        this.direcao = direcao;

        if ((y <= alturaTela / 2 - 35) && (direcao == 0)) {
            virarEsquerda();
        }
        if ((y <= alturaTela / 2 + 15) && (direcao == 0)) {
            virarDireita();
        }

        if ((y >= alturaTela / 2 - 35) && (direcao == 6)) {
            virarDireita();
        }
        if ((y >= alturaTela / 2 + 15) && (direcao == 6)) {
            virarEsquerda();
        }

        if ((x <= larguraTela / 2 + 18) && (direcao == 9)) {
            virarDireita();
        }
        if ((x <= larguraTela / 2 - 35) && (direcao == 9)) {
            virarEsquerda();
        }

        if ((x >= larguraTela / 2 + 18) && (direcao == 3)) {
            virarEsquerda();
        }
        if ((x >= larguraTela / 2 - 35) && (direcao == 3)) {
            virarDireita();
        }
    }

    public void setTrajeto(ArrayList<Quadrante> trajeto) {
        this.trajeto = trajeto;
    }

}
