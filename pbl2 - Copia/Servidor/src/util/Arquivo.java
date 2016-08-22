/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.Serializable;

/**
 * Classe responsável por ser uma abstração de um arquivo que será
 * compartilhado. Nela contém o nome do arquivo, seu tamanho, endereço no
 * diretório e informações do serversocket que está localizado.
 *
 * @author paiva
 * @see ServerSocket
 */
public class Arquivo implements Serializable {

    private String nome;
    private String ip;
    private int porta;
    private long tamanho;
    private String endereco;

    /**
     * Construtor que recebe como parâmetro informações sobre o arquivo.
     *
     * @param nome
     * @param tamanho
     * @param endereco
     */
    public Arquivo(String nome, long tamanho, String endereco) {
        this.nome = nome;
        this.ip = "";
        this.porta = 0;
        this.tamanho = tamanho;
        this.endereco = endereco;
    }

    public String getNome() {
        return nome;
    }

    public String getIp() {
        return ip;
    }

    public String getEndereco() {
        return endereco;
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
