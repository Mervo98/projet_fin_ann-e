package jeu;

import javax.swing.*;
import java.awt.*;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.AlphaComposite;
import java.io.IOException;
import javax.imageio.ImageIO;


public class ImagePanel extends JPanel {
    private Image imageFond;
    private Image imagePersonnage;
    private int xPersonnage = 200, yPersonnage = 200;

    public void setImage(String imagePath) {
        imageFond = chargerImage(imagePath);
        repaint();
    }

    public void setPersonnageImage(String imagePath) {
        imagePersonnage = removeBackground(imagePath);

        repaint();
    }

    public void deplacerPersonnage(int x, int y) {
        this.xPersonnage = x;
        this.yPersonnage = y;
        repaint();
    }
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

                    // Vérifier si le pixel est proche du blanc
                    if (color.getRed() > 240 && color.getGreen() > 240 && color.getBlue() > 240) {
                        newImage.setRGB(x, y, 0x00FFFFFF); // Rendre transparent
                    } else {
                        newImage.setRGB(x, y, pixel); // Garder le pixel
                    }
                }
            }
            return newImage;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    private Image chargerImage(String path) {
        java.net.URL imgURL = getClass().getClassLoader().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL).getImage();
        } else {
            System.err.println("Erreur : Image non trouvée " + path);
            return null;
        }
    }

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
