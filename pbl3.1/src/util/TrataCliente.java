/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import controller.Controller;
import controller.ControllerCarro;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
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
     private String cor;
    private Quadrante quadranteAtual; //Quarda o quadrante do usuario
    private Verificacao verifica;
    private ArrayList<String> ips;

    public void TrataCliente() {
        this.controller = Controller.getInstance();//pega instancia do controler atual
        this.quadranteAtual = new Quadrante("");
        ips = new ArrayList<String>();

    }

    @Override
    public void run() {
        while (true) {

            ArrayList<Quadrante> trajeto = new ArrayList<>();
            //sempre o usuario vai receber os dados do carro:posição do x e y, a direção e se o carro está parado ou andando, para que o usuario saiba e possa representar isso na tela
            //todas essas informações são recebidas através de um array

            String[] mensagem = Multicast.getInstancia().receberMensagem().split(";");
            
            float x = Float.parseFloat(mensagem[1]);
            float y = Float.parseFloat(mensagem[2]);
            int direcao = Integer.parseInt(mensagem[3]);
            boolean parado = Boolean.parseBoolean(mensagem[4]);
            //o trajeto é enviado quadrante por quadrante, e assim a junção é feita nessa instrução abaixo
            int tamanhoDoTrajeto = Integer.parseInt(mensagem[5]);
            for (int j = 6; j < tamanhoDoTrajeto + 6; j++) {
                trajeto.add(new Quadrante(mensagem[j]));
            }
            
            StringTokenizer tokenIp = new StringTokenizer(mensagem[0], ".");
            tokenIp.nextToken();
            tokenIp.nextToken();
            String chaveHach = tokenIp.nextToken() + tokenIp.nextToken();
            
            if(ips.contains(mensagem[0])){
                ControllerCarro carroAtual = controller.getCarro(chaveHach);
                carroAtual.setXY(x, y, direcao);
                carroAtual.setTrajeto(trajeto);

                if (!quadranteAtual.getNome().equals(trajeto.get(0).getNome())) {//verifica se o carro ainda está no quadrante para pode exibir a msg
                    Inicio.getInstance().mostrar("Carro " + cor + " saindo da pista " + quadranteAtual.getNome());
                    Inicio.getInstance().mostrar("Carro " + cor + " entrando  na pista " + trajeto.get(0).getNome());
                    quadranteAtual = trajeto.get(0);
                }
                carroAtual.noCruzamento(parado);
            }else{
                controller.adicionarCarro(chaveHach, x, y, direcao, trajeto);
                Inicio.getInstance().mostrar("iniciando carro " + cor + " na pista " + trajeto.get(0).getNome());
                quadranteAtual = trajeto.get(0);
            }
        }
    }
}
