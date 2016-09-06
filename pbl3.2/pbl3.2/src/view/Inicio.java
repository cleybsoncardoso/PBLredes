/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import application.App;
import controller.Controller;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static java.lang.Thread.sleep;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import util.Logica;
import util.Quadrante;
import util.Server;

/**
 *
 * @author cleyb
 */
public class Inicio extends JFrame implements ActionListener {

    private JComboBox dlist, olist;
    private Controller controller;

    public Inicio(Controller controller) {
        super("Rota");
        this.controller = controller;
        //frame.setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 100);
        
        JPanel panel = new JPanel(new FlowLayout());
        add(panel);
        panel.add(new JLabel("Escolha sua origem"));
        String[] od = {"A", "B", "C", "D"};
        olist = new JComboBox(od);
        panel.add(olist);
        panel.add(new JLabel("Escolha seu Destino"));
        dlist = new JComboBox(od);
        panel.add(dlist);
        JButton start = new JButton("START");
        panel.add(start);
        start.addActionListener(this);
        setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (olist.getSelectedItem().toString().equals(dlist.getSelectedItem().toString())) {
            JOptionPane.showMessageDialog(rootPane, "coloque destino diferente da origem");
        } else {
            controller.adicionarCarro(0, olist.getSelectedItem().toString(), dlist.getSelectedItem().toString());//primeiro cliente
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    CarroFrame bf = new CarroFrame(controller);
                    bf.setVisible(true);
                    bf.startMainLoop();
                }
            });
        }
    }

}
