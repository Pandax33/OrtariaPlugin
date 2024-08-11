package fr.ortaria.commandes;

import fr.ortaria.dao.PlayerFactory;
import fr.ortaria.models.Player_Class;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.Optional;

public class MoneyCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage("Usage: /money <show|give> [player] [amount]");
            return true;
        }

        String subCommand = args[0].toLowerCase();
        switch (subCommand) {
            case "show":
                try {
                    return handleShowMoney(sender, args);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            case "give":
                try {
                    return handleGiveMoney(sender, args);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            default:
                sender.sendMessage("Invalid command. Use /money <show|give> [player] [amount]");
                return true;
        }
    }

    private boolean handleShowMoney(CommandSender sender, String[] args) throws SQLException {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can perform this command.");
            return true;
        }

        Player player = (Player) sender;
        PlayerFactory playerFactory = PlayerFactory.getInstance();
        Optional<Player_Class> optPlayer = playerFactory.getPlayer(player.getUniqueId().toString());

        if (!optPlayer.isPresent()) {
            sender.sendMessage("Player data not found.");
            return true;
        }

        Player_Class playerClass = optPlayer.get();
        sender.sendMessage(player.getName() + " has $" + playerClass.getArgent());
        return true;
    }

    private boolean handleGiveMoney(CommandSender sender, String[] args) throws SQLException {
        if (args.length < 3) {
            sender.sendMessage("Usage: /money give <player> <amount>");
            return true;
        }

        Player sourcePlayer = (Player) sender;
        String targetPlayerName = args[1];
        double amount;

        try {
            amount = Double.parseDouble(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage("Invalid amount. Please enter a valid number.");
            return true;
        }

        Player targetPlayer = Bukkit.getPlayer(targetPlayerName);
        if (targetPlayer == null) {
            sender.sendMessage("Target player not found.");
            return true;
        }

        PlayerFactory playerFactory = PlayerFactory.getInstance();
        try {
            Optional<Player_Class> optSourcePlayer = playerFactory.getPlayer(sourcePlayer.getUniqueId().toString());
            Optional<Player_Class> optTargetPlayer = playerFactory.getPlayer(targetPlayer.getUniqueId().toString());

            if (!optSourcePlayer.isPresent() || !optTargetPlayer.isPresent()) {
                sender.sendMessage("Player data not found.");
                return true;
            }

            Player_Class sourcePlayerClass = optSourcePlayer.get();
            Player_Class targetPlayerClass = optTargetPlayer.get();

            if (sourcePlayerClass.getArgent() < amount) {
                sender.sendMessage("Insufficient funds.");
                return true;
            }

            sourcePlayerClass.setArgent(sourcePlayerClass.getArgent() - amount);
            targetPlayerClass.setArgent(targetPlayerClass.getArgent() + amount);

            playerFactory.updatePlayer(sourcePlayer.getUniqueId().toString());
            playerFactory.updatePlayer(targetPlayer.getUniqueId().toString());

            sender.sendMessage("Transferred $" + amount + " to " + targetPlayer.getName());
            targetPlayer.sendMessage("Received $" + amount + " from " + sourcePlayer.getName());

        } catch (SQLException e) {
            sender.sendMessage("Error updating player data: " + e.getMessage());
            return true;
        }

        return true;
    }
}
