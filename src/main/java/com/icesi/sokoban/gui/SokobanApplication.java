package com.icesi.sokoban.gui;

import com.icesi.sokoban.controller.IntroController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 * Punto de entrada de la aplicación JavaFX.
 *
 * Al arrancar por primera vez:
 *   → Carga intro.fxml (video + press any key)
 *   → IntroController navega al menú principal cuando el usuario presiona una tecla
 *
 * Si el jugador vuelve al menú desde el juego:
 *   → Va directo a main-menu.fxml (sin intro)
 *
 * El flag primeraVez controla que la intro solo se muestre una vez por sesión.
 */
public class SokobanApplication extends Application {

    public static boolean primeraVez = true;

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root;
        Scene scene;

        if (primeraVez) {
            primeraVez = false;

            // Cargar intro
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/icesi/sokoban/view/intro.fxml"));
            root = loader.load();

            IntroController introController = loader.getController();
            scene = new Scene(root, 840, 560);
            introController.attachKeyHandlers(scene);

        } else {
            // Ir directo al menú principal
            root = FXMLLoader.load(
                    getClass().getResource("/com/icesi/sokoban/view/main-menu.fxml"));
            scene = new Scene(root);
        }

        primaryStage.setTitle("Sokoban — A Jungle Puzzle Adventure");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}