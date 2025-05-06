package org.example.controllers.houss;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class SidebarEtudiantController {

    private void loadSceneWithSidebar(String centerFxmlPath, ActionEvent event) {
        try {
            // Charger le contenu central (interface cours, examens, etc.)
            Parent centerContent = FXMLLoader.load(getClass().getResource(centerFxmlPath));

            // Charger la sidebar à gauche
            Parent sidebar = FXMLLoader.load(getClass().getResource("/houss/sidebar_etudiant.fxml"));

            // Créer un BorderPane pour structurer la page
            BorderPane rootLayout = new BorderPane();
            rootLayout.setLeft(sidebar);         // Sidebar à gauche
            rootLayout.setCenter(centerContent); // Contenu au centre
            rootLayout.setRight(null);           // Rien à droite
            rootLayout.setTop(null);             // Rien en haut
            rootLayout.setBottom(null);          // Rien en bas

            // Créer la scène
            Scene scene = new Scene(rootLayout, 1200, 800);

            // Appliquer la nouvelle scène à la fenêtre actuelle
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de : " + centerFxmlPath);
            e.printStackTrace();
        }
    }

    // Appels spécifiques
    public void goToMesCours(ActionEvent event) {
        loadSceneWithSidebar("/houss/interface_cours_etudiant.fxml", event);
    }

    public void goToMesExamens(ActionEvent event) {
        loadSceneWithSidebar("/houss/examen_etudiant_interface.fxml", event);
    }
}
