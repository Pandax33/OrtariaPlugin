package fr.ortaria.dao;
import fr.ortaria.models.Mob;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
public class MobFactory {
    private static MobFactory instance;
    private final MobDAO mobDAO;
    private final List<Mob> mobs; // Liste en mémoire des mobs

    private MobFactory(Connection connection) throws SQLException {
        this.mobDAO = new MobDAO(connection);
        this.mobs = new ArrayList<>();
        loadAllMobs(); // Charger tous les mobs de la BDD au démarrage
    }

    // Méthode pour obtenir l'instance unique de la classe.
    public static synchronized MobFactory getInstance(Connection connection) throws SQLException {
        if (instance == null) {
            instance = new MobFactory(connection);
        }
        return instance;
    }

    public Mob createMob(String zoneApparition, int pv, ItemStack casque, ItemStack plastron, ItemStack pantalon, ItemStack bottes, ItemStack objetTenu, String nom, boolean isBoss, String slug, EntityType type) {
        // Créer un nouveau mob
        Mob mob = new Mob(zoneApparition, pv, casque, plastron, pantalon, bottes, objetTenu, nom, isBoss, slug, type);

        // Insérer le mob dans la base de données
        try {
            mobDAO.insertMob(mob);
            mobs.add(mob); // Ajouter le mob à la liste en mémoire
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return mob;
    }

    public Optional<Mob> getMob(int id) {
        return mobs.stream().filter(mob -> mob.getId() == id).findFirst();
    }

    private void loadAllMobs() throws SQLException {
        List<Mob> loadedMobs = mobDAO.getAllMobs();
        mobs.clear();
        mobs.addAll(loadedMobs);
    }

    public List<Mob> getAllMobs() {
        return new ArrayList<>(mobs);
    }

    public Optional<Mob> getMobBySlug(String slug) {
        return mobs.stream().filter(mob -> slug.equals(mob.getSlug())).findFirst();
    }
}
