package com.icesi.sokoban.structure.graph;

/**
 * Arista de un grafo. Implementa Comparable para ordenarse por peso,
 * que es lo que Kruskal necesita en su primer paso.
 */
public class Arista<T> implements Comparable<Arista<T>> {

    private final T origen;
    private final T destino;
    private final int peso;

    public Arista(T origen, T destino, int peso) {
        this.origen = origen;
        this.destino = destino;
        this.peso = peso;
    }

    public T getOrigen()  { return origen; }
    public T getDestino() { return destino; }
    public int getPeso()  { return peso; }

    @Override
    public int compareTo(Arista<T> otra) {
        return Integer.compare(this.peso, otra.peso);
    }

    @Override
    public String toString() {
        return "(" + origen + " -- " + destino + ", peso=" + peso + ")";
    }
}