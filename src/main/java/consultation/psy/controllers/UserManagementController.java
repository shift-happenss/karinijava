package consultation.psy.controllers;

import consultation.psy.entities.User;
import consultation.psy.services.ServiceUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.SQLException;

public class UserManagementController {

    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private ListView<User> userListView;

    private ServiceUser serviceUser;
    private ObservableList<User> userList;

    @FXML
    public void initialize() {
        serviceUser = new ServiceUser();
        userList = FXCollections.observableArrayList();
        loadUsers();

        // Détection du double-clic sur un utilisateur
        userListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {  // Vérifie si c'est un double-clic
                User selectedUser = userListView.getSelectionModel().getSelectedItem();
                if (selectedUser != null) {
                    openEditWindow(selectedUser); // Ouvre la fenêtre de modification
                }
            }
        });
    }

    // Charge tous les utilisateurs depuis la base de données
    private void loadUsers() {
        try {
            userList.clear();
            userList.addAll(serviceUser.getAllUsers());
            userListView.setItems(userList);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error loading users").show();
        }
    }

    // Ouvre une fenêtre pour modifier un utilisateur
    private void openEditWindow(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierUser.fxml")); // Le FXML pour la fenêtre de modification
            Parent root = loader.load();

            // Passer l'utilisateur sélectionné à la fenêtre de modification
            //ModifierUserController controller = loader.getController();
          //  controller.setUser(user);

            Stage stage = new Stage();
            stage.setTitle("Modifier l'utilisateur");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            // Recharge la liste après modification
            loadUsers();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error opening edit window").show();
        }
    }

    // Ajouter un nouvel utilisateur
    @FXML
    private void addUser() {
        String name = nameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "All fields must be filled!").show();
            return;
        }

        try {
            User newUser = new User(name, email, password); // ID sera généré automatiquement par la base de données
            serviceUser.addUser(newUser);
            loadUsers();
            clearFields();
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error adding user").show();
        }
    }

    // Met à jour un utilisateur sélectionné
    @FXML
    private void updateUser() {
        User selectedUser = userListView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            selectedUser.setName(nameField.getText());
            selectedUser.setEmail(emailField.getText());
            selectedUser.setPassword(passwordField.getText());

            try {
                serviceUser.updateUser(selectedUser);
                loadUsers();
                clearFields();
            } catch (SQLException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Error updating user").show();
            }
        } else {
            new Alert(Alert.AlertType.WARNING, "No user selected").show();
        }
    }

    // Supprime un utilisateur sélectionné
    @FXML
    private void deleteUser() {
        User selectedUser = userListView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            try {
                serviceUser.deleteUser(selectedUser.getId());
                loadUsers();
                clearFields();
            } catch (SQLException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Error deleting user").show();
            }
        } else {
            new Alert(Alert.AlertType.WARNING, "No user selected").show();
        }
    }

    // Efface les champs de saisie
    private void clearFields() {
        nameField.clear();
        emailField.clear();
        passwordField.clear();
    }
}
