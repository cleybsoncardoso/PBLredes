/*
 * Controller de cada carro
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
 *
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
    private String origem;
    private String destino;
    private Logica logica;
    private boolean noCruzamento = false; //boolean que verifica se o carro ja entrou no cruzamento
    private boolean meuCarro;

    /**
     * Construtor do meu carro
     *
     * @param origem
     * @param destino
     */
    public ControllerCarro(String origem, String destino) {
        this.origem = origem;
        this.destino = destino;
        this.logica = new Logica(this);
        this.trajeto = logica.calcularTrajeto(origem, destino);//gera o trajeto do carro
        this.meuCarro = true;
        Inicio.getInstance().mostrar("Iniciando Meu carro na pista " + origem); //exibe mensagem
        this.setup();
    }

    /**
     * contrutor dos outros carros
     *
     * @param x
     * @param y
     * @param direcao
     * @param trajeto
     */
    public ControllerCarro(float x, float y, int direcao, ArrayList<Quadrante> trajeto) {
        this.x = x;
        this.y = y;
        this.direcao = direcao;
        this.trajeto = trajeto;
        this.meuCarro = false;
        Inicio.getInstance().mostrar("Iniciando Novo Carro em pista " + trajeto.get(0).getNome());
        this.setup2();
    }

    public boolean isMeuCarro() {
        return meuCarro;
    }

    public void setMeuCarro(boolean meuCarro) {
        this.meuCarro = meuCarro;
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
     *
     * @param g2d
     */
    public void desenhar(Graphics2D g2d) {
        g2d.setColor(Color.RED);
        g2d.fill(carro.getRect());

    }

    public Rectangle2D getRect() {
        return this.carro.getRect();
    }

    public ArrayList<Quadrante> getTrajeto() {
        return trajeto;
    }

    /**
     * metodo que faz o carro se movimentar na tela e verifica se o carro ainda
     * esta no quadrante, caso não esteja ele remove o quadrante do trajeto
     */
    public void andar() {
        switch (direcao) {
            case 0:
                if (!trajeto.get(0).aindaQuadranteY(y)) {
                    if (trajeto.size() > 1) {
                        Inicio.getInstance().mostrar("Meu carro saindo da pista " + trajeto.get(0).getNome());
                        trajeto.remove(0);
                        Inicio.getInstance().mostrar("Meu carro entrando na pista " + trajeto.get(0).getNome());
                    }
                }
                y = y - v;
                carro.setXY(x, y);
                break;
            case 3:
                if (!trajeto.get(0).aindaQuadranteX(x)) {
                    if (trajeto.size() > 1) {
                        Inicio.getInstance().mostrar("Meu carro saindo da pista " + trajeto.get(0).getNome());
                        trajeto.remove(0);
                        Inicio.getInstance().mostrar("Meu carro entrando na pista " + trajeto.get(0).getNome());
                    }
                }
                x = x + v;
                carro.setXY(x, y);
                break;
            case 6:
                if (!trajeto.get(0).aindaQuadranteY(y)) {
                    if (trajeto.size() > 1) {
                        Inicio.getInstance().mostrar("Meu carro saindo da pista " + trajeto.get(0).getNome());
                        trajeto.remove(0);
                        Inicio.getInstance().mostrar("Meu carro entrando na pista " + trajeto.get(0).getNome());
                    }
                }
                y = y + v;
                carro.setXY(x, y);
                break;
            case 9:
                if (!trajeto.get(0).aindaQuadranteX(x)) {
                    if (trajeto.size() > 1) {
                        Inicio.getInstance().mostrar("Meu carro saindo da pista " + trajeto.get(0).getNome());
                        trajeto.remove(0);
                        Inicio.getInstance().mostrar("Meu carro entrando na pista " + trajeto.get(0).getNome());
                    }
                }
                x = x - v;
                carro.setXY(x, y);
                break;
        }

        //this.msgMandar();
        //manda msg com os dados do seu carrro

    }

    /**
     * metodo que manda a msg para os outros carros, com o x, y, direção, se
     * está dentro do cruzamento e o trajeto
     */
    private void msgMandar() {

        String msgAEnviar = "";
        msgAEnviar += x;
        msgAEnviar += ";";
        msgAEnviar += y;
        msgAEnviar += ";";
        msgAEnviar += direcao;
        msgAEnviar += ";";
        msgAEnviar += noCruzamento;
        msgAEnviar += ";";
        msgAEnviar += trajeto.size();
        msgAEnviar += ";";
        //ele separa o trajeto, mandando quafrante por quadrante
        for (Quadrante q : trajeto) {
            msgAEnviar += q.getNome();
            msgAEnviar += ";";
        }
        Controller.getInstance().replicarMsg(msgAEnviar);
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
     * método responsavel por escolher a ação a ser feita pelo carro, andar,
     * ficar parado esperando e retorna false quando sai da tela.
     *
     * @return
     */
    public boolean acao() {
        this.msgMandar();
        //quando o carro estiver na via principal ele vai andar sem parar, ate chegar no beira da pista
        if (origem.equals("A") && this.y > 317) {
            andar();
            return true;
        } else if (origem.equals("C") && this.y < 139) {
            andar();
            return true;
        } else if (origem.equals("B") && this.x > 318) {
            andar();
            return true;
        } else if (origem.equals("D") && this.x < 143) {
            andar();
            return true;
        } else if (destino.equals("D") && this.x < 0) {
            System.out.println("acabou o programa");
            return false;
        } else if (destino.equals("B") && this.x > 480) {
            System.out.println("acabou o programa");
            return false;
        } else if (destino.equals("A") && this.y > 480) {
            System.out.println("acabou o programa");
            return false;
        } else if (destino.equals("C") && this.y < 0) {
            System.out.println("acabou o programa");
            return false;
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

        return true;

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
