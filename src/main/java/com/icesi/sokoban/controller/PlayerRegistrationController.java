package com.icesi.sokoban.controller;

import com.icesi.sokoban.model.ExperienceLevel;

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

import java.net.URL;
import java.util.ResourceBundle;

/**
 * CONTROLLER — PlayerRegistrationController  (RF1: Registrar jugador — paso 1)
 *
 * Responsabilidades:
 *   - Recoger los datos del formulario (nombre, email, username, nivel).
 *   - Validar los campos antes de continuar.
 *   - Navegar a AvatarSelectController pasándole los datos mediante setDatosRegistro().
 *
 * El registro definitivo en PlayerRegistry ocurre en AvatarSelectController,
 * una vez que el jugador elige su avatar.
 */
public class PlayerRegistrationController implements Initializable {

    // ── Nodos inyectados desde player-registration.fxml ──────────────────
    @FXML private TextField      nameField;
    @FXML private TextField      emailField;
    @FXML private TextField      usernameField;
    @FXML private ChoiceBox<String> levelChoice;
    @FXML private Label          statusLabel;
    @FXML private Button         siguienteButton;

    // ─────────────────────────────────────────────────────────────────────
    // initialize() — poblamos el ChoiceBox
    // ─────────────────────────────────────────────────────────────────────
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        levelChoice.getItems().addAll("BEGINNER", "ADVANCED", "EXPERT");
        levelChoice.setValue("BEGINNER");
    }

    // ─────────────────────────────────────────────────────────────────────
    // onSiguienteClicked() — valida y navega a la selección de avatar
    // ─────────────────────────────────────────────────────────────────────
    @FXML
    private void onSiguienteClicked() {
        String name     = nameField.getText().trim();
        String email    = emailField.getText().trim();
        String username = usernameField.getText().trim();
        String level    = levelChoice.getValue();

        // Validaciones básicas — el registro completo lo hace AvatarSelectController
        if (name.isEmpty()) {
            mostrarError("El nombre no puede estar vacío.");
            return;
        }
        if (!email.contains("@") || !email.contains(".")) {
            mostrarError("Formato de correo inválido.");
            return;
        }
        if (username.isEmpty()) {
            mostrarError("El username no puede estar vacío.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/icesi/sokoban/view/Avatar_select.fxml"));
            Parent root = loader.load();

            AvatarSelectController ac = loader.getController();
            ac.setDatosRegistro(name, email, username, level);

            Stage stage = (Stage) siguienteButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.sizeToScene();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarError("Error al cargar la pantalla de avatar.");
        }
    }

    // ─────────────────────────────────────────────────────────────────────
    // Utilidades de la vista
    // ─────────────────────────────────────────────────────────────────────
    private void mostrarError(String msg) {
        statusLabel.setStyle("-fx-text-fill: #ef4565; -fx-font-size: 12px; -fx-font-weight: bold;");
        statusLabel.setText(msg);
    }
}