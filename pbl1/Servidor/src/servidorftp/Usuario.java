/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorftp;

import java.io.Serializable;
import java.net.Socket;

/**
 *
 * @author cleyb
 */
public class Usuario implements Serializable{
    String login, senha;
    Boolean especial,online;

    public Usuario(String login, String senha, Boolean especial) {
        this.login = login;
        this.senha = senha;
        this.especial = especial;
        this.online=false;
    }

    public String getLogin() {
        return login;
    }

    public String getSenha() {
        return senha;
    }

    public Boolean getEspecial() {
        return especial;
    }
    public Boolean getOnline(){
        return online;
    }

    public void setCliente(Boolean online) {
        this.online = online;
    }
    
}
