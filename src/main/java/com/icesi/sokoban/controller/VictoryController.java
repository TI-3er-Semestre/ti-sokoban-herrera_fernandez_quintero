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
 * Recibe un objeto Stats via setStats() — GameController
 * lo llama justo antes de navegar a esta pantalla.
 *
 * Patrón de paso de datos entre pantallas:
 *   1. GameController carga el FXML con FXMLLoader
 *   2. Obtiene este controlador con loader.getController()
 *   3. Llama controller.setStats(stats) para inyectar los datos
 *   4. Reemplaza la Scene
 *
 * initialize() se ejecuta ANTES de setStats(), por eso los labels
 * se actualizan en setStats() y no en initialize().
 */
public class VictoryController implements Initializable {

    @FXML private Label  playerNameLabel;
    @FXML private Label  movimientosLabel;
    @FXML private Label  empujesLabel;
    @FXML private Label  tiempoLabel;
    @FXML private Label  puntajeLabel;
    @FXML private Button rankingButton;
    @FXML private Button menuButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Los datos llegan por setStats() — no hay nada que inicializar aquí.
    }

    /**
     * Inyecta los Stats de la partida y actualiza los labels.
     * GameController llama este método después de loader.getController().
     */
    public void setStats(Stats stats) {
        if (stats == null) return;

        playerNameLabel.setText("¡Bien hecho, " + stats.getPlayerName() + "!");
        movimientosLabel.setText(String.valueOf(stats.getMovements()));
        empujesLabel.setText(String.valueOf(stats.getPushes()));
        tiempoLabel.setText(formatTime(stats.getTime()));
        puntajeLabel.setText(String.format("Puntaje: %.1f", stats.getScore()));
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
        int m = seconds / 60;
        int s = seconds % 60;
        return String.format("%d:%02d", m, s);
    }
}