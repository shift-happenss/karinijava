package org.example.controllers.ahmed;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.entities.Evenement;
import org.example.services.ServiceEvenement;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ToggleButton;
import org.example.utils.ThemeManager;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AfficherEvenementController {

    @FXML
    private VBox vboxEvenements;

    private ServiceEvenement serviceEvenement;

    @FXML
    private ComboBox<String> sortComboBox;
    @FXML
    private ToggleButton orderToggle;



    @FXML private Button themeToggleButton;

    public AfficherEvenementController() {
        try {
            serviceEvenement = new ServiceEvenement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void initialize() {

        // peupler la ComboBox
        sortComboBox.getItems().addAll(
                "Date de début",
                "Date de fin",
                "Nom"
                // vous pouvez ajouter “Type”, “Lieu”, etc.
        );
        sortComboBox.setValue("Date de début"); // valeur par défaut

        // écouteurs : relancer le tri
        sortComboBox.setOnAction(e -> refresh());
        orderToggle.setOnAction(e -> {
            if (orderToggle.isSelected()) {
                orderToggle.setText("Descendant");
            } else {
                orderToggle.setText("Ascendant");
            }
            refresh();
        });
        Platform.runLater(() -> {
            Scene scene = themeToggleButton.getScene();
            if (scene != null) {
                ThemeManager.applyTheme(scene);
                updateButtonText();
            }
        });
        refresh();
    }
    @FXML
    private void onToggleTheme(ActionEvent event) {
        Scene scene = ((Button) event.getSource()).getScene();
        ThemeManager.toggle(scene);
        updateButtonText();
    }

    private void updateButtonText() {
        if (ThemeManager.isDark()) {
            themeToggleButton.setText("☀️ Mode clair");
        } else {
            themeToggleButton.setText("🌙 Mode sombre");
        }
    }

    /**
     * Trie la liste selon le critère et l'ordre sélectionnés.
     */
    private void applySorting(List<Evenement> list) {
        Comparator<Evenement> comp;
        switch (sortComboBox.getValue()) {
            case "Date de fin":
                comp = Comparator.comparing(Evenement::getDateFin);
                break;
            case "Nom":
                comp = Comparator.comparing(Evenement::getNom, String.CASE_INSENSITIVE_ORDER);
                break;
            case "Date de début":
            default:
                comp = Comparator.comparing(Evenement::getDateDebut);
                break;
        }
        list.sort(comp);
        // inverser si mode descendant
        if (orderToggle.isSelected()) {
            Collections.reverse(list);
        }
    }


    public void refresh() {
        vboxEvenements.getChildren().clear();
        List<Evenement> evenements = serviceEvenement.afficherEvenements();

        // --- NOUVEAU : appliquer le tri ---
        applySorting(evenements);


        for (Evenement ev : evenements) {
            HBox hbox = new HBox(10);
            hbox.setStyle("-fx-padding: 10; -fx-background-color: #f0f8ff; -fx-border-color: #FFD700; -fx-border-width: 2;");

            // Partie gauche : Informations de l'événement
            VBox infoBox = new VBox(5);
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
            infoBox.getChildren().addAll(nomLabel, typeLabel, dateDebutLabel, dateFinLabel, lieuLabel);
            // Permet à infoBox d'occuper tout l'espace horizontal disponible
            HBox.setHgrow(infoBox, Priority.ALWAYS);

            // Partie droite : Boutons d'action
            HBox btnBox = new HBox(5);

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
                    stage.setScene(new Scene(root, 1000, 640));
                    stage.show();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });

            Button btnModifier = new Button("Modifier");
            btnModifier.setStyle("-fx-background-color: #FFD700; -fx-text-fill: #00008B;");
            btnModifier.setOnAction(event -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/evenement/ModifierEvenement.fxml"));
                    AnchorPane root = loader.load();
                    ModifierEvenementController controller = loader.getController();
                    controller.setEvenement(ev);
                    controller.setOnEvenementModifie(() -> {
                        System.out.println("Callback appelé : actualisation de la liste");
                        refresh();
                    });
                    Stage stage = new Stage();
                    stage.setTitle("Modifier Evenement");
                    stage.setScene(new Scene(root, 1000, 640));
                    stage.show();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });

            Button btnSupprimer = new Button("Supprimer");
            btnSupprimer.setStyle("-fx-background-color: #FFD700; -fx-text-fill: #00008B;");
            btnSupprimer.setOnAction(event -> {
                serviceEvenement.supprimerEvenement(ev.getId());
                refresh();
            });

            btnBox.getChildren().addAll(btnDetails, btnModifier, btnSupprimer);

            // Ajouter les deux conteneurs à la ligne
            hbox.getChildren().addAll(infoBox, btnBox);
            vboxEvenements.getChildren().add(hbox);
        }
    }


    @FXML
    private void handleAjouterEvenement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/evenement/AjouterEvenement.fxml"));
            AnchorPane root = loader.load();
            AjouterEvenementController controller = loader.getController();
            controller.setOnEvenementAjoute(() -> {
                refresh();
            });
            Stage stage = new Stage();
            stage.setTitle("Ajouter Evenement");
            stage.setScene(new Scene(root, 1000, 640));
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    @FXML
    private void handleRetourEmploi(ActionEvent event) {


            // ✅ Fermer la fenêtre actuelle (celle des événements)
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();


    }
    @FXML
    public void allerVersadmin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin.fxml")); // adapte le chemin
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();


            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
