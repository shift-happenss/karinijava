package org.example.services;

import org.example.entities.Cours;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ServiceEmploiCours {
    private final Connection cnx;

    public ServiceEmploiCours(Connection cnx) {
        this.cnx = cnx;
    }

    public void addCoursToEmploi(int emploiId, List<Cours> list) {
        String sql = "INSERT INTO emploi_cours(emploi_id, cours_id) VALUES(?,?)";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            for (Cours c : list) {
                ps.setInt(1, emploiId);
                ps.setInt(2, c.getId());
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Cours> getCoursByEmploi(int emploiId) {
        List<Cours> list = new ArrayList<>();
        String sql = "SELECT c.* FROM cours c " +
                "JOIN emploi_cours ec ON c.id=ec.cours_id " +
                "WHERE ec.emploi_id=?";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, emploiId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Cours c = new Cours();
                c.setId(rs.getInt("id"));
                c.setTitre(rs.getString("titre"));
                c.setDescription(rs.getString("description"));
                Timestamp t1 = rs.getTimestamp("dateDebut");
                Timestamp t2 = rs.getTimestamp("dateFin");
                if (t1 != null) c.setDateDebut(t1.toLocalDateTime());
                if (t2 != null) c.setDateFin(t2.toLocalDateTime());
                c.setLien(rs.getString("lien"));
                list.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
