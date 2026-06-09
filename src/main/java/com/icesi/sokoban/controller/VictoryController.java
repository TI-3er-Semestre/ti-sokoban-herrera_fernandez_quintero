package com.icesi.sokoban.controller;

import com.icesi.sokoban.model.Stats;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * CONTROLLER — VictoryController
 *
 * Muestra los resultados de la partida ganada.
 * Si hay un nivel siguiente (nivel < 3), muestra el botón
 * "Siguiente Nivel" que carga directamente el nivel n+1.
 *
 * Transición entre niveles:
 *   Nivel 1 completado → botón "Siguiente Nivel" → carga nivel 2
 *   Nivel 2 completado → botón "Siguiente Nivel" → carga nivel 3
 *   Nivel 3 completado → no hay botón (es el último nivel)
 */
public class VictoryController implements Initializable {

    @FXML private Label  playerNameLabel;
    @FXML private Label  movimientosLabel;
    @FXML private Label  empujesLabel;
    @FXML private Label  tiempoLabel;
    @FXML private Label  puntajeLabel;
    @FXML private Button siguienteButton;
    @FXML private Button rankingButton;
    @FXML private Button menuButton;

    private int levelCompleted = 1;
    private static final int MAX_LEVEL = 3;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Los datos llegan por setStats()
    }

    /**
     * Inyecta los Stats y configura la vista.
     * Si el nivel completado no es el último, muestra el botón siguiente.
     */
    public void setStats(Stats stats) {
        if (stats == null) return;

        this.levelCompleted = stats.getLevel();

        playerNameLabel.setText("¡Bien hecho, " + stats.getPlayerName() + "!");
        movimientosLabel.setText(String.valueOf(stats.getMovements()));
        empujesLabel.setText(String.valueOf(stats.getPushes()));
        tiempoLabel.setText(formatTime(stats.getTime()));
        puntajeLabel.setText(String.format("Puntaje: %.1f", stats.getScore()));

        // Mostrar botón siguiente solo si hay un nivel siguiente
        if (levelCompleted < MAX_LEVEL) {
            siguienteButton.setVisible(true);
            siguienteButton.setManaged(true);
            siguienteButton.setText("Siguiente Nivel → (Nivel " + (levelCompleted + 1) + ")");
        }
    }

    // ─────────────────────────────────────────────────────────────────────
    // Navegación
    // ─────────────────────────────────────────────────────────────────────

    /**
     * Carga el siguiente nivel directamente sin pasar por la selección.
     */
    @FXML
    private void onSiguienteNivelClicked() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/icesi/sokoban/view/game.fxml"));
        Parent root = loader.load();

        GameController gameController = loader.getController();
        gameController.setLevelNumber(levelCompleted + 1);

        Scene scene = new Scene(root);
        gameController.attachKeyHandlers(scene);

        Stage stage = (Stage) siguienteButton.getScene().getWindow();
        stage.setScene(scene);
    }

    @FXML
    private void onVerRankingClicked() throws IOException {
        Parent root = FXMLLoader.load(
                getClass().getResource("/com/icesi/sokoban/view/ranking.fxml"));
        Stage stage = (Stage) rankingButton.getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    @FXML
    private void onMenuClicked() throws IOException {
        Parent root = FXMLLoader.load(
                getClass().getResource("/com/icesi/sokoban/view/main-menu.fxml"));
        Stage stage = (Stage) menuButton.getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    private String formatTime(int seconds) {
        return String.format("%d:%02d", seconds / 60, seconds % 60);
    }
}