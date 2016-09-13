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
import view.Inicio;

/**
 *
 * @author paiva
 */
public class TrataCliente implements Runnable {

    private static int id_counter = 1;
    private Controller controller;
    private Socket cliente;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Server servidor;
    private String ip;
    private int id;
    private int modifier = 0;
    private Quadrante quadranteAtual;

    TrataCliente(Socket cliente) {
        this.id = id_counter;
        this.id_counter++;
        System.out.println(id);
        this.controller = Controller.getInstance();
        this.cliente = cliente;
        this.ip = this.cliente.getInetAddress().getHostAddress();
        this.servidor = servidor;
        this.quadranteAtual = new Quadrante("");
        try {
            output = new ObjectOutputStream(cliente.getOutputStream());
            input = new ObjectInputStream(cliente.getInputStream());
        } catch (IOException ex) {
//            controller.removerIp(this.ip);
            System.out.println("Conexão perdida com " + ip);
        }
    }

    @Override
    public void run() {

        try {
            enviaIps();
        } catch (IOException ex) {
            //           controller.removerIp(this.ip);
            System.out.println("Conexão perdida com " + ip);
            return;
        } catch (ClassNotFoundException ex) {
            return;
        }

        while (true) {
            try {
//                String msg = (String) input.readObject();
//                System.out.println(msg);
                ArrayList<Quadrante> trajeto = new ArrayList<>();
                ArrayList<Object> mensagem = (ArrayList<Object>) input.readObject();
                float x = (float) mensagem.get(0);
                float y = (float) mensagem.get(1);
                int direcao = (int) mensagem.get(2);
                boolean parado = (boolean) mensagem.get(3);

                //ArrayList<Quadrante> trajeto = (ArrayList<Quadrante>) mensagem.get(3);
                int tamanhoDoTrajeto = (int) mensagem.get(4);
                for (int j = 5; j < tamanhoDoTrajeto + 5; j++) {
                    Quadrante q = (Quadrante) mensagem.get(j);
                    trajeto.add(q);
                }
                
                //System.out.println("mensagem recebida");
                if (modifier == 0) {
                    controller.adicionarCarro(id, x, y, direcao, trajeto);
                    Inicio.getInstance().mostrar("iniciando carro " + id + "na pista " + trajeto.get(0).getNome());
                    quadranteAtual=trajeto.get(0);
                    modifier = 1;
                } else {
                    ControllerCarro carroAtual = controller.getCarro(this.id);
                    carroAtual.setXY(x, y, direcao);
                    carroAtual.setTrajeto(trajeto);
                    if(!quadranteAtual.getNome().equals(trajeto.get(0).getNome())){
                        Inicio.getInstance().mostrar("Carro "+ id + " saindo da pista " + trajeto.get(0).getNome());
                        Inicio.getInstance().mostrar("Carro "+ id + " entrandd  na pista " + quadranteAtual.getNome());
                        
                        quadranteAtual=trajeto.get(0);
                    }
                    carroAtual.setNaVia(parado);
                }

            } catch (IOException ex) {
//                controller.removerIp(this.ip);
                System.out.println("Conexão perdida com " + ip);
                Inicio.getInstance().mostrar("Carro " + id + " se desconectou");
                Controller.getInstance().removerCarro(id);
                return;
            } catch (ClassNotFoundException ex) {
                return;
            }
        }
    }

    private void enviaIps() throws IOException, ClassNotFoundException {
        String msg = (String) input.readObject();
        System.out.println(msg);
        if (msg.equals("primeiro")) {
            ArrayList<String> aux = new ArrayList<>();
            aux.addAll(controller.getIps());
            aux.remove(this.ip);
            output.writeObject(aux);
            System.out.println("saiu");
        }
    }

}
