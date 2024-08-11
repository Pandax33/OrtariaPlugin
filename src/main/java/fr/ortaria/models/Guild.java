package fr.ortaria.models;

import java.util.List;
import com.google.gson.Gson; // Importer Gson pour la manipulation de JSON

public class Guild {
    private int id;
    private String nom;
    private String suffixe;
    private String specialisation;
    private int niveau;
    private int rank_guilde;
    private float experience;
    private String description;
    private List<String> membres; // Liste de UUIDs des membres

    // Constructeur par défaut
    public Guild() {
    }

    // Constructeur avec tous les paramètres
    public Guild(int id, String nom, String suffixe, String specialisation, int niveau, int rank, float experience, String description, List<String> membres) {
        this.id = id;
        this.nom = nom;
        this.suffixe = suffixe;
        this.specialisation = specialisation;
        this.niveau = niveau;
        this.rank_guilde = rank;
        this.experience = experience;
        this.description = description;
        this.membres = membres;
    }

    // Getters et setters pour chaque propriété
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getSuffixe() {
        return suffixe;
    }

    public void setSuffixe(String suffixe) {
        this.suffixe = suffixe;
    }

    public String getSpecialisation() {
        return specialisation;
    }

    public void setSpecialisation(String specialisation) {
        this.specialisation = specialisation;
    }

    public int getNiveau() {
        return niveau;
    }

    public void setNiveau(int niveau) {
        this.niveau = niveau;
    }

    public int getRank() {
        return rank_guilde;
    }

    public void setRank(int rank) {
        this.rank_guilde = rank;
    }

    public float getExperience() {
        return experience;
    }

    public void setExperience(float experience) {
        this.experience = experience;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getMembres() {
        return membres;
    }

    public void setMembres(List<String> membres) {
        this.membres = membres;
    }

    // Méthode pour convertir la liste des membres en JSON
    public String getMembresJSON() {
        Gson gson = new Gson();
        return gson.toJson(this.membres);
    }

    // Méthode pour convertir le JSON en liste des membres
    public void setMembresFromJSON(String json) {
        Gson gson = new Gson();
        this.membres = gson.fromJson(json, List.class);
    }
}
