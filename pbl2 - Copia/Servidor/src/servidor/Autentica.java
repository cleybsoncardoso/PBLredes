/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author paiva
 */
public class Autentica implements Runnable{

    Autentica(int porta, String ip) throws IOException{
        Socket cliente = new Socket(ip, porta);
    }
    
    @Override
    public void run() {
        
        
        
    }
    
}
