package com.icesi.sokoban.controller;

import com.icesi.sokoban.model.Board;
import com.icesi.sokoban.model.Direction;
import com.icesi.sokoban.model.Game;
import com.icesi.sokoban.model.GameStatus;
import com.icesi.sokoban.model.Level;
import com.icesi.sokoban.model.Player;
import com.icesi.sokoban.model.Position;
import com.icesi.sokoban.ui.GameRenderer;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * CONTROLLER — GameController
 *
 * Intermediario MVC para la pantalla de juego.
 * Traduce teclas en operaciones del modelo (Game) y
 * manda a redibujar el tablero cuando algo cambia.
 *
 * Ahora también muestra el nombre del jugador activo
 * consultando PlayerRegistry.getInstance().getActivePlayer().
 * Si no hay jugador registrado, muestra "Invitado".
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

    // ─────────────────────────────────────────────────────────────────────
    // initialize()
    // ─────────────────────────────────────────────────────────────────────
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        renderer = new GameRenderer(gameCanvas);

        // Mostrar el jugador activo — si no hay ninguno, "Invitado"
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

    /**
     * Registra el teclado sobre la Scene.
     * Se llama desde MainMenuController después de crear la Scene.
     */
    public void attachKeyHandlers(Scene scene) {
        scene.setOnKeyPressed(this::handleKey);
    }

    // ─────────────────────────────────────────────────────────────────────
    // Manejo de eventos de teclado
    // ─────────────────────────────────────────────────────────────────────
    private void handleKey(KeyEvent event) {
        Direction dir = null;
        switch (event.getCode()) {
            case UP:    case W: dir = Direction.UP;    break;
            case DOWN:  case S: dir = Direction.DOWN;  break;
            case LEFT:  case A: dir = Direction.LEFT;  break;
            case RIGHT: case D: dir = Direction.RIGHT; break;
            case R:
                game.resetLevel();
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
    // Actualización de la vista
    // ─────────────────────────────────────────────────────────────────────
    private void updateLabels() {
        moveCounterLabel.setText("Movimientos: " + game.getMoveCount());
        pushCounterLabel.setText("Empujes: " + game.getPushCount());

        if (game.getState() == GameStatus.WON) {
            statusLabel.setText("¡Nivel completado! Presiona R para reiniciar.");
        } else {
            statusLabel.setText("Flechas o WASD para mover · R para reiniciar");
        }
    }

    // ─────────────────────────────────────────────────────────────────────
    // Nivel de prueba temporal
    // TODO: reemplazar por carga desde JSON cuando se implemente la selección de nivel
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