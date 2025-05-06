package org.example.services;

import org.example.entities.Psy;
import org.example.utils.MyDataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServicePsy implements IService<Psy>{

    Connection connection;

    public ServicePsy() {
        connection = MyDataBase.getInstance().getMyConnection();
    }

    @Override
    public void ajouter(Psy psy) throws SQLException {
        String sql= "INSERT INTO `psy`(`nom`, `numerotel`, `mail`, `specialite`, `datedispo`, `timedispo`) VALUES (?, ?, ?, ?, ?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, psy.getNom());
        preparedStatement.setInt(2, psy.getNumerotel());
        preparedStatement.setString(3, psy.getMail());
        preparedStatement.setString(4, psy.getSpecialite());
        preparedStatement.setString(5, psy.getDatedispo());
        preparedStatement.setString(6, psy.getTimedispo());
        preparedStatement.executeUpdate();

    }

    /*@Override
    public void supprimer(int id) throws SQLException {
        String sql= "DELETE FROM `psy` WHERE `id` = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();

    }*/
    @Override
    public void supprimer(int id) throws SQLException {
        // Supprimer d'abord toutes les consultations du psy
        String sqlDeleteConsult = "DELETE FROM consultation WHERE psy_id = ?";
        PreparedStatement psConsult = connection.prepareStatement(sqlDeleteConsult);
        psConsult.setInt(1, id);
        psConsult.executeUpdate();

        // Ensuite, supprimer le psy lui-même
        String sqlDeletePsy = "DELETE FROM psy WHERE id = ?";
        PreparedStatement psPsy = connection.prepareStatement(sqlDeletePsy);
        psPsy.setInt(1, id);
        psPsy.executeUpdate();
    }


    @Override
    public void modifier(Psy psy) throws SQLException {
        String sql="UPDATE `psy` SET `nom`=?,`numerotel`=?,`mail`=?,`specialite`=?,`datedispo`=?,`timedispo`=? WHERE id= ?"   ;
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, psy.getNom());
        preparedStatement.setInt(2, psy.getNumerotel());
        preparedStatement.setString(3, psy.getMail());
        preparedStatement.setString(4, psy.getSpecialite());
        preparedStatement.setString(5, psy.getDatedispo());
        preparedStatement.setString(6, psy.getTimedispo());
        preparedStatement.setInt(7, psy.getId());
        preparedStatement.executeUpdate();


    }

    @Override
    public List<Psy> getList() throws SQLException {
        List<Psy> psys = new ArrayList<>();
        String sql= "SELECT * FROM `psy`";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            Psy psy=new Psy();
            psy.setId(resultSet.getInt("id"));
            psy.setNom(resultSet.getString("nom"));
            psy.setNumerotel(resultSet.getInt("numerotel"));
            psy.setMail(resultSet.getString("mail"));
            psy.setSpecialite(resultSet.getString("specialite"));
            psy.setDatedispo(resultSet.getString("datedispo"));
            psy.setTimedispo(resultSet.getString("timedispo"));
            psys.add(psy);


        }
        return psys;
    }

    public Psy getById(int id) throws SQLException {
        String sql = "SELECT * FROM psy WHERE id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return new Psy(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getInt("numerotel"),
                    rs.getString("mail"),
                    rs.getString("specialite"),
                    rs.getString("datedispo"),
                    rs.getString("timedispo")
            );
        }
        return null;
    }

}
