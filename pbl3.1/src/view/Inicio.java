/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controller.Controller;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Frame responsável pela exibição do menu inicial e visualização das mensagens
 * trocada entre usuários.
 *
 * @author Lucas e Cleybson
 */
public class Inicio extends JFrame implements ActionListener {

    private JComboBox dlist, olist;
    private Controller controller;
    private JTextArea comunicacao;
    private String atual;
    private static Inicio inicio;
    private JPanel panel;

    /**
     * Método que inicia a classe para ser visualizada estaticamente.
     *
     * @param controller
     * @return
     */
    public static Inicio novoController(Controller controller) {
        inicio = new Inicio(controller);
        return inicio;
    }

    /**
     * Método que retorna a instancia atual da classe.
     *
     * @return
     */
    public static Inicio getInstance() {
        return inicio;
    }

    /**
     * Construtor que configura as dimensões e operações da tela bem como o
     * arranjo dos botoões.
     *
     * @param controller
     */
    public Inicio(Controller controller) {
        super("Rota");
        this.controller = controller;
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 100);
        panel = new JPanel(new FlowLayout());
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

    /**
     * Método que torna visível as mensagens trocadas entre os usuários.
     */
    private void mensagens() {
        setSize(600, 600);
        atual = "";
        remove(panel);
        Container cont = this.getContentPane();
        cont.setLayout(new BorderLayout());
        comunicacao = new JTextArea();
        comunicacao.setEditable(false);
        JScrollPane scroll = new JScrollPane(comunicacao);
        cont.add(scroll, BorderLayout.CENTER);
        setVisible(true);
    }

    /**
     * Método que adiciona uma mensagem na tela de mensagens.
     *
     * @param msg
     */
    public void mostrar(String msg) {
        atual = atual + "\n" + msg;
        if (comunicacao != null) {
            comunicacao.setText(atual);
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (olist.getSelectedItem().toString().equals(dlist.getSelectedItem().toString())) {
            JOptionPane.showMessageDialog(rootPane, "coloque destino diferente da origem");
        } else {
            this.mensagens();
            controller.adicionarCarro(olist.getSelectedItem().toString(), dlist.getSelectedItem().toString());//primeiro cliente
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

}
