package consultation.psy.services;

import consultation.psy.entities.Psy;
import consultation.psy.entities.User;
import consultation.psy.utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceUser {

    private Connection connection;

    public ServiceUser() {
        connection = MyDataBase.getInstance().getMyConnection();
    }


    public void addUser(User user) throws SQLException {
        String query = "INSERT INTO users (name, email, password) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.executeUpdate();
        }
    }

    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users";

        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);

            // Vérification si la table contient des données
            if (!rs.next()) {
                System.out.println("Aucun utilisateur trouvé");
            }

            // Une seule boucle while pour itérer sur les résultats
            do {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));

                // Ajouter l'utilisateur à la liste des utilisateurs
                users.add(user);

                // Affichage des utilisateurs dans la console
                System.out.println(user.getName() + " | " + user.getEmail());
            } while (rs.next());
        }

        return users;
    }



    public void updateUser(User user) throws SQLException {
        String query = "UPDATE users SET name = ?, email = ?, password = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setInt(4, user.getId());
            stmt.executeUpdate();
        }
    }

    public void deleteUser(int id) throws SQLException {
        String query = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
