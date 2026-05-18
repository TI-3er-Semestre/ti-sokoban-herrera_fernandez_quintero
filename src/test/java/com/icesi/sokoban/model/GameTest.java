package test;

import model.*;
import org.junit.jupiter.api.*;
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

    @Test
    public void testPlayerMovement_MoveRight_UpdatesPositionCorrectly() {
        setUpBasicLevel();
        // jugador (1,1) -> (1,2) hay caja, intenta empujar
        // mejor moverlo desde una posición sin caja inmediata
        // Como (1,1) tiene caja en (1,2), un movimiento RIGHT empuja la caja
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
    public void testPushBox_IntoEmptySpace_Success() {
        setUpBasicLevel();
        boolean pushed = game.move(Direction.RIGHT);
        assertTrue(pushed);
        assertEquals(3, game.getBoxes().get(0).getPosition().getColumn());
    }

    @Test
    public void testPushBox_AgainstWall_ReturnsFalse() {
        setUpBasicLevel();
        game.move(Direction.RIGHT);
        game.move(Direction.RIGHT);
        game.move(Direction.RIGHT);
        boolean pushed = game.move(Direction.RIGHT);
        assertFalse(pushed);
        assertEquals(4, game.getBoxes().get(0).getPosition().getColumn());
    }

    @Test
    public void testPushBox_AgainstAnotherBox_ReturnsFalse() {
        setUpTwoBoxesLevel();
        boolean pushed = game.move(Direction.RIGHT);
        assertFalse(pushed);
        assertEquals(2, game.getBoxes().get(0).getPosition().getColumn());
    }

    @Test
    public void testPushBox_OntoGoal_SetsBoxOnGoal() {
        setUpBasicLevel();
        game.move(Direction.RIGHT); // caja (1,3)
        game.move(Direction.RIGHT); // caja (1,4) goal
        assertTrue(game.getBoxes().get(0).isOnGoal());
    }

    @Test
    public void testVictoryCondition_AllBoxesOnGoals_ReturnsTrue() {
        setUpBasicLevel();
        game.move(Direction.RIGHT);
        game.move(Direction.RIGHT);
        assertTrue(game.isLevelComplete());
        assertEquals(GameStatus.WON, game.getState());
    }

    @Test
    public void testLoadLevel_FromJSON_LoadsCorrectly() {
        setUpBasicLevel();
        assertNotNull(game.getBoard());
        assertNotNull(game.getPlayer());
        assertFalse(game.getBoxes().isEmpty());
        assertEquals(0, game.getMoveCount());
    }

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
        game.move(Direction.RIGHT); // caja (1,3), jugador (1,2)
        game.undo();
        assertEquals(2, game.getBoxes().get(0).getPosition().getColumn());
        assertEquals(1, game.getPlayer().getPosition().getColumn());
    }

    @Test
    public void testMoveCounter_ValidMoves_IncrementsCorrectly() {
        setUpBasicLevel();
        game.move(Direction.RIGHT);
        game.move(Direction.RIGHT);
        assertEquals(2, game.getMoveCount());
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
}