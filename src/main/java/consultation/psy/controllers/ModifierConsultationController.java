package consultation.psy.controllers;

import consultation.psy.entities.Consultation;
import consultation.psy.entities.Psy;
import consultation.psy.services.ServiceConsultation;
import consultation.psy.services.ServicePsy;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;


public class ModifierConsultationController {

    @FXML private DatePicker datePicker;
    @FXML private TextField timeField;
    @FXML private TextArea raisonField;
    @FXML private TextField statusField;
    @FXML private ComboBox<Psy> psyCombo;
    @FXML private Button btnRetour;


    private Consultation consultation;

    public void setConsultation(Consultation consultation) {
        this.consultation = consultation;

        datePicker.setValue(java.time.LocalDate.parse(consultation.getDate()));
        timeField.setText(consultation.getTime());
        raisonField.setText(consultation.getRaison());
        statusField.setText(consultation.getStatus());

        try {
            ServicePsy servicePsy = new ServicePsy();
            psyCombo.getItems().addAll(servicePsy.getList());
            if (consultation.getPsy() != null) {
                for (Psy p : psyCombo.getItems()) {
                    if (p.getId() == consultation.getPsy().getId()) {
                        psyCombo.setValue(p);
                        break;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void modifier() {
        if (!champsValides()) return;

        consultation.setDate(datePicker.getValue().toString());
        consultation.setTime(timeField.getText());
        consultation.setRaison(raisonField.getText());
        consultation.setStatus(statusField.getText());
        consultation.setPsy(psyCombo.getValue());

        try {
            new ServiceConsultation().modifier(consultation);
            closeWindow();
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Erreur lors de la modification").show();
        }
    }

    private boolean champsValides() {
        LocalDate date = datePicker.getValue();
        String heure = timeField.getText().trim();
        String raison = raisonField.getText().trim();
        String status = statusField.getText().trim();
        Psy psy = psyCombo.getValue();

        if (date == null || heure.isEmpty() || raison.isEmpty() || status.isEmpty() || psy == null) {
            new Alert(Alert.AlertType.WARNING, "⚠️ Tous les champs sont obligatoires.").show();
            return false;
        }

        if (!heure.matches("^[0-2]\\d:[0-5]\\d$")) {
            new Alert(Alert.AlertType.WARNING, "❌ Heure invalide. Format attendu : HH:mm (ex : 10:30)").show();
            return false;
        }

        LocalDate today = LocalDate.now();
        if (date.isBefore(today)) {
            new Alert(Alert.AlertType.WARNING, "❌ La date doit être aujourd’hui ou dans le futur.").show();
            return false;
        }

        if (date.isEqual(today)) {
            try {
                LocalTime heureChoisie = LocalTime.parse(heure);
                if (heureChoisie.isBefore(LocalTime.now())) {
                    new Alert(Alert.AlertType.WARNING, "❌ L'heure doit être dans le futur.").show();
                    return false;
                }
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "Erreur de format d’heure.").show();
                return false;
            }
        }

        return true;
    }



    @FXML
    void supprimer() {
        try {
            new ServiceConsultation().supprimer(consultation.getId());
            closeWindow();
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Erreur lors de la suppression").show();
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) statusField.getScene().getWindow();
        stage.close();
    }

    @FXML
    void retour() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherConsultation.fxml"));
            Parent root = loader.load();
            btnRetour.getScene().setRoot(root); // 👈 Affiche la scène précédente dans la même fenêtre
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
