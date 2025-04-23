package jeu;

import javax.swing.JOptionPane;

/**
 * Classe utilitaire pour interagir avec l'utilisateur via une interface graphique simple (Swing).
 * Elle permet d'afficher une question et de récupérer une réponse en utilisant une boîte de dialogue.
 */
public class DemandeReponseGUI {

    /**
     * Affiche une boîte de dialogue contenant une question, et retourne la réponse saisie par l'utilisateur.
     *
     * @param question La question à poser à l'utilisateur
     * @return La réponse de l'utilisateur, ou une chaîne vide si l'utilisateur annule
     */
    public String demanderReponse(String question) {
        String reponse = JOptionPane.showInputDialog(null, question, "Réponse", JOptionPane.QUESTION_MESSAGE);
        return reponse != null ? reponse : "";
    }
}
