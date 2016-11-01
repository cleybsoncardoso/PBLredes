/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.Serializable;

/**
 * classe criada para armazenar os dados do caracter que será incrementado
 * @author paiva e cleybson
 */
public class Adicao extends Modificacao implements Serializable {

    private char conteudo;
    private int position;

    /**
     * 
     * @param nome nome do arquivo
     * @param conteudo caracter a ser incrementado
     * @param position posição que será colocado
     */
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

}
