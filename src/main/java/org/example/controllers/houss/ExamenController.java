package org.example.controllers.houss;

import org.example.entities.Examen;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.services.CoursService;
import org.example.services.ExamenService;

import java.io.IOException;
import java.util.List;

public class ExamenController {

    @FXML
    private GridPane examenContainer;

    private ExamenService examenService;
    private CoursService coursService;

    private Examen examen;  // Instance variable for 'examen'

    public ExamenController() {
        examenService = new ExamenService();
        coursService = new CoursService();
    }

    // Set the examen for the current controller
    public void setExamen(Examen examen) {
        this.examen = examen;
    }

    // Initialize method to populate the exam list upon loading
    @FXML
    public void initialize() {
        if (examenContainer != null) {
            afficherExamens();
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le conteneur d'examen n'a pas pu être initialisé.");
        }
    }

    // Display all exams in the container
    public void afficherExamens() {
        List<Examen> examenList = examenService.afficher();
        examenContainer.getChildren().clear();

        int row = 0;
        int col = 0;

        for (Examen e : examenList) {
            VBox card = new VBox();
            card.setSpacing(10);
            card.setStyle("-fx-background-color: #1e3c72; -fx-background-radius: 15; -fx-padding: 20; -fx-min-width: 280px; -fx-max-width: 280px; -fx-effect: dropshadow(gaussian, black, 8, 0, 0, 4);");

            Label titre = new Label("📝 Titre : " + e.getTitre());
            titre.setStyle("-fx-font-size: 16px; -fx-text-fill: white; -fx-font-weight: bold;");

            Label desc = new Label("📋 Description : " + e.getDescription());
            desc.setStyle("-fx-text-fill: white;");

            Label cours = new Label("📚 Cours : " + (e.getCours() != null ? e.getCours().getTitre() : "Aucun"));
            cours.setStyle("-fx-text-fill: white;");

            Label note = new Label("⭐ Note : " + e.getNote());
            note.setStyle("-fx-text-fill: white;");

            Button btnModif = new Button("✏️ Modifier");
            btnModif.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-background-radius: 10;");
            btnModif.setOnAction(event -> modifierExamen(e));

            Button btnSuppr = new Button("🗑️ Supprimer");
            btnSuppr.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white; -fx-background-radius: 10;");
            btnSuppr.setOnAction(event -> supprimerExamen(e));

            Button btnVoirQuestions = new Button("❓ Voir Questions");
            btnVoirQuestions.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-background-radius: 10;");
            btnVoirQuestions.setOnAction(event -> ouvrirQuestionsParExamen(e.getId()));

            VBox btnBox = new VBox(10, btnModif, btnSuppr, btnVoirQuestions);
            card.getChildren().addAll(titre, desc, cours, note, btnBox);

            examenContainer.add(card, col, row);

            col++;
            if (col > 2) {
                col = 0;
                row++;
            }
        }
    }

    // Open the "Add Examen" popup window
    public void openAddExamenPopup() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/houss/addExamen_interface.fxml"));
            Parent root = loader.load();

            AddExamenController addExamenController = loader.getController();
            addExamenController.setExamenController(this); // Pass the main controller reference

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter un examen");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir la fenêtre d'ajout.");
        }
    }

    // Open the "Modify Examen" window
    private void modifierExamen(Examen examen) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/houss/modifierExamen.fxml"));
            Parent root = loader.load();

            ModifierExamenController controller = loader.getController();
            controller.setExamen(examen, this);

            Stage stage = new Stage();
            stage.setTitle("Modifier l'examen");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir la fenêtre de modification: " + e.getMessage());
        }
    }

    // Method to delete an examen
    @FXML
    private void supprimerExamen(Examen examen) {
        if (examenService.supprimer(examen.getId())) {
            afficherExamens();
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Examen supprimé.");
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de la suppression.");
        }
    }

    // Utility function to show alerts
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Open the "Questions" section related to an examen
    private void ouvrirQuestionsParExamen(int examenId) {
        try {
            Examen examen = examenService.trouverParId(examenId);

            if (examen == null) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Examen introuvable.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/houss/question_interface.fxml"));
            Parent root = loader.load();

            QuestionController controller = loader.getController();
            controller.setExamen(examen);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Questions de l'examen");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir les questions.");
        }
    }
}
