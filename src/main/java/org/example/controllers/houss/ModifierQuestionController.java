package org.example.controllers.houss;

import org.example.entities.Examen;
import org.example.entities.Question;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.services.ExamenService;
import org.example.services.QuestionService;

import java.util.List;

public class ModifierQuestionController {

    @FXML
    private TextField texteField;

    @FXML
    private ComboBox<String> typeCombo;

    @FXML
    private ComboBox<Examen> examenCombo;

    private Question question;
    private final QuestionService questionService = new QuestionService();
    private final ExamenService examenService = new ExamenService();

    private Runnable onQuestionModified; // callback

    public void setOnQuestionModified(Runnable onQuestionModified) {
        this.onQuestionModified = onQuestionModified;
    }

    public void setQuestion(Question question) {
        this.question = question;

        // Initialiser les champs avec les valeurs de la question
        texteField.setText(question.getTexte());
        typeCombo.setValue(question.getType());

        // Charger tous les examens et sélectionner celui de la question
        List<Examen> examens = examenService.afficher(); // Utilise afficher() à la place de getAll()
        examenCombo.getItems().addAll(examens);
        if (question.getExamen() != null) {
            examenCombo.setValue(question.getExamen());
        }
    }

    @FXML
    public void initialize() {
        typeCombo.getItems().addAll("Choix Multiple", "Vrai/Faux", "Texte");

        // Charger tous les examens dans le ComboBox
        List<Examen> examens = examenService.afficher(); // Utilise afficher() à la place de getAll()
        examenCombo.getItems().addAll(examens);
    }

    @FXML
    private void modifierQuestion() {
        String texte = texteField.getText().trim();
        String type = typeCombo.getValue();
        Examen examen = examenCombo.getValue();

        // Validation des champs
        if (texte.isEmpty()) {
            showAlert("Le texte de la question ne peut pas être vide.");
            return;
        }

        if (texte.length() < 10) {
            showAlert("Le texte de la question doit contenir au moins 10 caractères.");
            return;
        }

        if (type == null) {
            showAlert("Veuillez sélectionner un type pour la question.");
            return;
        }

        if (examen == null) {
            showAlert("Veuillez sélectionner un examen.");
            return;
        }

        // Mise à jour de la question
        question.setTexte(texte);
        question.setType(type);
        question.setExamen(examen);

        // Modifier la question dans la base de données
        questionService.modifier(question);

        // Appeler le callback pour indiquer que la question a été modifiée
        if (onQuestionModified != null) {
            onQuestionModified.run();
        }

        // Fermer la fenêtre
        fermerFenetre();
    }

    private void fermerFenetre() {
        Stage stage = (Stage) texteField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Champs manquants");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
