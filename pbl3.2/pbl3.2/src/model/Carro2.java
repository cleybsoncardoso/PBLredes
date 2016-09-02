/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.net.Socket;
import java.util.ArrayList;
import util.Cliente;
import util.Quadrante;
import util.Coordenada;

/**
 *
 * @author cleyb
 */
public class Carro2 {
    private String origem, destino;
    private Coordenada coordenadaAtual;
    private ArrayList<Quadrante> trajeto;
    private Cliente cliente;

    public Carro2(String origem, String destino, Coordenada coordenadaAtual, ArrayList<Quadrante> trajeto) {
        this.origem = origem;
        this.destino = destino;
        this.coordenadaAtual = coordenadaAtual;
        this.trajeto = trajeto;
        this.cliente=cliente;
    }

    public String getOrigem() {
        return origem;
    }

    public String getDestino() {
        return destino;
    }

    public Coordenada getCoordenadaAtual() {
        return coordenadaAtual;
    }

    public ArrayList<Quadrante> getTrajeto() {
        return trajeto;
    }
    
}
