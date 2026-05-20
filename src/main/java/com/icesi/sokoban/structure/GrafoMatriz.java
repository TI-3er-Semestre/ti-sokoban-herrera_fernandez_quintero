package com.icesi.sokoban.structure.graph;

import com.icesi.sokoban.structure.CustomLinkedList;
import com.icesi.sokoban.structure.CustomQueue;

/**
 * Grafo dirigido y ponderado implementado con MATRIZ DE ADYACENCIA.
 *
 * Representacion interna:
 *   - vertices: lista de los vertices del grafo (cada uno con su contenido).
 *   - matriz:   matriz cuadrada int[n][n] donde matriz[i][j] es el peso
 *               de la arista del vertice i al vertice j.
 *               Se inicializa con INFINITO donde no hay arista y con 0
 *               en la diagonal (costo de un vertice a si mismo).
 *
 * Esta clase es compartida por el equipo. Cada persona implementa una
 * seccion, marcada con comentarios para facilitar el merge:
 *
 *   - Estructura base + DFS  ........  Persona A
 *   - BFS + Dijkstra ................  Persona B
 *   - Floyd-Warshall ................  Persona C
 *
 * Las secciones de A y B incluidas aqui son versiones funcionales
 * minimas para que el proyecto compile; al integrar se reemplazan por
 * las versiones definitivas de cada autor.
 *
 * @param <T> tipo del contenido de los vertices
 */
public class GrafoMatriz<T> implements IGrafo<T> {

    /** Valor que representa "sin arista directa". */
    public static final int INFINITO = Integer.MAX_VALUE / 2;

    private final CustomLinkedList<Vertice<T>> vertices;
    private int[][] matriz;
    private int cantidad;
    private final int capacidad;

    /** Reloj para los timestamps del DFS. */
    private int tiempo;

    /**
     * Crea un grafo vacio con capacidad maxima de vertices.
     * @param capacidad numero maximo de vertices que admitira el grafo
     */
    public GrafoMatriz(int capacidad) {
        if (capacidad <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser positiva");
        }
        this.capacidad = capacidad;
        this.vertices = new CustomLinkedList<>();
        this.matriz = new int[capacidad][capacidad];
        this.cantidad = 0;
        inicializarMatriz();
    }

    /** Llena la matriz con INFINITO y pone 0 en la diagonal. */
    private void inicializarMatriz() {
        for (int i = 0; i < capacidad; i++) {
            for (int j = 0; j < capacidad; j++) {
                matriz[i][j] = (i == j) ? 0 : INFINITO;
            }
        }
    }

    // =========================================================================
    // ESTRUCTURA BASE  (Persona A)
    // =========================================================================

    @Override
    public void agregarVertice(T contenido) {
        if (cantidad >= capacidad) {
            throw new IllegalStateException("El grafo alcanzo su capacidad maxima");
        }
        if (indiceDe(contenido) != -1) {
            throw new IllegalArgumentException("El vertice ya existe: " + contenido);
        }
        vertices.add(new Vertice<>(contenido));
        cantidad++;
    }

    @Override
    public void agregarArista(T origen, T destino, int peso) {
        int i = indiceDe(origen);
        int j = indiceDe(destino);
        if (i == -1 || j == -1) {
            throw new IllegalArgumentException(
                    "Origen o destino no existe en el grafo");
        }
        matriz[i][j] = peso;
    }

    @Override
    public boolean existeArista(T origen, T destino) {
        int i = indiceDe(origen);
        int j = indiceDe(destino);
        if (i == -1 || j == -1) {
            return false;
        }
        return i != j && matriz[i][j] != INFINITO;
    }

    @Override
    public int obtenerPeso(T origen, T destino) {
        int i = indiceDe(origen);
        int j = indiceDe(destino);
        if (i == -1 || j == -1) {
            throw new IllegalArgumentException(
                    "Origen o destino no existe en el grafo");
        }
        return matriz[i][j];
    }

    @Override
    public CustomLinkedList<T> obtenerVecinos(T contenido) {
        int i = indiceDe(contenido);
        if (i == -1) {
            throw new IllegalArgumentException("El vertice no existe: " + contenido);
        }
        CustomLinkedList<T> vecinos = new CustomLinkedList<>();
        for (int j = 0; j < cantidad; j++) {
            if (i != j && matriz[i][j] != INFINITO) {
                vecinos.add(vertices.get(j).getContenido());
            }
        }
        return vecinos;
    }

    @Override
    public int cantidadVertices() {
        return cantidad;
    }

    /** Devuelve el indice de un vertice por su contenido, o -1 si no existe. */
    private int indiceDe(T contenido) {
        for (int i = 0; i < cantidad; i++) {
            if (vertices.get(i).getContenido().equals(contenido)) {
                return i;
            }
        }
        return -1;
    }

    /** Reinicia color, distancia, predecesor y timestamps de todos los vertices. */
    private void reiniciarVertices() {
        for (int i = 0; i < cantidad; i++) {
            vertices.get(i).reiniciar();
        }
    }

    // ---- DFS (Persona A) ----------------------------------------------------

    /**
     * Recorrido en profundidad. Version minima provisional de Persona A.
     *
     * Se adentra lo mas posible por un camino antes de retroceder.
     * Usa el sistema de colores y registra timestamps de descubrimiento
     * y finalizacion. Si quedan vertices sin visitar, reinicia desde otro
     * (lo que produce un bosque DF cuando el grafo no es conexo).
     *
     * Complejidad: O(V + E).
     */
    @Override
    public CustomLinkedList<T> dfs(T origen) {
        reiniciarVertices();
        tiempo = 0;
        CustomLinkedList<T> orden = new CustomLinkedList<>();

        int inicio = indiceDe(origen);
        if (inicio == -1) {
            throw new IllegalArgumentException("El vertice origen no existe");
        }

        // Primero el origen pedido, luego el resto (para detectar bosque)
        dfsVisitar(inicio, orden);
        for (int i = 0; i < cantidad; i++) {
            if (vertices.get(i).getColor() == Color.BLANCO) {
                dfsVisitar(i, orden);
            }
        }
        return orden;
    }

    private void dfsVisitar(int i, CustomLinkedList<T> orden) {
        Vertice<T> u = vertices.get(i);
        tiempo++;
        u.setTiempoDescubrimiento(tiempo);
        u.setColor(Color.GRIS);
        orden.add(u.getContenido());

        for (int j = 0; j < cantidad; j++) {
            if (i != j && matriz[i][j] != INFINITO
                    && vertices.get(j).getColor() == Color.BLANCO) {
                vertices.get(j).setPredecesor(u);
                dfsVisitar(j, orden);
            }
        }
        u.setColor(Color.NEGRO);
        tiempo++;
        u.setTiempoFinalizacion(tiempo);
    }

    // =========================================================================
    // BFS y DIJKSTRA  (Persona B)
    // =========================================================================

    /**
     * Recorrido en amplitud. Version minima provisional de Persona B.
     *
     * Explora el grafo por niveles usando una cola FIFO. Calcula la
     * distancia minima en numero de aristas desde el origen y el
     * predecesor de cada vertice (arbol BF).
     *
     * Complejidad: O(V + E).
     */
    @Override
    public CustomLinkedList<T> bfs(T origen) {
        reiniciarVertices();
        CustomLinkedList<T> orden = new CustomLinkedList<>();

        int inicio = indiceDe(origen);
        if (inicio == -1) {
            throw new IllegalArgumentException("El vertice origen no existe");
        }

        Vertice<T> s = vertices.get(inicio);
        s.setColor(Color.GRIS);
        s.setDistancia(0);

        CustomQueue<Integer> cola = new CustomQueue<>();
        cola.enqueue(inicio);

        while (!cola.isEmpty()) {
            int i = cola.dequeue();
            Vertice<T> u = vertices.get(i);
            orden.add(u.getContenido());

            for (int j = 0; j < cantidad; j++) {
                if (i != j && matriz[i][j] != INFINITO) {
                    Vertice<T> v = vertices.get(j);
                    if (v.getColor() == Color.BLANCO) {
                        v.setColor(Color.GRIS);
                        v.setDistancia(u.getDistancia() + 1);
                        v.setPredecesor(u);
                        cola.enqueue(j);
                    }
                }
            }
            u.setColor(Color.NEGRO);
        }
        return orden;
    }

    /**
     * Dijkstra. Version minima provisional de Persona B.
     *
     * Camino minimo desde un origen en un grafo con pesos no negativos.
     *
     * Complejidad: O(V^2) en esta version simple sin heap.
     *
     * @return arreglo con la distancia minima del origen a cada vertice
     */
    public int[] dijkstra(T origen) {
        int inicio = indiceDe(origen);
        if (inicio == -1) {
            throw new IllegalArgumentException("El vertice origen no existe");
        }

        int[] dist = new int[cantidad];
        boolean[] visitado = new boolean[cantidad];
        for (int i = 0; i < cantidad; i++) {
            dist[i] = INFINITO;
        }
        dist[inicio] = 0;

        for (int paso = 0; paso < cantidad; paso++) {
            int u = -1;
            int mejor = INFINITO;
            for (int i = 0; i < cantidad; i++) {
                if (!visitado[i] && dist[i] < mejor) {
                    mejor = dist[i];
                    u = i;
                }
            }
            if (u == -1) {
                break;
            }
            visitado[u] = true;
            for (int v = 0; v < cantidad; v++) {
                if (matriz[u][v] != INFINITO && !visitado[v]) {
                    int nueva = dist[u] + matriz[u][v];
                    if (nueva < dist[v]) {
                        dist[v] = nueva;
                    }
                }
            }
        }
        return dist;
    }

    // =========================================================================
    // FLOYD-WARSHALL  (Persona C)
    // =========================================================================

    // Matriz de predecesores para reconstruir caminos despues de Floyd-Warshall.
    // siguiente[i][j] guarda el primer vertice del camino de i a j.
    private int[][] siguiente;

    /**
     * Algoritmo de Floyd-Warshall: calcula el camino minimo entre TODOS
     * los pares de vertices en una sola ejecucion.
     *
     * Es un algoritmo de programacion dinamica. La idea central es probar
     * cada vertice k como posible intermediario entre cada par (i, j):
     * si pasar por k acorta el camino de i a j, se actualiza la distancia.
     *
     * Formula recursiva aplicada en cada paso:
     *   dist[i][j] = min( dist[i][j], dist[i][k] + dist[k][j] )
     *
     * Funciona con pesos positivos o negativos, pero NO con ciclos de
     * peso negativo (en ese caso las distancias dejan de tener sentido).
     *
     * Complejidad temporal: Theta(V^3) por el triple bucle anidado.
     * Complejidad espacial: O(V^2) por las matrices de distancias y
     * de predecesores.
     *
     * @return matriz de distancias minimas entre todos los pares
     */
    @Override
    public int[][] floydWarshall() {
        int n = cantidad;

        // dist parte como una copia de la matriz de adyacencia actual.
        int[][] dist = new int[n][n];
        siguiente = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                dist[i][j] = matriz[i][j];
                // Si hay arista directa de i a j, el primer paso es j.
                if (i != j && matriz[i][j] != INFINITO) {
                    siguiente[i][j] = j;
                } else {
                    siguiente[i][j] = -1; // sin camino conocido todavia
                }
            }
        }

        // Triple bucle: para cada intermediario k, revisar cada par (i, j).
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    // Evitar desbordamiento: si i->k o k->j no existen, saltar.
                    if (dist[i][k] == INFINITO || dist[k][j] == INFINITO) {
                        continue;
                    }
                    int pasandoPorK = dist[i][k] + dist[k][j];
                    if (pasandoPorK < dist[i][j]) {
                        dist[i][j] = pasandoPorK;
                        // El camino de i a j ahora empieza igual que el de i a k.
                        siguiente[i][j] = siguiente[i][k];
                    }
                }
            }
        }
        return dist;
    }

    /**
     * Reconstruye el camino minimo exacto entre dos vertices despues de
     * haber ejecutado floydWarshall().
     *
     * Usa la matriz de predecesores 'siguiente' para ir saltando de
     * vertice en vertice desde el origen hasta el destino.
     *
     * @param origen  contenido del vertice origen
     * @param destino contenido del vertice destino
     * @return lista de vertices del camino, vacia si no hay camino
     * @throws IllegalStateException si no se ha ejecutado floydWarshall()
     */
    public CustomLinkedList<T> reconstruirCamino(T origen, T destino) {
        if (siguiente == null) {
            throw new IllegalStateException(
                    "Debe ejecutar floydWarshall() antes de reconstruir un camino");
        }
        int i = indiceDe(origen);
        int j = indiceDe(destino);
        if (i == -1 || j == -1) {
            throw new IllegalArgumentException(
                    "Origen o destino no existe en el grafo");
        }

        CustomLinkedList<T> camino = new CustomLinkedList<>();

        // No hay camino conocido entre i y j.
        if (i != j && siguiente[i][j] == -1) {
            return camino;
        }

        camino.add(vertices.get(i).getContenido());
        while (i != j) {
            i = siguiente[i][j];
            camino.add(vertices.get(i).getContenido());
        }
        return camino;
    }
}
