package com.icesi.sokoban.controller;

import com.icesi.sokoban.model.Board;
import com.icesi.sokoban.model.Box;
import com.icesi.sokoban.model.Direction;
import com.icesi.sokoban.model.Game;
import com.icesi.sokoban.model.Level;
import com.icesi.sokoban.model.Position;
import com.icesi.sokoban.structure.CustomLinkedList;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;

/**
 * Pruebas de guardado y carga de la partida
 */
public class GamePersistenceTest {

    private Game construirJuego(){
        Board board = new Board(6,5);
        for (int c = 0; c < 6; c++) {
            board.setCell(0, c, '#');
            board.setCell(4, c, '#');
        }
        for (int r = 0; r < 5; r++){
            board.setCell(r,0,'#');
            board.setCell(r,5,'#');
        }
        board.setCell(2,3,'$');
        board.setCell(2,4,'.');
        board.addGoal(new Position(2,4));

        Level level = new Level(1, "Nivel persistencia");
        level.setBoard(board);
        level.setPlayerStartPosition(new Position(2,2));

        Game game = new Game();
        game.loadLevel(level);
        return game;
    }

    @Test
    public void saveAndLoad_PartidaNueva_ConservaContadoresYPosiciones() throws IOException{
        Game original = construirJuego();
        original.move(Direction.RIGHT);

        File archivo = File.createTempFile("sokoban_test",".dat");
        archivo.deleteOnExit();

        boolean guardado = GamePersistence.save(original,archivo);
        assertTrue(guardado, "El guardado debe ser exitoso");

        Game cargado = GamePersistence.load(archivo);
        assertNotNull(cargado, "El juego cargado no debe ser null");

        assertEquals(original.getMoveCount(), cargado.getMoveCount());
        assertEquals(original.getPushCount(), cargado.getPushCount());
        assertEquals(original.getState(), cargado.getState());
        assertEquals(original.getPlayer().getPosition(), cargado.getPlayer().getPosition());

        archivo.delete();
    }

    @Test
    public void saveAndLoad_ConMovimientosConservaPosicionCaja() throws IOException{
        Game original = construirJuego();
        original.move(Direction.RIGHT);

        File archivo = File.createTempFile("sokoban_test", ".dat");
        archivo.deleteOnExit();

        GamePersistence.save(original, archivo);
        Game cargado = GamePersistence.load(archivo);
        assertNotNull(cargado);

        CustomLinkedList<Box> cajasOriginal = original.getBoxes();
        CustomLinkedList<Box> cajasCargado = cargado.getBoxes();
        assertEquals(cajasOriginal.size(), cajasCargado.size());

        for (int i = 0; i < cajasOriginal.size(); i++){
            assertEquals(cajasOriginal.get(i).getPosition(), cajasCargado.get(i).getPosition());
        }
        archivo.delete();
    }

    @Test
    public void load_ArchivoInexistente_RetornaNull(){
        File archivo = new File("no_existe_esta_partida_12345.dat");
        assertNull(GamePersistence.load(archivo), "Cargar un archivo inexistente debe retornar null");
    }
}
