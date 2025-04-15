package tn.esprit.gui;

import tn.esprit.models.Ressource;
import tn.esprit.services.RessourceService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;

public class AjouterRessourceController {

    // Déclaration des champs de l'interface
    @FXML
    private TextField titreField;
    @FXML
    private TextArea descriptionField;
    @FXML
    private TextField typeField;
    @FXML
    private TextField urlVideoField;
    @FXML
    private TextField urlImageField;
    @FXML
    private TextField urlFichierField;
    @FXML
    private TextArea contenuTexteField;

    private RessourceService ressourceService;

    public AjouterRessourceController() {
        // Initialiser le service Ressource
        this.ressourceService = new RessourceService();
    }



    private String sanitizePath(String path) {
        if (path == null || path.isEmpty()) {
            return path;
        }
        // Replace backslashes with forward slashes
        String cleanedPath = path.replace("\\", "/");
        // Remove invisible Unicode characters and trim
        cleanedPath = cleanedPath.replaceAll("[^\\x00-\\x7F]", "").trim();
        return cleanedPath;
    }

    // Méthode appelée lorsque l'on clique sur le bouton "Ajouter"
    @FXML
    private void ajouterRessource() {

        // Récupération des valeurs saisies par l'utilisateur
        String titre = titreField.getText();
        String description = descriptionField.getText();
        String type = typeField.getText();
        String urlImage = sanitizePath(urlImageField.getText());
        String urlVideo = sanitizePath(urlVideoField.getText());
        String urlFichier = sanitizePath(urlFichierField.getText());
        String contenuTexte = contenuTexteField.getText();

        // Vérifier que les champs obligatoires ne sont pas vides
        if (titre.isEmpty() || description.isEmpty() || type.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Champs manquants", "Veuillez remplir tous les champs obligatoires.");
        } else {
            // Création de la nouvelle ressource à ajouter
            Ressource nouvelleRessource = new Ressource(0, 1, titre, description, type, urlVideo, urlImage, urlFichier, contenuTexte);

            // Appel au service pour ajouter la ressource
            ressourceService.ajouterRessource(nouvelleRessource);

            // Affichage d'un message de confirmation
            showAlert(Alert.AlertType.INFORMATION, "Ressource ajoutée", "La ressource a été ajoutée avec succès.");

            // Réinitialisation des champs de texte après ajout
            viderChamps();
        }
    }

    // Méthode appelée lorsque l'on clique sur le bouton "Annuler"
    @FXML
    private void annuler() {
        // Réinitialisation des champs de texte
        viderChamps();
    }

    // Méthode pour vider les champs de saisie
    private void viderChamps() {
        titreField.clear();
        descriptionField.clear();
        typeField.clear();
        urlVideoField.clear();
        urlImageField.clear();
        urlFichierField.clear();
        contenuTexteField.clear();
    }

    // Méthode pour afficher des alertes
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Méthodes pour gérer la sélection de fichiers via un FileChooser
    @FXML
    private void handleSelectImageFile() {
        String imagePath = openFileChooser("Choisir une image", "*.png", "*.jpg", "*.jpeg", "*.gif");
        if (imagePath != null) {
            urlImageField.setText(sanitizePath(imagePath)); // Clean the path
        }
    }

    @FXML
    private void handleSelectVideoFile() {
        String videoPath = openFileChooser("Choisir une vidéo", "*.mp4", "*.avi", "*.mov");
        if (videoPath != null) {
            urlVideoField.setText(sanitizePath(videoPath)); // Clean the path
        }
    }

    // Méthode pour ouvrir le FileChooser et récupérer le chemin du fichier
    private String openFileChooser(String title, String... extensions) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);

        // Add file filters
        for (String ext : extensions) {
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Fichiers " + ext, ext)
            );
        }

        File file = fileChooser.showOpenDialog(getStage());
        return (file != null) ? file.getAbsolutePath() : null; // Return ABSOLUTE PATH
    }

    // Méthode pour obtenir la fenêtre principale (Stage)
    private Stage getStage() {
        return (Stage) titreField.getScene().getWindow();
    }
}
