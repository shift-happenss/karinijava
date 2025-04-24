package tn.esprit.controllers.Formation;

import javafx.application.HostServices;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tn.esprit.entities.Formation;
import tn.esprit.services.ServiceFormation;

import java.io.IOException;
import java.util.Optional;

public class FormationItemController {
    @FXML
    private Label titreLabel;
    @FXML
    private Label descriptionLabel;
    @FXML
    private Label formateurLabel;
    @FXML private Button modifyButton;
    private Formation formation;
    private final ServiceFormation serviceFormation = new ServiceFormation();
    private HostServices hostServices;

    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }
    private displayFormationsController parentController;
    public void setFormation(Formation formation) {
        this.formation = formation;
        updateUI();
    }
    public void setParentController(displayFormationsController controller) {
        this.parentController = controller;
    }


    private void updateUI() {
        titreLabel.setText(formation.getTitre());
        descriptionLabel.setText(formation.getDescription());
        formateurLabel.setText("Formateur: " + formation.getFormateur());
    }
    @FXML
    private void handleModify() {
        try {
            // Load the modification form
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Formation/ModifyFormation.fxml"));
            Parent root = loader.load();

            // Get the controller and pass the formation to modify
            ModifyFormationController controller = loader.getController();
            controller.setFormationToModify(formation);
            controller.setParentController(parentController);

            // Switch to the modification scene
            Stage stage = (Stage) modifyButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier Formation");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDelete() {
        // Show confirmation dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer cette formation");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer '" + formation.getTitre() + "' ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Delete from database
                serviceFormation.supprimer(formation.getId());

                // Refresh parent view
                if (parentController != null) {
                    parentController.refreshFormations();
                }

                // Show success message
                showAlert("Succès", "Formation supprimée avec succès!");
            } catch (Exception e) {
                showAlert("Erreur", "Erreur lors de la suppression: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleViewDetails() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Formation/FormationDetails.fxml"));
            Parent root = loader.load();

            FormationDetailsController detailsController = loader.getController();
            detailsController.setFormation(formation);

            detailsController.setHostServices(this.hostServices);


            Stage detailsStage = new Stage();
            detailsStage.setScene(new Scene(root));
            detailsStage.setTitle("Détails de la Formation");
            detailsStage.initModality(Modality.APPLICATION_MODAL);
            detailsStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}