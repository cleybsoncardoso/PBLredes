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
public class Documento {

    private String nome, conteudo;
    private int abertos;

    public Documento(String nome, String conteudo) {
        this.nome = nome;
        this.conteudo = conteudo;
        this.abertos = 1;
    }

    public int length() {
        return conteudo.length();
    }

    public String getNome() {
        return nome;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public int getAbertos() {
        return abertos;
    }

    public void increment() {
        abertos++;
    }

    public int decrement() {
        return --abertos;
    }

}
