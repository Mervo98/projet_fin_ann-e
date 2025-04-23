package jeu;

/**
 * L'énumération {@code Sortie} représente les différentes directions dans lesquelles le joueur peut se déplacer dans le jeu.
 * Chaque valeur de l'énumération correspond à une direction ou un mouvement spécifique dans le monde du jeu.
 * Ces directions sont utilisées pour déterminer où le joueur peut se rendre lorsqu'il interagit avec l'environnement.
 */
public enum Sortie {

	/**
	 * Direction vers le nord.
	 * Cela peut correspondre à un déplacement vers une zone située au nord du joueur.
	 */
	NORD,

	/**
	 * Direction vers le sud.
	 * Cela peut correspondre à un déplacement vers une zone située au sud du joueur.
	 */
	SUD,

	/**
	 * Direction vers l'est.
	 * Cela peut correspondre à un déplacement vers une zone située à l'est du joueur.
	 */
	EST,

	/**
	 * Direction vers l'ouest.
	 * Cela peut correspondre à un déplacement vers une zone située à l'ouest du joueur.
	 */
	OUEST,

	/**
	 * Direction spéciale représentant le vol.
	 * Cela pourrait indiquer un mouvement spécial, comme voler ou se déplacer dans les airs, dans un contexte particulier du jeu.
	 */
	VOL;
}
