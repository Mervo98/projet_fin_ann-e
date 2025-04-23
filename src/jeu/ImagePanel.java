package jeu;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Panneau graphique personnalisé qui affiche une image de fond et un personnage par-dessus.
 * Le personnage peut être déplacé dynamiquement, et son image est nettoyée pour supprimer le fond blanc.
 * Ajoute des effets d'animation pour un déplacement fluide et des transitions d'images.
 */
public class ImagePanel extends JPanel {
    private Image imageFond;
    private Image imagePersonnage;
    private int xPersonnage = 200, yPersonnage = 200;
    private Timer timer; // Timer pour l'animation du mouvement
    private int deltaX = 0, deltaY = 0; // Delta pour mouvement fluide

    /**
     * Définit l'image de fond à afficher dans le panneau.
     *
     * @param imagePath Le chemin de l'image de fond (dans les ressources)
     */
    public void setImage(String imagePath) {
        imageFond = chargerImage(imagePath);
        repaint();
    }

    /**
     * Définit l'image du personnage à afficher, en supprimant le fond blanc automatiquement.
     *
     * @param imagePath Le chemin de l'image du personnage (dans les ressources)
     */
    public void setPersonnageImage(String imagePath) {
        imagePersonnage = removeBackground(imagePath);
        repaint();
    }

    /**
     * Modifie la position du personnage dans le panneau de manière animée.
     *
     * @param x Position horizontale
     * @param y Position verticale
     */
    public void deplacerPersonnageAnime(int x, int y) {
        // Calculer le delta pour le mouvement fluide
        deltaX = (x - xPersonnage) / 10;
        deltaY = (y - yPersonnage) / 10;

        // Si on ne doit pas bouger, on arrête l'animation
        if (deltaX == 0 && deltaY == 0) {
            return;
        }

        // Démarrer l'animation du mouvement avec un Timer
        if (timer == null || !timer.isRunning()) {
            timer = new Timer(30, e -> {
                // Incrémenter la position du personnage de manière fluide
                xPersonnage += deltaX;
                yPersonnage += deltaY;

                // Si on atteint la position cible, on arrête le mouvement
                if (Math.abs(xPersonnage - x) <= Math.abs(deltaX) && Math.abs(yPersonnage - y) <= Math.abs(deltaY)) {
                    xPersonnage = x;
                    yPersonnage = y;
                    timer.stop();
                }

                repaint();
            });
            timer.start(); // Démarre le timer
        }
    }

    /**
     * Supprime le fond blanc d'une image pour rendre le personnage transparent.
     *
     * @param path Chemin de l'image du personnage
     * @return L'image avec fond transparent
     */
    private BufferedImage removeBackground(String path) {
        try {
            BufferedImage image = ImageIO.read(getClass().getClassLoader().getResource(path));
            int width = image.getWidth();
            int height = image.getHeight();

            BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int pixel = image.getRGB(x, y);
                    Color color = new Color(pixel, true);

                    // Suppression du fond blanc
                    if (color.getRed() > 240 && color.getGreen() > 240 && color.getBlue() > 240) {
                        newImage.setRGB(x, y, 0x00FFFFFF); // Transparent
                    } else {
                        newImage.setRGB(x, y, pixel);
                    }
                }
            }
            return newImage;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Charge une image depuis les ressources du projet.
     *
     * @param path Chemin de l'image
     * @return L'image chargée ou null si non trouvée
     */
    private Image chargerImage(String path) {
        java.net.URL imgURL = getClass().getClassLoader().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL).getImage();
        } else {
            System.err.println("Erreur : Image non trouvée " + path);
            return null;
        }
    }

    /**
     * Dessine les composants graphiques : image de fond et personnage.
     *
     * @param g Le contexte graphique
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (imageFond != null) {
            g.drawImage(imageFond, 0, 0, getWidth(), getHeight(), this);
        }

        if (imagePersonnage != null) {
            g.drawImage(imagePersonnage, xPersonnage, yPersonnage, 50, 50, this);
        }
    }
}
