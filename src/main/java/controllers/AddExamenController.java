package controllers;

import entities.Cours;
import entities.Examen;
import services.CoursService;
import services.ExamenService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.List;

public class AddExamenController {

    @FXML private ComboBox<Cours> coursComboBox;
    @FXML private TextField titreField;
    @FXML private TextField descriptionField;
    @FXML private TextField noteField;

    private final ExamenService examenService = new ExamenService();
    private final CoursService coursService = new CoursService();
    private ExamenController examenController;

    @FXML
    public void initialize() {
        List<Cours> coursList = coursService.afficher();
        coursComboBox.getItems().addAll(coursList);
    }

    @FXML
    public void ajouterExamen() {
        Cours selectedCours = coursComboBox.getSelectionModel().getSelectedItem();
        String titre = titreField.getText().trim();
        String description = descriptionField.getText().trim();
        String noteText = noteField.getText().trim();

        // Validation des champs vides
        if (selectedCours == null || titre.isEmpty() || description.isEmpty() || noteText.isEmpty()) {
            showAlert("Erreur", "Tous les champs doivent être remplis et un cours doit être sélectionné.");
            return;
        }

        // Validation de la longueur du titre et de la description
        if (titre.length() < 3) {
            showAlert("Erreur", "Le titre doit contenir au moins 3 caractères.");
            return;
        }

        if (description.length() < 5) {
            showAlert("Erreur", "La description doit contenir au moins 5 caractères.");
            return;
        }

        // Validation du champ note
        double note;
        try {
            note = Double.parseDouble(noteText);
            if (note < 0) {
                showAlert("Erreur", "La note doit être un nombre positif.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Erreur", "La note doit être un nombre valide.");
            return;
        }

        // Création et ajout de l'examen
        Examen examen = new Examen(selectedCours, titre, description, note);
        examenService.ajouter(examen);

        // Mise à jour de l'affichage
        if (examenController != null) {
            examenController.afficherExamens();
        }

        // Fermer la fenêtre après ajout
        Stage stage = (Stage) titreField.getScene().getWindow();
        stage.close();
    }

    public void setExamenController(ExamenController examenController) {
        this.examenController = examenController;
    }

    // Méthode d'alerte
    private void showAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
