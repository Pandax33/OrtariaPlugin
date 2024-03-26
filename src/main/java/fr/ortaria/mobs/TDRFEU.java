package fr.ortaria.mobs;
import fr.ortaria.Database;
import fr.ortaria.dao.MobFactory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.EntityType;

import java.sql.Connection;
import java.sql.SQLException;

public class TDRFEU implements CommandExecutor{

    private final Connection connection = Database.getConnection();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Cette commande ne peut être utilisée que par un joueur.");
            return true;
        }

        Player player = (Player) sender;
        if (!player.hasPermission("ortaria.summon")) {
            sender.sendMessage("Cette commande ne peut être utilisée que par un admin.");
            return true;
        }

        // Appliquer l'armure
        ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET);
        helmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        ItemStack chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);
        chestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        ItemStack leggings = new ItemStack(Material.DIAMOND_LEGGINGS);
        leggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS);
        boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);


        // Appliquer le stick avec Sharpness I
        ItemStack stick = new ItemStack(Material.STICK);
        stick.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
        MobFactory factory = null;
        try {
            factory = MobFactory.getInstance(connection);
            factory.createMob("zoneApparition", 1, helmet,chestplate, leggings, boots, stick,"test", true, "TDRFEU1", EntityType.ZOMBIE);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }




        sender.sendMessage("Boss TDR FEU 1 créé !");
        return true;
    }
}
