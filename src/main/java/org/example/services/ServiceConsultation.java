package org.example.services;

import org.example.entities.Consultation;
import org.example.entities.Psy;
import org.example.utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceConsultation implements IService<Consultation> {

    Connection connection;

    public ServiceConsultation() {
        connection = MyDataBase.getInstance().getMyConnection();
    }

    @Override
    public void ajouter(Consultation consultation) throws SQLException {
        String sql = "INSERT INTO consultation (psy_id, date, time, raison, status, excuse) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, consultation.getPsy().getId());
        ps.setString(2, consultation.getDate());
        ps.setString(3, consultation.getTime());
        ps.setString(4, consultation.getRaison());
        ps.setString(5, consultation.getStatus());
        ps.setString(6, consultation.getExcuse());
        ps.executeUpdate();
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM consultation WHERE id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();
    }

    @Override
    public void modifier(Consultation c) throws SQLException {
        String sql = "UPDATE consultation SET psy_id = ?, raison = ?, date = ?, time = ?, status = ?, excuse = ? WHERE id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, c.getPsy().getId());
        ps.setString(2, c.getRaison());
        ps.setString(3, c.getDate());
        ps.setString(4, c.getTime());
        ps.setString(5, c.getStatus());
        ps.setString(6, c.getExcuse());
        ps.setInt(7, c.getId());
        ps.executeUpdate();
    }

    @Override
    public List<Consultation> getList() throws SQLException {
        List<Consultation> consultations = new ArrayList<>();
        String sql = "SELECT * FROM consultation";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        ServicePsy servicePsy = new ServicePsy(); // pour récupérer les psy

        while (resultSet.next()) {
            Consultation c = new Consultation();
            c.setId(resultSet.getInt("id"));
            c.setDate(resultSet.getString("date"));
            c.setTime(resultSet.getString("time"));
            c.setRaison(resultSet.getString("raison"));
            c.setStatus(resultSet.getString("status"));
            c.setExcuse(resultSet.getString("excuse"));

            int psyId = resultSet.getInt("psy_id");
            Psy psy = servicePsy.getById(psyId);
            if (psy == null) {
                System.out.println("⚠️ Consultation sans psy (id = " + c.getId() + ")");
            } else {
                c.setPsy(psy);
            }

            consultations.add(c);
        }

        return consultations;
    }
}
