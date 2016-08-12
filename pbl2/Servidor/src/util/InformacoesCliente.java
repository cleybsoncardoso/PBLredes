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

    private ArrayList<Arquivo> nomeArquivos;

    public InformacoesCliente(ArrayList<Arquivo> nomeArquivos, int porta, String ip) {
        this.nomeArquivos = nomeArquivos;
        setInfo(ip, porta);
    }

    public ArrayList<Arquivo> getNomeArquivos() {
        return nomeArquivos;
    }

    public void setNomeArquivos(ArrayList<Arquivo> nomeArquivos) {
        this.nomeArquivos = nomeArquivos;
    }

    private void setInfo(String ip, int porta) {
        for (Arquivo arquivo : nomeArquivos) {
            arquivo.setIp(ip);
            arquivo.setPorta(porta);
        }
    }
}
