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
        this.setInfo(ip, porta);
    }

    public ArrayList<Arquivo> getNomeArquivos() {
        return nomeArquivos;
    }

    public void setNomeArquivos(ArrayList<Arquivo> nomeArquivos) {
        this.nomeArquivos = nomeArquivos;
    }

    public void setInfo(String ip, int porta) {
        for (Arquivo arquivo : nomeArquivos) {
            arquivo.setIp(ip);
            arquivo.setPorta(porta);
        }
    }
}
