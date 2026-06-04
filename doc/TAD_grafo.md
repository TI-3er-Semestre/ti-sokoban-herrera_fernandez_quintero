# TAD Grafo — Sokoban

## ¿Qué es un Grafo?

Un grafo es una estructura de datos que representa relaciones entre objetos.
Está compuesto por:
- **Vértices (V):** los objetos o nodos del grafo.
- **Aristas (E):** las conexiones entre los vértices, con un peso opcional.

En el contexto del Sokoban, cada estado del tablero (posición del jugador
y de las cajas) es un vértice, y cada movimiento válido es una arista.



## Operaciones del TAD

| Operación | Descripción | Complejidad (Matriz) | Complejidad (Lista) |
|---|---|---|---|
| `agregarVertice(T)` | Agrega un nodo al grafo | O(V²) | O(1) |
| `agregarArista(T, T, int)` | Conecta dos vértices con un peso | O(V) | O(V) |
| `existeArista(T, T)` | Verifica si hay conexión entre dos vértices | O(V) | O(V) |
| `obtenerDistancia(T, T)` | Retorna el peso de la arista entre dos vértices | O(V) | O(V) |
| `obtenerVecinos(T)` | Retorna los vértices conectados a uno dado | O(V) | O(degree) |
| `cantidadVertices()` | Retorna el número de vértices | O(1) | O(1) |



## Algoritmos implementados

| Algoritmo | Descripción | Complejidad (Matriz) | Complejidad (Lista) |
|---|---|---|---|
| `bfs(T)` | Recorrido por niveles desde un origen | O(V²) | O(V+E) |
| `dfs(T)` | Recorrido en profundidad desde un origen | O(V²) | O(V+E) |
| `floydWarshall()` | Caminos mínimos entre todos los pares | O(V³) | O(V³) |
| `prim()` | Árbol de expansión mínima (voraz) | O(V²) | O(V²) |
| `kruskal()` | Árbol de expansión mínima (voraz) | O(E log E) | O(E log E) |



## Representaciones

### GrafoMatriz
Usa una matriz de adyacencia `int[][]` donde `matriz[i][j]` guarda el peso
de la arista entre el vértice `i` y el vértice `j`. Si no hay arista, guarda `INF`.

- **Ventaja:** acceso directo O(1) a cualquier arista.
- **Desventaja:** ocupa siempre V² de memoria aunque haya pocas aristas.
- **Mejor para:** grafos densos (muchas aristas).

### GrafoLista
Usa una lista de adyacencia donde cada vértice tiene su propia lista de vecinos.
Cada vecino se guarda como `[índice, peso]`.

- **Ventaja:** ocupa solo O(V+E) de memoria.
- **Desventaja:** verificar si existe una arista requiere recorrer la lista.
- **Mejor para:** grafos dispersos (pocas aristas).



## Uso en el Sokoban

| Problema | Algoritmo | Representación |
|---|---|---|
| Resolver nivel automáticamente (RF14) | BFS / DFS | GrafoMatriz |
| Camino más corto del jugador entre dos celdas | BFS | GrafoLista |
| Conectar todas las metas al menor costo | Prim / Kruskal | GrafoMatriz / GrafoLista |
| Distancias entre todos los estados | Floyd-Warshall | GrafoMatriz |