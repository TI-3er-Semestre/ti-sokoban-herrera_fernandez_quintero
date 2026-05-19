package com.icesi.sokoban.structure.graph;

import com.icesi.sokoban.structure.CustomLinkedList;

public class GrafoMatriz<T> implements IGrafo<T> {

    private static final int INF = Integer.MAX_VALUE / 2;

    private int[][] matrizAdyacencia;
    private CustomLinkedList<Vertice<T>> vertices;
    private int capacidad;
    private int contador;

    public GrafoMatriz() {
        this.capacidad = 10;
        this.contador = 0;
        this.vertices = new CustomLinkedList<>();
        this.matrizAdyacencia = new int[capacidad][capacidad];
        inicializarMatriz();
    }

    private void inicializarMatriz() {
        for (int i = 0; i < capacidad; i++) {
            for (int j = 0; j < capacidad; j++) {
                matrizAdyacencia[i][j] = (i == j) ? 0 : INF;
            }
        }
    }

    @Override
    public void agregarVertice(T contenido) { }

    @Override
    public void agregarArista(T origen, T destino, int peso) { }

    @Override
    public boolean existeArista(T origen, T destino) { return false; }

    @Override
    public int obtenerDistancia(T origen, T destino) { return INF; }

    @Override
    public CustomLinkedList<T> obtenerVecinos(T vertice) { return null; }

    @Override
    public int cantidadVertices() { return contador; }

    @Override
    public CustomLinkedList<T> bfs(T origen) { return null; }

    @Override
    public CustomLinkedList<T> dfs(T origen) { return null; }

    @Override
    public int[][] floydWarshall() { return null; }
}
