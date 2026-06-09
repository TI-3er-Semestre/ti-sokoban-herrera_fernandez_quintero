package com.icesi.sokoban.ui;

import com.icesi.sokoban.model.Board;
import com.icesi.sokoban.model.Box;
import com.icesi.sokoban.model.Direction;
import com.icesi.sokoban.model.Game;
import com.icesi.sokoban.model.Player;
import com.icesi.sokoban.model.Position;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class GameRenderer {

    public static final int TILE_SIZE = 60;

    private final Canvas canvas;

    // Referencia al juego para redibujar en el blink
    private Game gameRef;

    // Sprites del jugador
    private Image spriteDown, spriteUp, spriteLeft, spriteRight;
    private boolean spritesLoaded = false;

    // Fondos por nivel
    private Image bgLevel1, bgLevel2, bgLevel3;
    private boolean bgsLoaded = false;

    // Tile de pared
    private Image wallTile;
    private boolean wallTileLoaded = false;

    // Sprites de cajas
    private Image boxNormal, boxOnGoal;
    private boolean boxSpritesLoaded = false;

    // Sprites de meta (titilando)
    private Image goalOff, goalOn;
    private boolean goalSpritesLoaded = false;
    private boolean goalFrame = true;
    private Timeline goalBlink;

    private int currentLevel = 1;
    private Direction lastDirection = Direction.DOWN;

    private static final Color C_BOX    = Color.web("#e07a5f");
    private static final Color C_BOX_OG = Color.web("#81b29a");
    private static final Color C_GOAL   = Color.web("#c77dff");

    private static final String BASE = "/com/icesi/sokoban/sprites/";
    private static final java.util.Map<String, String[]> SKIN_PATHS = new java.util.HashMap<>();
    static {
        SKIN_PATHS.put("Mage",  new String[]{
                BASE + "Personajes/Mage/player_down.png",
                BASE + "Personajes/Mage/player_up.png",
                BASE + "Personajes/Mage/player_left.png",
                BASE + "Personajes/Mage/player_right.png"
        });
        SKIN_PATHS.put("Ingrid", new String[]{
                BASE + "Personajes/Ingrid/Ingrid_down.png",
                BASE + "Personajes/Ingrid/Ingrid_up.png",
                BASE + "Personajes/Ingrid/Ingrid_left.png",
                BASE + "Personajes/Ingrid/Ingrid_Right.png"
        });
        SKIN_PATHS.put("Robot", new String[]{
                BASE + "Personajes/Robot/Robot_down.png",
                BASE + "Personajes/Robot/Robot_up.png",
                BASE + "Personajes/Robot/Robot_left.png",
                BASE + "Personajes/Robot/Robot_right.png"
        });
        SKIN_PATHS.put("Zorro", new String[]{
                BASE + "Personajes/Zorro/Zorro_down.png",
                BASE + "Personajes/Zorro/Zorro_up.png",
                BASE + "Personajes/Zorro/Zorro_left.png",
                BASE + "Personajes/Zorro/Zorro_right.png"
        });
    }

    public GameRenderer(Canvas canvas) {
        this.canvas = canvas;
        cargarSprites("Mage");
        cargarFondos();
        cargarJaguar();
        cargarCajas();
        cargarMetas();
    }

    // ─────────────────────────────────────────────────────────────────────
    // API pública
    // ─────────────────────────────────────────────────────────────────────

    public void setGameRef(Game game) {
        this.gameRef = game;
    }

    public void setSkin(String skinName) {
        if (skinName == null) return;
        cargarSprites(skinName);
    }

    public void setWallTile(String fileName) {
        if (fileName == null || fileName.isEmpty()) return;
        try {
            var stream = getClass().getResourceAsStream(BASE + "blocks/" + fileName);
            if (stream == null) { System.err.println("[GameRenderer] WallTile no encontrado: " + fileName); return; }
            Image w = new Image(stream);
            if (!w.isError()) { wallTile = w; wallTileLoaded = true; }
        } catch (Exception e) {
            System.err.println("[GameRenderer] Error cargando wallTile: " + e.getMessage());
        }
    }

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
    // Carga de recursos
    // ─────────────────────────────────────────────────────────────────────

    private void cargarSprites(String skinName) {
        String[] paths = SKIN_PATHS.getOrDefault(skinName, SKIN_PATHS.get("Mage"));
        try {
            Image d = new Image(getClass().getResourceAsStream(paths[0]));
            Image u = new Image(getClass().getResourceAsStream(paths[1]));
            Image l = new Image(getClass().getResourceAsStream(paths[2]));
            Image r = new Image(getClass().getResourceAsStream(paths[3]));
            if (d.isError() || u.isError() || l.isError() || r.isError()) {
                System.err.println("[GameRenderer] Sprite no cargó para skin: " + skinName);
                spritesLoaded = false;
                return;
            }
            spriteDown = d; spriteUp = u; spriteLeft = l; spriteRight = r;
            spritesLoaded = true;
        } catch (Exception e) {
            System.err.println("[GameRenderer] Error cargando skin " + skinName + ": " + e.getMessage());
            spritesLoaded = false;
        }
    }

    private void cargarFondos() {
        try {
            Image b1 = new Image(getClass().getResourceAsStream(BASE + "Level/bg_level1.jpg"));
            Image b2 = new Image(getClass().getResourceAsStream(BASE + "Level/bg_level2.png"));
            Image b3 = new Image(getClass().getResourceAsStream(BASE + "Level/bg_level3.png"));
            if (b1.isError() || b2.isError() || b3.isError()) { bgsLoaded = false; return; }
            bgLevel1 = b1; bgLevel2 = b2; bgLevel3 = b3;
            bgsLoaded = true;
        } catch (Exception e) {
            bgsLoaded = false;
        }
    }

    private void cargarJaguar() {
        try {
            Image w = new Image(getClass().getResourceAsStream(BASE + "blocks/block_00.jpg"));
            if (!w.isError()) { wallTile = w; wallTileLoaded = true; }
        } catch (Exception e) {
            wallTileLoaded = false;
        }
    }

    private void cargarCajas() {
        try {
            var sNormal = getClass().getResourceAsStream(BASE + "blocks/Wood_.png");
            var sExito  = getClass().getResourceAsStream(BASE + "blocks/Wood_Exito.png");
            if (sNormal != null && sExito != null) {
                Image n = new Image(sNormal);
                Image e = new Image(sExito);
                if (!n.isError() && !e.isError()) {
                    boxNormal = n; boxOnGoal = e;
                    boxSpritesLoaded = true;
                }
            }
        } catch (Exception e) {
            boxSpritesLoaded = false;
        }
    }

    private void cargarMetas() {
        try {
            var sOff = getClass().getResourceAsStream(BASE + "blocks/Off_ball.png");
            var sOn  = getClass().getResourceAsStream(BASE + "blocks/On_ball.png");
            if (sOff != null && sOn != null) {
                Image off = new Image(sOff);
                Image on  = new Image(sOn);
                if (!off.isError() && !on.isError()) {
                    goalOff = off; goalOn = on;
                    goalSpritesLoaded = true;
                    goalBlink = new Timeline(new KeyFrame(Duration.millis(500), e -> {
                        goalFrame = !goalFrame;
                        if (gameRef != null) render(gameRef);
                    }));
                    goalBlink.setCycleCount(Timeline.INDEFINITE);
                    goalBlink.play();
                }
            }
        } catch (Exception e) {
            goalSpritesLoaded = false;
        }
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

        dibujarFondo(gc);

        for (int row = 0; row < board.getHeight(); row++) {
            for (int col = 0; col < board.getWidth(); col++) {
                double x = col * TILE_SIZE;
                double y = row * TILE_SIZE;
                if (board.isWall(row, col))      drawWall(gc, x, y);
                else if (board.isGoal(row, col)) drawGoal(gc, x, y);
            }
        }

        for (int i = 0; i < game.getBoxes().size(); i++) {
            Box box = game.getBoxes().get(i);
            Position p = box.getPosition();
            drawBox(gc, p.getColumn() * TILE_SIZE, p.getRow() * TILE_SIZE, box.isOnGoal());
        }

        Player player = game.getPlayer();
        if (player != null && player.getPosition() != null) {
            Position p = player.getPosition();
            drawPlayer(gc, p.getColumn() * TILE_SIZE, p.getRow() * TILE_SIZE);
        }
    }

    // ─────────────────────────────────────────────────────────────────────
    // Primitivas de dibujo
    // ─────────────────────────────────────────────────────────────────────

    private void dibujarFondo(GraphicsContext gc) {
        double w = canvas.getWidth(), h = canvas.getHeight();
        if (bgsLoaded) {
            Image bg = currentLevel == 2 ? bgLevel2 : currentLevel == 3 ? bgLevel3 : bgLevel1;
            gc.drawImage(bg, 0, 0, w, h);
        } else {
            gc.setFill(Color.web("#0f0e17"));
            gc.fillRect(0, 0, w, h);
        }
    }

    private void drawWall(GraphicsContext gc, double x, double y) {
        if (wallTileLoaded) {
            gc.drawImage(wallTile, x, y, TILE_SIZE, TILE_SIZE);
        } else {
            gc.setFill(Color.web("#4a4e69"));
            gc.fillRoundRect(x + 1, y + 1, TILE_SIZE - 2, TILE_SIZE - 2, 8, 8);
        }
    }

    private void drawGoal(GraphicsContext gc, double x, double y) {
        if (goalSpritesLoaded) {
            gc.drawImage(goalFrame ? goalOn : goalOff, x, y, TILE_SIZE, TILE_SIZE);
        } else {
            double cx = x + TILE_SIZE / 2.0, cy = y + TILE_SIZE / 2.0, r = TILE_SIZE * 0.28;
            gc.setFill(C_GOAL);
            gc.fillOval(cx - r, cy - r, r * 2, r * 2);
            gc.setStroke(Color.web("#ffffff", 0.3));
            gc.setLineWidth(1);
            gc.strokeOval(cx - r, cy - r, r * 2, r * 2);
        }
    }

    private void drawBox(GraphicsContext gc, double x, double y, boolean onGoal) {
        if (boxSpritesLoaded) {
            gc.drawImage(onGoal ? boxOnGoal : boxNormal, x, y, TILE_SIZE, TILE_SIZE);
        } else {
            double m = 5;
            gc.setFill(onGoal ? C_BOX_OG : C_BOX);
            gc.fillRoundRect(x + m, y + m, TILE_SIZE - m * 2, TILE_SIZE - m * 2, 10, 10);
            gc.setStroke(onGoal ? Color.web("#4a7a5a") : Color.web("#8a3a2a"));
            gc.setLineWidth(2);
            gc.strokeRoundRect(x + m, y + m, TILE_SIZE - m * 2, TILE_SIZE - m * 2, 10, 10);
        }
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