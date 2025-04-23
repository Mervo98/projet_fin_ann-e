
package jeu;

import javax.swing.*;


public class Jeu {
    private GUI gui;
    private Utilisateurs utilisateur;
    private Zone zoneCourante;
    private final GestionUtilisateurs gestionUtilisateurs = new GestionUtilisateurs();

    private Zone[] zones;
    private int xPersonnage, yPersonnage; // sera initialisé avec les données de l'utilisateur
    private Inventaire inventaire;
    private Zone ancienneZone;

    public Jeu() {

        Database.creerTable(); // Créer la table si nécessaire
        creerCarte();
        utilisateur = demanderConnexion(); // retourne maintenant l'utilisateur connecté

        if (utilisateur == null) {
            System.out.println("Erreur : aucun utilisateur connecté.");
            System.exit(1);
        }


        //  Initialisation des données utilisateur dans le jeu :
        this.inventaire = utilisateur.getInventaire(); // Inventaire depuis la base
        this.xPersonnage = utilisateur.getXPersonnage();
        this.yPersonnage = utilisateur.getYPersonnage();

    }




    /**
     * Gère la connexion ou l’inscription de l'utilisateur. Retourne un utilisateur connecté ou null.
     *
     * @return L'utilisateur connecté ou null si la connexion échoue.
     */
    private Utilisateurs demanderConnexion() {
        // Afficher un message de bienvenue
        JOptionPane.showMessageDialog(null, "Bienvenue dans le jeu d'aventure !");

        while (true) {
            // Demander l'option de connexion ou d'inscription
            String[] options = {"Connexion", "Inscription"};
            int choix = JOptionPane.showOptionDialog(null,
                    "Choisissez une option :",
                    "Connexion / Inscription",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]);

            // Si l'utilisateur a choisi "Connexion"
            if (choix == 0) {
                String nomUtilisateur = JOptionPane.showInputDialog("Nom d'utilisateur :");
                String motDePasse = JOptionPane.showInputDialog("Mot de passe :");

                if (gestionUtilisateurs.connecter(nomUtilisateur, motDePasse)) {
                    Utilisateurs utilisateurConnecte = gestionUtilisateurs.getUtilisateurConnecte();

                    // Vérifier la validité de la zone
                    if (utilisateurConnecte.getZoneCourante() == null || utilisateurConnecte.getZoneCourante().equals("Zone inconnue")) {
                        if (zones.length > 1) {
                            utilisateurConnecte.setZoneCourante(zones[1]);
                        } else {
                            JOptionPane.showMessageDialog(null, "Erreur : aucune zone disponible.");
                            continue;
                        }
                    }

                    // Afficher le message de bienvenue après la connexion
                    JOptionPane.showMessageDialog(null,
                            "Bienvenue " + utilisateurConnecte.getNomUtilisateur() + " !\n" +
                                    "Dans un village, un héros doit trouver des ingrédients afin de créer un remède pour\n" +
                                    "purifier l’eau et sauver son village.\n\n" +
                                    "Et cette personne, c'est toi, " + utilisateurConnecte.getNomUtilisateur() + " !\n\nBonne chance !",
                            "Message", JOptionPane.INFORMATION_MESSAGE);

                    return utilisateurConnecte;
                } else {
                    JOptionPane.showMessageDialog(null, "Nom d'utilisateur ou mot de passe incorrect.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
            // Si l'utilisateur a choisi "Inscription"
            else if (choix == 1) {
                String nomUtilisateur = JOptionPane.showInputDialog("Nom d'utilisateur :");
                String motDePasse = JOptionPane.showInputDialog("Mot de passe :");

                if (zones.length > 1 && gestionUtilisateurs.inscrire(nomUtilisateur, motDePasse, zones[1])) {
                    JOptionPane.showMessageDialog(null, "Inscription réussie ! Vous pouvez maintenant vous connecter.");
                } else {
                    JOptionPane.showMessageDialog(null, "Nom d'utilisateur déjà pris ou erreur d'inscription.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Option invalide. Veuillez choisir 'Connexion' ou 'Inscription'.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    /**
     * Quand le joueur entre dans une nouvelle zone, le timer de l'ancienne zone est arrêté
     * et celui de la nouvelle zone est démarré avec son temps spécifique.
     *
     * @param nouvelleZone La zone dans laquelle le joueur entre.
     */
    public void changerZone(Zone nouvelleZone) {
        // Arrêter le timer de l'ancienne zone si elle existe
        if (ancienneZone != null) {
            ancienneZone.stopTimer();
        }

        // Passer à la nouvelle zone
        zoneCourante = nouvelleZone;

        // Démarrer le timer de la nouvelle zone avec son temps spécifique
        zoneCourante.startTimer(gui);

        // Mettre à jour l'ancienne zone
        ancienneZone = zoneCourante;


        // Mettre à jour le temps restant dans l'interface
        gui.mettreAJourTempsRestant(zoneCourante.getTempsRestant());
    }





    /**
     * Termine le jeu en sauvegardant l'utilisateur et en mettant à jour ses informations dans la base de données.
     */
    private void terminer() {
        if (utilisateur != null) {
            Database.sauvegarderUtilisateur(utilisateur);
            gui.afficher("Déconnexion réussie.");
            Database.mettreAJourUtilisateur(utilisateur);
        }
        gui.enable(false);
        System.exit(0);
    }

    /**
     * Définit l'interface graphique du jeu.
     *
     * @param g L'objet GUI utilisé pour afficher les informations du jeu.
     */
    public void setGUI(GUI g) {
        gui = g;
        afficherMessageDeBienvenue();
    }

    /**
     * Crée la carte du jeu avec différentes zones et objets.
     */
    private void creerCarte() {
        zones = new Zone[6];
        // Déclaration de l'array zones
        zones[0] = new Zone("Champ de Mandragore", "Champ_de_Mandragore.jpg",456);
        zones[1] = new Zone("Forêt Brune", "foret_brune.jpg",500);
        zones[2] = new Zone("Montagne Rocheuse", "Montagne_rocheuse.jpg",456);
        zones[3] = new Zone("Plantes Carnivores", "plante_carnivore.jpg",455);
        zones[4] = new Zone("Zone Volcanique", "zone_volcanique.jpg",467);
        zones[5] = new Zone("Le village", "village.png",0);


        // Ajout d'objets dans les zones"
        zones[0].ajouteObjet("Feuilles de Mandragore");
        zones[1].ajouteObjet("Fleur de Brume");
        zones[2].ajouteObjet("Champignon Bleu");
        zones[3].ajouteObjet("Chou mordeur de Chine");
        zones[4].ajouteObjet("Œuf de Serpentcendre");
        zones[5].ajouteObjet("voici les 5 objets diponible \n 1:Feuilles de Mandragore \n 2:Fleur de Brume \n 3:Champignon Bleu \n 4:Chou Mordeur de Chine\n 5:Œuf de Serpentcendre \n RETROUVEZ LES");

        // Définir les sorties


        zones[0].ajouteSortie(Sortie.SUD, zones[5]);
        zones[0].ajouteSortie(Sortie.EST, zones[1]);

        //zones[1].ajouteSortie(Sortie.OUEST, zones[0]);
        zones[1].ajouteSortie(Sortie.OUEST, zones[2]);
        zones[1].ajouteSortie(Sortie.SUD, zones[5]);

        zones[2].ajouteSortie(Sortie.EST, zones[3]);
       // zones[2].ajouteSortie(Sortie.NORD, zones[1]);
        zones[2].ajouteSortie(Sortie.SUD, zones[5]);

        //zones[3].ajouteSortie(Sortie.OUEST, zones[2]);
        zones[3].ajouteSortie(Sortie.NORD, zones[4]);
        zones[3].ajouteSortie(Sortie.SUD, zones[5]);


       // zones[4].ajouteSortie(Sortie.NORD, zones[3]);
        zones[4].ajouteSortie(Sortie.SUD, zones[5]);

        //zones[5].ajouteSortie(Sortie.SUD, zones[4]);
        zones[5].ajouteSortie(Sortie.NORD, zones[0]);

        zoneCourante = zones[5];  // Le joueur commence dans la zone 5 (Le village)
    }

    /**
     * Affiche un message de bienvenue et les informations liées à la zone courante de l'utilisateur.
     */
    private void afficherMessageDeBienvenue() {
        Utilisateurs user = Database.chargerUtilisateur(utilisateur.getNomUtilisateur());
        assert user != null;
        gui.afficher("Bienvenue !" + user.getNomUtilisateur());
        gui.afficher("VOICI LA CARTE DU VILLAGE CI-DESSUS");
        gui.afficher("Tapez '?' pour obtenir de l'aide.");
        gui.afficheImage(zoneCourante.nomImage());
        gui.deplacerPersonnage(xPersonnage, yPersonnage);

    }


    /**
     * Retourne au village si le joueur possède tous les ingrédients nécessaires.
     */
    public void retourAuVillage() {
        if (!inventaire.aTousLesIngredients()) {
            gui.afficher("Vous n'avez pas tous les ingrédients. Revenez chercher les ingrédients manquants.");
        } else {
            gui.afficher("Vous retournez au village avec tous les ingrédients.");
            allerEn("SUD");

        }
    }

    /**
     * Vérifie l'ordre des ingrédients dans l'inventaire du joueur.
     */
    public void verifierOrdreIngredients() {
        // Demander à l'utilisateur de saisir l'ordre des ingrédients
        if (inventaire.aTousLesIngredients()) {
            String message = "Saisissez l'ordre des ingrédients dans la potion (1 à 5) :\n" +
                    "1. Œuf de Serpentcendre\n" +
                    "2. Feuilles de Mandragore\n" +
                    "3. Chou mordeur de Chine\n" +
                    "4. Champignon Bleu\n" +
                    "5. Fleur de Brume\n" +
                    "Veuillez entrer l'ordre sous la forme : 3,4,5,1,2";

            // Afficher une fenêtre de saisie pour obtenir l'ordre de l'utilisateur
            String saisie = JOptionPane.showInputDialog(null, message, "Vérification de l'ordre", JOptionPane.QUESTION_MESSAGE);

            if (saisie != null && !saisie.isEmpty()) {
                if (verifierSaisie(saisie)) {
                    JOptionPane.showMessageDialog(null, "Bravo ! Vous avez réussi à préparer la potion.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                    terminer();
                } else {
                    JOptionPane.showMessageDialog(null, "Échec ! Vous avez perdu.", "Échec", JOptionPane.ERROR_MESSAGE);
                    terminer();

                }
            } else {
                JOptionPane.showMessageDialog(null, "Aucune saisie détectée. Veuillez réessayer.", "Erreur", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            gui.afficher("Veuillez retourner au village avec tous les ingrédients avant de préparer la potion.");
        }
    }

    /**
     * Vérifie si la saisie de l'utilisateur correspond à l'ordre correct des ingrédients.
     *
     * @param saisie La saisie de l'utilisateur à vérifier.
     * @return true si l'ordre est correct, false sinon.
     */
    private boolean verifierSaisie(String saisie) {
        // L'ordre correct est "3,4,5,1,2"
        return saisie.trim().equals("3,4,5,1,2");
    }

    /**
     * Déplace le personnage vers la fleur de brume.
     */
    private void allerfleur(){
        xPersonnage=259;
        yPersonnage=259;

        gui.deplacerPersonnage(xPersonnage,yPersonnage);
        gui.afficher("Vous vous dirigez vers la fleur.");
    }

    /**
     * Déplace le personnage vers un premier arbre dans le jeu.
     */
    private void arbre1(){
        xPersonnage=45;
        yPersonnage=437;

        gui.deplacerPersonnage(xPersonnage,yPersonnage);
        gui.afficher("Vous vous dirigez vers la fleur.");

    }

    /**
     * Déplace le personnage vers la mandragore.
     */
    private void mondra(){
        xPersonnage=226;
        yPersonnage=217;

        gui.deplacerPersonnage(xPersonnage,yPersonnage);
        gui.afficher("Vous vous dirigez vers la fleur.");

    }

    /**
     * Déplace le personnage vers le champignon bleu.
     */
    private void champblue(){
        xPersonnage=431;
        yPersonnage=371;

        gui.deplacerPersonnage(xPersonnage,yPersonnage);
        gui.afficher("Vous vous dirigez vers la fleur.");

    }

    /**
     * Déplace le personnage vers le chou mordeur de Chine.
     */
    private void chouMord(){
        xPersonnage=258;
        yPersonnage=242;

        gui.deplacerPersonnage(xPersonnage,yPersonnage);
        gui.afficher("Vous vous dirigez vers la fleur.");

    }

    /**
     * Déplace le personnage vers l'œuf du serpentcendre.
     */
    private void oeuf(){
        xPersonnage=366;
        yPersonnage=98;

        gui.deplacerPersonnage(xPersonnage,yPersonnage);
        gui.afficher("Vous vous dirigez vers la fleur.");

    }
    /**
     * Traite la commande lue et exécute l'action correspondante.
     *
     * @param commandeLue La commande saisie par l'utilisateur.
     */
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
                gui.mettreAJourTempsRestant(zoneCourante.getTempsRestant()); // Met à jour le temps restant
                break;
            case "S":
            case "SUD":
                allerEn("SUD");
                gui.mettreAJourTempsRestant(zoneCourante.getTempsRestant()); // Met à jour le temps restant
                break;
            case "E":
            case "EST":
                allerEn("EST");
                gui.mettreAJourTempsRestant(zoneCourante.getTempsRestant()); // Met à jour le temps restant
                break;
            case "O":
            case "OUEST":
                allerEn("OUEST");
                gui.mettreAJourTempsRestant(zoneCourante.getTempsRestant()); // Met à jour le temps restant
                break;
            case "Q":
            case "QUITTER":
                terminer();
                break;
            case "RETOUR":
                retourAuVillage();
                break;

            case "INVENTAIRE":
                gui.afficher("Voici votre inventaire :");

                // Recharge l'utilisateur depuis la BDD avec son inventaire à jour
                Utilisateurs utilisateurMisAJour = Database.chargerUtilisateur(utilisateur.getNomUtilisateur());

                if (utilisateurMisAJour != null) {
                    this.utilisateur = utilisateurMisAJour;
                    this.inventaire = utilisateur.getInventaire();
                }

                gui.afficher(utilisateur.getInventaire().afficherContenu());
                break;

            case "PRENDRE":
                if (zoneCourante.getObjets().isEmpty()) {
                    gui.afficher("Il n'y a rien à prendre ici.");
                } else if (zoneCourante != getZone(3)) {
                    String objet = zoneCourante.getObjets().get(0);
                    prendreObjet(objet);

                    Database.mettreAJourUtilisateur(utilisateur);
                } else {
                    gui.afficher("Vous ne pouvez pas utiliser la commande (PRENDRE) ici,\n utiliser la commande JETER.");
                }
                break;

            case "PREPARER":
                verifierOrdreIngredients();
                break;

            case "FLEUR":
                allerfleur();
                break;

            case "JETER":
                if (zoneCourante == getZone(3)) {
                    if (zoneCourante.getObjets().isEmpty()) {
                        gui.afficher("Il n'y a rien à jeter ici.");
                    } else {
                        String objet = zoneCourante.getObjets().get(0);
                        prendreObjet(objet);
                        gui.afficher("Vous avez jeté une pomme.");
                    }
                } else {
                    gui.afficher("Vous ne pouvez jeter des objets que dans cette zone !");
                }
                break;
        }
    }

    /**
     * Affiche l'aide disponible pour le joueur, y compris les commandes et les sorties disponibles.
     */
    private void afficherAide() {
        gui.afficher("Etes-vous perdu ?");
        gui.afficher("Vous êtes actuellement dans la zone : " + zoneCourante.getNom());

        // Définir les sorties spécifiques par zone
        String sortiesDisponibles = getSortiesDisponibles(zoneCourante.getNom());
        gui.afficher("Sorties disponibles :");
        gui.afficher(sortiesDisponibles);

        // Afficher les commandes disponibles
        gui.afficher("\nCommandes disponibles :");
        gui.afficher(" - NORD / SUD / EST / OUEST : Déplacement");
        gui.afficher(" - INVENTAIRE : Voir votre inventaire");
        gui.afficher(" - PRENDRE : Prendre un objet");
        gui.afficher(" - RETOUR : Retourner au village");
        gui.afficher(" - QUITTER : Quitter le jeu");

        // Commandes spéciales selon la zone
        if (zoneCourante.getNom().equals("Plantes Carnivores")) {
            gui.afficher(" - JETER : Jeter un objet");
        }

        gui.afficher();  // Ligne vide à la fin
    }

    /**
     * Retourne les sorties disponibles sous forme de chaîne pour une zone donnée.
     *
     * @param zoneNom Le nom de la zone pour laquelle on veut afficher les sorties.
     * @return La chaîne représentant les sorties disponibles.
     */
    private String getSortiesDisponibles(String zoneNom) {
        StringBuilder sorties = new StringBuilder();

        switch (zoneNom) {
            case "Champ de Mandragore":
                sorties.append(" - SUD\n - EST");
                break;
            case "Forêt Brune":
                sorties.append(" - SUD\n - OUEST");
                break;
            case "Montagne Rocheuse":
                sorties.append(" - EST\n - SUD\n ");
                break;
            case "Plantes Carnivores":
                sorties.append(" - NORD\n - SUD");
                break;
            case "Zone Volcanique":
                sorties.append(" - SUD\n");
                break;
            case "Le village":
                sorties.append(" - NORD");
                break;
            default:
                sorties.append("Aucune sortie définie.");
                break;
        }

        return sorties.toString();
    }

    /**
     * Déplace le personnage dans la direction spécifiée.
     *
     * @param direction La direction dans laquelle le personnage doit se déplacer.
     */
    private void allerEn(String direction) {
        // Récupérer la nouvelle zone en fonction de la direction
        Zone nouvelle = zoneCourante.obtientSortie(direction);
        if (nouvelle == null) {
            gui.afficher("Pas de sortie " + direction);
            gui.afficher();
        } else {
            // Changer la zone courante
            zoneCourante = nouvelle;
            changerZone(nouvelle);
            gui.afficher(zoneCourante.descriptionLongue());
            gui.afficher();
            gui.afficheImage(zoneCourante.nomImage());

            // Modifier la position du personnage en fonction de la direction
            switch (direction) {
                case "NORD":
                    yPersonnage = 50;
                    break;
                case "SUD":
                    yPersonnage = 50;
                    break;
                case "EST":
                    xPersonnage = 50;
                    break;
                case "OUEST":
                    xPersonnage = 50;
                    break;
                case "VOL":
                    xPersonnage = 50;
                    break;
            }

            // Mettre à jour la position du personnage dans l'interface graphique
            gui.deplacerPersonnage(xPersonnage, yPersonnage);

            // Mise à jour de la zone de l'utilisateur dans la base de données
            utilisateur.setZoneCourante(zoneCourante);
            Database.mettreAJourUtilisateur(utilisateur); // Cette méthode va mettre à jour la zone dans la base de données
        }
    }



    /**
     * Permet de prendre un objet dans la zone courante.
     *
     * @param objet Le nom de l'objet à prendre.
     */
    public void prendreObjet(String objet) {
        if (objet.isEmpty()) {
            gui.afficher("Que voulez-vous prendre ?");
            return;
        }

        if (!zoneCourante.contientObjet(objet)) {
            gui.afficher("L'objet \"" + objet + "\" n'est pas dans cette zone.");
            if (!zoneCourante.getObjets().isEmpty()) {
                gui.afficher("Objets disponibles : " + String.join(", ", zoneCourante.getObjets()));
            }
            return;
        }

        // 🔒 Vérifie si l'objet a déjà été pris (récupéré) dans l'inventaire
        Inventaire inventaire = utilisateur.getInventaire();
        if (inventaire.contientIngredientRecupere(objet)) {
            gui.afficher("Vous avez déjà récupéré cet ingrédient.");
            return;
        }

        switch (objet) {
            case "Feuilles de Mandragore":
                if (jouerPierreFeuilleCiseaux()) {
                    gui.afficher("Vous avez pris : Feuilles de Mandragore.");
                    inventaire.recupererIngredient("Feuilles de Mandragore");
                    zoneCourante.prendreObjet("Feuilles de Mandragore");
                    zoneCourante.stopTimer();  // Arrêter le timer de la zone
                } else {
                    gui.afficher("L'énigme n'est pas résolue.");
                }
                break;

            case "Fleur de Brume":
                if (traverserBrume()) {
                    gui.afficher("Vous avez pris : Fleur de Brume.");
                    inventaire.recupererIngredient("Fleur de Brume");
                    zoneCourante.prendreObjet("Fleur de Brume");
                    zoneCourante.stopTimer();  // Arrêter le timer de la zone
                } else {
                    gui.afficher("L'énigme n'est pas résolue.");
                }
                break;

            case "Champignon Bleu":
                if (resoudrePuzzlePlantes() && repondreDevinetteAbeilles() && choisirSymboles() && recupererChampignonBleu()) {
                    gui.afficher("Vous avez pris : Champignon Bleu.");
                    inventaire.recupererIngredient("Champignon Bleu");
                    zoneCourante.prendreObjet("Champignon Bleu");
                    zoneCourante.stopTimer();  // Arrêter le timer de la zone
                } else {
                    gui.afficher("L'énigme n'est pas résolue.");
                }
                break;

            case "Chou mordeur de Chine":
                if (distrairePlantes()) {
                    gui.afficher("Vous avez pris : Chou mordeur de Chine.");
                    inventaire.recupererIngredient("Chou mordeur de Chine");
                    zoneCourante.prendreObjet("Chou mordeur de Chine");
                    zoneCourante.stopTimer();  // Arrêter le timer de la zone
                } else {
                    gui.afficher("L'énigme n'est pas résolue.");
                }
                break;

            case "Œuf de Serpentcendre":
                if (resoudreLabyrinthe()) {
                    gui.afficher("Vous avez pris : Œuf de Serpentcendre.");
                    inventaire.recupererIngredient("Œuf de Serpentcendre");
                    zoneCourante.prendreObjet("Œuf de Serpentcendre");
                    zoneCourante.stopTimer();  // Arrêter le timer de la zone
                } else {
                    gui.afficher("L'énigme n'est pas résolue.");
                }
                break;

            default:
                gui.afficher("Vous ne pouvez pas prendre cet objet.");
        }

        // Mise à jour en base de données après modification de l'inventaire
        System.out.println("Sauvegarde inventaire dans la BDD : " + Database.inventaireToString(inventaire));
        Database.mettreAJourUtilisateur(utilisateur);
    }

    /**
     * Récupère une zone spécifique en fonction de son index.
     *
     * @param index L'index de la zone à récupérer.
     * @return La zone correspondant à l'index, ou null si l'index est invalide.
     */
    public Zone getZone(int index) {
        if (index >= 0 && index < zones.length) {
            return zones[index];
        } else {
            return null;  // Retourne null si l'index est invalide
        }
    }

/**
 * Résout l'énigme du labyrinthe pour récupérer l'œuf de Serpentcendre.
 * Le joueur doit choisir la direction correcte pour trouver l'œuf.
 *
 * @return true si le joueur trouve l'œuf, false sinon.
 */
public boolean resoudreLabyrinthe() {
    oeuf();
    gui.afficher("vous etes deplacer vers l'oeuf du dragon pour la prendre \n veuillez repondre a l'enigme");

    String chemin = gui.demanderReponse("Suivez les traces laissées par le serpent. Où allez-vous ? (Nord/Sud/Est/Ouest)");
    if (chemin.equalsIgnoreCase("Est")) {
        gui.afficher("Vous avez trouvé l'œuf de Serpentcendre !");
        inventaire.recupererIngredient("Œuf de Serpentcendre");
        return true;
    }
    gui.afficher("Vous êtes perdu, retournez au début.");
    return false;
}

/**
 * Permet au joueur de jouer à l'énigme de Pierre-Feuille-Ciseaux pour récupérer les Feuilles de Mandragore.
 * Le joueur choisit une option (Pierre, Feuille, Ciseaux) et résout l'énigme.
 *
 * @return true si le joueur gagne l'énigme, false sinon.
 */
public boolean jouerPierreFeuilleCiseaux() {
    mondra();
    gui.afficher("vous etes deplacer vers la feuille de mondragore pour la prendre \n veuillez repondre a l'enigme");
    // Demander au joueur de choisir une option (Pierre, Feuille, Ciseaux)
    String choixJoueur = gui.demanderReponse("Pierre, Feuille, Ciseaux. Choisissez votre option :").trim().toLowerCase();

    // Le cultivateur choisit "Pierre" de manière fixe (ou toute autre valeur que tu veux ici)
    String choixCultivateur = "Pierre".toLowerCase();  // Choix fixe du cultivateur en minuscule

    gui.afficher("Le cultivateur a choisi : " + choixCultivateur);

    // Si le joueur choisit Feuille, il gagne immédiatement
    if (choixJoueur.equals("feuille")) {
        gui.afficher("Vous avez choisi Feuille. Vous avez gagné !");
        inventaire.recupererIngredient("Feuilles de Mandragore");
        return true;
    }

    // Sinon, appliquer la logique de Pierre-Feuille-Ciseaux normale
    if (gagnant(choixJoueur, choixCultivateur)) {
        gui.afficher("Vous avez gagné !");
        inventaire.recupererIngredient("Feuilles de Mandragore");
        return true;
    } else {
        gui.afficher("Vous avez perdu, essayez encore.");
        return false;
    }
}

/**
 * Vérifie si le joueur a gagné à l'énigme Pierre-Feuille-Ciseaux.
 *
 * @param joueur L'option choisie par le joueur.
 * @param adversaire L'option choisie par l'adversaire.
 * @return true si le joueur gagne, false sinon.
 */
private boolean gagnant(String joueur, String adversaire) {
    if (joueur.equals("Pierre") && adversaire.equals("Ciseaux") ||
            joueur.equals("Feuille") && adversaire.equals("Pierre") ||
            joueur.equals("Ciseaux") && adversaire.equals("Feuille")) {
        return true;
    }
    return false;
}

/**
 * Résout l'énigme pour récupérer le Champignon Bleu en choisissant la plante magique.
 *
 * @return true si le joueur trouve la plante magique, false sinon.
 */
public boolean resoudrePuzzlePlantes() {
    champblue();
    gui.afficher("vous etes deplacer vers le champion bleue pour la prendre :\n veuillez repondre a l'enigme");

    String reponse = gui.demanderReponse("Quelle plante est magique ? (1: Fruits toxiques, 2: Plantes agressives, 3: Plante qui bouge)");
    if (reponse.equals("3")) {
        gui.afficher("Vous avez trouvé la plante magique !");
        return true;
    } else {
        gui.afficher("Ce n'est pas la bonne plante.");
        return false;
    }
}

/**
 * Résout l'énigme pour choisir la combinaison correcte de symboles (Lumière, Eau, Vent).
 *
 * @return true si la combinaison est correcte, false sinon.
 */
public boolean choisirSymboles() {
    String choix = gui.demanderReponse("Choisissez l'ordre des symboles : Lumière, Eau, Vent.");
    if (choix.equalsIgnoreCase("l,e,v")) {
        gui.afficher("La combinaison est correcte !");
        return true;
    } else {
        gui.afficher("Ce n'est pas la bonne combinaison.");
        return false;
    }
}

/**
 * Résout l'énigme pour récupérer le Champignon Bleu en choisissant de se déplacer doucement.
 *
 * @return true si le joueur récupère le Champignon Bleu, false sinon.
 */
public boolean recupererChampignonBleu() {
    String commande = gui.demanderReponse("Comment allez-vous vous déplacer ? (Avancer doucement / Sauter)");
    if (commande.equalsIgnoreCase("Avancer doucement")) {
        gui.afficher("Vous avez récupéré le champignon bleu !");
        inventaire.recupererIngredient("Champignon Bleu");
        return true;
    } else {
        gui.afficher("Les racines se sont activées. Vous avez échoué.");
        return false;
    }
}

/**
 * Résout l'énigme du Chou Mordeur de Chine en distrayant les plantes avec un objet.
 *
 * @return true si les plantes sont distraites, false sinon.
 */
public boolean distrairePlantes() {
    chouMord();
    gui.afficher("vous etes deplacer vers le choue pour le  prendre \n veuillez jeter un objet en repondrant a l'enigme");

    String action = gui.demanderReponse("Que voulez-vous utiliser pour distraire les plantes ? (Pierre / Pomme)");
    if (action.equalsIgnoreCase("Pomme")) {
        gui.afficher("Les plantes sont distraites, vous pouvez approcher.");
        inventaire.recupererIngredient("Chou Mordeur de Chine");
        return true;
    } else {
        gui.afficher("Les plantes réagissent, vous devez essayer autre chose.");
        return false;
    }
}

/**
 * Résout l'énigme de la brume pour trouver la Fleur de Brume.
 *
 * @return true si le joueur trouve la Fleur de Brume, false sinon.
 */
public boolean traverserBrume() {
    allerfleur();
    gui.afficher("vous etes deplacer vers la fleur brume pour la prendre \n veuillez repondre a l'enigme");

    String reponse = gui.demanderReponse("Je suis léger, je couvre tout, et je suis souvent vu le matin. Qui suis-je ?");
    if (reponse.equalsIgnoreCase("brume")) {
        gui.afficher("Vous avez trouvé la Fleur de Brume.");
        inventaire.recupererIngredient("Fleur de Brume");
        return true;
    } else {
        gui.afficher("Vous ne trouvez pas la fleur, la brume vous empêche de voir.");
        return false;
    }
}

/**
 * Résout l'énigme des abeilles pour permettre au joueur d'avancer.
 *
 * @return true si le joueur trouve la bonne réponse, false sinon.
 */
public boolean repondreDevinetteAbeilles() {
    String reponse = gui.demanderReponse("Je fais de la fumée, je suis chaud et j'éclaire la nuit. Qui suis-je ?");
    if (reponse.equalsIgnoreCase("fumée")) {
        gui.afficher("Les abeilles sont apaisées, vous pouvez avancer.");
        return true;
    } else {
        gui.afficher("Les abeilles ne vous laissent pas passer.");
        return false;
    }
}
}
