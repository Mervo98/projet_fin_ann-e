package jeu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Gui1 implements ActionListener {

    private JFrame fenetre;
    private JTextField entree;
    private JTextArea texte;
    private JButton boutonEnvoyer;
    private DemandeReponseGUI demandeReponseGUI;
    private String reponse;  // Variable pour stocker la réponse de l'utilisateur

    public Gui1() {
        // Initialisation de l'interface graphique
        fenetre = new JFrame("Jeu d'énigme");
        fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fenetre.setSize(400, 300);
        fenetre.setLayout(new BorderLayout());

        // Zone de texte pour afficher les messages
        texte = new JTextArea();
        texte.setEditable(false);  // Empêcher l'édition directe par l'utilisateur
        texte.setLineWrap(true);   // Enrouler le texte quand il atteint la fin de la ligne
        texte.setWrapStyleWord(true); // Enrouler les mots
        JScrollPane scrollPane = new JScrollPane(texte);
        fenetre.add(scrollPane, BorderLayout.CENTER);

        // Zone de saisie pour entrer des commandes
        entree = new JTextField();
        fenetre.add(entree, BorderLayout.SOUTH);

        // Bouton pour envoyer la commande
        boutonEnvoyer = new JButton("Envoyer");
        boutonEnvoyer.addActionListener(this); // L'action sera gérée par cette classe
        fenetre.add(boutonEnvoyer, BorderLayout.EAST);

        // Initialisation de la classe DemandeReponseGUI pour gérer les demandes de réponse
        demandeReponseGUI = new DemandeReponseGUI();

        // Affichage de la fenêtre
        fenetre.setVisible(true);
    }

    // Méthode pour afficher un message dans la zone de texte
    public void afficher(String message) {
        texte.append(message + "\n");
    }

    // Méthode pour récupérer une réponse de l'utilisateur
    public String demanderReponse(String question) {
        // Afficher la question dans le texte
        afficher(question);

        // Utiliser la classe DemandeReponseGUI pour demander la réponse
        reponse = demandeReponseGUI.demanderReponse(question);

        return reponse;
    }

    // Méthode pour gérer l'action du bouton "Envoyer"
    @Override
    public void actionPerformed(ActionEvent e) {
        String commande = entree.getText(); // Récupérer la commande entrée par l'utilisateur
        entree.setText(""); // Vider le champ de texte
        afficher("Vous avez entré: " + commande); // Afficher la commande dans la zone de texte

        // Vous pouvez ajouter ici une logique pour traiter la commande
        // Par exemple, appeler une méthode du jeu pour traiter la commande de l'utilisateur
        // traiterCommande(commande);
    }

    public static void main(String[] args) {
        // Lancer l'application
        new Gui1();
    }
}
