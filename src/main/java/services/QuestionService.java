package services;

import connection.MaConnexion;
import entities.Examen;
import entities.Question;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionService {

    private final ExamenService examenService = new ExamenService(); // Pour récupérer les objets Examen

    public List<Question> getQuestionsByExamenId(int examenId) {
        List<Question> questions = new ArrayList<>();
        String sql = "SELECT * FROM question WHERE examen_id = ?";

        try (Connection conn = MaConnexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, examenId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Examen examen = examenService.trouverParId(rs.getInt("examen_id"));
                // Récupération de l’objet Examen
                Question q = new Question(
                        rs.getInt("id"),
                        examen,
                        rs.getString("texte"),
                        rs.getString("type")
                );
                questions.add(q);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return questions;
    }

    public void ajouterQuestion(Question question) {
        String query = "INSERT INTO question (examen_id, texte, type) VALUES (?, ?, ?)";

        try (Connection conn = MaConnexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, question.getExamen().getId()); // Récupération de l'ID depuis l'objet Examen
            stmt.setString(2, question.getTexte());
            stmt.setString(3, question.getType());

            stmt.executeUpdate();
            System.out.println("✅ Question ajoutée avec succès.");

        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de l'ajout de la question : " + e.getMessage());
        }
    }

    public void modifier(Question question) {
        String sql = "UPDATE question SET texte = ?, type = ?, examen_id = ? WHERE id = ?";

        try (Connection conn = MaConnexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, question.getTexte());
            stmt.setString(2, question.getType());
            stmt.setInt(3, question.getExamen().getId()); // Utilise getExamen().getId()
            stmt.setInt(4, question.getId());

            stmt.executeUpdate();

            System.out.println("✅ Question modifiée avec succès !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void supprimer(int id) {
        String sql = "DELETE FROM question WHERE id = ?";

        try (Connection conn = MaConnexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public List<Question> getQuestionsByExamen(int examenId) {
        List<Question> questions = new ArrayList<>();
        String sql = "SELECT * FROM question WHERE examen_id = ?";

        try (Connection conn = MaConnexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, examenId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Question q = new Question();
                q.setId(rs.getInt("id"));
                q.setTexte(rs.getString("texte"));
                q.setType(rs.getString("type"));
                // Optionnel : setExamen si tu veux le relier
                questions.add(q);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return questions;
    }



}
