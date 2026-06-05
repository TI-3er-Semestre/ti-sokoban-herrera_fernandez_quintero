/**
 * Definicion del modulo de la aplicacion Sokoban.
 *
 * - requires: librerias que necesita la aplicacion (JavaFX).
 * - opens: paquetes que JavaFX necesita abrir por reflexion
 *   para poder inyectar los nodos @FXML en los controladores.
 * - exports: paquetes visibles a otros modulos.
 */
module com.icesi.sokoban {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.desktop;

    // JavaFX necesita acceso por reflexion a los controladores y a la clase de arranque
    opens com.icesi.sokoban.gui        to javafx.fxml;
    opens com.icesi.sokoban.controller to javafx.fxml;

    exports com.icesi.sokoban.gui;
    exports com.icesi.sokoban.controller;
    exports com.icesi.sokoban.model;
    exports com.icesi.sokoban.structure;
}
