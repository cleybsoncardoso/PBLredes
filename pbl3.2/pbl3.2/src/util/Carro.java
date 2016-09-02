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

    private static final float SPEED = 200; //Velocidade em 20 pixels / segundo
    private static int WIDTH = 20;
    private static int HEIGHT = 30;

    private int x;
    private int y;

    private float vx = SPEED;
    private float vy = SPEED / 2;

    private int screenWidth;
    private int screenHeight;

    public Rectangle2D car;

    public Carro(int screenWidth, int screenHeight) {
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
        car = new Rectangle2D.Float(0, 0, WIDTH, HEIGHT);
    }

    public Carro(int screenWidth, int screenHeight, int x, int y) {
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
        this.x = x;
        this.y = y;
        car = new Rectangle2D.Float(x, y, WIDTH, HEIGHT);
    }

    public Rectangle2D draw() {
        return car;
    }

    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
        if (car == null) {
            car = new Rectangle2D.Float(0, 0, WIDTH, HEIGHT);
        }
        car.setRect(this.x, this.y, WIDTH, HEIGHT);
    }

    public void virar(){
        int aux = HEIGHT;
        HEIGHT = WIDTH;
        WIDTH = aux;
        car.setRect(this.x, this.y, HEIGHT, WIDTH);
        System.out.println("virou");
    }
}
