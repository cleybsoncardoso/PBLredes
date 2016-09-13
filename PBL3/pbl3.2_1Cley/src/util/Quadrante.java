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
            if (x < 164) {
                return true;
            } else {
                return false;
            }
        } else if (x > 298) {
            return true;
        } else {
            return false;
        }

    }

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
            if (y < 160) {
                return true;
            } else {
                return false;
            }
        }else{
            if (y > 296) {
                return true;
            } else {
                return false;
            }
        }
    }
}
