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
    private String[] operacoes;
    private int inicio, fim;

    public Modificacao(String nome) {
        this.nome = nome;
    }

    public Modificacao(String nome, String operacao, int inicio, int fim) {
        this.nome = nome;
        operacoes = new String[3];
        operacoes[0] = operacao;
        operacoes[1] = String.valueOf(inicio);
        operacoes[2] = String.valueOf(fim);
    }

    public String getNome() {
        return nome;
    }

    public String[] getOperacoes() {
        return operacoes;
    }

}
