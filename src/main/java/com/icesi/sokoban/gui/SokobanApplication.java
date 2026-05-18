package com.icesi.sokoban.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

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

        // 1. Cargar el FXML: crea nodos + instancia controlador + inyecta + initialize()
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/icesi/sokoban/view/game.fxml"));
        Parent root = loader.load();

        // 2. Obtener el controlador creado por el FXMLLoader
        GameController controller = loader.getController();

        // 3. Construir la Scene
        Scene scene = new Scene(root);

        // 4. Conectar el teclado (la Scene ya existe)
        controller.attachKeyHandlers(scene);

        // 5. Configurar y mostrar la ventana
        primaryStage.setTitle("Sokoban — Tarea Integradora");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
