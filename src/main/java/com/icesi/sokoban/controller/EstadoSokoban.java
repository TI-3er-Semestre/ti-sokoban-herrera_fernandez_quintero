package com.icesi.sokoban.controller;

import com.icesi.sokoban.model.Board;
import com.icesi.sokoban.model.Box;
import com.icesi.sokoban.model.Game;
import com.icesi.sokoban.model.Position;
import com.icesi.sokoban.structure.CustomLinkedList;

/**
 * Representacion LIGERA e INMUTABLE de un estado del juego Sokoban,
 * usada por el SokobanSolver para la busqueda automatica.
 *
 * Por que una clase aparte y no usar Game directamente:
 *   El solver explora miles de estados. Si simulara los movimientos
 *   sobre el objeto Game real, tendria que mutarlo y revertirlo todo
 *   el tiempo, lo cual es fragil y lento. En cambio, cada estado se
 *   representa solo con lo que cambia entre un estado y otro:
 *
 *      - la posicion del jugador
 *      - las posiciones de las cajas
 *
 *   Los muros y las metas son fijos, asi que NO se guardan aqui:
 *   se consultan directamente del Board, que nunca cambia.
 *
 * Modelado como grafo:
 *   Cada EstadoSokoban es un VERTICE del grafo de estados.
 *   Cada movimiento valido que lleva de un estado a otro es una ARISTA.
 *   Resolver el nivel es encontrar un camino desde el estado inicial
 *   hasta un estado donde todas las cajas esten sobre metas.
 *
 * La clave generada por toString() es lo que se usa para detectar
 * estados repetidos con la TranspositionTable.
 */
public final class EstadoSokoban {

    private final int jugadorFila;
    private final int jugadorColumna;
    // Posiciones de las cajas. Se ordenan al construir para que dos
    // estados con las mismas cajas en distinto orden sean iguales.
    private final int[] cajasFila;
    private final int[] cajasColumna;

    /**
     * Construye un estado a partir de posiciones explicitas.
     */
    public EstadoSokoban(int jugadorFila, int jugadorColumna,
                         int[] cajasFila, int[] cajasColumna) {
        this.jugadorFila = jugadorFila;
        this.jugadorColumna = jugadorColumna;
        this.cajasFila = cajasFila;
        this.cajasColumna = cajasColumna;
        ordenarCajas();
    }

    /**
     * Crea el estado inicial leyendo la situacion actual de un Game.
     *
     * @param game juego con un nivel ya cargado
     * @return estado que representa la posicion actual
     */
    public static EstadoSokoban desdeJuego(Game game) {
        Position jugador = game.getPlayer().getPosition();
        CustomLinkedList<Box> cajas = game.getBoxes();

        int n = cajas.size();
        int[] filas = new int[n];
        int[] cols = new int[n];
        for (int i = 0; i < n; i++) {
            Position p = cajas.get(i).getPosition();
            filas[i] = p.getRow();
            cols[i] = p.getColumn();
        }
        return new EstadoSokoban(jugador.getRow(), jugador.getColumn(), filas, cols);
    }

    /**
     * Ordena las cajas por (fila, columna) con un insertion sort simple.
     * Asi la clave del estado no depende del orden en que se guardaron.
     */
    private void ordenarCajas() {
        for (int i = 1; i < cajasFila.length; i++) {
            int f = cajasFila[i];
            int c = cajasColumna[i];
            int j = i - 1;
            while (j >= 0 && esMayor(cajasFila[j], cajasColumna[j], f, c)) {
                cajasFila[j + 1] = cajasFila[j];
                cajasColumna[j + 1] = cajasColumna[j];
                j--;
            }
            cajasFila[j + 1] = f;
            cajasColumna[j + 1] = c;
        }
    }

    /** Devuelve true si (f1,c1) va despues que (f2,c2) en orden fila-columna. */
    private boolean esMayor(int f1, int c1, int f2, int c2) {
        if (f1 != f2) {
            return f1 > f2;
        }
        return c1 > c2;
    }

    /**
     * Indica si en este estado hay una caja en la posicion dada.
     */
    public boolean hayCaja(int fila, int columna) {
        for (int i = 0; i < cajasFila.length; i++) {
            if (cajasFila[i] == fila && cajasColumna[i] == columna) {
                return true;
            }
        }
        return false;
    }

    /**
     * Indica si este estado es ganador: todas las cajas sobre metas.
     *
     * @param board tablero del nivel (para consultar las metas)
     */
    public boolean esGanador(Board board) {
        for (int i = 0; i < cajasFila.length; i++) {
            if (!board.isGoal(cajasFila[i], cajasColumna[i])) {
                return false;
            }
        }
        return true;
    }

    public int getJugadorFila() {
        return jugadorFila;
    }

    public int getJugadorColumna() {
        return jugadorColumna;
    }

    public int cantidadCajas() {
        return cajasFila.length;
    }

    public int getCajaFila(int i) {
        return cajasFila[i];
    }

    public int getCajaColumna(int i) {
        return cajasColumna[i];
    }

    /**
     * Clave unica del estado, usada por la TranspositionTable para
     * detectar estados repetidos.
     *
     * Formato:  "filaJ,colJ;filaC1,colC1|filaC2,colC2|..."
     *
     * Como las cajas estan ordenadas, dos estados identicos siempre
     * producen exactamente la misma clave.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(jugadorFila).append(',').append(jugadorColumna).append(';');
        for (int i = 0; i < cajasFila.length; i++) {
            sb.append(cajasFila[i]).append(',').append(cajasColumna[i]).append('|');
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof EstadoSokoban)) {
            return false;
        }
        return this.toString().equals(obj.toString());
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}
