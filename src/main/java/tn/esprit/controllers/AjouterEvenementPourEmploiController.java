package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tn.esprit.entities.Emploi;
import tn.esprit.entities.Evenement;
import tn.esprit.services.ServiceEvenement;
import tn.esprit.services.ServiceEmploiEvenement;
import tn.esprit.utils.MyDataBase;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AjouterEvenementPourEmploiController {

    @FXML
    private VBox vboxEvenements;
    @FXML
    private Button btnValider;

    private Emploi emploi;
    private ServiceEvenement serviceEvenement;
    private ServiceEmploiEvenement serviceEmploiEvenement;

    // Méthode pour recevoir l'emploi concerné
    public void setEmploi(Emploi emploi) {
        this.emploi = emploi;
        loadEvenements();
    }

    // Charger tous les événements disponibles dans des CheckBox
    private void loadEvenements() {
        try {
            serviceEvenement = new ServiceEvenement();
            serviceEmploiEvenement = new ServiceEmploiEvenement(MyDataBase.getInstance().getMyConnection());

            List<Evenement> evenements = serviceEvenement.afficherEvenements();

            // On vide le conteneur sauf le bouton valider déjà placé en dernier
            List<Node> nodes = new ArrayList<>(vboxEvenements.getChildren());
            nodes.remove(btnValider);
            vboxEvenements.getChildren().removeAll(nodes);

            for (Evenement ev : evenements) {
                CheckBox cb = new CheckBox(ev.getNom());
                cb.setUserData(ev);
                // Ajout au début pour garder le bouton en bas
                vboxEvenements.getChildren().add(0, cb);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleValider() {
        List<Evenement> selectedEvenements = new ArrayList<>();
        // Parcourir le conteneur pour récupérer les CheckBox sélectionnées
        for (Node node : vboxEvenements.getChildren()) {
            if (node instanceof CheckBox) {
                CheckBox cb = (CheckBox) node;
                if (cb.isSelected()) {
                    selectedEvenements.add((Evenement) cb.getUserData());
                }
            }
        }
        if (!selectedEvenements.isEmpty()) {
            serviceEmploiEvenement.addEvenementsToEmploi(emploi.getId(), selectedEvenements);
        }
        // Fermer la fenêtre d'ajout
        Stage stage = (Stage) btnValider.getScene().getWindow();
        stage.close();
    }
}
