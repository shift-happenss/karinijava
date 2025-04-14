package org.example.controllers;

import org.example.entities.User;
import org.example.utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserController {

    private Connection connection;

    public UserController() {
        connection = MyDataBase.getInstance().getMyConnection();

    }

    public void addUser(User user) throws SQLException {
        String query = "INSERT INTO user (name, prenom, email, password, numtel, role, status, reset_token, url_image) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, user.getName());
        ps.setString(2, user.getPrenom());
        ps.setString(3, user.getEmail());
        ps.setString(4, user.getPassword());
        ps.setLong(5, user.getNumtel());
        ps.setString(6, user.getRole());
        ps.setString(7, user.getStatus());
        ps.setString(8, user.getResetToken());
        ps.setString(9, user.getUrlImage());

        ps.executeUpdate();
        System.out.println("✅ Utilisateur ajouté !");
    }

    public void editUser(User user) throws SQLException {
        String query = "UPDATE user SET name=?, prenom=?, email=?, password=?, numtel=?, role=?, status=?, reset_token=?, url_image=? WHERE id=?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, user.getName());
        ps.setString(2, user.getPrenom());
        ps.setString(3, user.getEmail());
        ps.setString(4, user.getPassword());
        ps.setLong(5, user.getNumtel());
        ps.setString(6, user.getRole());
        ps.setString(7, user.getStatus());
        ps.setString(8, user.getResetToken());
        ps.setString(9, user.getUrlImage());
        ps.setInt(10, user.getId());

        ps.executeUpdate();
        System.out.println("✏️ Utilisateur modifié !");
    }

    public void deleteUser(int id) throws SQLException {
        String query = "DELETE FROM user WHERE id=?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, id);
        ps.executeUpdate();
        System.out.println("🗑️ Utilisateur supprimé !");
    }

    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM user";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(query);

        while (rs.next()) {
            User user = new User(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("prenom"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getLong("numtel"),
                    rs.getString("role"),
                    rs.getString("status"),
                    rs.getString("reset_token"),
                    rs.getString("url_image")
            );
            users.add(user);
        }
        return users;
    }

    public User getUserById(int id) throws SQLException {
        String query = "SELECT * FROM user WHERE id=?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return new User(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("prenom"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getLong("numtel"),
                    rs.getString("role"),
                    rs.getString("status"),
                    rs.getString("reset_token"),
                    rs.getString("url_image")
            );
        }
        return null;
    }
}
