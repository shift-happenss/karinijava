package tn.esprit.controllers.Formation;

import javafx.application.HostServices;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tn.esprit.entities.Formation;

import java.io.IOException;

public class FormationCardFrontController {

    @FXML
    private Label titreLabel;
    @FXML
    private Label descriptionLabel;
    @FXML
    private Label formateurLabel;

    private Formation formation;
    private HostServices hostServices;
    @FXML
    private VBox cardContainer;
    @FXML
    private ImageView imageView;
    @FXML
    public void onHover() {
        cardContainer.setStyle(cardContainer.getStyle() + "; -fx-scale-x: 1.02; -fx-scale-y: 1.02; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 12, 0, 0, 6);");
    }

    @FXML
    public void onExit() {
        cardContainer.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-radius: 12; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 4);");
    }

    public void setFormation(Formation formation) {
        this.formation = formation;
        titreLabel.setText(formation.getTitre());
        descriptionLabel.setText(formation.getDescription());
        formateurLabel.setText("Formateur : " + formation.getFormateur());
    }

    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }

    @FXML
    private void handleVoirDetails() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Formation/FormationDetails.fxml"));
            Parent root = loader.load();

            FormationDetailsController controller = loader.getController();
            controller.setFormation(formation);
            controller.setHostServices(hostServices);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Détails de la Formation");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setData(Formation f) {
        titreLabel.setText(f.getTitre());
        descriptionLabel.setText(f.getDescription());
        formateurLabel.setText("Formateur : " + f.getFormateur());


        String imagePath = "/imageFormation.png";

        try {
            Image image = new Image(getClass().getResourceAsStream(imagePath));
            imageView.setImage(image);
        } catch (Exception e) {
            System.out.println("Erreur chargement image : " + e.getMessage());
        }
    }

}
