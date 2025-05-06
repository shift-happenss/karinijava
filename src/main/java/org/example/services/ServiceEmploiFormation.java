package org.example.services;

import org.example.entities.Formation;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceEmploiFormation {
    private final Connection cnx;

    public ServiceEmploiFormation(Connection cnx) {
        this.cnx = cnx;
    }

    public void addFormationsToEmploi(int emploiId, List<Formation> list) {
        String sql = "INSERT INTO emploi_formation(emploi_id, formation_id) VALUES(?,?)";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            for (Formation f : list) {
                ps.setInt(1, emploiId);
                ps.setInt(2, f.getId());
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Formation> getFormationsByEmploi(int emploiId) {
        List<Formation> list = new ArrayList<>();
        String sql = "SELECT f.* FROM formation f " +
                "JOIN emploi_formation ef ON f.id=ef.formation_id " +
                "WHERE ef.emploi_id=?";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, emploiId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Formation f = new Formation();
                f.setId(rs.getInt("id"));
                f.setTitre(rs.getString("titre"));
                f.setDescription(rs.getString("description"));
                f.setCible(rs.getString("cible"));
                f.setFormateur(rs.getString("formateur"));
                f.setEtat(rs.getString("etat"));
                list.add(f);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
