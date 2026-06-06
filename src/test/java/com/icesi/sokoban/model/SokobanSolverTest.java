package com.icesi.sokoban.model;

import com.icesi.sokoban.controller.SokobanSolver;
import com.icesi.sokoban.structure.CustomLinkedList;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas del solucionador automatico (RF14).
 *
 * El SokobanSolver hace BFS sobre el espacio de estados del juego.
 * Cada prueba valida un concepto: encuentra solucion, detecta nivel
 * sin solucion, y la secuencia devuelta realmente gana el nivel.
 *
 * Convencion de coordenadas: (fila, columna), (0,0) arriba-izquierda.
 * Caracteres: '#' muro, ' ' piso, '$' caja, '.' meta.
 *
 * Convencion de nombres: metodo_escenario_resultado.
 */
public class SokobanSolverTest {

    /**
     * Construye un nivel simple resoluble:
     *
     *   # # # # #
     *   #       #
     *   #  P $ . #   (jugador en (1,1), caja en (1,3), meta en (1,4))
     *   #       #
     *   # # # # #
     *
     * Empujando la caja una vez a la derecha queda sobre la meta.
     */
    private Game construirNivelResoluble() {
        Board board = new Board(6, 5);
        // Perimetro de muros.
        for (int c = 0; c < 6; c++) {
            board.setCell(0, c, '#');
            board.setCell(4, c, '#');
        }
        for (int r = 0; r < 5; r++) {
            board.setCell(r, 0, '#');
            board.setCell(r, 5, '#');
        }
        // Caja y meta.
        board.setCell(2, 3, '$');
        board.setCell(2, 4, '.');
        board.addGoal(new Position(2, 4));

        Level level = new Level(1, "Nivel resoluble");
        level.setBoard(board);
        level.setPlayerStartPosition(new Position(2, 2));

        Game game = new Game();
        game.loadLevel(level);
        return game;
    }

    /**
     * Construye un nivel sin solucion: la caja esta pegada contra un
     * muro en una esquina, no se puede mover hacia la meta.
     *
     *   # # # # #
     *   # $     #   (caja en (1,1), atrapada en la esquina)
     *   #   P   #
     *   #     . #   (meta en (3,3), inalcanzable para la caja)
     *   # # # # #
     */
    private Game construirNivelSinSolucion() {
        Board board = new Board(5, 5);
        for (int c = 0; c < 5; c++) {
            board.setCell(0, c, '#');
            board.setCell(4, c, '#');
        }
        for (int r = 0; r < 5; r++) {
            board.setCell(r, 0, '#');
            board.setCell(r, 4, '#');
        }
        // Caja atrapada en la esquina superior izquierda.
        board.setCell(1, 1, '$');
        board.setCell(3, 3, '.');
        board.addGoal(new Position(3, 3));

        Level level = new Level(2, "Nivel sin solucion");
        level.setBoard(board);
        level.setPlayerStartPosition(new Position(2, 2));

        Game game = new Game();
        game.loadLevel(level);
        return game;
    }

    @Test
    public void resolver_nivelSimple_encuentraSolucion() {
        Game game = construirNivelResoluble();
        SokobanSolver solver = new SokobanSolver(game);

        CustomLinkedList<Direction> solucion = solver.resolver(algoritmo);

        // Debe encontrar al menos un movimiento.
        assertFalse(solucion.isEmpty());
    }

    @Test
    public void resolver_nivelSinSolucion_retornaListaVacia() {
        Game game = construirNivelSinSolucion();
        SokobanSolver solver = new SokobanSolver(game);

        CustomLinkedList<Direction> solucion = solver.resolver(algoritmo);

        // Una caja atrapada en esquina hace el nivel irresoluble.
        assertTrue(solucion.isEmpty());
    }

    @Test
    public void resolver_secuenciaDevuelta_realmenteGanaElNivel() {
        Game game = construirNivelResoluble();
        SokobanSolver solver = new SokobanSolver(game);

        CustomLinkedList<Direction> solucion = solver.resolver(algoritmo);

        // Aplicar la solucion movimiento por movimiento sobre el juego.
        for (int i = 0; i < solucion.size(); i++) {
            game.move(solucion.get(i));
        }

        // Tras aplicar toda la secuencia, el nivel debe estar completo.
        assertTrue(game.isLevelComplete());
        assertEquals(GameStatus.WON, game.getState());
    }

    @Test
    public void resolver_nivelYaResuelto_devuelveSolucionVacia() {
        // Nivel donde la caja ya esta sobre la meta desde el inicio.
        Board board = new Board(5, 3);
        for (int c = 0; c < 5; c++) {
            board.setCell(0, c, '#');
            board.setCell(2, c, '#');
        }
        board.setCell(1, 0, '#');
        board.setCell(1, 4, '#');
        board.setCell(1, 2, '*'); // caja ya sobre meta
        board.addGoal(new Position(1, 2));

        Level level = new Level(3, "Nivel ya resuelto");
        level.setBoard(board);
        level.setPlayerStartPosition(new Position(1, 1));

        Game game = new Game();
        game.loadLevel(level);

        SokobanSolver solver = new SokobanSolver(game);
        CustomLinkedList<Direction> solucion = solver.resolver(algoritmo);

        // No hace falta ningun movimiento: la solucion es vacia.
        assertTrue(solucion.isEmpty());
    }

    @Test
    public void constructor_juegoSinNivel_lanzaExcepcion() {
        Game game = new Game(); // sin loadLevel
        assertThrows(IllegalArgumentException.class,
                () -> new SokobanSolver(game));
    }

    //Pruebas del selector de algoritmo: BFS vs DFS
    @Test
    public void resolver_conDFS_encuentraSolucion() {
        Game game = construirNivelResoluble();
        SokobanSolver solver = new SokobanSolver(game);
        CustomLinkedList<Direction> solucion =
                solver.resolver(SokobanSolver.Algoritmo.DFS);
        assertFalse(solucion.isEmpty());
    }

    @Test
    public void resolver_conDFS_secuenciaDevuelta_realmenteGanaElNivel() {
        Game game = construirNivelResoluble();
        SokobanSolver solver = new SokobanSolver(game);
        CustomLinkedList<Direction> solucion =
                solver.resolver(SokobanSolver.Algoritmo.DFS);
        for (int i = 0; i < solucion.size(); i++) {
            game.move(solucion.get(i));
        }
        assertTrue(game.isLevelComplete());
        assertEquals(GameStatus.WON, game.getState());
    }

    @Test
    public void resolver_conDFS_nivelSinSolucion_retornaListaVacia() {
        Game game = construirNivelSinSolucion();
        SokobanSolver solver = new SokobanSolver(game);
        CustomLinkedList<Direction> solucion =
                solver.resolver(SokobanSolver.Algoritmo.DFS);
        assertTrue(solucion.isEmpty());
    }

    @Test
    public void resolver_conBFS_devuelveCaminoMinimo() {
        // Basta un empuje a la derecha (1 movimiento). BFS da el camino minimo.
        Game game = construirNivelResoluble();
        SokobanSolver solver = new SokobanSolver(game);
        CustomLinkedList<Direction> solucion =
                solver.resolver(SokobanSolver.Algoritmo.BFS);
        assertEquals(1, solucion.size());
        assertEquals(Direction.RIGHT, solucion.get(0));
    }
}
