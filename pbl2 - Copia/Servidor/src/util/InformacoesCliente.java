package util;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * Classe que contém a lista de arquivos de determinado cliente. Cada Arquivo
 * possui sua informação de porta e IP.
 *
 * @see Usuario
 * @see Arquivo
 * @see Socket
 * @see Serializable
 *
 */
public class InformacoesCliente implements Serializable {

    private ArrayList<Arquivo> nomeArquivos;//lista de Arquivo contendo os arquivos da pasta compartilhada do usuario

    public InformacoesCliente(ArrayList<Arquivo> nomeArquivos, int porta, String ip) {
        this.nomeArquivos = nomeArquivos;
        this.setInfo(ip, porta);
    }

    /**
     * Método que retorna a lista de arquivos.
     *
     * @see Arquivo
     * @return
     */
    public ArrayList<Arquivo> getNomeArquivos() {
        return nomeArquivos;
    }

    /**
     * Método que atribui uma nova listagem de arquivos para a lista de
     * arquivos.
     *
     * @see Arquivo
     * @param nomeArquivos
     */
    public void setNomeArquivos(ArrayList<Arquivo> nomeArquivos) {
        this.nomeArquivos = nomeArquivos;
    }

    /**
     * Método que altera as informações dos arquivos da lista de arquivos.
     *
     * @see Arquivo
     * @param ip
     * @param porta
     */
    public void setInfo(String ip, int porta) {
        for (Arquivo arquivo : nomeArquivos) {
            arquivo.setIp(ip);
            arquivo.setPorta(porta);
        }
    }
}
