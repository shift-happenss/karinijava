package org.example.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HeaderController {

    public void goToHome(ActionEvent event) {
        loadScene(event, "/home.fxml");
    }

    public void goToLogin(ActionEvent event) {
        loadScene(event, "/login.fxml");
    }

    public void goToAbout(ActionEvent event) {
        loadScene(event, "/about.fxml");
    }

    public void goToFormations(ActionEvent event) {
        loadScene(event, "/formations.fxml");
    }

    public void goToBlogs(ActionEvent event) {
        loadScene(event, "/blogs.fxml");
    }

    public void goToHelp(ActionEvent event) {
        loadScene(event, "/help.fxml");
    }

    /**
     * Méthode utilitaire pour changer de scène.
     */
    private void loadScene(ActionEvent event, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de la scène : " + fxmlPath);
            e.printStackTrace();
        }
    }
}
