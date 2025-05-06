package org.example.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.example.entities.User;
import org.example.services.Authservice;
import org.example.utils.SessionManager;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField idField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void handleLoginButton(ActionEvent event) {
        String idInput = idField.getText();
        String password = passwordField.getText();

        // Cas spécial : Utilisateur admin fictif
        if ("admin".equalsIgnoreCase(idInput) && "admin".equals(password)) {
            User adminUser = new User(
                    0,                      // ID fictif
                    "Admin",                // nom
                    "Super",                // prénom
                    "admin@karini.com",     // email fictif
                    "admin",                // mot de passe
                    0L,                     // numéro fictif
                    "admin",                // rôle
                    "active",               // statut
                    null,                   // resetToken
                    null                    // urlImage
            );
            SessionManager.setCurrentUser(adminUser);
            try {
                loadScene("/admin.fxml");
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Impossible de charger le tableau de bord admin.");
            }
            return;
        }

        try {
            int id = Integer.parseInt(idInput);
            String pwd = password;

            User authenticatedUser = Authservice.authenticate(id, pwd);

            if (authenticatedUser != null) {
                if (!"active".equalsIgnoreCase(authenticatedUser.getStatus())) {
                    showAlert("Votre compte est inactif. Veuillez contacter l'administrateur.");
                    return;
                }

                SessionManager.setCurrentUser(authenticatedUser);

                switch (authenticatedUser.getRole().toLowerCase()) {
                    case "parent":
                        loadScene("/parent.fxml");
                        break;
                    case "teacher":
                        loadScene("/teacher.fxml");
                        break;
                    case "student":
                        loadScene("/student.fxml");
                        break;
                    default:
                        showAlert("Rôle inconnu : " + authenticatedUser.getRole());
                }

            } else {
                showAlert("ID ou mot de passe incorrect.");
            }

        } catch (NumberFormatException e) {
            showAlert("Veuillez entrer un ID valide (nombre) ou utilisez 'admin'.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur lors de la connexion.");
        }
    }

    @FXML
    private void handleRegisterLink() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/register.fxml"));
            Parent registerRoot = fxmlLoader.load();

            Stage registerStage = new Stage();
            registerStage.setTitle("Inscription");
            registerStage.setScene(new Scene(registerRoot));
            registerStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadScene(String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Dashboard");
        stage.show();

        // Fermer la fenêtre de login
        idField.getScene().getWindow().hide();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur de connexion");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
