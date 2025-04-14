package services;

import connection.MaConnexion;
import entities.Reponse;
import entities.ReponseSoumise;
import entities.Question;

import java.sql.*;

public class ReponseSoumiseService {

    // Méthode pour ajouter la réponse soumise dans la base de données
    public void ajouterReponseSoumise(ReponseSoumise r) {
        if (r == null) {
            System.err.println("Erreur : L'objet ReponseSoumise est null.");
            return;
        }

        // Vérification si question ou réponse est null
        if (r.getQuestion() == null || r.getReponse() == null) {
            System.err.println("Erreur : Question ou Réponse est null dans ReponseSoumise.");
            return;
        }

        String req = "INSERT INTO reponse_soumise (question_id, reponse_id, texte, est_correct) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = MaConnexion.getConnection().prepareStatement(req)) {

            // Préparation des paramètres
            ps.setInt(1, r.getQuestion().getId());  // question_id
            ps.setInt(2, r.getReponse().getId());   // reponse_id
            ps.setString(3, r.getTexte());          // texte

            // Par défaut, on définit est_correct comme "xl"
            String estCorrect = "xl";  // valeur par défaut
            ps.setString(4, estCorrect);  // est_correct (valeur par défaut est "xl")

            // Exécution de la requête
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Réponse soumise ajoutée avec succès.");
            } else {
                System.out.println("Aucune réponse soumise ajoutée.");
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de l'insertion de la réponse soumise : " + e.getMessage());
            e.printStackTrace();
        }
    }




    // Méthode pour vérifier si la réponse soumise est correcte
    public boolean estBonneReponse(int reponseId, String texteSoumis) {
        String req = "SELECT texte FROM reponse WHERE id = ?";
        try (PreparedStatement ps = MaConnexion.getConnection().prepareStatement(req)) {
            ps.setInt(1, reponseId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String bonneReponse = rs.getString("texte");
                return bonneReponse.trim().equalsIgnoreCase(texteSoumis.trim());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Méthode pour obtenir la bonne réponse pour une question donnée
    public Reponse getBonneReponseByQuestionId(int questionId) {
        String req = "SELECT * FROM reponse WHERE question_id = ? AND est_correct = 1 LIMIT 1";
        try (PreparedStatement ps = MaConnexion.getConnection().prepareStatement(req)) {
            ps.setInt(1, questionId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                // Utilisation du constructeur sans arguments
                Reponse reponse = new Reponse();
                reponse.setId(rs.getInt("id"));
                reponse.setTexte(rs.getString("texte"));
                return reponse; // Retourne l'objet Reponse correctement initialisé
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Retourne null si aucune bonne réponse n'est trouvée
    }
    public void ajouterReponseSoumise(int questionId, String texteSoumis) {
        String sql = "INSERT INTO reponse_soumise (question_id, texte) VALUES (?, ?)";
        try (PreparedStatement ps = MaConnexion.getConnection().prepareStatement(sql)) {
            ps.setInt(1, questionId);
            ps.setString(2, texteSoumis);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}
