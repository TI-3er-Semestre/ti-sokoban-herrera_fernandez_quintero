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
        grafo.agregarVertice("C"); // C no está conectado
        grafo.agregarArista("A", "B", 1);
        assertEquals(3, grafo.dfs("A").size());
    }

    @Test
    void existeArista_sinConexion_retornaFalse() {
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        assertFalse(grafo.existeArista("A", "B"));
    }
}
