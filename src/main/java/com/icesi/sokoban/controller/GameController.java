package com.icesi.sokoban.controller;

import com.icesi.sokoban.model.Direction;
import com.icesi.sokoban.model.Game;
import com.icesi.sokoban.model.GameStatus;
import com.icesi.sokoban.model.Level;
import com.icesi.sokoban.model.Player;
import com.icesi.sokoban.model.Stats;
import com.icesi.sokoban.ui.GameRenderer;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ResourceBundle;

/**
 * CONTROLLER — GameController
 *
 * Carga el nivel desde JSON usando Level.loadFromJson().
 * El número de nivel (1, 2 o 3) lo recibe de LevelSelectController
 * a través de setLevelNumber() antes de que initialize() corra.
 *
 * Flujo de carga:
 *   1. LevelSelectController llama setLevelNumber(n)
 *   2. initialize() lee levelNumber y llama cargarNivelDesdeJson(n)
 *   3. cargarNivelDesdeJson() lee el archivo JSON desde resources
 *      y llama level.loadFromJson(jsonString)
 *   4. Si falla la carga JSON, cae al nivel de prueba como fallback
 */
public class GameController implements Initializable {

    // ── Nodos inyectados desde game.fxml ──────────────────────────────────
    @FXML private Canvas gameCanvas;
    @FXML private Label  playerLabel;
    @FXML private Label  moveCounterLabel;
    @FXML private Label  pushCounterLabel;
    @FXML private Label  statusLabel;
    @FXML private Button undoButton;

    // ── Modelo ────────────────────────────────────────────────────────────
    private final Game game = new Game();
    private GameRenderer renderer;
    private boolean victoryHandled = false;

    // Nivel a cargar — LevelSelectController lo inyecta antes de initialize()
    private int levelNumber = 1;

    // ─────────────────────────────────────────────────────────────────────
    // API para LevelSelectController
    // ─────────────────────────────────────────────────────────────────────

    /**
     * Recibe el número de nivel elegido en la pantalla de selección.
     * Debe llamarse ANTES de que JavaFX llame initialize().
     * En la práctica, FXMLLoader llama initialize() al final de load(),
     * así que LevelSelectController hace: loader.load() → getController()
     * → setLevelNumber() → ya es tarde.
     *
     * Por eso initialize() usa levelNumber con valor por defecto 1,
     * y setLevelNumber() recarga el nivel si ya está inicializado.
     */
    public void setLevelNumber(int levelNumber) {
        this.levelNumber = levelNumber;
        // Recargar el nivel con el nuevo número
        Level level = cargarNivelDesdeJson(levelNumber);
        game.loadLevel(level);
        if (renderer != null) {
            renderer.resizeToBoard(game.getBoard());
            renderer.render(game);
            updateLabels();
        }
    }

    // ─────────────────────────────────────────────────────────────────────
    // initialize()
    // ─────────────────────────────────────────────────────────────────────
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        renderer = new GameRenderer(gameCanvas);

        // Mostrar jugador activo
        Player activePlayer = PlayerRegistry.getInstance().getActivePlayer();
        if (activePlayer != null) {
            playerLabel.setText("👤 " + activePlayer.getUsername());
        } else {
            playerLabel.setText("👤 Invitado");
        }

        // Cargar nivel desde JSON (levelNumber viene de setLevelNumber o default 1)
        Level level = cargarNivelDesdeJson(levelNumber);
        game.loadLevel(level);

        renderer.resizeToBoard(game.getBoard());
        renderer.render(game);
        updateLabels();
    }

    public void attachKeyHandlers(Scene scene) {
        scene.setOnKeyPressed(this::handleKey);
    }

    // ─────────────────────────────────────────────────────────────────────
    // Carga de niveles desde JSON
    // ─────────────────────────────────────────────────────────────────────

    /**
     * Lee el archivo JSON del nivel desde resources y construye el Level.
     * Si la lectura falla, retorna el nivel de prueba como fallback.
     *
     * Los archivos viven en:
     *   src/main/resources/com/icesi/sokoban/levels/level_N.json
     */
    private Level cargarNivelDesdeJson(int n) {
        String path = "/com/icesi/sokoban/levels/level_" + n + ".json";
        try (InputStream is = getClass().getResourceAsStream(path)) {
            if (is == null) {
                System.err.println("No se encontró el archivo: " + path);
                return buildFallbackLevel();
            }
            String json = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            Level level = new Level(n, "Nivel " + n);
            level.loadFromJson(json);
            return level;
        } catch (Exception e) {
            System.err.println("Error cargando nivel " + n + ": " + e.getMessage());
            return buildFallbackLevel();
        }
    }

    // ─────────────────────────────────────────────────────────────────────
    // Manejo de teclado
    // ─────────────────────────────────────────────────────────────────────
    private void handleKey(KeyEvent event) {
        if (victoryHandled) return;

        Direction dir = null;
        switch (event.getCode()) {
            case UP:    case W: dir = Direction.UP;    break;
            case DOWN:  case S: dir = Direction.DOWN;  break;
            case LEFT:  case A: dir = Direction.LEFT;  break;
            case RIGHT: case D: dir = Direction.RIGHT; break;
            case R:
                game.resetLevel();
                victoryHandled = false;
                renderer.render(game);
                updateLabels();
                event.consume();
                return;
            default: return;
        }

        game.queueCommand(dir);
        game.processInputBuffer();
        renderer.render(game);
        updateLabels();

        if (game.getState() == GameStatus.WON && !victoryHandled) {
            victoryHandled = true;
            navegarAVictoria();
        }
        event.consume();
    }

    @FXML
    private void onUndoClicked() {
        if (game.undo()) {
            renderer.render(game);
            updateLabels();
        }
    }

    // ─────────────────────────────────────────────────────────────────────
    // Victoria
    // ─────────────────────────────────────────────────────────────────────
    private void navegarAVictoria() {
        try {
            Player activePlayer = PlayerRegistry.getInstance().getActivePlayer();
            String playerId   = activePlayer != null ? activePlayer.getUsername() : "invitado";
            String playerName = activePlayer != null ? activePlayer.getUsername() : "Invitado";

            Stats stats = new Stats(
                    playerId, playerName, levelNumber,
                    game.getMoveCount(), game.getPushCount(),
                    (int) game.getElapsedTime(), true,
                    LocalDate.now().toString()
            );

            RankingController.registrarPartida(stats);

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/icesi/sokoban/view/victory.fxml"));
            javafx.scene.Parent root = loader.load();
            VictoryController vc = loader.getController();
            vc.setStats(stats);

            Stage stage = (Stage) gameCanvas.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            statusLabel.setText("¡Nivel completado!");
        }
    }

    // ─────────────────────────────────────────────────────────────────────
    // Labels
    // ─────────────────────────────────────────────────────────────────────
    private void updateLabels() {
        moveCounterLabel.setText("Movimientos: " + game.getMoveCount());
        pushCounterLabel.setText("Empujes: " + game.getPushCount());
        statusLabel.setText(game.getState() == GameStatus.WON
                ? "¡Nivel completado!"
                : "Flechas o WASD para mover · R para reiniciar");
    }

    // ─────────────────────────────────────────────────────────────────────
    // Fallback — nivel mínimo si falla la carga JSON
    // ─────────────────────────────────────────────────────────────────────
    private Level buildFallbackLevel() {
        com.icesi.sokoban.model.Board board =
                new com.icesi.sokoban.model.Board(7, 5);
        for (int c = 0; c < 7; c++) {
            board.setCell(0, c, '#'); board.setCell(4, c, '#');
        }
        for (int r = 0; r < 5; r++) {
            board.setCell(r, 0, '#'); board.setCell(r, 6, '#');
        }
        board.setCell(2, 2, '$');
        board.setCell(2, 4, '.');
        board.addGoal(new com.icesi.sokoban.model.Position(2, 4));
        Level level = new Level(0, "Fallback");
        level.setBoard(board);
        level.setPlayerStartPosition(new com.icesi.sokoban.model.Position(2, 1));
        level.setDifficulty("BASIC");
        return level;
    }
}
