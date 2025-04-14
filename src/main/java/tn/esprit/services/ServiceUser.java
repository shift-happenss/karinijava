package tn.esprit.services;

import tn.esprit.entities.User;
import tn.esprit.utils.MyDataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServiceUser {

    private final Connection conn;

    public ServiceUser() throws SQLException {
        conn = MyDataBase.getInstance().getMyConnection();
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String req = "SELECT * FROM user";

        try {
            PreparedStatement pst = conn.prepareStatement(req);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                User u = new User();
                u.setId(rs.getInt("id"));
                u.setNom(rs.getString("nom"));
                users.add(u);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des utilisateurs : " + e.getMessage());
        }

        return users;
    }
}
