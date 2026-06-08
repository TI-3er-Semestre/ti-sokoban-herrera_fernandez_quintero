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
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * CONTROLLER — RankingController  (RF11)
 *
 * Dos vistas:
 *   1. Tabla — lista de partidas ordenadas por puntaje
 *   2. Árbol BST — visualización gráfica del BinarySearchTree
 *
 * El árbol se dibuja recursivamente sobre un Canvas usando JavaFX 2D.
 * Cada nodo muestra: username + puntaje.
 * Hijos izquierdos = puntaje menor, derechos = mayor.
 */
public class RankingController implements Initializable {

    private static final BinarySearchTree<Stats> rankingBST = new BinarySearchTree<>();
    private static final int MAX_ENTRIES = 10;

    @FXML private VBox   rankingContainer;
    @FXML private Label  titleLabel;
    @FXML private Button volverButton;
    @FXML private Button toggleViewButton;
    @FXML private ScrollPane scrollPane;

    private boolean mostrandoArbol = false;

    // ─────────────────────────────────────────────────────────────────────
    // initialize
    // ─────────────────────────────────────────────────────────────────────
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (rankingContainer == null) return;
        cargarDatosEjemplo();
        mostrarTabla();
    }

    // ─────────────────────────────────────────────────────────────────────
    // Datos de ejemplo para cuando no hay partidas reales
    // ─────────────────────────────────────────────────────────────────────
    private void cargarDatosEjemplo() {
        if (!rankingBST.isEmpty()) return;
        rankingBST.insert(new Stats("dayfn",   "dayfn",   1, 12, 3, 45, true, "2026-06-07"));
        rankingBST.insert(new Stats("daniel",  "daniel",  2, 20, 5, 90, true, "2026-06-07"));
        rankingBST.insert(new Stats("andres",  "andres",  1, 8,  2, 30, true, "2026-06-07"));
        rankingBST.insert(new Stats("invitado","invitado",3, 35, 8, 120,true, "2026-06-07"));
        rankingBST.insert(new Stats("dayfn",   "dayfn",   2, 15, 4, 60, true, "2026-06-07"));
    }

    // ─────────────────────────────────────────────────────────────────────
    // Toggle entre tabla y árbol
    // ─────────────────────────────────────────────────────────────────────
    @FXML
    private void onToggleView() {
        mostrandoArbol = !mostrandoArbol;
        if (mostrandoArbol) {
            mostrarArbol();
            toggleViewButton.setText("📋 Ver Tabla");
            titleLabel.setText("🌳 Árbol BST — Ranking");
        } else {
            mostrarTabla();
            toggleViewButton.setText("🌳 Ver Árbol BST");
            titleLabel.setText("🏆 Ranking de Puntajes");
        }
    }

    // ─────────────────────────────────────────────────────────────────────
    // Vista 1: Tabla
    // ─────────────────────────────────────────────────────────────────────
    private void mostrarTabla() {
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
    // Vista 2: Árbol BST dibujado en Canvas
    // ─────────────────────────────────────────────────────────────────────
    private void mostrarArbol() {
        rankingContainer.getChildren().clear();

        int altura = alturaArbol(rankingBST.getRoot());
        int canvasW = Math.max(600, (int) Math.pow(2, altura) * 80);
        int canvasH = altura * 100 + 60;

        Canvas canvas = new Canvas(canvasW, canvasH);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Fondo
        gc.setFill(Color.web("#0f0e17"));
        gc.fillRect(0, 0, canvasW, canvasH);

        // Dibujar árbol recursivamente
        dibujarNodo(gc, rankingBST.getRoot(), canvasW / 2.0, 50, canvasW / 4.0);

        rankingContainer.getChildren().add(canvas);

        // Leyenda
        Label leyenda = new Label("Izquierda = puntaje menor  ·  Derecha = puntaje mayor  ·  Raíz = primer insertado");
        leyenda.setStyle("-fx-text-fill: #a7a9be; -fx-font-size: 11px;");
        leyenda.setPadding(new Insets(8, 0, 0, 0));
        rankingContainer.getChildren().add(leyenda);
    }

    /**
     * Dibuja un nodo y sus hijos recursivamente.
     * @param gc        contexto de dibujo
     * @param node      nodo actual del BST
     * @param x         centro horizontal del nodo
     * @param y         centro vertical del nodo
     * @param offset    distancia horizontal al hijo
     */
    private void dibujarNodo(GraphicsContext gc,
                             BinarySearchTree.Node<Stats> node,
                             double x, double y, double offset) {
        if (node == null) return;

        double radio = 30;

        // Línea al hijo izquierdo
        if (node.left != null) {
            gc.setStroke(Color.web("#3da9fc", 0.6));
            gc.setLineWidth(1.5);
            gc.strokeLine(x, y, x - offset, y + 90);
            dibujarNodo(gc, node.left, x - offset, y + 90, offset / 2);
        }

        // Línea al hijo derecho
        if (node.right != null) {
            gc.setStroke(Color.web("#ff8906", 0.6));
            gc.setLineWidth(1.5);
            gc.strokeLine(x, y, x + offset, y + 90);
            dibujarNodo(gc, node.right, x + offset, y + 90, offset / 2);
        }

        // Nodo — círculo
        boolean esRaiz = node == rankingBST.getRoot();
        gc.setFill(esRaiz ? Color.web("#ff8906") : Color.web("#2a2a4e"));
        gc.fillOval(x - radio, y - radio, radio * 2, radio * 2);
        gc.setStroke(esRaiz ? Color.web("#ff8906") : Color.web("#3da9fc"));
        gc.setLineWidth(2);
        gc.strokeOval(x - radio, y - radio, radio * 2, radio * 2);

        // Texto: username
        gc.setFill(esRaiz ? Color.web("#0f0e17") : Color.web("#fffffe"));
        gc.setFont(Font.font("monospace", 10));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText(node.data.getPlayerName(), x, y - 4);

        // Texto: puntaje
        gc.setFont(Font.font("monospace", 9));
        gc.setFill(esRaiz ? Color.web("#0f0e17") : Color.web("#ff8906"));
        gc.fillText(String.format("%.0f", node.data.getScore()), x, y + 8);
    }

    private int alturaArbol(BinarySearchTree.Node<Stats> node) {
        if (node == null) return 0;
        return 1 + Math.max(alturaArbol(node.left), alturaArbol(node.right));
    }

    // ─────────────────────────────────────────────────────────────────────
    // API pública
    // ─────────────────────────────────────────────────────────────────────
    public static void registrarPartida(Stats stats) {
        if (stats != null && stats.isCompleted()) {
            rankingBST.insert(stats);
        }
    }

    public static BinarySearchTree<Stats> getRankingBST() {
        return rankingBST;
    }

    @FXML
    private void onVolverClicked() throws IOException {
        Parent root = FXMLLoader.load(
                getClass().getResource("/com/icesi/sokoban/view/main-menu.fxml"));
        Stage stage = (Stage) volverButton.getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    // ─────────────────────────────────────────────────────────────────────
    // Tabla — utilidades
    // ─────────────────────────────────────────────────────────────────────
    private CustomLinkedList<Stats> getTopStats(int maxN) {
        CustomLinkedList<Stats> todas = rankingBST.inOrderTraversal();
        CustomLinkedList<Stats> top   = new CustomLinkedList<>();
        int limit = Math.min(maxN, todas.size());
        for (int i = 0; i < limit; i++) top.add(todas.get(i));
        return top;
    }

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

        String posStr = position <= 3
                ? (new String[]{"🥇", "🥈", "🥉"})[position - 1]
                : String.valueOf(position);

        row.getChildren().addAll(
                styledLabel(posStr,                                       40,  "#fffffe", false),
                styledLabel(stats.getPlayerName(),                       160, "#fffffe", false),
                styledLabel(String.valueOf(stats.getLevel()),             60,  "#a7a9be", false),
                styledLabel(String.valueOf(stats.getMovements()),         60,  "#a7a9be", false),
                styledLabel(String.valueOf(stats.getPushes()),            70,  "#a7a9be", false),
                styledLabel(formatTime(stats.getTime()),                  70,  "#a7a9be", false),
                styledLabel(String.format("%.1f", stats.getScore()),      90,  "#ff8906", false)
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
        return String.format("%d:%02d", seconds / 60, seconds % 60);
    }
}