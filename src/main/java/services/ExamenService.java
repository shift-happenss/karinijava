package services;

import connection.MaConnexion;
import entities.Cours;
import entities.Examen;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExamenService {

    // Déclarez un objet coursService
    private CoursService coursService;

    // Constructeur pour initialiser coursService
    public ExamenService() {
        this.coursService = new CoursService();  // Initialisation de coursService
    }

    // Méthode pour afficher tous les examens
    public List<Examen> afficher() {
        List<Examen> examenList = new ArrayList<>();
        String query = "SELECT e.*, c.titre as cours_titre, c.description as cours_description FROM examen e LEFT JOIN cours c ON e.cours_id = c.id";

        try (Connection conn = MaConnexion.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                // Création de l'objet Cours à partir des données récupérées
                Cours cours = new Cours(rs.getInt("cours_id"), rs.getString("cours_titre"), rs.getString("cours_description"));

                // Création de l'objet Examen à partir des données récupérées
                Examen examen = new Examen(
                        rs.getInt("id"),
                        cours,
                        rs.getString("titre"),
                        rs.getString("description"),
                        rs.getDouble("note")
                );
                examenList.add(examen);
            }

        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de l'affichage des examens : " + e.getMessage());
        }

        return examenList;
    }

    // Méthode pour ajouter un nouvel examen
    public void ajouter(Examen examen) {
        if (examen == null || examen.getCours() == null) {
            System.err.println("❌ Erreur : l'examen ou le cours associé est nul");
            return;
        }

        String query = "INSERT INTO examen (cours_id, titre, description, note) VALUES (?, ?, ?, ?)";

        try (Connection conn = MaConnexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, examen.getCours().getId());
            stmt.setString(2, examen.getTitre());
            stmt.setString(3, examen.getDescription());
            stmt.setDouble(4, examen.getNote());

            stmt.executeUpdate();
            System.out.println("✅ Examen ajouté avec succès.");

        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de l'ajout de l'examen : " + e.getMessage());
        }
    }

    // Méthode pour modifier un examen
    public boolean modifier(Examen examen) {
        String query = "UPDATE examen SET titre = ?, description = ?, cours_id = ?, note = ? WHERE id = ?";
        try (Connection conn = MaConnexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, examen.getTitre());
            stmt.setString(2, examen.getDescription());
            stmt.setInt(3, examen.getCours().getId()); // Assurez-vous que Cours a un ID valide
            stmt.setDouble(4, examen.getNote());
            stmt.setInt(5, examen.getId()); // Utiliser l'ID de l'examen pour la mise à jour

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;  // Retourner true si la mise à jour a réussi
        } catch (SQLException e) {
            e.printStackTrace();
            return false;  // Retourner false en cas d'erreur
        }
    }

    // Méthode pour supprimer un examen
    public boolean supprimer(int examenId) {
        String query = "DELETE FROM examen WHERE id = ?";

        try (Connection conn = MaConnexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, examenId);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("✅ Examen supprimé avec succès.");
                return true;
            } else {
                System.out.println("❌ Aucune suppression effectuée (examen introuvable).");
                return false;
            }

        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la suppression de l'examen : " + e.getMessage());
            return false;
        }
    }

    // Méthode pour trouver un examen par ID
    public Examen trouverParId(int examenId) {
        Examen examen = null;
        String sql = "SELECT * FROM examen WHERE id = ?";  // Requête SQL pour trouver un examen par ID

        try (Connection connection = MaConnexion.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, examenId);  // Remplacer le paramètre ? par l'ID de l'examen
            ResultSet resultSet = statement.executeQuery();

            // Vérifier si un examen a été trouvé
            if (resultSet.next()) {
                examen = new Examen();
                examen.setId(resultSet.getInt("id"));
                examen.setTitre(resultSet.getString("titre"));
                examen.setDescription(resultSet.getString("description"));
                examen.setNote(resultSet.getInt("note"));

                // Si vous avez une relation avec la table "cours", vous pouvez récupérer l'objet "cours"
                int coursId = resultSet.getInt("cours_id");
                Cours cours = coursService.trouverParId(coursId);  // Appel à coursService
                examen.setCours(cours);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return examen;  // Retourner l'examen trouvé, ou null si aucun examen n'est trouvé
    }
    public void updateNoteForExam(int examId, double note) {
        String sql = "UPDATE examen SET note = ? WHERE id = ?";
        try (Connection conn = MaConnexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, note);
            stmt.setInt(2, examId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
