package jeu;

import java.util.ArrayList;
import java.util.List;

public enum Commande {
	NORD("N", "N (aller à la sortie nord)"),
	SUD("S", "S (aller à la sortie sud)"),
	EST("E", "E (aller à la sortie est)"),
	OUEST("O", "O (aller à la sortie ouest)"),
	VOL("V", "V (aller à la sortie vol)"),
	AIDE("?", "? (aide)"),
	QUITTER("Q", "Q (quitter)"),

	PRENDRE("P", "P (prendre un objet)"),
	REPONDRE("R", "R (répondre à une énigme)"),
	JETER("J", "J (jeter un objet)"),
	UTILISER("U", "U (utiliser un objet)"),
	DEPLACER("D", "D (se déplacer entre les zones)"),
	RETOURNER("RT", "RT (retourner dans une zone déjà visitée)"),
	ENTRER("E", "E (entrer dans un lieu)"),
	SORTIR("S", "S (sortir d'un lieu)"),
	VOIR_CARTE("VC", "VC (voir la carte)"),
	INVENTAIRE("I", "I (afficher l'inventaire)"),
	CREER_FUMEE("CF", "CF (créer de la fumée avec des objets de l'inventaire)"),
	CUEILLIR("C", "C (cueillir des plantes)"),
	OBSERVER("O", "O (observer un objet ou l'environnement)"),
	CHOISIR_SYMBOLE("CS", "CS (choisir un symbole pour une énigme)");

	private String abreviation;
	private String description;

	private Commande(String c, String d) {
		abreviation = c;
		description = d;
	}

	@Override
	public String toString() {
		return name();
	}

	public static List<String> toutesLesDescriptions() {
		ArrayList<String> resultat = new ArrayList<String>();
		for (Commande c : values()) {
			resultat.add(c.description);
		}
		return resultat;
	}

	public static List<String> toutesLesAbreviations() {
		ArrayList<String> resultat = new ArrayList<String>();
		for (Commande c : values()) {
			resultat.add(c.abreviation);
		}
		return resultat;
	}

	public static List<String> tousLesNoms() {
		ArrayList<String> resultat = new ArrayList<String>();
		for (Commande c : values()) {
			resultat.add(c.name());
		}
		return resultat;
	}
}
