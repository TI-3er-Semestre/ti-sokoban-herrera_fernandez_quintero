package com.icesi.sokoban.controller;

import com.icesi.sokoban.model.Board;
import com.icesi.sokoban.model.Direction;
import com.icesi.sokoban.model.Game;
import com.icesi.sokoban.model.GameStatus;
import com.icesi.sokoban.model.Level;
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
 * Es el intermediario del patron MVC para la pantalla de juego.
 * Tiene dos responsabilidades:
 *
 *   1. Controlador FXML: el FXMLLoader lo instancia, le inyecta los
 *      nodos @FXML desde game.fxml y llama a initialize().
 *
 *   2. Controlador del juego: traduce las teclas en operaciones del
 *      modelo (Game) y manda a redibujar el tablero cuando algo cambia.
 *
 * Reglas:
 *   - El modelo (Game) no sabe nada de JavaFX.
 *   - El archivo game.fxml no tiene codigo Java.
 *   - Esta clase es la unica que toca ambas capas.
 *
 * ESTADO ACTUAL (para el equipo):
 *   Este controlador ya conecta teclado -> Game -> render.
 *   PENDIENTE por implementar el equipo:
 *     - Cargar niveles reales desde JSON (hoy usa un nivel de prueba).
 *     - Boton de deshacer enlazado al metodo undo() del Game.
 *     - Mostrar tiempo transcurrido en vivo.
 *     - Pantalla de victoria.
 */
public class GameController implements Initializable {

    // ── Nodos inyectados desde game.fxml (coinciden por fx:id) ────────────
    @FXML private Canvas gameCanvas;
    @FXML private Label  moveCounterLabel;
    @FXML private Label  pushCounterLabel;
    @FXML private Label  statusLabel;
    @FXML private Button undoButton;

    // ── Modelo y renderizador ─────────────────────────────────────────────
    private final Game game = new Game();
    private GameRenderer renderer;

    // ─────────────────────────────────────────────────────────────────────
    // initialize() — lo llama el FXMLLoader despues de inyectar los @FXML
    // ─────────────────────────────────────────────────────────────────────
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        renderer = new GameRenderer(gameCanvas);

        // TODO equipo: reemplazar este nivel de prueba por carga desde JSON.
        Level testLevel = buildTestLevel();
        game.loadLevel(testLevel);

        renderer.resizeToBoard(game.getBoard());
        renderer.render(game);
        updateLabels();
    }

    /**
     * Registra el manejo del teclado sobre la Scene.
     * Se llama desde SokobanApplication despues de crear la Scene,
     * porque la Scene no existe cuando initialize() se ejecuta.
     */
    public void attachKeyHandlers(Scene scene) {
        scene.setOnKeyPressed(this::handleKey);
    }

    // ─────────────────────────────────────────────────────────────────────
    // Manejo de eventos
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

        // Usa el buffer de entrada (cola FIFO) del modelo
        game.queueCommand(dir);
        game.processInputBuffer();

        renderer.render(game);
        updateLabels();
        event.consume();
    }

    /**
     * Accion del boton "Deshacer". Enlazado en game.fxml con onAction.
     */
    @FXML
    private void onUndoClicked() {
        if (game.undo()) {
            renderer.render(game);
            updateLabels();
        }
    }

    // ─────────────────────────────────────────────────────────────────────
    // Actualizacion de la vista
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
    // TODO equipo: borrar esto cuando Level.loadFromJson() este implementado.
    // ─────────────────────────────────────────────────────────────────────
    private Level buildTestLevel() {
        Board board = new Board(7, 5);
        // Perimetro de muros
        for (int c = 0; c < 7; c++) {
            board.setCell(0, c, '#');
            board.setCell(4, c, '#');
        }
        for (int r = 0; r < 5; r++) {
            board.setCell(r, 0, '#');
            board.setCell(r, 6, '#');
        }
        // Una caja y una meta
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
