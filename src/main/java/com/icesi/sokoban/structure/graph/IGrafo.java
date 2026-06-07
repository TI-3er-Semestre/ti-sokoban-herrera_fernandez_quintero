package com.icesi.sokoban.structure.graph;

import com.icesi.sokoban.structure.CustomLinkedList;

public interface IGrafo<T> {

    void agregarVertice(T contenido);

    void agregarArista(T origen, T destino, int peso);

    boolean existeArista(T origen, T destino);

    int obtenerDistancia(T origen, T destino);

    CustomLinkedList<T> obtenerVecinos(T vertice);

    int cantidadVertices();

    CustomLinkedList<T> bfs(T origen);

    CustomLinkedList<T> dfs(T origen);

    int[][] floydWarshall();

    CustomLinkedList<int[]> prim();

    CustomLinkedList<Arista<T>> kruskal();

    int costoAGM();
}

