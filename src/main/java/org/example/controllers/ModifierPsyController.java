package org.example.controllers;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import org.example.entities.Psy;
import org.example.services.ServicePsy;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;


public class ModifierPsyController {

    @FXML private TextField nomTF;
    @FXML private TextField telTF;
    @FXML private TextField emailTF;
    @FXML private TextField specTF;
    @FXML private DatePicker dateTF;
    @FXML private TextField heureTF;
    @FXML private Button btnRetour;


    private Psy psy;

    public void setPsy(Psy psy) {
        this.psy = psy;
        nomTF.setText(psy.getNom());
        telTF.setText(String.valueOf(psy.getNumerotel()));
        emailTF.setText(psy.getMail());
        specTF.setText(psy.getSpecialite());
        dateTF.setValue(LocalDate.parse(psy.getDatedispo()));
        heureTF.setText(psy.getTimedispo());
    }

    @FXML
    void modifier() {
        if (!champsValides()) return;

        try {
            psy.setNom(nomTF.getText());
            psy.setNumerotel(Integer.parseInt(telTF.getText()));
            psy.setMail(emailTF.getText());
            psy.setSpecialite(specTF.getText());
            psy.setDatedispo(dateTF.getValue().toString());
            psy.setTimedispo(heureTF.getText());

            new ServicePsy().modifier(psy);
            close();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private boolean champsValides() {
        String nom = nomTF.getText().trim();
        String num = telTF.getText().trim();
        String email = emailTF.getText().trim();
        String spec = specTF.getText().trim();
        String heure = heureTF.getText().trim();
        LocalDate date = dateTF.getValue();

        if (nom.isEmpty() || num.isEmpty() || email.isEmpty() || spec.isEmpty() || date == null || heure.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "⚠️ Tous les champs sont obligatoires.").show();
            return false;
        }

        if (!num.matches("\\d{8}")) {
            new Alert(Alert.AlertType.WARNING, "❌ Le numéro de téléphone doit contenir exactement 8 chiffres.").show();
            return false;
        }


        if (!email.matches("^[\\w-.]+@[\\w-]+\\.[a-z]{2,4}$")) {
            new Alert(Alert.AlertType.WARNING, "❌ Adresse email invalide.").show();
            return false;
        }

        if (!heure.matches("^[0-2]\\d:[0-5]\\d$")) {
            new Alert(Alert.AlertType.WARNING, "❌ L'heure doit être au format HH:mm (ex: 14:30)").show();
            return false;
        }

        // Vérifie que la date est dans le futur
        LocalDate today = LocalDate.now();
        if (date.isBefore(today)) {
            new Alert(Alert.AlertType.WARNING, "❌ La date de disponibilité doit être dans le futur.").show();
            return false;
        }

        // Si la date est aujourd’hui, vérifie que l’heure est future
        if (date.isEqual(today)) {
            try {
                LocalTime heureChoisie = LocalTime.parse(heure);
                if (heureChoisie.isBefore(LocalTime.now())) {
                    new Alert(Alert.AlertType.WARNING, "❌ L'heure doit être dans le futur.").show();
                    return false;
                }
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "Erreur de format d'heure.").show();
                return false;
            }
        }

        return true;
    }

    @FXML
    void supprimer() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "⚠️ Êtes-vous sûr de vouloir supprimer ce psy et toutes ses consultations ?",
                ButtonType.YES, ButtonType.NO);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Suppression définitive");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                try {
                    new ServicePsy().supprimer(psy.getId());


                    Alert info = new Alert(Alert.AlertType.INFORMATION, "Psychologue supprimé avec succès !");
                    info.showAndWait();

                    close();
                } catch (Exception e) {
                    new Alert(Alert.AlertType.ERROR, "Erreur : " + e.getMessage()).show();
                }
            }
        });
    }

    void close() {
        Stage stage = (Stage) nomTF.getScene().getWindow();
        stage.close();
    }

    @FXML
    void retour() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherPsy.fxml"));
            Parent root = loader.load();
            btnRetour.getScene().setRoot(root); // 👈 Affiche la scène précédente dans la même fenêtre
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void allerVersadmin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin.fxml")); // adapte le chemin
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();


            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
