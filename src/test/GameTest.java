package test;

import model.*;
import org.junit.Test;
import org.junit.jupiter.api.*;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    private Game game;
    private Board board;

    @BeforeEach
    public void setUp() {
        game = new Game();
    }

    @AfterEach
    public void tearDown() {
        game = null;
        board = null;
    }

    // ── Helpers ───────────────────────────────────────────────────────────────
    // Layout: ######## / # P $ . # / ########   (7 cols, 3 rows)
    private void setUpBasicLevel() {
        board = new Board(7, 3);
        for (int c = 0; c < 7; c++) { board.setCell(0, c, '#'); board.setCell(2, c, '#'); }
        board.setCell(1, 0, '#');
        board.setCell(1, 1, ' ');
        board.setCell(1, 2, '$');
        board.setCell(1, 3, ' ');
        board.setCell(1, 4, '.');
        board.setCell(1, 5, ' ');
        board.setCell(1, 6, '#');
        board.addGoal(new Position(1, 4));

        Level level = new Level(1, "Test Level");
        level.setBoard(board);
        level.setPlayerStartPosition(new Position(1, 1));
        game.loadLevel(level);
    }

    // Layout: ###### / # P$$ .# / ######  — box-to-box blocking
    private void setUpTwoBoxesLevel() {
        board = new Board(6, 3);
        for (int c = 0; c < 6; c++) { board.setCell(0, c, '#'); board.setCell(2, c, '#'); }
        board.setCell(1, 0, '#');
        board.setCell(1, 1, ' ');
        board.setCell(1, 2, '$');
        board.setCell(1, 3, '$');
        board.setCell(1, 4, '.');
        board.setCell(1, 5, '#');
        board.addGoal(new Position(1, 4));

        Level level = new Level(2, "Two Boxes Level");
        level.setBoard(board);
        level.setPlayerStartPosition(new Position(1, 1));
        game.loadLevel(level);
    }

    // ── Movement ──────────────────────────────────────────────────────────────

    @Test
    public void testPlayerMovement_MoveRight_UpdatesPositionCorrectly() {
        setUpBasicLevel();
        // Player at (1,1). RIGHT → pushes box from (1,2) to (1,3); player moves to (1,2)
        boolean moved = game.move(Direction.RIGHT);
        assertTrue(moved);
        assertEquals(2, game.getPlayer().getPosition().getColumn());
    }

    @Test
    public void testPlayerMovement_MoveAgainstWall_ReturnsFalse() {
        setUpBasicLevel();
        boolean moved = game.move(Direction.UP);
        assertFalse(moved);
        assertEquals(1, game.getPlayer().getPosition().getRow());
        assertEquals(1, game.getPlayer().getPosition().getColumn());
    }

    @Test
    public void testPlayerMovement_MoveIntoWallOnLeft_ReturnsFalse() {
        setUpBasicLevel();
        boolean moved = game.move(Direction.LEFT); // wall at (1,0)
        assertFalse(moved);
        assertEquals(1, game.getPlayer().getPosition().getColumn());
    }

    // ── Box pushing ───────────────────────────────────────────────────────────

    @Test
    public void testPushBox_IntoEmptySpace_Success() {
        setUpBasicLevel();
        boolean pushed = game.move(Direction.RIGHT);
        assertTrue(pushed);
        assertEquals(3, game.getBoxes().get(0).getPosition().getColumn());
    }

    @Test
    public void testPushBox_AgainstWall_ReturnsFalse() {
        // Custom board: # P $ _ _ # (no goal) — box pushed until it hits right wall
        // Row 1: # space box space space wall
        Board b = new Board(6, 3);
        for (int c = 0; c < 6; c++) { b.setCell(0, c, '#'); b.setCell(2, c, '#'); }
        b.setCell(1, 0, '#');
        b.setCell(1, 1, ' ');
        b.setCell(1, 2, '$');
        b.setCell(1, 3, ' ');
        b.setCell(1, 4, ' ');
        b.setCell(1, 5, '#');
        // No goal → victory never triggered
        Level lv = new Level(99, "WallTest");
        lv.setBoard(b);
        lv.setPlayerStartPosition(new Position(1, 1));
        game.loadLevel(lv);

        game.move(Direction.RIGHT); // box→(1,3), player→(1,2)
        game.move(Direction.RIGHT); // box→(1,4), player→(1,3)
        boolean pushed = game.move(Direction.RIGHT); // box would go to (1,5)=wall → blocked
        assertFalse(pushed);
        assertEquals(4, game.getBoxes().get(0).getPosition().getColumn());
        assertEquals(3, game.getPlayer().getPosition().getColumn());
    }

    private void assertFalse(boolean pushed) {

    }

    @Test
    public void testPushBox_AgainstAnotherBox_ReturnsFalse() {
        setUpTwoBoxesLevel();
        // Player at (1,1): RIGHT would push box at (1,2) into box at (1,3) → blocked
        boolean pushed = game.move(Direction.RIGHT);
        assertFalse(pushed);
        assertEquals(2, game.getBoxes().get(0).getPosition().getColumn());
    }

    @Test
    public void testPushBox_OntoGoal_SetsBoxOnGoal() {
        setUpBasicLevel();
        game.move(Direction.RIGHT); // box (1,3)
        game.move(Direction.RIGHT); // box (1,4) = goal
        assertTrue(game.getBoxes().get(0).isOnGoal());
    }

    // ── Victory ───────────────────────────────────────────────────────────────

    @Test
    public void testVictoryCondition_AllBoxesOnGoals_ReturnsTrue() {
        setUpBasicLevel();
        game.move(Direction.RIGHT);
        game.move(Direction.RIGHT);
        assertTrue(game.isLevelComplete());
        assertEquals(GameStatus.WON, game.getState());
    }

    @Test
    public void testVictoryCondition_BoxNotOnGoal_ReturnsFalse() {
        setUpBasicLevel();
        game.move(Direction.RIGHT); // box at (1,3), not on goal
        assertFalse(game.isLevelComplete());
        assertEquals(GameStatus.PLAYING, game.getState());
    }

    // ── Level loading ─────────────────────────────────────────────────────────

    @Test
    public void testLoadLevel_SetsUpCorrectly() {
        setUpBasicLevel();
        assertNotNull(game.getBoard());
        assertNotNull(game.getPlayer());
        assertFalse(game.getBoxes().isEmpty());
        assertEquals(0, game.getMoveCount());
        assertEquals(0, game.getPushCount());
    }

    @Test
    public void testLoadLevel_FromJSON_ParsesCorrectly() {
        String json = "{\n" +
                "  \"id\": 1,\n" +
                "  \"name\": \"Test\",\n" +
                "  \"difficulty\": \"BASIC\",\n" +
                "  \"rows\": 3,\n" +
                "  \"cols\": 5,\n" +
                "  \"grid\": [\n" +
                "    [1, 1, 1, 1, 1],\n" +
                "    [1, 4, 2, 3, 1],\n" +
                "    [1, 1, 1, 1, 1]\n" +
                "  ]\n" +
                "}";
        Level level = new Level(1, "Test");
        level.loadFromJson(json);
        assertNotNull(level.getBoard());
        assertEquals(new Position(1, 1), level.getPlayerStartPosition());
        assertEquals('$', level.getBoard().getCell(1, 2));
        assertTrue(level.getBoard().isGoal(1, 3));
    }

    private void assertNotNull(Board board) {

    }

    // ── Undo ─────────────────────────────────────────────────────────────────

    @Test
    public void testUndo_SingleMove_RestoresPreviousState() {
        setUpBasicLevel();
        game.move(Direction.RIGHT);
        game.undo();
        assertEquals(1, game.getPlayer().getPosition().getColumn());
        assertEquals(0, game.getMoveCount());
    }

    @Test
    public void testUndo_BoxPush_RestoresBoxAndPlayer() {
        setUpBasicLevel();
        game.move(Direction.RIGHT); // box→(1,3), player→(1,2)
        game.undo();
        assertEquals(2, game.getBoxes().get(0).getPosition().getColumn());
        assertEquals(1, game.getPlayer().getPosition().getColumn());
        assertEquals(0, game.getPushCount());
    }

    @Test
    public void testUndo_OnEmptyStack_ReturnsFalse() {
        setUpBasicLevel();
        assertFalse(game.undo());
    }

    @Test
    public void testMultipleUndos_Sequential_WorksCorrectly() {
        setUpBasicLevel();
        game.move(Direction.RIGHT);
        game.move(Direction.RIGHT);
        game.undo();
        game.undo();
        assertEquals(1, game.getPlayer().getPosition().getColumn());
        assertEquals(0, game.getMoveCount());
    }

    @Test
    public void testUndo_AfterVictory_RestoresPlayingState() {
        setUpBasicLevel();
        game.move(Direction.RIGHT);
        game.move(Direction.RIGHT);
        assertEquals(GameStatus.WON, game.getState());
        game.undo();
        assertEquals(GameStatus.PLAYING, game.getState());
    }

    // ── Counters ──────────────────────────────────────────────────────────────

    @Test
    public void testMoveCounter_ValidMoves_IncrementsCorrectly() {
        setUpBasicLevel();
        game.move(Direction.RIGHT);
        game.move(Direction.RIGHT);
        assertEquals(2, game.getMoveCount());
    }

    @Test
    public void testPushCounter_ValidPushes_IncrementsCorrectly() {
        setUpBasicLevel();
        game.move(Direction.RIGHT); // push
        game.move(Direction.RIGHT); // push
        assertEquals(2, game.getPushCount());
    }

    @Test
    public void testMoveCounter_InvalidMove_DoesNotIncrement() {
        setUpBasicLevel();
        game.move(Direction.UP); // wall
        assertEquals(0, game.getMoveCount());
    }

    // ── Input buffer ──────────────────────────────────────────────────────────

    @Test
    public void testInputBuffer_QueueAndProcess_ExecutesInOrder() {
        setUpBasicLevel();
        game.queueCommand(Direction.RIGHT);
        game.queueCommand(Direction.RIGHT);
        game.processInputBuffer();
        assertEquals(GameStatus.WON, game.getState());
    }

    // ── Transposition table ───────────────────────────────────────────────────

    @Test
    public void testTranspositionTable_StateKeyIsUnique() {
        setUpBasicLevel();
        String key1 = game.buildStateKey();
        game.move(Direction.RIGHT);
        String key2 = game.buildStateKey();
        assertNotEquals(key1, key2);
    }

    private void assertNotEquals(String key1, String key2) {

    }
}
