package jeu;

import java.util.HashMap;
import java.util.Map;

public class GestionUtilisateurs {
    private Map<String, Utilisateurs> utilisateurs = new HashMap<>();
    private Utilisateurs utilisateurConnecte;

    // Inscription d'un nouvel utilisateur
    public boolean inscrire(String nomUtilisateur, String motDePasse, Zone zoneDepart) {
        if (utilisateurs.containsKey(nomUtilisateur)) {
            return false; // L'utilisateur existe déjà
        }

        // Sauvegarder dans la base de données
        if (Database.ajouterUtilisateur(nomUtilisateur, motDePasse)) {
            utilisateurs.put(nomUtilisateur, new Utilisateurs(nomUtilisateur, motDePasse, zoneDepart));
            return true;
        }

        return false; // Si l'ajout dans la base échoue
    }

    // Connexion d'un utilisateur existant
    public boolean connecter(String nomUtilisateur, String motDePasse) {
        Utilisateurs utilisateur = Database.chargerUtilisateur(nomUtilisateur); // Charger depuis la base de données
        if (utilisateur != null && utilisateur.verifierMotDePasse(motDePasse)) {
            utilisateurConnecte = utilisateur;
            return true;
        }
        return false;
    }

    public Utilisateurs getUtilisateurConnecte() {
        return utilisateurConnecte;
    }
}
