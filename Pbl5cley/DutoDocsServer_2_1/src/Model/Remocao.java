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

    private int position;

    public Remocao(String nome, int position) {
        super(nome);
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

}
