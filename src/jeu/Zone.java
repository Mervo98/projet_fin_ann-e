package jeu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * La classe {@code Zone} représente une zone dans le jeu. Chaque zone a une description, une image associée,
 * des objets disponibles à récupérer, ainsi que des sorties vers d'autres zones.
 * Elle gère les objets récupérables et permet de définir les directions vers lesquelles l'utilisateur peut se déplacer.
 */
public class Zone {

    private String description;
    private String nomImage;
    private HashMap<String, Boolean> objetsDisponibles;  // Pour gérer les objets récupérables dans la zone
    private HashMap<String, Zone> sorties;   // Pour gérer les sorties vers les autres zones
    private Timer timer;  // Timer spécifique à la zone
    private int tempsRestant;  // Temps restant dans la zone


    /**
     * Constructeur de la classe {@code Zone}.
     * Initialise la zone avec une description, une image associée et un temps spécifique.
     *
     * @param description La description de la zone.
     * @param image Le nom de l'image associée à la zone.
     * @param tempsInitial Le temps initial pour cette zone (en secondes).
     */
    public Zone(String description, String image, int tempsInitial) {
        this.description = description;
        this.nomImage = image;
        this.sorties = new HashMap<>();
        this.objetsDisponibles = new HashMap<>();
        this.tempsRestant = tempsInitial;  // Temps spécifique pour cette zone
    }

    /**
     * Ajoute une sortie à cette zone.
     *
     * @param sortie La direction de la sortie (NORD, SUD, EST, OUEST, etc.).
     * @param zoneVoisine La zone voisine vers laquelle cette sortie mène.
     */
    public void ajouteSortie(Sortie sortie, Zone zoneVoisine) {
        sorties.put(sortie.name(), zoneVoisine);
    }


    /**
     * Ajoute un objet récupérable à cette zone.
     *
     * @param objet Le nom de l'objet à ajouter.
     */
    public void ajouteObjet(String objet) {
        objetsDisponibles.put(objet, false); // Par défaut, l'objet n'est pas récupéré
    }

    /**
     * Vérifie si un objet donné est présent dans cette zone.
     *
     * @param objet Le nom de l'objet à vérifier.
     * @return {@code true} si l'objet est présent, {@code false} sinon.
     */
    public boolean contientObjet(String objet) {
        return objetsDisponibles.containsKey(objet);
    }

    /**
     * Récupère la liste des objets non récupérés disponibles dans cette zone.
     *
     * @return La liste des objets non récupérés sous forme de liste de chaînes.
     */
    public List<String> getObjets() {
        return objetsDisponibles.entrySet().stream()
                .filter(entry -> !entry.getValue())  // Sélectionne les objets non récupérés
                .map(entry -> entry.getKey())  // Récupère uniquement le nom de l'objet
                .toList();
    }

    /**
     * Permet de récupérer un objet de la zone.
     *
     * @param objet Le nom de l'objet à récupérer.
     */
    public void prendreObjet(String objet) {
        if (objetsDisponibles.containsKey(objet) && !objetsDisponibles.get(objet)) {
            objetsDisponibles.put(objet, true);  // L'objet est désormais récupéré
        }
    }

    /**
     * Retourne une description détaillée de la zone, y compris ses sorties et objets disponibles.
     *
     * @return La description longue de la zone.
     */
    public String descriptionLongue() {
        return "Vous êtes dans " + description + "\nSorties : " + sorties() + "\nObjets disponibles : " + objetsDisponibles();
    }

    /**
     * Retourne une chaîne représentant les directions des sorties possibles depuis cette zone.
     *
     * @return Les directions des sorties sous forme de chaîne.
     */
    private String sorties() {
        return sorties.keySet().toString();
    }

    /**
     * Retourne une chaîne représentant les objets non récupérés disponibles dans cette zone.
     *
     * @return La liste des objets disponibles sous forme de chaîne.
     */
    private String objetsDisponibles() {
        StringBuilder objets = new StringBuilder();
        for (String objet : objetsDisponibles.keySet()) {
            if (!objetsDisponibles.get(objet)) { // Si l'objet n'est pas encore récupéré
                objets.append(objet).append(", ");
            }
        }
        return objets.length() > 0 ? objets.toString().substring(0, objets.length() - 2) : "Aucun objet disponible";
    }

    /**
     * Permet de récupérer une zone voisine en fonction d'une direction donnée.
     *
     * @param direction La direction de la sortie (NORD, SUD, EST, OUEST, etc.).
     * @return La zone voisine correspondante à la direction donnée, ou {@code null} si la direction est invalide.
     */
    public Zone obtientSortie(String direction) {
        return sorties.get(direction);
    }

    /**
     * Retourne le nom de l'image associée à cette zone.
     *
     * @return Le nom de l'image de la zone.
     */
    public String nomImage() {
        return nomImage;
    }

    /**
     * Retourne la description courte de la zone.
     *
     * @return La description courte de la zone.
     */
    public String toString() {
        return description;
    }

    /**
     * Permet de récupérer une zone par son nom.
     * Cette méthode est un exemple, il est possible de la personnaliser selon la structure de gestion des zones.
     *
     * @param nom Le nom de la zone à récupérer.
     * @return La zone correspondante à ce nom, ou {@code null} si la zone n'est pas trouvée.
     */
    public static Zone obtenirZoneParNom(String nom) {
        // Exemple de structure de données pour stocker les zones, à définir selon votre logique
        Map<String, Zone> zones = new HashMap<>();
        // Ajouter des zones à cette map (ou charger depuis une autre source)
        return zones.get(nom);  // Retourner la zone correspondante à ce nom
    }

    /**
     * Retourne le nom de la zone, qui est la description de la zone dans ce cas.
     *
     * @return Le nom de la zone.
     */
    public String getNom() {
        return description;
    }

    /**
     * Démarre un timer pour cette zone avec le temps initial spécifique.
     * Le timer décompte le temps restant chaque seconde.
     */
    public void startTimer(GUI gui) {
        // Ne démarrer le timer que si le temps restant est supérieur à zéro
        if (tempsRestant > 0 && (timer == null || !timer.isRunning())) {
            timer = new Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    tempsRestant--;  // Réduit le temps restant
                    gui.mettreAJourTempsRestant(tempsRestant); // Met à jour le label du temps restant dans l'interface

                    // Si le temps est écoulé
                    if (tempsRestant <= 0) {
                        timer.stop();  // Arrête le timer lorsque le temps est écoulé

                        // Afficher un message "Vous avez perdu"
                        JOptionPane.showMessageDialog(null, "Vous avez perdu ! Le temps est écoulé.", "Échec", JOptionPane.ERROR_MESSAGE);

                        System.out.println("Le temps est écoulé dans la zone " + description);
                    }
                }
            });
            timer.start();  // Démarre le timer
        }
    }


    /**
     * Arrête le timer pour cette zone.
     */
    public void stopTimer() {
        if (timer != null) {
            timer.stop();  // Arrête le timer
        }
    }

    /**
     * Retourne le temps restant dans la zone.
     *
     * @return Le temps restant en secondes.
     */
    public int getTempsRestant() {
        return tempsRestant;
    }


}
