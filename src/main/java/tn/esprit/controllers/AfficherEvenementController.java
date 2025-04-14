package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import tn.esprit.entities.Evenement;
import tn.esprit.services.ServiceEvenement;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AfficherEvenementController {

    @FXML
    private VBox vboxEvenements;

    private ServiceEvenement serviceEvenement;

    public AfficherEvenementController() {
        try {
            serviceEvenement = new ServiceEvenement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void initialize() {
        refresh();
    }

    public void refresh() {
        vboxEvenements.getChildren().clear();
        List<Evenement> evenements = serviceEvenement.afficherEvenements();

        for (Evenement ev : evenements) {
            HBox hbox = new HBox(10);
            hbox.setStyle("-fx-padding: 10; -fx-background-color: #f0f8ff; -fx-border-color: #FFD700; -fx-border-width: 2;");

            Label nomLabel = new Label(ev.getNom());
            nomLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #00008B;");

            Label typeLabel = new Label("Type: " + ev.getType());
            typeLabel.setStyle("-fx-text-fill: #00008B;");

            Label dateDebutLabel = new Label("Début: " + ev.getDateDebut());
            dateDebutLabel.setStyle("-fx-text-fill: #00008B;");

            Label dateFinLabel = new Label("Fin: " + ev.getDateFin());
            dateFinLabel.setStyle("-fx-text-fill: #00008B;");

            Label lieuLabel = new Label("Lieu: " + ev.getLieu());
            lieuLabel.setStyle("-fx-text-fill: #00008B;");

            // Bouton Détails
            Button btnDetails = new Button("Détails");
            btnDetails.setStyle("-fx-background-color: #FFD700; -fx-text-fill: #00008B;");
            btnDetails.setOnAction(event -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/evenement/DetailsEvenement.fxml"));
                    AnchorPane root = loader.load();
                    DetailsEvenementController controller = loader.getController();
                    controller.setEvenement(ev);
                    Stage stage = new Stage();
                    stage.setTitle("Détails Evenement");
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });

            // Bouton Modifier
            Button btnModifier = new Button("Modifier");
            btnModifier.setStyle("-fx-background-color: #FFD700; -fx-text-fill: #00008B;");
            // Bouton Modifier dans AfficherEvenementController, dans la boucle sur les événements
            btnModifier.setOnAction(event -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/evenement/ModifierEvenement.fxml"));
                    AnchorPane root = loader.load();
                    ModifierEvenementController controller = loader.getController();
                    controller.setEvenement(ev);
                    // Définir le callback pour actualiser la liste après modification
                    controller.setOnEvenementModifie(() -> {
                        System.out.println("Callback appelé : actualisation de la liste");
                        refresh();
                    });
                    Stage stage = new Stage();
                    stage.setTitle("Modifier Evenement");
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });


            // Bouton Supprimer
            Button btnSupprimer = new Button("Supprimer");
            btnSupprimer.setStyle("-fx-background-color: #FFD700; -fx-text-fill: #00008B;");
            btnSupprimer.setOnAction(event -> {
                serviceEvenement.supprimerEvenement(ev.getId());
                refresh();
            });

            hbox.getChildren().addAll(nomLabel, typeLabel, dateDebutLabel, dateFinLabel, lieuLabel, btnDetails, btnModifier, btnSupprimer);
            vboxEvenements.getChildren().add(hbox);
        }
    }

    @FXML
    private void handleAjouterEvenement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/evenement/AjouterEvenement.fxml"));
            AnchorPane root = loader.load();
            AjouterEvenementController controller = loader.getController();
            controller.setOnEvenementAjoute(() -> refresh());
            Stage stage = new Stage();
            stage.setTitle("Ajouter Evenement");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    @FXML
    private void handleRetourEmploi(javafx.event.ActionEvent event) {


            // ✅ Fermer la fenêtre actuelle (celle des événements)
            Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            currentStage.close();


    }

}
