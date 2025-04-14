package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.entities.Evenement;
import tn.esprit.services.ServiceEvenement;

import java.sql.SQLException;
import java.time.LocalDate;

public class AjouterEvenementController {

    @FXML
    private TextField tfNom;
    @FXML
    private TextField tfType;
    @FXML
    private DatePicker dpDateDebut;
    @FXML
    private DatePicker dpDateFin;
    @FXML
    private TextArea taContenu;
    @FXML
    private TextField tfLieu;
    @FXML
    private TextField tfLatitude;
    @FXML
    private TextField tfLongitude;

    private ServiceEvenement serviceEvenement;
    private Runnable onEvenementAjoute;

    public AjouterEvenementController() {
        try {
            serviceEvenement = new ServiceEvenement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void setOnEvenementAjoute(Runnable callback) {
        this.onEvenementAjoute = callback;
    }

    @FXML
    private void handleAjouter() {
        if (!validateInputs()) {
            return;
        }

        String nom = tfNom.getText().trim();
        String type = tfType.getText().trim();
        LocalDate dateDebut = dpDateDebut.getValue();
        LocalDate dateFin = dpDateFin.getValue();
        String contenu = taContenu.getText().trim();
        String lieu = tfLieu.getText().trim();
        double latitude;
        double longitude;
        try {
            latitude = Double.parseDouble(tfLatitude.getText().trim());
            longitude = Double.parseDouble(tfLongitude.getText().trim());
        } catch (NumberFormatException e) {
            setFieldError(tfLatitude, "Doit être un nombre");
            setFieldError(tfLongitude, "Doit être un nombre");
            return;
        }

        Evenement ev = new Evenement(nom, type, dateDebut, dateFin, contenu, lieu, latitude, longitude);
        serviceEvenement.ajouterEvenement(ev);
        if (onEvenementAjoute != null) {
            onEvenementAjoute.run();
        }
        closeWindow();
    }

    @FXML
    private void handleAnnuler() {
        closeWindow();
    }

    private boolean validateInputs() {
        boolean valid = true;
        // Réinitialiser les styles
        resetField(tfNom);
        resetField(tfType);
        resetField(dpDateDebut);
        resetField(dpDateFin);
        resetField(taContenu);
        resetField(tfLieu);
        resetField(tfLatitude);
        resetField(tfLongitude);

        if (tfNom.getText() == null || tfNom.getText().trim().isEmpty()) {
            setFieldError(tfNom, "Nom obligatoire");
            valid = false;
        }
        if (tfType.getText() == null || tfType.getText().trim().isEmpty()) {
            setFieldError(tfType, "Type obligatoire");
            valid = false;
        }
        if (dpDateDebut.getValue() == null) {
            setFieldError(dpDateDebut, "Date début obligatoire");
            valid = false;
        }
        if (dpDateFin.getValue() == null) {
            setFieldError(dpDateFin, "Date fin obligatoire");
            valid = false;
        }
        if (taContenu.getText() == null || taContenu.getText().trim().isEmpty()) {
            setFieldError(taContenu, "Contenu obligatoire");
            valid = false;
        }
        if (tfLieu.getText() == null || tfLieu.getText().trim().isEmpty()) {
            setFieldError(tfLieu, "Lieu obligatoire");
            valid = false;
        }
        if (tfLatitude.getText() == null || tfLatitude.getText().trim().isEmpty()) {
            setFieldError(tfLatitude, "Latitude obligatoire");
            valid = false;
        }
        if (tfLongitude.getText() == null || tfLongitude.getText().trim().isEmpty()) {
            setFieldError(tfLongitude, "Longitude obligatoire");
            valid = false;
        }
        return valid;
    }

    private void setFieldError(Control field, String errorMessage) {
        field.setStyle("-fx-border-color: red;");
        // Pour TextField ou TextArea, on met à jour le promptText pour indiquer l'erreur
        if (field instanceof TextInputControl) {
            ((TextInputControl) field).setPromptText(errorMessage);
        } else if (field instanceof DatePicker) {
            ((DatePicker) field).setPromptText(errorMessage);
        }
    }

    private void resetField(Control field) {
        field.setStyle(""); // Réinitialise le style
        // On peut remettre le promptText par défaut si désiré (si vous avez défini un prompt initial dans le FXML)
    }

    private void closeWindow() {
        Stage stage = (Stage) tfNom.getScene().getWindow();
        stage.close();
    }

    // Optionnel : vous pouvez garder showAlert si vous souhaitez afficher un message d'erreur général
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
