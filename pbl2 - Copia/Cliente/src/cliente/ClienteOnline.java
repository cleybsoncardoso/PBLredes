/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

/**
 *
 * @author cleyb
 */
public class ClienteOnline {
    private String nomeCLiente;

    public ClienteOnline() {
        nomeCLiente="deslogado";
    }

    public String getNomeCLiente() {
        return nomeCLiente;
    }

    public void setNomeCLiente(String nomeCLiente) {
        this.nomeCLiente = nomeCLiente;
    }
    
}
