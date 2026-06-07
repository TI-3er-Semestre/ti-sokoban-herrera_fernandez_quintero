package com.icesi.sokoban.ui;

import com.icesi.sokoban.model.Board;
import com.icesi.sokoban.model.Box;
import com.icesi.sokoban.model.Direction;
import com.icesi.sokoban.model.Game;
import com.icesi.sokoban.model.Player;
import com.icesi.sokoban.model.Position;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * UI — GameRenderer
 *
 * Dibuja el estado del juego sobre el Canvas.
 * Usa sprites PNG para el jugador — uno por dirección.
 * El sprite cambia según la última dirección de movimiento.
 *
 * Los sprites viven en:
 *   src/main/resources/com/icesi/sokoban/sprites/
 *     player_down.png, player_up.png, player_left.png, player_right.png
 */
public class GameRenderer {

    public static final int TILE_SIZE = 60;

    private final Canvas canvas;

    // Sprites del jugador — uno por dirección
    private Image spriteDown;
    private Image spriteUp;
    private Image spriteLeft;
    private Image spriteRight;
    private boolean spritesLoaded = false;

    // Última dirección — determina qué sprite mostrar
    private Direction lastDirection = Direction.DOWN;

    // Paleta de colores para elementos no-sprite
    private static final Color C_BG     = Color.web("#0f0e17");
    private static final Color C_FLOOR  = Color.web("#1a1a2e");
    private static final Color C_WALL   = Color.web("#4a4e69");
    private static final Color C_GOAL   = Color.web("#c77dff");
    private static final Color C_BOX    = Color.web("#e07a5f");
    private static final Color C_BOX_OG = Color.web("#81b29a");
    private static final Color C_PLAYER = Color.web("#f2cc8f");

    public GameRenderer(Canvas canvas) {
        this.canvas = canvas;
        cargarSprites();
    }

    // ─────────────────────────────────────────────────────────────────────
    // Carga de sprites
    // ─────────────────────────────────────────────────────────────────────

    /**
     * Carga los 4 sprites del jugador desde resources.
     * Si alguno falla, spritesLoaded queda false y se usa el dibujo
     * geométrico como fallback — el juego nunca crashea por un sprite.
     */
    private void cargarSprites() {
        try {
            String base = "/com/icesi/sokoban/sprites/";
            spriteDown  = new Image(getClass().getResourceAsStream(base + "player_down.png"));
            spriteUp    = new Image(getClass().getResourceAsStream(base + "player_up.png"));
            spriteLeft  = new Image(getClass().getResourceAsStream(base + "player_left.png"));
            spriteRight = new Image(getClass().getResourceAsStream(base + "player_right.png"));
            spritesLoaded = true;
        } catch (Exception e) {
            System.err.println("No se pudieron cargar los sprites: " + e.getMessage());
            spritesLoaded = false;
        }
    }

    /**
     * Actualiza la dirección del último movimiento.
     * GameController llama esto antes de render() en cada tecla.
     */
    public void setLastDirection(Direction direction) {
        if (direction != null) this.lastDirection = direction;
    }

    // ─────────────────────────────────────────────────────────────────────
    // Render principal
    // ─────────────────────────────────────────────────────────────────────

    public void resizeToBoard(Board board) {
        canvas.setWidth(board.getWidth() * TILE_SIZE);
        canvas.setHeight(board.getHeight() * TILE_SIZE);
    }

    public void render(Game game) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Board board = game.getBoard();

        if (board == null) {
            gc.setFill(C_BG);
            gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
            return;
        }

        // Fondo
        gc.setFill(C_BG);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Tablero: muros, metas, piso
        for (int row = 0; row < board.getHeight(); row++) {
            for (int col = 0; col < board.getWidth(); col++) {
                double x = col * TILE_SIZE;
                double y = row * TILE_SIZE;
                if (board.isWall(row, col))       drawWall(gc, x, y);
                else if (board.isGoal(row, col))  drawGoal(gc, x, y);
                else                              drawFloor(gc, x, y);
            }
        }

        // Cajas
        for (int i = 0; i < game.getBoxes().size(); i++) {
            Box box = game.getBoxes().get(i);
            Position p = box.getPosition();
            drawBox(gc, p.getColumn() * TILE_SIZE, p.getRow() * TILE_SIZE, box.isOnGoal());
        }

        // Jugador con sprite
        Player player = game.getPlayer();
        if (player != null && player.getPosition() != null) {
            Position p = player.getPosition();
            drawPlayer(gc, p.getColumn() * TILE_SIZE, p.getRow() * TILE_SIZE);
        }
    }

    // ─────────────────────────────────────────────────────────────────────
    // Dibujo del jugador
    // ─────────────────────────────────────────────────────────────────────

    private void drawPlayer(GraphicsContext gc, double x, double y) {
        if (spritesLoaded) {
            Image sprite = getSpriteForDirection(lastDirection);
            // Centrar el sprite en la celda con un pequeño margen
            double margin = 2;
            gc.drawImage(sprite,
                    x + margin,
                    y + margin,
                    TILE_SIZE - margin * 2,
                    TILE_SIZE - margin * 2);
        } else {
            // Fallback geométrico si no hay sprites
            double cx = x + TILE_SIZE / 2.0;
            double headR = TILE_SIZE * 0.18;
            gc.setFill(C_PLAYER);
            gc.fillRoundRect(x + 16, y + 26, TILE_SIZE - 32, TILE_SIZE - 32, 6, 6);
            gc.fillOval(cx - headR, y + 10, headR * 2, headR * 2);
        }
    }

    private Image getSpriteForDirection(Direction dir) {
        switch (dir) {
            case UP:    return spriteUp;
            case DOWN:  return spriteDown;
            case LEFT:  return spriteLeft;
            case RIGHT: return spriteRight;
            default:    return spriteDown;
        }
    }

    // ─────────────────────────────────────────────────────────────────────
    // Primitivas de dibujo
    // ─────────────────────────────────────────────────────────────────────

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
        double r  = TILE_SIZE * 0.28;
        gc.setFill(C_GOAL);
        gc.fillOval(cx - r, cy - r, r * 2, r * 2);
    }

    private void drawBox(GraphicsContext gc, double x, double y, boolean onGoal) {
        double m = 6;
        gc.setFill(onGoal ? C_BOX_OG : C_BOX);
        gc.fillRoundRect(x + m, y + m, TILE_SIZE - m * 2, TILE_SIZE - m * 2, 8, 8);
    }
}
