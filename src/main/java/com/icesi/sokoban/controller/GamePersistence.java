package com.icesi.sokoban.controller;

import com.icesi.sokoban.model.Game;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/** RF1 - Guardado y carga de la partida
 * Esta clase guarda el estado completo del objeto Game, en un archivo usando
 * objectOutputStream y lo vuelve a leer con ObjectImputStream.
 */

public class GamePersistence {

    //Archivo donde se guarda la partida al cerrar el juego
    public static final String DEFAULT_SAVE_PATH = "savegame.dat";

    /**
     * Guarda el estado del juego en el archivo indicado.
     * return true si se guardo correctamente y false si ocurrio un error
     */
    public static boolean save(Game game,File file){
        if (game == null || file == null){
            return false;
        }
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(game);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /** Carga el estado del juego desde el archivo
     * @return el juego recuperado o null si el archivo no existe o hay un error
     */
    public static Game load(File file){
        if (file == null || !file.exists()){
            return null;
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))){
            Object object = in.readObject();
            if (object instanceof Game){
                return (Game) object;
            }
            return null;
        } catch (IOException | ClassNotFoundException e){
            return null;
        }
    }

    //Guarda la partida en el archivo por defecto "savegame.dat"
    public static boolean saveGame(Game game){
        return save(game, new File(DEFAULT_SAVE_PATH));
    }

    //Carga la partida desde el archivo por defecto "savegame.dat"
    public static Game loadGame(){
        return load(new File(DEFAULT_SAVE_PATH));
    }

    //Indica si existe una partida guardada en el archivo por defecto
    public static boolean hasSavedGame(){
        return new File(DEFAULT_SAVE_PATH).exists();
    }

}


