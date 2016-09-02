/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controller.ControllerCarro;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
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
    private Rectangle2D car;

    private long previous = System.currentTimeMillis();

    private ControllerCarro control;

    //os controllers estarao no controller principal e quando for desenhar percorrer a lista de controllers e chamar o metodo de desenhar
    //para isso é necessario que cada controller ja tenha iniciado seu veiculo
    private int i = 0;

    private Image background;

    public CarroFrame() {
        super("Cruzamento");
        i = 0;
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
    }

    public void startMainLoop() {
        //Iniciamos o main loop
        new Thread(loop, "Main loop").start();
    }

    public void setup() {
        //Subtrai a decora��o da janela da largura e altura m�ximas
        //percorridas pela bola.
        control = new ControllerCarro(getWidth() - getInsets().left - getInsets().right,
                getHeight() - getInsets().top - getInsets().bottom);

        try {
            this.background = ImageIO.read(new File("background.png"));
        } catch (IOException ex) {
            Logger.getLogger(CarroFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

        repaint();
    }

    public void processLogics() {
        //Calcula o tempo entre dois updates
        //long time = System.currentTimeMillis() - previous;

        control.acao();

        //previous = System.currentTimeMillis();
        //i = 1;
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(background, 0, 0, null);
        i = 1;
        if (control != null) {
            control.desenhar((Graphics2D) g);
            //carro.draw((Graphics2D) g); //Desenhamos a bola
        }
//        if (control != null) {
//            control.desenhar((Graphics2D) g); // desenha o carro
//        }
        // g.dispose(); //Liberamos o contexto criado.
    }

    public void paintScreen() {
        repaint();
    }

    public void entrarCarro() {
        control = new ControllerCarro(getWidth() - getInsets().left - getInsets().right,
                getHeight() - getInsets().top - getInsets().bottom);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                CarroFrame bf = new CarroFrame();
                bf.setVisible(true);
                bf.startMainLoop();
            }
        });
    }

}
