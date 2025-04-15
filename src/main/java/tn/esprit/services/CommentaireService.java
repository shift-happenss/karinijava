package tn.esprit.services;

import tn.esprit.models.Commentaire;
import tn.esprit.utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentaireService {

    private final Connection connection;

    public CommentaireService() {
        connection = MyDataBase.getInstance().getCnx(); // Connexion via Singleton
    }

    // Méthode pour récupérer tous les commentaires de la base de données
    public List<Commentaire> getAllCommentaires() {
        List<Commentaire> commentaires = new ArrayList<>();
        String sql = "SELECT * FROM commentaire";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Parcours les résultats de la requête
            while (rs.next()) {
                Commentaire commentaire = new Commentaire();

                // Récupère les données de chaque colonne et les affecte au commentaire
                commentaire.setId(rs.getInt("id"));

                // Vérifie si les valeurs sont nulles ou pas avant d'assigner
                commentaire.setProprietereId(rs.getObject("proprietere_id") != null ? rs.getInt("proprietere_id") : null);
                commentaire.setRessourceId(rs.getObject("ressource_id") != null ? rs.getInt("ressource_id") : null);
                commentaire.setContenu(rs.getString("contenu"));
                commentaire.setReponse(rs.getString("reponse"));

                // Ajoute le commentaire à la liste
                commentaires.add(commentaire);
            }

        } catch (SQLException e) {
            // Log the error and provide feedback to the developer
            System.err.println("Erreur lors de la récupération des commentaires : " + e.getMessage());
            e.printStackTrace();
        }

        return commentaires; // Retourne la liste des commentaires récupérés
    }
    public List<Commentaire> getCommentairesParRessource(int ressourceId) {
        List<Commentaire> commentaires = new ArrayList<>();

        try {
            String query = "SELECT * FROM commentaire WHERE ressource_id = ?";
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setInt(1, ressourceId);

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Commentaire c = new Commentaire();
                c.setId(rs.getInt("id"));
                c.setProprietereId(rs.getInt("proprietere_id"));
                c.setRessourceId(rs.getInt("ressource_id"));
                c.setContenu(rs.getString("contenu"));
                c.setReponse(rs.getString("reponse"));

                commentaires.add(c);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return commentaires;
    }

    // Méthode pour ajouter un commentaire (insertion)
    public void ajouterCommentaire(Commentaire commentaire) {
        String sql = "INSERT INTO commentaire (proprietere_id, ressource_id, contenu, reponse) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Remplir les paramètres de la requête
            stmt.setInt(1, commentaire.getProprietereId());
            stmt.setInt(2, commentaire.getRessourceId());
            stmt.setString(3, commentaire.getContenu());
            stmt.setString(4, commentaire.getReponse());

            System.out.println("Exécution de la requête : " + stmt.toString());  // Affichage de la requête SQL

            // Exécute la requête d'insertion
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Commentaire ajouté avec succès.");
            } else {
                System.out.println("Aucun commentaire ajouté.");
            }

        } catch (SQLException e) {
            e.printStackTrace();  // Affiche les erreurs de l'insertion
        }
    }
    public boolean deleteCommentaire(int commentaireId) {
        String req = "DELETE FROM commentaire WHERE id=?";

        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setInt(1, commentaireId);
            System.out.println("Exécution de la requête de suppression pour le commentaire ID : " + commentaireId); // Ajout du log

            int rowsAffected = pst.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("🗑️ Commentaire supprimé avec succès.");
                return true;
            } else {
                System.out.println("❌ Aucun commentaire trouvé avec cet ID : " + commentaireId); // Affiche l'ID si la suppression échoue
                return false;
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la suppression du commentaire ID " + commentaireId + " : " + e.getMessage());
            e.printStackTrace(); // Affiche les détails de l'erreur SQL
            return false;
        }
    }






}