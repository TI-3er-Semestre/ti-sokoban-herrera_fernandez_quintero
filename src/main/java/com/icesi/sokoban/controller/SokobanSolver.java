package com.icesi.sokoban.controller;

import com.icesi.sokoban.model.Board;
import com.icesi.sokoban.model.Direction;
import com.icesi.sokoban.model.Game;
import com.icesi.sokoban.structure.CustomLinkedList;
import com.icesi.sokoban.structure.CustomQueue;
import com.icesi.sokoban.structure.CustomStack;
import com.icesi.sokoban.structure.TranspositionTable;
import com.icesi.sokoban.model.Position;
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

    public enum Algoritmo {
        BFS, DFS
    }

    private static final Direction[] MOVIMIENTOS = {
            Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT
    };

    private final Board board;
    private final EstadoSokoban estadoInicial;
    private final boolean[][] casillaMuerta;

    private static final int LIMITE_ESTADOS = 200_000;

    public SokobanSolver(Game game) {
        if (game == null || game.getBoard() == null) {
            throw new IllegalArgumentException("El juego debe tener un nivel cargado");
        }
        this.board = game.getBoard();
        this.estadoInicial = EstadoSokoban.desdeJuego(game);
        this.casillaMuerta = calcularCasillasMuertas();
    }

    public CustomLinkedList<Direction> resolver() {
        return resolver(Algoritmo.BFS);
    }

    public CustomLinkedList<Direction> resolver(Algoritmo algoritmo) {
        if (algoritmo == Algoritmo.DFS) {
            return resolverConPila();
        } else {
            return resolverConCola();
        }
    }

    private CustomLinkedList<Direction> resolverConCola() {
        if (estadoInicial.esGanador(board)) {
            return new CustomLinkedList<>();
        }
        CustomQueue<Nodo> frontera = new CustomQueue<>();
        TranspositionTable visitados = new TranspositionTable();

        frontera.enqueue(new Nodo(estadoInicial, null, null));
        visitados.put(claveCanonica(estadoInicial), Boolean.TRUE);

        int explorados = 0;
        while (!frontera.isEmpty()) {
            Nodo actual = frontera.dequeue();
            explorados++;
            if (explorados > LIMITE_ESTADOS) {
                return new CustomLinkedList<>();
            }
            CustomLinkedList<Sucesor> sucesores = generarSucesores(actual.estado);
            for (int i = 0; i < sucesores.size(); i++) {
                Sucesor s = sucesores.get(i);
                if (visitados.containsKey(s.clave)) {
                    continue;
                }
                visitados.put(s.clave, Boolean.TRUE);
                Nodo hijo = new Nodo(s.estado, actual, s.camino);
                if (s.estado.esGanador(board)) {
                    return reconstruir(hijo);
                }
                frontera.enqueue(hijo);
            }
        }
        return new CustomLinkedList<>();
    }

    private CustomLinkedList<Direction> resolverConPila() {
        if (estadoInicial.esGanador(board)) {
            return new CustomLinkedList<>();
        }
        CustomStack<Nodo> frontera = new CustomStack<>();
        TranspositionTable visitados = new TranspositionTable();

        frontera.push(new Nodo(estadoInicial, null, null));
        visitados.put(claveCanonica(estadoInicial), Boolean.TRUE);

        int explorados = 0;
        while (!frontera.isEmpty()) {
            Nodo actual = frontera.pop();
            explorados++;
            if (explorados > LIMITE_ESTADOS) {
                return new CustomLinkedList<>();
            }
            CustomLinkedList<Sucesor> sucesores = generarSucesores(actual.estado);
            for (int i = 0; i < sucesores.size(); i++) {
                Sucesor s = sucesores.get(i);
                if (visitados.containsKey(s.clave)) {
                    continue;
                }
                visitados.put(s.clave, Boolean.TRUE);
                Nodo hijo = new Nodo(s.estado, actual, s.camino);
                if (s.estado.esGanador(board)) {
                    return reconstruir(hijo);
                }
                frontera.push(hijo);
            }
        }
        return new CustomLinkedList<>();
    }

    private CustomLinkedList<Sucesor> generarSucesores(EstadoSokoban actual) {
        CustomLinkedList<Sucesor> resultado = new CustomLinkedList<>();
        boolean[][] ocupadas = ocupadasPorCajas(actual);
        boolean[][] alcanzables = alcanzablesJugador(
                ocupadas, actual.getJugadorFila(), actual.getJugadorColumna());

        int n = actual.cantidadCajas();
        for (int i = 0; i < n; i++) {
            int cajaF = actual.getCajaFila(i);
            int cajaC = actual.getCajaColumna(i);
            for (Direction dir : MOVIMIENTOS) {
                int dF = deltaFila(dir);
                int dC = deltaColumna(dir);
                int empujadorF = cajaF - dF;
                int empujadorC = cajaC - dC;
                int destinoF = cajaF + dF;
                int destinoC = cajaC + dC;

                if (!board.isValidPosition(empujadorF, empujadorC)) continue;
                if (!alcanzables[empujadorF][empujadorC]) continue;
                if (!board.isValidPosition(destinoF, destinoC)) continue;
                if (board.isWall(destinoF, destinoC)) continue;
                if (ocupadas[destinoF][destinoC]) continue;
                if (casillaMuerta[destinoF][destinoC]) continue;

                CustomLinkedList<Direction> camino = caminar(
                        ocupadas, actual.getJugadorFila(), actual.getJugadorColumna(),
                        empujadorF, empujadorC);
                if (camino == null) continue;
                camino.add(dir);

                int[] nuevasF = new int[n];
                int[] nuevasC = new int[n];
                for (int k = 0; k < n; k++) {
                    nuevasF[k] = actual.getCajaFila(k);
                    nuevasC[k] = actual.getCajaColumna(k);
                }
                nuevasF[i] = destinoF;
                nuevasC[i] = destinoC;

                EstadoSokoban nuevo = new EstadoSokoban(cajaF, cajaC, nuevasF, nuevasC);
                resultado.add(new Sucesor(nuevo, claveCanonica(nuevo), camino));
            }
        }
        return resultado;
    }

    private boolean[][] ocupadasPorCajas(EstadoSokoban estado) {
        boolean[][] ocup = new boolean[board.getHeight()][board.getWidth()];
        for (int i = 0; i < estado.cantidadCajas(); i++) {
            ocup[estado.getCajaFila(i)][estado.getCajaColumna(i)] = true;
        }
        return ocup;
    }

    private boolean[][] alcanzablesJugador(boolean[][] ocupadas, int sf, int sc) {
        boolean[][] visto = new boolean[board.getHeight()][board.getWidth()];
        CustomQueue<int[]> cola = new CustomQueue<>();
        visto[sf][sc] = true;
        cola.enqueue(new int[]{sf, sc});
        while (!cola.isEmpty()) {
            int[] c = cola.dequeue();
            for (Direction dir : MOVIMIENTOS) {
                int nf = c[0] + deltaFila(dir);
                int nc = c[1] + deltaColumna(dir);
                if (board.isValidPosition(nf, nc) && !board.isWall(nf, nc)
                        && !ocupadas[nf][nc] && !visto[nf][nc]) {
                    visto[nf][nc] = true;
                    cola.enqueue(new int[]{nf, nc});
                }
            }
        }
        return visto;
    }

    private CustomLinkedList<Direction> caminar(boolean[][] ocupadas,
                                                int fromF, int fromC, int toF, int toC) {
        if (fromF == toF && fromC == toC) {
            return new CustomLinkedList<>();
        }
        int alto = board.getHeight();
        int ancho = board.getWidth();
        boolean[][] visto = new boolean[alto][ancho];
        int[][] deDondeF = new int[alto][ancho];
        int[][] deDondeC = new int[alto][ancho];
        Direction[][] movHacia = new Direction[alto][ancho];

        CustomQueue<int[]> cola = new CustomQueue<>();
        visto[fromF][fromC] = true;
        cola.enqueue(new int[]{fromF, fromC});

        while (!cola.isEmpty()) {
            int[] c = cola.dequeue();
            for (Direction dir : MOVIMIENTOS) {
                int nf = c[0] + deltaFila(dir);
                int nc = c[1] + deltaColumna(dir);
                if (board.isValidPosition(nf, nc) && !board.isWall(nf, nc)
                        && !ocupadas[nf][nc] && !visto[nf][nc]) {
                    visto[nf][nc] = true;
                    deDondeF[nf][nc] = c[0];
                    deDondeC[nf][nc] = c[1];
                    movHacia[nf][nc] = dir;
                    if (nf == toF && nc == toC) {
                        return reconstruirCaminata(deDondeF, deDondeC, movHacia,
                                fromF, fromC, toF, toC);
                    }
                    cola.enqueue(new int[]{nf, nc});
                }
            }
        }
        return null;
    }

    private CustomLinkedList<Direction> reconstruirCaminata(int[][] deDondeF, int[][] deDondeC,
                                                            Direction[][] movHacia,
                                                            int fromF, int fromC, int toF, int toC) {
        CustomLinkedList<Direction> alReves = new CustomLinkedList<>();
        int f = toF;
        int c = toC;
        while (!(f == fromF && c == fromC)) {
            alReves.add(movHacia[f][c]);
            int pf = deDondeF[f][c];
            int pc = deDondeC[f][c];
            f = pf;
            c = pc;
        }
        CustomLinkedList<Direction> camino = new CustomLinkedList<>();
        for (int i = alReves.size() - 1; i >= 0; i--) {
            camino.add(alReves.get(i));
        }
        return camino;
    }

    private String claveCanonica(EstadoSokoban estado) {
        int ancho = board.getWidth();
        int n = estado.cantidadCajas();
        int[] celdas = new int[n];
        for (int i = 0; i < n; i++) {
            celdas[i] = estado.getCajaFila(i) * ancho + estado.getCajaColumna(i);
        }
        for (int i = 1; i < n; i++) {
            int v = celdas[i];
            int j = i - 1;
            while (j >= 0 && celdas[j] > v) {
                celdas[j + 1] = celdas[j];
                j--;
            }
            celdas[j + 1] = v;
        }
        boolean[][] ocup = ocupadasPorCajas(estado);
        boolean[][] reach = alcanzablesJugador(
                ocup, estado.getJugadorFila(), estado.getJugadorColumna());
        int minCelda = Integer.MAX_VALUE;
        for (int r = 0; r < board.getHeight(); r++) {
            for (int col = 0; col < ancho; col++) {
                if (reach[r][col]) {
                    int idx = r * ancho + col;
                    if (idx < minCelda) minCelda = idx;
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append(celdas[i]).append(',');
        }
        sb.append('|').append(minCelda);
        return sb.toString();
    }

    private CustomLinkedList<Direction> reconstruir(Nodo nodoFinal) {
        CustomLinkedList<CustomLinkedList<Direction>> tramos = new CustomLinkedList<>();
        Nodo n = nodoFinal;
        while (n != null && n.camino != null) {
            tramos.add(n.camino);
            n = n.padre;
        }
        CustomLinkedList<Direction> completo = new CustomLinkedList<>();
        for (int i = tramos.size() - 1; i >= 0; i--) {
            CustomLinkedList<Direction> tramo = tramos.get(i);
            for (int j = 0; j < tramo.size(); j++) {
                completo.add(tramo.get(j));
            }
        }
        return completo;
    }

    private boolean[][] calcularCasillasMuertas() {
        int alto = board.getHeight();
        int ancho = board.getWidth();
        boolean[][] vivas = new boolean[alto][ancho];

        CustomQueue<int[]> cola = new CustomQueue<>();
        CustomLinkedList<Position> metas = board.getGoals();
        for (int i = 0; i < metas.size(); i++) {
            Position meta = metas.get(i);
            int r = meta.getRow();
            int c = meta.getColumn();
            if (!vivas[r][c]) {
                vivas[r][c] = true;
                cola.enqueue(new int[]{r, c});
            }
        }
        while (!cola.isEmpty()) {
            int[] celda = cola.dequeue();
            int r = celda[0];
            int c = celda[1];
            for (Direction dir : MOVIMIENTOS) {
                int dr = deltaFila(dir);
                int dc = deltaColumna(dir);
                int ar = r - dr;
                int ac = c - dc;
                int pr = r - 2 * dr;
                int pc = c - 2 * dc;
                if (board.isValidPosition(ar, ac) && !board.isWall(ar, ac)
                        && board.isValidPosition(pr, pc) && !board.isWall(pr, pc)
                        && !vivas[ar][ac]) {
                    vivas[ar][ac] = true;
                    cola.enqueue(new int[]{ar, ac});
                }
            }
        }
        boolean[][] muertas = new boolean[alto][ancho];
        for (int r = 0; r < alto; r++) {
            for (int c = 0; c < ancho; c++) {
                if (board.isValidPosition(r, c) && !board.isWall(r, c) && !vivas[r][c]) {
                    muertas[r][c] = true;
                }
            }
        }
        return muertas;
    }

    private int deltaFila(Direction dir) {
        if (dir == Direction.UP) return -1;
        if (dir == Direction.DOWN) return 1;
        return 0;
    }

    private int deltaColumna(Direction dir) {
        if (dir == Direction.LEFT) return -1;
        if (dir == Direction.RIGHT) return 1;
        return 0;
    }

    private static class Sucesor {
        private final EstadoSokoban estado;
        private final String clave;
        private final CustomLinkedList<Direction> camino;

        Sucesor(EstadoSokoban estado, String clave, CustomLinkedList<Direction> camino) {
            this.estado = estado;
            this.clave = clave;
            this.camino = camino;
        }
    }

    private static class Nodo {
        private final EstadoSokoban estado;
        private final Nodo padre;
        private final CustomLinkedList<Direction> camino;

        Nodo(EstadoSokoban estado, Nodo padre, CustomLinkedList<Direction> camino) {
            this.estado = estado;
            this.padre = padre;
            this.camino = camino;
        }
    }
}
