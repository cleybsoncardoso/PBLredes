/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author paiva
 */
public class Adicao extends Modificacao {

    private char conteudo;
    private int position;
    private long chegada;

    public Adicao(String nome, char conteudo, int position) {
        super(nome);
        this.conteudo = conteudo;
        this.position = position;
    }

    public char getConteudo() {
        return conteudo;
    }

    public int getPosition() {
        return position;
    }

    public long getChegada() {
        return chegada;
    }
}
