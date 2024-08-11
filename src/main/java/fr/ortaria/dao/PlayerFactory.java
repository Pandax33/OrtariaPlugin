package fr.ortaria.dao;

import fr.ortaria.Database;
import fr.ortaria.models.Player_Class;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PlayerFactory {

    private static PlayerFactory instance;
    private final PlayerDAO playerDAO;
    private final List<Player_Class> players; // Liste en mémoire des joueurs

    private PlayerFactory() throws SQLException {
        this.playerDAO = new PlayerDAO(Database.getConnection()); // Utiliser Database pour obtenir la connexion
        this.players = new ArrayList<>();
        loadAllPlayers(); // Charger tous les joueurs de la BDD au démarrage
    }

    public static synchronized PlayerFactory getInstance() throws SQLException {
        if (instance == null) {
            instance = new PlayerFactory();
        }
        return instance;
    }


    public Player_Class createPlayer(String uuid, String pseudo, Timestamp dateFirstConnexion, String destin, String classe, String guilde, int niveauAventure, double argent, int pointsTdr, String grade, int niveauAlchimiste, int niveauArcaniste, int niveauArcheologue, int niveauCuisinier, int niveauForgeron, int niveauPelleteur, int niveauBucheron, int niveauMineur, int faveur_astrale, int recharge_astrale, int vitesse, int PV, int force_joueur, int id_guilde, int rank_guilde){
        // Créer un nouveau joueur
        Player_Class player = new Player_Class(
                uuid, pseudo, dateFirstConnexion, destin, classe, guilde, niveauAventure, argent,
                pointsTdr, grade, niveauAlchimiste, niveauArcaniste, niveauArcheologue, niveauCuisinier,
                niveauForgeron, niveauPelleteur, niveauBucheron, niveauMineur, faveur_astrale, recharge_astrale, vitesse, PV, force_joueur, id_guilde, rank_guilde
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

    public Optional<Player_Class> getPlayer(String uuid) {
        return players.stream().filter(player -> player.getUuid().equals(uuid)).findFirst();
    }

    public void resetGuildInfoInPlayers(List<String> memberUuids) throws SQLException {
        for (Player_Class player : players) {
            if (memberUuids.contains(player.getUuid())) {
                player.setId_guilde(0);
                player.setRank_guilde(0);
                updatePlayer(player.getUuid());
            }
        }
    }
    private void loadAllPlayers() throws SQLException {
        List<Player_Class> loadedPlayers = playerDAO.getAllPlayers();
        players.clear();
        players.addAll(loadedPlayers);
    }
    public void updatePlayer(String uuid) throws SQLException {
        getPlayer(uuid).ifPresent(player -> {
            try {
                playerDAO.updatePlayer(player);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
    public void updateAllPlayers() throws SQLException {
        playerDAO.updateAllPlayers(players);
    }

    public List<Player_Class> getAllPlayers() {
        return new ArrayList<>(players);
    }
}