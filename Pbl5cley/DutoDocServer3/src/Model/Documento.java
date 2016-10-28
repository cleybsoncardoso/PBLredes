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
public class Documento {

    private String nome, conteudo;
    
    
    public Documento(String nome, String conteudo) {
        this.nome = nome;
        this.conteudo = conteudo;
    }
    
    public int length(){
        return conteudo.length();
    }

    public String getNome() {
        return nome;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

}
