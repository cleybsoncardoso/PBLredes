/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

<<<<<<< HEAD
import Model.TrataCliente;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author cleyb
 */
public class Controller implements Runnable {

    private Socket cliente;
    private ServerSocket servidor;
    private TrataCliente tc;

    public Controller(Socket cliente, ServerSocket servidor) {
        this.cliente = cliente;
        this.servidor = servidor;
        tc = new TrataCliente();
    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

=======
import model.Servidor;

/**
 *
 * @author paiva
 */
public class Controller {
    
    private Servidor servidor;
    private final int porta = 12345;

    public void iniciarServidor() {
        servidor = new Servidor(porta);
        new Thread(servidor).start();
    }
    
>>>>>>> 85c279a371044bb662fc6867e93ba00cc8b92925
}
