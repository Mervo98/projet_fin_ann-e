package jeu;

public class Ingredient {
    private String nom;
    private boolean estRecupere;

    public Ingredient(String nom) {
        this.nom = nom;
        this.estRecupere = false;
    }

    public String getNom() {
        return nom;
    }

    public void recuperer() {
        this.estRecupere = true;
    }

    public boolean estRecupere() {
        return estRecupere;
    }
}
