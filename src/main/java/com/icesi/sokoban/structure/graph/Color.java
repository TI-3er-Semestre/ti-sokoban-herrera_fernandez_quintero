package com.icesi.sokoban.structure.graph;

/**
 * Estado de un vertice durante un recorrido de grafo (BFS o DFS).
 *
 * Sistema de colores visto en clase:
 *   BLANCO - el vertice no ha sido descubierto todavia.
 *   GRIS   - el vertice fue descubierto pero aun no se exploraron
 *            todos sus vecinos (esta en la frontera).
 *   NEGRO  - el vertice fue completamente procesado.
 */
public enum Color {
    BLANCO,
    GRIS,
    NEGRO
}
