/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import View.Texto;
import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cleyb
 */
public class AtualizacaoPeriodica implements Runnable{

    private long ultimaAtualizacao;
    private Texto texto;

    public AtualizacaoPeriodica(long ultimaAtualizacao, Texto texto) {
        this.ultimaAtualizacao = ultimaAtualizacao;
        this.texto = texto;
    }
            
    
    @Override
    public void run() {
        while(true){
            try {
                sleep(100);
                if((System.currentTimeMillis()-ultimaAtualizacao)>3000){
                    texto.atualizarTextArea();
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(AtualizacaoPeriodica.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }
    
    public void atualiza(long ultima){
        ultimaAtualizacao=ultima;
    }
    
}
