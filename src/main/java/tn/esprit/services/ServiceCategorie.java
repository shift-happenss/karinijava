package tn.esprit.services;

import tn.esprit.entities.Categorie;
import tn.esprit.utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceCategorie implements IService<Categorie> {

    private Connection connection;

    public ServiceCategorie() {
        connection = MyDataBase.getInstance().getMyConnection();
    }

    @Override
    public void ajouter(Categorie categorie) throws SQLException {
        String sql = "INSERT INTO categorie (name, description) VALUES (?, ?)";
        PreparedStatement pst = connection.prepareStatement(sql);
        pst.setString(1, categorie.getName());
        pst.setString(2, categorie.getDescription());
        pst.executeUpdate();
    }

    @Override
    public void modifier(Categorie categorie) throws SQLException {
        String sql = "UPDATE categorie SET name = ?, description = ? WHERE id = ?";
        PreparedStatement pst = connection.prepareStatement(sql);
        pst.setString(1, categorie.getName());
        pst.setString(2, categorie.getDescription());
        pst.setInt(3, categorie.getId());
        pst.executeUpdate();
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM categorie WHERE id = ?";
        PreparedStatement pst = connection.prepareStatement(sql);
        pst.setInt(1, id);
        pst.executeUpdate();
    }

    @Override
    public List<Categorie> afficher() throws SQLException {
        List<Categorie> categories = new ArrayList<>();
        String sql = "SELECT * FROM categorie";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);

        while (rs.next()) {
            Categorie c = new Categorie();
            c.setId(rs.getInt("id"));
            c.setName(rs.getString("name"));
            c.setDescription(rs.getString("description"));
            categories.add(c);
        }

        return categories;
    }

    public List<Categorie> getAll() {
        List<Categorie> categories = new ArrayList<>();

        String query = "SELECT * FROM categorie";
        try {
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery(query);

            while (rs.next()) {
                Categorie c = new Categorie();
                c.setId(rs.getInt("id"));
                c.setName(rs.getString("name"));
                c.setDescription(rs.getString("description"));
                categories.add(c);
            }

        } catch (Exception e) {
            System.out.println("Erreur lors de la récupération des catégories : " + e.getMessage());
        }

        return categories;
    }
    }

