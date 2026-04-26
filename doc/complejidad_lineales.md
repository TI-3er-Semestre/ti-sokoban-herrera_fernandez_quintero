# Análisis de Complejidad - Estructuras Lineales

Tabla resumen

| Operación      | Stack  | Queue  | PriorityQueue |
|----------------|--------|--------|---------------|
| insertar       | O(1)   | O(1)   | O(log n)      |
| eliminar       | O(1)   | O(1)   | O(log n)      |
| peek           | O(1)   | O(1)   | O(1)          |
| buscar         | O(n)   | O(n)   | O(n)          |
| isEmpty / size | O(1)   | O(1)   | O(1)          |
| espacio        | O(n)   | O(n)   | O(n)          |

Donde n es la cantidad de elementos almacenados.


## CustomStack

Implementación: lista enlazada simple con un puntero al tope.

push(x): O(1) porque solo se crea un nodo nuevo y se enlaza al tope, sin recorrer nada.

pop(): O(1) porque se accede al tope directamente, se desenlaza y se decrementa el tamaño.

peek(): O(1) porque es acceso directo al nodo tope.

isEmpty() y size(): O(1) porque se mantiene un contador interno.

Espacio: O(n) porque hay un nodo por cada elemento almacenado.


## CustomQueue

Implementación: lista enlazada simple con dos punteros (front y rear).

enqueue(x): O(1) porque se accede directamente a rear, se enlaza el nuevo nodo y se actualiza el puntero sin recorrer la lista.

dequeue(): O(1) porque se accede directamente a front, se desenlaza y se actualiza el puntero.

peek(): O(1) porque es acceso directo al nodo front.

isEmpty() y size(): O(1) porque hay un contador interno.

Espacio: O(n) porque hay un nodo por elemento.

Mantener dos punteros permite que tanto enqueue como dequeue sean O(1). Si se implementara con un solo puntero al inicio, el enqueue sería O(n) porque habría que recorrer toda la lista hasta el final para agregar.


## CustomPriorityQueue

Implementación: min-heap binario sobre arreglo dinámico.

insert(x): O(log n) porque el elemento se añade al final del arreglo y luego se ejecuta heapifyUp, que en el peor caso recorre la altura del árbol que es log n.

extractMin(): O(log n) porque se retira la raíz, se mueve el último elemento al frente y se ejecuta heapifyDown, que también recorre la altura del árbol.

peek(): O(1) porque es acceso directo a heap[0].

resize(): O(n) en el peor caso, pero amortizado O(1) por inserción gracias a la política de duplicar la capacidad.

Espacio: O(n) porque el arreglo crece dinámicamente.

El heap binario aprovecha que un árbol binario completo se puede mapear a un arreglo usando las fórmulas: padre en (i-1)/2, hijo izquierdo en 2i+1 e hijo derecho en 2i+2. La política de duplicación de capacidad garantiza que las inserciones sean amortizadas O(1) para el resize, a diferencia de sumar una constante que resultaría en O(n^2) en total.


## Aplicación en Sokoban

Stack se usa para el sistema de undo. Cada movimiento válido empuja un snapshot a la pila y cuando se llama undo, pop restaura el estado anterior del jugador y las cajas.

Queue se usa como buffer de entrada del jugador, garantizando que los comandos de teclado se procesen en orden FIFO sin perder ninguno aunque el usuario presione rápido.

PriorityQueue se usa para el ranking de mejores puntajes por nivel, manteniendo los mejores resultados ordenados y accesibles de forma eficiente.