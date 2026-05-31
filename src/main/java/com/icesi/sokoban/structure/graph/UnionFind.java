package com.icesi.sokoban.structure.graph;

/**
 * Estructura de CONJUNTOS DISJUNTOS (Union-Find).
 *
 * Usada por Kruskal para detectar de forma eficiente si una arista
 * forma un ciclo: si los dos extremos ya estan en el mismo conjunto,
 * agregar la arista cerraria un ciclo.
 *
 * Optimizaciones aplicadas:
 *   - UNION POR ALTURAS: el arbol mas bajo cuelga del mas alto.
 *   - COMPRESION DE CAMINOS: en find, cada nodo del camino se reapunta
 *     directamente a la raiz.
 *
 * Con ambas optimizaciones, cada operacion cuesta casi O(1) amortizado.
 */
public class UnionFind {

    private final int[] padre;
    private final int[] altura;

    /** Crea n conjuntos disjuntos {0}, {1}, ..., {n-1}. */
    public UnionFind(int n) {
        if (n <= 0) throw new IllegalArgumentException("n debe ser positivo");
        this.padre = new int[n];
        this.altura = new int[n];
        for (int i = 0; i < n; i++) {
            padre[i] = i;
            altura[i] = 0;
        }
    }

    /**
     * FIND-SET(x): devuelve el representante del conjunto de x.
     * Aplica compresion de caminos.
     */
    public int find(int x) {
        if (padre[x] != x) {
            padre[x] = find(padre[x]);
        }
        return padre[x];
    }

    /**
     * UNION(x, y): fusiona los conjuntos de x e y por alturas.
     * @return true si se unieron, false si ya estaban en el mismo conjunto.
     */
    public boolean union(int x, int y) {
        int raizX = find(x);
        int raizY = find(y);
        if (raizX == raizY) return false;

        if (altura[raizX] < altura[raizY]) {
            padre[raizX] = raizY;
        } else if (altura[raizX] > altura[raizY]) {
            padre[raizY] = raizX;
        } else {
            padre[raizY] = raizX;
            altura[raizX]++;
        }
        return true;
    }

    public boolean mismoConjunto(int x, int y) {
        return find(x) == find(y);
    }
}