/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.ArrayList;

/**
 *
 * @author cleyb
 */
public class InformacoesCliente {
    private ArrayList<String> nomeArquivos;
    private String ip;
    private int porta;

    public InformacoesCliente(ArrayList<String> nomeArquivos, int porta) {
        this.nomeArquivos = nomeArquivos;
        this.ip = ip;
        this.porta=porta;
    }

    public ArrayList<String> getNomeArquivos() {
        return nomeArquivos;
    }

    public String getIp() {
        return ip;
    }

    public int getPorta() {
        return porta;
    }
    
    
    
}
