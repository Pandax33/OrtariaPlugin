package fr.ortaria.commandes;

import fr.ortaria.dao.PlayerFactory;
import fr.ortaria.models.Player_Class;

import me.arcaniax.hdb.api.HeadDatabaseAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Optional;

public class StatCommand implements CommandExecutor {

    private final HeadDatabaseAPI headDatabaseAPI;

    public StatCommand() {
        this.headDatabaseAPI = new HeadDatabaseAPI();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }

        Player player = (Player) sender;

        // Si aucun argument n'est fourni, utiliser le joueur qui a exécuté la commande
        Player targetPlayer = player;
        if (args.length > 0) {
            // Récupérer le joueur spécifié dans les arguments
            targetPlayer = Bukkit.getPlayer(args[0]);
            if (targetPlayer == null) {
                sender.sendMessage("Joueur non trouvé: " + args[0]);
                return true;
            }
        }

        // Afficher le menu des stats
        return handleMenu(targetPlayer);
    }

    private boolean handleMenu(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 27, ChatColor.GREEN + "Player Stats");

        try {
            Optional<Player_Class> playerDataOptional = PlayerFactory.getInstance().getPlayer(player.getUniqueId().toString());
            if (!playerDataOptional.isPresent()) {
                player.sendMessage(ChatColor.RED + "Player data not found.");
                return false;
            }

            Player_Class playerData = playerDataOptional.get();

            // Ajouter des têtes d'illustration pour chaque statistique
            inventory.setItem(0, createCustomHeadItem("1546", "Pseudo", playerData.getPseudo()));
            inventory.setItem(1, createCustomHeadItem("8137", "Classe", playerData.getClasse()));
            inventory.setItem(2, createCustomHeadItem("13892", "Guilde", playerData.getGuilde()));
            inventory.setItem(3, createCustomHeadItem("3458", "Niveau Aventure", String.valueOf(playerData.getNiveauAventure())));
            inventory.setItem(4, createCustomHeadItem("4553", "Argent", String.valueOf(playerData.getArgent())));
            inventory.setItem(5, createCustomHeadItem("4657", "Points TDR", String.valueOf(playerData.getPointsTdr())));
            inventory.setItem(6, createCustomHeadItem("3874", "Grade", playerData.getGrade()));
            inventory.setItem(7, createCustomHeadItem("6791", "Niveau Alchimiste", String.valueOf(playerData.getNiveauAlchimiste())));
            // Continuez à ajouter des items pour les autres statistiques

            player.openInventory(inventory);

        } catch (SQLException e) {
            e.printStackTrace();
            player.sendMessage(ChatColor.RED + "An error occurred while fetching player data.");
        }
        return true;
    }

    private ItemStack createCustomHeadItem(String headID, String statName, String statValue) {
        ItemStack item = headDatabaseAPI.getItemHead(headID);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW + statName);
        meta.setLore(Arrays.asList(ChatColor.WHITE + statValue));
        item.setItemMeta(meta);
        return item;
    }
}
