package model;

import model.ExperienceLevel;
import model.Player;
import util.CustomLinkedList;

public class Controller {

    private CustomLinkedList<Player> registeredPlayers;

    public Controller() {
        this.registeredPlayers = new CustomLinkedList<>();
    }

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

        // Validar nombre
        if (name == null || name.trim().isEmpty()) {
            return "ERROR: El nombre no puede estar vacío";
        }

        // Validar correo electrónico
        if (email == null || !isValidEmail(email)) {
            return "ERROR: Formato de correo inválido";
        }

        // Validar username no vacío
        if (username == null || username.trim().isEmpty()) {
            return "ERROR: El username no puede estar vacío";
        }

        // Validar que el username sea único
        if (!isUniqueUsername(username)) {
            return "ERROR: El username ya existe";
        }

        // Validar avatar
        if (avatar == null || avatar.trim().isEmpty()) {
            return "ERROR: El avatar no puede estar vacío";
        }

        // Validar nivel de experiencia
        if (experienceLevel == null) {
            return "ERROR: El nivel de experiencia no puede ser nulo";
        }

        // Crear y registrar el jugador
        Player newPlayer = new Player(name, email, username, avatar, experienceLevel);
        registeredPlayers.add(newPlayer);

        return "ÉXITO: Jugador " + username + " registrado correctamente";
    }

    /**
     * Valida el formato del correo electrónico
     */
    private boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) return false;
        return email.contains("@") && email.contains(".");
    }

    /**
     * Verifica que el username sea único en la lista de jugadores registrados
     */
    private boolean isUniqueUsername(String username) {
        for (int i = 0; i < registeredPlayers.size(); i++) {
            Player player = registeredPlayers.get(i);
            if (player.getUsername().equalsIgnoreCase(username)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Retorna la lista de jugadores registrados
     */
    public CustomLinkedList<Player> getRegisteredPlayers() {
        return registeredPlayers;
    }
}
