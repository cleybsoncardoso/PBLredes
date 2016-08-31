/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author cleyb
 */
public class Quadrante {
    private String nome;
    private Coordenada coordenada;
    private int turn;

    public Quadrante(String nome, int x, int y, int turn) {
        coordenada = new Coordenada(x, y);
        this.turn = turn;
    }

    public String getNome() {
        return nome;
    }

    public Coordenada getCoordenada() {
        return coordenada;
    }
    public void PassouTurn(){
        this.turn-=this.turn;
    }
    
}
