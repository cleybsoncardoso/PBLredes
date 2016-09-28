/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import util.Carro;
import util.Logica;
import util.Quadrante;
import view.Inicio;

/**
 * Classe responsavel por controlar as ações dos carros
 * @author cleybson e lucas
 */
public class ControllerCarro {

    private float x; //posição x do carro
    private float y; //posição y do carro
    public Carro carro; //dados do carro a ser controlado
    private int alturaTela = 480;
    private int larguraTela = 482;
    private int direcao;
    private float v = 0.3f; //velocidade do carro
    private ArrayList<Quadrante> trajeto;
    private int id; //id do carro a ser controlado
    private String origem;
    private String destino;
    private Logica logica;
    private boolean noCruzamento = false; //boolean que verifica se o carro ja entrou no cruzamento

    /**
     * Construtor do meu carro
     * @param id
     * @param origem
     * @param destino 
     */
    public ControllerCarro(int id, String origem, String destino) {
        this.id = id;
        this.origem = origem;
        this.destino = destino;
        this.logica = new Logica(this);
        this.trajeto = logica.calcularTrajeto(origem, destino);//gera o trajeto do carro
        Inicio.getInstance().mostrar("Iniciando Carro " + "azul" + " em pista " + origem); //exibe mensagem
        this.setup();
    }

    /**
     * contrutor dos outros carros
     * @param id
     * @param x
     * @param y
     * @param direcao 
     * @param trajeto 
     */
    public ControllerCarro(int id, float x, float y, int direcao, ArrayList<Quadrante> trajeto) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.direcao = direcao;
        this.trajeto = trajeto;
        String cor;
        //verifica qual a cor do carro
        if (this.id == 0) {
            cor = "azul";
        } else if (this.id == 1) {
            cor = "amarelo";
        } else if (this.id == 2) {
            cor = "verde";
        } else if (this.id == 3) {
            cor = "preto";
        } else {
            cor = "vermelho";
        }
        Inicio.getInstance().mostrar("Iniciando Carro " + cor + " em pista " + trajeto.get(0).getNome());
        this.setup2();
    }

    public int getId() {
        return this.id;
    }

    /**
     * cria o carro que se conecta com seu serversocket
     */
    private void setup2() {
        switch (direcao) {
            case 0:
                carro = new Carro(this.x, this.y, direcao);
                break;
            case 9:
                carro = new Carro(this.x, this.y, direcao);
                break;
            case 6:
                carro = new Carro(this.x, this.y, direcao);
                break;
            case 3:
                carro = new Carro(this.x, this.y, direcao);
                break;
        }
        this.origem = trajeto.get(0).getNome();
        this.destino = trajeto.get(trajeto.size() - 1).getNome();
    }

    /**
     * cria seu carro
     */
    private void setup() {
        switch (origem) {
            case "A":
                this.x = 257;
                this.y = alturaTela;
                this.direcao = 0;
                carro = new Carro(this.x, this.y, direcao);
                break;
            case "B":
                this.x = larguraTela;
                this.y = 208;
                this.direcao = 9;
                carro = new Carro(this.x, this.y, direcao);
                break;
            case "C":
                this.x = 210;
                this.y = 0;
                this.direcao = 6;
                carro = new Carro(this.x, this.y, direcao);
                break;
            case "D":
                this.x = 0;
                this.y = 257;
                this.direcao = 3;
                carro = new Carro(this.x, this.y, direcao);
                break;
        }
        this.msgMandar();
    }

    /**
     * coloca a cor do carro
     * @param g2d 
     */
    public void desenhar(Graphics2D g2d) {
        if (this.id == 0) {
            g2d.setColor(Color.BLUE);
        } else if (this.id == 1) {
            g2d.setColor(Color.YELLOW);
        } else if (this.id == 2) {
            g2d.setColor(Color.GREEN);
        } else if (this.id == 3) {
            g2d.setColor(Color.BLACK);
        } else {
            g2d.setColor(Color.RED);
        }

        g2d.fill(carro.getRect());

    }

    public Rectangle2D getRect() {
        return this.carro.getRect();
    }

    public ArrayList<Quadrante> getTrajeto() {
        return trajeto;
    }

    /**
     * metodo que faz o carro se movimentar na tela e verifica se o carro ainda esta no quadrante, caso não esteja
     * ele remove o quadrante do trajeto
     */
    public void andar() {
        switch (direcao) {
            case 0:
                if (!trajeto.get(0).aindaQuadranteY(y)) {
                    if (trajeto.size() > 1) {
                        Inicio.getInstance().mostrar("Carro azul saindo da pista " + trajeto.get(0).getNome());
                        trajeto.remove(0);
                        Inicio.getInstance().mostrar("Carro azul entrando na pista " + trajeto.get(0).getNome());
                    }
                }
                y = y - v;
                carro.setXY(x, y);
                break;
            case 3:
                if (!trajeto.get(0).aindaQuadranteX(x)) {
                    if (trajeto.size() > 1) {
                        Inicio.getInstance().mostrar("Carro azul saindo da pista " + trajeto.get(0).getNome());
                        trajeto.remove(0);
                        Inicio.getInstance().mostrar("Carro azul entrando na pista " + trajeto.get(0).getNome());
                    }
                }
                x = x + v;
                carro.setXY(x, y);
                break;
            case 6:
                if (!trajeto.get(0).aindaQuadranteY(y)) {
                    if (trajeto.size() > 1) {
                        Inicio.getInstance().mostrar("carro azul saindo da pista " + trajeto.get(0).getNome());
                        trajeto.remove(0);
                        Inicio.getInstance().mostrar("Carro azul entrando na pista " + trajeto.get(0).getNome());
                    }
                }
                y = y + v;
                carro.setXY(x, y);
                break;
            case 9:
                if (!trajeto.get(0).aindaQuadranteX(x)) {
                    if (trajeto.size() > 1) {
                        Inicio.getInstance().mostrar("Carro azul saindo da pista " + trajeto.get(0).getNome());
                        trajeto.remove(0);
                        Inicio.getInstance().mostrar("Carro azul entrando na pista " + trajeto.get(0).getNome());
                    }
                }
                x = x - v;
                carro.setXY(x, y);
                break;
        }
        this.msgMandar();
        //manda msg com os dados do seu carrro
    }

    /**
     * metodo que manda a msg para os outros carros, com o x, y, direção, se está dentro do cruzamento e o trajeto
     */
    private void msgMandar() {

        ArrayList<Object> msg = new ArrayList<Object>();

        msg.add(x);
        msg.add(y);
        msg.add(direcao);
        msg.add(noCruzamento);
        msg.add(trajeto.size());
        //ele separa o trajeto, mandando quafrante por quadrante
        for (Quadrante q : trajeto) {
            msg.add(q);
        }
        Controller.getInstance().replicarMsg(msg);
    }

    /**
     * muda a direção do carro
     */
    public void virarEsquerda() {
        direcao = direcao - 3;
        if (direcao == -3) {
            direcao = 9;
        }
        carro.virar();
    }

    /**
     * muda a direção do carro
     */
    public void virarDireita() {
        direcao = direcao + 3;
        if (direcao == 12) {
            direcao = 0;
        }
        carro.virar();
    }

    public boolean noCruzamento() {
        return noCruzamento;
    }

    public void noCruzamento(boolean noCruzamento) {
        this.noCruzamento = noCruzamento;
    }

    /**
     * método responsavel por escolher a ação a ser feita pelo carro, andar, ficar parado esperando
     */
    public void acao() {

        //quando o carro estiver na via principal ele vai andar sem parar, ate chegar no beira da pista
        if (origem.equals("A") && this.y > 317) {
            andar();
        } else if (origem.equals("C") && this.y < 139) {
            andar();
        } else if (origem.equals("B") && this.x > 318) {
            andar();
        } else if (origem.equals("D") && this.x < 143) {
            andar();
        }

        //verifica se o carro esta na via principal ou cruzamento
        if (trajeto.get(0).getNome().equals("A") || trajeto.get(0).getNome().equals("B") || trajeto.get(0).getNome().equals("C") || trajeto.get(0).getNome().equals("D")) {
            noCruzamento = false;
        } else {
            noCruzamento = true;
        }

        //verifica se tem conflito, uma possivel batida ao entrar no cruzamento
        if (!logica.conflito()) {//caso não ocorra conflito, o carro anda o sem parar ate o fim do cruzamento
            if (origem.equals("A") && destino.equals("D")) {
                if ((y <= 208) && (direcao == 0)) {
                    virarEsquerda();
                } else {
                    andar();
                }
            } else if (origem.equals("A") && destino.equals("B")) {
                if ((y <= 257) && (direcao == 0)) {
                    virarDireita();
                } else {
                    andar();
                }
            } else if (origem.equals("A") && destino.equals("C")) {
                andar();
            }

            if (origem.equals("C") && destino.equals("D")) {
                if ((y >= 208) && (direcao == 6)) {
                    virarDireita();
                } else {
                    andar();
                }
            } else if (origem.equals("C") && destino.equals("B")) {
                if ((y >= 257) && (direcao == 6)) {
                    virarEsquerda();
                } else {
                    andar();
                }
            } else if (origem.equals("C") && destino.equals("A")) {
                andar();
            }

            if (origem.equals("B") && destino.equals("C")) {
                if ((x <= 257) && (direcao == 9)) {
                    virarDireita();
                } else {
                    andar();
                }
            } else if (origem.equals("B") && destino.equals("A")) {
                if ((x <= 210) && (direcao == 9)) {
                    virarEsquerda();
                } else {
                    andar();
                }
            } else if (origem.equals("B") && destino.equals("D")) {
                andar();
            }

            if (origem.equals("D") && destino.equals("C")) {
                if ((x >= 257) && (direcao == 3)) {
                    virarEsquerda();
                } else {
                    andar();
                }
            } else if (origem.equals("D") && destino.equals("A")) {
                if ((x >= 210) && (direcao == 3)) {
                    virarDireita();
                } else {
                    andar();
                }
            } else if (origem.equals("D") && destino.equals("B")) {
                andar();
            }
        }

    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setXY(float x, float y, int direcao) {
        this.x = x;
        this.y = y;
        this.direcao = direcao;
        carro.setXY(x, y, direcao);

    }

    public void setTrajeto(ArrayList<Quadrante> trajeto) {
        this.trajeto = trajeto;
    }

    public int getDirecao() {
        return this.direcao;
    }

}
