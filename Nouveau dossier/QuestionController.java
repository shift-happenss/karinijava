package controllers;

import entities.Examen;
import entities.Question;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import services.ExamenService;
import services.QuestionService;

public class QuestionController {

    @FXML private TextField txtTexte, txtType;
    @FXML private ComboBox<Examen> comboExamen; // ComboBox pour sélectionner l'examen
    @FXML private TableView<Question> tableView;
    @FXML private TableColumn<Question, Integer> idColumn;
    @FXML private TableColumn<Question, Integer> examenIdColumn;
    @FXML private TableColumn<Question, String> texteColumn;
    @FXML private TableColumn<Question, String> typeColumn;

    private final QuestionService questionService = new QuestionService();
    private final ExamenService examenService = new ExamenService();
    private ObservableList<Question> questionList;

    @FXML
    public void initialize() {
        // Initialiser les colonnes de la table
        idColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleIntegerProperty(cell.getValue().getId()).asObject());
        examenIdColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleIntegerProperty(cell.getValue().getExamenId()).asObject());
        texteColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getTexte()));
        typeColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getType()));

        // Charger les questions dans la table
        loadQuestions();

        // Charger les examens dans le ComboBox
        loadExamens();
    }

    private void loadQuestions() {
        try {
            questionList = FXCollections.observableArrayList(questionService.afficher());
            tableView.setItems(questionList);
        } catch (Exception e) {
            showError("Erreur lors du chargement des questions.");
        }
    }

    private void loadExamens() {
        // Récupérer la liste des examens
        ObservableList<Examen> examensList = FXCollections.observableArrayList(examenService.listerExamens());
        if (examensList.isEmpty()) {
            showError("Aucun examen disponible.");
        } else {
            comboExamen.setItems(examensList);
        }
    }

    @FXML
    public void ajouterQuestion() {
        String texte = txtTexte.getText().trim();
        String type = txtType.getText().trim();
        Examen examenSelectionne = comboExamen.getValue();

        // Validation des champs
        if (examenSelectionne == null) {
            showError("Veuillez sélectionner un examen.");
            return;
        }
        if (texte.isEmpty() || type.isEmpty()) {
            showError("Le texte et le type de la question doivent être remplis.");
            return;
        }

        // Créer la question et l'ajouter
        Question q = new Question(examenSelectionne.getId(), texte, type);
        questionService.ajouter(q);
        loadQuestions();

        // Réinitialiser les champs
        txtTexte.clear();
        txtType.clear();
        comboExamen.getSelectionModel().clearSelection();

        // Message de succès
        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle("Succès");
        successAlert.setHeaderText(null);
        successAlert.setContentText("La question a été ajoutée avec succès.");
        successAlert.showAndWait();
    }

    @FXML
    public void modifierQuestion() {
        Question selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            String texte = txtTexte.getText().trim();
            String type = txtType.getText().trim();
            Examen examenSelectionne = comboExamen.getValue();

            // Validation des champs
            if (examenSelectionne == null) {
                showError("Veuillez sélectionner un examen.");
                return;
            }
            if (texte.isEmpty() || type.isEmpty()) {
                showError("Le texte et le type de la question doivent être remplis.");
                return;
            }

            // Mettre à jour la question
            selected.setExamenId(examenSelectionne.getId());
            selected.setTexte(texte);
            selected.setType(type);
            questionService.modifier(selected);
            loadQuestions();

            // Réinitialiser les champs
            txtTexte.clear();
            txtType.clear();
            comboExamen.getSelectionModel().clearSelection();

            // Message de succès
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Succès");
            successAlert.setHeaderText(null);
            successAlert.setContentText("La question a été modifiée avec succès.");
            successAlert.showAndWait();
        } else {
            showError("Veuillez sélectionner une question à modifier.");
        }
    }

    @FXML
    public void supprimerQuestion() {
        Question selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            questionService.supprimer(selected.getId());
            loadQuestions();

            // Message de succès
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Succès");
            successAlert.setHeaderText(null);
            successAlert.setContentText("La question a été supprimée avec succès.");
            successAlert.showAndWait();
        } else {
            showError("Veuillez sélectionner une question à supprimer.");
        }
    }

    // Méthode pour afficher un message d'erreur
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
