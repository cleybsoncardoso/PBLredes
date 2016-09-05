/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;


/**
 *
 * @author cleyb
 */
public class Quadrante {
    private String nome;
    private Coordenada coordenada;

    public Quadrante(String nome, int x, int y, int turn) {
        this.nome=nome;
        coordenada = new Coordenada(x, y);
    }

    public String getNome() {
        return nome;
    }

    public Coordenada getCoordenada() {
        return coordenada;
    }

    
}
