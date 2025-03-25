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

    // Méthode pour ajouter un ingrédient à l'inventaire
    public void ajouterObjet(String nom) {
        boolean trouve = false;
        for (Ingredient ingredient : ingredients) {
            if (ingredient.getNom().equals(nom) && !ingredient.estRecupere()) {
                ingredient.recuperer();
                trouve = true;
                break;
            }
        }
        if (!trouve) {
            ingredients.add(new Ingredient(nom));  // Ajoute un nouvel ingrédient
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

    // Vérifie que l'ordre des ingrédients est correct
    public boolean aBonOrdreIngredients() {
        String[] ordreCorrect = {
                "Œuf de Serpentcendre",
                "Feuilles de Mandragore",
                "Chou mordeur de Chine",
                "Champignon Bleu",
                "Fleur de Brume"
        };

        List<String> ordreActuel = new ArrayList<>();
        for (Ingredient ingredient : ingredients) {
            if (ingredient.estRecupere()) {
                ordreActuel.add(ingredient.getNom());
            }
        }

        // Vérifie que l'ordre des ingrédients est correct
        return ordreActuel.equals(List.of(ordreCorrect));
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

    public List<Ingredient> getIngredients() {
        return ingredients;
    }


    // Méthode pour vérifier si un ingrédient est dans l'inventaire
    public boolean contient(String nom) {
        for (Ingredient ingredient : ingredients) {
            if (ingredient.getNom().equals(nom) && ingredient.estRecupere()) {
                return true;
            }
        }
        return false;
    }
    public void setObjets(List<String> nouveauxObjets) {
        ingredients.clear();
        for (String nom : nouveauxObjets) {
            ingredients.add(new Ingredient(nom));
        }
    }

    public List<String> getObjets() {
        List<String> objetsRecuperes = new ArrayList<>();
        for (Ingredient ingredient : ingredients) {
            if (ingredient.estRecupere()) {
                objetsRecuperes.add(ingredient.getNom());
            }
        }
        return objetsRecuperes;
    }


}
