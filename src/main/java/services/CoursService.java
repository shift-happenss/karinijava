package services;

import connection.MaConnexion;
import entities.Cours;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CoursService {

    // Méthode pour afficher tous les cours
    public List<Cours> afficher() {
        List<Cours> coursList = new ArrayList<>();
        String query = "SELECT * FROM cours";

        try (Connection conn = MaConnexion.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Cours cours = new Cours(
                        rs.getInt("id"),
                        rs.getString("titre"),
                        rs.getString("description"),
                        rs.getTimestamp("date_debut") != null ? rs.getTimestamp("date_debut").toLocalDateTime() : null,
                        rs.getTimestamp("date_fin") != null ? rs.getTimestamp("date_fin").toLocalDateTime() : null,
                        rs.getString("lien")
                );
                coursList.add(cours);
            }

        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de l'affichage des cours : " + e.getMessage());
        }

        return coursList;
    }

    // Méthode pour ajouter un cours
    public boolean ajouter(Cours cours) {
        String query = "INSERT INTO cours (titre, description, date_debut, date_fin, lien) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = MaConnexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, cours.getTitre());
            stmt.setString(2, cours.getDescription());
            stmt.setTimestamp(3, cours.getDateDebut() != null ? Timestamp.valueOf(cours.getDateDebut()) : null);
            stmt.setTimestamp(4, cours.getDateFin() != null ? Timestamp.valueOf(cours.getDateFin()) : null);
            stmt.setString(5, cours.getLien());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;  // Retourner true si l'ajout a réussi
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de l'ajout du cours : " + e.getMessage());
            return false;  // Retourner false en cas d'erreur
        }
    }

    // Méthode pour modifier un cours
    public void modifier(Cours cours) {
        String query = "UPDATE cours SET titre = ?, description = ?, date_debut = ?, date_fin = ?, lien = ? WHERE id = ?";

        try (Connection conn = MaConnexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, cours.getTitre());
            stmt.setString(2, cours.getDescription());
            stmt.setTimestamp(3, cours.getDateDebut() != null ? Timestamp.valueOf(cours.getDateDebut()) : null);  // LocalDateTime -> Timestamp
            stmt.setTimestamp(4, cours.getDateFin() != null ? Timestamp.valueOf(cours.getDateFin()) : null);    // LocalDateTime -> Timestamp
            stmt.setString(5, cours.getLien());
            stmt.setInt(6, cours.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la modification du cours : " + e.getMessage());
        }
    }

    // Méthode pour supprimer un cours
    public boolean supprimer(int coursId) {
        String query = "DELETE FROM cours WHERE id = ?";

        try (Connection conn = MaConnexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, coursId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la suppression du cours : " + e.getMessage());
            return false;
        }
    }

    // Méthode pour trouver un cours par son ID
    public Cours trouverParId(int coursId) {
        Cours cours = null;
        String query = "SELECT * FROM cours WHERE id = ?";

        try (Connection conn = MaConnexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, coursId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                cours = new Cours(
                        rs.getInt("id"),
                        rs.getString("titre"),
                        rs.getString("description"),
                        rs.getTimestamp("date_debut") != null ? rs.getTimestamp("date_debut").toLocalDateTime() : null,
                        rs.getTimestamp("date_fin") != null ? rs.getTimestamp("date_fin").toLocalDateTime() : null,
                        rs.getString("lien")
                );
            }

        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la recherche du cours par ID : " + e.getMessage());
        }

        return cours;
    }
}
