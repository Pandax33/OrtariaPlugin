package fr.ortaria.dao;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import fr.ortaria.models.Guild;
import fr.ortaria.models.Player_Class;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GuildDAO {
    private final Connection connection;

    public GuildDAO(Connection connection) {
        this.connection = connection;
    }

    public void insertGuild(Guild guild) throws SQLException {
        String sql = "INSERT INTO guildes (nom, suffixe, specialisation, niveau, rank_guilde, experience, description, membres) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        try {
            stmt.setString(1, guild.getNom());
            stmt.setString(2, guild.getSuffixe());
            stmt.setString(3, guild.getSpecialisation());
            stmt.setInt(4, guild.getNiveau());
            stmt.setInt(5, guild.getRank());
            stmt.setFloat(6, guild.getExperience());
            stmt.setString(7, guild.getDescription());
            stmt.setString(8, guild.getMembresJSON());  // Convert list of UUIDs to JSON string
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating guild failed, no rows affected.");
            }
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    guild.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating guild failed, no ID obtained.");
                }
            }
        } finally {
            stmt.close();
        }
    }

    public void removeGuild(int guildId) throws SQLException {
        connection.setAutoCommit(false);
        try {
            // Récupérer la guilde
            Guild guild = getGuild(guildId);
            if (guild == null) {
                throw new SQLException("Guild not found with ID: " + guildId);
            }

            List<String> memberUuids = guild.getMembres(); // Liste des UUIDs des membres

            // Réinitialiser les informations de guilde pour les joueurs en mémoire
            PlayerFactory.getInstance().resetGuildInfoInPlayers(memberUuids);

            // Suppression de la guilde de la base de données
            String sqlDeleteGuild = "DELETE FROM guildes WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sqlDeleteGuild)) {
                stmt.setInt(1, guildId);
                stmt.executeUpdate();
            }

            // Validation de la transaction
            connection.commit();
        } catch (SQLException e) {
            // Annulation des modifications en cas d'erreur
            connection.rollback();
            throw e;
        } finally {
            // Réinitialisation du mode de commit automatique
            connection.setAutoCommit(true);
        }
    }


    public Guild getGuild(int id) throws SQLException {
        String sql = "SELECT * FROM guildes WHERE id = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        Guild guild = null;

        try {
            stmt.setInt(1, id);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                String jsonMembers = resultSet.getString("membres");  // Retrieve JSON string of members
                List<String> members = new Gson().fromJson(jsonMembers, new TypeToken<List<String>>(){}.getType());

                guild = new Guild(
                        resultSet.getInt("id"),
                        resultSet.getString("nom"),
                        resultSet.getString("suffixe"),
                        resultSet.getString("specialisation"),
                        resultSet.getInt("niveau"),
                        resultSet.getInt("rank_guilde"),
                        resultSet.getFloat("experience"),
                        resultSet.getString("description"),
                        members
                );
            }
        } finally {
            stmt.close();
        }

        return guild;
    }

    public List<Guild> getAllGuilds() throws SQLException {
        List<Guild> guilds = new ArrayList<>();
        String sql = "SELECT * FROM guildes";
        PreparedStatement stmt = connection.prepareStatement(sql);

        try {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String jsonMembers = rs.getString("membres");
                List<String> members = new Gson().fromJson(jsonMembers, new TypeToken<List<String>>(){}.getType());

                Guild guild = new Guild(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("suffixe"),
                        rs.getString("specialisation"),
                        rs.getInt("niveau"),
                        rs.getInt("rank_guilde"),
                        rs.getFloat("experience"),
                        rs.getString("description"),
                        members
                );
                guilds.add(guild);
            }
        } finally {
            stmt.close();
        }

        return guilds;
    }
    public void updateGuild(Guild guild) throws SQLException {
        String sql = "UPDATE guildes SET " +
                "nom = ?, " +
                "suffixe = ?, " +
                "specialisation = ?, " +
                "niveau = ?, " +
                "rank_guilde = ?, " +
                "experience = ?, " +
                "description = ?, " +
                "membres = ? " +
                "WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, guild.getNom());
            stmt.setString(2, guild.getSuffixe());
            stmt.setString(3, guild.getSpecialisation());
            stmt.setInt(4, guild.getNiveau());
            stmt.setInt(5, guild.getRank());
            stmt.setFloat(6, guild.getExperience());
            stmt.setString(7, guild.getDescription());
            stmt.setString(8, new Gson().toJson(guild.getMembres()));  // Assuming membres is a List<String>
            stmt.setInt(9, guild.getId());

            stmt.executeUpdate();
        }
    }
    public void updateAllGuilds(List<Guild> guilds) throws SQLException {
        for (Guild guild : guilds) {
            updateGuild(guild);
        }
    }

}
