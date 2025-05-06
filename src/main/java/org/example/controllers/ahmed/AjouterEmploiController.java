package org.example.controllers.ahmed;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.entities.Emploi;
import org.example.entities.User;
import org.example.services.ServiceEmploi;


import java.util.List;

public class AjouterEmploiController {

    @FXML
    private TextField tfTitre;
    @FXML
    private TextArea taDescription;
    @FXML
    private ComboBox<User> cbProprietaire; // Utilise une ComboBox pour afficher les utilisateurs

    private ServiceEmploi serviceEmploi;
    private Runnable onEmploiAjoute;

    public AjouterEmploiController() {

            serviceEmploi = new ServiceEmploi();

    }

    public void setOnEmploiAjoute(Runnable callback) {
        this.onEmploiAjoute = callback;
    }

    @FXML
    private void initialize() {
        List<User> users = serviceEmploi.getAllUsers();
        cbProprietaire.setItems(FXCollections.observableArrayList(users));

        // Configure la ComboBox pour afficher uniquement le nom de l'utilisateur
        cbProprietaire.setCellFactory(param -> new ListCell<User>() {
            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                setText(empty || user == null ? "" : user.getName());
            }
        });
        cbProprietaire.setButtonCell(new ListCell<User>() {
            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                setText(empty || user == null ? "" : user.getName());
            }
        });
    }

    @FXML
    private void handleAjouter() {
        if (!validateInputs()) {
            return;
        }

        String titre = tfTitre.getText().trim();
        String description = taDescription.getText().trim();
        User selectedUser = cbProprietaire.getValue();

        Emploi emp = new Emploi(titre, description, selectedUser.getId());
        serviceEmploi.ajouterEmploi(emp);
        if (onEmploiAjoute != null) {
            onEmploiAjoute.run();
        }
        closeWindow();
    }

    /**
     * Vérifie que tous les champs sont correctement renseignés.
     * Si un champ est vide, on affiche dans son promptText un message d'erreur et
     * on applique un style (bordure rouge).
     */
    private boolean validateInputs() {
        boolean valid = true;

        // Réinitialise le style de base
        tfTitre.setStyle("");
        taDescription.setStyle("");
        cbProprietaire.setStyle("");

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
        if (cbProprietaire.getValue() == null) {
            // Pour la ComboBox, le prompt text peut ne pas s'afficher, on met un style rouge
            cbProprietaire.setStyle("-fx-border-color: red;");
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
}
