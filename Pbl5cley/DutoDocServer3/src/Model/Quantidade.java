/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author cleyb
 */
public class Quantidade {
    
    private String nome;
    private int cont;

    public Quantidade(String nome) {
        this.nome = nome;
        cont = 1;
    }
    
    public void increment(){
        cont=cont+1;
    } 
    
     public void decrement(){
        cont= cont -1;
    } 

    public String getNome() {
        return nome;
    }

    public int getCont() {
        return cont;
    }
    
    
    
    
}
