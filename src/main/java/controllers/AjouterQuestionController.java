package controllers;

import entities.Examen;
import entities.Question;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.ExamenService;
import services.QuestionService;

public class AjouterQuestionController {

    @FXML
    private TextField texteField;

    @FXML
    private ComboBox<String> typeCombo;

    @FXML
    private ComboBox<Examen> examenCombo;

    private final QuestionService questionService = new QuestionService();
    private final ExamenService examenService = new ExamenService();

    @FXML
    public void initialize() {
        // Charger les examens dans le ComboBox
        ObservableList<Examen> examenList = FXCollections.observableArrayList(examenService.afficher());
        examenCombo.setItems(examenList);

        // Ajouter les types de question
        ObservableList<String> typesList = FXCollections.observableArrayList("Choix Multiple", "Vrai/Faux", "Réponse ouverte");
        typeCombo.setItems(typesList);
    }

    @FXML
    private void ajouterQuestion() {
        String texte = texteField.getText().trim();
        String type = typeCombo.getValue();
        Examen examenSelectionne = examenCombo.getValue();

        // Contrôles de saisie
        if (texte.isEmpty() || type == null || examenSelectionne == null) {
            showAlert(Alert.AlertType.ERROR, "Champs manquants", "Veuillez remplir tous les champs.");
            return;
        }

        if (texte.length() < 5) {
            showAlert(Alert.AlertType.ERROR, "Texte trop court", "Le texte de la question doit contenir au moins 5 caractères.");
            return;
        }

        // ✅ Création de la question
        Question nouvelleQuestion = new Question(examenSelectionne, texte, type);
        questionService.ajouterQuestion(nouvelleQuestion);

        showAlert(Alert.AlertType.INFORMATION, "Succès", "Question ajoutée avec succès !");

        // Callback pour mettre à jour la liste si nécessaire
        if (onQuestionAdded != null) {
            onQuestionAdded.run();
        }

        fermerFenetre();
    }

    @FXML
    private void fermerFenetre() {
        Stage stage = (Stage) texteField.getScene().getWindow();
        stage.close();
    }

    public void setExamen(Examen examen) {
        if (examenCombo != null) {
            examenCombo.getSelectionModel().select(examen);
        }
    }

    private Runnable onQuestionAdded;

    public void setOnQuestionAdded(Runnable callback) {
        this.onQuestionAdded = callback;
    }

    private void showAlert(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
