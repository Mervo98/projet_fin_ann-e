package jeu;

public class Jeu {
    private GUI gui;
    private Enigme enigme;
    private Zone zoneCourante;

    private Zone[] zones;
    private int xPersonnage = 200, yPersonnage = 200; // Position initiale
    private Inventaire inventaire = new Inventaire(); // Inventaire du joueur

    public Jeu() {
        creerCarte();
        enigme = new Enigme(new Gui1());
    }

    public void setGUI(GUI g) {
        gui = g;
        afficherMessageDeBienvenue();
    }

    private void creerCarte() {
        zones = new Zone[5]; // Déclaration de l'array zones
        zones[0] = new Zone("Champ de Mandragore", "Champ_de_Mandragore.jpg");
        zones[1] = new Zone("Forêt Brune", "foret_brune.jpg");
        zones[2] = new Zone("Montagne Rocheuse", "Montagne_rocheuse.jpg");
        zones[3] = new Zone("Plantes Carnivores", "plante_carnivore.jpg");
        zones[4] = new Zone("Zone Volcanique", "zone_volcanique.jpg");

        // Ajout d'objets dans les zones
        zones[0].ajouteObjet("Feuilles de Mandragore");
        zones[1].ajouteObjet("Fleur de Brume");
        zones[2].ajouteObjet("Champignon Bleu");
        zones[3].ajouteObjet("Chou Mordeur de Chine");
        zones[4].ajouteObjet("Œuf de Serpentcendre");

        // Définir les sorties
        zones[0].ajouteSortie(Sortie.EST, zones[1]);
        zones[1].ajouteSortie(Sortie.OUEST, zones[0]);
        zones[1].ajouteSortie(Sortie.VOL, zones[2]);
        zones[2].ajouteSortie(Sortie.EST, zones[1]);
        zones[2].ajouteSortie(Sortie.VOL, zones[3]);
        zones[3].ajouteSortie(Sortie.NORD, zones[2]);
        zones[3].ajouteSortie(Sortie.VOL, zones[4]);
        zones[4].ajouteSortie(Sortie.SUD, zones[4]);

        zoneCourante = zones[1];
    }


    private void afficherLocalisation() {
        gui.afficher(zoneCourante.descriptionLongue());
        gui.afficher();
    }

    private void afficherMessageDeBienvenue() {
        gui.afficher("Bienvenue !");
        gui.afficher();
        gui.afficher("Tapez '?' pour obtenir de l'aide.");
        gui.afficher();
        afficherLocalisation();
        gui.afficheImage(zoneCourante.nomImage());

        // Positionne le personnage au début
        gui.deplacerPersonnage(xPersonnage, yPersonnage);
    }

    public void traiterCommande(String commandeLue) {
        gui.afficher("> " + commandeLue + "\n");

        switch (commandeLue.toUpperCase()) {
            case "?":
            case "AIDE":
                afficherAide();
                break;
            case "N":
            case "NORD":
                allerEn("NORD");
                break;
            case "S":
            case "SUD":
                allerEn("SUD");
                break;
            case "E":
            case "EST":
                allerEn("EST");
                break;
            case "O":
            case "OUEST":
                allerEn("OUEST");
                break;
            case "V":
            case "VOL":
                allerEn("VOL");
                break;
            case "Q":
            case "QUITTER":
                terminer();
                break;
            case "INVENTAIRE":
                gui.afficher("Voici votre inventaire :");
                gui.afficher(inventaire.afficherContenu());
                break;
            case "PRENDRE":
                if (zoneCourante.getObjets().isEmpty()) {
                    gui.afficher("Il n'y a rien à prendre ici.");
                } else {
                    String objet = zoneCourante.getObjets().get(0); // Prend le premier objet de la liste des objets disponibles
                    prendreObjet(objet);
                }
                break;

            case "JETER":
                if (zoneCourante.getObjets().isEmpty()) {
                    gui.afficher("Il n'y a rien à jeter ici.");
                } else {
                    String objet = zoneCourante.getObjets().get(0); // Prenez l'objet que vous souhaitez jeter
                    Zone zone2 = getZone(3); // Accéder à la "zone 2"
                    System.out.println( zone2);
                   prendreObjet(objet); // Retirer l'objet de la zone courante
                }
                break;



            default:

                // Ajoute les autres commandes supplémentaires ici après le "default"
                if (commandeLue.toUpperCase().startsWith("RÉPONDRE")) {
                    repondreEnigme(commandeLue);
                } else if (commandeLue.toUpperCase().startsWith("UTILISER")) {
                    utiliserObjet(commandeLue);
                } else if (commandeLue.toUpperCase().startsWith("RETIRER")) {
                    retourner(commandeLue);
                } else if (commandeLue.toUpperCase().startsWith("ENTRER")) {
                    entrer(commandeLue);
                } else if (commandeLue.toUpperCase().startsWith("SORTIR")) {
                    sortir(commandeLue);
                } else if (commandeLue.toUpperCase().startsWith("VOIR CARTE")) {
                    voirCarte();
                } else if (commandeLue.toUpperCase().startsWith("CRÉER FUMÉE")) {
                    creerFumee();
                } else if (commandeLue.toUpperCase().startsWith("CUEILLIR")) {
                    cueillir(commandeLue);
                } else if (commandeLue.toUpperCase().startsWith("OBSERVER")) {
                    observer(commandeLue);
                } else if (commandeLue.toUpperCase().startsWith("CHOISIR SYMBOLE")) {
                    choisirSymbole(commandeLue);
                } else {
                    gui.afficher("Commande inconnue");
                }
                break;
        }
    }


    private void afficherAide() {
        gui.afficher("Etes-vous perdu ?");
        gui.afficher("Les commandes autorisées sont :");
        // Afficher chaque description sur une nouvelle ligne
        for (String description : Commande.toutesLesDescriptions()) {
            gui.afficher(description);  // Affiche chaque commande sur une nouvelle ligne
        }

        gui.afficher();  // Ligne vide à la fin si nécessaire
    }

    private void allerEn(String direction) {
        Zone nouvelle = zoneCourante.obtientSortie(direction);
        if (nouvelle == null) {
            gui.afficher("Pas de sortie " + direction);
            gui.afficher();
        } else {
            zoneCourante = nouvelle;
            gui.afficher(zoneCourante.descriptionLongue());
            gui.afficher();
            gui.afficheImage(zoneCourante.nomImage());

            // Modifier la position du personnage en fonction de la direction
            switch (direction) {
                case "NORD":
                    yPersonnage -= 50;
                    break;
                case "SUD":
                    yPersonnage += 50;
                    break;
                case "EST":
                    xPersonnage += 50;
                    break;
                case "OUEST":
                    xPersonnage -= 50;
                    break;

                case "VOL":
                    xPersonnage -= 50;
                    break;
            }
            gui.deplacerPersonnage(xPersonnage, yPersonnage);
        }
    }

    private void terminer() {
        gui.afficher("Au revoir...");
        gui.enable(false);
    }

    // Commande Prendre un objet
    public void prendreObjet(String objet) {
        if (objet.isEmpty()) {
            gui.afficher("Que voulez-vous prendre ?");
            return;
        }

        if (zoneCourante.contientObjet(objet)) {
            switch (objet) {
                case "Feuilles de Mandragore":
                    if (enigme.jouerPierreFeuilleCiseaux()) {
                        gui.afficher("Vous avez pris : Feuilles de Mandragore.");
                        inventaire.recupererIngredient("Feuilles de Mandragore");
                        zoneCourante.prendreObjet("Feuilles de Mandragore");
                    } else {
                        gui.afficher("L'énigme n'est pas résolue.");
                    }
                    break;
                case "Fleur de Brume":
                    if (enigme.traverserBrume()) {
                        gui.afficher("Vous avez pris : Fleur de Brume.");
                        inventaire.recupererIngredient("Fleur de Brume");
                        zoneCourante.prendreObjet("Fleur de Brume");
                    } else {
                        gui.afficher("L'énigme n'est pas résolue.");
                    }
                    break;
                case "Champignon Bleu":
                    if (enigme.resoudrePuzzlePlantes()&& enigme.repondreDevinetteAbeilles() &&enigme.choisirSymboles() && enigme.recupererChampignonBleu()) {
                        gui.afficher("Vous avez pris : Champignon Bleu.");
                        inventaire.recupererIngredient("Champignon Bleu");
                        zoneCourante.prendreObjet("Champignon Bleu");
                    } else {
                        gui.afficher("L'énigme n'est pas résolue.");
                    }
                    break;
                case "Chou Mordeur de Chine":
                    if (enigme.distrairePlantes()) {
                        gui.afficher("Vous avez pris : Chou Mordeur de Chine.");
                        inventaire.recupererIngredient("Chou mordeur de Chine");
                        zoneCourante.prendreObjet("Chou mordeur de Chine");
                    } else {
                        gui.afficher("L'énigme n'est pas résolue.");
                    }
                    break;
                case "Œuf de Serpentcendre":
                    if (enigme.resoudreLabyrinthe()) {
                        gui.afficher("Vous avez pris : Œuf de Serpentcendre.");
                        inventaire.recupererIngredient("Œuf de Serpentcendre");
                        zoneCourante.prendreObjet("Œuf de Serpentcendre");
                    } else {
                        gui.afficher("L'énigme n'est pas résolue.");
                    }
                    break;
                default:
                    gui.afficher("Vous ne pouvez pas prendre cet objet.");
            }
        } else {
            gui.afficher("L'objet \"" + objet + "\" n'est pas dans cette zone.");

            // Suggérer les objets disponibles dans la zone
            if (!zoneCourante.getObjets().isEmpty()) {
                gui.afficher("Objets disponibles : " + String.join(", ", zoneCourante.getObjets()));
            }
        }
    }

    public Zone getZone(int index) {
        if (index >= 0 && index < zones.length) {
            return zones[index];
        } else {
            return null;  // Retourne null si l'index est invalide
        }
    }

    // Commande Répondre à une énigme
    private void repondreEnigme(String commande) {
        if (commande.contains("fumée")) {
            gui.afficher("Vous avez répondu correctement à l'énigme !");
            inventaire.ajouterObjet("Fleur de Brume");
        } else {
            gui.afficher("Réponse incorrecte, essayez encore.");
        }
    }

    // Commande Jeter un objet
    private void jeterObjet(String commande) {
        if (commande.contains("pomme")) {
            gui.afficher("Vous avez jeté une pomme pour attirer les plantes carnivores.");
            // Logique de jet d'objet, par exemple : attirer l'attention des plantes.
        } else {
            gui.afficher("Objet inconnu à jeter.");
        }
    }

    // Commande Utiliser un objet
    private void utiliserObjet(String commande) {
        if (commande.contains("torche")) {
            gui.afficher("Vous utilisez la torche pour éclairer votre chemin.");
            // Logique pour l'utilisation de la torche.
        } else {
            gui.afficher("Objet inconnu à utiliser.");
        }
    }

    // Commande Retourner (revenir en arrière)
    private void retourner(String commande) {
        // Logique pour revenir en arrière dans la zone précédente.
        gui.afficher("Vous retournez à la zone précédente.");
    }

    // Commande Entrer dans une zone
    private void entrer(String commande) {
        if (commande.contains("volcan")) {
            gui.afficher("Vous entrez dans la zone volcanique.");
        } else {
            gui.afficher("Zone inconnue à entrer.");
        }
    }

    // Commande Sortir d'une zone
    private void sortir(String commande) {
        if (commande.contains("volcan")) {
            gui.afficher("Vous sortez de la zone volcanique.");
        } else {
            gui.afficher("Zone inconnue à sortir.");
        }
    }

    // Commande Voir Carte
    private void voirCarte() {
        gui.afficher("Voici la carte du monde :");
        // Logique pour afficher la carte.
    }

    // Commande Inventaire
    private void afficherInventaire() {
        gui.afficher("Voici votre inventaire :");
        gui.afficher(inventaire.afficherContenu());
    }

    // Commande Créer Fumée
    private void creerFumee() {
        if (inventaire.contient("torche") && inventaire.contient("bois")) {
            gui.afficher("Vous avez créé de la fumée avec la torche et le bois.");
        } else {
            gui.afficher("Il vous manque un ou plusieurs objets pour créer de la fumée.");
        }
    }

    // Commande Cueillir une plante
    private void cueillir(String commande) {
        if (commande.contains("ChouMordeur")) {
            gui.afficher("Vous cueillez un Chou Mordeur.");
        } else {
            gui.afficher("Plante inconnue à cueillir.");
        }
    }

    // Commande Observer un objet ou environnement
    private void observer(String commande) {
        if (commande.contains("plantes")) {
            gui.afficher("Vous observez les plantes et repérez une Fleur de Brume.");
        } else {
            gui.afficher("Objet ou environnement inconnu à observer.");
        }
    }

    // Commande Choisir un Symbole
    private void choisirSymbole(String commande) {
        if (commande.contains("lumière")) {
            gui.afficher("Vous avez choisi le symbole de la lumière.");
        } else {
            gui.afficher("Symbole inconnu.");
        }
    }
}
