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
import java.util.ResourceBundle;

/**
 * CONTROLLER — ManualController  (RF5: Mostrar manual del juego)
 *
 * El manual es contenido estático — este controlador solo
 * maneja el botón "Volver al Menú".
 */
public class ManualController implements Initializable {

    @FXML private Button volverButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Sin estado que inicializar — el manual es contenido estático.
    }

    @FXML
    private void onVolverClicked() throws IOException {
        Parent root = FXMLLoader.load(
                getClass().getResource("/com/icesi/sokoban/view/main-menu.fxml"));
        Stage stage = (Stage) volverButton.getScene().getWindow();
        stage.setScene(new Scene(root));
    }
}
