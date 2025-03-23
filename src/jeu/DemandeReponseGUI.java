package jeu;

import javax.swing.JOptionPane;

public class DemandeReponseGUI {

    // Méthode pour afficher une question et obtenir une réponse
    public String demanderReponse(String question) {
        // Utilisation de JOptionPane pour demander une réponse à l'utilisateur
        String reponse = JOptionPane.showInputDialog(null, question, "Réponse", JOptionPane.QUESTION_MESSAGE);
        return reponse != null ? reponse : ""; // Si l'utilisateur annule, retourne une chaîne vide
    }
}

