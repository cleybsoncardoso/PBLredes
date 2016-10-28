/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dutodocsserver;

import Controller.Controller;

/**
 *
 * @author cleyb
 */
public class DutoDocsServer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Controller controller = new Controller("192.168.25.83");
    }
    
}
