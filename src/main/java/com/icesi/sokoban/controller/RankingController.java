package com.icesi.sokoban.controller;

import com.icesi.sokoban.model.Stats;
import com.icesi.sokoban.structure.BinarySearchTree;
import com.icesi.sokoban.structure.CustomLinkedList;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * CONTROLLER — RankingController  (RF11: Mostrar ranking de puntajes)
 *
 * Responsabilidades:
 *   - Recibir el BST de Stats del juego (inyectado desde afuera o estático).
 *   - Extraer los puntajes en orden descendente (inOrder del BST, que ordena
 *     por score gracias a Stats.compareTo).
 *   - Mostrar las primeras N entradas del ranking en la vista ranking.fxml.
 *
 * El BST almacena Stats ordenados por score (mayor puntaje = raíz más a la
 * izquierda, porque Stats.compareTo ordena descendentemente).
 */
public class RankingController implements Initializable {

    // ── Registro global de partidas (compartido con GameController) ───────
    // Se usa un BST estático para que persista entre pantallas sin necesidad
    // de un sistema de persistencia externo.
    private static final BinarySearchTree<Stats> rankingBST = new BinarySearchTree<>();

    private static final int MAX_ENTRIES = 10;

    // ── Nodos FXML ────────────────────────────────────────────────────────
    @FXML private VBox rankingContainer;
    @FXML private Label titleLabel;

    // ─────────────────────────────────────────────────────────────────────
    //  initialize() — construye la vista con los datos del BST
    // ─────────────────────────────────────────────────────────────────────

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (rankingContainer == null) return; // protección en tests sin FXML

        rankingContainer.getChildren().clear();

        // Encabezado de columnas
        rankingContainer.getChildren().add(buildHeader());

        // Obtener entradas ordenadas por score descendente
        CustomLinkedList<Stats> ordenadas = getTopStats(MAX_ENTRIES);

        if (ordenadas.isEmpty()) {
            Label empty = new Label("Aún no hay partidas registradas.");
            empty.setStyle("-fx-text-fill: #a7a9be; -fx-font-size: 13px;");
            empty.setPadding(new Insets(10, 0, 0, 0));
            rankingContainer.getChildren().add(empty);
            return;
        }

        for (int i = 0; i < ordenadas.size(); i++) {
            rankingContainer.getChildren().add(buildRow(i + 1, ordenadas.get(i)));
        }
    }

    // ─────────────────────────────────────────────────────────────────────
    //  API pública — GameController llama esto al terminar un nivel
    // ─────────────────────────────────────────────────────────────────────

    /**
     * Registra una partida terminada en el BST de ranking.
     * Llamar desde GameController cuando state == WON.
     */
    public static void registrarPartida(Stats stats) {
        if (stats != null && stats.isCompleted()) {
            rankingBST.insert(stats);
        }
    }

    /**
     * Retorna el BST completo (para testing o persistencia futura).
     */
    public static BinarySearchTree<Stats> getRankingBST() {
        return rankingBST;
    }

    // ─────────────────────────────────────────────────────────────────────
    //  Lógica interna
    // ─────────────────────────────────────────────────────────────────────

    /**
     * Extrae hasta maxN Stats del BST en orden (inOrder = ascendente por
     * compareTo, que en Stats significa descendente por score).
     */
    private CustomLinkedList<Stats> getTopStats(int maxN) {
        CustomLinkedList<Stats> todas = rankingBST.inOrderTraversal();
        CustomLinkedList<Stats> top = new CustomLinkedList<>();
        int limit = Math.min(maxN, todas.size());
        for (int i = 0; i < limit; i++) {
            top.add(todas.get(i));
        }
        return top;
    }

    // ─────────────────────────────────────────────────────────────────────
    //  Construcción de filas UI
    // ─────────────────────────────────────────────────────────────────────

    private HBox buildHeader() {
        HBox header = new HBox(10);
        header.setPadding(new Insets(4, 8, 4, 8));
        header.setStyle("-fx-background-color: #2e2f3e; -fx-background-radius: 6;");

        header.getChildren().addAll(
                styledLabel("#",      40,  "#a7a9be", true),
                styledLabel("Jugador", 160, "#a7a9be", true),
                styledLabel("Nivel",   60,  "#a7a9be", true),
                styledLabel("Mov",     60,  "#a7a9be", true),
                styledLabel("Empujes", 70,  "#a7a9be", true),
                styledLabel("Tiempo",  70,  "#a7a9be", true),
                styledLabel("Puntaje", 90,  "#a7a9be", true)
        );
        return header;
    }

    private HBox buildRow(int position, Stats stats) {
        HBox row = new HBox(10);
        row.setPadding(new Insets(6, 8, 6, 8));

        // Alternar colores de fila
        String bg = (position % 2 == 0) ? "#1a1b2e" : "#16213e";
        row.setStyle("-fx-background-color: " + bg + "; -fx-background-radius: 4;");

        // Medalla para el top 3
        String posStr = position <= 3
                ? (new String[]{"🥇", "🥈", "🥉"})[position - 1]
                : String.valueOf(position);

        String scoreStr = String.format("%.1f", stats.getScore());
        String timeStr  = formatTime(stats.getTime());

        row.getChildren().addAll(
                styledLabel(posStr,                     40,  "#fffffe", false),
                styledLabel(stats.getPlayerName(),      160, "#fffffe", false),
                styledLabel(String.valueOf(stats.getLevel()), 60, "#a7a9be", false),
                styledLabel(String.valueOf(stats.getMovements()), 60, "#a7a9be", false),
                styledLabel(String.valueOf(stats.getPushes()),    70, "#a7a9be", false),
                styledLabel(timeStr,                    70,  "#a7a9be", false),
                styledLabel(scoreStr,                   90,  "#ff8906", false)
        );
        return row;
    }

    private Label styledLabel(String text, double width, String color, boolean bold) {
        Label lbl = new Label(text);
        lbl.setMinWidth(width);
        lbl.setPrefWidth(width);
        String weight = bold ? "bold" : "normal";
        lbl.setStyle("-fx-text-fill: " + color + "; -fx-font-size: 13px; -fx-font-weight: " + weight + ";");
        return lbl;
    }

    private String formatTime(int seconds) {
        int m = seconds / 60;
        int s = seconds % 60;
        return String.format("%d:%02d", m, s);
    }
}