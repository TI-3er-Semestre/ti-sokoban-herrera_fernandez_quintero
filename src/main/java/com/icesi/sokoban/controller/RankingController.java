package com.icesi.sokoban.controller;

import com.icesi.sokoban.model.Stats;
import com.icesi.sokoban.structure.BinarySearchTree;
import com.icesi.sokoban.structure.CustomLinkedList;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * CONTROLLER — RankingController  (RF11: Mostrar ranking de puntajes)
 *
 * Responsabilidades:
 *   - Leer el BST estático de Stats y construir las filas de la tabla.
 *   - Mostrar hasta 10 partidas ordenadas por puntaje descendente.
 *   - Navegar de vuelta al menú principal.
 *
 * El BST es estático — GameController llama registrarPartida() al ganar,
 * y esta pantalla lo lee cada vez que se abre.
 */
public class RankingController implements Initializable {

    // ── BST estático compartido con GameController ────────────────────────
    private static final BinarySearchTree<Stats> rankingBST = new BinarySearchTree<>();
    private static final int MAX_ENTRIES = 10;

    // ── Nodos FXML ────────────────────────────────────────────────────────
    @FXML private VBox   rankingContainer;
    @FXML private Label  titleLabel;
    @FXML private Button volverButton;

    // ─────────────────────────────────────────────────────────────────────
    //  initialize() — construye la tabla con los datos del BST
    // ─────────────────────────────────────────────────────────────────────
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (rankingContainer == null) return;

        rankingContainer.getChildren().clear();
        rankingContainer.getChildren().add(buildHeader());

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
    //  API pública — GameController llama esto al ganar un nivel
    // ─────────────────────────────────────────────────────────────────────
    public static void registrarPartida(Stats stats) {
        if (stats != null && stats.isCompleted()) {
            rankingBST.insert(stats);
        }
    }

    public static BinarySearchTree<Stats> getRankingBST() {
        return rankingBST;
    }

    // ─────────────────────────────────────────────────────────────────────
    //  Navegación
    // ─────────────────────────────────────────────────────────────────────
    @FXML
    private void onVolverClicked() throws IOException {
        Parent root = FXMLLoader.load(
                getClass().getResource("/com/icesi/sokoban/view/main-menu.fxml"));
        Stage stage = (Stage) volverButton.getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    // ─────────────────────────────────────────────────────────────────────
    //  Lógica interna
    // ─────────────────────────────────────────────────────────────────────
    private CustomLinkedList<Stats> getTopStats(int maxN) {
        CustomLinkedList<Stats> todas = rankingBST.inOrderTraversal();
        CustomLinkedList<Stats> top   = new CustomLinkedList<>();
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
                styledLabel("#",        40,  "#a7a9be", true),
                styledLabel("Jugador",  160, "#a7a9be", true),
                styledLabel("Nivel",    60,  "#a7a9be", true),
                styledLabel("Mov",      60,  "#a7a9be", true),
                styledLabel("Empujes",  70,  "#a7a9be", true),
                styledLabel("Tiempo",   70,  "#a7a9be", true),
                styledLabel("Puntaje",  90,  "#a7a9be", true)
        );
        return header;
    }

    private HBox buildRow(int position, Stats stats) {
        HBox row = new HBox(10);
        row.setPadding(new Insets(6, 8, 6, 8));
        String bg = (position % 2 == 0) ? "#1a1b2e" : "#16213e";
        row.setStyle("-fx-background-color: " + bg + "; -fx-background-radius: 4;");

        String posStr   = position <= 3
                ? (new String[]{"🥇", "🥈", "🥉"})[position - 1]
                : String.valueOf(position);
        String scoreStr = String.format("%.1f", stats.getScore());
        String timeStr  = formatTime(stats.getTime());

        row.getChildren().addAll(
                styledLabel(posStr,                              40,  "#fffffe", false),
                styledLabel(stats.getPlayerName(),              160, "#fffffe", false),
                styledLabel(String.valueOf(stats.getLevel()),    60,  "#a7a9be", false),
                styledLabel(String.valueOf(stats.getMovements()), 60, "#a7a9be", false),
                styledLabel(String.valueOf(stats.getPushes()),   70,  "#a7a9be", false),
                styledLabel(timeStr,                             70,  "#a7a9be", false),
                styledLabel(scoreStr,                            90,  "#ff8906", false)
        );
        return row;
    }

    private Label styledLabel(String text, double width, String color, boolean bold) {
        Label lbl = new Label(text);
        lbl.setMinWidth(width);
        lbl.setPrefWidth(width);
        lbl.setStyle("-fx-text-fill: " + color + "; -fx-font-size: 13px; " +
                "-fx-font-weight: " + (bold ? "bold" : "normal") + ";");
        return lbl;
    }

    private String formatTime(int seconds) {
        int m = seconds / 60;
        int s = seconds % 60;
        return String.format("%d:%02d", m, s);
    }
}