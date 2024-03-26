package fr.ortaria.commandes;
import fr.ortaria.Database;
import fr.ortaria.dao.MobFactory;
import fr.ortaria.models.Mob;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import java.sql.Connection;

import java.sql.SQLException;
import java.util.Optional;

public class MobCommand implements CommandExecutor {

    Connection connection = Database.getConnection();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            // Vérifier si le joueur a la permission
            if (player.hasPermission("ortaria.summon")) {
                // Logique de la commande
                if (args.length > 0) {
                    String slug = args[0];
                    try {
                        MobFactory mobFactory = MobFactory.getInstance(connection);
                        if(mobFactory.getMobBySlug(slug).isEmpty()) {
                            player.sendMessage("Le mob avec le slug " + slug + " n'existe pas.");
                            return true;
                        }else{
                            player.sendMessage("Le mob avec le slug " + slug + " existe.");
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                } else {
                    player.sendMessage("Usage: /summon <slug>");
                }
            } else {
                player.sendMessage("Vous n'avez pas la permission d'utiliser cette commande.");
            }
        } else {
            sender.sendMessage("Cette commande ne peut être utilisée que par un joueur.");
        }
        return true;
    }
}