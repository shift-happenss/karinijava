package org.example.controllers.houss;

import org.example.entities.Examen;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.services.ExamenService;

import java.io.IOException;
import java.util.List;

public class ExamenEtudiantController {

    @FXML
    private GridPane examenContainer;

    private ExamenService examenService = new ExamenService();

    @FXML
    public void initialize() {
        if (examenContainer != null) {
            afficherExamensPourEtudiants();
        }
    }

    public void afficherExamensPourEtudiants() {
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

            Button passerExamenBtn = new Button("Passer l'examen");
            passerExamenBtn.setStyle("-fx-background-color: #c33764; -fx-text-fill: white; -fx-font-weight: bold;");
            passerExamenBtn.setOnAction(event -> passerExamen(e));

            card.getChildren().addAll(titre, desc, cours, note, passerExamenBtn);
            examenContainer.add(card, col, row);

            col++;
            if (col > 2) {
                col = 0;
                row++;
            }
        }
    }

    private void passerExamen(Examen examen) {
        try {
            // Charger la scène du quiz pour cet examen spécifique
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/houss/quiz_interface.fxml")); // Chemin correct ici
            VBox quizView = loader.load();

            // Passer l'examen sélectionné à l'interface du quiz (assurez-vous que l'interface Quiz reçoit l'examen)
            QuizController quizController = loader.getController();
            quizController.setExamen(examen);

            Scene scene = new Scene(quizView);
            Stage stage = new Stage();
            stage.setTitle("Passer l'Examen : " + examen.getTitre());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
