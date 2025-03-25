
package jeu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

public class Jeu {
    private GUI gui;


    private Utilisateurs utilisateur;
    private Zone zoneCourante;
    private final GestionUtilisateurs gestionUtilisateurs = new GestionUtilisateurs();


    private Zone[] zones;
    private int xPersonnage = 200, yPersonnage = 200; // Position initiale
    private Inventaire inventaire = new Inventaire(); // Inventaire du joueur
    private int tempsRestant; // Variable pour suivre le temps restant en secondes
    private Timer timer; //

    private final JLabel compteAReboursLabel;



    public Jeu() {
        creerCarte();
        demanderConnexion();
        Database.creerTable();
        gui= null;
        inventaire=new Inventaire();
        this.tempsRestant = 60; // Initialisation du temps à 60 secondes

        // Créez un JLabel pour afficher le temps restant
        this.compteAReboursLabel = new JLabel("Temps restant : " + tempsRestant + " secondes");
        compteAReboursLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        compteAReboursLabel.setForeground(Color.RED); // Vous pouvez choisir une autre couleur si vous le souhaitez.

        // Créer un panneau pour afficher le compte à rebours
        JPanel panel = new JPanel();
        panel.add(compteAReboursLabel); // Ajouter le label au panneau

        // Ajouter ce panneau à votre GUI, en supposant que vous avez une fenêtre principale (JFrame)
        JFrame frame = new JFrame("Jeu de Potion");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setLayout(new BorderLayout());
        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);

        // Timer pour le compte à rebours
        this.timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Décrémenter le temps restant à chaque seconde
                tempsRestant--;
                // Mettre à jour le texte du JLabel pour afficher le temps restant
                compteAReboursLabel.setText("Temps restant : " + tempsRestant + " secondes");

                if (tempsRestant <= 0) {
                    // Si le temps est écoulé, afficher un message et arrêter le timer
                    timer.stop();
                    JOptionPane.showMessageDialog(null, "Temps écoulé ! Vous avez perdu.", "Échec", JOptionPane.ERROR_MESSAGE);
                    // Logique d'échec (exemple : fin du jeu)
                }
            }
        });
    }

    private void demanderConnexion() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Bienvenue dans le jeu !");

        while (true) {
            System.out.println("1. Connexion");
            System.out.println("2. Inscription");
            System.out.print("Choisissez une option : ");
            String choix = scanner.nextLine().trim();

            System.out.print("Nom d'utilisateur : ");
            String nomUtilisateur = scanner.nextLine().trim();
            System.out.print("Mot de passe : ");
            String motDePasse = scanner.nextLine().trim();

            if (choix.equals("1")) { // Connexion
                if (gestionUtilisateurs.connecter(nomUtilisateur, motDePasse)) {
                    utilisateur = gestionUtilisateurs.getUtilisateurConnecte();

                    // Vérifier si la zone courante est valide
                    if (utilisateur.getZoneCourante() == null || utilisateur.getZoneCourante().equals("Zone inconnue")) {
                        if (zones.length > 1) {
                            utilisateur.setZoneCourante(zones[1]); // Zone par défaut
                        } else {
                            System.out.println("Erreur : aucune zone disponible.");
                            continue;
                        }
                    }

                    System.out.println("Connexion réussie !");
                    System.out.println("Zone actuelle : " + utilisateur.getZoneCourante());

                    // Affichage d'un message de bienvenue avec JOptionPane
                    JOptionPane.showMessageDialog(null,
                            "Bienvenue " + utilisateur.getNomUtilisateur() + " !\n" +
                                    "Dans un village,  un hero(une personne) doit trouver des ingrédients afin de créer un remède pour\n" +
                                    "purifier l’eau et sauver son village.\n\n" +
                                    "Et cette personne, c'est toi, " + utilisateur.getNomUtilisateur() + " !\n\nBonne chance !",
                            "Message",
                            JOptionPane.INFORMATION_MESSAGE);

                    break; // Sortie de la boucle après une connexion réussie
                } else {
                    System.out.println("Nom d'utilisateur ou mot de passe incorrect.");
                }
            } else if (choix.equals("2")) { // Inscription
                if (zones.length > 1 && gestionUtilisateurs.inscrire(nomUtilisateur, motDePasse, zones[1])) {
                    System.out.println("Inscription réussie ! Vous pouvez maintenant vous connecter.");
                } else {
                    System.out.println("Nom d'utilisateur déjà pris ou erreur d'inscription.");
                }
            } else {
                System.out.println("Option invalide. Veuillez entrer '1' pour Connexion ou '2' pour Inscription.");
            }
        }
    }



    private void terminer() {
        if (utilisateur != null) {
            Database.sauvegarderUtilisateur(utilisateur);
            gui.afficher("Déconnexion réussie.");
            Database.mettreAJourUtilisateur(utilisateur);
        }
            gui.enable(false);
    }



    public void setGUI(GUI g) {
        gui = g;
        afficherMessageDeBienvenue();
    }


    private void creerCarte() {

        zones = new Zone[6];
        // Déclaration de l'array zones
        zones[0] = new Zone("Champ de Mandragore", "Champ_de_Mandragore.jpg");
        zones[1] = new Zone("Forêt Brune", "foret_brune.jpg");
        zones[2] = new Zone("Montagne Rocheuse", "Montagne_rocheuse.jpg");
        zones[3] = new Zone("Plantes Carnivores", "plante_carnivore.jpg");
        zones[4] = new Zone("Zone Volcanique", "zone_volcanique.jpg");
        zones[5] = new Zone("Le village", "village.png");


        // Ajout d'objets dans les zones"
        zones[0].ajouteObjet("Feuilles de Mandragore");
        zones[1].ajouteObjet("Fleur de Brume");
        zones[2].ajouteObjet("Champignon Bleu");
        zones[3].ajouteObjet("Chou Mordeur de Chine");
        zones[4].ajouteObjet("Œuf de Serpentcendre");
        zones[5].ajouteObjet("voici les 5 objets diponible \n 1:Feuilles de Mandragore \n 2:Fleur de Brume \n 3:Champignon Bleu \n 4:Chou Mordeur de Chine\n 5:Œuf de Serpentcendre \n RETROUVEZ LES");

        // Définir les sorties
        zones[0].ajouteSortie(Sortie.EST, zones[1]);
        zones[1].ajouteSortie(Sortie.OUEST, zones[0]);
        zones[1].ajouteSortie(Sortie.VOL, zones[2]);
        zones[2].ajouteSortie(Sortie.EST, zones[1]);
        zones[2].ajouteSortie(Sortie.VOL, zones[3]);
        zones[3].ajouteSortie(Sortie.NORD, zones[2]);
        zones[3].ajouteSortie(Sortie.VOL, zones[4]);
        zones[4].ajouteSortie(Sortie.EST, zones[1]);
        zones[4].ajouteSortie(Sortie.SUD, zones[5]);
        zones[5].ajouteSortie(Sortie.SUD, zones[1]);

        zoneCourante = zones[5];
    }






    private void afficherMessageDeBienvenue() {
        gui.afficher("Bienvenue !");
        gui.afficher("Tapez '?' pour obtenir de l'aide.");
        gui.afficheImage(zoneCourante.nomImage());
        gui.afficher(zoneCourante.descriptionLongue());
        gui.afficher("VOICI LA CARTE DU VILLAGE CI-DESSUS");
        // Positionne le personnage au début
        gui.deplacerPersonnage(xPersonnage, yPersonnage);
    }

    // Démarrer le timer lorsque le joueur commence une action
    public void demarrerTimer() {
        timer.start();
    }

    // Arrêter le timer manuellement si nécessaire (par exemple, quand le joueur réussit la mission)
    public void arreterTimer() {

        timer.stop();

    }




    public void retourAuVillage() {
        if (!inventaire.aTousLesIngredients()) {
            gui.afficher("Vous n'avez pas tous les ingrédients. Revenez chercher les ingrédients manquants.");
        } else {
            gui.afficher("Vous retournez au village avec tous les ingrédients.");
            allerEn("SUD");
            if (tempsRestant > 0) {
                // Effectuer les actions liées à la préparation de la potion
            } else {
                // Si le temps est écoulé, afficher un message d'échec
                JOptionPane.showMessageDialog(null, "Le temps est écoulé ! Vous avez échoué.", "Échec", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

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
                // Vérifier si la saisie correspond à l'ordre correct
                if (verifierSaisie(saisie)) {
                    JOptionPane.showMessageDialog(null, "Bravo ! Vous avez réussi à préparer la potion.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                    terminer();
                    arreterTimer();// Arrêter le timer si la mission est réussie
                } else {
                    JOptionPane.showMessageDialog(null, "Échec ! Vous avez perdu.", "Échec", JOptionPane.ERROR_MESSAGE);
                    terminer();
                    arreterTimer();

                    // Arrêter le timer en cas d'échec
                }
            } else {
                JOptionPane.showMessageDialog(null, "Aucune saisie détectée. Veuillez réessayer.", "Erreur", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            gui.afficher("Veuillez retourner au village avec tous les ingrédients avant de préparer la potion.");
        }
    }

    // Méthode pour vérifier si la saisie est correcte
    private boolean verifierSaisie(String saisie) {
        // L'ordre correct est "3,4,5,1,2"
        return saisie.trim().equals("3,4,5,1,2");
    }



    private void allerfleur(){
        xPersonnage=259;
        yPersonnage=259;

        gui.deplacerPersonnage(xPersonnage,yPersonnage);
        gui.afficher("Vous vous dirigez vers la fleur.");
    }

    private void arbre1(){
        xPersonnage=45;
        yPersonnage=437;

        gui.deplacerPersonnage(xPersonnage,yPersonnage);
        gui.afficher("Vous vous dirigez vers la fleur.");

    }

    private void mondra(){
        xPersonnage=226;
        yPersonnage=217;

        gui.deplacerPersonnage(xPersonnage,yPersonnage);
        gui.afficher("Vous vous dirigez vers la fleur.");

    }

    private void champblue(){
        xPersonnage=431;
        yPersonnage=371;

        gui.deplacerPersonnage(xPersonnage,yPersonnage);
        gui.afficher("Vous vous dirigez vers la fleur.");

    }

    private void chouMord(){
        xPersonnage=258;
        yPersonnage=242;

        gui.deplacerPersonnage(xPersonnage,yPersonnage);
        gui.afficher("Vous vous dirigez vers la fleur.");

    }

    private void oeuf(){
        xPersonnage=366;
        yPersonnage=98;

        gui.deplacerPersonnage(xPersonnage,yPersonnage);
        gui.afficher("Vous vous dirigez vers la fleur.");

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
            case "RETOUR":
                retourAuVillage();
                break;

            case "INVENTAIRE":
                gui.afficher("Voici votre inventaire :");
                gui.afficher(inventaire.afficherContenu());
                Database.mettreAJourUtilisateur(utilisateur);
                System.out.println(Database.inventaireToString(inventaire));

                System.out.println(Database.inventaireToString(utilisateur.getInventaire()));
                break;

            case "PRENDRE":
                if (zoneCourante.getObjets().isEmpty()) {
                    gui.afficher("Il n'y a rien à prendre ici.");
                } else if  (zoneCourante != getZone(3)) {
                    String objet = zoneCourante.getObjets().get(0); // Prend le premier objet de la liste des objets disponibles
                    prendreObjet(objet);
                }
                else {
                    gui.afficher("Vous ne ouvez pas utiliser la commande (prendre) ici");
                }
                break;

            case "PREPARER":
                verifierOrdreIngredients();
                break;
            case "FLEUR":
                allerfleur();
                break;


            case "JETER":
                if (zoneCourante == getZone(3)) { // Vérifie si on est bien dans la zone 2
                    if (zoneCourante.getObjets().isEmpty()) {
                        gui.afficher("Il n'y a rien à jeter ici.");
                    } else {
                        String objet = zoneCourante.getObjets().get(0);
                        prendreObjet(objet); // Supposons que cette méthode existe
                        gui.afficher("Vous avez jeté une pomme");
                    }
                } else {
                    gui.afficher("Vous ne pouvez jeter des objets que dans cette zone!");
                }
                break;
        }
    }


    private void afficherAide() {
        gui.afficher("Etes-vous perdu ?");
        System.out.println(zoneCourante);
        gui.afficher("Les commandes autorisées sont :");
        //gui.afficher(utilisateurActuel.getZoneCourante());
        System.out.println(utilisateur.getNomUtilisateur());
        // Afficher chaque description sur une nouvelle ligne
        for (String description : Commande.toutesLesDescriptions()) {
            gui.afficher(description);  // Affiche chaque commande sur une nouvelle ligne
        }

        gui.afficher();  // Ligne vide à la fin si nécessaire
    }

    private void allerEn(String direction) {
        // Récupérer la nouvelle zone en fonction de la direction
        Zone nouvelle = zoneCourante.obtientSortie(direction);
        if (nouvelle == null) {
            gui.afficher("Pas de sortie " + direction);
            gui.afficher();
        } else {
            // Changer la zone courante
            zoneCourante = nouvelle;
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




    // Commande Prendre un objet
    public void prendreObjet(String objet) {
        if (objet.isEmpty()) {
            gui.afficher("Que voulez-vous prendre ?");
            return;
        }

        if (zoneCourante.contientObjet(objet)) {
            switch (objet) {
                case "Feuilles de Mandragore":
                    if (jouerPierreFeuilleCiseaux()) {
                        gui.afficher("Vous avez pris : Feuilles de Mandragore.");
                        inventaire.recupererIngredient("Feuilles de Mandragore");
                        zoneCourante.prendreObjet("Feuilles de Mandragore");
                    } else {
                        gui.afficher("L'énigme n'est pas résolue.");
                    }
                    break;
                case "Fleur de Brume":
                    if (traverserBrume()) {
                        gui.afficher("Vous avez pris : Fleur de Brume.");
                        inventaire.recupererIngredient("Fleur de Brume");
                        zoneCourante.prendreObjet("Fleur de Brume");
                    } else {
                        gui.afficher("L'énigme n'est pas résolue.");
                    }
                    break;
                case "Champignon Bleu":
                    if (resoudrePuzzlePlantes()&& repondreDevinetteAbeilles() &&choisirSymboles() && recupererChampignonBleu()) {
                        gui.afficher("Vous avez pris : Champignon Bleu.");
                        inventaire.recupererIngredient("Champignon Bleu");
                        zoneCourante.prendreObjet("Champignon Bleu");
                    } else {
                        gui.afficher("L'énigme n'est pas résolue.");
                    }
                    break;
                case "Chou Mordeur de Chine":
                    if (distrairePlantes()) {
                        gui.afficher("Vous avez pris : Chou Mordeur de Chine.");
                        inventaire.recupererIngredient("Chou mordeur de Chine");
                        zoneCourante.prendreObjet("Chou mordeur de Chine");
                    } else {
                        gui.afficher("L'énigme n'est pas résolue.");
                    }
                    break;
                case "Œuf de Serpentcendre":
                    if (resoudreLabyrinthe()) {
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

    // Énigme du cultivateur (Pierre-Feuille-Ciseaux)
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

    // Vérifie si le joueur a gagné
    private boolean gagnant(String joueur, String adversaire) {
        if (joueur.equals("Pierre") && adversaire.equals("Ciseaux") ||
                joueur.equals("Feuille") && adversaire.equals("Pierre") ||
                joueur.equals("Ciseaux") && adversaire.equals("Feuille")) {
            return true;
        }
        return false;
    }

    // Énigme du puzzle de plantes
    public boolean resoudrePuzzlePlantes() {
        champblue();
        gui.afficher("vous etes deplacer vers le champion bleue pour la prendre :\n veuillez repondre a l'enigme");

        String reponse = gui.demanderReponse("Quelle plante est magique ? (1: Fruits toxiques, 2: Plantes agressives, 3: Plante qui bouge)");
        if (reponse.equals("3")) {
            gui.afficher("Vous avez trouvé la plante magique !");
            //inventaire.recupererIngredient("Champignon Bleu");
            return true;
        } else {
            gui.afficher("Ce n'est pas la bonne plante.");
            return false;
        }
    }

    // Énigme de la combinaison de symboles
    public boolean choisirSymboles() {
        String choix = gui.demanderReponse("Choisissez l'ordre des symboles : Lumière, Eau, Vent.");
        if (choix.equalsIgnoreCase("l,e,v")) {
            gui.afficher("La combinaison est correcte !");
            //inventaire.recupererIngredient("Champignon Bleu");
            return true;
        } else {
            gui.afficher("Ce n'est pas la bonne combinaison.");
            return false;
        }
    }



    // Énigme pour récupérer le champignon bleu
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

    // Énigme du chou mordeur de Chine
    public boolean distrairePlantes() {
        chouMord();
        gui.afficher("vous etes deplacer vers le choue pour le  prendre \n veuillez jeter un objet en repondrant a l'enigme");

        String action = gui.demanderReponse("Que voulez-vous utiliser pour distraire les plantes ? (Pierre / Pomme)");
        if (action.equalsIgnoreCase("Pomme")) {
            gui.afficher("Les plantes sont distraites, vous pouvez approcher.");
            //gui.afficher("Vous avez récupéré Chou Mordeur de Chine !");
            inventaire.recupererIngredient("Chou Mordeur de Chine");
            return true;
        } else {
            gui.afficher("Les plantes réagissent, vous devez essayer autre chose.");
            return false;
        }
    }
    // Énigme de la brume
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

    // Énigme des abeilles
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