package org.example.services;

import org.example.entities.Question;
import org.example.entities.Reponse;
import org.example.utils.MyDataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReponseService implements IServicee<Reponse> {

    Connection connection;

    public ReponseService() {
        connection = MyDataBase.getInstance().getMyConnection();
    }
    // Constructeur


    // ✅ Ajouter une réponse
    public void ajouterReponse(Reponse reponse, int idQuestion) {
        if (reponseExiste(reponse.getTexte(), idQuestion)) {
            System.out.println("⚠️ Cette réponse existe déjà pour cette question !");
            return;
        }

        String query = "INSERT INTO reponse (texte, est_correcte, question_id) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, reponse.getTexte());
            stmt.setString(2, reponse.getEstCorrecte());
            stmt.setInt(3, idQuestion);
            int rowsInserted = stmt.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("✅ Réponse ajoutée avec succès !");
                if (isFirstReponse(idQuestion)) {
                    System.out.println("🚨 C'est la première réponse ajoutée pour cette question !");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Vérifie si c'est la première réponse pour cette question
    private boolean isFirstReponse(int idQuestion) {
        String query = "SELECT COUNT(*) FROM reponse WHERE question_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idQuestion);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 1; // Si le nombre de réponses est égal à 1, c'est la première réponse
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ✅ Récupérer les réponses via un objet Question
    public List<Reponse> getReponsesParQuestion(Question question) {
        List<Reponse> reponses = new ArrayList<>();
        String query = "SELECT * FROM reponse WHERE question_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, question.getId());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Reponse reponse = new Reponse();
                reponse.setId(rs.getInt("id"));
                reponse.setTexte(rs.getString("texte"));
                reponse.setEstCorrecte(rs.getString("est_correcte"));
                reponses.add(reponse);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reponses;
    }

    // ✅ Récupérer les réponses via l'ID de la question
    public List<Reponse> getReponsesByQuestionId(int idQuestion) {
        List<Reponse> reponses = new ArrayList<>();
        String query = "SELECT * FROM reponse WHERE question_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idQuestion);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Reponse reponse = new Reponse();
                reponse.setId(rs.getInt("id"));
                reponse.setTexte(rs.getString("texte"));
                reponse.setEstCorrecte(rs.getString("est_correcte"));
                reponses.add(reponse);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reponses;
    }

    // ✅ Supprimer une réponse
    public void supprimerReponse(int id) {
        try {
            //Connection cnx = MyDataBase.getInstance().getMyConnection();
            String sql = "DELETE FROM reponse WHERE id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("✅ Réponse supprimée !");
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la suppression : " + e.getMessage());
        }
    }

    // ✅ Modifier une réponse
    public void modifierReponse(Reponse reponse) {
        String query = "UPDATE reponse SET texte = ?, est_correcte = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, reponse.getTexte());
            statement.setString(2, reponse.getEstCorrecte());
            statement.setInt(3, reponse.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Vérifie si la réponse existe déjà pour la question donnée
    private boolean reponseExiste(String texte, int idQuestion) {
        texte = normalizeText(texte);
        String query = "SELECT COUNT(*) FROM reponse WHERE texte = ? AND question_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, texte);
            stmt.setInt(2, idQuestion);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Normalise le texte pour éliminer les espaces et les caractères invisibles
    private String normalizeText(String text) {
        return text.replaceAll("\\s+", " ").trim();
    }

    public void ajouterReponseSoumise(int questionId, String texteSoumis) {
        String sql = "INSERT INTO reponse_soumise (question_id, texte) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, questionId);
            ps.setString(2, texteSoumis);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Reponse> getBonneReponseByQuestionId(int questionId) {
        List<Reponse> liste = new ArrayList<>();
        try {
            //  Connection con = MyDataBase.getInstance().getMyConnection();
            String req = "SELECT * FROM reponse WHERE question_id = ? AND est_correcte = 'vrai'";
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setInt(1, questionId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Reponse r = new Reponse();
                r.setId(rs.getInt("id"));
                r.setTexte(rs.getString("texte"));
                r.setEstCorrecte(rs.getString("est_correcte"));
                liste.add(r);
            }
        } catch (SQLException e) {
            System.out.println("Erreur getBonneReponseByQuestionId: " + e.getMessage());
        }
        return liste;
    }

    // Méthodes de l'interface IService
    @Override
    public boolean ajouter(Reponse reponse) {
        ajouterReponse(reponse, reponse.getQuestion().getId());
        return true;
    }

    @Override
    public void modifier(Reponse reponse) {
        modifierReponse(reponse);
    }

    @Override
    public boolean supprimer(int id) {
        supprimerReponse(id);
        return true;
    }

    @Override
    public List<Reponse> afficher() {
        // Implémentez une logique pour afficher toutes les réponses si nécessaire
        return null;
    }

    @Override
    public Reponse trouverParId(int id) {
        Reponse reponse = null;
        String query = "SELECT * FROM reponse WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                reponse = new Reponse();
                reponse.setId(rs.getInt("id"));
                reponse.setTexte(rs.getString("texte"));
                reponse.setEstCorrecte(rs.getString("est_correcte"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reponse;
    }
}
