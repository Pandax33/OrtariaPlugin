package fr.ortaria;

import fr.ortaria.commandes.*;
import fr.ortaria.controllers.FirstJoin;
import fr.ortaria.dao.GuildFactory;
import fr.ortaria.mobs.TDRFEU.TDRFEU;
import org.bukkit.plugin.java.JavaPlugin;
import fr.ortaria.dao.PlayerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        System.out.println("Plugin actif!");

        // Établir la connexion à la base de données
        try {
            Connection connection = Database.openConnection();

            PlayerFactory.getInstance();
            GuildFactory.getInstance();

            // Enregistrer FirstJoin en tant qu'écouteur d'événements
            this.getServer().getPluginManager().registerEvents(new FirstJoin(connection), this);
            loadCommands();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Impossible de se connecter à la base de données. Le plugin sera désactivé.");
            this.getServer().getPluginManager().disablePlugin(this); // Désactiver le plugin en cas d'erreur
        }
    }

    @Override
    public void onDisable() {
        System.out.println("Désactivation du plugin...");
        System.out.println("Sauvegarde des données des joueurs...");
        try {
            PlayerFactory.getInstance().updateAllPlayers();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Sauvegarde des données des guildes...");
        try {
            GuildFactory.getInstance().updateAllGuilds();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // Fermer la connexion à la base de données si nécessaire
        Database.closeConnection();
        System.out.println("Plugin désactivé.");
    }

    public void loadCommands() throws SQLException {
        // Debugging pour vérifier si les commandes sont reconnues
        if (this.getCommand("summon") == null) {
            getLogger().warning("La commande 'summon' n'est pas définie dans plugin.yml.");
        } else {
            this.getCommand("summon").setExecutor(new MobCommand());
        }

        if (this.getCommand("createBossTDRFEU1") == null) {
            getLogger().warning("La commande 'createBossTDRFEU1' n'est pas définie dans plugin.yml.");
        } else {
            this.getCommand("createBossTDRFEU1").setExecutor(new TDRFEU());
        }

        if (this.getCommand("guilde") == null) {
            getLogger().warning("La commande 'guilde' n'est pas définie dans plugin.yml.");
        } else {
            this.getCommand("guilde").setExecutor(new GuildCommandExecutor());
        }

        if (this.getCommand("setdestin") == null) {
            getLogger().warning("La commande 'setdestin' n'est pas définie dans plugin.yml.");
        } else {
            this.getCommand("setdestin").setExecutor(new PlayerCommand());
        }

        if (this.getCommand("setargent") == null) {
            getLogger().warning("La commande 'setargent' n'est pas définie dans plugin.yml.");
        } else {
            this.getCommand("setargent").setExecutor(new PlayerCommand());
        }

        if (this.getCommand("setgrade") == null) {
            getLogger().warning("La commande 'setgrade' n'est pas définie dans plugin.yml.");
        } else {
            this.getCommand("setgrade").setExecutor(new PlayerCommand());
        }

        if (this.getCommand("setniveauaventure") == null) {
            getLogger().warning("La commande 'setniveauaventure' n'est pas définie dans plugin.yml.");
        } else {
            this.getCommand("setniveauaventure").setExecutor(new PlayerCommand());
        }

        if (this.getCommand("setpointstdr") == null) {
            getLogger().warning("La commande 'setpointstdr' n'est pas définie dans plugin.yml.");
        } else {
            this.getCommand("setpointstdr").setExecutor(new PlayerCommand());
        }

        if (this.getCommand("setlevel") == null) {
            getLogger().warning("La commande 'setlevel' n'est pas définie dans plugin.yml.");
        } else {
            this.getCommand("setlevel").setExecutor(new PlayerCommand());
        }

        if (this.getCommand("show") == null) {
            getLogger().warning("La commande 'show' n'est pas définie dans plugin.yml.");
        } else {
            this.getCommand("show").setExecutor(new PlayerCommand());
        }

        if (this.getCommand("money") == null) {
            getLogger().warning("La commande 'money' n'est pas définie dans plugin.yml.");
        } else {
            this.getCommand("money").setExecutor(new MoneyCommand());
        }

        if (this.getCommand("menu") == null) {
            getLogger().warning("La commande 'menu' n'est pas définie dans plugin.yml.");
        } else {
            this.getCommand("menu").setExecutor(new StatCommand());
        }

    }
}
