package fr.ortaria.helper;
import org.bukkit.inventory.ItemStack;
import org.bukkit.configuration.file.YamlConfiguration;

public class ItemSerialization {
    public static String itemStackToString(ItemStack item) {
        YamlConfiguration yamlConfiguration = new YamlConfiguration();
        yamlConfiguration.set("item", item);
        return yamlConfiguration.saveToString();
    }
    public static ItemStack stringToItemStack(String itemString) {
        YamlConfiguration yamlConfiguration = new YamlConfiguration();
        try {
            yamlConfiguration.loadFromString(itemString);
            ItemStack item = yamlConfiguration.getItemStack("item");
            return item;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
