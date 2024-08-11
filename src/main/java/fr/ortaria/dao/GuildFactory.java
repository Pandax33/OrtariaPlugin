package fr.ortaria.dao;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import fr.ortaria.Database;
import fr.ortaria.models.Guild;
import fr.ortaria.models.Player_Class;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GuildFactory {
    private static GuildFactory instance;
    private final GuildDAO guildDAO;
    private final List<Guild> cachedGuilds;

    private GuildFactory() throws SQLException {
        this.guildDAO = new GuildDAO(Database.getConnection()); // Utiliser Database pour obtenir la connexion
        this.cachedGuilds = new ArrayList<>();
        loadAllGuilds(); // Charger toutes les guildes de la BDD au démarrage
    }

    public static synchronized GuildFactory getInstance() throws SQLException {
        if (instance == null) {
            instance = new GuildFactory();
        }
        return instance;
    }

    public Guild createGuild(String nom, String suffixe, String specialisation, int niveau, int rank, float experience, String description, List<String> membres, Player_Class gamePlayer) throws SQLException {
        String membresJSON = new Gson().toJson(membres);  // Convert list of UUIDs to JSON string
        Guild guild = new Guild(0, nom, suffixe, specialisation, niveau, rank, experience, description, membres);
        guildDAO.insertGuild(guild);  // Persist the new guild into database
        cachedGuilds.add(guild);  // Add to local cache

        gamePlayer.setId_guilde(guild.getId());
        gamePlayer.setRank_guilde(1);
        System.out.println("Player guild id: " + gamePlayer.getPseudo());
        PlayerFactory.getInstance().updatePlayer(gamePlayer.getUuid());
        return guild;
    }

    public void removeGuild(int guildId) throws SQLException {
        guildDAO.removeGuild(guildId);
        // Remove the guild from the cached list
        cachedGuilds.removeIf(guild -> guild.getId() == guildId);
    }

    public Optional<Guild> getGuild(int id) {
        return cachedGuilds.stream()
                .filter(guild -> guild.getId() == id)
                .findFirst();
    }

    public List<Guild> getAllGuilds() {
        return new ArrayList<>(cachedGuilds);  // Return a copy of the cached guilds
    }
    public boolean guildNameExists(String name) {
        return cachedGuilds.stream().anyMatch(guild -> guild.getNom().equalsIgnoreCase(name));
    }
    public boolean guildTagExists(String tag) {
        return cachedGuilds.stream().anyMatch(guild -> guild.getSuffixe().equalsIgnoreCase(tag));
    }
    private void loadAllGuilds() throws SQLException {
        List<Guild> loadedGuilds = guildDAO.getAllGuilds();
        cachedGuilds.clear();
        cachedGuilds.addAll(loadedGuilds);  // Refresh the cache with the loaded guilds
    }
    public void updateGuild(Guild guild) throws SQLException {
        guildDAO.updateGuild(guild);
    }

    public void updateAllGuilds() throws SQLException {
        for (Guild guild : cachedGuilds) {
            updateGuild(guild);
        }
    }

    public void leaveGuild(int guildId, String playerUuid) throws SQLException {
        // Récupérer la guilde comme un Optional
        Optional<Guild> optionalGuild = getGuild(guildId);

        if (!optionalGuild.isPresent()) {
            throw new SQLException("Guild not found.");
        }

        Guild guild = optionalGuild.get();

        // Enlever le joueur de la liste des membres
        List<String> members = guild.getMembres();
        System.out.println("Members: " + members);
        if (members.remove(playerUuid)) {  // Retourne true si le joueur était présent et a été retiré
            updateGuild(guild);  // Mettre à jour la guilde dans la base de données
        } else {
            throw new SQLException("Player not found in guild members.");
        }
    }



}
