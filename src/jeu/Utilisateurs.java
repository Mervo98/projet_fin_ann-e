package jeu;

/**
 * La classe {@code Utilisateurs} représente un utilisateur du jeu.
 * Elle contient les informations relatives à un utilisateur, telles que son nom d'utilisateur,
 * son mot de passe, sa position dans le jeu (zone courante), ses coordonnées dans l'environnement,
 * ainsi que son inventaire.
 */
public class Utilisateurs {

    private String nomUtilisateur;
    private String motDePasse;
    private Zone zoneCourante;
    private int xPersonnage;
    private int yPersonnage;
    private Inventaire inventaire;

    /**
     * Constructeur de la classe {@code Utilisateurs}.
     * Initialise un utilisateur avec son nom d'utilisateur, son mot de passe, et une zone de départ.
     *
     * @param nomUtilisateur Le nom d'utilisateur de l'utilisateur.
     * @param motDePasse     Le mot de passe de l'utilisateur.
     * @param zoneDepart     La zone de départ où l'utilisateur commence le jeu.
     */
    public Utilisateurs(String nomUtilisateur, String motDePasse, Zone zoneDepart, Inventaire inventaire) {
        this.nomUtilisateur = nomUtilisateur;
        this.motDePasse = motDePasse;
        this.zoneCourante = zoneDepart;
        this.xPersonnage = 0;  // Valeurs par défaut pour les coordonnées du personnage
        this.yPersonnage = 0;
        this.inventaire = inventaire;

    }

    /**
     * Récupère le nom d'utilisateur de l'utilisateur.
     *
     * @return Le nom d'utilisateur.
     */
    public String getNomUtilisateur() {
        return nomUtilisateur;
    }

    /**
     * Récupère la zone courante de l'utilisateur.
     *
     * @return La zone courante sous forme de chaîne de caractères, ou "Zone inconnue" si la zone est nulle.
     */
    public String getZoneCourante() {
        return zoneCourante != null ? zoneCourante.toString() : "Zone inconnue";
    }

    /**
     * Définit une nouvelle zone pour l'utilisateur.
     *
     * @param nouvelleZone La nouvelle zone à affecter à l'utilisateur.
     */
    public void setZoneCourante(Zone nouvelleZone) {
        this.zoneCourante = nouvelleZone;
    }

    /**
     * Vérifie si le mot de passe fourni est correct.
     *
     * @param motDePasse Le mot de passe à vérifier.
     * @return {@code true} si le mot de passe est correct, {@code false} sinon.
     */
    public boolean verifierMotDePasse(String motDePasse) {
        return this.motDePasse.equals(motDePasse);
    }

    /**
     * Récupère la position actuelle du personnage en X.
     *
     * @return La coordonnée X du personnage.
     */
    public int getXPersonnage() {
        return xPersonnage;
    }

    /**
     * Récupère la position actuelle du personnage en Y.
     *
     * @return La coordonnée Y du personnage.
     */
    public int getYPersonnage() {
        return yPersonnage;
    }

    /**
     * Récupère l'inventaire de l'utilisateur.
     *
     * @return L'inventaire de l'utilisateur.
     */
    public Inventaire getInventaire() {
        return inventaire;
    }


    /**
     * Définit la nouvelle position en X du personnage.
     *
     * @param x La nouvelle coordonnée X du personnage.
     */
    public void setXPersonnage(int x) {
        this.xPersonnage = x;
    }

    /**
     * Définit la nouvelle position en Y du personnage.
     *
     * @param y La nouvelle coordonnée Y du personnage.
     */
    public void setYPersonnage(int y) {
        this.yPersonnage = y;
    }

    /**
     * Définit un nouvel inventaire pour l'utilisateur.
     *
     * @param inventaire L'inventaire à assigner à l'utilisateur.
     */
    public void setInventaire(Inventaire inventaire) {
        this.inventaire = inventaire;
    }

    /**
     * Récupère le mot de passe de l'utilisateur (utile pour les tests ou certaines validations).
     *
     * @return Le mot de passe de l'utilisateur.
     */
    public String getMotDePasse() {
        return motDePasse;
    }
}

