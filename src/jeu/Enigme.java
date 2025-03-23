package jeu;

public class Enigme {
    public Gui1 gui;

    public Inventaire inventaire;


    public Enigme(Gui1 gui) {
        this.inventaire = new Inventaire();
        this.gui = gui;  // Initialiser gui1 avec l'instance passée en paramètre
    }
    // Énigme du labyrinthe du serpentcendre
    public boolean resoudreLabyrinthe() {
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
        String reponse = gui.demanderReponse("Je suis léger, je couvre tout, et je suis souvent vu le matin. Qui suis-je ?");
        if (reponse.equalsIgnoreCase("brume")) {
            gui.afficher("Vous avez trouvé la Fleur de Brume.");
            //inventaire.recupererIngredient("Fleur de Brume");
            return false;
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
