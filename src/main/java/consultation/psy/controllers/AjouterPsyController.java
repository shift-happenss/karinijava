package consultation.psy.controllers;

import consultation.psy.entities.Psy;
import consultation.psy.services.ServicePsy;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;


public class AjouterPsyController {

    @FXML private TextField nomTF, numTF, emailTF, specTF, heureTF;
    @FXML private DatePicker dateTF;

    @FXML private Button btnRetour;


    @FXML
    void ajouterPsy(ActionEvent event) {
        if (!champsValides()) return;

        Psy p = new Psy(
                nomTF.getText(),
                Integer.parseInt(numTF.getText()),
                emailTF.getText(),
                specTF.getText(),
                dateTF.getValue().toString(),
                heureTF.getText()
        );
        try {
            new ServicePsy().ajouter(p);
            new Alert(Alert.AlertType.INFORMATION, "Psy ajouté avec succès !").show();
            nomTF.clear(); numTF.clear(); emailTF.clear(); specTF.clear(); dateTF.setValue(null); heureTF.clear();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }
    private boolean champsValides() {
        String nom = nomTF.getText().trim();
        String num = numTF.getText().trim();
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

        //  Date future
        LocalDate today = LocalDate.now();
        if (date.isBefore(today)) {
            new Alert(Alert.AlertType.WARNING, "❌ La date de disponibilité doit être dans le futur.").show();
            return false;
        }

        // Heure future si date = aujourd'hui
        if (date.isEqual(today)) {
            try {
                LocalTime heureChoisie = LocalTime.parse(heure);
                if (heureChoisie.isBefore(LocalTime.now())) {
                    new Alert(Alert.AlertType.WARNING, "❌ L'heure choisie doit être dans le futur.").show();
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
    void afficherPsy(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AfficherPsy.fxml"));
            nomTF.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
