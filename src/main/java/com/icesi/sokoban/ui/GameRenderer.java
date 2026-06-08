package com.icesi.sokoban.ui;

import com.icesi.sokoban.model.Board;
import com.icesi.sokoban.model.Box;
import com.icesi.sokoban.model.Direction;
import com.icesi.sokoban.model.Game;
import com.icesi.sokoban.model.Player;
import com.icesi.sokoban.model.Position;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * UI — GameRenderer
 *
 * Dibuja el estado del juego sobre el Canvas.
 * Cada nivel tiene su propio fondo (bg_level1.png, bg_level2.png, bg_level3.png).
 * El tablero se dibuja semitransparente encima del fondo.
 * El jugador usa sprites direccionales.
 */
public class GameRenderer {

    public static final int TILE_SIZE = 60;

    private final Canvas canvas;

    // Sprites del jugador
    private Image spriteDown, spriteUp, spriteLeft, spriteRight;
    private boolean spritesLoaded = false;

    // Fondos por nivel
    private Image bgLevel1, bgLevel2, bgLevel3;
    private boolean bgsLoaded = false;

    // Nivel actual para elegir fondo
    private int currentLevel = 1;

    // Última dirección
    private Direction lastDirection = Direction.DOWN;

    // Colores del tablero
    private static final Color C_BG        = Color.web("#0f0e17");
    private static final Color C_FLOOR     = Color.web("#2a2a3e", 0.75);
    private static final Color C_WALL      = Color.web("#4a4e69", 0.90);
    private static final Color C_GOAL      = Color.web("#c77dff", 0.85);
    private static final Color C_BOX       = Color.web("#e07a5f");
    private static final Color C_BOX_OG    = Color.web("#81b29a");

    public GameRenderer(Canvas canvas) {
        this.canvas = canvas;
        cargarSprites();
        cargarFondos();
    }

    // ─────────────────────────────────────────────────────────────────────
    // Carga de recursos
    // ─────────────────────────────────────────────────────────────────────
    private void cargarSprites() {
        try {
            String base = "/com/icesi/sokoban/sprites/";
            spriteDown  = new Image(getClass().getResourceAsStream(base + "player_down.png"));
            spriteUp    = new Image(getClass().getResourceAsStream(base + "player_up.png"));
            spriteLeft  = new Image(getClass().getResourceAsStream(base + "player_left.png"));
            spriteRight = new Image(getClass().getResourceAsStream(base + "player_right.png"));
            spritesLoaded = true;
        } catch (Exception e) {
            spritesLoaded = false;
        }
    }

    private void cargarFondos() {
        try {
            String base = "/com/icesi/sokoban/sprites/";
            bgLevel1 = new Image(getClass().getResourceAsStream(base + "bg_level1.png"));
            bgLevel2 = new Image(getClass().getResourceAsStream(base + "bg_level2.png"));
            bgLevel3 = new Image(getClass().getResourceAsStream(base + "bg_level3.png"));
            bgsLoaded = true;
        } catch (Exception e) {
            bgsLoaded = false;
        }
    }

    // ─────────────────────────────────────────────────────────────────────
    // API pública
    // ─────────────────────────────────────────────────────────────────────
    public void setLastDirection(Direction direction) {
        if (direction != null) this.lastDirection = direction;
    }

    public void setCurrentLevel(int level) {
        this.currentLevel = level;
    }

    public void resizeToBoard(Board board) {
        canvas.setWidth(board.getWidth() * TILE_SIZE);
        canvas.setHeight(board.getHeight() * TILE_SIZE);
    }

    // ─────────────────────────────────────────────────────────────────────
    // Render principal
    // ─────────────────────────────────────────────────────────────────────
    public void render(Game game) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Board board = game.getBoard();

        if (board == null) {
            gc.setFill(C_BG);
            gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
            return;
        }

        // 1. Fondo del nivel
        dibujarFondo(gc);

        // 2. Tablero semitransparente
        for (int row = 0; row < board.getHeight(); row++) {
            for (int col = 0; col < board.getWidth(); col++) {
                double x = col * TILE_SIZE;
                double y = row * TILE_SIZE;
                if (board.isWall(row, col))      drawWall(gc, x, y);
                else if (board.isGoal(row, col)) drawGoal(gc, x, y);
                else                             drawFloor(gc, x, y);
            }
        }

        // 3. Cajas
        for (int i = 0; i < game.getBoxes().size(); i++) {
            Box box = game.getBoxes().get(i);
            Position p = box.getPosition();
            drawBox(gc, p.getColumn() * TILE_SIZE, p.getRow() * TILE_SIZE, box.isOnGoal());
        }

        // 4. Jugador
        Player player = game.getPlayer();
        if (player != null && player.getPosition() != null) {
            Position p = player.getPosition();
            drawPlayer(gc, p.getColumn() * TILE_SIZE, p.getRow() * TILE_SIZE);
        }
    }

    // ─────────────────────────────────────────────────────────────────────
    // Fondo del nivel
    // ─────────────────────────────────────────────────────────────────────
    private void dibujarFondo(GraphicsContext gc) {
        double w = canvas.getWidth();
        double h = canvas.getHeight();

        if (bgsLoaded) {
            Image bg = getFondoParaNivel(currentLevel);
            if (bg != null) {
                gc.drawImage(bg, 0, 0, w, h);
                return;
            }
        }

        // Fallback — color sólido por nivel
        switch (currentLevel) {
            case 1: gc.setFill(Color.web("#1a2e1a")); break;
            case 2: gc.setFill(Color.web("#1a1a2e")); break;
            case 3: gc.setFill(Color.web("#2e1a1a")); break;
            default: gc.setFill(C_BG);
        }
        gc.fillRect(0, 0, w, h);
    }

    private Image getFondoParaNivel(int level) {
        switch (level) {
            case 1: return bgLevel1;
            case 2: return bgLevel2;
            case 3: return bgLevel3;
            default: return bgLevel1;
        }
    }

    // ─────────────────────────────────────────────────────────────────────
    // Primitivas
    // ─────────────────────────────────────────────────────────────────────
    private void drawFloor(GraphicsContext gc, double x, double y) {
        gc.setFill(C_FLOOR);
        gc.fillRect(x, y, TILE_SIZE, TILE_SIZE);
    }

    private void drawWall(GraphicsContext gc, double x, double y) {
        gc.setFill(C_WALL);
        gc.fillRoundRect(x + 1, y + 1, TILE_SIZE - 2, TILE_SIZE - 2, 8, 8);
        // Borde más oscuro para dar relieve
        gc.setStroke(Color.web("#2a2d3e", 0.8));
        gc.setLineWidth(1.5);
        gc.strokeRoundRect(x + 1, y + 1, TILE_SIZE - 2, TILE_SIZE - 2, 8, 8);
    }

    private void drawGoal(GraphicsContext gc, double x, double y) {
        drawFloor(gc, x, y);
        double cx = x + TILE_SIZE / 2.0;
        double cy = y + TILE_SIZE / 2.0;
        double r  = TILE_SIZE * 0.28;
        gc.setFill(C_GOAL);
        gc.fillOval(cx - r, cy - r, r * 2, r * 2);
        gc.setStroke(Color.web("#ffffff", 0.3));
        gc.setLineWidth(1);
        gc.strokeOval(cx - r, cy - r, r * 2, r * 2);
    }

    private void drawBox(GraphicsContext gc, double x, double y, boolean onGoal) {
        double m = 5;
        gc.setFill(onGoal ? C_BOX_OG : C_BOX);
        gc.fillRoundRect(x + m, y + m, TILE_SIZE - m * 2, TILE_SIZE - m * 2, 10, 10);
        gc.setStroke(onGoal ? Color.web("#4a7a5a") : Color.web("#8a3a2a"));
        gc.setLineWidth(2);
        gc.strokeRoundRect(x + m, y + m, TILE_SIZE - m * 2, TILE_SIZE - m * 2, 10, 10);
    }

    private void drawPlayer(GraphicsContext gc, double x, double y) {
        if (spritesLoaded) {
            Image sprite = getSpriteForDirection(lastDirection);
            double margin = 2;
            gc.drawImage(sprite, x + margin, y + margin,
                    TILE_SIZE - margin * 2, TILE_SIZE - margin * 2);
        } else {
            double cx = x + TILE_SIZE / 2.0;
            double headR = TILE_SIZE * 0.18;
            gc.setFill(Color.web("#f2cc8f"));
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
}