package org.example.controllers;


import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;

public class SidebarController {

    public void goToDash(ActionEvent event) { loadScene("/admin.fxml", event); }

    public void goToGestionUser(ActionEvent event) { loadScene("/gestion_user.fxml", event); }

    public void afficherCategories(ActionEvent event) { loadScene("/Categorie/ListeCategories.fxml", event); }

    public void afficherFormations(ActionEvent event) { loadScene("/Formation/displayFormations.fxml", event); }

    public void afficherEmploi(ActionEvent event) { loadScene("/Emploi/AfficherEmploi.fxml", event); }

    public void afficherrpsys(ActionEvent event) { loadScene("/AjouterPsy.fxml", event); }

    public void afficherrcons(ActionEvent event) { loadScene("/AjouterConsultation.fxml", event); }

    public void afficherrexam(ActionEvent event) { loadScene("/houss/examen_interface.fxml", event); }

    public void afficherrcours(ActionEvent event) { loadScene("/houss/cours_interface.fxml", event); }

    public void afficherrcourse(ActionEvent event) { loadScene("/houss/interface_cours_etudiant.fxml", event); }

    public void afficherrblogfront(ActionEvent event) { loadScene("/blog/AfficherRessource.fxml", event); }

    public void afficherrblogback(ActionEvent event) { loadScene("/blog/AfficherRessourceBack.fxml", event); }

    public void handleLogout(ActionEvent event) { loadScene("/login.fxml", event); }

    private void loadScene(String fxmlPath, ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

            // Fermer la fenêtre actuelle
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
