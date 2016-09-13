/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.Serializable;

/**
 * classe que contem as informações de cada quadrante, as delimitações e o nome dele
 * @author cleybson e Lucas
 */
public class Quadrante implements Serializable{

    private String nome;

    public Quadrante(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    /**
     * verifica pelo x, se o carro ainda esta naquele quadrante
     * @param x
     * @return 
     */
    public boolean aindaQuadranteX(double x) {
        if (nome.equals("d")) {
            if (x < 278) {
                return true;
            } else {
                return false;
            }
        } else if (nome.equals("b")) {
            if (x > 230) {
                return true;
            } else {
                return false;
            }
        } else if (nome.equals("a")) {
            if (x > 183) {
                return true;
            } else {
                return false;
            }
        } else if (nome.equals("c")) {
            if (x < 230) {
                return true;
            } else {
                return false;
            }
        } else if (nome.equals("D")) {
            if (x < 154) {
                return true;
            } else {
                return false;
            }
        } else if (x > 308) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * verifica pelo y, se o carro ainda esta naquele quadrante
     * @param y
     * @return 
     */
     public boolean aindaQuadranteY(double y) {
        if (nome.equals("d")) {
            if (y > 229) {
                return true;
            } else {
                return false;
            }
        } else if (nome.equals("b")) {
            if (y > 183) {
                return true;
            } else {
                return false;
            }
        } else if (nome.equals("a")) {
            if (y < 230) {
                return true;
            } else {
                return false;
            }
        } else if(nome.equals("c")){
            if (y < 280) {
                return true;
            } else {
                return false;
            }
        }else if(nome.equals("C")){
            if (y < 150) {
                return true;
            } else {
                return false;
            }
        }else{
            if (y > 306) {
                return true;
            } else {
                return false;
            }
        }
    }
}
