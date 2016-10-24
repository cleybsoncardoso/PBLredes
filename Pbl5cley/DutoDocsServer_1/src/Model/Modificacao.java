/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author cleyb
 */
public class Modificacao {
    
    private String nome;
    private char conteudo;
    private int carent;
    private long chegada;

    public Modificacao(String nome, char conteudo, int carent) {
        this.nome = nome;
        this.conteudo = conteudo;
        this.carent = carent;
        chegada = System.currentTimeMillis();
    }

    public String getNome() {
        return nome;
    }

    public char getConteudo() {
        return conteudo;
    }

    public int getCarent() {
        return carent;
    }

    public long getChegada() {
        return chegada;
    }
    
    
    
}
