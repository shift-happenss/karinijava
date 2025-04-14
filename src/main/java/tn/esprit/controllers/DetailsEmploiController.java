package tn.esprit.controllers;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.scene.control.Separator;
import javafx.stage.Stage;
import javafx.util.Duration;
import tn.esprit.entities.Emploi;
import tn.esprit.entities.Evenement;
import tn.esprit.services.ServiceEmploiEvenement;
import tn.esprit.utils.MyDataBase;

import java.sql.Connection;
import java.util.List;

public class DetailsEmploiController {

    @FXML
    private Label lblTitre;
    @FXML
    private Label lblDescription;
    @FXML
    private Label lblProprietaire;
    @FXML
    private VBox vboxEvenements;

    private Emploi emploi;

    public void setEmploi(Emploi emp) {
        this.emploi = emp;
        lblTitre.setText(emp.getTitre());
        lblDescription.setText(emp.getDescription());
        if (emp.getProprietaireNom() != null) {
            lblProprietaire.setText("Propriétaire : " + emp.getProprietaireNom());
        } else {
            lblProprietaire.setText("Propriétaire ID : " + emp.getProprietaireId());
        }
        loadEvenements();
    }

    // Charger et afficher les événements associés à cet emploi avec un effet d'apparition
    private void loadEvenements() {
        Connection cnx = MyDataBase.getInstance().getMyConnection();
        ServiceEmploiEvenement serviceEE = new ServiceEmploiEvenement(cnx);
        List<Evenement> evenements = serviceEE.getEvenementsByEmploi(emploi.getId());

        // Nettoyer la zone d'affichage
        vboxEvenements.getChildren().clear();

        if (evenements.isEmpty()) {
            Label aucun = new Label("Aucun événement associé.");
            aucun.setStyle("-fx-font-size: 14px; -fx-text-fill: #00008B;");
            vboxEvenements.getChildren().add(aucun);
        } else {
            for (Evenement ev : evenements) {
                // Création d'un label avec style et quelques mots en gras
                Label lbl = new Label("Nom : " + ev.getNom() + " (" + ev.getType() + "), " +
                        "du : " + ev.getDateDebut() + " au : " + ev.getDateFin());
                lbl.setStyle("-fx-font-size: 14px; -fx-text-fill: #00008B;");
                // Par exemple, mettre en gras le type
                // Vous pouvez enrichir le style avec HTML-like markup dans un WebView ou utiliser une combinaison de Labels si besoin

                // Ajout du label dans le VBox
                vboxEvenements.getChildren().add(lbl);

                // Optionnel : ajouter un séparateur pour mieux distinguer chaque événement
                Separator sep = new Separator();
                vboxEvenements.getChildren().add(sep);

                // Animation fade-in pour le label
                FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), lbl);
                fadeTransition.setFromValue(0);
                fadeTransition.setToValue(1);
                fadeTransition.play();
            }
        }
    }

    @FXML
    private void handleRetour() {
        Stage stage = (Stage) lblTitre.getScene().getWindow();
        stage.close();
    }
}
