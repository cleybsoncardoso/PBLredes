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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

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
        frame.setSize(300, 300);
        frame.setVisible(true);
        JPanel panel = new JPanel(new GridBagLayout());
        JPanel panel2 = new JPanel();
        panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));
        frame.add(panel2);
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        panel.add(new JLabel("Escolha sua origem"), c);
        String[] od = {"A", "B", "C", "D"};
        olist = new JComboBox(od);
        c.gridx = 1;
        c.gridy = 0;
        panel.add(olist, c);
        c.gridx = 0;
        c.gridy = 1;
        panel.add(new JLabel("Escolha seu Destino"), c);
        dlist = new JComboBox(od);
        c.gridx = 1;
        c.gridy = 2;
        panel.add(dlist, c);
        JButton start = new JButton("START");
        panel2.add(panel);
        panel2.add(start);
        start.addActionListener(this);
       

    }

    public JComboBox getDlist() {
        return dlist;
    }

    public JComboBox getOlist() {
        return olist;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
    }

    public static void main(String[] args) {
        new Inicio();
    }

}
