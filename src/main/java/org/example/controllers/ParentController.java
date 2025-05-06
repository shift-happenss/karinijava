package org.example.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class ParentController {

    @FXML
    private Button logoutButton;

    @FXML
    private void logout(ActionEvent event) {
        try {
            // Charger la page de login
            Parent loginPage = FXMLLoader.load(getClass().getResource("/org/example/views/login.fxml"));

            // Obtenir la scène actuelle via le bouton logout
            Stage stage = (Stage) logoutButton.getScene().getWindow();

            // Remplacer la scène actuelle par la page de login
            stage.setScene(new Scene(loginPage));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            // Affiche une erreur si besoin
        }
    }
}
