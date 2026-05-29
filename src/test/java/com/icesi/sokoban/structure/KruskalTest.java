package com.icesi.sokoban.structure;

import com.icesi.sokoban.structure.graph.GrafoMatriz;
import com.icesi.sokoban.structure.graph.Arista;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class KruskalTest {

    @Test
    void kruskal_ejemploClase_costo6() {
        // Grafo del ejemplo de clase con 5 vertices
        GrafoMatriz<String> g = new GrafoMatriz<>();
        for (String v : new String[]{"a","b","c","d","e"}) g.agregarVertice(v);
        g.agregarArista("a","b",1); g.agregarArista("b","a",1);
        g.agregarArista("c","d",1); g.agregarArista("d","c",1);
        g.agregarArista("a","e",2); g.agregarArista("e","a",2);
        g.agregarArista("e","d",2); g.agregarArista("d","e",2);
        g.agregarArista("b","e",3); g.agregarArista("e","b",3);
        g.agregarArista("b","d",3); g.agregarArista("d","b",3);
        g.agregarArista("e","c",3); g.agregarArista("c","e",3);
        g.agregarArista("a","c",4); g.agregarArista("c","a",4);

        assertEquals(6, g.costoAGM());
    }

    @Test
    void kruskal_devuelveNMenosUnaAristas() {
        GrafoMatriz<Integer> g = new GrafoMatriz<>();
        g.agregarVertice(1); g.agregarVertice(2); g.agregarVertice(3);
        g.agregarArista(1,2,1); g.agregarArista(2,1,1);
        g.agregarArista(2,3,1); g.agregarArista(3,2,1);
        g.agregarArista(1,3,5); g.agregarArista(3,1,5);

        CustomLinkedList<Arista<Integer>> agm = g.kruskal();
        assertEquals(2, agm.size()); // 3 vertices → 2 aristas
    }

    @Test
    void kruskal_grafoVacio_devuelveListaVacia() {
        GrafoMatriz<Integer> g = new GrafoMatriz<>();
        assertTrue(g.kruskal().isEmpty());
    }
}