package test;

import model.*;
import org.junit.*;
import static org.junit.Assert.*;

public class GameTest {

    private Game game;
    private Board board;

    @Before
    public void setUp() {
        game = new Game();
    }

    @After
    public void tearDown() {
        game = null;
        board = null;
    }

    // Escenario básico: tablero simple con jugador, caja y objetivo
    private void setUpBasicLevel() {
        board = new Board(5, 3);

        board.setCell(0, 0, '#'); board.setCell(0, 1, '#');
        board.setCell(0, 2, '#'); board.setCell(0, 3, '#');
        board.setCell(0, 4, '#');

        board.setCell(1, 0, '#'); board.setCell(1, 1, ' ');
        board.setCell(1, 2, '$'); board.setCell(1, 3, '.');
        board.setCell(1, 4, '#');
        board.addGoal(new Position(1, 3));

        board.setCell(2, 0, '#'); board.setCell(2, 1, '#');
        board.setCell(2, 2, '#'); board.setCell(2, 3, '#');
        board.setCell(2, 4, '#');

        Level level = new Level(1, "Test Level");
        level.setBoard(board);
        level.setPlayerStartPosition(new Position(1, 1));
        game.loadLevel(level);
    }

    // Escenario con dos cajas: jugador en (1,1), caja en (1,2), caja en (1,3)
    private void setUpTwoBoxesLevel() {
        board = new Board(6, 3);

        board.setCell(0, 0, '#'); board.setCell(0, 1, '#');
        board.setCell(0, 2, '#'); board.setCell(0, 3, '#');
        board.setCell(0, 4, '#'); board.setCell(0, 5, '#');

        board.setCell(1, 0, '#'); board.setCell(1, 1, ' ');
        board.setCell(1, 2, '$'); board.setCell(1, 3, '$');
        board.setCell(1, 4, '.'); board.setCell(1, 5, '#');
        board.addGoal(new Position(1, 4));

        board.setCell(2, 0, '#'); board.setCell(2, 1, '#');
        board.setCell(2, 2, '#'); board.setCell(2, 3, '#');
        board.setCell(2, 4, '#'); board.setCell(2, 5, '#');

        Level level = new Level(2, "Two Boxes Level");
        level.setBoard(board);
        level.setPlayerStartPosition(new Position(1, 1));
        game.loadLevel(level);
    }

    // Verifica que el jugador se mueve correctamente hacia la derecha
    @Test
    public void testPlayerMovement_MoveRight_UpdatesPositionCorrectly() {
        // Arrange
        setUpBasicLevel();

        // Act
        boolean moved = game.move(Direction.RIGHT);

        // Assert
        assertTrue("El jugador debe poder moverse a la derecha", moved);
        assertEquals("La fila del jugador debe ser 1", 1, game.getPlayer().getPosition().getRow());
        assertEquals("La columna del jugador debe ser 2", 2, game.getPlayer().getPosition().getColumn());
    }

    // Verifica que el jugador no puede moverse contra un muro
    @Test
    public void testPlayerMovement_MoveAgainstWall_ReturnsFalse() {
        // Arrange
        setUpBasicLevel();

        // Act
        boolean moved = game.move(Direction.UP);

        // Assert
        assertFalse("El jugador no debe poder moverse contra un muro", moved);
        assertEquals("La fila no debe cambiar", 1, game.getPlayer().getPosition().getRow());
        assertEquals("La columna no debe cambiar", 1, game.getPlayer().getPosition().getColumn());
    }

    // Verifica que una caja se empuja correctamente
    @Test
    public void testPushBox_IntoEmptySpace_Success() {
        // Arrange
        setUpBasicLevel();
        game.move(Direction.RIGHT);

        // Act
        boolean pushed = game.move(Direction.RIGHT);

        // Assert
        assertTrue("La caja debe poder ser empujada", pushed);
        assertEquals("La caja debe estar en columna 3", 3, game.getBoxes().get(0).getPosition().getColumn());
    }

    // Verifica que una caja no se puede empujar contra un muro
    @Test
    public void testPushBox_AgainstWall_ReturnsFalse() {
        // Arrange
        setUpBasicLevel();
        game.move(Direction.RIGHT);
        game.move(Direction.RIGHT);

        // Act
        boolean pushed = game.move(Direction.RIGHT);

        // Assert
        assertFalse("La caja no debe poder ser empujada contra un muro", pushed);
        assertEquals("La caja debe seguir en columna 3", 3, game.getBoxes().get(0).getPosition().getColumn());
    }

    // Verifica que una caja no se puede empujar contra otra caja
    @Test
    public void testPushBox_AgainstAnotherBox_ReturnsFalse() {
        // Arrange
        setUpTwoBoxesLevel();
        game.move(Direction.RIGHT);

        // Act
        boolean pushed = game.move(Direction.RIGHT);

        // Assert
        assertFalse("La caja no debe poder ser empujada contra otra caja", pushed);
        assertEquals("La caja debe seguir en columna 2", 2, game.getBoxes().get(0).getPosition().getColumn());
    }

    // Verifica que la caja queda marcada como en objetivo
    @Test
    public void testPushBox_OntoGoal_SetsBoxOnGoal() {
        // Arrange
        setUpBasicLevel();
        game.move(Direction.RIGHT);

        // Act
        game.move(Direction.RIGHT);

        // Assert
        assertTrue("La caja debe estar marcada como en objetivo", game.getBoxes().get(0).isOnGoal());
    }

    // Verifica que el nivel se completa cuando todas las cajas están en objetivos
    @Test
    public void testVictoryCondition_AllBoxesOnGoals_ReturnsTrue() {
        // Arrange
        setUpBasicLevel();
        game.move(Direction.RIGHT);

        // Act
        game.move(Direction.RIGHT);

        // Assert
        assertTrue("El nivel debe estar completo", game.isLevelComplete());
        assertEquals("El estado debe ser WON", GameStatus.WON, game.getState());
    }

    // Verifica que el nivel carga correctamente
    @Test
    public void testLoadLevel_FromJSON_LoadsCorrectly() {
        // Arrange & Act
        setUpBasicLevel();

        // Assert
        assertNotNull("El tablero no debe ser nulo", game.getBoard());
        assertNotNull("El jugador no debe ser nulo", game.getPlayer());
        assertFalse("Las cajas no deben estar vacías", game.getBoxes().isEmpty());
        assertEquals("El contador de movimientos debe ser 0", 0, game.getMoveCount());
    }

    // Verifica que el undo restaura el estado anterior
    @Test
    public void testUndo_SingleMove_RestoresPreviousState() {
        // Arrange
        setUpBasicLevel();
        game.move(Direction.RIGHT);

        // Act
        game.undo();

        // Assert
        assertEquals("La columna del jugador debe volver a 1", 1, game.getPlayer().getPosition().getColumn());
        assertEquals("El contador de movimientos debe ser 0", 0, game.getMoveCount());
    }

    // Verifica que el undo restaura la caja y el jugador
    @Test
    public void testUndo_BoxPush_RestoresBoxAndPlayer() {
        // Arrange
        setUpBasicLevel();
        game.move(Direction.RIGHT);
        game.move(Direction.RIGHT);

        // Act
        game.undo();

        // Assert
        assertEquals("La caja debe volver a columna 2", 2, game.getBoxes().get(0).getPosition().getColumn());
        assertEquals("El jugador debe volver a columna 1", 1, game.getPlayer().getPosition().getColumn());
    }

    // Verifica que el contador de movimientos incrementa correctamente
    @Test
    public void testMoveCounter_ValidMoves_IncrementsCorrectly() {
        // Arrange
        setUpBasicLevel();

        // Act
        game.move(Direction.RIGHT);
        game.move(Direction.RIGHT);

        // Assert
        assertEquals("El contador debe ser 2", 2, game.getMoveCount());
    }

    // Verifica que múltiples undos funcionan correctamente
    @Test
    public void testMultipleUndos_Sequential_WorksCorrectly() {
        // Arrange
        setUpBasicLevel();
        game.move(Direction.RIGHT);
        game.move(Direction.RIGHT);

        // Act
        game.undo();
        game.undo();

        // Assert
        assertEquals("El jugador debe volver a la posición inicial", 1, game.getPlayer().getPosition().getColumn());
        assertEquals("El contador de movimientos debe ser 0", 0, game.getMoveCount());
    }
}