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
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import util.Logica;
import util.Quadrante;

/**
 *
 * @author cleyb
 */
public class Inicio implements ActionListener{

    JFrame frame;
    private JComboBox dlist, olist;

    public Inicio() {
        frame = new JFrame("Rota");
        //frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 100);
        frame.setVisible(true);
        JPanel panel = new JPanel(new FlowLayout());
        frame.add(panel);
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
       

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        
    }

    public static void main(String[] args) {
        new Inicio();
    }

}
