package com.icesi.sokoban.gui;

import com.icesi.sokoban.controller.IntroController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;

import com.icesi.sokoban.controller.MainMenuController;
import com.icesi.sokoban.controller.GameController;

public class SokobanApplication extends Application {

    public static boolean primeraVez = true;

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root;
        Scene scene;

        if (primeraVez) {
            primeraVez = false;

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/icesi/sokoban/view/intro.fxml"));
            root = loader.load();

            IntroController introController = loader.getController();
            scene = new Scene(root);
            introController.attachKeyHandlers(scene);

        } else {
            root = FXMLLoader.load(
                    getClass().getResource("/com/icesi/sokoban/view/main-menu.fxml"));
            scene = new Scene(root);
        }

        primaryStage.setTitle("Sokoban — A Jungle Puzzle Adventure");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.sizeToScene();
        primaryStage.show();

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}