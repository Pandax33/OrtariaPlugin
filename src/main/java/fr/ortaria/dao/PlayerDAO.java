package fr.ortaria.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import fr.ortaria.models.Player;
public class PlayerDAO {

    private final Connection connection;

    public PlayerDAO(Connection connection) {
        this.connection = connection;
    }

    public void insertPlayer(Player player) throws SQLException {
        String sql = "INSERT INTO joueurs (uuid, pseudo, date_first_connexion, destin, classe, guilde, niveau_aventure, argent, points_tdr, grade, niveau_alchimiste, niveau_arcaniste, niveau_archeologue, niveau_cuisinier, niveau_forgeron, niveau_pelleteur, niveau_bucheron, niveau_mineur, faveur_astrale,recharge_astrale,vitesse,PV,force_joueur) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, player.getUuid());
            stmt.setString(2, player.getPseudo());
            stmt.setTimestamp(3, player.getDateFirstConnexion());
            stmt.setString(4, player.getDestin());
            stmt.setString(5, player.getClasse());
            stmt.setString(6, player.getGuilde());
            stmt.setInt(7, player.getNiveauAventure());
            stmt.setDouble(8, player.getArgent());
            stmt.setInt(9, player.getPointsTdr());
            stmt.setString(10, player.getGrade());
            stmt.setInt(11, player.getNiveauAlchimiste());
            stmt.setInt(12, player.getNiveauArcaniste());
            stmt.setInt(13, player.getNiveauArcheologue());
            stmt.setInt(14, player.getNiveauCuisinier());
            stmt.setInt(15, player.getNiveauForgeron());
            stmt.setInt(16, player.getNiveauPelleteur());
            stmt.setInt(17, player.getNiveauBucheron());
            stmt.setInt(18, player.getNiveauMineur());
            stmt.setInt(19, player.getFaveur_astrale());
            stmt.setInt(20, player.getRecharge_astrale());
            stmt.setInt(21, player.getVitesse());
            stmt.setInt(22, player.getPV());
            stmt.setInt(23, player.getForce_joueur());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating player failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    player.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating player failed, no ID obtained.");
                }
            }
        }
    }

    public Player getPlayer(String uuid) throws SQLException {
        String sql = "SELECT * FROM joueurs WHERE uuid = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, uuid);

            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                Player player = new Player(
                        resultSet.getString("uuid"),
                        resultSet.getString("pseudo"),
                        resultSet.getTimestamp("date_first_connexion"),
                        resultSet.getString("destin"),
                        resultSet.getString("classe"),
                        resultSet.getString("guilde"),
                        resultSet.getInt("niveau_aventure"),
                        resultSet.getDouble("argent"),
                        resultSet.getInt("points_tdr"),
                        resultSet.getString("grade"),
                        resultSet.getInt("niveau_alchimiste"),
                        resultSet.getInt("niveau_arcaniste"),
                        resultSet.getInt("niveau_archeologue"),
                        resultSet.getInt("niveau_cuisinier"),
                        resultSet.getInt("niveau_forgeron"),
                        resultSet.getInt("niveau_pelleteur"),
                        resultSet.getInt("niveau_bucheron"),
                        resultSet.getInt("niveau_mineur"),
                        resultSet.getInt("faveur_astrale"),
                        resultSet.getInt("recharge_astrale"),
                        resultSet.getInt("vitesse"),
                        resultSet.getInt("PV"),
                        resultSet.getInt("force_joueur")
                );
                return player;
            } else {
                return null;
            }
        }
    }

    public void updatePlayer(Player player) throws SQLException {
        String sql = "UPDATE joueurs SET pseudo = ?, destin = ?, classe = ?, guilde = ?, niveau_aventure = ?, argent = ?, points_tdr = ?, grade = ?, niveau_alchimiste = ?, niveau_arcaniste = ?, niveau_archeologue = ?, niveau_cuisinier = ?, niveau_forgeron = ?, niveau_pelleteur = ?, niveau_bucheron = ?, niveau_mineur = ?,faveur_astrale = ?, recharge_astrale = ?,vitesse = ?, PV = ?, force_joueur = ? WHERE uuid = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, player.getPseudo());
            stmt.setString(2, player.getDestin());
            stmt.setString(3, player.getClasse());
            stmt.setString(4, player.getGuilde());
            stmt.setInt(5, player.getNiveauAventure());
            stmt.setDouble(6, player.getArgent());
            stmt.setInt(7, player.getPointsTdr());
            stmt.setString(8, player.getGrade());
            stmt.setInt(9, player.getNiveauAlchimiste());
            stmt.setInt(10, player.getNiveauArcaniste());
            stmt.setInt(11, player.getNiveauArcheologue());
            stmt.setInt(12, player.getNiveauCuisinier());
            stmt.setInt(13, player.getNiveauForgeron());
            stmt.setInt(14, player.getNiveauPelleteur());
            stmt.setInt(15, player.getNiveauBucheron());
            stmt.setInt(16, player.getNiveauMineur());
            stmt.setInt(17, player.getFaveur_astrale());
            stmt.setInt(18, player.getRecharge_astrale());
            stmt.setInt(19, player.getVitesse());
            stmt.setInt(20, player.getPV());
            stmt.setInt(21, player.getForce_joueur());
            stmt.setString(22, player.getUuid());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating player failed, no rows affected.");
            }
        }
    }

    public List<Player> getAllPlayers() throws SQLException {
        List<Player> players = new ArrayList<>();
        String sql = "SELECT * FROM joueurs";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Player player = new Player(
                        rs.getString("uuid"),
                        rs.getString("pseudo"),
                        rs.getTimestamp("date_first_connexion"),
                        rs.getString("destin"),
                        rs.getString("classe"),
                        rs.getString("guilde"),
                        rs.getInt("niveau_aventure"),
                        rs.getDouble("argent"),
                        rs.getInt("points_tdr"),
                        rs.getString("grade"),
                        rs.getInt("niveau_alchimiste"),
                        rs.getInt("niveau_arcaniste"),
                        rs.getInt("niveau_archeologue"),
                        rs.getInt("niveau_cuisinier"),
                        rs.getInt("niveau_forgeron"),
                        rs.getInt("niveau_pelleteur"),
                        rs.getInt("niveau_bucheron"),
                        rs.getInt("niveau_mineur"),
                        rs.getInt("faveur_astrale"),
                        rs.getInt("recharge_astrale"),
                        rs.getInt("vitesse"),
                        rs.getInt("PV"),
                        rs.getInt("force_joueur")
                );
                player.setId(rs.getInt("id")); // Assurez-vous d'avoir un setter pour id dans Player
                players.add(player);
            }
        }

        return players;
    }


}
