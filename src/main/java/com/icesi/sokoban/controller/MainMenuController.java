package com.icesi.sokoban.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * CONTROLLER — MainMenuController
 *
 * Controlador FXML para la pantalla de menú principal.
 * Su única responsabilidad es navegar a otras pantallas
 * cuando el usuario presiona un botón.
 *
 * Patrón de navegación usado en todo el proyecto:
 *   1. Cargar el FXML de destino con FXMLLoader.
 *   2. Obtener el Stage actual desde cualquier nodo en escena.
 *   3. Reemplazar la Scene del Stage con la nueva.
 *
 * El Stage es la ventana. La Scene es lo que se muestra dentro.
 * Cambiar la Scene es equivalente a "ir a otra pantalla".
 */
public class MainMenuController implements Initializable {

    // Cualquier botón sirve para obtener el Stage — usamos el de Jugar.
    @FXML private Button jugarButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // No hay estado que inicializar en el menú principal.
    }

    // ─────────────────────────────────────────────────────────────────────
    // Manejadores de botones — uno por cada opción del menú
    // ─────────────────────────────────────────────────────────────────────

    /**
     * Navega a la pantalla del juego (game.fxml).
     * Después de cargar el FXML, conecta el teclado a la nueva Scene
     * igual que lo hace SokobanApplication al arrancar.
     */
    @FXML
    private void onJugarClicked() throws IOException {
        // Navega a la pantalla de selección de nivel.
        // LevelSelectController se encarga de cargar game.fxml
        // con el nivel elegido y conectar el teclado.
        navegarA("/com/icesi/sokoban/view/level-select.fxml");
    }

    /**
     * Navega a la pantalla de registro de jugador (player-registration.fxml).
     */
    @FXML
    private void onRegistroClicked() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(
                "/com/icesi/sokoban/view/player-registration.fxml"));
        Stage stage = (Stage) jugarButton.getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    /**
     * Navega a la pantalla de ranking (ranking.fxml).
     */
    @FXML
    private void onRankingClicked() throws IOException {
        navegarA("/com/icesi/sokoban/view/ranking.fxml");
    }

    /**
     * Navega a la pantalla del manual (manual.fxml).
     */
    @FXML
    private void onManualClicked() throws IOException {
        navegarA("/com/icesi/sokoban/view/manual.fxml");
    }

    /**
     * Cierra la aplicación limpiamente.
     * Platform.exit() avisa a JavaFX para que limpie sus hilos
     * antes de salir — es la forma correcta de cerrar.
     */
    @FXML
    private void onSalirClicked() {
        Platform.exit();
    }

    // ─────────────────────────────────────────────────────────────────────
    // Utilidad de navegación
    // ─────────────────────────────────────────────────────────────────────

    /**
     * Carga un FXML y reemplaza la escena actual del Stage.
     * Usado por todos los botones excepto "Jugar" (que necesita
     * conectar el teclado adicionalmente).
     *
     * @param fxmlPath ruta absoluta al recurso FXML dentro del classpath
     */
    private void navegarA(String fxmlPath) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
        Stage stage = (Stage) jugarButton.getScene().getWindow();
        stage.setScene(new Scene(root));
    }
}