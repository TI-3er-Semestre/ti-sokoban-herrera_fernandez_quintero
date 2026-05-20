package com.icesi.sokoban.structure.graph;

import com.icesi.sokoban.structure.CustomLinkedList;

public class GrafoMatriz<T> implements IGrafo<T> {

    private static final int INF = Integer.MAX_VALUE / 2;

    private int[][] matrizAdyacencia;
    private CustomLinkedList<Vertice<T>> vertices;
    private int capacidad;
    private int contador;
    private int tiempo;

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

    @Override
    public int[][] floydWarshall() { return null; }
}
