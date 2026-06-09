package com.icesi.sokoban.controller;

import com.icesi.sokoban.model.ExperienceLevel;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * CONTROLLER — AvatarSelectController  (RF1: Selección de avatar)
 *
 * Responsabilidades:
 *   - Mostrar el video 360° del personaje actual en un MediaView.
 *   - Navegar entre los 4 personajes con las flechas ◄ ►.
 *   - Al presionar "Elegir este avatar": registrar el jugador en PlayerRegistry
 *     con el avatar seleccionado y navegar a la selección de nivel.
 *   - Al presionar "Volver al menú": descartar y regresar al menú principal.
 *
 * Recibe los datos del formulario de registro mediante setDatosRegistro().
 */
public class AvatarSelectController implements Initializable {

    // ── Nodos inyectados desde Avatar_select.fxml ─────────────────────────
    @FXML private MediaView mediaView;
    @FXML private Label     skinNameLabel;
    @FXML private Button    volverButton;
    @FXML private Button    anteriorButton;

    // ── Personajes disponibles ────────────────────────────────────────────
    private static final String[] NOMBRES = {"Ingrid", "Robot", "Zorro", "Mage"};
    private static final String[] VIDEOS  = {
            "skin_mage.mp4", "skin_ingrid.mp4",
            "skin_robot.mp4", "skin_zorro.mp4"
    };

    // ── Estado interno ────────────────────────────────────────────────────
    private int         index = 0;
    private MediaPlayer mediaPlayer;

    // Datos del formulario — llegaron desde PlayerRegistrationController
    private String nombre, email, username, nivel;

    // ─────────────────────────────────────────────────────────────────────
    // initialize() — arranca reproduciendo el primer personaje
    // ─────────────────────────────────────────────────────────────────────
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        reproducir(0);
    }

    // ─────────────────────────────────────────────────────────────────────
    // setDatosRegistro() — llamado por PlayerRegistrationController
    // antes de mostrar esta pantalla
    // ─────────────────────────────────────────────────────────────────────
    public void setDatosRegistro(String nombre, String email,
                                 String username, String nivel) {
        this.nombre   = nombre;
        this.email    = email;
        this.username = username;
        this.nivel    = nivel;
    }

    // ─────────────────────────────────────────────────────────────────────
    // Navegación entre personajes
    // ─────────────────────────────────────────────────────────────────────
    @FXML
    private void onAnteriorClicked() {
        index = (index - 1 + NOMBRES.length) % NOMBRES.length;
        reproducir(index);
    }

    @FXML
    private void onSiguienteClicked() {
        index = (index + 1) % NOMBRES.length;
        reproducir(index);
    }

    // ─────────────────────────────────────────────────────────────────────
    // reproducir() — carga y reproduce el video del personaje i
    // ─────────────────────────────────────────────────────────────────────
    private void reproducir(int i) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
        }
        skinNameLabel.setText(NOMBRES[i]);
        try {
            URL url = getClass().getResource(
                    "/com/icesi/sokoban/sprites/Video/" + VIDEOS[i]);
            if (url == null) {
                System.err.println("[AvatarSelect] Video no encontrado: " + VIDEOS[i]);
                return;
            }
            mediaPlayer = new MediaPlayer(new Media(url.toExternalForm()));
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaView.setMediaPlayer(mediaPlayer);
            mediaPlayer.setOnReady(() -> mediaPlayer.play());
            mediaPlayer.setOnError(() ->
                    System.err.println("[AvatarSelect] Error video: " + mediaPlayer.getError()));
        } catch (Exception e) {
            System.err.println("[AvatarSelect] Excepcion: " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────────────────────────────────
    // onElegirClicked() — registra el jugador y va a level-select
    // ─────────────────────────────────────────────────────────────────────
    @FXML
    private void onElegirClicked() {
        if (mediaPlayer != null) mediaPlayer.dispose();
        try {
            ExperienceLevel expLevel = ExperienceLevel.valueOf(
                    nivel != null ? nivel : "BEGINNER");

            String avatarElegido = NOMBRES[index];

            // Intentar registrar — puede fallar si el username ya existe
            String resultado = PlayerRegistry.getInstance()
                    .registerPlayer(nombre, email, username, avatarElegido, expLevel);
            System.out.println("[AvatarSelect] Registro: " + resultado);

            // Sin importar si el registro fue nuevo o ya existía,
            // siempre actualizamos el avatar del jugador activo
            com.icesi.sokoban.model.Player active =
                    PlayerRegistry.getInstance().getActivePlayer();
            if (active != null) {
                active.setAvatar(avatarElegido);
                System.out.println("[AvatarSelect] Avatar actualizado: " + avatarElegido);
            }

            // Navegar a selección de nivel
            Parent root = FXMLLoader.load(
                    getClass().getResource("/com/icesi/sokoban/view/level-select.fxml"));
            Stage stage = (Stage) volverButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ─────────────────────────────────────────────────────────────────────
    // onVolverClicked() — descarta y regresa al menú principal
    // ─────────────────────────────────────────────────────────────────────
    @FXML
    private void onVolverClicked() {
        if (mediaPlayer != null) mediaPlayer.dispose();
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/com/icesi/sokoban/view/main-menu.fxml"));
            Stage stage = (Stage) volverButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}