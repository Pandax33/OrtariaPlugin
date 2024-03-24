package fr.ortaria.dao;

import fr.ortaria.models.Player;
import org.bukkit.event.Listener;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PlayerFactory {

    private static PlayerFactory instance;
    private final PlayerDAO playerDAO;
    private final List<Player> players; // Liste en mémoire des joueurs

    private PlayerFactory(Connection connection) throws SQLException {
        this.playerDAO = new PlayerDAO(connection);
        this.players = new ArrayList<>();
        loadAllPlayers(); // Charger tous les joueurs de la BDD au démarrage
    }

    // Méthode pour obtenir l'instance unique de la classe.
    public static synchronized PlayerFactory getInstance(Connection connection) throws SQLException {
        if (instance == null) {
            instance = new PlayerFactory(connection);
        }
        return instance;
    }

    public Player createPlayer(String uuid, String pseudo, Timestamp dateFirstConnexion, String destin,
                               String classe, String guilde, int niveauAventure, double argent, int pointsTdr, String grade,
                               int niveauAlchimiste, int niveauArcaniste, int niveauArcheologue, int niveauCuisinier,
                               int niveauForgeron, int niveauPelleteur, int niveauBucheron, int niveauMineur) {
        // Créer un nouveau joueur
        Player player = new Player(
                uuid, pseudo, dateFirstConnexion, destin, classe, guilde, niveauAventure, argent,
                pointsTdr, grade, niveauAlchimiste, niveauArcaniste, niveauArcheologue, niveauCuisinier,
                niveauForgeron, niveauPelleteur, niveauBucheron, niveauMineur
        );

        // Insérer le joueur dans la base de données
        try {
            playerDAO.insertPlayer(player);
            players.add(player); // Ajouter le joueur à la liste en mémoire
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer l'exception ou la propager
        }

        return player;
    }

    public Optional<Player> getPlayer(String uuid) {
        return players.stream().filter(player -> player.getUuid().equals(uuid)).findFirst();
    }

    private void loadAllPlayers() throws SQLException {
        List<Player> loadedPlayers = playerDAO.getAllPlayers();
        players.clear();
        players.addAll(loadedPlayers);
    }

    public List<Player> getAllPlayers() {
        return new ArrayList<>(players);
    }
}