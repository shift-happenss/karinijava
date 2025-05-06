package org.example.services;

import org.example.entities.Evenement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServiceEmploiEvenement {

    private Connection cnx;

    public ServiceEmploiEvenement(Connection cnx) {
        this.cnx = cnx;
    }

    // Méthode pour associer une liste d’événements à un emploi
    public void addEvenementsToEmploi(int emploiId, List<Evenement> evenements) {
        String query = "INSERT INTO emploi_evenement (emploi_id, evenement_id) VALUES (?, ?)";
        try {
            PreparedStatement pst = cnx.prepareStatement(query);
            for (Evenement ev : evenements) {
                pst.setInt(1, emploiId);
                pst.setInt(2, ev.getId());
                pst.addBatch();
            }
            pst.executeBatch();
            System.out.println("Événements ajoutés à l'emploi " + emploiId);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Méthode pour récupérer tous les événements associés à un emploi
    public List<Evenement> getEvenementsByEmploi(int emploiId) {
        List<Evenement> evenements = new ArrayList<>();
        String query = "SELECT e.* FROM evenement e JOIN emploi_evenement ee ON e.id = ee.evenement_id WHERE ee.emploi_id = ?";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setInt(1, emploiId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Evenement ev = new Evenement();
                ev.setId(rs.getInt("id"));
                ev.setNom(rs.getString("nom"));
                ev.setType(rs.getString("type"));
                // Utiliser les noms corrects "date_debut" et "date_fin"
                ev.setDateDebut(rs.getDate("date_debut").toLocalDate());
                ev.setDateFin(rs.getDate("date_fin").toLocalDate());
                ev.setContenu(rs.getString("contenu"));
                ev.setLieu(rs.getString("lieu"));
                ev.setLatitude(rs.getDouble("latitude"));
                ev.setLongitude(rs.getDouble("longitude"));
                evenements.add(ev);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return evenements;
    }

}
