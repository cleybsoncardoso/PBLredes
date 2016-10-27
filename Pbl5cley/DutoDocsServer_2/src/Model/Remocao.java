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
public class Remocao extends Modificacao {

    private int inicio;
    private int fim;

    public Remocao(String nome, int inicio, int fim) {
        super(nome);
        this.inicio = this.inicio;
        this.fim = this.fim;
    }

    public int getFim() {
        return fim;
    }

    public int getInicio() {
        return inicio;
    }

}
