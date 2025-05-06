package org.example.controllers.Formation;

import javafx.application.HostServices;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.entities.Formation;

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
}
