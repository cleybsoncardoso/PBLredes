/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Controller.Controller;

/**
 *
 * @author cleyb
 */
public class Atualizacao implements Runnable {

    private Controller controller;

    public Atualizacao(Controller controller) {
        this.controller = controller;
    }
    
    
    
    @Override
    public void run() {
        
        while(controller.getTitulo()!=null){
            String[] operacoes = controller.requisicao(controller.getNome()+controller.getTitulo());
            switch (operacoes[0]){
                case "incrementa":
                    
            }
        }
    }
    
}
