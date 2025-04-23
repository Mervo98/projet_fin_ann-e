package jeu;

import java.util.ArrayList;
import java.util.List;

public class Inventaire {
    private List<Ingredient> ingredients;

    public Inventaire() {
        ingredients = new ArrayList<>();
        // Initialisation avec quelques ingrédients
        ingredients.add(new Ingredient("Œuf de Serpentcendre"));
        ingredients.add(new Ingredient("Feuilles de Mandragore"));
        ingredients.add(new Ingredient("Chou mordeur de Chine"));
        ingredients.add(new Ingredient("Champignon Bleu"));
        ingredients.add(new Ingredient("Fleur de Brume"));
    }

    // Méthode pour récupérer un ingrédient spécifique
    public void recupererIngredient(String nom) {
        for (Ingredient ingredient : ingredients) {
            if (ingredient.getNom().equals(nom) && !ingredient.estRecupere()) {
                ingredient.recuperer();
                break;
            }
        }
    }


    // Vérifie si l'inventaire a tous les ingrédients
    public boolean aTousLesIngredients() {
        for (Ingredient ingredient : ingredients) {
            if (!ingredient.estRecupere()) {
                return false;
            }
        }
        return true;
    }


    // Méthode pour afficher l'inventaire
    public String afficherContenu() {
        StringBuilder contenu = new StringBuilder();
        for (Ingredient ingredient : ingredients) {
            contenu.append(ingredient.getNom())
                    .append(ingredient.estRecupere() ? " (Récupéré)" : " (Non récupéré)") // Affiche si récupéré ou non
                    .append("\n");
        }
        return contenu.toString();
    }




    public boolean contientIngredientRecupere(String nom) {
        for (Ingredient ingredient : ingredients) {
            if (ingredient.getNom().equals(nom) && ingredient.estRecupere()) {
                return true;
            }
        }
        return false;
    }


}
