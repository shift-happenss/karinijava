package org.example.services;

import org.example.entities.User;
import org.example.utils.MyDataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Authservice {

    public static User authenticate(int id, String password) {
        String sql = "SELECT * FROM user WHERE id = ? AND password = ?";

        try (Connection conn = MyDataBase.getInstance().getMyConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setPrenom(rs.getString("prenom"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setNumtel(rs.getLong("numtel"));
                user.setRole(rs.getString("role"));
                user.setStatus(rs.getString("status"));


                return user;
            }

        } catch (Exception e) {
            e.printStackTrace(); // Tu peux le logger proprement plus tard
        }

        return null;
    }
}
