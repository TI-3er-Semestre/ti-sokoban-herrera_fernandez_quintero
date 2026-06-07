package com.icesi.sokoban.model;

import com.icesi.sokoban.controller.MstSolver;
import com.icesi.sokoban.structure.CustomLinkedList;
import com.icesi.sokoban.structure.graph.Arista;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MstSolverTest {

    private Level construirNivel(Position... metas) {
        Board board = new Board(10, 10);
        for (Position meta : metas) {
            board.addGoal(meta);
        }
        Level level = new Level(1, "Test");
        level.setBoard(board);
        level.setPlayerStartPosition(new Position(0, 0));
        return level;
    }

    @Test
    void prim_dosMetas_devuelveUnaArista() {
        Level level = construirNivel(new Position(0, 0), new Position(0, 3));
        MstSolver solver = new MstSolver(level);
        assertEquals(1, solver.primConMatriz().size());
    }

    @Test
    void prim_tresMetas_devuelveDosAristas() {
        Level level = construirNivel(
                new Position(0, 0),
                new Position(0, 3),
                new Position(3, 0)
        );
        MstSolver solver = new MstSolver(level);
        assertEquals(2, solver.primConMatriz().size());
    }

    @Test
    void kruskal_dosMetas_devuelveUnaArista() {
        Level level = construirNivel(new Position(0, 0), new Position(0, 3));
        MstSolver solver = new MstSolver(level);
        assertEquals(1, solver.kruskalConMatriz().size());
    }

    @Test
    void primYKruskal_mismopesoTotal() {
        Level level = construirNivel(
                new Position(0, 0),
                new Position(0, 3),
                new Position(3, 0)
        );
        MstSolver solver = new MstSolver(level);

        CustomLinkedList<int[]> mstPrim = solver.primConMatriz();
        CustomLinkedList<Arista<String>> mstKruskal = solver.kruskalConMatriz();

        int pesoPrim = 0;
        for (int i = 0; i < mstPrim.size(); i++) pesoPrim += mstPrim.get(i)[2];

        int pesoKruskal = 0;
        for (int i = 0; i < mstKruskal.size(); i++) pesoKruskal += mstKruskal.get(i).getPeso();

        assertEquals(pesoPrim, pesoKruskal);
    }

    @Test
    void matrizYLista_mismoPesoTotal() {
        Level level = construirNivel(
                new Position(0, 0),
                new Position(0, 3),
                new Position(3, 0)
        );
        MstSolver solver = new MstSolver(level);

        CustomLinkedList<int[]> mstMatriz = solver.primConMatriz();
        CustomLinkedList<int[]> mstLista = solver.primConLista();

        int pesoMatriz = 0;
        for (int i = 0; i < mstMatriz.size(); i++) pesoMatriz += mstMatriz.get(i)[2];

        int pesoLista = 0;
        for (int i = 0; i < mstLista.size(); i++) pesoLista += mstLista.get(i)[2];

        assertEquals(pesoMatriz, pesoLista);
    }

    @Test
    void constructor_nivelNull_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> new MstSolver(null));
    }
}
