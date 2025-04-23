package jeu;

import java.util.ArrayList;
import java.util.List;

/**
 * L'énumération Commande représente les différentes commandes disponibles dans le jeu.
 * Chaque commande possède une abréviation et une description textuelle.
 */
public enum Commande {
	/** Aller vers la sortie nord */
	NORD("N", "N (aller à la sortie nord)"),

	/** Aller vers la sortie sud */
	SUD("S", "S (aller à la sortie sud)"),

	/** Aller vers la sortie est */
	EST("E", "E (aller à la sortie est)"),

	/** Aller vers la sortie ouest */
	OUEST("O", "O (aller à la sortie ouest)"),

	/** Prendre la sortie 'vol' */
	VOL("V", "V (aller à la sortie vol)"),

	/** Afficher l'aide du jeu */
	AIDE("?", "? (aide)"),

	/** Quitter le jeu */
	QUITTER("Q", "Q (quitter)"),

	/** Prendre un objet */
	PRENDRE("Prd", "Prd (prendre un objet)"),

	/** Répondre à une énigme */
	PREPARER("Prp", "Prp (répondre à une énigme)"),

	/** Jeter un objet */
	JETER("J", "J (jeter un objet)"),

	/** Revenir au village */
	RETOUR("R", "R (utiliser pour retourner au village)"),

	/** Aller vers la fleur pour la prendre */
	FLEUR("F", "F (aller vers la fleur pour la prendre)");

	/** Abréviation de la commande */
	private final String abreviation;

	/** Description détaillée de la commande */
	private final String description;

	/**
	 * Constructeur privé de l'énumération Commande.
	 *
	 * @param c l'abréviation
	 * @param d la description
	 */
	Commande(String c, String d) {
		abreviation = c;
		description = d;
	}

	/**
	 * Retourne le nom de l'énumération.
	 *
	 * @return nom de la commande
	 */
	@Override
	public String toString() {
		return name();
	}

	/**
	 * Retourne la liste de toutes les descriptions des commandes.
	 *
	 * @return liste des descriptions
	 */
	public static List<String> toutesLesDescriptions() {
		ArrayList<String> resultat = new ArrayList<String>();
		for (Commande c : values()) {
			resultat.add(c.description);
		}
		return resultat;
	}
}
