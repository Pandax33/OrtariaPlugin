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

public class PlayerCommand implements CommandExecutor {

    private final HeadDatabaseAPI headDatabaseAPI;

    public PlayerCommand() {
        this.headDatabaseAPI = new HeadDatabaseAPI();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }

        Player player = (Player) sender;

        // Vérifier si le nombre d'arguments est suffisant
        if (args.length < 2) {
            sender.sendMessage("Usage incorrect. Syntaxe: /" + command.getName() + " <joueur> <valeur>");
            return true;
        }

        // Récupérer le joueur spécifié
        Player targetPlayer = Bukkit.getPlayer(args[0]);
        if (targetPlayer == null) {
            sender.sendMessage("Joueur non trouvé: " + args[0]);
            return true;
        }

        // Appliquer la modification en fonction de la commande
        switch (command.getName().toLowerCase()) {
            case "setdestin":
                try {
                    return handleSetDestin(sender, targetPlayer, args[1]);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            case "setargent":
                return handleSetArgent(sender, targetPlayer, args[1]);
            case "setgrade":
                try {
                    return handleSetGrade(sender, targetPlayer, args[1]);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
        }
        return true;
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

    private boolean handleSetDestin(CommandSender sender, Player player, String destin) throws SQLException {
        PlayerFactory playerFactory = PlayerFactory.getInstance();
        Optional<Player_Class> optPlayer = playerFactory.getPlayer(player.getUniqueId().toString());
        if (optPlayer.isPresent()) {
            Player_Class playerClass = optPlayer.get();
            playerClass.setDestin(destin);
            try {
                playerFactory.updatePlayer(player.getUniqueId().toString());
                player.sendMessage("Votre destin a été mis à jour à: " + destin);
                sender.sendMessage("Le destin de " + player.getName() + " a été mis à jour à: " + destin);
                return true;
            } catch (SQLException e) {
                sender.sendMessage("Erreur lors de la mise à jour du destin: " + e.getMessage());
            }
        } else {
            sender.sendMessage("Joueur non trouvé.");
        }
        return false;
    }

    private boolean handleSetArgent(CommandSender sender, Player player, String amount) {
        try {
            double amountDouble = Double.parseDouble(amount);
            PlayerFactory playerFactory = PlayerFactory.getInstance();
            Optional<Player_Class> optPlayer = playerFactory.getPlayer(player.getUniqueId().toString());
            if (optPlayer.isPresent()) {
                Player_Class playerClass = optPlayer.get();
                playerClass.setArgent(amountDouble);
                playerFactory.updatePlayer(player.getUniqueId().toString());
                player.sendMessage("Votre argent a été mis à jour à: " + amountDouble);
                sender.sendMessage("L'argent de " + player.getName() + " a été mis à jour à: " + amountDouble);
                return true;
            } else {
                sender.sendMessage("Joueur non trouvé.");
            }
        } catch (NumberFormatException | SQLException e) {
            sender.sendMessage("Montant invalide.");
        }
        return false;
    }

    private boolean handleSetGrade(CommandSender sender, Player player, String grade) throws SQLException {
        PlayerFactory playerFactory = PlayerFactory.getInstance();
        Optional<Player_Class> optPlayer = playerFactory.getPlayer(player.getUniqueId().toString());
        if (optPlayer.isPresent()) {
            Player_Class playerClass = optPlayer.get();
            playerClass.setGrade(grade);
            try {
                playerFactory.updatePlayer(player.getUniqueId().toString());
                player.sendMessage("Votre grade a été mis à jour à: " + grade);
                sender.sendMessage("Le grade de " + player.getName() + " a été mis à jour à: " + grade);
                return true;
            } catch (SQLException e) {
                sender.sendMessage("Erreur lors de la mise à jour du grade: " + e.getMessage());
            }
        } else {
            sender.sendMessage("Joueur non trouvé.");
        }
        return false;
    }

    private boolean handleSetNiveauAventure(CommandSender sender, Player player, String niveau) {
        try {
            int niveauAventure = Integer.parseInt(niveau);
            return updatePlayerIntAttribute(sender, player, "niveauAventure", niveauAventure);
        } catch (NumberFormatException | SQLException e) {
            sender.sendMessage("Niveau invalide.");
        }
        return false;
    }

    private boolean handleSetPointsTdr(CommandSender sender, Player player, String points) {
        try {
            int pointsTdr = Integer.parseInt(points);
            return updatePlayerIntAttribute(sender, player, "pointsTdr", pointsTdr);
        } catch (NumberFormatException | SQLException e) {
            sender.sendMessage("Points TDR invalide.");
        }
        return false;
    }

    private boolean updatePlayerIntAttribute(CommandSender sender, Player player, String attribute, int value) throws SQLException {
        PlayerFactory playerFactory = PlayerFactory.getInstance();
        Optional<Player_Class> optPlayer = playerFactory.getPlayer(player.getUniqueId().toString());
        if (optPlayer.isPresent()) {
            Player_Class playerClass = optPlayer.get();
            switch (attribute) {
                case "niveauAventure":
                    playerClass.setNiveauAventure(value);
                    break;
                case "pointsTdr":
                    playerClass.setPointsTdr(value);
                    break;
                default:
                    sender.sendMessage("Attribut inconnu: " + attribute);
                    return false;
            }
            try {
                playerFactory.updatePlayer(player.getUniqueId().toString());
                sender.sendMessage("Le " + attribute + " de " + player.getName() + " a été mis à jour à: " + value);
                return true;
            } catch (SQLException e) {
                sender.sendMessage("Erreur lors de la mise à jour de " + attribute + ": " + e.getMessage());
                return false;
            }
        } else {
            sender.sendMessage("Joueur non trouvé.");
            return false;
        }
    }

    private boolean handleSetSpecialisationLevel(CommandSender sender, Player player, String specialisation, String level) {
        try {
            int levelInt = Integer.parseInt(level);
            PlayerFactory playerFactory = PlayerFactory.getInstance();
            Optional<Player_Class> optPlayer = playerFactory.getPlayer(player.getUniqueId().toString());
            if (optPlayer.isPresent()) {
                Player_Class playerClass = optPlayer.get();
                if (setSpecialisationLevel(playerClass, specialisation, levelInt)) {
                    playerFactory.updatePlayer(player.getUniqueId().toString());
                    sender.sendMessage("Le niveau de " + specialisation + " de " + player.getName() + " a été mis à jour à: " + levelInt);
                    return true;
                } else {
                    sender.sendMessage("Spécialisation inconnue: " + specialisation);
                    return false;
                }
            } else {
                sender.sendMessage("Joueur non trouvé.");
                return false;
            }
        } catch (NumberFormatException e) {
            sender.sendMessage("Niveau invalide.");
            return false;
        } catch (SQLException e) {
            sender.sendMessage("Erreur lors de la mise à jour: " + e.getMessage());
            return false;
        }
    }

    private boolean setSpecialisationLevel(Player_Class player, String specialisation, int level) {
        switch (specialisation.toLowerCase()) {
            case "alchimiste":
                player.setNiveauAlchimiste(level);
                break;
            case "arcaniste":
                player.setNiveauArcaniste(level);
                break;
            case "archeologue":
                player.setNiveauArcheologue(level);
                break;
            case "cuisinier":
                player.setNiveauCuisinier(level);
                break;
            case "forgeron":
                player.setNiveauForgeron(level);
                break;
            case "pelleteur":
                player.setNiveauPelleteur(level);
                break;
            case "bucheron":
                player.setNiveauBucheron(level);
                break;
            case "mineur":
                player.setNiveauMineur(level);
                break;
            default:
                return false;
        }
        return true;
    }

    private boolean handleShow(CommandSender sender, String[] args) throws SQLException {
        if (args.length < 2) {
            sender.sendMessage("Usage: /show <joueur> <attribut>");
            return true;
        }

        String playerName = args[0];
        String attribute = args[1].toLowerCase();
        Player targetPlayer = Bukkit.getPlayer(playerName);
        if (targetPlayer == null) {
            sender.sendMessage("Joueur non trouvé: " + playerName);
            return true;
        }

        PlayerFactory playerFactory = PlayerFactory.getInstance();
        Optional<Player_Class> optPlayer = playerFactory.getPlayer(targetPlayer.getUniqueId().toString());
        if (!optPlayer.isPresent()) {
            sender.sendMessage("Données du joueur non trouvées pour: " + playerName);
            return true;
        }

        Player_Class player = optPlayer.get();
        switch (attribute) {
            case "niveauaventure":
                sender.sendMessage(playerName + " - Niveau d'aventure: " + player.getNiveauAventure());
                break;
            case "pointstdr":
                sender.sendMessage(playerName + " - Points TDR: " + player.getPointsTdr());
                break;
            case "alchimiste":
                sender.sendMessage(playerName + " - Niveau Alchimiste: " + player.getNiveauAlchimiste());
                break;
            case "arcaniste":
                sender.sendMessage(playerName + " - Niveau Arcaniste: " + player.getNiveauArcaniste());
                break;
            case "archeologue":
                sender.sendMessage(playerName + " - Niveau Archeologue: " + player.getNiveauArcheologue());
                break;
            case "cuisinier":
                sender.sendMessage(playerName + " - Niveau Cuisinier: " + player.getNiveauCuisinier());
                break;
            case "forgeron":
                sender.sendMessage(playerName + " - Niveau Forgeron: " + player.getNiveauForgeron());
                break;
            case "pelleteur":
                sender.sendMessage(playerName + " - Niveau Pelleteur: " + player.getNiveauPelleteur());
                break;
            case "bucheron":
                sender.sendMessage(playerName + " - Niveau Bucheron: " + player.getNiveauBucheron());
                break;
            case "mineur":
                sender.sendMessage(playerName + " - Niveau Mineur: " + player.getNiveauMineur());
                break;
            default:
                sender.sendMessage("Attribut non reconnu: " + attribute);
                break;
        }
        return true;
    }

}
