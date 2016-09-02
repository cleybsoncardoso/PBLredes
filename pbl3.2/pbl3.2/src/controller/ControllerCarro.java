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
import util.Quadrante;

/**
 *
 * @author paiva
 */
public class ControllerCarro {

    private int x;
    private int y;
    public Carro carro;
    private int alturaTela;
    private int larguraTela;
    private int direcao;
    private int v = 5;
    private ArrayList<Quadrante> trajeto;
    private int id;

    public ControllerCarro(int id, int screenWidth, int screenHeight) {
        this.id = id;
        this.alturaTela = screenHeight;
        this.larguraTela = screenWidth;
        this.x = screenHeight / 2 + 18;
        this.y = screenWidth;
        carro = new Carro(screenWidth, screenHeight, this.x, this.y);
        direcao = 0;
    }

    public void desenhar(Graphics2D g2d) {
        //Graphics2D g1 = (Graphics2D) g2d.create();
        //g1.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.RED);
        g2d.fill(carro.draw());
        //g1.dispose();
    }

    public ArrayList<Quadrante> getTrajeto() {
        return trajeto;
    }

    public void andar() {

        switch (direcao) {
            case 0:
                y = y - v;
                carro.setXY(x, y);
                break;
            case 3:
                x = x + v;
                carro.setXY(x, y);
                break;
            case 6:
                y = y + v;
                carro.setXY(x, y);
                break;
            case 9:
                x = x - v;
                carro.setXY(x, y);
                break;
        }
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
        if ((y <= alturaTela / 2 - 25) && (direcao == 0)) {
            virarEsquerda();
        } else {
            andar();
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
