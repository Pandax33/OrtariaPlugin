package fr.ortaria.dao;

import fr.ortaria.models.Mob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import fr.ortaria.helper.ItemSerialization;
import net.minecraft.world.item.ItemStack;
import org.bukkit.entity.EntityType;

public class MobDAO {

    private final Connection connection;

    public MobDAO(Connection connection) {
        this.connection = connection;
    }

    public void insertMob(Mob mob) throws SQLException {
        String sql = "INSERT INTO Mobs (zoneApparition, pv, casque, plastron, pantalon, bottes, objetTenu, nom, isBoss,slug,type) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, mob.getZoneApparition());
            stmt.setInt(2, mob.getPv());
            stmt.setString(3, ItemSerialization.itemStackToString(mob.getCasque()));
            stmt.setString(4, ItemSerialization.itemStackToString(mob.getPlastron()));
            stmt.setString(5, ItemSerialization.itemStackToString(mob.getPantalon()));
            stmt.setString(6, ItemSerialization.itemStackToString(mob.getBottes()));
            stmt.setString(7, ItemSerialization.itemStackToString(mob.getObjetTenu()));
            stmt.setString(8, mob.getNom());
            stmt.setBoolean(9, mob.isBoss());
            stmt.setString(10, mob.getSlug());
            stmt.setString(11, mob.getType().name());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating mob failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    mob.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating mob failed, no ID obtained.");
                }
            }
        }
    }

    public Mob getMob(int id) throws SQLException {
        String sql = "SELECT * FROM Mobs WHERE ID = ?";
        Mob mob = null;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    mob = new Mob(
                            resultSet.getString("zoneApparition"),
                            resultSet.getInt("pv"),
                            // Désérialisation des String en ItemStacks
                            ItemSerialization.stringToItemStack(resultSet.getString("casque")),
                            ItemSerialization.stringToItemStack(resultSet.getString("plastron")),
                            ItemSerialization.stringToItemStack(resultSet.getString("pantalon")),
                            ItemSerialization.stringToItemStack(resultSet.getString("bottes")),
                            ItemSerialization.stringToItemStack(resultSet.getString("objetTenu")),
                            resultSet.getString("nom"),
                            resultSet.getBoolean("isBoss"),
                            resultSet.getString("slug"),
                            EntityType.valueOf(resultSet.getString("type"))
                    );
                    mob.setId(resultSet.getInt("ID"));
                }
            }
        }
        return mob;
    }

    public List<Mob> getAllMobs() throws SQLException {
        List<Mob> mobs = new ArrayList<>();
        String sql = "SELECT * FROM Mobs";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Mob mob = new Mob(
                            rs.getString("zoneApparition"),
                            rs.getInt("pv"),
                            ItemSerialization.stringToItemStack(rs.getString("casque")),
                            ItemSerialization.stringToItemStack(rs.getString("plastron")),
                            ItemSerialization.stringToItemStack(rs.getString("pantalon")),
                            ItemSerialization.stringToItemStack(rs.getString("bottes")),
                            ItemSerialization.stringToItemStack(rs.getString("objetTenu")),
                            rs.getString("nom"),
                            rs.getBoolean("isBoss"),
                            rs.getString("slug"),
                            EntityType.valueOf(rs.getString("type"))
                    );
                    mob.setId(rs.getInt("ID"));
                    mobs.add(mob);
                }
            }
        }

        return mobs;
    }

    // Vous pouvez ajouter d'autres méthodes ici selon vos besoins, par exemple pour mettre à jour ou supprimer des monstres.
}
