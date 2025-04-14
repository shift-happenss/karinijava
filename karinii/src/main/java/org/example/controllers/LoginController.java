package org.example.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private void handleRegisterLink() {
        try {
            // Charge le fichier Register.fxml
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/register.fxml"));
            Parent registerRoot = fxmlLoader.load();

            // Affiche la nouvelle scène dans une nouvelle fenêtre
            Stage registerStage = new Stage();
            registerStage.setTitle("Inscription");
            registerStage.setScene(new Scene(registerRoot));
            registerStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
