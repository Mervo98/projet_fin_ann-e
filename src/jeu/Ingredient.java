package jeu;

/**
 * Représente un ingrédient dans le jeu. Un ingrédient possède un nom et un état
 * indiquant s'il a été récupéré par le joueur.
 */
public class Ingredient {
    private final String nom;
    private boolean estRecupere;

    /**
     * Constructeur pour créer un ingrédient avec un nom.
     *
     * @param nom Le nom de l'ingrédient
     */
    public Ingredient(String nom) {
        this.nom = nom;
        this.estRecupere = false; // Par défaut, l'ingrédient n'est pas récupéré
    }

    /**
     * Récupère le nom de l'ingrédient.
     *
     * @return Le nom de l'ingrédient
     */
    public String getNom() {
        return nom;
    }

    /**
     * Marque l'ingrédient comme récupéré.
     */
    public void recuperer() {
        this.estRecupere = true;
    }

    /**
     * Vérifie si l'ingrédient a été récupéré.
     *
     * @return true si l'ingrédient a été récupéré, false sinon
     */
    public boolean estRecupere() {
        return estRecupere;
    }
}
