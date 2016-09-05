/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.awt.geom.Rectangle2D;

/**
 *
 * @author paiva
 */
public class Carro {

    private int WIDTH = 20;
    private int HEIGHT = 30;

    private int x;
    private int y;

    private Rectangle2D car;

    public Carro(int x, int y, int direcao) {
        this.x = x;
        this.y = y;
        if (direcao == 3 || direcao == 9) {
            int aux = HEIGHT;
            HEIGHT = WIDTH;
            WIDTH = aux;
        }
        car = new Rectangle2D.Float(x, y, WIDTH, HEIGHT);
    }

    public Rectangle2D draw() {
        return car;
    }

    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
        car.setRect(this.x, this.y, WIDTH, HEIGHT);
    }

    public void virar() {
        int aux = HEIGHT;
        HEIGHT = WIDTH;
        WIDTH = aux;
        car.setRect(this.x, this.y, HEIGHT, WIDTH);
        System.out.println("virou");
    }
}
