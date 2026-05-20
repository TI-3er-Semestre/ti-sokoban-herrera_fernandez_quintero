package com.icesi.sokoban.structure.graph;

import com.icesi.sokoban.structure.CustomLinkedList;

/**
 * Contrato de un grafo generico ponderado.
 *
 * Define las operaciones que cualquier implementacion de grafo debe
 * ofrecer, sin importar si por dentro usa matriz o listas de adyacencia.
 *
 * NOTA: esta interfaz pertenece a la estructura base del grafo (Persona A).
 * Se incluye aqui para que el codigo de Persona C compile de forma
 * independiente. Al integrar, debe quedar una sola version compartida.
 *
 * @param <T> tipo del contenido de los vertices
 */
public interface IGrafo<T> {

    /**
     * Agrega un vertice nuevo al grafo.
     * @param contenido valor del vertice
     */
    void agregarVertice(T contenido);

    /**
     * Agrega una arista dirigida con peso entre dos vertices existentes.
     * @param origen  contenido del vertice origen
     * @param destino contenido del vertice destino
     * @param peso    peso de la arista
     */
    void agregarArista(T origen, T destino, int peso);

    /**
     * Indica si existe una arista directa entre dos vertices.
     */
    boolean existeArista(T origen, T destino);

    /**
     * Devuelve el peso de la arista directa entre dos vertices,
     * o infinito si no hay arista directa.
     */
    int obtenerPeso(T origen, T destino);

    /**
     * Devuelve la lista de vertices vecinos (adyacentes) de un vertice.
     */
    CustomLinkedList<T> obtenerVecinos(T contenido);

    /**
     * Cantidad de vertices del grafo.
     */
    int cantidadVertices();

    /**
     * Recorrido en amplitud desde un vertice origen.
     * @return vertices en orden de visita
     */
    CustomLinkedList<T> bfs(T origen);

    /**
     * Recorrido en profundidad desde un vertice origen.
     * @return vertices en orden de visita
     */
    CustomLinkedList<T> dfs(T origen);

    /**
     * Calcula la distancia minima entre todos los pares de vertices.
     * @return matriz de distancias minimas
     */
    int[][] floydWarshall();
}
