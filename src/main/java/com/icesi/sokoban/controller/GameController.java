package com.icesi.sokoban.controller;

import com.icesi.sokoban.model.Direction;
import com.icesi.sokoban.model.Game;
import com.icesi.sokoban.model.GameStatus;
import com.icesi.sokoban.model.Level;
import com.icesi.sokoban.model.Player;
import com.icesi.sokoban.model.Stats;
import com.icesi.sokoban.ui.GameRenderer;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    @FXML private Canvas gameCanvas;
    @FXML private Label  playerLabel;
    @FXML private Label  moveCounterLabel;
    @FXML private Label  pushCounterLabel;
    @FXML private Label  timerLabel;
    @FXML private Label  statusLabel;
    @FXML private Button undoButton;

    private final Game game = new Game();
    private GameRenderer renderer;
    private boolean gameEnded = false;
    private int levelNumber = 1;
    private Level currentLevel;

    private Timeline cronometro;
    private int segundos = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        renderer = new GameRenderer(gameCanvas);

        Player activePlayer = PlayerRegistry.getInstance().getActivePlayer();
        playerLabel.setText(activePlayer != null
                ? "👤 " + activePlayer.getUsername() : "👤 Invitado");

        currentLevel = cargarNivelDesdeJson(levelNumber);
        game.loadLevel(currentLevel);

        renderer.resizeToBoard(game.getBoard());
        renderer.render(game);
        updateLabels();
        iniciarCronometro();
    }

    // ─────────────────────────────────────────────────────────────────────
    // Cronómetro
    // ─────────────────────────────────────────────────────────────────────
    private void iniciarCronometro() {
        segundos = 0;
        cronometro = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            segundos++;
            timerLabel.setText("⏱ " + formatTime(segundos));
            int timeLimit = currentLevel != null ? currentLevel.getTimeLimit() : 0;
            if (timeLimit > 0 && segundos >= timeLimit && !gameEnded) {
                gameEnded = true;
                detenerCronometro();
                navegarAGameOver("⏰ ¡Se agotó el tiempo!");
            }
        }));
        cronometro.setCycleCount(Timeline.INDEFINITE);
        cronometro.play();
    }

    private void detenerCronometro() {
        if (cronometro != null) cronometro.stop();
    }

    private String formatTime(int s) {
        return String.format("%d:%02d", s / 60, s % 60);
    }

    // ─────────────────────────────────────────────────────────────────────
    // API pública
    // ─────────────────────────────────────────────────────────────────────
    public void setLevelNumber(int levelNumber) {
        this.levelNumber = levelNumber;
        this.gameEnded = false;
        currentLevel = cargarNivelDesdeJson(levelNumber);
        game.loadLevel(currentLevel);
        detenerCronometro();
        iniciarCronometro();
        if (renderer != null) {
            renderer.resizeToBoard(game.getBoard());
            renderer.render(game);
            updateLabels();
        }
    }

    public void attachKeyHandlers(Scene scene) {
        scene.setOnKeyPressed(this::handleKey);
    }

    // ─────────────────────────────────────────────────────────────────────
    // Teclado — actualiza sprite según dirección
    // ─────────────────────────────────────────────────────────────────────
    private void handleKey(KeyEvent event) {
        if (gameEnded) return;

        Direction dir = null;
        switch (event.getCode()) {
            case UP:    case W: dir = Direction.UP;    break;
            case DOWN:  case S: dir = Direction.DOWN;  break;
            case LEFT:  case A: dir = Direction.LEFT;  break;
            case RIGHT: case D: dir = Direction.RIGHT; break;
            case R:
                game.resetLevel();
                gameEnded = false;
                detenerCronometro();
                iniciarCronometro();
                renderer.render(game);
                updateLabels();
                event.consume();
                return;
            default: return;
        }

        // Actualizar sprite ANTES de renderizar
        renderer.setLastDirection(dir);

        game.queueCommand(dir);
        game.processInputBuffer();
        renderer.render(game);
        updateLabels();

        if (game.getState() == GameStatus.WON && !gameEnded) {
            gameEnded = true;
            detenerCronometro();
            navegarAVictoria();
        } else if (!gameEnded && hayDeadlock()) {
            gameEnded = true;
            detenerCronometro();
            navegarAGameOver("📦 ¡Una caja quedó bloqueada sin salida!");
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
    // Carga JSON
    // ─────────────────────────────────────────────────────────────────────
    private Level cargarNivelDesdeJson(int n) {
        String path = "/com/icesi/sokoban/levels/level_" + n + ".json";
        try (InputStream is = getClass().getResourceAsStream(path)) {
            if (is == null) return buildFallbackLevel();
            String json = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            Level level = new Level(n, "Nivel " + n);
            level.loadFromJson(json);
            return level;
        } catch (Exception e) {
            return buildFallbackLevel();
        }
    }

    // ─────────────────────────────────────────────────────────────────────
    // Deadlock
    // ─────────────────────────────────────────────────────────────────────
    private boolean hayDeadlock() {
        com.icesi.sokoban.model.Board board = game.getBoard();
        com.icesi.sokoban.structure.CustomLinkedList<com.icesi.sokoban.model.Box> cajas = game.getBoxes();

        for (int i = 0; i < cajas.size(); i++) {
            com.icesi.sokoban.model.Box caja = cajas.get(i);
            int r = caja.getPosition().getRow();
            int c = caja.getPosition().getColumn();
            if (board.isGoal(r, c)) continue;
            boolean bloqueadoV = esBloqueado(board, r-1, c) || esBloqueado(board, r+1, c);
            boolean bloqueadoH = esBloqueado(board, r, c-1) || esBloqueado(board, r, c+1);
            if (bloqueadoV && bloqueadoH) return true;
        }
        return false;
    }

    private boolean esBloqueado(com.icesi.sokoban.model.Board board, int r, int c) {
        if (r < 0 || r >= board.getHeight() || c < 0 || c >= board.getWidth()) return true;
        return board.getCell(r, c) == '#';
    }

    // ─────────────────────────────────────────────────────────────────────
    // Navegación
    // ─────────────────────────────────────────────────────────────────────
    private void navegarAVictoria() {
        try {
            Player activePlayer = PlayerRegistry.getInstance().getActivePlayer();
            String playerId   = activePlayer != null ? activePlayer.getUsername() : "invitado";
            String playerName = activePlayer != null ? activePlayer.getUsername() : "Invitado";

            Stats stats = new Stats(playerId, playerName, levelNumber,
                    game.getMoveCount(), game.getPushCount(),
                    segundos, true, LocalDate.now().toString());

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

    private void navegarAGameOver(String motivo) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/icesi/sokoban/view/gameover.fxml"));
            javafx.scene.Parent root = loader.load();
            GameOverController gc = loader.getController();
            gc.setMotivo(motivo, levelNumber);
            Stage stage = (Stage) gameCanvas.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            statusLabel.setText("Game Over: " + motivo);
        }
    }

    // ─────────────────────────────────────────────────────────────────────
    // Labels
    // ─────────────────────────────────────────────────────────────────────
    private void updateLabels() {
        moveCounterLabel.setText("👣 Mov: " + game.getMoveCount());
        pushCounterLabel.setText("📦 Emp: " + game.getPushCount());
        statusLabel.setText(game.getState() == GameStatus.WON
                ? "¡Nivel completado!"
                : "Flechas o WASD para mover · R para reiniciar");
    }

    // ─────────────────────────────────────────────────────────────────────
    // Fallback
    // ─────────────────────────────────────────────────────────────────────
    private Level buildFallbackLevel() {
        com.icesi.sokoban.model.Board board = new com.icesi.sokoban.model.Board(7, 5);
        for (int c = 0; c < 7; c++) { board.setCell(0, c, '#'); board.setCell(4, c, '#'); }
        for (int r = 0; r < 5; r++) { board.setCell(r, 0, '#'); board.setCell(r, 6, '#'); }
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