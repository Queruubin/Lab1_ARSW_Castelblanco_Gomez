/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.threads.punto1;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author samulito
 */
public class CountThread1 extends Thread{
    private int inicio;
    private int fin;
    private String nombreHilo;
    private List<Integer> arr;

    public CountThread1(String nombreHilo, int inicio, int fin) {
        this.nombreHilo = nombreHilo;
        this.inicio = inicio;
        this.fin = fin;
        this.arr = new ArrayList<>();
    }

    public List<Integer> devolverArr() {
        return arr;
    }

    @Override
    public void run() {
        for (int i = inicio; i < fin; i++) {
            arr.add(i);
        }

    }
}
