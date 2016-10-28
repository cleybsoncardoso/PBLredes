/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dutodocscliente;

import Controller.Controller;
import View.Tela;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cleyb
 */
public class DutoDocsCliente {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Iniciando servidor");
        try {
            Controller controller = new Controller("192.168.0.4");
            Tela tela = new Tela(controller);
            tela.setVisible(true);
        } catch (NotBoundException ex) {
            Logger.getLogger(DutoDocsCliente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(DutoDocsCliente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(DutoDocsCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
