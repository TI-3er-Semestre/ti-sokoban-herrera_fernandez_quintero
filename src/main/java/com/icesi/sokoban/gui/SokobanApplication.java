package com.icesi.sokoban.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;

import com.icesi.sokoban.controller.MainMenuController;
import com.icesi.sokoban.controller.GamePersistence;
import com.icesi.sokoban.controller.GameController;

/**
 * MAIN — Punto de entrada de la aplicacion.
 *
 * Conecta las tres capas del patron MVC mediante el FXMLLoader.
 *
 * Secuencia de arranque (el orden importa):
 *
 *   1. FXMLLoader.load()
 *        a. Lee game.fxml y crea todos los nodos de la interfaz.
 *        b. Instancia GameController (definido en el atributo fx:controller).
 *        c. Inyecta los campos @FXML (Canvas, Label) en el controlador.
 *        d. Llama automaticamente a GameController.initialize().
 *
 *   2. loader.getController()
 *        Obtiene la instancia del controlador creada en el paso 1.
 *
 *   3. new Scene(root)
 *        Envuelve el arbol de nodos en una Scene.
 *
 *   4. controller.attachKeyHandlers(scene)
 *        Registra el manejo del teclado. Se hace aqui y no en initialize()
 *        porque la Scene todavia no existe cuando initialize() se ejecuta.
 *        El objeto Game completo se serializa en savegame.dat
 *
 *   5. stage.show()
 *        Muestra la ventana, ya pintada y con el teclado conectado.
 *
 * Reglas de diseno MVC:
 *   - El Model (model/, structure/) no tiene ningun import de JavaFX.
 *   - La Vista (game.fxml) no tiene codigo Java.
 *   - El Controller es el unico que toca ambas capas.
 */
public class SokobanApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        // 1. Cargar el menú principal como primera pantalla
        Parent root = FXMLLoader.load(
                getClass().getResource("/com/icesi/sokoban/view/main-menu.fxml"));

        // 2. Construir la Scene con el menú
        Scene scene = new Scene(root);

        // 3. Configurar y mostrar la ventana
        // El menú no necesita teclado — los botones manejan la interacción.
        // Cuando el usuario haga clic en "Jugar", MainMenuController cargará
        // game.fxml y conectará el teclado en ese momento.
        primaryStage.setTitle("Sokoban — Tarea Integradora");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                if (GameController.getActiveGame() != null){
                    GamePersistence.saveGame(GameController.getActiveGame());
                }
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}