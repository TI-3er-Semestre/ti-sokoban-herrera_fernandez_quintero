package com.icesi.sokoban.controller;

import com.icesi.sokoban.model.Board;
import com.icesi.sokoban.model.Direction;
import com.icesi.sokoban.model.Game;
import com.icesi.sokoban.model.GameStatus;
import com.icesi.sokoban.model.Level;
import com.icesi.sokoban.model.Player;
import com.icesi.sokoban.model.Position;
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
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

/**
 * CONTROLLER — GameController
 *
 * Traduce teclas en operaciones del modelo (Game) y
 * redibuja el tablero cuando algo cambia.
 *
 * Al detectar GameStatus.WON:
 *   1. Construye un objeto Stats con los datos de la partida.
 *   2. Llama RankingController.registrarPartida(stats) para guardarlo en el BST.
 *   3. Navega a victory.fxml pasándole los Stats al VictoryController.
 */
public class GameController implements Initializable {

    // ── Nodos inyectados desde game.fxml ──────────────────────────────────
    @FXML private Canvas gameCanvas;
    @FXML private Label  playerLabel;
    @FXML private Label  moveCounterLabel;
    @FXML private Label  pushCounterLabel;
    @FXML private Label  statusLabel;
    @FXML private Button undoButton;

    // ── Modelo y renderizador ─────────────────────────────────────────────
    private final Game game = new Game();
    private GameRenderer renderer;

    // Para no navegar a victoria múltiples veces si se sigue presionando teclas
    private boolean victoryHandled = false;

    // ─────────────────────────────────────────────────────────────────────
    // initialize()
    // ─────────────────────────────────────────────────────────────────────
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        renderer = new GameRenderer(gameCanvas);

        // Mostrar el jugador activo
        Player activePlayer = PlayerRegistry.getInstance().getActivePlayer();
        if (activePlayer != null) {
            playerLabel.setText("👤 " + activePlayer.getUsername());
        } else {
            playerLabel.setText("👤 Invitado");
        }

        // TODO: reemplazar por carga desde JSON
        Level testLevel = buildTestLevel();
        game.loadLevel(testLevel);

        renderer.resizeToBoard(game.getBoard());
        renderer.render(game);
        updateLabels();
    }

    public void attachKeyHandlers(Scene scene) {
        scene.setOnKeyPressed(this::handleKey);
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
            default:
                return;
        }

        game.queueCommand(dir);
        game.processInputBuffer();
        renderer.render(game);
        updateLabels();

        // Detectar victoria después de cada movimiento
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
    // Navegar a pantalla de victoria
    // ─────────────────────────────────────────────────────────────────────
    private void navegarAVictoria() {
        try {
            // 1. Obtener datos del jugador activo
            Player activePlayer = PlayerRegistry.getInstance().getActivePlayer();
            String playerId   = activePlayer != null ? activePlayer.getUsername() : "invitado";
            String playerName = activePlayer != null ? activePlayer.getUsername() : "Invitado";

            // 2. Construir Stats con los datos de la partida
            Stats stats = new Stats(
                    playerId,
                    playerName,
                    1,                               // nivel (por ahora fijo, cambia con JSON)
                    game.getMoveCount(),
                    game.getPushCount(),
                    (int) game.getElapsedTime(),
                    true,                            // completed = true porque ganó
                    LocalDate.now().toString()
            );

            // 3. Guardar en el BST de ranking
            RankingController.registrarPartida(stats);

            // 4. Cargar victory.fxml e inyectar los Stats
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/icesi/sokoban/view/victory.fxml"));
            javafx.scene.Parent root = loader.load();

            VictoryController victoryController = loader.getController();
            victoryController.setStats(stats);

            // 5. Navegar
            Stage stage = (Stage) gameCanvas.getScene().getWindow();
            stage.setScene(new Scene(root));

        } catch (IOException e) {
            // Si falla la navegación, al menos muestra el mensaje en el label
            statusLabel.setText("¡Nivel completado! " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────────────────────────────────
    // Actualización de labels
    // ─────────────────────────────────────────────────────────────────────
    private void updateLabels() {
        moveCounterLabel.setText("Movimientos: " + game.getMoveCount());
        pushCounterLabel.setText("Empujes: " + game.getPushCount());

        if (game.getState() == GameStatus.WON) {
            statusLabel.setText("¡Nivel completado!");
        } else {
            statusLabel.setText("Flechas o WASD para mover · R para reiniciar");
        }
    }

    // ─────────────────────────────────────────────────────────────────────
    // Nivel de prueba temporal
    // TODO: reemplazar por carga desde JSON
    // ─────────────────────────────────────────────────────────────────────
    private Level buildTestLevel() {
        Board board = new Board(7, 5);
        for (int c = 0; c < 7; c++) {
            board.setCell(0, c, '#');
            board.setCell(4, c, '#');
        }
        for (int r = 0; r < 5; r++) {
            board.setCell(r, 0, '#');
            board.setCell(r, 6, '#');
        }
        board.setCell(2, 2, '$');
        board.setCell(2, 4, '.');
        board.addGoal(new Position(2, 4));

        Level level = new Level(0, "Nivel de prueba");
        level.setBoard(board);
        level.setPlayerStartPosition(new Position(2, 1));
        level.setDifficulty("BASIC");
        return level;
    }
}
