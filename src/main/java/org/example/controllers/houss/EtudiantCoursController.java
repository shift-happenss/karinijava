package org.example.controllers.houss;

import org.example.entities.Cours;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.example.services.CoursService;

import java.awt.*;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class EtudiantCoursController implements Initializable {

    @FXML
    private GridPane coursContainer;

    private final CoursService coursService = new CoursService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        coursContainer.setPrefWidth(Region.USE_COMPUTED_SIZE);
        coursContainer.setMaxWidth(Region.USE_COMPUTED_SIZE);
        coursContainer.setMinWidth(Region.USE_COMPUTED_SIZE);

        afficherCours();
    }


    private void afficherCours() {
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
                    URI uri = new URI(c.getLien());
                    if (Desktop.isDesktopSupported()) {
                        Desktop.getDesktop().browse(uri);
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir le lien. L'application Desktop n'est pas supportée.");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir le lien.");
                }
            });

            card.getChildren().addAll(titre, desc, debut, fin, lienButton);
            coursContainer.add(card, col, row);

            col++;
            if (col > 2) { // Ajoute des éléments sur la ligne suivante après 3 éléments
                col = 0;
                row++;
            }
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
    private void goToExamens() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/houss/examen_etudiant_interface.fxml"));
            javafx.scene.Parent root = loader.load();
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Examens disponibles");
            stage.setScene(new javafx.scene.Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d’ouvrir l’interface des examens.");
        }
    }
}
