package tn.esprit.services;

import tn.esprit.entities.Evenement;
import tn.esprit.utils.MyDataBase;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ServiceEvenement {

    private final Connection conn;

    public ServiceEvenement() throws SQLException {
        conn = MyDataBase.getInstance().getMyConnection();
    }

    // Ajouter un Evenement
    public void ajouterEvenement(Evenement ev) {
        String req = "INSERT INTO evenement (nom, type, date_debut, date_fin, contenu, lieu, latitude, longitude) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pst = conn.prepareStatement(req)) {
            pst.setString(1, ev.getNom());
            pst.setString(2, ev.getType());
            pst.setDate(3, Date.valueOf(ev.getDateDebut()));
            pst.setDate(4, Date.valueOf(ev.getDateFin()));
            pst.setString(5, ev.getContenu());
            pst.setString(6, ev.getLieu());
            pst.setDouble(7, ev.getLatitude());
            pst.setDouble(8, ev.getLongitude());
            pst.executeUpdate();
            System.out.println("✅ Evenement ajouté avec succès !");
        } catch (SQLException ex) {
            System.out.println("❌ Erreur lors de l'ajout de l'événement : " + ex.getMessage());
        }
    }

    // Afficher tous les Evenements
    public List<Evenement> afficherEvenements() {
        List<Evenement> evenements = new ArrayList<>();
        String req = "SELECT * FROM evenement";
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(req)) {
            while (rs.next()) {
                Evenement ev = new Evenement(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("type"),
                        rs.getDate("date_debut").toLocalDate(),
                        rs.getDate("date_fin").toLocalDate(),
                        rs.getString("contenu"),
                        rs.getString("lieu"),
                        rs.getDouble("latitude"),
                        rs.getDouble("longitude")
                );
                evenements.add(ev);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return evenements;
    }

    // Modifier un Evenement existant
    public void modifierEvenement(Evenement ev) {
        String req = "UPDATE evenement SET nom = ?, type = ?, date_debut = ?, date_fin = ?, contenu = ?, lieu = ?, latitude = ?, longitude = ? WHERE id = ?";
        try (PreparedStatement pst = conn.prepareStatement(req)) {
            pst.setString(1, ev.getNom());
            pst.setString(2, ev.getType());
            pst.setDate(3, Date.valueOf(ev.getDateDebut()));
            pst.setDate(4, Date.valueOf(ev.getDateFin()));
            pst.setString(5, ev.getContenu());
            pst.setString(6, ev.getLieu());
            pst.setDouble(7, ev.getLatitude());
            pst.setDouble(8, ev.getLongitude());
            pst.setInt(9, ev.getId());
            pst.executeUpdate();
            System.out.println("✏️ Evenement modifié avec succès !");
        } catch (SQLException ex) {
            System.out.println("❌ Erreur lors de la modification de l'événement : " + ex.getMessage());
        }
    }

    // Supprimer un Evenement
    public void supprimerEvenement(int id) {
        String req = "DELETE FROM evenement WHERE id = ?";
        try (PreparedStatement pst = conn.prepareStatement(req)) {
            pst.setInt(1, id);
            pst.executeUpdate();
            System.out.println("🗑️ Evenement supprimé avec succès !");
        } catch (SQLException ex) {
            System.out.println("❌ Erreur lors de la suppression de l'événement : " + ex.getMessage());
        }
    }
}
