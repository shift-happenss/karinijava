package org.example.controllers.houss;

import org.example.entities.Cours;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.services.CoursService;

import java.time.LocalDate;

public class ModifierCoursController {

    @FXML
    private TextField titreField, descriptionField, lienField; // Ajout du champ lienField

    @FXML
    private DatePicker dateDebutField, dateFinField;

    private Cours cours;
    private final CoursService coursService = new CoursService();
    private CoursController coursController;

    public void setCours(Cours cours, CoursController controller) {
        this.cours = cours;
        this.coursController = controller;

        titreField.setText(cours.getTitre());
        descriptionField.setText(cours.getDescription());
        dateDebutField.setValue(cours.getDateDebut().toLocalDate());
        dateFinField.setValue(cours.getDateFin().toLocalDate());
        lienField.setText(cours.getLien()); // Pré-remplir le lien
    }

    @FXML
    private void modifierCours() {
        String titre = titreField.getText().trim();
        String description = descriptionField.getText().trim();
        String lien = lienField.getText().trim(); // Récupérer le lien
        LocalDate debutDate = dateDebutField.getValue();
        LocalDate finDate = dateFinField.getValue();

        if (titre.isEmpty() || description.isEmpty() || lien.isEmpty() || debutDate == null || finDate == null) {
            showAlert("Erreur", "Tous les champs doivent être remplis.");
            return;
        }

        if (titre.length() < 3) {
            showAlert("Erreur", "Le titre doit contenir au moins 3 caractères.");
            return;
        }

        if (description.length() < 5) {
            showAlert("Erreur", "La description doit contenir au moins 5 caractères.");
            return;
        }

        if (!lien.startsWith("http://") && !lien.startsWith("https://")) {
            showAlert("Erreur", "Le lien doit commencer par http:// ou https://");
            return;
        }

        if (debutDate.isAfter(finDate)) {
            showAlert("Erreur", "La date de début ne peut pas être après la date de fin.");
            return;
        }

        // Mise à jour des champs
        cours.setTitre(titre);
        cours.setDescription(description);
        cours.setLien(lien);
        cours.setDateDebut(debutDate.atStartOfDay());
        cours.setDateFin(finDate.atStartOfDay());

        coursService.modifier(cours);

        if (coursController != null) {
            coursController.afficherCours();
        }

        showAlert("Succès", "Le cours a été modifié avec succès.");
        Stage stage = (Stage) titreField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
