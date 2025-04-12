package consultation.psy.controllers;

import consultation.psy.entities.Psy;
import consultation.psy.services.ServicePsy;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class AfficherPsyController {

    @FXML private ListView<Psy> listViewPsy;
    @FXML private Button btnRetour;


    @FXML
    public void initialize() {
        try {
            // Vider la liste avant de recharger
            listViewPsy.getItems().clear();

            List<Psy> psys = new ServicePsy().getList();
            listViewPsy.getItems().addAll(psys);

            listViewPsy.setCellFactory(param -> new ListCell<>() {
                @Override
                protected void updateItem(Psy p, boolean empty) {
                    super.updateItem(p, empty);
                    if (empty || p == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        Label label = new Label(p.getNom() + " | " + p.getSpecialite() + " | " + p.getMail());
                        Button btnModifier = new Button("Modifier");
                        Button btnDetails = new Button("Détails");

                        btnModifier.setOnAction(e -> modifierPsy(p));
                        btnDetails.setOnAction(e -> ouvrirConsultationsDuPsy(p));

                        HBox hBox = new HBox(15, label, btnModifier, btnDetails);
                        setGraphic(hBox);
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void modifierPsy(Psy p) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierPsy.fxml"));
            Parent root = loader.load();
            ModifierPsyController controller = loader.getController();
            controller.setPsy(p);
            Stage stage = new Stage();
            stage.setTitle("Modifier Psy");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            initialize(); // recharge la liste après modification

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ouvrirConsultationsDuPsy(Psy p) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ConsultationsParPsy.fxml"));
            Parent root = loader.load();
            ConsultationsParPsyController controller = loader.getController();
            controller.setPsy(p);
            Stage stage = new Stage();
            stage.setTitle("Consultations de " + p.getNom());
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void retour() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterPsy.fxml"));
            Parent root = loader.load();
            btnRetour.getScene().setRoot(root); // 👈 Affiche la scène précédente dans la même fenêtre
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void retourAccueil(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Home.fxml"));
            ((Button) event.getSource()).getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
