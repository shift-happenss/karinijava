package tn.esprit.services;

import tn.esprit.entities.Personne;
import tn.esprit.utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServicePersonne implements IService<Personne> {

    private Connection connection;

    public ServicePersonne(){
        connection = MyDataBase.getInstance().getMyConnection();
    }
    @Override
    public void ajouter(Personne personne) throws SQLException {
        String sql = "INSERT INTO `personne`(`nom`, `prenom`, `age`) VALUES ('"+personne.getNom()+"','"+personne.getPrenom()+"',"+personne.getAge()+")";
        Statement stm = connection.createStatement();
        stm.executeUpdate(sql);
    }

    @Override
    public void modifier(Personne personne) throws SQLException {

        String sql = "UPDATE `personne` SET `nom`=? ,`prenom`=?,`age`=? WHERE id = ?";
        PreparedStatement pst = connection.prepareStatement(sql);
        pst.setString(1, personne.getNom());
        pst.setString(2, personne.getPrenom());
        pst.setInt(3,personne.getAge());
        pst.setInt(4,personne.getId());
        pst.executeUpdate();

    }

    @Override
    public void supprimer(int id) throws SQLException {

        String sql = "DELETE FROM `personne` WHERE id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1,id);
        ps.executeUpdate();

    }

    @Override
    public List<Personne> afficher() throws SQLException {
        List<Personne> personnes = new ArrayList<>();
        String sql = "Select * from personne";
        Statement statement = connection.createStatement();
        ResultSet rs =statement.executeQuery(sql);
        while (rs.next()){
            Personne p = new Personne();
            p.setId(rs.getInt("id"));
            p.setNom(rs.getString(2));
            p.setPrenom(rs.getString(3));
            p.setAge(rs.getInt("age"));

            personnes.add(p);
        }
        return personnes;
    }
}
