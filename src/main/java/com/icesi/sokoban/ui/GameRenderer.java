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

public class GameRenderer {

    public static final int TILE_SIZE = 60;

    private final Canvas canvas;

    // Sprites del jugador
    private Image spriteDown, spriteUp, spriteLeft, spriteRight;
    private boolean spritesLoaded = false;

    // Fondos por nivel — se cargan independientemente del jaguar
    private Image bgLevel1, bgLevel2, bgLevel3;
    private boolean bgsLoaded = false;

    // Tile de pared (jaguar)
    private Image wallTile;
    private boolean wallTileLoaded = false;

    private int currentLevel = 1;
    private Direction lastDirection = Direction.DOWN;

    private static final Color C_BOX    = Color.web("#e07a5f");
    private static final Color C_BOX_OG = Color.web("#81b29a");
    private static final Color C_GOAL   = Color.web("#c77dff");

    public GameRenderer(Canvas canvas) {
        this.canvas = canvas;
        cargarSprites();
        cargarFondos();
        cargarJaguar();
    }

    // ─────────────────────────────────────────────────────────────────────
    // Carga de recursos — cada uno en su propio try/catch
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
            Image b1 = new Image(getClass().getResourceAsStream(base + "bg_level1.png"));
            Image b2 = new Image(getClass().getResourceAsStream(base + "bg_level2.png"));
            Image b3 = new Image(getClass().getResourceAsStream(base + "bg_level3.png"));
            // Verificar que se cargaron correctamente
            if (b1.isError() || b2.isError() || b3.isError()) {
                bgsLoaded = false;
                return;
            }
            bgLevel1 = b1;
            bgLevel2 = b2;
            bgLevel3 = b3;
            bgsLoaded = true;
        } catch (Exception e) {
            bgsLoaded = false;
        }
    }

    private void cargarJaguar() {
        try {
            String base = "/com/icesi/sokoban/sprites/";
            Image w = new Image(getClass().getResourceAsStream(base + "block_00.jpg"));
            if (!w.isError()) {
                wallTile = w;
                wallTileLoaded = true;
            }
        } catch (Exception e) {
            wallTileLoaded = false;
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
            gc.setFill(Color.web("#0f0e17"));
            gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
            return;
        }

        // 1. Fondo del nivel
        dibujarFondo(gc);

        // 2. Tablero
        for (int row = 0; row < board.getHeight(); row++) {
            for (int col = 0; col < board.getWidth(); col++) {
                double x = col * TILE_SIZE;
                double y = row * TILE_SIZE;
                if (board.isWall(row, col))      drawWall(gc, x, y);
                else if (board.isGoal(row, col)) drawGoal(gc, x, y);
                // piso = transparente, se ve el fondo
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
    // Fondo
    // ─────────────────────────────────────────────────────────────────────

    private void dibujarFondo(GraphicsContext gc) {
        double w = canvas.getWidth();
        double h = canvas.getHeight();

        if (bgsLoaded) {
            Image bg = currentLevel == 2 ? bgLevel2
                    : currentLevel == 3 ? bgLevel3
                      : bgLevel1;
            gc.drawImage(bg, 0, 0, w, h);
        } else {
            gc.setFill(Color.web("#0f0e17"));
            gc.fillRect(0, 0, w, h);
        }
    }

    // ─────────────────────────────────────────────────────────────────────
    // Primitivas
    // ─────────────────────────────────────────────────────────────────────

    private void drawWall(GraphicsContext gc, double x, double y) {
        if (wallTileLoaded) {
            gc.drawImage(wallTile, x, y, TILE_SIZE, TILE_SIZE);
        } else {
            gc.setFill(Color.web("#4a4e69"));
            gc.fillRoundRect(x + 1, y + 1, TILE_SIZE - 2, TILE_SIZE - 2, 8, 8);
        }
    }

    private void drawGoal(GraphicsContext gc, double x, double y) {
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
            Image sprite;
            switch (lastDirection) {
                case UP:    sprite = spriteUp;    break;
                case LEFT:  sprite = spriteLeft;  break;
                case RIGHT: sprite = spriteRight; break;
                default:    sprite = spriteDown;  break;
            }
            gc.drawImage(sprite, x + 2, y + 2, TILE_SIZE - 4, TILE_SIZE - 4);
        } else {
            gc.setFill(Color.web("#f2cc8f"));
            gc.fillOval(x + 15, y + 10, 30, 30);
        }
    }
}