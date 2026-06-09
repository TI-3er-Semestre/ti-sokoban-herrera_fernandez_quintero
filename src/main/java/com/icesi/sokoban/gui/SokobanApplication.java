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
import com.icesi.sokoban.controller.GamePersistence;
import com.icesi.sokoban.controller.GameController;

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