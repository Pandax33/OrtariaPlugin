package fr.ortaria.models;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

public class Mob {
    private int id;
    private String zoneApparition;
    private int pv;
    private ItemStack casque;
    private ItemStack plastron;
    private ItemStack pantalon;
    private ItemStack bottes;
    private ItemStack objetTenu;
    private String nom;
    private String slug;
    private boolean isBoss;
    private EntityType type;


    // Constructeur
    public Mob(String zoneApparition, int pv, ItemStack casque, ItemStack plastron, ItemStack pantalon, ItemStack bottes, ItemStack objetTenu, String nom, boolean isBoss, String slug, EntityType type) {
        this.zoneApparition = zoneApparition;
        this.pv = pv;
        this.casque = casque;
        this.plastron = plastron;
        this.pantalon = pantalon;
        this.bottes = bottes;
        this.objetTenu = objetTenu;
        this.nom = nom;
        this.isBoss = isBoss;
        this.slug = slug;
        this.type = type;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getZoneApparition() {
        return zoneApparition;
    }

    public void setZoneApparition(String zoneApparition) {
        this.zoneApparition = zoneApparition;
    }

    public int getPv() {
        return pv;
    }

    public void setPv(int pv) {
        this.pv = pv;
    }

    public ItemStack getCasque() {
        return casque;
    }

    public void setCasque(ItemStack casque) {
        this.casque = casque;
    }

    public ItemStack getPlastron() {
        return plastron;
    }

    public void setPlastron(ItemStack plastron) {
        this.plastron = plastron;
    }

    public ItemStack getPantalon() {
        return pantalon;
    }

    public void setPantalon(ItemStack pantalon) {
        this.pantalon = pantalon;
    }

    public ItemStack getBottes() {
        return bottes;
    }

    public void setBottes(ItemStack bottes) {
        this.bottes = bottes;
    }

    public ItemStack getObjetTenu() {
        return objetTenu;
    }

    public void setObjetTenu(ItemStack objetTenu) {
        this.objetTenu = objetTenu;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public boolean isBoss() {
        return isBoss;
    }

    public void setBoss(boolean isBoss) {
        this.isBoss = isBoss;
    }
    public String getSlug() {
        return slug;
    }
    public void setSlug(String slug) {
        this.slug = slug;
    }
    public EntityType getType() {
        return type;
    }
    public void setType(EntityType type) {
        this.type = type;
    }
}
