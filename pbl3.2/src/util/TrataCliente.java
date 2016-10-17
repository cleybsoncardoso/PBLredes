/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import controller.Controller;
import controller.ControllerCarro;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Multicast;
import model.Verificacao;
import view.Inicio;

/**
 * Classe responsavel para se comunicar com os outros carros através de conexão
 * tcp
 *
 * @author cleybson e Lucas
 */
public class TrataCliente implements Runnable {

    private Controller controller;
    private Quadrante quadranteAtual; //Quarda o quadrante do usuario
    private Verificacao verifica;
    private ArrayList<String> chavesips;
    private String meuIp;

    public TrataCliente() {
        this.controller = Controller.getInstance();//pega instancia do controler atual
        this.quadranteAtual = new Quadrante("");
        chavesips = new ArrayList<String>();
        verifica = new Verificacao(this);
        new Thread(verifica).start();
        try {
            meuIp = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException ex) {
            Logger.getLogger(TrataCliente.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void run() {
        while (true) {

            ArrayList<Quadrante> trajeto = new ArrayList<>();
            //sempre o usuario vai receber os dados do carro:posição do x e y, a direção e se o carro está parado ou andando, para que o usuario saiba e possa representar isso na tela
            //todas essas informações são recebidas através de um array

            String[] mensagem = Multicast.getInstancia().receberMensagem().split(";");
            if (!meuIp.equals(mensagem[0])) {
                String chaveHash = mensagem[1];
                float x = Float.parseFloat(mensagem[2]);
                float y = Float.parseFloat(mensagem[3]);
                int direcao = Integer.parseInt(mensagem[4]);
                boolean parado = Boolean.parseBoolean(mensagem[5]);
                //o trajeto é enviado quadrante por quadrante, e assim a junção é feita nessa instrução abaixo
                int tamanhoDoTrajeto = Integer.parseInt(mensagem[6]);
                for (int j = 6; j < tamanhoDoTrajeto + 7; j++) {
                    trajeto.add(new Quadrante(mensagem[j]));
                }
                verifica.atualiza(chaveHash);
                if (chavesips.contains(chaveHash)) {
                    ControllerCarro carroAtual = controller.getCarro(chaveHash);
                    carroAtual.setXY(x, y, direcao);
                    carroAtual.setTrajeto(trajeto);

                    if (!quadranteAtual.getNome().equals(trajeto.get(0).getNome())) {//verifica se o carro ainda está no quadrante para pode exibir a msg
                        Inicio.getInstance().mostrar("Carro " + mensagem[0] + " saindo da pista " + quadranteAtual.getNome());
                        Inicio.getInstance().mostrar("Carro " + mensagem[0] + " entrando  na pista " + trajeto.get(0).getNome());
                        quadranteAtual = trajeto.get(0);
                    }
                    carroAtual.noCruzamento(parado);
                } else {
                    chavesips.add(chaveHash);
                    controller.adicionarCarro(chaveHash, x, y, direcao, trajeto);
                    Inicio.getInstance().mostrar("iniciando carro " + mensagem[0] + " na pista " + trajeto.get(0).getNome());
                    quadranteAtual = trajeto.get(0);
                }
            }
        }
    }

    public ArrayList<String> getChavesips() {
        return chavesips;
    }

}
