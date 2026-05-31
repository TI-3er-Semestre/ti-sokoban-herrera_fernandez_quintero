package com.icesi.sokoban.structure.graph;

import com.icesi.sokoban.structure.CustomLinkedList;
import com.icesi.sokoban.structure.CustomQueue;

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
    public CustomLinkedList<T> bfs(T origen) {
        int origenIdx = obtenerIndice(origen);
        if (origenIdx == -1) return new CustomLinkedList<>();

        // Inicializar todos los vértices
        for (int i = 0; i < contador; i++) {
            vertices.get(i).setColor(Vertice.Color.BLANCO);
            vertices.get(i).setDistancia(INF);
            vertices.get(i).setPredecesor(null);
        }

        // Configurar el origen
        vertices.get(origenIdx).setColor(Vertice.Color.GRIS);
        vertices.get(origenIdx).setDistancia(0);

        // Usar la CustomQueue del proyecto
        CustomQueue<Integer> cola = new CustomQueue<>();
        cola.enqueue(origenIdx);

        CustomLinkedList<T> resultado = new CustomLinkedList<>();

        while (!cola.isEmpty()) {
            int u = cola.dequeue();
            resultado.add(vertices.get(u).getContenido());

            // Revisar todos los vecinos
            for (int v = 0; v < contador; v++) {
                if (matrizAdyacencia[u][v] != INF && u != v) {
                    if (vertices.get(v).getColor() == Vertice.Color.BLANCO) {
                        vertices.get(v).setColor(Vertice.Color.GRIS);
                        vertices.get(v).setDistancia(vertices.get(u).getDistancia() + 1);
                        vertices.get(v).setPredecesor(vertices.get(u));
                        cola.enqueue(v);
                    }
                }
            }
            vertices.get(u).setColor(Vertice.Color.NEGRO);
        }
        return resultado;
    }


    // FLOYD-WARSHALL


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
        if (contador == 0) return new CustomLinkedList<>();

        boolean[] visitado = new boolean[contador]; // ¿ya está en el árbol?
        int[] menorPeso = new int[contador];        // menor costo para llegar
        int[] padre = new int[contador];            // de dónde vine

        // Al inicio nadie está conectado
        for (int i = 0; i < contador; i++) {
            menorPeso[i] = INF;
            padre[i] = -1;
        }

        // Arranco desde el vértice 0
        menorPeso[0] = 0;

        for (int i = 0; i < contador; i++) {

            //Busco el vértice más barato que aún no haya visitado
            int u = -1;
            for (int v = 0; v < contador; v++) {
                if (!visitado[v] && (u == -1 || menorPeso[v] < menorPeso[u])) {
                    u = v;
                }
            }

            //Lo marco como visitado
            visitado[u] = true;

            //Reviso sus vecinos: ¿puedo llegar más barato desde u?
            for (int v = 0; v < contador; v++) {
                boolean hayConexion = matrizAdyacencia[u][v] != INF;
                boolean esMasBarato = matrizAdyacencia[u][v] < menorPeso[v];
                if (!visitado[v] && hayConexion && esMasBarato) {
                    menorPeso[v] = matrizAdyacencia[u][v];
                    padre[v] = u;
                }
            }
        }

        //Armo la lista de aristas del árbol resultante
        CustomLinkedList<int[]> mst = new CustomLinkedList<>();
        for (int v = 1; v < contador; v++) {
            if (padre[v] != -1) {
                mst.add(new int[]{padre[v], v, menorPeso[v]});
            }
        }
        return mst;
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


    // KRUSKAL


    /**
     * Algoritmo de Kruskal: encuentra el ARBOL GENERADOR MINIMO (AGM).
     *
     * Voraz (greedy):
     *   1. Ordenar TODAS las aristas de menor a mayor peso.
     *   2. Recorrerlas en orden. Si los extremos estan en conjuntos
     *      distintos, agregar al AGM y unir los conjuntos. Si ya estan
     *      en el mismo conjunto, descartar (formaria ciclo).
     *   3. Terminar al tener n-1 aristas.
     *
     * El grafo se trata como NO DIRIGIDO: solo se procesa i < j.
     *
     * Complejidad: O(E log E) dominado por el ordenamiento.
     *
     * @return lista de aristas del AGM
     */
    public CustomLinkedList<Arista<T>> kruskal() {
        CustomLinkedList<Arista<T>> agm = new CustomLinkedList<>();
        if (contador == 0) return agm;

        // 1. Recolectar todas las aristas distintas (solo i < j).
        int maxAristas = contador * (contador - 1) / 2;
        @SuppressWarnings("unchecked")
        Arista<T>[] aristas = new Arista[maxAristas];
        int total = 0;

        int INF = Integer.MAX_VALUE / 2;
        for (int i = 0; i < contador; i++) {
            for (int j = i + 1; j < contador; j++) {
                int p1 = matrizAdyacencia[i][j];
                int p2 = matrizAdyacencia[j][i];
                int peso = (p1 != INF) ? p1 : p2;
                if (peso != INF) {
                    aristas[total++] = new Arista<>(
                            vertices.get(i).getContenido(),
                            vertices.get(j).getContenido(),
                            peso);
                }
            }
        }

        // 2. Ordenar por peso ascendente (selection sort).
        for (int i = 0; i < total - 1; i++) {
            int idxMin = i;
            for (int j = i + 1; j < total; j++) {
                if (aristas[j].compareTo(aristas[idxMin]) < 0) idxMin = j;
            }
            if (idxMin != i) {
                Arista<T> t = aristas[i];
                aristas[i] = aristas[idxMin];
                aristas[idxMin] = t;
            }
        }

        // 3. Recorrer aristas con Union-Find descartando ciclos.
        UnionFind uf = new UnionFind(contador);
        for (int k = 0; k < total; k++) {
            Arista<T> a = aristas[k];
            int iU = obtenerIndice(a.getOrigen());
            int iV = obtenerIndice(a.getDestino());
            if (uf.union(iU, iV)) {
                agm.add(a);
                if (agm.size() == contador - 1) break;
            }
        }
        return agm;
    }

    /** Costo total del AGM: suma de los pesos de las aristas devueltas por kruskal(). */
    public int costoAGM() {
        CustomLinkedList<Arista<T>> agm = kruskal();
        int total = 0;
        for (int i = 0; i < agm.size(); i++) {
            total += agm.get(i).getPeso();
        }
        return total;
    }
}