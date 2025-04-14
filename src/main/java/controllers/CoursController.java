package controllers;

import entities.Cours;
import services.CoursService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

public class CoursController implements Initializable {

    @FXML
    private GridPane coursContainer;

    private CoursService coursService;

    public CoursController() {
        coursService = new CoursService();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        afficherCours();
    }

    public void afficherCours() {
        List<Cours> coursList = coursService.afficher();
        coursContainer.getChildren().clear();

        int row = 0;
        int col = 0;

        for (Cours c : coursList) {
            VBox card = new VBox();
            card.setSpacing(10);
            card.setStyle("-fx-background-color: #1e3c72; -fx-background-radius: 15; -fx-padding: 20; -fx-min-width: 280px; -fx-max-width: 280px; -fx-effect: dropshadow(gaussian, black, 8, 0, 0, 4);");

            Label titre = new Label("📘 Titre : " + c.getTitre());
            titre.setStyle("-fx-font-size: 16px; -fx-text-fill: white; -fx-font-weight: bold;");

            Label desc = new Label("📝 Description : " + c.getDescription());
            desc.setStyle("-fx-text-fill: white;");

            Label debut = new Label("🕒 Début : " + c.getDateDebut());
            debut.setStyle("-fx-text-fill: white;");

            Label fin = new Label("⏳ Fin : " + c.getDateFin());
            fin.setStyle("-fx-text-fill: white;");

            Button lienButton = new Button("🔗 Ouvrir le lien");
            lienButton.setStyle("-fx-background-color: #6c5ce7; -fx-text-fill: white; -fx-background-radius: 15; -fx-padding: 6 15;");
            lienButton.setOnAction(e -> {
                try {
                    Desktop.getDesktop().browse(new URI(c.getLien()));
                } catch (Exception ex) {
                    ex.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir le lien.");
                }
            });

            Button btnModif = new Button("✏️ Modifier");
            btnModif.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-background-radius: 10;");
            btnModif.setOnAction(e -> modifierCours(c));

            Button btnSuppr = new Button("🗑️ Supprimer");
            btnSuppr.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white; -fx-background-radius: 10;");
            btnSuppr.setOnAction(e -> supprimerCours(c));

            VBox btnBox = new VBox(10, lienButton, btnModif, btnSuppr);
            card.getChildren().addAll(titre, desc, debut, fin, btnBox);

            coursContainer.add(card, col, row);

            col++;
            if (col > 2) {
                col = 0;
                row++;
            }
        }
    }

    public void openAddCoursPopup() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/add_cours_popup.fxml"));
            Parent root = loader.load();

            AddCoursController addCoursController = loader.getController();
            addCoursController.setCoursController(this);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter un cours");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir la fenêtre d'ajout.");
        }
    }

    private void modifierCours(Cours cours) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/modifier_cours_popup.fxml"));
            Parent root = loader.load();

            ModifierCoursController controller = loader.getController();
            controller.setCours(cours, this);

            Stage stage = new Stage();
            stage.setTitle("Modifier le cours");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir la fenêtre de modification.");
        }
    }

    @FXML
    private void supprimerCours(Cours cours) {
        if (coursService.supprimer(cours.getId())) {
            afficherCours();
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Cours supprimé.");
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de la suppression.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    private void openExamenInterface() {
        try {
            // Charger le fichier FXML pour la page des examens
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/examen_interface.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène pour afficher l'interface des examens
            Stage stage = new Stage();
            stage.setTitle("Interface des Examens");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir la page des examens.");
        }
    }
}
