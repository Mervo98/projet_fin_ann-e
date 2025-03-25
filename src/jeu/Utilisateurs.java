package jeu;

public class Utilisateurs {
    private String nomUtilisateur;
    private String motDePasse;
    private Zone zoneCourante;
    private int xPersonnage;
    private int yPersonnage;
    private Inventaire inventaire;

    public Utilisateurs(String nomUtilisateur, String motDePasse, Zone zoneDepart) {
        this.nomUtilisateur = nomUtilisateur;
        this.motDePasse = motDePasse;
        this.zoneCourante = zoneDepart;
        this.xPersonnage = 0;  // Valeurs par défaut, à ajuster
        this.yPersonnage = 0;
        this.inventaire = new Inventaire();  // Création d'un inventaire vide
    }

    public String getNomUtilisateur() {
        return nomUtilisateur;
    }

    public String getZoneCourante() {
        return zoneCourante != null ? zoneCourante.toString() : "Zone inconnue";
    }

    public void setZoneCourante(Zone nouvelleZone) {
        this.zoneCourante = nouvelleZone;
    }

    public boolean verifierMotDePasse(String motDePasse) {
        return this.motDePasse.equals(motDePasse); // Comparaison directe du mot de passe en clair
    }

    // Ajout des getters pour X, Y, et inventaire
    public int getXPersonnage() {
        return xPersonnage;
    }

    public int getYPersonnage() {
        return yPersonnage;
    }

    public Inventaire getInventaire() {
        return inventaire;
    }

    // Méthodes de modification des coordonnées et de l'inventaire
    public void setXPersonnage(int x) {
        this.xPersonnage = x;
    }

    public void setYPersonnage(int y) {
        this.yPersonnage = y;
    }

    public void setInventaire(Inventaire inventaire) {
        this.inventaire = inventaire;
    }

    public String getMotDePasse() {
        return motDePasse;
    }


    // Ajoutez d'autres méthodes nécessaires pour votre logique
}
