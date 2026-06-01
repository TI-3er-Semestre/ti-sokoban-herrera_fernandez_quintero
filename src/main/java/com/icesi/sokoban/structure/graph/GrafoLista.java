package com.icesi.sokoban.structure.graph;

import com.icesi.sokoban.structure.CustomLinkedList;
import com.icesi.sokoban.structure.CustomQueue;

public class GrafoLista<T> implements IGrafo<T> {

    private static final int INF = Integer.MAX_VALUE / 2;

    private CustomLinkedList<Vertice<T>> vertices;
    private CustomLinkedList<CustomLinkedList<int[]>> adyacencia;
    private int contador;

    public GrafoLista() {
        this.vertices = new CustomLinkedList<>();
        this.adyacencia = new CustomLinkedList<>();
        this.contador = 0;
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
        vertices.add(new Vertice<>(contenido));
        adyacencia.add(new CustomLinkedList<>());
        contador++;
    }

    @Override
    public void agregarArista(T origen, T destino, int peso) {
        int i = obtenerIndice(origen);
        int j = obtenerIndice(destino);
        if (i == -1 || j == -1) {
            throw new IllegalArgumentException("Uno o ambos vértices no existen en el grafo");
        }
        adyacencia.get(i).add(new int[]{j, peso});
    }

    @Override
    public boolean existeArista(T origen, T destino) {
        int i = obtenerIndice(origen);
        int j = obtenerIndice(destino);
        if (i == -1 || j == -1) return false;
        CustomLinkedList<int[]> vecinos = adyacencia.get(i);
        for (int k = 0; k < vecinos.size(); k++) {
            if (vecinos.get(k)[0] == j) return true;
        }
        return false;
    }

    @Override
    public int obtenerDistancia(T origen, T destino) {
        int i = obtenerIndice(origen);
        int j = obtenerIndice(destino);
        if (i == -1 || j == -1) {
            throw new IllegalArgumentException("Uno o ambos vértices no existen en el grafo");
        }
        CustomLinkedList<int[]> vecinos = adyacencia.get(i);
        for (int k = 0; k < vecinos.size(); k++) {
            if (vecinos.get(k)[0] == j) return vecinos.get(k)[1];
        }
        return INF;
    }

    @Override
    public CustomLinkedList<T> obtenerVecinos(T vertice) {
        int i = obtenerIndice(vertice);
        if (i == -1) throw new IllegalArgumentException("El vértice no existe en el grafo");
        CustomLinkedList<T> vecinos = new CustomLinkedList<>();
        CustomLinkedList<int[]> lista = adyacencia.get(i);
        for (int k = 0; k < lista.size(); k++) {
            vecinos.add(vertices.get(lista.get(k)[0]).getContenido());
        }
        return vecinos;
    }

    @Override
    public int cantidadVertices() { return contador; }

    @Override
    public CustomLinkedList<T> bfs(T origen) {
        int origenIdx = obtenerIndice(origen);
        if (origenIdx == -1) return new CustomLinkedList<>();

        for (int i = 0; i < contador; i++) {
            vertices.get(i).setColor(Vertice.Color.BLANCO);
            vertices.get(i).setDistancia(INF);
            vertices.get(i).setPredecesor(null);
        }

        vertices.get(origenIdx).setColor(Vertice.Color.GRIS);
        vertices.get(origenIdx).setDistancia(0);

        CustomQueue<Integer> cola = new CustomQueue<>();
        cola.enqueue(origenIdx);

        CustomLinkedList<T> resultado = new CustomLinkedList<>();

        while (!cola.isEmpty()) {
            int u = cola.dequeue();
            resultado.add(vertices.get(u).getContenido());

            CustomLinkedList<int[]> vecinos = adyacencia.get(u);
            for (int k = 0; k < vecinos.size(); k++) {
                int v = vecinos.get(k)[0];
                if (vertices.get(v).getColor() == Vertice.Color.BLANCO) {
                    vertices.get(v).setColor(Vertice.Color.GRIS);
                    vertices.get(v).setDistancia(vertices.get(u).getDistancia() + 1);
                    vertices.get(v).setPredecesor(vertices.get(u));
                    cola.enqueue(v);
                }
            }
            vertices.get(u).setColor(Vertice.Color.NEGRO);
        }
        return resultado;
    }

    @Override
    public CustomLinkedList<T> dfs(T origen) {
        for (int i = 0; i < contador; i++) {
            vertices.get(i).setColor(Vertice.Color.BLANCO);
            vertices.get(i).setPredecesor(null);
        }
        CustomLinkedList<T> resultado = new CustomLinkedList<>();
        int origenIdx = obtenerIndice(origen);
        if (origenIdx != -1) dfsVisitar(origenIdx, resultado);
        for (int i = 0; i < contador; i++) {
            if (vertices.get(i).getColor() == Vertice.Color.BLANCO) {
                dfsVisitar(i, resultado);
            }
        }
        return resultado;
    }

    private void dfsVisitar(int u, CustomLinkedList<T> resultado) {
        vertices.get(u).setColor(Vertice.Color.GRIS);
        resultado.add(vertices.get(u).getContenido());
        CustomLinkedList<int[]> vecinos = adyacencia.get(u);
        for (int k = 0; k < vecinos.size(); k++) {
            int v = vecinos.get(k)[0];
            if (vertices.get(v).getColor() == Vertice.Color.BLANCO) {
                vertices.get(v).setPredecesor(vertices.get(u));
                dfsVisitar(v, resultado);
            }
        }
        vertices.get(u).setColor(Vertice.Color.NEGRO);
    }

    @Override
    public int[][] floydWarshall() {
        int n = contador;
        int[][] dist = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                dist[i][j] = (i == j) ? 0 : INF;
            }
        }
        for (int i = 0; i < n; i++) {
            CustomLinkedList<int[]> vecinos = adyacencia.get(i);
            for (int k = 0; k < vecinos.size(); k++) {
                int j = vecinos.get(k)[0];
                int peso = vecinos.get(k)[1];
                dist[i][j] = peso;
            }
        }
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (dist[i][k] == INF || dist[k][j] == INF) continue;
                    if (dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                    }
                }
            }
        }
        return dist;
    }

    @Override
    public CustomLinkedList<int[]> prim() {
        if (contador == 0) return new CustomLinkedList<>();
        boolean[] visitado = new boolean[contador];
        int[] menorPeso = new int[contador];
        int[] padre = new int[contador];
        for (int i = 0; i < contador; i++) {
            menorPeso[i] = INF;
            padre[i] = -1;
        }
        menorPeso[0] = 0;
        for (int i = 0; i < contador; i++) {
            int u = -1;
            for (int v = 0; v < contador; v++) {
                if (!visitado[v] && (u == -1 || menorPeso[v] < menorPeso[u])) u = v;
            }
            visitado[u] = true;
            CustomLinkedList<int[]> vecinos = adyacencia.get(u);
            for (int k = 0; k < vecinos.size(); k++) {
                int v = vecinos.get(k)[0];
                int peso = vecinos.get(k)[1];
                if (!visitado[v] && peso < menorPeso[v]) {
                    menorPeso[v] = peso;
                    padre[v] = u;
                }
            }
        }
        CustomLinkedList<int[]> mst = new CustomLinkedList<>();
        for (int v = 1; v < contador; v++) {
            if (padre[v] != -1) mst.add(new int[]{padre[v], v, menorPeso[v]});
        }
        return mst;
    }
}