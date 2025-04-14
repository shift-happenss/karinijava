package controllers;

import entities.Examen;
import entities.Question;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import services.QuestionService;

import java.io.IOException;
import java.util.List;

public class QuestionController {

    @FXML
    private VBox questionContainer;

    @FXML
    private Label labelExamenTitre;

    private Examen examen;

    private final QuestionService questionService = new QuestionService();

    public void setExamen(Examen examen) {
        this.examen = examen;
        labelExamenTitre.setText("Examen : " + examen.getTitre());
        afficherQuestions();  // Rafraîchir la liste des questions
    }

    // Méthode pour afficher toutes les questions de l'examen
    public void afficherQuestions() {
        questionContainer.getChildren().clear();  // Vider la liste des questions avant de la réafficher

        if (examen == null) {
            System.out.println("Aucun examen défini.");
            return;
        }

        List<Question> questions = questionService.getQuestionsByExamenId(examen.getId());

        // Parcourir et afficher chaque question sous forme de carte
        for (Question question : questions) {
            HBox card = new HBox(10);
            card.setStyle("-fx-background-color: #ffffff; -fx-padding: 15; -fx-border-color: #ddd; -fx-border-radius: 10; -fx-alignment: center-left; -fx-effect: dropshadow(gaussian, #7f8c8d, 5, 0, 2, 2);");
            card.setPrefHeight(100.0);

            // Label de texte de la question
            Label texteLabel = new Label("🗨️ " + question.getTexte() + " — 📘 Type : " + question.getType());
            texteLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #333; -fx-font-weight: bold;");
            HBox.setHgrow(texteLabel, Priority.ALWAYS);

            // Bouton Modifier
            Button modifierBtn = new Button("✏️ Modifier");
            modifierBtn.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white; -fx-border-radius: 8; -fx-padding: 10 20; -fx-font-size: 14px; -fx-font-weight: bold; -fx-effect: dropshadow(gaussian, #f39c12, 5, 0, 0, 2);");
            modifierBtn.setOnAction(e -> ouvrirModification(question));

            // Bouton Supprimer
            Button supprimerBtn = new Button("🗑️ Supprimer");
            supprimerBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-border-radius: 8; -fx-padding: 10 20; -fx-font-size: 14px; -fx-font-weight: bold; -fx-effect: dropshadow(gaussian, #e74c3c, 5, 0, 0, 2);");
            supprimerBtn.setOnAction(e -> {
                questionService.supprimer(question.getId());
                afficherQuestions();  // Rafraîchir la liste des questions après suppression
            });

            // Bouton Voir Réponses
            Button voirReponsesBtn = new Button("👁️ Voir Réponses");
            voirReponsesBtn.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white; -fx-border-radius: 8; -fx-padding: 10 20; -fx-font-size: 14px; -fx-font-weight: bold; -fx-effect: dropshadow(gaussian, #2980b9, 5, 0, 0, 2);");
            voirReponsesBtn.setOnAction(e -> voirReponses(question));  // Action pour voir les réponses associées à cette question

            HBox buttons = new HBox(10, modifierBtn, supprimerBtn, voirReponsesBtn);  // Ajouter le bouton à la ligne des autres boutons
            buttons.setStyle("-fx-alignment: center-right;"); // Aligner les boutons à droite
            card.getChildren().addAll(texteLabel, buttons);
            questionContainer.getChildren().add(card);
        }
    }

    @FXML
    private void ouvrirAjout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ajouter_question.fxml"));
            Parent root = loader.load();

            AjouterQuestionController controller = loader.getController();
            controller.setExamen(examen);  // ✅ Examen à pré-sélectionner
            controller.setOnQuestionAdded(this::afficherQuestions);  // ✅ Callback pour rafraîchir la liste

            Stage stage = new Stage();
            stage.setTitle("Ajouter une Question");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour ouvrir la fenêtre de modification d'une question
    private void ouvrirModification(Question question) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/modifier_question.fxml"));
            Parent root = loader.load();

            ModifierQuestionController controller = loader.getController();
            controller.setQuestion(question);
            controller.setOnQuestionModified(this::afficherQuestions);  // Passer le callback pour mise à jour après modification

            Stage stage = new Stage();
            stage.setTitle("Modifier la Question");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour voir les réponses associées à la question
    @FXML
    private void voirReponses(Question question) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/reponses_interface.fxml"));
            Parent root = loader.load();

            // Passer l'objet Question directement à ReponseController
            ReponseController controller = loader.getController();
            controller.setQuestion(question);  // Passer la question entière

            Stage stage = new Stage();
            stage.setTitle("Réponses associées à la question");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
