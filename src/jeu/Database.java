package jeu;

import java.io.File;
import java.sql.*;

import static java.sql.DriverManager.getConnection;

public class Database {
    private static final String URL = "jdbc:sqlite:jeu.db";

    public static void creerTable() {
        File dir = new File("data");
        if (!dir.exists()) {
            dir.mkdir();
        }

        try (Connection conn = getConnection(URL)) {
            if (conn != null) {
                Statement stmt = conn.createStatement();
                String sql = "CREATE TABLE IF NOT EXISTS utilisateurs (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "nom_utilisateur TEXT NOT NULL UNIQUE, " +
                        "mot_de_passe TEXT NOT NULL, " +
                        "zone_courante TEXT, " +
                        "position_x INTEGER, " +
                        "position_y INTEGER, " +
                        "inventaire TEXT);";
                stmt.execute(sql);
                System.out.println("Table créée avec succès.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la création de la table : " + e.getMessage());
        }
    }

    public static void sauvegarderUtilisateur(Utilisateurs utilisateur) {
        try (Connection conn = getConnection(URL)) {
            String sql = "INSERT OR REPLACE INTO utilisateurs (nom_utilisateur, mot_de_passe, zone_courante, position_x, position_y, inventaire) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, utilisateur.getNomUtilisateur());
            stmt.setString(2, utilisateur.getMotDePasse());
            stmt.setString(3, utilisateur.getZoneCourante());
            stmt.setInt(4, utilisateur.getXPersonnage());
            stmt.setInt(5, utilisateur.getYPersonnage());
            stmt.setString(6, inventaireToString(utilisateur.getInventaire()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la sauvegarde de l'utilisateur : " + e.getMessage());
        }
    }

    public static Utilisateurs chargerUtilisateur(String nomUtilisateur) {
        try (Connection conn = getConnection(URL)) {
            String query = "SELECT * FROM utilisateurs WHERE nom_utilisateur = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, nomUtilisateur);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    String motDePasse = rs.getString("mot_de_passe");
                    Zone zoneCourante = Zone.obtenirZoneParNom(rs.getString("zone_courante"));
                    int x = rs.getInt("position_x");
                    int y = rs.getInt("position_y");
                    Inventaire inventaire = stringToInventaire(rs.getString("inventaire"));

                    Utilisateurs utilisateur = new Utilisateurs(nomUtilisateur, motDePasse, zoneCourante);
                    utilisateur.setXPersonnage(x);
                    utilisateur.setYPersonnage(y);
                    utilisateur.setInventaire(inventaire);

                    return utilisateur;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }



    public static String inventaireToString(Inventaire inventaire) {
        return inventaire.afficherContenu();  // Adaptation selon votre inventaire
    }

    private static Inventaire stringToInventaire(String inventaireStr) {
        return new Inventaire();  // Adaptation selon votre inventaire
    }

    public static boolean ajouterUtilisateur(String nomUtilisateur, String motDePasse) {
        try (Connection conn = getConnection(URL)) {
            String sql = "INSERT INTO utilisateurs (nom_utilisateur, mot_de_passe) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nomUtilisateur);
            stmt.setString(2, motDePasse);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void mettreAJourUtilisateur(Utilisateurs utilisateur) {
        try (Connection conn = getConnection(URL)) {
            // Préparer la requête de mise à jour
            String sql = "UPDATE utilisateurs SET zone_courante = ?, position_x = ?, position_y = ?, inventaire = ? WHERE nom_utilisateur = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);

            // Assurez-vous que les méthodes getZoneCourante(), getXPersonnage(), getYPersonnage() existent dans Utilisateurs
            stmt.setString(1, utilisateur.getZoneCourante().toString()); // Nom de la zone
            stmt.setInt(2, utilisateur.getXPersonnage()); // Position X
            stmt.setInt(3, utilisateur.getYPersonnage()); // Position Y
            stmt.setString(4, inventaireToString(utilisateur.getInventaire())); // Conversion de l'inventaire en chaîne
            stmt.setString(5, utilisateur.getNomUtilisateur()); // Nom d'utilisateur pour identifier l'enregistrement à mettre à jour

            // Exécuter la requête de mise à jour
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
               // System.out.println("Informations mises à jour avec succès pour l'utilisateur : " + utilisateur.getNomUtilisateur());
            } else {
                System.out.println("Aucune mise à jour effectuée.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la mise à jour de l'utilisateur : " + e.getMessage());
        }
    }


}
