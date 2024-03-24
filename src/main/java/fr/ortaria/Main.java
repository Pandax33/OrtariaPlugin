package fr.ortaria;

import com.sun.jna.NativeLibrary;
import fr.ortaria.controllers.FirstJoin;
import org.bukkit.plugin.java.JavaPlugin;
import fr.ortaria.dao.PlayerFactory;

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
}
