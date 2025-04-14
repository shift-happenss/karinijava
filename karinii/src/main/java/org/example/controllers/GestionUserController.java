package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import org.example.entities.User;
import org.example.controllers.UserController;

import java.sql.SQLException;

public class GestionUserController {

    @FXML private ListView<User> userListView;
    @FXML private TextField tfNom, tfPrenom, tfEmail, tfNumTel, tfRole;

    private ObservableList<User> userList;
    private UserController userController = new UserController();  // Correction ici pour UserController
    private User selectedUser;

    @FXML
    public void initialize() {
        // Chargement initial des utilisateurs
        refreshListView();

        // Sélection d'un utilisateur dans la ListView pour modification
        userListView.setOnMouseClicked(event -> onUserSelect(event));
    }

    @FXML
    private void refreshListView() {
        try {
            userList = FXCollections.observableArrayList(userController.getAllUsers());
            userListView.setItems(userList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onUserSelect(MouseEvent event) {
        selectedUser = userListView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            tfNom.setText(selectedUser.getName());
            tfPrenom.setText(selectedUser.getPrenom());
            tfEmail.setText(selectedUser.getEmail());
            tfNumTel.setText(String.valueOf(selectedUser.getNumtel()));
            tfRole.setText(selectedUser.getRole());
        }
    }

    // Modifier l'utilisateur sélectionné
    @FXML
    void onUpdate() {
        if (selectedUser != null) {
            selectedUser.setName(tfNom.getText());
            selectedUser.setPrenom(tfPrenom.getText());
            selectedUser.setEmail(tfEmail.getText());
            selectedUser.setNumtel(Long.parseLong(tfNumTel.getText()));
            selectedUser.setRole(tfRole.getText());

            try {
                userController.editUser(selectedUser);
                refreshListView();
                clearFields();
                showAlert("Utilisateur mis à jour avec succès !");
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Erreur lors de la mise à jour.");
            }
        }
    }

    // Supprimer l'utilisateur sélectionné
    @FXML
    void onDelete() {
        if (selectedUser != null) {
            try {
                userController.deleteUser(selectedUser.getId()); // Suppression de l'utilisateur dans la base de données
                refreshListView(); // Actualisation de la liste après suppression
                clearFields(); // Réinitialisation des champs
                showAlert("Utilisateur supprimé avec succès !");
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Erreur lors de la suppression.");
            }
        } else {
            showAlert("Veuillez sélectionner un utilisateur à supprimer.");
        }
    }

    // Effacer les champs de saisie
    private void clearFields() {
        tfNom.clear();
        tfPrenom.clear();
        tfEmail.clear();
        tfNumTel.clear();
        tfRole.clear();
        selectedUser = null;
    }

    // Afficher une alerte
    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
