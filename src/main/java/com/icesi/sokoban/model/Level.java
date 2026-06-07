package com.icesi.sokoban.model;

import com.icesi.sokoban.structure.CustomLinkedList;

/**
 * Representa un nivel del juego Sokoban.
 *
 * Incluye loadFromJson() que parsea manualmente el JSON del nivel
 * sin usar librerías externas (Gson, Jackson, etc.).
 *
 * Formato esperado del JSON:
 * {
 *   "id": 1,
 *   "name": "First Steps",
 *   "difficulty": "BASIC",
 *   "rows": 8,
 *   "cols": 10,
 *   "playerStart": { "row": 3, "col": 2 },
 *   "targets": [ { "row": 3, "col": 7 } ],
 *   "boxes": [ { "row": 3, "col": 4 } ],
 *   "grid": [
 *     [1,1,1,1,...],
 *     ...
 *   ]
 * }
 *
 * Leyenda del grid:
 *   0 = FLOOR   → ' '
 *   1 = WALL    → '#'
 *   2 = BOX     → '$'
 *   3 = TARGET  → '.'
 *   4 = PLAYER  → '@' (también fija playerStart)
 *   5 = BOX_ON_TARGET → '*'
 *   6 = PLAYER_ON_TARGET → '+'
 */
public class Level {

    private int levelId;
    private String name;
    private Board board;
    private Position playerStartPosition;
    private String difficulty;
    private int timeLimit; // segundos, 0 = sin límite

    public Level(int levelId, String name) {
        this.levelId = levelId;
        this.name = name;
    }

    // ─────────────────────────────────────────────────────────────────────
    //  loadFromJson — parser manual, sin librerías externas
    // ─────────────────────────────────────────────────────────────────────

    /**
     * Carga el estado del nivel a partir de un String JSON.
     * Precondición: jsonString no es null y tiene el formato descrito arriba.
     * Postcondición: board, playerStartPosition y difficulty quedan inicializados.
     */
    public void loadFromJson(String jsonString) {
        // Eliminar saltos de línea y espacios redundantes para simplificar el parsing
        String json = jsonString.replaceAll("\\s+", " ").trim();

        // ── Campos escalares ──────────────────────────────────────────────
        this.levelId   = parseInt(extractValue(json, "id"));
        this.name      = extractString(json, "name");
        this.difficulty = extractString(json, "difficulty");
        this.timeLimit = parseInt(extractValue(json, "timeLimit"));

        int rows = parseInt(extractValue(json, "rows"));
        int cols = parseInt(extractValue(json, "cols"));

        // ── Tablero ───────────────────────────────────────────────────────
        this.board = new Board(cols, rows);

        // ── playerStart ───────────────────────────────────────────────────
        String playerStartBlock = extractBlock(json, "playerStart");
        int playerRow = parseInt(extractValue(playerStartBlock, "row"));
        int playerCol = parseInt(extractValue(playerStartBlock, "col"));
        this.playerStartPosition = new Position(playerRow, playerCol);

        // ── grid → rellena el tablero fila por fila ───────────────────────
        // Nota: las metas (3) y cajas (2) se leen directamente del grid.
        // No procesamos el array "targets" por separado porque el grid
        // ya contiene toda la información y procesarlo dos veces
        // duplicaría las goals en el Board.
        String gridArray = extractArray(json, "grid");
        CustomLinkedList<String> gridRows = extractArrayRows(gridArray);

        for (int r = 0; r < gridRows.size() && r < rows; r++) {
            CustomLinkedList<Integer> rowValues = parseIntArray(gridRows.get(r));
            for (int c = 0; c < rowValues.size() && c < cols; c++) {
                int val = rowValues.get(c);
                char cell;
                switch (val) {
                    case 1: cell = '#'; break;  // WALL
                    case 2: cell = '$'; break;  // BOX
                    case 3:                     // TARGET
                        cell = '.';
                        board.addGoal(new Position(r, c));
                        break;
                    case 4:                     // PLAYER
                        cell = '@';
                        this.playerStartPosition = new Position(r, c);
                        break;
                    case 5: cell = '*'; break;  // BOX_ON_TARGET
                    case 6: cell = '+'; break;  // PLAYER_ON_TARGET
                    default: cell = ' '; break; // FLOOR
                }
                board.setCell(r, c, cell);
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────────
    //  Utilidades de parsing manual
    // ─────────────────────────────────────────────────────────────────────

    /** Extrae el valor de un campo escalar: "key": valor  (sin comillas en el valor) */
    private String extractValue(String json, String key) {
        String search = "\"" + key + "\"";
        int idx = json.indexOf(search);
        if (idx == -1) return "";
        int colon = json.indexOf(':', idx + search.length());
        if (colon == -1) return "";
        int start = colon + 1;
        while (start < json.length() && json.charAt(start) == ' ') start++;
        // El valor termina con coma, }, ] o espacio
        int end = start;
        while (end < json.length()) {
            char ch = json.charAt(end);
            if (ch == ',' || ch == '}' || ch == ']') break;
            end++;
        }
        return json.substring(start, end).trim();
    }

    /** Extrae el valor de un campo String: "key": "valor" */
    private String extractString(String json, String key) {
        String search = "\"" + key + "\"";
        int idx = json.indexOf(search);
        if (idx == -1) return "";
        int colon = json.indexOf(':', idx + search.length());
        if (colon == -1) return "";
        int openQuote = json.indexOf('"', colon + 1);
        if (openQuote == -1) return "";
        int closeQuote = json.indexOf('"', openQuote + 1);
        if (closeQuote == -1) return "";
        return json.substring(openQuote + 1, closeQuote);
    }

    /** Extrae el bloque { ... } de un campo objeto: "key": { ... } */
    private String extractBlock(String json, String key) {
        String search = "\"" + key + "\"";
        int idx = json.indexOf(search);
        if (idx == -1) return "{}";
        int open = json.indexOf('{', idx + search.length());
        if (open == -1) return "{}";
        int depth = 0;
        int end = open;
        while (end < json.length()) {
            char ch = json.charAt(end);
            if (ch == '{') depth++;
            else if (ch == '}') { depth--; if (depth == 0) break; }
            end++;
        }
        return json.substring(open, end + 1);
    }

    /** Extrae el contenido de un campo array: "key": [ ... ] → devuelve lo que hay dentro */
    private String extractArray(String json, String key) {
        String search = "\"" + key + "\"";
        int idx = json.indexOf(search);
        if (idx == -1) return "";
        int open = json.indexOf('[', idx + search.length());
        if (open == -1) return "";
        int depth = 0;
        int end = open;
        while (end < json.length()) {
            char ch = json.charAt(end);
            if (ch == '[') depth++;
            else if (ch == ']') { depth--; if (depth == 0) break; }
            end++;
        }
        return json.substring(open + 1, end).trim();
    }

    /** Divide un array de objetos { ... }, { ... } en una lista de bloques */
    private CustomLinkedList<String> extractObjects(String arrayContent) {
        CustomLinkedList<String> result = new CustomLinkedList<>();
        int i = 0;
        while (i < arrayContent.length()) {
            if (arrayContent.charAt(i) == '{') {
                int depth = 0;
                int end = i;
                while (end < arrayContent.length()) {
                    char ch = arrayContent.charAt(end);
                    if (ch == '{') depth++;
                    else if (ch == '}') { depth--; if (depth == 0) break; }
                    end++;
                }
                result.add(arrayContent.substring(i, end + 1));
                i = end + 1;
            } else {
                i++;
            }
        }
        return result;
    }

    /** Divide un array de arrays [ [...], [...] ] en filas individuales "[...]" */
    private CustomLinkedList<String> extractArrayRows(String arrayContent) {
        CustomLinkedList<String> result = new CustomLinkedList<>();
        int i = 0;
        while (i < arrayContent.length()) {
            if (arrayContent.charAt(i) == '[') {
                int depth = 0;
                int end = i;
                while (end < arrayContent.length()) {
                    char ch = arrayContent.charAt(end);
                    if (ch == '[') depth++;
                    else if (ch == ']') { depth--; if (depth == 0) break; }
                    end++;
                }
                result.add(arrayContent.substring(i + 1, end).trim());
                i = end + 1;
            } else {
                i++;
            }
        }
        return result;
    }

    /** Parsea una fila tipo "1, 0, 2, 3" en una lista de enteros */
    private CustomLinkedList<Integer> parseIntArray(String row) {
        CustomLinkedList<Integer> result = new CustomLinkedList<>();
        String[] parts = row.split(",");
        for (String part : parts) {
            String trimmed = part.trim();
            if (!trimmed.isEmpty()) {
                try { result.add(Integer.parseInt(trimmed)); }
                catch (NumberFormatException ignored) {}
            }
        }
        return result;
    }

    /** Convierte un String a int, retorna 0 si falla */
    private int parseInt(String value) {
        try { return Integer.parseInt(value.trim()); }
        catch (NumberFormatException e) { return 0; }
    }

    // ─────────────────────────────────────────────────────────────────────
    //  Getters y Setters
    // ─────────────────────────────────────────────────────────────────────

    public int getLevelId() { return levelId; }

    public String getName() { return name; }

    public Board getBoard() { return board; }
    public void setBoard(Board board) { this.board = board; }

    public Position getPlayerStartPosition() { return playerStartPosition; }
    public void setPlayerStartPosition(Position position) { this.playerStartPosition = position; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public int getTimeLimit() { return timeLimit; }
    public void setTimeLimit(int timeLimit) { this.timeLimit = timeLimit; }
}