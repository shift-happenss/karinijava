package org.example.controllers.blog;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.entities.Ressource;
import org.example.services.RessourceService;

public class UpdateRessourceController {

    @FXML private TextField titreField;
    @FXML private TextField descriptionField;
    @FXML private TextField typeField;
    @FXML private TextField videoField;
    @FXML private TextField imageField;
    @FXML private TextField fileField;
    @FXML private TextField contentField;

    private Ressource selectedRessource;
    private RessourceService ressourceService;
    private Runnable refreshCallback; // For parent window refresh

    public void initialize(Ressource ressource) {
        selectedRessource = ressource;
        ressourceService = new RessourceService();

        // Populate fields
        titreField.setText(ressource.getTitre());
        descriptionField.setText(ressource.getDescription());
        typeField.setText(ressource.getType());
        videoField.setText(ressource.getUrlVideo());
        imageField.setText(ressource.getUrlImage());
        fileField.setText(ressource.getUrlFichier());
        contentField.setText(ressource.getContenuTexte());
    }

    public void setRefreshCallback(Runnable refreshCallback) {
        this.refreshCallback = refreshCallback;
    }

    @FXML
    public void handleSaveClick() {
        // Validate mandatory fields
        if (titreField.getText().isEmpty() || descriptionField.getText().isEmpty() || typeField.getText().isEmpty()) {
            showAlert(AlertType.WARNING, "Avertissement", "Champs obligatoires manquants", "Titre, Description et Type doivent être remplis.");
            return;
        }

        // Update resource
        selectedRessource.setTitre(titreField.getText());
        selectedRessource.setDescription(descriptionField.getText());
        selectedRessource.setType(typeField.getText());
        selectedRessource.setUrlVideo(videoField.getText());
        selectedRessource.setUrlImage(imageField.getText());
        selectedRessource.setUrlFichier(fileField.getText());
        selectedRessource.setContenuTexte(contentField.getText());

        try {
            ressourceService.modifierRessource(selectedRessource);

            // Refresh parent window
            if (refreshCallback != null) {
                refreshCallback.run();
            }

            showAlert(AlertType.INFORMATION, "Succès", "Ressource mise à jour", "La ressource a été mise à jour avec succès.");
            closeWindow();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Erreur", "Erreur lors de la mise à jour", "Erreur: " + e.getMessage());
        }
    }

    @FXML
    public void handleCancelClick() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) titreField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}