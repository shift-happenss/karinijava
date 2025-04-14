package services;

import connection.MaConnexion;
import entities.Examen;
import entities.Cours;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExamenService {

    private Connection connection;

    public ExamenService() {
        this.connection = MaConnexion.getInstance();
    }

    // Méthode pour ajouter un examen
    public void ajouterExamen(Examen examen) {
        try {
            String query = "INSERT INTO examen (titre, description, note, cours_id) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, examen.getTitre());
            stmt.setString(2, examen.getDescription());
            stmt.setDouble(3, examen.getNote());
            stmt.setInt(4, examen.getCours().getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour lister tous les examens
    public List<Examen> listerExamens() {
        List<Examen> examensList = new ArrayList<>();
        try {
            String query = "SELECT * FROM examen";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String titre = resultSet.getString("titre");
                String description = resultSet.getString("description");
                double note = resultSet.getDouble("note");
                int coursId = resultSet.getInt("cours_id");

                // Récupérer le cours correspondant à cet examen
                Cours cours = getCoursById(coursId);

                Examen examen = new Examen(id, titre, description, note, cours);
                examensList.add(examen);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return examensList;
    }

    // Méthode pour récupérer un cours par son ID
    private Cours getCoursById(int id) {
        try {
            String query = "SELECT * FROM cours WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, id);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                return new Cours(
                        resultSet.getInt("id"),
                        resultSet.getString("titre"),
                        resultSet.getString("description"),
                        resultSet.getDate("date_debut").toLocalDate(),
                        resultSet.getDate("date_fin").toLocalDate(),
                        resultSet.getString("lien")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Méthode pour modifier un examen
    public void modifierExamen(Examen examen) {
        try {
            String query = "UPDATE examen SET titre = ?, description = ?, note = ?, cours_id = ? WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, examen.getTitre());
            stmt.setString(2, examen.getDescription());
            stmt.setDouble(3, examen.getNote());
            stmt.setInt(4, examen.getCours().getId());
            stmt.setInt(5, examen.getId());  // Assurer que l'ID de l'examen est passé pour la mise à jour

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour supprimer un examen
    public void supprimerExamen(int examenId) {
        try {
            String query = "DELETE FROM examen WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, examenId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
