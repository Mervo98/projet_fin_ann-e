package jeu;

import java.io.File;
import java.sql.*;

import static java.sql.DriverManager.getConnection;

public class Database {

    private static final String URL = "jdbc:sqlite:jeu.db";

    /**
     * Crée la table "utilisateurs" dans la base de données si elle n'existe pas déjà.
     * La table contient les informations nécessaires pour gérer les utilisateurs du jeu.
     */
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

    /**
     * Sauvegarde les informations d'un utilisateur dans la base de données.
     * Si l'utilisateur existe déjà, ses informations seront mises à jour.
     *
     * @param utilisateur L'objet Utilisateurs à sauvegarder.
     */
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

    /**
     * Charge un utilisateur à partir de la base de données en utilisant son nom d'utilisateur.
     *
     * @param nomUtilisateur Le nom de l'utilisateur à charger.
     * @return L'objet Utilisateurs correspondant à l'utilisateur chargé, ou null si l'utilisateur n'existe pas.
     */
    public static Utilisateurs chargerUtilisateur(String nomUtilisateur) {
        try (Connection conn = getConnection(URL)) {
            String query = "SELECT mot_de_passe, zone_courante, inventaire FROM utilisateurs WHERE nom_utilisateur = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, nomUtilisateur);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    String motDePasse = rs.getString("mot_de_passe");
                    String zoneCouranteStr = rs.getString("zone_courante");
                    Zone zoneCourante = Zone.obtenirZoneParNom(zoneCouranteStr);
                    String inventaireStr = rs.getString("inventaire");
                    Inventaire inventaire = stringToInventaire(inventaireStr);

                    return new Utilisateurs(nomUtilisateur, motDePasse, zoneCourante, inventaire);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Affiche l'erreur dans la console
        }
        return null;  // Retourne null si l'utilisateur n'a pas été trouvé
    }

    /**
     * Convertit un objet Inventaire en chaîne de caractères pour pouvoir le sauvegarder dans la base de données.
     *
     * @param inventaire L'objet Inventaire à convertir.
     * @return La chaîne représentant l'inventaire.
     */
    public static String inventaireToString(Inventaire inventaire) {
        return inventaire.afficherContenu();
    }

    /**
     * Convertit une chaîne représentant un inventaire en un objet Inventaire.
     *
     * @param inventaireStr La chaîne à convertir en inventaire.
     * @return L'objet Inventaire construit à partir de la chaîne donnée.
     */
    private static Inventaire stringToInventaire(String inventaireStr) {
        Inventaire inventaire = new Inventaire(); // Initialiser un inventaire vide par défaut

        if (inventaireStr == null || inventaireStr.isEmpty()) {
            // Si l'inventaire est null ou vide, retourner juste l'inventaire vide
            return inventaire;
        }

        // Sinon, on peut parser normalement
        String[] lignes = inventaireStr.split("\n");
        for (String ligne : lignes) {
            if (ligne.contains(" (Récupéré)")) {
                String nom = ligne.replace(" (Récupéré)", "").trim();
                inventaire.recupererIngredient(nom);
            }
        }

        return inventaire;
    }

    /**
     * Ajoute un nouvel utilisateur dans la base de données.
     *
     * @param nomUtilisateur Le nom de l'utilisateur à ajouter.
     * @param motDePasse Le mot de passe de l'utilisateur.
     * @return true si l'utilisateur a été ajouté avec succès, false sinon.
     */
    public static boolean ajouterUtilisateur(String nomUtilisateur, String motDePasse) {
        try (Connection conn = getConnection(URL)) {
            // Vérifie si l'utilisateur existe déjà
            String checkSql = "SELECT COUNT(*) FROM utilisateurs WHERE nom_utilisateur = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, nomUtilisateur);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                // L'utilisateur existe déjà
                System.out.println("Nom d'utilisateur déjà pris.");
                return false;
            }

            // Ajouter l'utilisateur s'il n'existe pas
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


    /**
     * Met à jour les informations d'un utilisateur dans la base de données.
     *
     * @param utilisateur L'objet Utilisateurs contenant les nouvelles informations à mettre à jour.
     */
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
