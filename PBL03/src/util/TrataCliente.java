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
import java.util.logging.Level;
import java.util.logging.Logger;
import view.Inicio;

/**
 * Classe responsavel para se comunicar com os outros carros através de conexão
 * tcp
 *
 * @author cleybson e Lucas
 */
public class TrataCliente implements Runnable {

    private static int id_counter = 1; //contador que indica o ID de cada carro, cada carro tem um ID diferente que é utilizado para identidicar o carro
    private Controller controller;
    private Socket cliente;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Server servidor; //servidor de cada usuario que recebe conexões de outros usuarios
    private String ip; //ip do usuario conectado
    private int id; //ID do respectivo carro
    private int modifier = 0; // variavel utilizada saber se é a primeira conexão de um usuario, para adiciona-lo na tela
    private String cor;
    private Quadrante quadranteAtual; //Quarda o quadrante do usuario

    TrataCliente(Socket cliente) {
        this.id = id_counter;
        this.id_counter++;
        this.controller = Controller.getInstance();//pega instancia do controler atual
        this.cliente = cliente;
        this.ip = this.cliente.getInetAddress().getHostAddress();
        this.servidor = servidor;
        this.quadranteAtual = new Quadrante("");
        try {
            output = new ObjectOutputStream(cliente.getOutputStream());
            input = new ObjectInputStream(cliente.getInputStream());
        } catch (IOException ex) {
        }
    }

    @Override
    public void run() {

        enviaIps();//envia a lista de ip que esta conectado com ele
        while (true) {
            try {
                ArrayList<Quadrante> trajeto = new ArrayList<>();
                //sempre o usuario vai receber os dados do carro:posição do x e y, a direção e se o carro está parado ou andando, para que o usuario saiba e possa representar isso na tela
                //todas essas informações são recebidas através de um array
                ArrayList<Object> mensagem = (ArrayList<Object>) input.readObject();
                float x = (float) mensagem.get(0);
                float y = (float) mensagem.get(1);
                int direcao = (int) mensagem.get(2);
                boolean parado = (boolean) mensagem.get(3);
                //o trajeto é enviado quadrante por quadrante, e assim a junção é feita nessa instrução abaixo
                int tamanhoDoTrajeto = (int) mensagem.get(4);
                for (int j = 5; j < tamanhoDoTrajeto + 5; j++) {
                    Quadrante q = (Quadrante) mensagem.get(j);
                    trajeto.add(q);
                }

                if (modifier == 0) {//coloca carro na tela
                    controller.adicionarCarro(id, x, y, direcao, trajeto);
                    if (id == 1) {
                        cor = "amarelo";
                    } else if (id == 2) {
                        cor = "verde";
                    } else if (id == 3) {
                        cor = "preto";
                    } else {
                        cor = "vermelho";
                    }

                    Inicio.getInstance().mostrar("iniciando carro " + cor + "na pista " + trajeto.get(0).getNome());
                    quadranteAtual = trajeto.get(0);
                    modifier = 1;
                } else {//atualiza os dados do carro
                    ControllerCarro carroAtual = controller.getCarro(this.id);
                    carroAtual.setXY(x, y, direcao);
                    carroAtual.setTrajeto(trajeto);

                    if (!quadranteAtual.getNome().equals(trajeto.get(0).getNome())) {//verifica se o carro ainda está no quadrante para pode exibir a msg
                        Inicio.getInstance().mostrar("Carro " + cor + " saindo da pista " + quadranteAtual.getNome());
                        Inicio.getInstance().mostrar("Carro " + cor + " entrando  na pista " + trajeto.get(0).getNome());
                        quadranteAtual = trajeto.get(0);
                    }
                    carroAtual.noCruzamento(parado);
                }

            } catch (IOException ex) {
                //caso ocorra algum erro de comunição, o carro é retirado da tela
                Inicio.getInstance().mostrar("Carro " + cor + " se desconectou");
                Controller.getInstance().removerCarro(id);
                return;
            } catch (ClassNotFoundException ex) {
                return;
            }
        }
    }

    /**
     * envia a lista de todos os ips de computadores que estão conectados com
     * meu serversocket
     */
    private void enviaIps() {
        try {
            String msg = (String) input.readObject();
            if (msg.equals("primeiro")) {
                ArrayList<String> aux = new ArrayList<>();
                aux.addAll(controller.getIps());
                aux.remove(this.ip);
                output.writeObject(aux);
            }
        } catch (IOException ex) {
            //caso ocorra algum erro de comunição, o carro é retirado da tela
            Inicio.getInstance().mostrar("Carro " + cor + " se desconectou");
            Controller.getInstance().removerCarro(id);
            return;
        } catch (ClassNotFoundException ex) {
            return;
        }

    }

}
