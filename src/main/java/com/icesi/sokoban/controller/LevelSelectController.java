package com.icesi.sokoban.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

/**
 * CONTROLLER — LevelSelectController
 *
 * Pantalla intermedia entre el menú principal y el juego.
 * Permite elegir entre los 3 niveles reales o uno aleatorio.
 *
 * Patrón de paso de datos al GameController:
 *   - Se guarda el número de nivel elegido en GameController.selectedLevel
 *     (campo estático) antes de navegar.
 *   - GameController lo lee en initialize() para cargar el JSON correcto.
 *
 * Por qué estático: GameController se instancia automáticamente por
 * FXMLLoader — no podemos pasarle datos por constructor. La alternativa
 * al campo estático es usar loader.getController() después de cargar,
 * lo que hacemos aquí para pasar el nivel antes de mostrar la escena.
 */
public class LevelSelectController implements Initializable {

    @FXML private Button volverButton;

    private static final Random RANDOM = new Random();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Sin estado que inicializar
    }

    // ─────────────────────────────────────────────────────────────────────
    // Selección de niveles
    // ─────────────────────────────────────────────────────────────────────

    @FXML
    private void onNivel1Clicked() throws IOException {
        navegarAlJuego(1);
    }

    @FXML
    private void onNivel2Clicked() throws IOException {
        navegarAlJuego(2);
    }

    @FXML
    private void onNivel3Clicked() throws IOException {
        navegarAlJuego(3);
    }

    @FXML
    private void onAleatorioClicked() throws IOException {
        // Elige aleatoriamente entre nivel 1, 2 o 3
        int nivelAleatorio = RANDOM.nextInt(3) + 1;
        navegarAlJuego(nivelAleatorio);
    }

    @FXML
    private void onVolverClicked() throws IOException {
        Parent root = FXMLLoader.load(
                getClass().getResource("/com/icesi/sokoban/view/main-menu.fxml"));
        Stage stage = (Stage) volverButton.getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    // ─────────────────────────────────────────────────────────────────────
    // Navegación al juego
    // ─────────────────────────────────────────────────────────────────────

    /**
     * Carga game.fxml, le inyecta el nivel elegido al GameController
     * usando loader.getController(), y reemplaza la escena.
     *
     * @param levelNumber número del nivel (1, 2 o 3)
     */
    private void navegarAlJuego(int levelNumber) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/icesi/sokoban/view/game.fxml"));
        Parent root = loader.load();

        // Inyectar el nivel ANTES de que el usuario vea la pantalla
        GameController gameController = loader.getController();
        gameController.setLevelNumber(levelNumber);

        Scene scene = new Scene(root);
        gameController.attachKeyHandlers(scene);

        Stage stage = (Stage) volverButton.getScene().getWindow();
        stage.setScene(scene);
    }
}