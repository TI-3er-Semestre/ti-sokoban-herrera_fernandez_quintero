package com.icesi.sokoban.controller;

import com.icesi.sokoban.model.Board;
import com.icesi.sokoban.model.Direction;
import com.icesi.sokoban.model.Game;
import com.icesi.sokoban.structure.CustomLinkedList;
import com.icesi.sokoban.structure.CustomQueue;
import com.icesi.sokoban.structure.CustomStack;
import com.icesi.sokoban.structure.TranspositionTable;

/**
 * SOLUCIONADOR AUTOMATICO DEL JUEGO  —  RF14
 *
 * Resuelve un nivel de Sokoban automaticamente: encuentra una secuencia
 * de movimientos que lleva de la configuracion inicial a una donde todas
 * las cajas estan sobre metas.
 *
 * Algoritmos disponibles: BFS (solucion optima) y DFS (solucion rapida).
 * El jugador elige cual usar desde el ChoiceBox en la pantalla de juego.
 *
 * COMPLEJIDAD (sea S = numero de estados alcanzables):
 *   Temporal: O(S)  — cada estado se visita una vez; verificacion O(1) con hash
 *   Espacial: O(S)  — cola/pila y tabla hash pueden guardar todos los estados
 */
public class SokobanSolver {

    /**
     * Algoritmos de busqueda que el jugador puede elegir. Lo unico que
     * cambia entre ellos es la estructura de la frontera (cola para BFS,
     * pila para DFS).
     */
    public enum Algoritmo {
        BFS, DFS
    }

    /**
     * Los cuatro movimientos posibles, en orden fijo.
     */
    private static final Direction[] MOVIMIENTOS = {
            Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT
    };

    private final Board board;
    private final EstadoSokoban estadoInicial;

    /**
     * Limite de estados a explorar, para no colgarse en niveles enormes.
     */
    private static final int LIMITE_ESTADOS = 200_000;

    public SokobanSolver(Game game) {
        if (game == null || game.getBoard() == null) {
            throw new IllegalArgumentException("El juego debe tener un nivel cargado");
        }
        this.board = game.getBoard();
        this.estadoInicial = EstadoSokoban.desdeJuego(game);
    }

    /**
     * Resuelve el nivel con BFS (version rapida sin parametro).
     * Resuelve el nivel con BFS sobre el espacio de estados.
     *
     */
    public CustomLinkedList<Direction> resolver() {
        return resolver(Algoritmo.BFS);
    }

    /**
     * Resuelve el nivel con el algoritmo elegido (BFS o DFS).
     * Lo unico que cambia es la estructura de la frontera.
     *
     * @param algoritmo BFS o DFS
     */
    public CustomLinkedList<Direction> resolver(Algoritmo algoritmo) {
     * Resuelve el nivel recorriendo el espacio de estados con el
     * algoritmo elegido (BFS o DFS). Lo unico que cambia es la estructura
     * que guarda los estados pendientes, FIFO para cola y LIFO para pila.
     *
     * @param algoritmo BFS o DFS
     * @return secuencia de movimientos que resuelve el nivel o lista vacia
     * si el nivel no tiene solucion
     */
    public CustomLinkedList<Direction> resolver(Algoritmo algoritmo) {
        if (algoritmo == Algoritmo.DFS) {
            return resolverConPila();
        } else {
            return resolverConCola();
        }
    }

    /**
     * BFS (busqueda en amplitud): usa una COLA (FIFO). Como saca primero
     * el estado mas antiguo, explora por niveles, asi que el primer estado
     * ganador que encuentra esta a la minima distancia del inicio. Por eso
     * BFS devuelve la solucion con el MENOR numero de movimientos.
     *
     * @return secuencia de movimientos que resuelve el nivel;
     * lista VACIA si el nivel no tiene solucion o si se
     * alcanza el limite de estados sin encontrarla
     */
    private CustomLinkedList<Direction> resolverConCola() {
        // Caso borde: el nivel ya esta resuelto.
        if (estadoInicial.esGanador(board)) {
            return new CustomLinkedList<>();
        }

        if (algoritmo == Algoritmo.DFS) {
            return resolverDFS();
        }
        return resolverBFS();
    }

    // ─────────────────────────────────────────────────────────────────────
    // BFS — busqueda en amplitud, garantiza solucion optima
    // ─────────────────────────────────────────────────────────────────────
    private CustomLinkedList<Direction> resolverBFS() {
        CustomQueue<EstadoSokoban> frontera = new CustomQueue<>();
        // Cola con los estados pendientes por explorar
        CustomQueue<EstadoSokoban> frontera = new CustomQueue<>();

        // Tabla hash de estados ya visitados. Por cada estado guardamos
        // como se llego a el (estado anterior + movimiento), para poder
        // reconstruir la secuencia al final.
        TranspositionTable visitados = new TranspositionTable();

        frontera.enqueue(estadoInicial);
        visitados.put(estadoInicial.toString(), new Rastro(null, null));

        int explorados = 0;

        while (!frontera.isEmpty()) {
            EstadoSokoban actual = frontera.dequeue();
            if (++explorados > LIMITE_ESTADOS) return new CustomLinkedList<>();

            if (estadosExplorados > LIMITE_ESTADOS) {
                //El espacio de busqueda es demaciado grande, abortar mision
                return new CustomLinkedList<>();
            }

            //Probar los cuatro movimientos desde el estado actual
            for (Direction dir : MOVIMIENTOS) {
                EstadoSokoban vecino = simularMovimiento(actual, dir);

                //Movimiento invalido: No genera nuevo estado
                if (vecino == null) {
                    continue;
                }
                String clave = vecino.toString();

                //Estado ya visitado: Lo ignoramos para no ciclar
                if (visitados.containsKey(clave)) {
                    continue;
                }

                //Registrar como se llego a este estado nuevo
                visitados.put(clave, new Rastro(actual, dir));

                // Es un estado ganador? Reconstruir y devolver el camino
                if (vecino.esGanador(board)) {
                    return reconstruirCamino(vecino, visitados);
                }

                //Estado nuevo no ganador: Encolarlo para explorarlo luego.
                frontera.enqueue(vecino);
            }
        }
        // Se agoto la cola sin encontrar solucion
        return new CustomLinkedList<>();
    }

    /**
     * DFS (busqueda en profundidad): usa una PILA (LIFO). Como saca
     * primero el estado mas reciente, se hunde por un camino hasta el
     * fondo antes de retroceder. Encuentra UNA solucion valida, aunque no
     * necesariamente la mas corta.
     *
     * @return secuencia de movimientos que resuelve el nivel;
     * lista VACIA si el nivel no tiene solucion (o si se
     * alcanza el limite de estados sin encontrarla)
     */
    private CustomLinkedList<Direction> resolverConPila() {
        // Caso borde: el nivel ya esta resuelto.
        if (estadoInicial.esGanador(board)) {
            return new CustomLinkedList<>();
        }

        // Pila con los estados pendientes por explorar.
        CustomStack<EstadoSokoban> frontera = new CustomStack<>();

        // Tabla hash de estados ya visitados. Por cada estado guardamos
        // como se llego a el (estado anterior + movimiento), para poder
        // reconstruir la secuencia al final.
        TranspositionTable visitados = new TranspositionTable();

        frontera.push(estadoInicial);
        visitados.put(estadoInicial.toString(), new Rastro(null, null));

        int estadosExplorados = 0;

        while (!frontera.isEmpty()) {
            EstadoSokoban actual = frontera.pop();
            estadosExplorados++;

            if (estadosExplorados > LIMITE_ESTADOS) {
                // El espacio de busqueda es demasiado grande; abortar.
                return new CustomLinkedList<>();
            }

            // Probar los cuatro movimientos desde el estado actual.
            for (Direction dir : MOVIMIENTOS) {
                EstadoSokoban vecino = simularMovimiento(actual, dir);
                if (vecino == null) continue;
                String clave = vecino.toString();
                if (visitados.containsKey(clave)) continue;
                visitados.put(clave, new Rastro(actual, dir));
                if (vecino.esGanador(board)) return reconstruirCamino(vecino, visitados);
                frontera.enqueue(vecino);
            }
        }
        return new CustomLinkedList<>();
    }

    // ─────────────────────────────────────────────────────────────────────
    // DFS — busqueda en profundidad, usa pila (CustomStack)
    // ─────────────────────────────────────────────────────────────────────
    private CustomLinkedList<Direction> resolverDFS() {
        CustomStack<EstadoSokoban> frontera = new CustomStack<>();
        TranspositionTable visitados = new TranspositionTable();

        frontera.push(estadoInicial);
        visitados.put(estadoInicial.toString(), new Rastro(null, null));

        int explorados = 0;

        while (!frontera.isEmpty()) {
            EstadoSokoban actual = frontera.pop();
            if (++explorados > LIMITE_ESTADOS) return new CustomLinkedList<>();

            for (Direction dir : MOVIMIENTOS) {
                EstadoSokoban vecino = simularMovimiento(actual, dir);
                if (vecino == null) continue;
                String clave = vecino.toString();
                if (visitados.containsKey(clave)) continue;
                visitados.put(clave, new Rastro(actual, dir));
                if (vecino.esGanador(board)) return reconstruirCamino(vecino, visitados);
                frontera.push(vecino);
            }
        }
        return new CustomLinkedList<>();
    }

    // ─────────────────────────────────────────────────────────────────────
    // Simulacion de movimiento — no toca el objeto Game
    // ─────────────────────────────────────────────────────────────────────
                // Estado nuevo no ganador: apilarlo para explorarlo luego.
                frontera.push(vecino);
            }
        }

        // Se agoto la pila sin encontrar solucion.
        return new CustomLinkedList<>();
    }

    /**
     * Simula un movimiento del jugador sobre un estado, sin tocar el
     * objeto Game. Aplica las mismas reglas que Game.move():
     * <p>
     * - el jugador no puede atravesar muros ni salir del tablero;
     * - si hay una caja al frente, se empuja solo si la celda
     * siguiente esta libre (no es muro ni otra caja);
     * - dos cajas seguidas no se pueden empujar.
     *
     * @param estado estado de partida
     * @param dir    direccion del movimiento
     * @return el estado resultante, o null si el movimiento es invalido
     */
    private EstadoSokoban simularMovimiento(EstadoSokoban estado, Direction dir) {
        int dFila = 0, dCol = 0;
        switch (dir) {
            case UP:
                dFila = -1;
                break;
            case DOWN:
                dFila = +1;
                break;
            case LEFT:
                dCol = -1;
                break;
            case RIGHT:
                dCol = +1;
                break;
            default:
                return null;
        }

        int nuevaFilaJ = estado.getJugadorFila() + dFila;
        int nuevaColJ = estado.getJugadorColumna() + dCol;

        if (!board.isValidPosition(nuevaFilaJ, nuevaColJ)) return null;
        if (board.isWall(nuevaFilaJ, nuevaColJ)) return null;

        int n = estado.cantidadCajas();
        int[] cajasFila = new int[n];
        int[] cajasCol  = new int[n];
        for (int i = 0; i < n; i++) {
            cajasFila[i] = estado.getCajaFila(i);
            cajasCol[i]  = estado.getCajaColumna(i);
        }

        int indiceCaja = -1;
        for (int i = 0; i < n; i++) {
            if (cajasFila[i] == nuevaFilaJ && cajasCol[i] == nuevaColJ) {
                indiceCaja = i;
                break;
            }
        }

        if (indiceCaja != -1) {
            int nuevaFilaC = nuevaFilaJ + dFila;
            int nuevaColC  = nuevaColJ  + dCol;
            if (!board.isValidPosition(nuevaFilaC, nuevaColC)) return null;
            if (board.isWall(nuevaFilaC, nuevaColC)) return null;
            if (estado.hayCaja(nuevaFilaC, nuevaColC)) return null;
            cajasFila[indiceCaja] = nuevaFilaC;
            cajasCol[indiceCaja]  = nuevaColC;
        }

        return new EstadoSokoban(nuevaFilaJ, nuevaColJ, cajasFila, cajasCol);
    }

    // ─────────────────────────────────────────────────────────────────────
    // Reconstruccion del camino desde el estado ganador hacia el inicial
    // ─────────────────────────────────────────────────────────────────────
    /**
     * Reconstruye la secuencia de movimientos desde el estado inicial
     * hasta el estado ganador, recorriendo los rastros hacia atras.
     * <p>
     * Como los rastros apuntan al pasado (cada estado guarda de donde
     * vino), se arma la lista al reves y luego se invierte.
     *
     * @param estadoFinal estado ganador encontrado
     * @param visitados   tabla con el rastro de cada estado
     * @return secuencia de movimientos en el orden correcto
     */
    private CustomLinkedList<Direction> reconstruirCamino(
            EstadoSokoban estadoFinal, TranspositionTable visitados) {

        CustomLinkedList<Direction> alReves = new CustomLinkedList<>();
        EstadoSokoban actual = estadoFinal;

        while (true) {
            Rastro rastro = (Rastro) visitados.get(actual.toString());
            if (rastro == null || rastro.movimiento == null) break;
            alReves.add(rastro.movimiento);
            actual = rastro.estadoAnterior;
        }

        CustomLinkedList<Direction> camino = new CustomLinkedList<>();
        for (int i = alReves.size() - 1; i >= 0; i--) {
            camino.add(alReves.get(i));
        }
        return camino;
    }

    private static class Rastro {
        private final EstadoSokoban estadoAnterior;
        private final Direction movimiento;

        Rastro(EstadoSokoban estadoAnterior, Direction movimiento) {
            this.estadoAnterior = estadoAnterior;
            this.movimiento     = movimiento;
        }
    }
}
