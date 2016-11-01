/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.Serializable;

/**
 *
 * @author paiva
 */
public class RemocaoSelecao extends Modificacao implements Serializable {

    private int posBegin, posEnd;

    /**
     * 
     * @param nome nome do arquivo
     * @param posBegin primeiro caracter a ser apagado
     * @param posEnd ultimo caracter a ser apagado
     */
    RemocaoSelecao(String nome, int posBegin, int posEnd) {
        super(nome);
        this.posBegin=posBegin;
        this.posEnd=posEnd;
    }

    public int getPosBegin() {
        return posBegin;
    }

    public int getPosEnd() {
        return posEnd;
    }
    
    

}
