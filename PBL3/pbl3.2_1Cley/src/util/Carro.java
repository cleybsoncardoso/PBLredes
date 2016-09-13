/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.awt.geom.Rectangle2D;

/**
 * Classe que possue todos os cados de cada carro
 * @author paiva
 */
public class Carro {

    private int WIDTH = 15, HEIGHT = 20;
    //tamanho da altura e largura do carro, que é padrão para todos
    private double x, y;

    private Rectangle2D car;

    /**
     * Construtor, que além de colocar o x e o y do carro, verifica a direção do carro para mudar a forma do retangulo, para dar a impressão do carro estar indo reto(iniciado em A ou C) ou para os lado (B ou D)
     * @param x 
     * @param y
     * @param direcao 
     */
    public Carro(float x, float y, int direcao) {
        this.x = x;
        this.y = y;
        if (direcao == 3 || direcao == 9) {
            int aux = HEIGHT;
            HEIGHT = WIDTH;
            WIDTH = aux;
        }
        car = new Rectangle2D.Float(x, y, WIDTH, HEIGHT);
    }
    
    //retorna o retangulo do carro
    public Rectangle2D getRect() {
        return car;
    }

    public void setXY(double x, double y) {
        this.x = x;
        this.y = y;
        car.setRect(this.x, this.y, WIDTH, HEIGHT);
    }

    public void setXY(double x, double y, int direcao) {
        this.x = x;
        this.y = y;
        if (direcao == 3 || direcao == 9) {
            HEIGHT = 15;
            WIDTH = 20;
        }else{
            HEIGHT = 20;
            WIDTH = 15;
        }
        car.setRect(this.x, this.y, WIDTH, HEIGHT);
    }

    //altera a forma do retangulo, para da a impressão dele estar indo para outra direção
    public void virar() {
        int aux = HEIGHT;
        HEIGHT = WIDTH;
        WIDTH = aux;
        car.setRect(this.x, this.y, HEIGHT, WIDTH);
    }
}
