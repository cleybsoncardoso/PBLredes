/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.net.Socket;
import model.Carro;

/**
 *
 * @author paiva
 */
public class TrataCliente implements Runnable{

    private Socket cliente;
    private Carro carro;
    
    public TrataCliente(Socket cliente, Carro carro){
        this.cliente = cliente;
        this.carro = carro;
    }
    
    @Override
    public void run() {
    }
    
    
    
}
