/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cleyb
 */
class TratarCliente implements Runnable {

    private Servidor servidor;
    private Socket cliente;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    public TratarCliente(Servidor servidor, Socket cliente) {
        this.cliente = cliente;
        this.servidor = servidor;
        try {
            input = new ObjectInputStream(cliente.getInputStream());
            output = new ObjectOutputStream(cliente.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(TratarCliente.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void run() {
        System.out.println("esperando opção do cliente " + cliente.getInetAddress().getHostAddress());
        String opcaoCliente = null;
        try {
            opcaoCliente = input.readObject().toString();
            switch (opcaoCliente) {
                case "cadastro":
                    System.out.println("Opcao escolhida por " + cliente + " foi cadastro");
                    input.readObject().toString();
                    input.readObject().toString();
                    output.writeObject("cadastrado");
            }
        } catch (IOException ex) {
            Logger.getLogger(TratarCliente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TratarCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
