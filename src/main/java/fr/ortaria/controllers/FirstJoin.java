package fr.ortaria.controllers;

import fr.ortaria.Database;
import fr.ortaria.dao.PlayerDAO;
import fr.ortaria.dao.PlayerFactory;
import fr.ortaria.models.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Random;

public class FirstJoin implements Listener {

    private PlayerFactory playerFactory;
    public FirstJoin(Connection connection) {
        try {
            this.playerFactory = PlayerFactory.getInstance(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) throws SQLException {
        // Obtenir les informations du joueur
        String uuid = event.getPlayer().getUniqueId().toString();
        String pseudo = event.getPlayer().getName();
        Timestamp dateFirstConnexion = new Timestamp(System.currentTimeMillis());
        System.out.println("UUID");
        System.out.println(uuid);
        String[] destins = {"Ange", "Démon", "Monstre", "Dieu"};
        Random random = new Random();
        String destin = destins[random.nextInt(destins.length)];
        String classe = "Recrue";
        String guilde = "";
        int niveauAventure = 1;
        double argent = 0.0;
        int pointsTdr = 0;
        String grade = "Recrue";
        int niveauAlchimiste = 0;
        int niveauArcaniste = 0;
        int niveauArcheologue = 0;
        int niveauCuisinier = 0;
        int niveauForgeron = 0;
        int niveauPelleteur = 0;
        int niveauBucheron = 0;
        int niveauMineur = 0;
        int faveur_astrale = 0;
        int recharge_astrale = 0;
        int vitesse = 0;
        int PV = 0;
        int force_joueur = 0;

        if (isNewPlayer(uuid)) {
            Player player = playerFactory.createPlayer(uuid, pseudo, dateFirstConnexion, destin, classe, guilde, niveauAventure, argent, pointsTdr, grade, niveauAlchimiste, niveauArcaniste, niveauArcheologue, niveauCuisinier, niveauForgeron, niveauPelleteur, niveauBucheron, niveauMineur, faveur_astrale, recharge_astrale, vitesse, PV, force_joueur);

            event.getPlayer().sendMessage("Bienvenue sur le serveur pour la première fois, " + pseudo + "!");
        }else {
            event.getPlayer().sendMessage("Bienvenue sur le serveur, " + pseudo + "!");

        }
    }

    private boolean isNewPlayer(String uuid) {
        System.out.println(playerFactory.getPlayer(uuid));
        return playerFactory.getPlayer(uuid).isEmpty();
    }
}
