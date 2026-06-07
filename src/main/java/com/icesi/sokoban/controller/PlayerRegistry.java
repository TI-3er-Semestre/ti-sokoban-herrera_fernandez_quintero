package com.icesi.sokoban.controller;

import com.icesi.sokoban.model.Player;
import com.icesi.sokoban.model.ExperienceLevel;
import com.icesi.sokoban.structure.CustomLinkedList;

public class PlayerRegistry {

    // ── Singleton ─────────────────────────────────────────────────────────
    // Una sola instancia compartida entre todos los controladores.
    // PlayerRegistrationController registra jugadores aquí,
    // y GameController consulta quién está activo para guardar estadísticas.
    private static final PlayerRegistry INSTANCE = new PlayerRegistry();

    public static PlayerRegistry getInstance() {
        return INSTANCE;
    }

    private CustomLinkedList<Player> registeredPlayers;

    // El jugador que está jugando actualmente
    private Player activePlayer;

    private PlayerRegistry() {
        this.registeredPlayers = new CustomLinkedList<>();
    }

    public Player getActivePlayer() { return activePlayer; }
    public void setActivePlayer(Player player) { this.activePlayer = player; }

    /**
     * RF1 - Registrar jugador
     * @pre name != null && !name.isEmpty()
     * @pre email != null && isValidEmail(email)
     * @pre username != null && !username.isEmpty() && isUniqueUsername(username)
     * @pre avatar != null && !avatar.isEmpty()
     * @pre experienceLevel != null
     * @post El jugador queda registrado en el sistema
     */
    public String registerPlayer(String name, String email, String username,
                                 String avatar, ExperienceLevel experienceLevel) {

        if (name == null || name.trim().isEmpty()) {
            return "ERROR: El nombre no puede estar vacío";
        }
        if (email == null || !isValidEmail(email)) {
            return "ERROR: Formato de correo inválido";
        }
        if (username == null || username.trim().isEmpty()) {
            return "ERROR: El username no puede estar vacío";
        }
        if (!isUniqueUsername(username)) {
            return "ERROR: El username ya existe";
        }
        if (avatar == null || avatar.trim().isEmpty()) {
            return "ERROR: El avatar no puede estar vacío";
        }
        if (experienceLevel == null) {
            return "ERROR: El nivel de experiencia no puede ser nulo";
        }

        Player newPlayer = new Player(name, email, username, avatar, experienceLevel);
        registeredPlayers.add(newPlayer);

        // El primer jugador registrado queda activo automáticamente
        if (activePlayer == null) {
            activePlayer = newPlayer;
        }

        return "ÉXITO: Jugador " + username + " registrado correctamente";
    }

    private boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) return false;
        return email.contains("@") && email.contains(".");
    }

    private boolean isUniqueUsername(String username) {
        for (int i = 0; i < registeredPlayers.size(); i++) {
            Player player = registeredPlayers.get(i);
            if (player.getUsername().equalsIgnoreCase(username)) {
                return false;
            }
        }
        return true;
    }

    public CustomLinkedList<Player> getRegisteredPlayers() {
        return registeredPlayers;
    }
}