package consultation.psy.controllers;

import consultation.psy.entities.Consultation;
import consultation.psy.entities.Psy;
import consultation.psy.services.ServiceConsultation;
import consultation.psy.services.ServicePsy;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

public class AjouterConsultationController {

    @FXML private DatePicker datePicker;
    @FXML private TextField timeField;
    @FXML private TextArea raisonTA;
    @FXML private ComboBox<Psy> psyCB;
    @FXML private Button ajouterBtn;
    @FXML private Button btnAfficher;
    @FXML private Button btnRetour;
    @FXML private Button btnAffpsy;



    @FXML
    public void initialize() {
        try {
            psyCB.getItems().addAll(new ServicePsy().getList());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void ajouterConsultation(ActionEvent event) {
        if (!champsValides()) return;


        Consultation c = new Consultation(
                datePicker.getValue().toString(),
                timeField.getText(),
                raisonTA.getText(),
                "en attente",  // statut par défaut
                "",
                psyCB.getValue()
        );

        try {
            new ServiceConsultation().ajouter(c);
            new Alert(Alert.AlertType.INFORMATION, "Consultation ajoutée avec succès !").show();

            // Réinitialiser les champs
            datePicker.setValue(null);
            timeField.clear();
            raisonTA.clear();
            psyCB.setValue(null);

        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Erreur SQL : " + e.getMessage()).show();
        }
    }

    private boolean champsValides() {
        LocalDate date = datePicker.getValue();
        String heure = timeField.getText().trim();
        String raison = raisonTA.getText().trim();
        Psy psy = psyCB.getValue();

        if (date == null || heure.isEmpty() || raison.isEmpty() || psy == null) {
            new Alert(Alert.AlertType.WARNING, "⚠️ Tous les champs sont obligatoires.").show();
            return false;
        }

        if (!heure.matches("^[0-2]\\d:[0-5]\\d$")) {
            new Alert(Alert.AlertType.WARNING, "❌ Heure invalide. Format attendu : HH:mm (ex : 09:00)").show();
            return false;
        }

        // ✅ Date future
        LocalDate today = LocalDate.now();
        if (date.isBefore(today)) {
            new Alert(Alert.AlertType.WARNING, "❌ La date doit être aujourd'hui ou dans le futur.").show();
            return false;
        }

        // ✅ Heure future si date = aujourd'hui
        if (date.isEqual(today)) {
            try {
                LocalTime heureChoisie = LocalTime.parse(heure);
                if (heureChoisie.isBefore(LocalTime.now())) {
                    new Alert(Alert.AlertType.WARNING, "❌ L'heure choisie doit être dans le futur.").show();
                    return false;
                }
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "Erreur dans le format d'heure.").show();
                return false;
            }
        }

        return true;
    }

    @FXML
    void afficherConsultations(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherConsultation.fxml"));
            Parent root = loader.load();
            datePicker.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Erreur lors du chargement de la liste : " + e.getMessage()).show();
        }
    }

    @FXML
    void afficherpsys(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherPsy.fxml"));
            Parent root = loader.load();
            btnAffpsy.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Erreur lors du chargement de la liste : " + e.getMessage()).show();
        }


    }

  /*  @FXML
    void retour() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherPsy.fxml"));
            Parent root = loader.load();
            btnRetour.getScene().setRoot(root); // 👈 Affiche la scène précédente dans la même fenêtre
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

}
