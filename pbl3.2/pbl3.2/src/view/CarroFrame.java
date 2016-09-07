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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
 *
 * @author paiva
 */
public class CarroFrame extends JFrame {

    private MainLoop loop = new MainLoop(this, 60);
    private ArrayList<ControllerCarro> carros;
    private Controller controller;

    //os controllers estarao no controller principal e quando for desenhar percorrer a lista de controllers e chamar o metodo de desenhar
    //para isso é necessario que cada controller ja tenha iniciado seu veiculo
    private Image background;

    public CarroFrame(Controller controller) {
        super("Cruzamento");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(482, 480);
        setResizable(false);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                //Se apertar o x, paramos o loop.
                loop.stop();
            }
        });
        this.controller = controller;
        this.carros = this.controller.getCarros();
    }

    public void startMainLoop() {
        //Iniciamos o main loop
        new Thread(loop, "Main loop").start();
    }

    public void setup() {
        //Subtrai a decora��o da janela da largura e altura m�ximas
        //percorridas pela bola.
        try {
            this.background = ImageIO.read(new File("background.png"));
        } catch (IOException ex) {
            Logger.getLogger(CarroFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        repaint();
    }

    public void processLogics() {
        this.carros = this.controller.getCarros();
        controller.getCarro(0).acao();
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

    public void paintScreen() {
        repaint();
    }
}
