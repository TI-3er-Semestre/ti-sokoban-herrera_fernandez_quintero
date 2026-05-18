package com.icesi.sokoban.ui;

import com.icesi.sokoban.model.Board;
import com.icesi.sokoban.model.Box;
import com.icesi.sokoban.model.Game;
import com.icesi.sokoban.model.Player;
import com.icesi.sokoban.model.Position;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * UI — GameRenderer
 *
 * Clase de apoyo a la VISTA. Se encarga de dibujar el estado del juego
 * sobre el Canvas. No es parte del modelo: no contiene reglas del juego,
 * solo lee el estado y lo pinta.
 *
 * El Controller le entrega un Game y esta clase lo dibuja.
 *
 * NOTA PARA EL EQUIPO:
 * Este es un esqueleto funcional basico. Se puede mejorar agregando
 * imagenes (sprites) en lugar de figuras dibujadas, animaciones, etc.
 */
public class GameRenderer {

    // Tamano de cada celda en pixeles
    public static final int TILE_SIZE = 60;

    private final Canvas canvas;

    // Paleta de colores
    private static final Color C_BG     = Color.web("#0f0e17");
    private static final Color C_FLOOR  = Color.web("#1a1a2e");
    private static final Color C_WALL   = Color.web("#4a4e69");
    private static final Color C_GOAL   = Color.web("#c77dff");
    private static final Color C_BOX    = Color.web("#e07a5f");
    private static final Color C_BOX_OG = Color.web("#81b29a");
    private static final Color C_PLAYER = Color.web("#f2cc8f");

    public GameRenderer(Canvas canvas) {
        this.canvas = canvas;
    }

    /**
     * Ajusta el tamano del canvas segun las dimensiones del tablero.
     * Se llama una vez al cargar un nivel.
     */
    public void resizeToBoard(Board board) {
        canvas.setWidth(board.getWidth() * TILE_SIZE);
        canvas.setHeight(board.getHeight() * TILE_SIZE);
    }

    /**
     * Dibuja el estado completo del juego. Lee el modelo, nunca lo modifica.
     */
    public void render(Game game) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Board board = game.getBoard();

        if (board == null) {
            // Todavia no se ha cargado un nivel
            gc.setFill(C_BG);
            gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
            return;
        }

        // Fondo
        gc.setFill(C_BG);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Dibujar el tablero: muros, metas y piso
        for (int row = 0; row < board.getHeight(); row++) {
            for (int col = 0; col < board.getWidth(); col++) {
                double x = col * TILE_SIZE;
                double y = row * TILE_SIZE;

                if (board.isWall(row, col)) {
                    drawWall(gc, x, y);
                } else if (board.isGoal(row, col)) {
                    drawGoal(gc, x, y);
                } else {
                    drawFloor(gc, x, y);
                }
            }
        }

        // Dibujar las cajas
        for (int i = 0; i < game.getBoxes().size(); i++) {
            Box box = game.getBoxes().get(i);
            Position p = box.getPosition();
            drawBox(gc, p.getColumn() * TILE_SIZE, p.getRow() * TILE_SIZE, box.isOnGoal());
        }

        // Dibujar el jugador
        Player player = game.getPlayer();
        if (player != null && player.getPosition() != null) {
            Position p = player.getPosition();
            drawPlayer(gc, p.getColumn() * TILE_SIZE, p.getRow() * TILE_SIZE);
        }
    }

    // ── Primitivas de dibujo ──────────────────────────────────────────────

    private void drawFloor(GraphicsContext gc, double x, double y) {
        gc.setFill(C_FLOOR);
        gc.fillRect(x, y, TILE_SIZE, TILE_SIZE);
    }

    private void drawWall(GraphicsContext gc, double x, double y) {
        gc.setFill(C_WALL);
        gc.fillRoundRect(x + 1, y + 1, TILE_SIZE - 2, TILE_SIZE - 2, 6, 6);
    }

    private void drawGoal(GraphicsContext gc, double x, double y) {
        drawFloor(gc, x, y);
        double cx = x + TILE_SIZE / 2.0;
        double cy = y + TILE_SIZE / 2.0;
        double r = TILE_SIZE * 0.28;
        gc.setFill(C_GOAL);
        gc.fillOval(cx - r, cy - r, r * 2, r * 2);
    }

    private void drawBox(GraphicsContext gc, double x, double y, boolean onGoal) {
        double m = 6;
        gc.setFill(onGoal ? C_BOX_OG : C_BOX);
        gc.fillRoundRect(x + m, y + m, TILE_SIZE - m * 2, TILE_SIZE - m * 2, 8, 8);
    }

    private void drawPlayer(GraphicsContext gc, double x, double y) {
        double cx = x + TILE_SIZE / 2.0;
        double headR = TILE_SIZE * 0.18;
        gc.setFill(C_PLAYER);
        gc.fillRoundRect(x + 16, y + 26, TILE_SIZE - 32, TILE_SIZE - 32, 6, 6);
        gc.fillOval(cx - headR, y + 10, headR * 2, headR * 2);
    }
}
