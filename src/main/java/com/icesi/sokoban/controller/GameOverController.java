package com.icesi.sokoban.controller;

import com.icesi.sokoban.model.Player;

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
 * CONTROLLER — GameOverController
 *
 * Muestra el motivo del game over y permite reintentar o volver al menú.
 * Recibe el motivo y el nivel via setMotivo() y setLevelNumber()
 * antes de que se muestre la pantalla.
 */
public class GameOverController implements Initializable {

    @FXML private Label  motivoLabel;
    @FXML private Label  nivelLabel;
    @FXML private Label  jugadorLabel;
    @FXML private Button reintentarButton;
    @FXML private Button menuButton;

    private int levelNumber = 1;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Los datos llegan por setMotivo() y setLevelNumber()
    }

    /**
     * Inyecta el motivo del game over y datos del contexto.
     * Llamado por GameController después de loader.getController().
     */
    public void setMotivo(String motivo, int levelNumber) {
        this.levelNumber = levelNumber;

        motivoLabel.setText(motivo);
        nivelLabel.setText("Nivel " + levelNumber);

        Player activePlayer = PlayerRegistry.getInstance().getActivePlayer();
        jugadorLabel.setText("👤 " + (activePlayer != null
                ? activePlayer.getUsername() : "Invitado"));
    }

    // ─────────────────────────────────────────────────────────────────────
    // Navegación
    // ─────────────────────────────────────────────────────────────────────

    /**
     * Reintentar — vuelve al juego con el mismo nivel.
     * Carga game.fxml y le pasa el mismo levelNumber.
     */
    @FXML
    private void onReintentarClicked() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/icesi/sokoban/view/game.fxml"));
        Parent root = loader.load();

        GameController gameController = loader.getController();
        gameController.setLevelNumber(levelNumber);

        Scene scene = new Scene(root);
        gameController.attachKeyHandlers(scene);

        Stage stage = (Stage) reintentarButton.getScene().getWindow();
        stage.setScene(scene);
    }

    @FXML
    private void onMenuClicked() throws IOException {
        Parent root = FXMLLoader.load(
                getClass().getResource("/com/icesi/sokoban/view/main-menu.fxml"));
        Stage stage = (Stage) menuButton.getScene().getWindow();
        stage.setScene(new Scene(root));
    }
}