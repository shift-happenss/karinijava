package org.example.services;

import org.example.entities.Cours;
import org.example.entities.Examen;
import org.example.utils.MyDataBase;
import org.example.services.CoursService;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExamenService implements IServicee<Examen> {
    Connection connection;

    public ExamenService() {
        connection = MyDataBase.getInstance().getMyConnection();
        this.coursService = new CoursService();

    }

    private CoursService coursService;

    /*public ExamenService() {
        this.coursService = new CoursService();
    }
*/
    // Méthode pour afficher tous les examens
    @Override
    public List<Examen> afficher() {
        List<Examen> examenList = new ArrayList<>();
        String query = "SELECT e.*, c.titre AS cours_titre, c.description AS cours_description FROM examen e LEFT JOIN cours c ON e.cours_id = c.id";

        try (
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Cours cours = new Cours(
                        rs.getInt("cours_id"),
                        rs.getString("cours_titre"),
                        rs.getString("cours_description")
                );

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
    @Override
    public boolean ajouter(Examen examen) {
        if (examen == null || examen.getCours() == null) {
            System.err.println("❌ Examen invalide ou cours non spécifié.");
            return false;
        }

        String query = "INSERT INTO examen (cours_id, titre, description, note) VALUES (?, ?, ?, ?)";

        try (
                PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, examen.getCours().getId());
            stmt.setString(2, examen.getTitre());
            stmt.setString(3, examen.getDescription());
            stmt.setDouble(4, examen.getNote());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Examen ajouté avec succès.");
                return true;
            } else {
                System.out.println("⚠️ Aucun examen n'a été ajouté.");
                return false;
            }

        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de l'ajout de l'examen : " + e.getMessage());
            return false;
        }
    }

    // Méthode pour modifier un examen
    @Override
    public void modifier(Examen examen) {
        String query = "UPDATE examen SET titre = ?, description = ?, cours_id = ?, note = ? WHERE id = ?";

        try (
                PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, examen.getTitre());
            stmt.setString(2, examen.getDescription());
            stmt.setInt(3, examen.getCours().getId());
            stmt.setDouble(4, examen.getNote());
            stmt.setInt(5, examen.getId());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Examen modifié avec succès.");
            } else {
                System.out.println("⚠️ Aucun examen trouvé à modifier.");
            }

        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la modification de l'examen : " + e.getMessage());
        }
    }

    // Méthode pour supprimer un examen
    @Override
    public boolean supprimer(int examenId) {
        String query = "DELETE FROM examen WHERE id = ?";

        try (
                PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, examenId);
            int rows = stmt.executeUpdate();

            if (rows > 0) {
                System.out.println("✅ Examen supprimé avec succès.");
                return true;
            } else {
                System.out.println("⚠️ Aucune suppression effectuée, examen introuvable.");
                return false;
            }

        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la suppression de l'examen : " + e.getMessage());
            return false;
        }
    }

    // Méthode pour trouver un examen par ID
    @Override
    public Examen trouverParId(int examenId) {
        Examen examen = null;
        String sql = "SELECT * FROM examen WHERE id = ?";

        try (//Connection connection = MaConnexion.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, examenId);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                examen = new Examen();
                examen.setId(rs.getInt("id"));
                examen.setTitre(rs.getString("titre"));
                examen.setDescription(rs.getString("description"));
                examen.setNote(rs.getDouble("note"));

                int coursId = rs.getInt("cours_id");
                Cours cours = coursService.trouverParId(coursId);
                examen.setCours(cours);
            }

        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la recherche de l'examen : " + e.getMessage());
        }

        return examen;
    }

    // Méthode spécifique à cette entité : mise à jour de la note
    public void updateNoteForExam(int examId, double note) {
        String sql = "UPDATE examen SET note = ? WHERE id = ?";

        try (
                PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setDouble(1, note);
            stmt.setInt(2, examId);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Note mise à jour pour l'examen ID : " + examId);
            } else {
                System.out.println("⚠️ Aucun examen trouvé pour mettre à jour la note.");
            }

        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la mise à jour de la note : " + e.getMessage());
        }
    }
}
