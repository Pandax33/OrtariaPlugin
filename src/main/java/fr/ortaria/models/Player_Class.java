package fr.ortaria.models;
import java.sql.Timestamp;
public class Player_Class {
    private int id;
    private String uuid;
    private String pseudo;
    private Timestamp dateFirstConnexion;
    private String destin;
    private String classe;
    private String guilde;
    private int niveauAventure;
    private double argent;
    private int pointsTdr;
    private String grade;
    private int niveauAlchimiste;
    private int niveauArcaniste;
    private int niveauArcheologue;
    private int niveauCuisinier;
    private int niveauForgeron;
    private int niveauPelleteur;
    private int niveauBucheron;
    private int niveauMineur;
    private int faveur_astrale;
    private int recharge_astrale;
    private int vitesse;
    private int PV;
    private int force_joueur;
    private int id_guilde;
    private int rank_guilde;
    private boolean gChat;
    private int invitation_guilde;


    // Constructeur
    public Player_Class(String uuid, String pseudo, Timestamp dateFirstConnexion, String destin, String classe, String guilde, int niveauAventure, double argent, int pointsTdr, String grade, int niveauAlchimiste, int niveauArcaniste, int niveauArcheologue, int niveauCuisinier, int niveauForgeron, int niveauPelleteur, int niveauBucheron, int niveauMineur, int faveur_astrale, int recharge_astrale, int vitesse, int PV, int force_joueur, int id_guilde,int rank_guilde) {
        this.uuid = uuid;
        this.pseudo = pseudo;
        this.dateFirstConnexion = dateFirstConnexion;
        this.destin = destin;
        this.classe = classe;
        this.guilde = guilde;
        this.niveauAventure = niveauAventure;
        this.argent = argent;
        this.pointsTdr = pointsTdr;
        this.grade = grade;
        this.niveauAlchimiste = niveauAlchimiste;
        this.niveauArcaniste = niveauArcaniste;
        this.niveauArcheologue = niveauArcheologue;
        this.niveauCuisinier = niveauCuisinier;
        this.niveauForgeron = niveauForgeron;
        this.niveauPelleteur = niveauPelleteur;
        this.niveauBucheron = niveauBucheron;
        this.niveauMineur = niveauMineur;
        this.faveur_astrale = faveur_astrale;
        this.recharge_astrale = recharge_astrale;
        this.vitesse = vitesse;
        this.PV = PV;
        this.force_joueur = force_joueur;
        this.gChat = false;
        this.id_guilde = id_guilde;
        this.rank_guilde = rank_guilde;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public Timestamp getDateFirstConnexion() {
        return dateFirstConnexion;
    }

    public void setDateFirstConnexion(Timestamp dateFirstConnexion) {
        this.dateFirstConnexion = dateFirstConnexion;
    }

    public String getDestin() {
        return destin;
    }

    public void setDestin(String destin) {
        this.destin = destin;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public String getGuilde() {
        return guilde;
    }

    public void setGuilde(String guilde) {
        this.guilde = guilde;
    }

    public int getNiveauAventure() {
        return niveauAventure;
    }

    public void setNiveauAventure(int niveauAventure) {
        this.niveauAventure = niveauAventure;
    }

    public double getArgent() {
        return argent;
    }

    public void setArgent(double argent) {
        this.argent = argent;
    }

    public int getPointsTdr() {
        return pointsTdr;
    }

    public void setPointsTdr(int pointsTdr) {
        this.pointsTdr = pointsTdr;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public int getNiveauAlchimiste() {
        return niveauAlchimiste;
    }

    public void setNiveauAlchimiste(int niveauAlchimiste) {
        this.niveauAlchimiste = niveauAlchimiste;
    }

    public int getNiveauArcaniste() {
        return niveauArcaniste;
    }

    public void setNiveauArcaniste(int niveauArcaniste) {
        this.niveauArcaniste = niveauArcaniste;
    }

    public int getNiveauArcheologue() {
        return niveauArcheologue;
    }

    public void setNiveauArcheologue(int niveauArcheologue) {
        this.niveauArcheologue = niveauArcheologue;
    }

    public int getNiveauCuisinier() {
        return niveauCuisinier;
    }

    public void setNiveauCuisinier(int niveauCuisinier) {
        this.niveauCuisinier = niveauCuisinier;
    }

    public int getNiveauForgeron() {
        return niveauForgeron;
    }

    public void setNiveauForgeron(int niveauForgeron) {
        this.niveauForgeron = niveauForgeron;
    }

    public int getNiveauPelleteur() {
        return niveauPelleteur;
    }

    public void setNiveauPelleteur(int niveauPelleteur) {
        this.niveauPelleteur = niveauPelleteur;
    }

    public int getNiveauBucheron() {
        return niveauBucheron;
    }

    public void setNiveauBucheron(int niveauBucheron) {
        this.niveauBucheron = niveauBucheron;
    }

    public int getNiveauMineur() {
        return niveauMineur;
    }

    public void setNiveauMineur(int niveauMineur) {
        this.niveauMineur = niveauMineur;
    }
    public int getFaveur_astrale() {
        return faveur_astrale;
    }
    public void setFaveur_astrale(int faveur_astrale) {
        this.faveur_astrale = faveur_astrale;
    }
    public int getRecharge_astrale() {
        return recharge_astrale;
    }
    public void setRecharge_astrale(int recharge_astrale) {
        this.recharge_astrale = recharge_astrale;
    }
    public int getVitesse() {
        return vitesse;
    }
    public void setVitesse(int vitesse) {
        this.vitesse = vitesse;
    }
    public int getPV() {
        return PV;
    }
    public void setPV(int PV) {
        this.PV = PV;
    }
    public int getForce_joueur() {
        return force_joueur;
    }
    public void setForce_joueur(int force_joueur) {
        this.force_joueur = force_joueur;
    }
    public boolean getGChat() {
        return gChat;
    }
    public void setGChat(boolean gChat) {
        this.gChat = gChat;
    }
    public int getId_guilde() {
        return id_guilde;
    }
    public void setId_guilde(int id_guilde) {
        this.id_guilde = id_guilde;
    }
    public int getRank_guilde() {
        return rank_guilde;
    }
    public void setRank_guilde(int rank_guilde) {
        this.rank_guilde = rank_guilde;
    }
    public int getInvitation_guilde() {
        return invitation_guilde;
    }
    public void setInvitation_guilde(int invitation_guilde) {
        this.invitation_guilde = invitation_guilde;
    }
}

