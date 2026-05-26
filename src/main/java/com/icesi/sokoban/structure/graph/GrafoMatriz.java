package com.icesi.sokoban.structure.graph;

import com.icesi.sokoban.structure.CustomLinkedList;

public class GrafoMatriz<T> implements IGrafo<T> {

    private static final int INF = Integer.MAX_VALUE / 2;

    private int[][] matrizAdyacencia;
    private CustomLinkedList<Vertice<T>> vertices;
    private int capacidad;
    private int contador;
    private int tiempo;

    // Floyd-Warshall: matriz de predecesores para reconstruir caminos
    private int[][] siguiente;

    public GrafoMatriz() {
        this.capacidad = 10;
        this.contador = 0;
        this.tiempo = 0;
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

    private void redimensionar() {
        int nuevaCapacidad = capacidad * 2;
        int[][] nuevaMatriz = new int[nuevaCapacidad][nuevaCapacidad];
        for (int i = 0; i < nuevaCapacidad; i++) {
            for (int j = 0; j < nuevaCapacidad; j++) {
                nuevaMatriz[i][j] = (i == j) ? 0 : INF;
            }
        }
        for (int i = 0; i < capacidad; i++) {
            for (int j = 0; j < capacidad; j++) {
                nuevaMatriz[i][j] = matrizAdyacencia[i][j];
            }
        }
        matrizAdyacencia = nuevaMatriz;
        capacidad = nuevaCapacidad;
    }

    private int obtenerIndice(T contenido) {
        for (int i = 0; i < contador; i++) {
            if (vertices.get(i).getContenido().equals(contenido)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void agregarVertice(T contenido) {
        if (contador == capacidad) {
            redimensionar();
        }
        vertices.add(new Vertice<>(contenido));
        contador++;
    }

    @Override
    public void agregarArista(T origen, T destino, int peso) {
        int i = obtenerIndice(origen);
        int j = obtenerIndice(destino);
        if (i == -1 || j == -1) {
            throw new IllegalArgumentException("Uno o ambos vértices no existen en el grafo");
        }
        matrizAdyacencia[i][j] = peso;
    }

    @Override
    public boolean existeArista(T origen, T destino) {
        int i = obtenerIndice(origen);
        int j = obtenerIndice(destino);
        if (i == -1 || j == -1) {
            return false;
        }
        return matrizAdyacencia[i][j] != INF;
    }

    @Override
    public int obtenerDistancia(T origen, T destino) {
        int i = obtenerIndice(origen);
        int j = obtenerIndice(destino);
        if (i == -1 || j == -1) {
            throw new IllegalArgumentException("Uno o ambos vértices no existen en el grafo");
        }
        return matrizAdyacencia[i][j];
    }

    @Override
    public CustomLinkedList<T> obtenerVecinos(T vertice) {
        int i = obtenerIndice(vertice);
        if (i == -1) {
            throw new IllegalArgumentException("El vértice no existe en el grafo");
        }
        CustomLinkedList<T> vecinos = new CustomLinkedList<>();
        for (int j = 0; j < contador; j++) {
            if (matrizAdyacencia[i][j] != INF && i != j) {
                vecinos.add(vertices.get(j).getContenido());
            }
        }
        return vecinos;
    }

    @Override
    public int cantidadVertices() { return contador; }

    @Override
    public CustomLinkedList<T> dfs(T origen) {
        for (int i = 0; i < contador; i++) {
            vertices.get(i).setColor(Vertice.Color.BLANCO);
            vertices.get(i).setPredecesor(null);
        }
        tiempo = 0;
        CustomLinkedList<T> resultado = new CustomLinkedList<>();

        int origenIdx = obtenerIndice(origen);
        if (origenIdx != -1) {
            dfsVisitar(origenIdx, resultado);
        }
        for (int i = 0; i < contador; i++) {
            if (vertices.get(i).getColor() == Vertice.Color.BLANCO) {
                dfsVisitar(i, resultado);
            }
        }
        return resultado;
    }

    private void dfsVisitar(int u, CustomLinkedList<T> resultado) {
        tiempo++;
        vertices.get(u).setTiempoDescubrimiento(tiempo);
        vertices.get(u).setColor(Vertice.Color.GRIS);
        resultado.add(vertices.get(u).getContenido());

        for (int v = 0; v < contador; v++) {
            if (matrizAdyacencia[u][v] != INF && u != v) {
                if (vertices.get(v).getColor() == Vertice.Color.BLANCO) {
                    vertices.get(v).setPredecesor(vertices.get(u));
                    dfsVisitar(v, resultado);
                }
            }
        }

        vertices.get(u).setColor(Vertice.Color.NEGRO);
        tiempo++;
        vertices.get(u).setTiempoFinalizacion(tiempo);
    }

    @Override
    public CustomLinkedList<T> bfs(T origen) { return null; }

    // =========================================================================
    // FLOYD-WARSHALL  (Persona C)
    // =========================================================================

    /**
     * Floyd-Warshall: camino minimo entre TODOS los pares de vertices.
     * Programacion dinamica. Complejidad Theta(V^3).
     */
    @Override
    public int[][] floydWarshall() {
        int n = contador;
        int[][] dist = new int[n][n];
        siguiente = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                dist[i][j] = matrizAdyacencia[i][j];
                if (i != j && matrizAdyacencia[i][j] != INF) {
                    siguiente[i][j] = j;
                } else {
                    siguiente[i][j] = -1;
                }
            }
        }

        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (dist[i][k] == INF || dist[k][j] == INF) continue;
                    int pasandoPorK = dist[i][k] + dist[k][j];
                    if (pasandoPorK < dist[i][j]) {
                        dist[i][j] = pasandoPorK;
                        siguiente[i][j] = siguiente[i][k];
                    }
                }
            }
        }
        return dist;
    }

    @Override
    public CustomLinkedList<int[]> prim() {
        return null;
    }

    /**
     * Reconstruye el camino minimo exacto entre dos vertices.
     * Requiere haber ejecutado floydWarshall() antes.
     */
    public CustomLinkedList<T> reconstruirCamino(T origen, T destino) {
        if (siguiente == null) {
            throw new IllegalStateException("Debe ejecutar floydWarshall() antes");
        }
        int i = obtenerIndice(origen);
        int j = obtenerIndice(destino);
        if (i == -1 || j == -1) {
            throw new IllegalArgumentException("Origen o destino no existe en el grafo");
        }
        CustomLinkedList<T> camino = new CustomLinkedList<>();
        if (i != j && siguiente[i][j] == -1) return camino;

        camino.add(vertices.get(i).getContenido());
        while (i != j) {
            i = siguiente[i][j];
            camino.add(vertices.get(i).getContenido());
        }
        return camino;
    }
}