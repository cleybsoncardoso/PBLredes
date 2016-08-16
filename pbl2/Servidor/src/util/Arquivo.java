/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.Serializable;

/**
 *
 * @author paiva
 */
public class Arquivo implements Serializable {

    private String nome;
    private String ip;
    private int porta;
    private long tamanho;

    public Arquivo(String nome, long tamanho) {
        this.nome = nome;
        this.ip = "";
        this.porta = 0;
        this.tamanho = tamanho;
    }

    public String getNome() {
        return nome;
    }

    public String getIp() {
        return ip;
    }

    public int getPorta() {
        return porta;
    }

    public long getTamanho() {
        return tamanho;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPorta(int porta) {
        this.porta = porta;
    }

}
