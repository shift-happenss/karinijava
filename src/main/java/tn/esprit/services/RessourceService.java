package tn.esprit.services;

import tn.esprit.models.Ressource;
import tn.esprit.utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RessourceService {

    private Connection cnx;

    public RessourceService() {
        cnx = MyDataBase.getInstance().getCnx();
    }

    // CREATE
    // CREATE
    public void ajouterRessource(Ressource r) {
        String req = "INSERT INTO ressource (category_id, titre, description, type, url_video, url_image, url_fichier, contenu_texte) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pst = cnx.prepareStatement(req)) {
            // Check if the resource object has valid data
            if (r.getTitre() == null || r.getDescription() == null || r.getType() == null) {
                System.err.println("❌ Ressource invalide: Titre, Description ou Type manquant.");
                return;
            }

            pst.setInt(1, r.getCategoryId());
            pst.setString(2, r.getTitre());
            pst.setString(3, r.getDescription());
            pst.setString(4, r.getType());
            pst.setString(5, r.getUrlVideo());
            pst.setString(6, r.getUrlImage());
            pst.setString(7, r.getUrlFichier());
            pst.setString(8, r.getContenuTexte());

            int rowsAffected = pst.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("✅ Ressource ajoutée avec succès.");
            } else {
                System.err.println("❌ Aucun changement n'a été effectué. Ressource non ajoutée.");
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de l'ajout de la ressource: " + e.getMessage());
            e.printStackTrace();  // Print stack trace to help debug the issue
        }
    }


    // READ
    public List<Ressource> afficherRessources() {
        List<Ressource> list = new ArrayList<>();
        String req = "SELECT * FROM ressource";

        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(req)) {

            while (rs.next()) {
                Ressource r = new Ressource(
                        rs.getInt("id"),
                        rs.getInt("category_id"),
                        rs.getString("titre"),
                        rs.getString("description"),
                        rs.getString("type"),
                        rs.getString("url_video"),
                        rs.getString("url_image"),
                        rs.getString("url_fichier"),
                        rs.getString("contenu_texte")
                );
                list.add(r);
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la récupération : " + e.getMessage());
        }

        return list;
    }

    // ALIAS de afficherRessources()
    public List<Ressource> getAll() {
        return afficherRessources();
    }

    // UPDATE
    public void modifierRessource(Ressource r) {
        String req = "UPDATE ressource SET category_id=?, titre=?, description=?, type=?, url_video=?, url_image=?, url_fichier=?, contenu_texte=? WHERE id=?";

        // Affichage des données envoyées
        System.out.println("Essai de mise à jour de la ressource avec ID = " + r.getId());
        System.out.println("Données envoyées : " + r.toString());

        try (PreparedStatement pst = cnx.prepareStatement(req)) {
            // Initialisation des paramètres dans la requête
            pst.setInt(1, r.getCategoryId());
            pst.setString(2, r.getTitre());
            pst.setString(3, r.getDescription());
            pst.setString(4, r.getType());
            pst.setString(5, r.getUrlVideo());
            pst.setString(6, r.getUrlImage());
            pst.setString(7, r.getUrlFichier());
            pst.setString(8, r.getContenuTexte());
            pst.setInt(9, r.getId());

            // Exécution de la mise à jour
            int rowsAffected = pst.executeUpdate();
            System.out.println("Lignes affectées par la mise à jour : " + rowsAffected);

            // Vérification du résultat
            if (rowsAffected > 0) {
                System.out.println("✅ La ressource a été mise à jour avec succès.");
            } else {
                System.err.println("❌ La ressource avec l'ID " + r.getId() + " n'a pas été mise à jour.");
            }

        } catch (SQLException e) {
            // Gestion des erreurs
            e.printStackTrace(); // Afficher la trace complète de l'exception
            System.err.println("Erreur lors de la mise à jour de la ressource : " + e.getMessage());
        }
    }


    public boolean supprimerRessource(int id) {
        String req = "DELETE FROM ressource WHERE id=?";

        try (PreparedStatement pst = cnx.prepareStatement(req)) {
            pst.setInt(1, id);
            int rowsAffected = pst.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("🗑️ Ressource supprimée avec succès.");
                return true;  // Return true if the deletion was successful
            } else {
                System.out.println("❌ Aucune ressource trouvée avec cet ID.");
                return false; // Return false if no rows were affected
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la suppression : " + e.getMessage());
            return false;  // Return false if an error occurred
        }
    }

}
