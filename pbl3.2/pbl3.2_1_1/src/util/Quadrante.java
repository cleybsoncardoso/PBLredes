/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.Serializable;

/**
 *
 * @author cleyb
 */
public class Quadrante implements Serializable{

    private String nome;

    public Quadrante(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public boolean aindaQuadranteX(double x) {
        x = x + 13.5;
        if (nome.equals("d")) {
            if (x < 283) {
                return true;
            } else {
                return false;
            }
        } else if (nome.equals("b")) {
            if (x > 250) {
                return true;
            } else {
                return false;
            }
        } else if (nome.equals("a")) {
            if (x > 176) {
                return true;
            } else {
                return false;
            }
        } else if (nome.equals("c")) {
            if (x < 210) {
                return true;
            } else {
                return false;
            }
        } else if (nome.equals("D")) {
            if (x < 170) {
                return true;
            } else {
                return false;
            }
        } else if (x > 288) {
            return true;
        } else {
            return false;
        }

    }

     public boolean aindaQuadranteY(double y) {
        y+=13.5;
        if (nome.equals("d")) {
            if (y > 247) {
                return true;
            } else {
                return false;
            }
        } else if (nome.equals("b")) {
            if (y > 174) {
                return true;
            } else {
                return false;
            }
        } else if (nome.equals("a")) {
            if (y < 210) {
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
            if (y < 169) {
                return true;
            } else {
                return false;
            }
        }else{
            if (y > 288) {
                return true;
            } else {
                return false;
            }
        }
    }
}
