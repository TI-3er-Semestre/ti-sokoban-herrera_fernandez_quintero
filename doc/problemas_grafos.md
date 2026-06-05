# Problemas con Grafos — Sokoban

## Problema 1 — Resolver el nivel automáticamente (SokobanSolver)

### ¿Qué problema resuelve?
Dado un nivel del Sokoban, encontrar automáticamente la secuencia de
movimientos que lleva todas las cajas a sus metas.

### ¿Cómo se modela como grafo?
- Cada **estado del juego** (posición del jugador + posiciones de todas
  las cajas) es un **vértice**.
- Cada **movimiento válido** desde un estado genera un nuevo estado = una **arista**.
- El objetivo es encontrar un camino desde el estado inicial hasta un
  estado donde todas las cajas estén en meta.

### ¿Qué algoritmo usa?
**BFS** — porque garantiza encontrar la solución con el menor número de
movimientos posible (camino más corto en número de aristas).

### ¿Por qué BFS y no DFS?
- BFS visita los estados por niveles, garantizando la solución óptima.
- DFS podría encontrar una solución más larga o quedar atrapado en
  ciclos si no se controla con la TranspositionTable.

### ¿Dónde está implementado?
`controller/SokobanSolver.java` — usa `CustomQueue` (BFS) y
`TranspositionTable` para evitar estados repetidos.



## Problema 2 — Árbol de expansión mínima conectando metas (MstSolver)

### ¿Qué problema resuelve?
Dado un nivel del Sokoban con múltiples metas, encontrar la forma de
conectarlas todas gastando la menor distancia posible.

### ¿Cómo se modela como grafo?
- Cada **meta** del tablero es un **vértice**.
- La **distancia Manhattan** entre dos metas es el **peso de la arista**.
- Se construye un grafo completo (todas las metas conectadas entre sí).

### ¿Qué algoritmo usa?
**Prim y Kruskal** — ambos encuentran el Árbol de Expansión Mínima (MST)
que conecta todas las metas al menor costo total.

### ¿En qué se diferencian Prim y Kruskal?
| | Prim | Kruskal |
|---|---|---|
| Estrategia | Crece desde un vértice | Ordena aristas globalmente |
| Estructura | Arreglo de pesos mínimos | Union-Find |
| Resultado | Mismo MST | Mismo MST |

### ¿Dónde está implementado?
`controller/MstSolver.java` — implementado en ambas representaciones:
- `primConMatriz()` / `primConLista()`
- `kruskalConMatriz()` / `kruskalConLista()`

### Ejemplo
Para un nivel con 3 metas en (0,0), (0,3) y (3,0):