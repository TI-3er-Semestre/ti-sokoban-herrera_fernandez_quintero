package com.icesi.sokoban.controller;

import com.icesi.sokoban.model.ExperienceLevel;
import com.icesi.sokoban.model.Player;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * CONTROLLER — PlayerRegistrationController  (RF1: Registrar jugador)
 *
 * Responsabilidades:
 *   - Recoger los datos del formulario (nombre, email, username, nivel, avatar).
 *   - Llamar a PlayerRegistry.registerPlayer() con esos datos.
 *   - Mostrar el mensaje de éxito o error en statusLabel.
 *   - Si el registro es exitoso, guardar el jugador activo en PlayerRegistry
 *     para que GameController sepa quién está jugando.
 *
 * PlayerRegistry es estático — se comparte entre todas las pantallas.
 * Así GameController puede preguntar quién está registrado sin que
 * esta pantalla le pase nada explícitamente.
 */
public class PlayerRegistrationController implements Initializable {

    // ── Nodos inyectados desde player-registration.fxml ──────────────────
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField usernameField;
    @FXML private ChoiceBox<String> levelChoice;
    @FXML private Button avatarWizard;
    @FXML private Button avatarRobot;
    @FXML private Button avatarFox;
    @FXML private Label statusLabel;
    @FXML private Button volverButton;

    // ── Estado interno ────────────────────────────────────────────────────
    // Avatar seleccionado — empieza vacío, el usuario debe elegir uno.
    private String selectedAvatar = "";

    // ── Colores para feedback visual del avatar seleccionado ──────────────
    private static final String STYLE_AVATAR_NORMAL =
            "-fx-background-color: #2a2a4a; -fx-text-fill: #fffffe; " +
                    "-fx-border-color: #555577; -fx-border-radius: 4; " +
                    "-fx-background-radius: 4; -fx-padding: 8 16 8 16; -fx-cursor: hand;";
    private static final String STYLE_AVATAR_SELECTED =
            "-fx-background-color: #ff8906; -fx-text-fill: #0f0e17; " +
                    "-fx-border-color: #ff8906; -fx-border-radius: 4; " +
                    "-fx-background-radius: 4; -fx-padding: 8 16 8 16; -fx-cursor: hand;";

    // ─────────────────────────────────────────────────────────────────────
    // initialize() — se ejecuta después de inyectar los @FXML
    // ─────────────────────────────────────────────────────────────────────

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Poblar el ChoiceBox con los valores del enum ExperienceLevel
        levelChoice.getItems().addAll("BEGINNER", "ADVANCED", "EXPERT");
        levelChoice.setValue("BEGINNER"); // valor por defecto
    }

    // ─────────────────────────────────────────────────────────────────────
    // Selección de avatar — resalta el botón elegido
    // ─────────────────────────────────────────────────────────────────────

    @FXML
    private void onAvatarWizard() {
        selectedAvatar = "🧙";
        avatarWizard.setStyle(STYLE_AVATAR_SELECTED);
        avatarRobot.setStyle(STYLE_AVATAR_NORMAL);
        avatarFox.setStyle(STYLE_AVATAR_NORMAL);
    }

    @FXML
    private void onAvatarRobot() {
        selectedAvatar = "🤖";
        avatarWizard.setStyle(STYLE_AVATAR_NORMAL);
        avatarRobot.setStyle(STYLE_AVATAR_SELECTED);
        avatarFox.setStyle(STYLE_AVATAR_NORMAL);
    }

    @FXML
    private void onAvatarFox() {
        selectedAvatar = "🦊";
        avatarWizard.setStyle(STYLE_AVATAR_NORMAL);
        avatarRobot.setStyle(STYLE_AVATAR_NORMAL);
        avatarFox.setStyle(STYLE_AVATAR_SELECTED);
    }

    // ─────────────────────────────────────────────────────────────────────
    // Registro — recoge los campos y llama a PlayerRegistry
    // ─────────────────────────────────────────────────────────────────────

    @FXML
    private void onRegistrarClicked() {
        String name     = nameField.getText().trim();
        String email    = emailField.getText().trim();
        String username = usernameField.getText().trim();
        String level    = levelChoice.getValue();

        // Validar que eligió un avatar
        if (selectedAvatar.isEmpty()) {
            mostrarError("Por favor elige un avatar.");
            return;
        }

        // Convertir el String del ChoiceBox al enum
        ExperienceLevel expLevel = ExperienceLevel.valueOf(level);

        // Llamar al registro — PlayerRegistry valida y retorna un mensaje
        String resultado = PlayerRegistry.getInstance()
                .registerPlayer(name, email, username, selectedAvatar, expLevel);

        if (resultado.startsWith("ÉXITO")) {
            mostrarExito(resultado);
            limpiarFormulario();
        } else {
            mostrarError(resultado);
        }
    }

    // ─────────────────────────────────────────────────────────────────────
    // Navegación — volver al menú principal
    // ─────────────────────────────────────────────────────────────────────

    @FXML
    private void onVolverClicked() throws IOException {
        Parent root = FXMLLoader.load(
                getClass().getResource("/com/icesi/sokoban/view/main-menu.fxml"));
        Stage stage = (Stage) volverButton.getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    // ─────────────────────────────────────────────────────────────────────
    // Utilidades de la vista
    // ─────────────────────────────────────────────────────────────────────

    private void mostrarExito(String mensaje) {
        statusLabel.setStyle("-fx-text-fill: #00c896; -fx-font-size: 13px; -fx-font-weight: bold;");
        statusLabel.setText(mensaje);
    }

    private void mostrarError(String mensaje) {
        statusLabel.setStyle("-fx-text-fill: #ef4565; -fx-font-size: 13px; -fx-font-weight: bold;");
        statusLabel.setText(mensaje);
    }

    private void limpiarFormulario() {
        nameField.clear();
        emailField.clear();
        usernameField.clear();
        levelChoice.setValue("BEGINNER");
        selectedAvatar = "";
        avatarWizard.setStyle(STYLE_AVATAR_NORMAL);
        avatarRobot.setStyle(STYLE_AVATAR_NORMAL);
        avatarFox.setStyle(STYLE_AVATAR_NORMAL);
    }
}