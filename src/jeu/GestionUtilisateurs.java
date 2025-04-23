package jeu;

import java.util.HashMap;
import java.util.Map;

/**
 * La classe GestionUtilisateurs permet de gérer l'inscription, la connexion,
 * et le suivi de l'utilisateur actuellement connecté.
 */
public class GestionUtilisateurs {

    /** Dictionnaire contenant tous les utilisateurs inscrits en mémoire */
    private final Map<String, Utilisateurs> utilisateurs = new HashMap<>();

    /** Référence vers l'utilisateur actuellement connecté */
    private Utilisateurs utilisateurConnecte;

    /**
     * Inscrit un nouvel utilisateur s'il n'existe pas déjà.
     *
     * @param nomUtilisateur Le nom d'utilisateur
     * @param motDePasse Le mot de passe
     * @param zoneDepart La zone de départ du joueur
     * @return true si l'inscription a réussi, false sinon
     */
    public boolean inscrire(String nomUtilisateur, String motDePasse, Zone zoneDepart) {
        // Vérification que le nom d'utilisateur et le mot de passe ne sont pas vides
        if (nomUtilisateur == null || nomUtilisateur.trim().isEmpty() || motDePasse == null || motDePasse.trim().isEmpty()) {
            return false; // L'un des champs est vide, donc l'inscription échoue
        }

        if (utilisateurs.containsKey(nomUtilisateur)) {
            return false; // L'utilisateur existe déjà en mémoire
        }

        // Sauvegarde en base de données
        if (Database.ajouterUtilisateur(nomUtilisateur, motDePasse)) {
            utilisateurs.put(nomUtilisateur, new Utilisateurs(nomUtilisateur, motDePasse, zoneDepart, new Inventaire()));
            return true;
        }

        return false; // Échec de l'ajout dans la base
    }

    /**
     * Connecte un utilisateur existant, après vérification du mot de passe.
     *
     * @param nomUtilisateur Le nom de l'utilisateur
     * @param motDePasse Le mot de passe à vérifier
     * @return true si la connexion réussit, false sinon
     */
    public boolean connecter(String nomUtilisateur, String motDePasse) {
        // Vérification que les champs de connexion ne sont pas vides
        if (nomUtilisateur == null || nomUtilisateur.trim().isEmpty() || motDePasse == null || motDePasse.trim().isEmpty()) {
            return false; // L'un des champs est vide, donc la connexion échoue
        }

        Utilisateurs utilisateur = Database.chargerUtilisateur(nomUtilisateur);
        if (utilisateur != null && utilisateur.verifierMotDePasse(motDePasse)) {
            utilisateurConnecte = utilisateur;
            return true;
        }
        return false;
    }

    /**
     * Récupère l'utilisateur actuellement connecté.
     *
     * @return L'utilisateur connecté, ou null s'il n'y en a pas
     */
    public Utilisateurs getUtilisateurConnecte() {
        return utilisateurConnecte;
    }
}
