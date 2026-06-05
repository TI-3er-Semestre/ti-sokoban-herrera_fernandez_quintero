package com.icesi.sokoban.controller;

import com.icesi.sokoban.model.Level;
import com.icesi.sokoban.model.Position;
import com.icesi.sokoban.structure.CustomLinkedList;
import com.icesi.sokoban.structure.graph.Arista;
import com.icesi.sokoban.structure.graph.GrafoLista;
import com.icesi.sokoban.structure.graph.GrafoMatriz;

/**
 * Problema 2 — Árbol de expansión mínima conectando metas.
 *
 * Cada meta del tablero es un vértice. La distancia Manhattan
 * entre dos metas es el peso de la arista. Aplica Prim y Kruskal
 * en GrafoMatriz y GrafoLista.
 */
public class MstSolver {

    private final Level level;

    public MstSolver(Level level) {
        if (level == null) throw new IllegalArgumentException("El nivel no puede ser null");
        this.level = level;
    }

    private int distancia(Position a, Position b) {
        return Math.abs(a.getRow() - b.getRow()) + Math.abs(a.getColumn() - b.getColumn());
    }

    public GrafoMatriz<String> construirGrafoMatriz() {
        GrafoMatriz<String> grafo = new GrafoMatriz<>();
        CustomLinkedList<Position> metas = level.getBoard().getGoals();
        for (int i = 0; i < metas.size(); i++) {
            grafo.agregarVertice("M" + i);
        }
        for (int i = 0; i < metas.size(); i++) {
            for (int j = 0; j < metas.size(); j++) {
                if (i != j) {
                    grafo.agregarArista("M" + i, "M" + j, distancia(metas.get(i), metas.get(j)));
                }
            }
        }
        return grafo;
    }

    public GrafoLista<String> construirGrafoLista() {
        GrafoLista<String> grafo = new GrafoLista<>();
        CustomLinkedList<Position> metas = level.getBoard().getGoals();
        for (int i = 0; i < metas.size(); i++) {
            grafo.agregarVertice("M" + i);
        }
        for (int i = 0; i < metas.size(); i++) {
            for (int j = 0; j < metas.size(); j++) {
                if (i != j) {
                    grafo.agregarArista("M" + i, "M" + j, distancia(metas.get(i), metas.get(j)));
                }
            }
        }
        return grafo;
    }

    public CustomLinkedList<int[]> primConMatriz() {
        return construirGrafoMatriz().prim();
    }

    public CustomLinkedList<int[]> primConLista() {
        return construirGrafoLista().prim();
    }

    public CustomLinkedList<Arista<String>> kruskalConMatriz() {
        return construirGrafoMatriz().kruskal();
    }

    public CustomLinkedList<Arista<String>> kruskalConLista() {
        return construirGrafoLista().kruskal();
    }
}
