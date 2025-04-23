package jeu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * La classe <code>GUI</code> représente l'interface graphique du jeu.
 * Elle gère l'affichage des éléments de l'interface utilisateur et la prise en charge des événements.
 */
public class GUI implements ActionListener {
    private final Jeu jeu;
    private JFrame fenetre;
    private JTextField entree;
    private DemandeReponseGUI demandeReponseGUI;
    private JTextArea texte;
    private ImagePanel imagePanel;
    private String reponse;
    private JLabel tempsRestantLabel; // Label pour afficher le temps restant

    /**
     * Constructeur qui initialise la fenêtre de l'interface graphique du jeu.
     *
     * @param j L'objet <code>Jeu</code> qui représente la logique du jeu.
     */
    public GUI(Jeu j) {
        jeu = j;
        creerGUI();
    }

    /**
     * Affiche une question et demande une réponse à l'utilisateur.
     *
     * @param question La question à poser à l'utilisateur.
     * @return La réponse de l'utilisateur.
     */
    public String demanderReponse(String question) {
        // Afficher la question dans le texte
        afficher(question);

        // Utiliser la classe DemandeReponseGUI pour demander la réponse
        reponse = demandeReponseGUI.demanderReponse(question);

        return reponse;
    }

    /**
     * Crée et configure l'interface graphique du jeu, y compris la fenêtre,
     * les panneaux et les composants d'entrée.
     */
    private void creerGUI() {
        fenetre = new JFrame("Jeu");

        entree = new JTextField(34);
        texte = new JTextArea();
        texte.setEditable(false);
        JScrollPane listScroller = new JScrollPane(texte);
        listScroller.setPreferredSize(new Dimension(200, 200));

        imagePanel = new ImagePanel();
        imagePanel.setPreferredSize(new Dimension(500, 500));

        // Charger les images
        imagePanel.setPersonnageImage("jeu/images/Boy.jpg");

        // Panel pour afficher le temps restant
        tempsRestantLabel = new JLabel("Temps restant : 0 secondes");
        tempsRestantLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        tempsRestantLabel.setForeground(Color.RED);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(imagePanel, BorderLayout.NORTH);
        panel.add(listScroller, BorderLayout.CENTER);
        panel.add(entree, BorderLayout.SOUTH);

        // Ajouter le label du temps restant en haut à la fenêtre
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(tempsRestantLabel, BorderLayout.NORTH);
        topPanel.add(panel, BorderLayout.CENTER);

        fenetre.getContentPane().add(topPanel, BorderLayout.CENTER);
        fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        entree.addActionListener(this);
        fenetre.pack();
        fenetre.setResizable(false);

        fenetre.setVisible(true);

        demandeReponseGUI = new DemandeReponseGUI();
        entree.requestFocus();
    }

    /**
     * Affiche une question dans la zone de texte.
     *
     * @param message Le message à afficher.
     */
    public void afficher(String message) {
        texte.append(message + "\n");
        texte.setCaretPosition(texte.getDocument().getLength());  // Faire défiler jusqu'à la fin du texte
    }

    /**
     * Affiche une ligne vide dans la zone de texte.
     */
    public void afficher() {
        afficher("\n");
    }

    /**
     * Affiche une image dans le panneau d'image.
     *
     * @param nomImage Le nom de l'image à afficher (chemin relatif).
     */
    public void afficheImage(String nomImage) {
        imagePanel.setImage("jeu/images/" + nomImage);
        fenetre.repaint();
    }

    /**
     * Déplace le personnage à une nouvelle position dans le panneau d'image.
     *
     * @param x La nouvelle position en X du personnage.
     * @param y La nouvelle position en Y du personnage.
     */
    public void deplacerPersonnage(int x, int y) {
        imagePanel.deplacerPersonnageAnime(x, y);
    }

    /**
     * Active ou désactive le champ de texte pour l'entrée utilisateur.
     *
     * @param ok Si <code>true</code>, le champ de texte est activé. Si <code>false</code>,
     *           le champ de texte est désactivé et le curseur cesse de clignoter.
     */
    public void enable(boolean ok) {
        entree.setEditable(ok);
        if (!ok) {
            entree.getCaret().setBlinkRate(0);
        }
    }

    /**
     * Cette méthode est appelée lorsqu'un événement d'action est déclenché
     * (lorsque l'utilisateur appuie sur la touche <code>Enter</code> dans le champ de texte).
     * Elle appelle la méthode <code>executerCommande</code> pour traiter la commande entrée par l'utilisateur.
     *
     * @param e L'événement d'action généré.
     */
    public void actionPerformed(ActionEvent e) {
        executerCommande();
    }

    /**
     * Traite la commande entrée par l'utilisateur dans le champ de texte.
     * Cette méthode est appelée lorsqu'une action est effectuée sur le champ de texte.
     */
    private void executerCommande() {
        String commandeLue = entree.getText();
        entree.setText("");
        jeu.traiterCommande(commandeLue);
    }

    /**
     * Met à jour le label du temps restant avec le temps restant pour la zone courante.
     */
    public void mettreAJourTempsRestant(int tempsRestant) {
        tempsRestantLabel.setText("Temps restant : " + tempsRestant + " secondes");
    }
}
