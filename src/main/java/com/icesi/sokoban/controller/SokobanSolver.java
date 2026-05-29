package com.icesi.sokoban.controller;

import com.icesi.sokoban.model.Board;
import com.icesi.sokoban.model.Direction;
import com.icesi.sokoban.model.Game;
import com.icesi.sokoban.structure.CustomLinkedList;
import com.icesi.sokoban.structure.CustomQueue;
import com.icesi.sokoban.structure.TranspositionTable;

/**
 * SOLUCIONADOR AUTOMATICO DEL JUEGO  —  RF14
 *
 * Resuelve un nivel de Sokoban automaticamente: encuentra una secuencia
 * de movimientos que lleva de la configuracion inicial a una donde todas
 * las cajas estan sobre metas.
 *
 * ─────────────────────────────────────────────────────────────────────
 *  MODELADO COMO GRAFO
 * ─────────────────────────────────────────────────────────────────────
 *  El problema se modela como un GRAFO DE ESTADOS:
 *
 *    - Cada VERTICE es un estado del juego (posicion del jugador y de
 *      las cajas). Se representa con la clase EstadoSokoban.
 *
 *    - Cada ARISTA es un movimiento valido (UP, DOWN, LEFT, RIGHT) que
 *      transforma un estado en otro.
 *
 *    - Resolver el nivel = encontrar un CAMINO desde el estado inicial
 *      hasta cualquier estado ganador.
 *
 *  El grafo de estados NO se construye por adelantado (seria gigantesco).
 *  Se va generando "al vuelo": desde cada estado se calculan sus vecinos
 *  probando los cuatro movimientos.
 *
 * ─────────────────────────────────────────────────────────────────────
 *  POR QUE BFS
 * ─────────────────────────────────────────────────────────────────────
 *  Se usa BUSQUEDA EN AMPLITUD (BFS) porque garantiza encontrar la
 *  solucion con el MENOR numero de movimientos: BFS explora por niveles,
 *  asi que el primer estado ganador que encuentra esta a la minima
 *  distancia posible del inicio.
 *
 *  BFS necesita una COLA (FIFO): se usa CustomQueue, la estructura
 *  propia del proyecto.
 *
 * ─────────────────────────────────────────────────────────────────────
 *  POR QUE LA TABLA HASH
 * ─────────────────────────────────────────────────────────────────────
 *  Durante la busqueda, el mismo estado puede alcanzarse por caminos
 *  distintos. Sin control de repetidos, BFS entraria en ciclos y
 *  exploraria infinitamente.
 *
 *  La TranspositionTable (tabla hash con encadenamiento) registra cada
 *  estado ya visitado. Preguntar "¿ya vi este estado?" cuesta O(1)
 *  promedio. Sin ella, la verificacion seria O(n) y la busqueda total
 *  pasaria de O(n) a O(n^2), volviendose impracticable.
 *
 * ─────────────────────────────────────────────────────────────────────
 *  COMPLEJIDAD
 * ─────────────────────────────────────────────────────────────────────
 *  Sea S el numero de estados alcanzables. BFS visita cada estado una
 *  vez y desde cada uno prueba 4 movimientos:
 *
 *    Temporal:  O(S)   (4 es constante; cada verificacion de repetido
 *                       es O(1) amortizado gracias a la tabla hash)
 *    Espacial:  O(S)   (la cola y la tabla hash pueden llegar a guardar
 *                       todos los estados alcanzables)
 */
public class SokobanSolver {

    /** Los cuatro movimientos posibles, en orden fijo. */
    private static final Direction[] MOVIMIENTOS = {
            Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT
    };

    private final Board board;
    private final EstadoSokoban estadoInicial;

    /** Limite de estados a explorar, para no colgarse en niveles enormes. */
    private static final int LIMITE_ESTADOS = 200_000;

    /**
     * Crea un solucionador para el nivel actualmente cargado en el juego.
     *
     * @param game juego con un nivel ya cargado
     */
    public SokobanSolver(Game game) {
        if (game == null || game.getBoard() == null) {
            throw new IllegalArgumentException(
                    "El juego debe tener un nivel cargado");
        }
        this.board = game.getBoard();
        this.estadoInicial = EstadoSokoban.desdeJuego(game);
    }

    /**
     * Resuelve el nivel con BFS sobre el espacio de estados.
     *
     * @return secuencia de movimientos que resuelve el nivel;
     *         lista VACIA si el nivel no tiene solucion (o si se
     *         alcanza el limite de estados sin encontrarla)
     */
    public CustomLinkedList<Direction> resolver() {
        // Caso borde: el nivel ya esta resuelto.
        if (estadoInicial.esGanador(board)) {
            return new CustomLinkedList<>();
        }

        // Cola de BFS: guarda los estados pendientes por explorar.
        CustomQueue<EstadoSokoban> frontera = new CustomQueue<>();

        // Tabla hash de estados ya visitados (clave -> registro de camino).
        // Guardamos en la tabla, por cada estado, como se llego a el:
        // el estado anterior y el movimiento que se hizo. Eso permite
        // reconstruir la secuencia al final.
        TranspositionTable visitados = new TranspositionTable();

        frontera.enqueue(estadoInicial);
        visitados.put(estadoInicial.toString(), new Rastro(null, null));

        int estadosExplorados = 0;

        while (!frontera.isEmpty()) {
            EstadoSokoban actual = frontera.dequeue();
            estadosExplorados++;

            if (estadosExplorados > LIMITE_ESTADOS) {
                // El espacio de busqueda es demasiado grande; abortar.
                return new CustomLinkedList<>();
            }

            // Probar los cuatro movimientos desde el estado actual.
            for (Direction dir : MOVIMIENTOS) {
                EstadoSokoban vecino = simularMovimiento(actual, dir);

                // Movimiento invalido: no genera estado nuevo.
                if (vecino == null) {
                    continue;
                }

                String clave = vecino.toString();

                // Estado ya visitado: lo ignoramos para no ciclar.
                if (visitados.containsKey(clave)) {
                    continue;
                }

                // Registrar como se llego a este estado nuevo.
                visitados.put(clave, new Rastro(actual, dir));

                // ¿Es un estado ganador? Reconstruir y devolver el camino.
                if (vecino.esGanador(board)) {
                    return reconstruirCamino(vecino, visitados);
                }

                // Estado nuevo no ganador: encolarlo para explorarlo luego.
                frontera.enqueue(vecino);
            }
        }

        // Se agoto la frontera sin encontrar solucion.
        return new CustomLinkedList<>();
    }

    /**
     * Simula un movimiento del jugador sobre un estado, sin tocar el
     * objeto Game. Aplica las mismas reglas que Game.move():
     *
     *   - el jugador no puede atravesar muros ni salir del tablero;
     *   - si hay una caja al frente, se empuja solo si la celda
     *     siguiente esta libre (no es muro ni otra caja);
     *   - dos cajas seguidas no se pueden empujar.
     *
     * @param estado estado de partida
     * @param dir    direccion del movimiento
     * @return el estado resultante, o null si el movimiento es invalido
     */
    private EstadoSokoban simularMovimiento(EstadoSokoban estado, Direction dir) {
        int dFila = 0;
        int dCol = 0;
        switch (dir) {
            case UP:    dFila = -1; break;
            case DOWN:  dFila = +1; break;
            case LEFT:  dCol  = -1; break;
            case RIGHT: dCol  = +1; break;
            default: return null;
        }

        int nuevaFilaJ = estado.getJugadorFila() + dFila;
        int nuevaColJ  = estado.getJugadorColumna() + dCol;

        // El jugador no puede salir del tablero ni entrar a un muro.
        if (!board.isValidPosition(nuevaFilaJ, nuevaColJ)) {
            return null;
        }
        if (board.isWall(nuevaFilaJ, nuevaColJ)) {
            return null;
        }

        // Copiar las posiciones de las cajas (estado nuevo independiente).
        int n = estado.cantidadCajas();
        int[] cajasFila = new int[n];
        int[] cajasCol = new int[n];
        for (int i = 0; i < n; i++) {
            cajasFila[i] = estado.getCajaFila(i);
            cajasCol[i] = estado.getCajaColumna(i);
        }

        // ¿Hay una caja en la celda a la que entra el jugador?
        int indiceCaja = -1;
        for (int i = 0; i < n; i++) {
            if (cajasFila[i] == nuevaFilaJ && cajasCol[i] == nuevaColJ) {
                indiceCaja = i;
                break;
            }
        }

        if (indiceCaja != -1) {
            // Hay caja: intentar empujarla una celda mas en la misma direccion.
            int nuevaFilaC = nuevaFilaJ + dFila;
            int nuevaColC = nuevaColJ + dCol;

            if (!board.isValidPosition(nuevaFilaC, nuevaColC)) {
                return null;
            }
            if (board.isWall(nuevaFilaC, nuevaColC)) {
                return null;
            }
            // Detras de la caja no puede haber otra caja.
            if (estado.hayCaja(nuevaFilaC, nuevaColC)) {
                return null;
            }

            // Empuje valido: mover la caja.
            cajasFila[indiceCaja] = nuevaFilaC;
            cajasCol[indiceCaja] = nuevaColC;
        }

        // El jugador avanza a la nueva celda.
        return new EstadoSokoban(nuevaFilaJ, nuevaColJ, cajasFila, cajasCol);
    }

    /**
     * Reconstruye la secuencia de movimientos desde el estado inicial
     * hasta el estado ganador, recorriendo los rastros hacia atras.
     *
     * Como los rastros apuntan al pasado (cada estado guarda de donde
     * vino), se arma la lista al reves y luego se invierte.
     *
     * @param estadoFinal estado ganador encontrado
     * @param visitados   tabla con el rastro de cada estado
     * @return secuencia de movimientos en el orden correcto
     */
    private CustomLinkedList<Direction> reconstruirCamino(
            EstadoSokoban estadoFinal, TranspositionTable visitados) {

        // Recolectar los movimientos yendo del final hacia el inicio.
        CustomLinkedList<Direction> alReves = new CustomLinkedList<>();

        EstadoSokoban actual = estadoFinal;
        while (true) {
            Rastro rastro = (Rastro) visitados.get(actual.toString());
            if (rastro == null || rastro.movimiento == null) {
                // Llegamos al estado inicial (no tiene rastro de movimiento).
                break;
            }
            alReves.add(rastro.movimiento);
            actual = rastro.estadoAnterior;
        }

        // Invertir para obtener el orden inicio -> final.
        CustomLinkedList<Direction> camino = new CustomLinkedList<>();
        for (int i = alReves.size() - 1; i >= 0; i--) {
            camino.add(alReves.get(i));
        }
        return camino;
    }

    /**
     * Registro de "de donde vino" un estado durante la busqueda.
     * Guarda el estado anterior y el movimiento que llevo hasta el
     * estado actual. Sirve para reconstruir la solucion al final.
     */
    private static class Rastro {
        private final EstadoSokoban estadoAnterior;
        private final Direction movimiento;

        Rastro(EstadoSokoban estadoAnterior, Direction movimiento) {
            this.estadoAnterior = estadoAnterior;
            this.movimiento = movimiento;
        }
    }
}
