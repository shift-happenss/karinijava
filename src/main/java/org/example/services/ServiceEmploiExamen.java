package org.example.services;

import org.example.entities.Examen;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceEmploiExamen {
    private final Connection cnx;

    public ServiceEmploiExamen(Connection cnx) {
        this.cnx = cnx;
    }

    public void addExamensToEmploi(int emploiId, List<Examen> list) {
        String sql = "INSERT INTO emploi_examen(emploi_id, examen_id) VALUES(?,?)";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            for (Examen e : list) {
                ps.setInt(1, emploiId);
                ps.setInt(2, e.getId());
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Examen> getExamensByEmploi(int emploiId) {
        List<Examen> list = new ArrayList<>();
        String sql = "SELECT e.* FROM examen e " +
                "JOIN emploi_examen ee ON e.id=ee.examen_id " +
                "WHERE ee.emploi_id=?";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, emploiId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Examen ex = new Examen();
                ex.setId(rs.getInt("id"));
                ex.setTitre(rs.getString("titre"));
                ex.setDescription(rs.getString("description"));
                ex.setNote(rs.getDouble("note"));
                list.add(ex);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
