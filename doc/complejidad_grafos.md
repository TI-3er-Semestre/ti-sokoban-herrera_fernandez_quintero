# Análisis de Complejidad — Algoritmos de Grafos

## Representaciones del grafo

El proyecto implementa dos representaciones del grafo:

- **GrafoMatriz:** usa una matriz de adyacencia `int[][]` de tamaño V×V.
- **GrafoLista:** usa una lista de adyacencia donde cada vértice tiene su propia lista de vecinos.

La elección de representación afecta directamente la complejidad de cada algoritmo.



## Tabla de complejidad

| Algoritmo | GrafoMatriz | GrafoLista | Justificación |
|---|---|---|---|
| BFS | O(V²) | O(V+E) | Matriz recorre toda la fila; lista solo vecinos reales |
| DFS | O(V²) | O(V+E) | Igual que BFS — recorre vecinos de cada vértice |
| Floyd-Warshall | O(V³) | O(V³) | Triple bucle anidado, independiente de la representación |
| Prim | O(V²) | O(V²) | Usa arreglos simples; con heap sería O(E log V) |
| Kruskal | O(E log E) | O(E log E) | Dominado por el ordenamiento de aristas |



## Análisis por algoritmo

### BFS (Búsqueda en Anchura)

**GrafoMatriz — O(V²):**
Para cada vértice desencolado, recorre toda su fila en la matriz
buscando vecinos (V operaciones por vértice, V vértices = V²).

**GrafoLista — O(V+E):**
Para cada vértice desencolado, solo recorre sus vecinos reales.
En total se visitan V vértices y E aristas.



### DFS (Búsqueda en Profundidad)

**GrafoMatriz — O(V²):**
`dfsVisitar` recorre toda la fila del vértice actual buscando
vecinos BLANCOS (V operaciones), y se llama V veces en total.

**GrafoLista — O(V+E):**
`dfsVisitar` solo recorre los vecinos reales del vértice actual.
En total se procesan V vértices y E aristas.



### Floyd-Warshall

**Ambas representaciones — O(V³):**
Usa un triple bucle anidado k, i, j que itera V veces cada uno.
La representación no afecta la complejidad porque Floyd-Warshall
trabaja sobre una matriz de distancias construida al inicio.

Complejidad espacial: O(V²) para las matrices `dist` y `siguiente`.



### Prim

**Ambas representaciones — O(V²):**
El bucle principal itera V veces. En cada iteración busca el
vértice de menor peso (V operaciones) y actualiza sus vecinos.

En GrafoMatriz recorre toda la fila (V). En GrafoLista recorre
solo los vecinos reales, pero el bucle externo sigue siendo V²
porque la búsqueda del mínimo es O(V) en cada iteración.

Con una cola de prioridad (min-heap) la complejidad sería O(E log V).



### Kruskal

**Ambas representaciones — O(E log E):**
1. Recolectar aristas: O(E)
2. Ordenar aristas por peso (selection sort): O(E²) — pero para
   grafos dispersos con pocas aristas es aceptable.
3. Union-Find con compresión de caminos: O(E α(V)) ≈ O(E)

El paso dominante es el ordenamiento. Con un algoritmo de
ordenamiento eficiente (MergeSort) sería O(E log E).



## Comparación de representaciones

| Criterio | GrafoMatriz | GrafoLista |
|---|---|---|
| Memoria | O(V²) — siempre | O(V+E) — solo lo que existe |
| Verificar arista | O(V) | O(degree) |
| Recorrer vecinos | O(V) | O(degree) |
| Mejor para | Grafos densos | Grafos dispersos |

En el Sokoban, el grafo de estados es **disperso** (cada estado
tiene máximo 4 vecinos), por lo que GrafoLista es más eficiente
en memoria y en BFS/DFS.



## Conclusión

Para el problema del Sokoban:
- **BFS con GrafoLista** es la opción más eficiente para resolver
  niveles automáticamente: O(V+E) en tiempo y O(V+E) en espacio.
- **Floyd-Warshall** es útil para calcular distancias entre todos
  los estados, pero su costo O(V³) lo hace inviable para espacios
  de estados grandes.
- **Prim y Kruskal** son ideales para el problema del árbol mínimo
  de metas (MstSolver), donde el grafo es pequeño (pocos vértices).