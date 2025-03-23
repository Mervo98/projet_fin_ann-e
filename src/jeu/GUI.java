package jeu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GUI implements ActionListener {
    private Jeu jeu;
    private JFrame fenetre;
    private JTextField entree;
    private JTextArea texte;
    private ImagePanel imagePanel;  // Panel personnalisé pour l'image de fond
    private JLabel personnage;      // Image du personnage

    // Variable pour stocker la réponse
    private String reponse = "";

    public GUI(Jeu j) {
        jeu = j;
        creerGUI();
    }

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
        imagePanel.setImage("jeu/images/SalleAManger.jpg");
        imagePanel.setPersonnageImage("jeu/images/Boy.jpg");

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(imagePanel, BorderLayout.NORTH);
        panel.add(listScroller, BorderLayout.CENTER);
        panel.add(entree, BorderLayout.SOUTH);

        fenetre.getContentPane().add(panel, BorderLayout.CENTER);
        fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        entree.addActionListener(this);
        fenetre.pack();
        fenetre.setVisible(true);
        entree.requestFocus();
    }

    public void afficher(String message) {
        texte.append(message + "\n");  // Ajoutez "\n" pour un saut de ligne après chaque message
        texte.setCaretPosition(texte.getDocument().getLength());  // Faire défiler jusqu'à la fin du texte
    }


    public void afficher() {
        afficher("\n");
    }

    public void afficheImage(String nomImage) {
        imagePanel.setImage("jeu/images/" + nomImage);
        fenetre.repaint();
    }

    public void deplacerPersonnage(int x, int y) {
        imagePanel.deplacerPersonnage(x, y);
    }

    public void enable(boolean ok) {
        entree.setEditable(ok);
        if (!ok) {
            entree.getCaret().setBlinkRate(0);
        }
    }

    public void actionPerformed(ActionEvent e) {
        executerCommande();
    }

    private void executerCommande() {
        String commandeLue = entree.getText();
        entree.setText("");
        jeu.traiterCommande(commandeLue);
    }

    /**
     * Demande une réponse à l'utilisateur et attend la saisie dans le champ de texte.
     *
     * @param question La question à afficher à l'utilisateur.
     * @return La réponse fournie par l'utilisateur.
     */

    /**
     * Méthode appelée lorsqu'une action est effectuée dans le champ de texte.
     */
    public void setReponse(String nouvelleReponse) {
        this.reponse = nouvelleReponse;
    }
}
