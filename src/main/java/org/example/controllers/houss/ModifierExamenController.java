package org.example.controllers.houss;

import org.example.entities.Cours;
import org.example.entities.Examen;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.services.CoursService;
import org.example.services.ExamenService;

public class ModifierExamenController {

    @FXML
    private TextField titreField;
    @FXML
    private TextField descriptionField;
    @FXML
    private ComboBox<Cours> coursComboBox;
    @FXML
    private Spinner<Double> noteSpinner;

    private Examen examen;
    private ExamenController examenController;
    private final ExamenService examenService = new ExamenService();
    private final CoursService coursService = new CoursService();

    @FXML
    public void initialize() {
        // Initialisation supplémentaire si besoin
    }

    public void setExamen(Examen examen, ExamenController controller) {
        this.examen = examen;
        this.examenController = controller;

        titreField.setText(examen.getTitre());
        descriptionField.setText(examen.getDescription());

        // Remplir ComboBox des cours
        ObservableList<Cours> coursList = FXCollections.observableArrayList(coursService.afficher());
        coursComboBox.setItems(coursList);
        coursComboBox.setValue(examen.getCours());

        coursComboBox.setCellFactory(param -> new ListCell<Cours>() {
            @Override
            protected void updateItem(Cours item, boolean empty) {
                super.updateItem(item, empty);
                setText((empty || item == null) ? null : item.getTitre());
            }
        });
        coursComboBox.setButtonCell(new ListCell<Cours>() {
            @Override
            protected void updateItem(Cours item, boolean empty) {
                super.updateItem(item, empty);
                setText((empty || item == null) ? null : item.getTitre());
            }
        });

        // Configurer Spinner
        SpinnerValueFactory<Double> valueFactory =
                new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 20, examen.getNote(), 0.5);
        noteSpinner.setValueFactory(valueFactory);
    }

    @FXML
    private void modifierExamen() {
        String titre = titreField.getText().trim();
        String description = descriptionField.getText().trim();
        Cours selectedCours = coursComboBox.getValue();
        Double note = noteSpinner.getValue();

        // Validation des champs
        if (titre.isEmpty() || description.isEmpty() || selectedCours == null || note == null) {
            showAlert("Erreur", "Tous les champs doivent être remplis !");
            return;
        }

        // Validation de la longueur du titre
        if (titre.length() < 3) {
            showAlert("Erreur", "Le titre doit contenir au moins 3 caractères.");
            return;
        }

        // Validation de la longueur de la description
        if (description.length() < 5) {
            showAlert("Erreur", "La description doit contenir au moins 5 caractères.");
            return;
        }

        // Validation de la note (doit être entre 0 et 20)
        if (note < 0 || note > 20) {
            showAlert("Erreur", "La note doit être entre 0 et 20.");
            return;
        }

        // Mise à jour de l'examen
        examen.setTitre(titre);
        examen.setDescription(description);
        examen.setCours(selectedCours);
        examen.setNote(note);

        // Modifier l'examen dans la base de données
        examenService.modifier(examen);

        // Rafraîchir la liste des examens
        examenController.afficherExamens();

        // Confirmation de la modification réussie
        showInfo("Succès", "L'examen a été modifié avec succès.");

        // Fermer la fenêtre
        Stage stage = (Stage) titreField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
