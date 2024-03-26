package fr.ortaria;

import com.sun.jna.NativeLibrary;
import fr.ortaria.controllers.FirstJoin;
import fr.ortaria.mobs.TDRFEU;
import org.bukkit.plugin.java.JavaPlugin;
import fr.ortaria.dao.PlayerFactory;
import fr.ortaria.commandes.MobCommand;

import java.sql.Connection;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        System.out.println("Plugin actif!");

        // Établir la connexion à la base de données
        try {
            Connection connection = Database.openConnection();

            PlayerFactory.getInstance(connection);

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
        // Fermer la connexion à la base de données si nécessaire
        Database.closeConnection(); // Assurez-vous que cette méthode ferme correctement votre connexion
        System.out.println("Plugin désactivé.");
    }

    public void loadCommands() {
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
    }
}
