package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import tn.esprit.entities.Emploi;
import tn.esprit.services.ServiceEmploi;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AfficherEmploiController {

    @FXML
    private VBox vboxEmplois;

    private ServiceEmploi serviceEmploi;

    public AfficherEmploiController() {
        try {
            serviceEmploi = new ServiceEmploi();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void initialize() {
        refresh();
    }

    public void refresh() {
        vboxEmplois.getChildren().clear();
        List<Emploi> emplois = serviceEmploi.afficherEmplois();

        for (Emploi emp : emplois) {
            HBox hbox = new HBox(10);
            hbox.setStyle("-fx-padding: 10; -fx-background-color: #f0f8ff; -fx-border-color: #FFD700; -fx-border-width: 2;");

            // Labels d'affichage (titre, description, propriétaire)
            Label titreLabel = new Label(emp.getTitre());
            titreLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #00008B;");

            Label descriptionLabel = new Label(emp.getDescription());
            descriptionLabel.setStyle("-fx-text-fill: #00008B;");

            Label ownerLabel = new Label("Propriétaire : " + emp.getProprietaireNom());
            ownerLabel.setStyle("-fx-text-fill: #00008B;");

            // Bouton Détails
            Button btnDetails = new Button("Détails");
            btnDetails.setStyle("-fx-background-color: #FFD700; -fx-text-fill: #00008B;");
            btnDetails.setOnAction(event -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/emploi/DetailsEmploi.fxml"));
                    AnchorPane root = loader.load();
                    DetailsEmploiController controller = loader.getController();
                    controller.setEmploi(emp);
                    Stage stage = new Stage();
                    stage.setTitle("Détails Emploi");
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });

            // Bouton Modifier
            Button btnModifier = new Button("Modifier");
            btnModifier.setStyle("-fx-background-color: #FFD700; -fx-text-fill: #00008B;");
            btnModifier.setOnAction(event -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/emploi/ModifierEmploi.fxml"));
                    AnchorPane root = loader.load();
                    ModifierEmploiController controller = loader.getController();
                    controller.setEmploi(emp);  // Appel de la méthode renommée setEmploi
                    controller.setOnEmploiModifie(() -> refresh());
                    Stage stage = new Stage();
                    stage.setTitle("Modifier Emploi");
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
                serviceEmploi.supprimerEmploi(emp.getId());
                refresh();
            });

            // Bouton Ajouter Événement
            Button btnAjouterEvenement = new Button("Ajouter événement");
            btnAjouterEvenement.setStyle("-fx-background-color: #FFD700; -fx-text-fill: #00008B;");
            btnAjouterEvenement.setOnAction(event -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/emploi/AjouterEvenementPourEmploi.fxml"));
                    AnchorPane root = loader.load();
                    AjouterEvenementPourEmploiController controller = loader.getController();
                    controller.setEmploi(emp); // on passe l'emploi en contexte
                    Stage stage = new Stage();
                    stage.setTitle("Ajouter des événements");
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });

            hbox.getChildren().addAll(titreLabel, descriptionLabel, ownerLabel, btnDetails, btnModifier, btnSupprimer, btnAjouterEvenement);
            vboxEmplois.getChildren().add(hbox);
        }
    }

    @FXML
    private void handleAjouterEmploi() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/emploi/AjouterEmploi.fxml"));
            AnchorPane root = loader.load();
            AjouterEmploiController controller = loader.getController();
            controller.setOnEmploiAjoute(() -> refresh());
            Stage stage = new Stage();
            stage.setTitle("Ajouter Emploi");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void handleVoirEvenements() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/evenement/AfficherEvenement.fxml"));
            AnchorPane root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Liste des Événements");
            stage.setScene(new Scene(root));
            stage.setWidth(800);
            stage.setHeight(700);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
