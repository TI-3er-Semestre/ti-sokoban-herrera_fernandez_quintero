package com.icesi.sokoban.gui;

import javafx.application.Application;

/**
 * LAUNCHER — Punto de entrada alternativo.
 *
 * Esta clase existe para poder ejecutar la aplicacion sin que el classpath
 * tenga que estar configurado como modulo JavaFX. Simplemente delega el
 * arranque a SokobanApplication.
 *
 * Usar esta clase como "Main class" en IntelliJ si JavaFX da problemas
 * de modulos al ejecutar SokobanApplication directamente.
 */
public class Launcher {
    public static void main(String[] args) {
        Application.launch(SokobanApplication.class, args);
    }
}
