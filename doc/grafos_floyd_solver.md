# Grafos en Sokoban — Floyd-Warshall y Solucionador Automatico

Cubro el algoritmo de Floyd-Warshall, el solucionador automatico de niveles (RF14) y el analisis de complejidad
de ambos.

## 1. El juego Sokoban como un grafo

El problema de resolver un nivel de Sokoban se modela como un **grafo de
estados**:

- Cada vertice es un estado del juego: la posicion del jugador junto
  con la posicion de todas las cajas. Los muros y las metas no se
  guardan en el estado porque son fijos durante toda la partida.
- Cada arista es un movimiento valido (arriba, abajo, izquierda o
  derecha) que transforma un estado en otro.
- Resolver el nivel equivale a encontrar un camino desde el estado
  inicial hasta cualquier estado donde todas las cajas esten sobre metas.

El grafo de estados no se construye por adelantado, seria enorme. Se
genera sobre la marcha, calculando los vecinos de cada estado solo
cuando hace falta.


## 2. Floyd-Warshall

### 2.1 Que hace

Floyd-Warshall calcula el camino minimo entre todos los pares de
vertices de un grafo ponderado, en una sola ejecucion. A diferencia de
BFS o Dijkstra, que parten de un unico origen, Floyd-Warshall entrega de
una vez la distancia minima entre cualquier par de vertices.

### 2.2 Idea central

Es un algoritmo de programacion dinamica. La idea es probar cada
vertice **k** como posible intermediario entre cada par **(i, j)**:

- Para ir de **i** a **j**, ¿conviene pasar por **k**? Si el camino
- **i -> k -> j** es mas corto que el mejor camino conocido de **i** a **j**,
- se actualiza la distancia.

La formula que se aplica en cada paso es:

$dist[i][j] = min( dist[i][j], dist[i][k] + dist[k][j] )$

### 2.3 Estructura del algoritmo

1. La matriz dist se inicializa como copia de la matriz de adyacencia:
   peso directo donde hay arista, infinito donde no la hay, y 0 en la
   diagonal.
2. Un triple bucle anidado recorre primero el intermediario **k**, luego
   el origen **i** y luego el destino **j**, aplicando la formula.
3. En paralelo se mantiene una matriz **siguiente** de predecesores, que
   registra por donde empieza el camino minimo de cada par. Con ella se
   reconstruye la ruta exacta despues.

### 2.4 Restricciones

- Funciona con pesos positivos o negativos.
- No funciona si el grafo tiene ciclos de peso negativo, en ese caso
  las distancias dejarian de tener sentido 

### 2.5 Reconstruccion del camino

El algoritmo base solo calcula distancias. Para obtener la secuencia
exacta de vertices se usa la matriz **siguiente**, se hace partiendo del origen,
se salta de vertice en vertice siguiendo **siguiente[i][j]** hasta llegar
al destino.


## 3. Solucionador automatico (SokobanSolver) — RF14

### 3.1 Que hace

Encuentra una secuencia de movimientos que resuelve el nivel
automaticamente, llevando el juego de su estado inicial a un estado
ganador.

### 3.2 Por que BFS y no DFS

El solucionador usa busqueda en amplitud (BFS) sobre el grafo de
estados. La razon es que BFS explora el grafo por niveles, asi que el
primer estado ganador que encuentra esta siempre a la minima
distancia posible del inicio. Esto garantiza que la solucion devuelta
use el menor numero de movimientos.

DFS no daria esa garantia: podria encontrar una solucion mucho mas larga
antes que la optima.

### 3.3 Estructuras reutilizadas

- **CustomQueue** (cola FIFO propia): es la cola de la frontera de BFS.
  Guarda los estados pendientes por explorar.
- **TranspositionTable** (tabla hash propia): registra los estados ya
  visitados. Esto es indispensable: durante la busqueda, el mismo estado
  puede alcanzarse por caminos distintos. Sin control de repetidos, BFS
  entraria en ciclos infinitos.

### 3.4 Por que la tabla hash es critica

Preguntar "¿ya visite este estado?" se hace en **O(1) promedio** gracias
a la tabla hash. Si esa verificacion se hiciera sobre una lista, costaria
O(n), y el costo total de la busqueda pasaria de O(S) a O(S^2),
volviendose impracticable en niveles grandes.

### 3.5 Reconstruccion de la solucion

Cada vez que se descubre un estado nuevo, se guarda un **rastro**: de que
estado se vino y con que movimiento. Al encontrar el estado ganador, se
recorren los rastros hacia atras para armar la secuencia de movimientos,
y se invierte para obtener el orden correcto.

## 4. Analisis de complejidad

### 4.1 Floyd-Warshall

| Aspecto   | Complejidad | Justificacion |
|-----------|-------------|---------------|
| Temporal  | Theta(V^3)  | Triple bucle anidado sobre los V vertices: para cada intermediario k, cada par (i, j). |
| Espacial  | O(V^2)      | Matriz de distancias mas matriz de predecesores, ambas de tamano V x V. |

La complejidad es la misma en mejor, peor y caso promedio (de ahi la
notacion Theta): el triple bucle se ejecuta completo siempre, sin
importar la forma del grafo.

### 4.2 Reconstruccion de camino (Floyd-Warshall)

| Aspecto   | Complejidad | Justificacion |
|-----------|-------------|---------------|
| Temporal  | O(V)        | El camino entre dos vertices tiene a lo sumo V vertices. |
| Espacial  | O(V)        | La lista del camino guarda a lo sumo V vertices. |

### 4.3 SokobanSolver (BFS sobre estados)

Sea **S** el numero de estados alcanzables desde el inicial.

| Aspecto   | Complejidad | Justificacion |
|-----------|-------------|---------------|
| Temporal  | O(S)        | BFS visita cada estado una vez. Desde cada estado se prueban 4 movimientos (constante). Cada verificacion de repetido es O(1) amortizado por la tabla hash. |
| Espacial  | O(S)        | La cola y la tabla hash pueden llegar a guardar todos los estados alcanzables. |

Sin la tabla hash, la deteccion de estados repetidos costaria O(S) por
consulta y el total seria O(S^2).

---

## 5. Tabla resumen de los algoritmos de grafos del proyecto

| Algoritmo        | Para que sirve                          | Estructura de apoyo   | Temporal           | Espacial |
|------------------|------------------------------------------|------------------------|--------------------|----------|
| BFS              | Camino minimo en numero de aristas       | Cola (FIFO)            | O(V + E)           | O(V)     |
| DFS              | Recorrido en profundidad, deteccion de conexidad | Recursion (pila) | O(V + E)           | O(V)     |
| Dijkstra         | Camino minimo con pesos no negativos     | Cola de prioridad      | O((V+E) log V)     | O(V)     |
| Floyd-Warshall   | Camino minimo entre todos los pares      | Matrices               | Theta(V^3)         | O(V^2)   |
| SokobanSolver    | Resolver un nivel automaticamente (RF14) | Cola + Tabla hash      | O(S)               | O(S)     |

Donde V = vertices, E = aristas, S = estados alcanzables del juego.
