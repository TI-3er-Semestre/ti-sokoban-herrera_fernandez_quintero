package com.icesi.sokoban.structure;

import com.icesi.sokoban.structure.graph.Arista;
import com.icesi.sokoban.structure.graph.GrafoLista;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GrafoListaTest {

    private GrafoLista<String> grafo;

    @BeforeEach
    void setUp() {
        grafo = new GrafoLista<>();
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
    void bfs_grafoConexo_visitaTodosLosVertices() {
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        grafo.agregarVertice("C");
        grafo.agregarArista("A", "B", 1);
        grafo.agregarArista("B", "C", 1);
        assertEquals(3, grafo.bfs("A").size());
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
    void prim_grafoConexo_devuelveArbolDeExpansionMinima() {
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        grafo.agregarVertice("C");
        grafo.agregarArista("A", "B", 10);
        grafo.agregarArista("B", "A", 10);
        grafo.agregarArista("A", "C", 5);
        grafo.agregarArista("C", "A", 5);
        grafo.agregarArista("B", "C", 3);
        grafo.agregarArista("C", "B", 3);
        assertEquals(2, grafo.prim().size());
    }

    @Test
    void kruskal_grafoConexo_devuelveArbolDeExpansionMinima() {
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        grafo.agregarVertice("C");
        grafo.agregarArista("A", "B", 10);
        grafo.agregarArista("A", "C", 5);
        grafo.agregarArista("B", "C", 3);
        CustomLinkedList<Arista<String>> agm = grafo.kruskal();
        assertEquals(2, agm.size());
    }

    @Test
    void existeArista_sinConexion_retornaFalse() {
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        assertFalse(grafo.existeArista("A", "B"));
    }
}