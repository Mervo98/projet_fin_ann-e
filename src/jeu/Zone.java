package jeu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Zone {
    private String description;
    private String nomImage;
    private HashMap<String, Boolean> objetsDisponibles;  // Pour gérer les objets récupérables dans la zone
    private HashMap<String, Zone> sorties;   // Pour gérer les sorties vers les autres zones

    public Zone(String description, String image) {
        this.description = description;
        this.nomImage = image;
        this.sorties = new HashMap<>();
        this.objetsDisponibles = new HashMap<>();
    }

    // Ajoute une sortie (direction, zone voisine)
    public void ajouteSortie(Sortie sortie, Zone zoneVoisine) {
        sorties.put(sortie.name(), zoneVoisine);
    }

    // Ajoute un objet à cette zone
    public void ajouteObjet(String objet) {
        objetsDisponibles.put(objet, false); // Par défaut, l'objet n'est pas récupéré
    }

    // Vérifie si un objet est dans cette zone
    public boolean contientObjet(String objet) {
        return objetsDisponibles.containsKey(objet);
    }

    public List<String> getObjets() {
        return objetsDisponibles.entrySet().stream()
                .filter(entry -> !entry.getValue())  // Sélectionne les objets non récupérés
                .map(entry -> entry.getKey())  // Récupère uniquement le nom de l'objet
                .toList();
    }

    // Permet de récupérer un objet
    public void prendreObjet(String objet) {
        if (objetsDisponibles.containsKey(objet) && !objetsDisponibles.get(objet)) {
            objetsDisponibles.put(objet, true);  // L'objet est désormais récupéré
        }
    }

    // Méthode pour obtenir la description longue de la zone
    public String descriptionLongue()  {
        return "Vous êtes dans " + description + "\nSorties : " + sorties() + "\nObjets disponibles : " + objetsDisponibles();
    }

    // Retourne les sorties possibles sous forme de chaîne
    private String sorties() {
        return sorties.keySet().toString();
    }

    // Retourne les objets disponibles sous forme de chaîne
    private String objetsDisponibles() {
        StringBuilder objets = new StringBuilder();
        for (String objet : objetsDisponibles.keySet()) {
            if (!objetsDisponibles.get(objet)) { // Si l'objet n'est pas encore récupéré
                objets.append(objet).append(", ");
            }
        }
        return objets.length() > 0 ? objets.toString().substring(0, objets.length() - 2) : "Aucun objet disponible";
    }

    // Méthode pour obtenir une sortie par direction
    public Zone obtientSortie(String direction) {
        return sorties.get(direction);
    }

    // Retourne le nom de l'image de la zone
    public String nomImage() {
        return nomImage;
    }

    // Retourne la description courte de la zone
    public String toString() {
        return description;
    }
    public static Zone obtenirZoneParNom(String nom) {
        // Exemple de structure de données pour stocker les zones, à définir selon votre logique
        Map<String, Zone> zones = new HashMap<>();
        // Ajouter des zones à cette map (ou charger depuis une autre source)
        return zones.get(nom);  // Retourner la zone correspondante à ce nom
    }

    public String getNom() {
        return description;
    }


}
