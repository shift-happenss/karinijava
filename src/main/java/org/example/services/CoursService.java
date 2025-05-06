package org.example.services;

import org.example.entities.Cours;
import org.example.utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CoursService implements IServicee<Cours> {

    Connection connection;

    public CoursService() {
        connection = MyDataBase.getInstance().getMyConnection();
    }


    public List<Cours> afficher() {
        List<Cours> coursList = new ArrayList<>();
        String query = "SELECT * FROM cours";

        try (
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Cours cours = new Cours(
                        rs.getInt("id"),
                        rs.getString("titre"),
                        rs.getString("description"),
                        rs.getTimestamp("dateDebut") != null ? rs.getTimestamp("date_debut").toLocalDateTime() : null,
                        rs.getTimestamp("dateFin") != null ? rs.getTimestamp("date_fin").toLocalDateTime() : null,
                        rs.getString("lien")
                );
                coursList.add(cours);
            }

        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de l'affichage des cours : " + e.getMessage());
        }

        return coursList;
    }

    @Override
    public boolean ajouter(Cours cours) {
        String query = "INSERT INTO cours (titre, description, date_debut, date_fin, lien) VALUES (?, ?, ?, ?, ?)";

        try (
                PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, cours.getTitre());
            stmt.setString(2, cours.getDescription());
            stmt.setTimestamp(3, cours.getDateDebut() != null ? Timestamp.valueOf(cours.getDateDebut()) : null);
            stmt.setTimestamp(4, cours.getDateFin() != null ? Timestamp.valueOf(cours.getDateFin()) : null);
            stmt.setString(5, cours.getLien());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de l'ajout du cours : " + e.getMessage());
            return false;
        }
    }

    @Override
    public void modifier(Cours cours) {
        String query = "UPDATE cours SET titre = ?, description = ?, date_debut = ?, date_fin = ?, lien = ? WHERE id = ?";

        try (
                PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, cours.getTitre());
            stmt.setString(2, cours.getDescription());
            stmt.setTimestamp(3, cours.getDateDebut() != null ? Timestamp.valueOf(cours.getDateDebut()) : null);
            stmt.setTimestamp(4, cours.getDateFin() != null ? Timestamp.valueOf(cours.getDateFin()) : null);
            stmt.setString(5, cours.getLien());
            stmt.setInt(6, cours.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la modification du cours : " + e.getMessage());
        }
    }



    @Override
    public boolean supprimer(int coursId) {
        String query = "DELETE FROM cours WHERE id = ?";

        try (
                PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, coursId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la suppression du cours : " + e.getMessage());
            return false;
        }
    }

    @Override
    public Cours trouverParId(int coursId) {
        Cours cours = null;
        String query = "SELECT * FROM cours WHERE id = ?";

        try (
                PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, coursId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                cours = new Cours(
                        rs.getInt("id"),
                        rs.getString("titre"),
                        rs.getString("description"),
                        rs.getTimestamp("dateDebut") != null ? rs.getTimestamp("date_debut").toLocalDateTime() : null,
                        rs.getTimestamp("dateFin") != null ? rs.getTimestamp("date_fin").toLocalDateTime() : null,
                        rs.getString("lien")
                );
            }

        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la recherche du cours par ID : " + e.getMessage());
        }

        return cours;
    }
}
