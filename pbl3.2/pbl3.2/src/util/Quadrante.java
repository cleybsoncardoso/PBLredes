/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 *
 * @author cleyb
 */
public class Quadrante {

    private String nome;

    public Quadrante(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public boolean aindaQuadranteX(double x) {
        if (nome.equals("d")) {
            if (x < 288) {
                return false;
            } else {
                return true;
            }
        } else if (nome.equals("b")) {
            if (x > 231) {
                return true;
            } else {
                return false;
            }
        } else if (nome.equals("a")) {
            if (x > 194) {
                return true;
            } else {
                return false;
            }
        } else if(nome.equals("c")){
            if (x < 257) {
                return false;
            } else {
                return true;
            }
        }else if(nome.equals("D")){
            if (x < 194) {
                return true;
            } else {
                return false;
            }
        }else{
            if (x > 288) {
                return true;
            } else {
                return false;
            }
        }

    }

    public boolean aindaQuadranteY(int y) {
        if (nome.equals("d")) {
            if (y < 288) {
                return false;
            } else {
                return true;
            }
        } else if (nome.equals("b")) {
            if (y < 167) {
                return false;
            } else {
                return true;
            }
        } else if (nome.equals("a")) {
            if (y < 163) {
                return false;
            } else {
                return true;
            }
        } else if (y < 257) {
            return false;
        } else {
            return true;
        }
    }

}
