/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author cleyb
 */
public class InformacoesCliente implements Serializable {

    private ArrayList<String> nomeArquivos;
    private String ip;
    private int porta;

    public InformacoesCliente(ArrayList<String> nomeArquivos, int porta, String ip) {
        this.nomeArquivos = nomeArquivos;
        this.ip = ip;
        this.porta = porta;
    }

    public ArrayList<String> getNomeArquivos() {
        return nomeArquivos;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIp() {
        return ip;
    }

    public int getPorta() {
        return porta;
    }

    public void setNomeArquivos(ArrayList<String> nomeArquivos) {
        this.nomeArquivos = nomeArquivos;
    }

    public void setPorta(int porta) {
        this.porta = porta;
    }

}
