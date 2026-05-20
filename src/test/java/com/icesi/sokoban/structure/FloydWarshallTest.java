package com.icesi.sokoban.structure;

import com.icesi.sokoban.structure.graph.GrafoMatriz;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas del algoritmo de Floyd-Warshall sobre GrafoMatriz.
 *
 * Floyd-Warshall calcula el camino minimo entre TODOS los pares de
 * vertices. Cada prueba valida un concepto: distancias correctas,
 * reconstruccion de camino, diagonal en cero y pesos negativos.
 *
 * Convencion de nombres: metodo_escenario_resultado.
 */
public class FloydWarshallTest {

    /**
     * Construye un grafo dirigido y ponderado de prueba con 4 vertices:
     *
     *   0 --(3)--> 1
     *   0 --(8)--> 3
     *   1 --(1)--> 2
     *   2 --(2)--> 3
     *
     * El camino directo 0->3 cuesta 8, pero 0->1->2->3 cuesta 3+1+2 = 6.
     * Floyd-Warshall debe descubrir esa mejora.
     */
    private GrafoMatriz<Integer> construirGrafoBase() {
        GrafoMatriz<Integer> g = new GrafoMatriz<>();
        g.agregarVertice(0);
        g.agregarVertice(1);
        g.agregarVertice(2);
        g.agregarVertice(3);
        g.agregarArista(0, 1, 3);
        g.agregarArista(0, 3, 8);
        g.agregarArista(1, 2, 1);
        g.agregarArista(2, 3, 2);
        return g;
    }

    @Test
    public void floydWarshall_calculaTodosLosPares_distanciasCorrectas() {
        GrafoMatriz<Integer> g = construirGrafoBase();
        int[][] dist = g.floydWarshall();

        // Distancia directa conocida.
        assertEquals(3, dist[0][1]);
        // Camino compuesto 0->1->2 = 3 + 1 = 4.
        assertEquals(4, dist[0][2]);
        // El camino 0->1->2->3 (6) debe ganarle al directo 0->3 (8).
        assertEquals(6, dist[0][3]);
    }

    @Test
    public void floydWarshall_diagonal_esCero() {
        GrafoMatriz<Integer> g = construirGrafoBase();
        int[][] dist = g.floydWarshall();

        // La distancia de un vertice a si mismo siempre es 0.
        for (int i = 0; i < 4; i++) {
            assertEquals(0, dist[i][i]);
        }
    }

    @Test
    public void floydWarshall_reconstruirCamino_devuelveRutaMasCorta() {
        GrafoMatriz<Integer> g = construirGrafoBase();
        g.floydWarshall();

        // El camino minimo de 0 a 3 debe ser 0 -> 1 -> 2 -> 3.
        CustomLinkedList<Integer> camino = g.reconstruirCamino(0, 3);

        assertEquals(4, camino.size());
        assertEquals(0, camino.get(0));
        assertEquals(1, camino.get(1));
        assertEquals(2, camino.get(2));
        assertEquals(3, camino.get(3));
    }

    @Test
    public void floydWarshall_sinCamino_reconstruccionVacia() {
        // Grafo con un vertice aislado: 0->1, pero 2 sin conexiones.
        GrafoMatriz<Integer> g = new GrafoMatriz<>();
        g.agregarVertice(0);
        g.agregarVertice(1);
        g.agregarVertice(2);
        g.agregarArista(0, 1, 5);
        g.floydWarshall();

        // No hay forma de llegar de 0 a 2: el camino debe venir vacio.
        CustomLinkedList<Integer> camino = g.reconstruirCamino(0, 2);
        assertTrue(camino.isEmpty());
    }

    @Test
    public void floydWarshall_pesosNegativosSinCiclo_funcionaCorrecto() {
        // Floyd-Warshall admite pesos negativos mientras no haya
        // ciclos de peso negativo.
        GrafoMatriz<Integer> g = new GrafoMatriz<>();
        g.agregarVertice(0);
        g.agregarVertice(1);
        g.agregarVertice(2);
        g.agregarArista(0, 1, 4);
        g.agregarArista(1, 2, -2);
        g.agregarArista(0, 2, 5);

        int[][] dist = g.floydWarshall();

        // 0->1->2 = 4 + (-2) = 2, mejor que el directo 0->2 = 5.
        assertEquals(2, dist[0][2]);
    }

    @Test
    public void reconstruirCamino_sinEjecutarFloyd_lanzaExcepcion() {
        GrafoMatriz<Integer> g = construirGrafoBase();
        // Si no se ejecuto floydWarshall(), no hay matriz de predecesores.
        assertThrows(IllegalStateException.class,
                () -> g.reconstruirCamino(0, 3));
    }
}
