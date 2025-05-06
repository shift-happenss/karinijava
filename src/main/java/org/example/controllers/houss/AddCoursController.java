package org.example.controllers.houss;

import org.example.entities.Cours;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.services.CoursService;

import java.time.LocalDate;

public class AddCoursController {

    @FXML
    private TextField titreField;
    @FXML
    private TextField descriptionField;
    @FXML
    private DatePicker dateDebutField;
    @FXML
    private DatePicker dateFinField;
    @FXML
    private TextField lienField;
    @FXML
    private Button btnAjouter;

    private CoursService coursService;
    private CoursController coursController; // Pour rafraîchir la liste après ajout

    public AddCoursController() {
        coursService = new CoursService();  // Initialiser le service des cours
    }

    // Méthode appelée lorsque le bouton "Ajouter" est cliqué
    @FXML
    private void ajouterCours() {
        // Récupération des champs
        String titre = titreField.getText();
        String description = descriptionField.getText();
        LocalDate dateDebut = dateDebutField.getValue();
        LocalDate dateFin = dateFinField.getValue();
        String lien = lienField.getText();

        // Vérification des champs vides
        if (titre.isEmpty() || description.isEmpty() || dateDebut == null || dateFin == null || lien.isEmpty()) {
            showAlert(AlertType.ERROR, "Erreur", "Tous les champs doivent être remplis.");
            return;
        }

        // Validation du titre
        if (titre.length() < 3) {
            showAlert(AlertType.ERROR, "Erreur de saisie", "Le titre doit contenir au moins 3 caractères.");
            return;
        }

        // Validation de la description
        if (description.length() > 300) {
            showAlert(AlertType.ERROR, "Erreur de saisie", "La description est trop longue (max 300 caractères).");
            return;
        }

        // Validation de la date
        if (dateDebut.isAfter(dateFin)) {
            showAlert(AlertType.ERROR, "Erreur de dates", "La date de début ne peut pas être après la date de fin.");
            return;
        }

        // Validation du lien
        if (!lien.startsWith("http://") && !lien.startsWith("https://")) {
            showAlert(AlertType.ERROR, "Lien invalide", "Le lien doit commencer par http:// ou https://");
            return;
        }

        // Création du cours
        Cours cours = new Cours(0, titre, description, dateDebut.atStartOfDay(), dateFin.atStartOfDay(), lien);

        try {
            boolean success = coursService.ajouter(cours);
            if (success) {
                showAlert(AlertType.INFORMATION, "Succès", "Le cours a été ajouté avec succès.");
                if (coursController != null) {
                    coursController.afficherCours();
                }
                // Fermer la fenêtre
                Stage stage = (Stage) titreField.getScene().getWindow();
                stage.close();
            } else {
                showAlert(AlertType.ERROR, "Erreur", "Impossible d'ajouter le cours.");
            }
        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Exception", "Une erreur est survenue : " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setCoursController(CoursController controller) {
        this.coursController = controller;
    }
}
