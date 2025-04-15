package tn.esprit.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tn.esprit.models.Commentaire;
import tn.esprit.models.Ressource;
import tn.esprit.services.CommentaireService;
import tn.esprit.services.RessourceService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;

import java.io.IOException;
import java.util.List;

public class ShowRessourceController {

    @FXML private Label titleLabel;
    @FXML private Label idLabel;
    @FXML private Label categoryLabel;
    @FXML private Label titreLabel;
    @FXML private Label descriptionLabel;
    @FXML private Label typeLabel;
    @FXML private Label videoLabel;
    @FXML private Label fileLabel;
    @FXML private Label contentLabel;
    @FXML private ImageView imageView;
    @FXML private VBox commentairesContainer;

    @FXML private Button deleteButton;
    @FXML private Button updateButton;

    private Ressource selectedRessource;
    private RessourceService ressourceService;
    private CommentaireService commentaireService;

    public ShowRessourceController() {
        this.ressourceService = new RessourceService();
        this.commentaireService = new CommentaireService();
    }

    public void initialize(Ressource ressource) {
        selectedRessource = ressource;

        titleLabel.setText(ressource.getTitre());
        idLabel.setText("ID: " + ressource.getId());
        titreLabel.setText("Titre: " + ressource.getTitre());
        descriptionLabel.setText("Description: " + ressource.getDescription());
        typeLabel.setText("Type: " + ressource.getType());
        videoLabel.setText("Vidéo: " + (ressource.getUrlVideo() != null ? ressource.getUrlVideo() : "Non spécifiée"));
        contentLabel.setText("Contenu: " + (ressource.getContenuTexte() != null ? ressource.getContenuTexte() : "Non spécifié"));

        // 🔁 Image loading logic
        if (ressource.getUrlImage() != null && !ressource.getUrlImage().isEmpty()) {
            try {
                String imageUrl = ressource.getUrlImage();
                if (!imageUrl.startsWith("http") && !imageUrl.startsWith("file:")) {
                    imageUrl = "file:" + imageUrl;  // Add prefix if it's a local file path
                }
                Image image = new Image(imageUrl, true);
                imageView.setImage(image);
            } catch (Exception e) {
                System.out.println("Erreur lors du chargement de l'image : " + e.getMessage());
            }
        }

        List<Commentaire> commentaires = commentaireService.getCommentairesParRessource(ressource.getId());
        afficherCommentaires(commentaires);
    }

    @FXML
    public void handleDeleteClick() {
        // Perform delete operation
        try {
            boolean isDeleted = ressourceService.supprimerRessource(selectedRessource.getId()); // Returns a boolean

            if (isDeleted) {
                // Show success alert
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Succès");
                alert.setHeaderText("Ressource supprimée");
                alert.setContentText("La ressource a été supprimée avec succès.");
                alert.showAndWait();

                // Close the current window (or navigate back)
                Stage stage = (Stage) deleteButton.getScene().getWindow();
                stage.close();
            } else {
                // Show error alert if delete fails
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Suppression échouée");
                alert.setContentText("Impossible de supprimer la ressource.");
                alert.showAndWait();
            }
        } catch (Exception e) {
            // Handle any exceptions that occur
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur lors de la suppression");
            alert.setContentText("Une erreur s'est produite lors de la suppression de la ressource.");
            alert.showAndWait();
        }
    }

    @FXML
    private void onUpdateClicked() {
        if (selectedRessource == null || selectedRessource.getId() <= 0) {
            showAlert(AlertType.ERROR, "Erreur", "Aucune ressource sélectionnée", "L'ID de la ressource sélectionnée est invalide.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateRessource.fxml"));
            Parent root = loader.load();

            UpdateRessourceController controller = loader.getController();
            controller.initialize(selectedRessource);

            Stage stage = new Stage();
            stage.setTitle("Modifier la Ressource");
            stage.setScene(new Scene(root));
            stage.show();

            ((Stage) updateButton.getScene().getWindow()).close();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Erreur", "Impossible d'ouvrir la fenêtre de modification", "Détails de l'erreur: " + e.getMessage());
        }
    }

    private void showAlert(AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void closeCurrentWindow(Button button) {
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
    }

    private void afficherCommentaires(List<Commentaire> commentaires) {
        commentairesContainer.getChildren().clear();

        if (commentaires.isEmpty()) {
            Label noCommentsLabel = new Label("Aucun commentaire pour cette ressource.");
            noCommentsLabel.setStyle("-fx-font-style: italic; -fx-text-fill: gray;");
            commentairesContainer.getChildren().add(noCommentsLabel);
            return;
        }

        for (Commentaire c : commentaires) {
            Label commentaireLabel = new Label("Utilisateur " + c.getProprietereId() + " : " + c.getContenu());
            commentaireLabel.setStyle("-fx-background-color: #f5f5f5; -fx-padding: 10px; -fx-background-radius: 10px; -fx-text-fill: black;");

            Button deleteCommentButton = new Button("Supprimer");
            deleteCommentButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
            deleteCommentButton.setOnAction(e -> handleDeleteCommentaireClick(c));

            VBox commentBox = new VBox(commentaireLabel, deleteCommentButton);
            commentBox.setSpacing(5);
            commentairesContainer.getChildren().add(commentBox);

            if (c.getReponse() != null && !c.getReponse().isEmpty()) {
                Label reponseLabel = new Label("Réponse : " + c.getReponse());
                reponseLabel.setStyle("-fx-background-color: #d3f8e2; -fx-padding: 8px; -fx-font-style: italic; -fx-background-radius: 8px; -fx-text-fill: black;");
                commentairesContainer.getChildren().add(reponseLabel);
            }
        }
    }

    @FXML
    public void handleDeleteCommentaireClick(Commentaire commentaire) {
        if (commentaire == null || commentaire.getId() == 0) {
            showAlert(AlertType.ERROR, "Erreur", "ID de commentaire invalide", "Le commentaire que vous essayez de supprimer n'a pas un ID valide.");
            return;
        }

        Alert confirmDeleteAlert = new Alert(AlertType.CONFIRMATION);
        confirmDeleteAlert.setTitle("Confirmation de suppression");
        confirmDeleteAlert.setHeaderText("Êtes-vous sûr de vouloir supprimer ce commentaire ?");
        confirmDeleteAlert.setContentText("Cette action est irréversible.");

        confirmDeleteAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                boolean isDeleted = commentaireService.deleteCommentaire(commentaire.getId());

                if (isDeleted) {
                    showAlert(AlertType.INFORMATION, "Succès", "Commentaire supprimé", "Le commentaire a été supprimé avec succès.");
                    List<Commentaire> commentaires = commentaireService.getCommentairesParRessource(selectedRessource.getId());
                    afficherCommentaires(commentaires);
                } else {
                    showAlert(AlertType.ERROR, "Erreur", "Suppression échouée", "Impossible de supprimer le commentaire.");
                }
            }
        });
    }
}
