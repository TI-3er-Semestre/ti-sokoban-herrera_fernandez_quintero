package com.icesi.sokoban.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * CONTROLLER — IntroController
 *
 * Maneja la secuencia de introducción al iniciar la app:
 *   1. Reproduce intro_video.mp4 automáticamente
 *   2. Cuando el video termina → muestra press_any_key.png
 *   3. Al presionar cualquier tecla → navega al menú principal
 *
 * Esta pantalla solo se muestra una vez al abrir la app.
 * SokobanApplication.primeraVez controla esto.
 */
public class IntroController implements Initializable {

    @FXML private MediaView mediaView;
    @FXML private ImageView pressAnyKeyImage;

    private MediaPlayer mediaPlayer;
    private boolean videoTerminado = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarImagen();
        reproducirVideo();
    }

    // ─────────────────────────────────────────────────────────────────────
    // Carga de recursos
    // ─────────────────────────────────────────────────────────────────────

    private void cargarImagen() {
        try {
            URL imgUrl = getClass().getResource(
                    "/com/icesi/sokoban/sprites/press_any_key.png");
            if (imgUrl != null) {
                pressAnyKeyImage.setImage(new Image(imgUrl.toExternalForm()));
            }
        } catch (Exception e) {
            System.err.println("No se pudo cargar press_any_key.png: " + e.getMessage());
        }
    }

    private void reproducirVideo() {
        try {
            // Intentar cargar desde el classpath primero
            URL videoUrl = getClass().getResource(
                    "/com/icesi/sokoban/sprites/intro_video.mp4");

            String mediaUri;
            if (videoUrl != null) {
                mediaUri = videoUrl.toExternalForm();
            } else {
                // Fallback: buscar junto al jar ejecutable
                java.io.File videoFile = new java.io.File("src/main/resources/com/icesi/sokoban/sprites/intro_video.mp4");
                if (!videoFile.exists()) {
                    mostrarPressAnyKey();
                    return;
                }
                mediaUri = videoFile.toURI().toString();
            }

            Media media = new Media(mediaUri);
            mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);

            // Buffering — esperar a que esté listo antes de reproducir
            mediaPlayer.setOnReady(() -> mediaPlayer.play());
            mediaPlayer.setOnEndOfMedia(this::mostrarPressAnyKey);

            // Si hay error de media, ir a la imagen
            mediaPlayer.setOnError(() -> {
                System.err.println("Error de media: " + mediaPlayer.getError());
                mostrarPressAnyKey();
            });

        } catch (Exception e) {
            System.err.println("No se pudo cargar el video: " + e.getMessage());
            mostrarPressAnyKey();
        }
    }

    // ─────────────────────────────────────────────────────────────────────
    // Transiciones
    // ─────────────────────────────────────────────────────────────────────

    private void mostrarPressAnyKey() {
        videoTerminado = true;
        if (mediaPlayer != null) mediaPlayer.stop();
        mediaView.setVisible(false);
        pressAnyKeyImage.setVisible(true);
    }

    /**
     * Llamado desde SokobanApplication después de crear la Scene.
     * Registra el teclado para navegar al menú al presionar cualquier tecla.
     */
    public void attachKeyHandlers(Scene scene) {
        scene.setOnKeyPressed(event -> {
            if (videoTerminado) {
                navegarAlMenu(scene);
            } else {
                // Si presionan tecla durante el video, saltar al press any key
                mostrarPressAnyKey();
            }
        });

        // También funciona con clic del mouse
        scene.setOnMouseClicked(event -> {
            if (videoTerminado) {
                navegarAlMenu(scene);
            } else {
                mostrarPressAnyKey();
            }
        });
    }

    private void navegarAlMenu(Scene scene) {
        try {
            if (mediaPlayer != null) mediaPlayer.dispose();
            Parent root = FXMLLoader.load(
                    getClass().getResource("/com/icesi/sokoban/view/main-menu.fxml"));
            Stage stage = (Stage) scene.getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            System.err.println("Error navegando al menú: " + e.getMessage());
        }
    }
}