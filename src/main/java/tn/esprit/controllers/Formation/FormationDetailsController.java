package tn.esprit.controllers.Formation;

import javafx.application.HostServices;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebView;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import tn.esprit.entities.Formation;

import java.awt.Desktop;
import java.io.File;
import java.net.URI;

public class FormationDetailsController {

    @FXML private Label titreLabel;
    @FXML private Label categorieLabel;
    @FXML private Label formateurLabel;
    @FXML private Label cibleLabel;
    @FXML private Label etatLabel;
    @FXML private Hyperlink videoLink;
    @FXML private Hyperlink fichierLink;
    @FXML private ImageView formationImageView;
    @FXML private TextArea descriptionArea;
    @FXML private TextArea contenuArea;

    private Formation selectedFormation;
    private HostServices hostServices;

    public void setFormation(Formation formation) {
        this.selectedFormation = formation;
        populateDetails();
    }
    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }

    private void populateDetails() {
        if (selectedFormation == null) return;

        titreLabel.setText(selectedFormation.getTitre());
        categorieLabel.setText(selectedFormation.getCategorie().getName());
        formateurLabel.setText(selectedFormation.getFormateur());
        cibleLabel.setText(selectedFormation.getCible());
        etatLabel.setText(selectedFormation.getEtat());
        descriptionArea.setText(selectedFormation.getDescription());
        contenuArea.setText(selectedFormation.getContenuTexte());

        // Image
        if (selectedFormation.geturl_image() != null && !selectedFormation.geturl_image().isEmpty()) {
            File imageFile = new File(selectedFormation.geturl_image());
            if (imageFile.exists()) {
                formationImageView.setImage(new Image(imageFile.toURI().toString()));
            } else {
                System.out.println("Image introuvable : " + imageFile.getAbsolutePath());
            }
        }

        // Vidéo
        if (selectedFormation.geturl_video() != null && !selectedFormation.geturl_video().isEmpty()) {
            videoLink.setText("Voir sur YouTube");
        } else {
            videoLink.setVisible(false);
        }

        // Fichier
        if (selectedFormation.geturl_fichier() != null && !selectedFormation.geturl_fichier().isEmpty()) {
            fichierLink.setText(new File(selectedFormation.geturl_fichier()).getName());
        } else {
            fichierLink.setVisible(false);
        }
    }

    @FXML
    private void openVideoUrl() {
        try {
            Desktop.getDesktop().browse(new URI(selectedFormation.geturl_video()));
        } catch (Exception e) {
            showAlert("Erreur", "Impossible d'ouvrir la vidéo");
        }
    }

    @FXML
    private void showVideoPreview() {
        try {
            String videoId = extractYouTubeId(selectedFormation.geturl_video());
            String embedUrl = "https://www.youtube.com/embed/" + videoId + "?autoplay=1";
            String html = "<iframe width='800' height='450' src='" + embedUrl + "' frameborder='0' allowfullscreen></iframe>";

            WebView webView = new WebView();
            webView.getEngine().loadContent(html, "text/html");

            Stage stage = new Stage();
            stage.setScene(new Scene(new StackPane(webView), 820, 480));
            stage.setTitle("Aperçu vidéo");
            stage.show();
        } catch (Exception e) {
            showAlert("Erreur", "Impossible d'afficher la vidéo");
        }
    }

    private String extractYouTubeId(String url) {
        try {
            String[] parts = url.split("v=");
            String id = parts[1].split("&")[0];
            return id;
        } catch (Exception e) {
            return "";
        }
    }

    @FXML
    private void openFile() {
        try {
            File file = new File(selectedFormation.geturl_fichier());
            if (file.exists()) {
                Desktop.getDesktop().open(file);
            } else {
                showAlert("Erreur", "Fichier introuvable");
            }
        } catch (Exception e) {
            showAlert("Erreur", "Impossible d'ouvrir le fichier");
        }
    }

    @FXML
    private void handleBack() {
        ((Stage) titreLabel.getScene().getWindow()).close();
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
