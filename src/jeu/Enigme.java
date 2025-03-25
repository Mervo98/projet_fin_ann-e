package jeu;

public class Enigme {
    public GUI gui;

    public Inventaire inventaire;


    public Enigme(GUI gui) {
        this.inventaire = new Inventaire();

        this.gui = gui;
        System.out.println("Enigme reçoit une instance de GUI : " + gui);

    }
    // Énigme du labyrinthe du serpentcendre




}
