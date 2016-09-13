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
import view.Inicio;

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
    private boolean parado = false;

    public ControllerCarro(int id, String origem, String destino) {
        this.id = id;
        this.origem = origem;
        this.destino = destino;
        this.logica = new Logica(this);
        this.trajeto = logica.calcularTrajeto(origem, destino);
        Inicio.getInstance().mostrar("Iniciando Carro " + id + " em pista " + origem);
        this.setup();
    }

    public ControllerCarro(int id, float x, float y, int direcao, ArrayList<Quadrante> trajeto) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.direcao = direcao;
        this.trajeto = trajeto;
        Inicio.getInstance().mostrar("Iniciando Carro " + id + " em pista " + trajeto.get(0).getNome());
        this.setup2();
    }

    public int getId() {
        return this.id;
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
        this.origem = trajeto.get(0).getNome();
        this.destino = trajeto.get(trajeto.size() - 1).getNome();
    }

    private void setup() {
        switch (origem) {
            case "A":
                this.x = 257;
                this.y = alturaTela;
                this.direcao = 0;
                carro = new Carro(this.x, this.y, direcao);
                break;
            case "B":
                this.x = larguraTela;
                this.y = 208;
                this.direcao = 9;
                carro = new Carro(this.x, this.y, direcao);
                break;
            case "C":
                this.x = 210;
                this.y = 0;
                this.direcao = 6;
                carro = new Carro(this.x, this.y, direcao);
                break;
            case "D":
                this.x = 0;
                this.y = 257;
                this.direcao = 3;
                carro = new Carro(this.x, this.y, direcao);
                break;
        }
        this.msgMandar();
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
                        Inicio.getInstance().mostrar("carro " + id + " saindo da pista " + trajeto.get(0).getNome());
                        trajeto.remove(0);
                        Inicio.getInstance().mostrar("carro " + id + " entrando na pista " + trajeto.get(0).getNome());
                    }
                }
                y = y - v;
                carro.setXY(x, y);
                break;
            case 3:
                if (!trajeto.get(0).aindaQuadranteX(x)) {
                    if (trajeto.size() > 1) {
                        Inicio.getInstance().mostrar("carro " + id + " saindo da pista " + trajeto.get(0).getNome());
                        trajeto.remove(0);
                        Inicio.getInstance().mostrar("carro " + id + " entrando na pista " + trajeto.get(0).getNome());
                    }
                }
                x = x + v;
                carro.setXY(x, y);
                break;
            case 6:
                if (!trajeto.get(0).aindaQuadranteY(y)) {
                    if (trajeto.size() > 1) {
                        Inicio.getInstance().mostrar("carro " + id + " saindo da pista " + trajeto.get(0).getNome());
                        trajeto.remove(0);
                        Inicio.getInstance().mostrar("carro " + id + " entrando na pista " + trajeto.get(0).getNome());
                    }
                }
                y = y + v;
                carro.setXY(x, y);
                break;
            case 9:
                if (!trajeto.get(0).aindaQuadranteX(x)) {
                    if (trajeto.size() > 1) {
                        Inicio.getInstance().mostrar("carro " + id + " saindo da pista " + trajeto.get(0).getNome());
                        trajeto.remove(0);
                        Inicio.getInstance().mostrar("carro " + id + " entrando na pista " + trajeto.get(0).getNome());
                    }
                }
                x = x - v;
                carro.setXY(x, y);
                break;
        }
        this.msgMandar();

    }

    private void msgMandar() {

        ArrayList<Object> msg = new ArrayList<Object>();

        msg.add(x);
        msg.add(y);
        msg.add(direcao);
        msg.add(parado);
        msg.add(trajeto.size());
        for (Quadrante q : trajeto) {
            msg.add(q);
        }
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

    public boolean isParado() {
        return parado;
    }

    public void setParado(boolean parado) {
        this.parado = parado;
    }

    public void acao() {

        if (origem.equals("A") && this.y > 287) {
            andar();
        } else if (origem.equals("C") && this.y < 169) {
            andar();
        } else if (origem.equals("B") && this.x > 288) {
            andar();
        } else if (origem.equals("D") && this.x < 163) {
            andar();
        }
        if (!logica.conflito()) {
            if (origem.equals("A") && destino.equals("D")) {
                if ((y <= 208) && (direcao == 0)) {
                    virarEsquerda();
                } else {
                    andar();
                }
            } else if (origem.equals("A") && destino.equals("B")) {
                if ((y <= 257) && (direcao == 0)) {
                    virarDireita();
                } else {
                    andar();
                }
            } else if (origem.equals("A") && destino.equals("C")) {
                andar();
            }

            if (origem.equals("C") && destino.equals("D")) {
                if ((y >= 208) && (direcao == 6)) {
                    virarDireita();
                } else {
                    andar();
                }
            } else if (origem.equals("C") && destino.equals("B")) {
                if ((y >= 257) && (direcao == 6)) {
                    virarEsquerda();
                } else {
                    andar();
                }
            } else if (origem.equals("C") && destino.equals("A")) {
                andar();
            }

            if (origem.equals("B") && destino.equals("C")) {
                if ((x <= 257) && (direcao == 9)) {
                    virarDireita();
                } else {
                    andar();
                }
            } else if (origem.equals("B") && destino.equals("A")) {
                if ((x <= 210) && (direcao == 9)) {
                    virarEsquerda();
                } else {
                    andar();
                }
            } else if (origem.equals("B") && destino.equals("D")) {
                andar();
            }

            if (origem.equals("D") && destino.equals("C")) {
                if ((x >= 257) && (direcao == 3)) {
                    virarEsquerda();
                } else {
                    andar();
                }
            } else if (origem.equals("D") && destino.equals("A")) {
                if ((x >= 210) && (direcao == 3)) {
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
        carro.setXY(x, y, direcao);

    }

    public void setTrajeto(ArrayList<Quadrante> trajeto) {
        this.trajeto = trajeto;
    }

}
