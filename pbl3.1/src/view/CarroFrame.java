/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controller.Controller;
import controller.ControllerCarro;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import util.MainLoop;

/**
 * Classe que representa a tela onde serão apresentados os veículos presentes na
 * aplicação. Está classe é responsável por desenhar os carros e acionar o
 * movimento do veículo principal (veículo do usuário).
 *
 * @author Lucas e Cleybson
 *
 * @see MainLoop
 */
public class CarroFrame extends JFrame {

    private MainLoop loop = new MainLoop(this, 60); //variável que guarda o loop da movimentação.
    private ArrayList<ControllerCarro> carros; //array contendo os carros presentes na via.
    private Controller controller;
    private Image background; //imagem de fundo que representa as vias e o cruzamento.

    /**
     * Contrutor que define características à tela. Aqui são definidos as
     * dimensoes da tela e desabilita a possibilidade de mudar o tamanho da
     * tela.
     *
     * @see JFrame
     */
    public CarroFrame() {
        super("Cruzamento");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(482, 480);
        this.setResizable(false);
        this.controller = Controller.getInstance();
        this.carros = this.controller.getCarros();
    }

    /**
     * Método que inicia o loop da aplicação responsável pela movimentação dos
     * veículos.
     *
     * @see MainLoop
     */
    public void startMainLoop() {
        //Iniciamos o main loop
        new Thread(loop, "Main loop").start();
    }

    /**
     * Método que carrega a imagem do plano de fundo que representa o ambiente
     * que os veículos se movem.
     *
     * @see Image
     */
    public void setup() {
        try {
            this.background = ImageIO.read(new File("background.png"));
        } catch (IOException ex) {
            System.out.println("\nImagem de fundo não encontrada\n");
        }
        repaint();
    }

    /**
     * Método que aciona a operação no veículo principal da aplicação (carro
     * deste usuário).
     *
     * @see ControllerCarro
     */
    public void processLogics() {
        this.carros = this.controller.getCarros();
        if (controller.getMeuCarro() != null) {
            controller.getMeuCarro().acao();
        }
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(background, 0, 0, null);
        for (ControllerCarro carro : carros) {
            if (carro != null) {
                carro.desenhar((Graphics2D) g);
            }
        }
    }

    /**
     * Método que repinta o frame chamando a função paint.
     */
    public void paintScreen() {
        repaint();
    }
}
