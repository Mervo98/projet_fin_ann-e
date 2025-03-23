package jeu;

public class Personnage {
    private String nom;
    private String description;
    private Zone position;
    private String imagePersonnage;

    public Personnage(String nom, String description, Zone position) {
        this.nom = nom;
        this.description = description;
        this.position = position;
        // Initialisation d'une image par défaut
        this.imagePersonnage = "theman.png"; // Exemple
    }

    public String getNom() {
        return nom;
    }

    public String getDescription() {
        return description;
    }

    public Zone getPosition() {
        return position;
    }

    public void deplacer(Zone nouvelleZone, String direction) {
        this.position = nouvelleZone;
        changerImage(direction);  // Met à jour l'image selon la direction
    }

    public String getImagePersonnage() {
        return imagePersonnage;
    }

    // Mettre à jour l'image en fonction de la direction
    private void changerImage(String direction) {
        switch (direction.toUpperCase()) {
            case "NORD":
                imagePersonnage = "personnage_haut.png";
                break;
            case "SUD":
                imagePersonnage = "personnage_bas.png";
                break;
            case "OUEST":
                imagePersonnage = "personnage_gauche.png";
                break;
            case "EST":
                imagePersonnage = "personnage_droite.png";
                break;
            default:
                imagePersonnage = "personnage_bas.png"; // Par défaut
                break;
        }
    }
}
