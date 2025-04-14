package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.event.ActionEvent;

import org.example.entities.User;
import org.example.controllers.UserController;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField motDePasseField;

    @FXML
    private TextField nomField;

    @FXML
    private TextField prenomField;

    @FXML
    private TextField numeroTelField;

    @FXML
    void initialize() {
        // Initialisation des éléments
    }

    @FXML
    void handleEnvoyer(ActionEvent event) {
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String phoneText = numeroTelField.getText();
        String email = emailField.getText();
        String password = motDePasseField.getText();

        // Validation des champs vides
        if (nom == null || nom.trim().isEmpty() ||
                prenom == null || prenom.trim().isEmpty() ||
                phoneText == null || phoneText.trim().isEmpty() ||
                email == null || email.trim().isEmpty() ||
                password == null || password.trim().isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs.");
            return;
        }

        // Validation du format de l'email
        if (!isValidEmail(email)) {
            showAlert("Erreur", "L'adresse email n'est pas valide.");
            return;
        }

        // Validation du mot de passe
        if (!isValidPassword(password)) {
            showAlert("Erreur", "Le mot de passe doit contenir au moins 6 caractères, un caractère spécial et ne pas contenir d'espaces.");
            return;
        }

        // Validation du numéro de téléphone
        try {
            long phone = Long.parseLong(phoneText);

            // Création de l'objet User
            User user = new User(
                    0,              // id (auto-incrémenté par la BDD)
                    nom,
                    prenom,
                    email,
                    password,
                    phone,
                    "user",         // rôle par défaut
                    "active",       // statut par défaut
                    null,           // reset_token
                    null            // url_image
            );

            // Ajout à la base de données
            UserController userController = new UserController();
            userController.addUser(user);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText(null);
            alert.setContentText("Utilisateur enregistré avec succès !");
            alert.showAndWait();

        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le numéro de téléphone est invalide.");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de l'enregistrement de l'utilisateur.");
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isValidPassword(String password) {
        if (password.length() < 6 || password.contains(" ")) {
            return false;
        }
        String specialCharRegex = ".*[!@#$%^&*(),.?\":{}|<>].*";
        Pattern pattern = Pattern.compile(specialCharRegex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
