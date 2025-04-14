package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.entities.Emploi;
import tn.esprit.entities.User;
import tn.esprit.services.ServiceEmploi;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModifierEmploiController {

    @FXML
    private TextField tfTitre;
    @FXML
    private TextArea taDescription;
    @FXML
    private ComboBox<String> cbProprietaireNom; // On affiche le nom

    private Map<String, Integer> nomToIdMap = new HashMap<>();
    private ServiceEmploi serviceEmploi;
    private Emploi emploiAModifier;
    private Runnable onEmploiModifie;

    public ModifierEmploiController() {
        try {
            serviceEmploi = new ServiceEmploi();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void setEmploi(Emploi emp) {
        emploiAModifier = emp;
        tfTitre.setText(emp.getTitre());
        taDescription.setText(emp.getDescription());

        List<User> users = serviceEmploi.getAllUsers();
        cbProprietaireNom.getItems().clear();
        nomToIdMap.clear();

        for (User u : users) {
            cbProprietaireNom.getItems().add(u.getNom());
            nomToIdMap.put(u.getNom(), u.getId());
        }

        cbProprietaireNom.setValue(emp.getProprietaireNom());
    }

    @FXML
    private void handleModifier() {
        if (!validateInputs()) {
            return;
        }

        String titre = tfTitre.getText().trim();
        String description = taDescription.getText().trim();
        String nomProprietaire = cbProprietaireNom.getValue();

        Integer proprietaireId = nomToIdMap.get(nomProprietaire);
        if (proprietaireId == null) {
            cbProprietaireNom.setStyle("-fx-border-color: red;");
            return;
        }

        emploiAModifier.setTitre(titre);
        emploiAModifier.setDescription(description);
        emploiAModifier.setProprietaireId(proprietaireId);
        emploiAModifier.setProprietaireNom(nomProprietaire);

        serviceEmploi.modifierEmploi(emploiAModifier);
        if (onEmploiModifie != null) {
            onEmploiModifie.run();
        }
        closeWindow();
    }

    /**
     * Valide les champs et applique un style d'erreur si nécessaire.
     */
    private boolean validateInputs() {
        boolean valid = true;
        // Réinitialise les styles
        tfTitre.setStyle("");
        taDescription.setStyle("");
        cbProprietaireNom.setStyle("");

        if (tfTitre.getText().trim().isEmpty()) {
            tfTitre.setPromptText("Titre obligatoire");
            tfTitre.setStyle("-fx-border-color: red;");
            valid = false;
        }
        if (taDescription.getText().trim().isEmpty()) {
            taDescription.setPromptText("Description obligatoire");
            taDescription.setStyle("-fx-border-color: red;");
            valid = false;
        }
        if (cbProprietaireNom.getValue() == null || cbProprietaireNom.getValue().trim().isEmpty()) {
            cbProprietaireNom.setStyle("-fx-border-color: red;");
            valid = false;
        }
        return valid;
    }

    @FXML
    private void handleAnnuler() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) tfTitre.getScene().getWindow();
        stage.close();
    }

    public void setOnEmploiModifie(Runnable callback) {
        this.onEmploiModifie = callback;
    }

    private void showAlert(String title, String message) {
        // Optionnel : cette méthode peut être omise si on se contente du feedback visuel
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
