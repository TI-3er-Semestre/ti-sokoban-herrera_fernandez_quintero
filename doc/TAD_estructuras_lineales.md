# TAD - Estructuras Lineales

## 1. TAD Pila (Stack)

Dominio: elementos genéricos de tipo T.

Operaciones:
- push(T element) -> void
- pop() -> T
- peek() -> T
- isEmpty() -> boolean
- size() -> int
- clear() -> void

Precondiciones:
- pop() y peek() requieren que la pila no esté vacía.

Postcondiciones:
- push(x): el tamaño aumenta en 1, el elemento queda en la cima.
- pop(): el tamaño disminuye en 1, retorna el elemento que estaba en la cima.

Invariantes:
- size >= 0.
- top == null si y solo si size == 0.

Excepciones:
- IllegalStateException si se llama pop() o peek() sobre pila vacía.

Representación interna: lista enlazada simple con un puntero al tope.

Uso en Sokoban: guardar snapshots del estado del juego para implementar la funcionalidad de deshacer movimientos (undo).


## 2. TAD Cola (Queue)

Dominio: elementos genéricos de tipo T.

Operaciones:
- enqueue(T element) -> void
- dequeue() -> T
- peek() -> T
- isEmpty() -> boolean
- size() -> int
- clear() -> void

Precondiciones:
- dequeue() y peek() requieren que la cola no esté vacía.

Postcondiciones:
- enqueue(x): el tamaño aumenta en 1, el elemento queda al final.
- dequeue(): el tamaño disminuye en 1, retorna el elemento del frente.

Invariantes:
- size >= 0.
- size == 0 si y solo si front == null y rear == null.
- Disciplina FIFO: el primero que entra es el primero que sale.

Excepciones:
- IllegalStateException si se llama dequeue() o peek() sobre cola vacía.

Representación interna: lista enlazada simple con dos punteros (front y rear).

Uso en Sokoban: buffer de entrada del jugador. Almacena los comandos de teclado en orden de llegada para procesarlos sin perder ninguno cuando el usuario presiona rápido.


## 3. TAD Cola de Prioridad (Priority Queue)

Dominio: elementos genéricos de tipo T que implementan Comparable<T>.

Operaciones:
- insert(T element) -> void
- extractMin() -> T
- peek() -> T
- isEmpty() -> boolean
- size() -> int

Precondiciones:
- extractMin() y peek() requieren que la cola no esté vacía.
- Los elementos deben implementar Comparable<T>.

Postcondiciones:
- insert(x): el tamaño aumenta en 1, el elemento queda ubicado según su prioridad.
- extractMin(): el tamaño disminuye en 1, retorna el elemento de menor valor.

Invariantes:
- Propiedad de min-heap: para todo nodo i con padre p, heap[p] <= heap[i].
- El mínimo siempre está en heap[0].
- size >= 0.

Excepciones:
- IllegalStateException si se llama extractMin() o peek() sobre cola vacía.

Representación interna: heap binario sobre arreglo, con duplicación de capacidad cuando se llena.

Uso en Sokoban: ranking de mejores puntajes por nivel. Mantiene los top-K resultados con menor cantidad de movimientos, empujes y tiempo.