package com.icesi.sokoban.structure;

import com.icesi.sokoban.structure.graph.GrafoMatriz;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GrafoMatrizTest {

    private GrafoMatriz<String> grafo;

    @BeforeEach
    void setUp() {
        grafo = new GrafoMatriz<>();
    }

    @Test
    void agregarVertice_grafoVacio_aumentaCantidad() {
        grafo.agregarVertice("A");
        assertEquals(1, grafo.cantidadVertices());
    }

    @Test
    void agregarArista_verticesExistentes_creaConexion() {
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        grafo.agregarArista("A", "B", 5);
        assertTrue(grafo.existeArista("A", "B"));
    }

    @Test
    void agregarArista_verticeInexistente_lanzaExcepcion() {
        grafo.agregarVertice("A");
        assertThrows(IllegalArgumentException.class, () ->
                grafo.agregarArista("A", "Z", 1)
        );
    }

    @Test
    void dfs_grafoConexo_visitaTodosLosVertices() {
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        grafo.agregarVertice("C");
        grafo.agregarArista("A", "B", 1);
        grafo.agregarArista("B", "C", 1);
        assertEquals(3, grafo.dfs("A").size());
    }

    @Test
    void dfs_grafoNoConexo_produceBosque() {
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        grafo.agregarVertice("C");
        grafo.agregarArista("A", "B", 1);
        assertEquals(3, grafo.dfs("A").size());
    }

    @Test
    void existeArista_sinConexion_retornaFalse() {
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        assertFalse(grafo.existeArista("A", "B"));
    }

    @Test
    void prim_grafoConexo_devuelveArbolDeExpansionMinima() {
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        grafo.agregarVertice("C");
        grafo.agregarVertice("D");
        grafo.agregarArista("A", "B", 10);
        grafo.agregarArista("B", "A", 10);
        grafo.agregarArista("A", "C", 5);
        grafo.agregarArista("C", "A", 5);
        grafo.agregarArista("B", "D", 3);
        grafo.agregarArista("D", "B", 3);
        grafo.agregarArista("C", "D", 8);
        grafo.agregarArista("D", "C", 8);
        CustomLinkedList<int[]> mst = grafo.prim();
        assertEquals(3, mst.size());
    }

    @Test
    void prim_calculaPesoTotalMinimoCorrecto() {
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        grafo.agregarVertice("C");
        grafo.agregarArista("A", "B", 4);
        grafo.agregarArista("B", "A", 4);
        grafo.agregarArista("A", "C", 2);
        grafo.agregarArista("C", "A", 2);
        grafo.agregarArista("B", "C", 1);
        grafo.agregarArista("C", "B", 1);
        CustomLinkedList<int[]> mst = grafo.prim();
        int pesoTotal = 0;
        for (int i = 0; i < mst.size(); i++) {
            pesoTotal += mst.get(i)[2];
        }
        assertEquals(3, pesoTotal);
    }

    @Test
    void prim_grafoConUnVertice_devuelveListaVacia() {
        grafo.agregarVertice("A");
        CustomLinkedList<int[]> mst = grafo.prim();
        assertEquals(0, mst.size());
    }

    @Test
    void prim_grafoConDosVertices_devuelveUnaArista() {
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        grafo.agregarArista("A", "B", 7);
        grafo.agregarArista("B", "A", 7);
        CustomLinkedList<int[]> mst = grafo.prim();
        assertEquals(1, mst.size());
        assertEquals(7, mst.get(0)[2]);
    }
}
