package org.example.controllers.blog;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import org.example.entities.Commentaire;
import org.example.entities.Ressource;
import org.example.services.CommentaireService;

import java.util.List;

public class ShowFrontRessourceController {

    @FXML private Label categoryLabel;
    @FXML private Label titreLabel;
    @FXML private Label descriptionLabel;
    @FXML private Label contentLabel;
    @FXML private MediaView videoLabel;
    @FXML private ImageView imageLabel;
    @FXML private VBox commentairesContainer;
    @FXML private Button playPauseButton;

    private Ressource currentRessource;
    private final CommentaireService commentaireService = new CommentaireService();
    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;

    @FXML
    public void initialize() {
        // Compact media dimensions
        videoLabel.setFitWidth(220);
        videoLabel.setFitHeight(130);
        imageLabel.setFitWidth(140);
        imageLabel.setFitHeight(100);
        imageLabel.setPreserveRatio(true);

        // Compact label styling
        categoryLabel.setStyle("-fx-font-size: 10px;");
        titreLabel.setStyle("-fx-font-size: 11px; -fx-font-weight: bold;");
        descriptionLabel.setStyle("-fx-font-size: 10px;");
        contentLabel.setStyle("-fx-font-size: 10px;");
    }

    public void setRessource(Ressource ressource) {
        if (ressource != null) {
            this.currentRessource = ressource;
            String imagePath = ressource.getUrlImage();
            String videoPath = ressource.getUrlVideo();

            // Load video
            if (videoPath != null && !videoPath.isEmpty()) {
                initializeMediaPlayer(videoPath);
            }

            // Load image
            if (imagePath != null && !imagePath.isEmpty()) {
                try {
                    Image image = new Image("file:" + imagePath);
                    imageLabel.setImage(image);
                } catch (Exception e) {
                    System.err.println("Error loading image: " + e.getMessage());
                }
            } else {
                imageLabel.setImage(null);
            }

            // Set resource info
            titreLabel.setText("Titre: " + ressource.getTitre());
            descriptionLabel.setText("Desc: " + ressource.getDescription());
            contentLabel.setText("Contenu: " + (ressource.getContenuTexte() != null ? ressource.getContenuTexte() : "N/A"));

            // Load comments
            List<Commentaire> commentaires = commentaireService.getCommentairesParRessource(ressource.getId());
            afficherCommentaires(commentaires);
        }
    }

    private void initializeMediaPlayer(String videoPath) {
        try {
            videoPath = videoPath.replaceAll("[^\\x00-\\x7F]", "").trim();
            if (videoPath.startsWith("C:/")) {
                videoPath = "file:///" + videoPath;
            } else {
                videoPath = "file://" + videoPath;
            }

            Media media = new Media(videoPath);
            mediaPlayer = new MediaPlayer(media);
            videoLabel.setMediaPlayer(mediaPlayer);
            mediaPlayer.play();
            isPlaying = true;
        } catch (Exception e) {
            System.err.println("Error loading video: " + e.getMessage());
        }
    }

    @FXML
    private void togglePlayPause() {
        if (mediaPlayer != null) {
            if (isPlaying) {
                mediaPlayer.pause();
                playPauseButton.setText("▶ Play");
            } else {
                mediaPlayer.play();
                playPauseButton.setText("⏸ Pause");
            }
            isPlaying = !isPlaying;
        }
    }

    @FXML
    private void handleAddCommentaire() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/blog/AjouterCommentaire.fxml"));
            AnchorPane root = loader.load();

            AjouterCommentaireController commentController = loader.getController();
            if (currentRessource != null) {
                commentController.setRessourceId(currentRessource.getId());
            }

            Stage stage = new Stage();
            stage.setTitle("Ajouter un Commentaire");
            stage.setScene(new Scene(root));

            stage.setOnHidden(e -> {
                List<Commentaire> commentaires = commentaireService.getCommentairesParRessource(currentRessource.getId());
                afficherCommentaires(commentaires);
            });

            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Erreur lors de l'ouverture de la fenêtre des commentaires.");
            alert.showAndWait();
        }
    }

    private void afficherCommentaires(List<Commentaire> commentaires) {
        commentairesContainer.getChildren().clear();

        for (Commentaire c : commentaires) {
            Label contenuLabel = new Label("👤 " + c.getProprietereId() + ": " + c.getContenu());
            contenuLabel.setStyle("-fx-background-color: #ecf0f1; -fx-padding: 2px; -fx-font-size: 9px; -fx-background-radius: 2px;");
            contenuLabel.setWrapText(true);
            commentairesContainer.getChildren().add(contenuLabel);

            if (c.getReponse() != null && !c.getReponse().isEmpty()) {
                Label reponseLabel = new Label("↪ " + c.getReponse());
                reponseLabel.setStyle("-fx-background-color: #d6eaf8; -fx-padding: 1px 2px; -fx-font-style: italic; -fx-font-size: 8px; -fx-background-radius: 2px;");
                reponseLabel.setWrapText(true);
                commentairesContainer.getChildren().add(reponseLabel);
            }
        }
    }

    public void shutdown() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
        }
    }
}
