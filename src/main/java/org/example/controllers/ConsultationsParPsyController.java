package org.example.controllers;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.entities.Consultation;
import org.example.entities.Psy;
import org.example.services.ServiceConsultation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.List;

public class ConsultationsParPsyController {

    @FXML private ListView<Consultation> consultationListView;
    @FXML private Label labelNomPsy;
    @FXML private Button btnRetour;


    private Psy psy;

    public void setPsy(Psy p) {
        this.psy = p;
        labelNomPsy.setText("Consultations de : " + p.getNom());
        afficherConsultations();
    }

    private void afficherConsultations() {
        try {
            List<Consultation> toutes = new ServiceConsultation().getList();
            ObservableList<Consultation> pourCePsy = FXCollections.observableArrayList();

            for (Consultation c : toutes) {
                if (c.getPsy() != null && c.getPsy().getId() == psy.getId()) {
                    pourCePsy.add(c);
                }
            }

            consultationListView.setItems(pourCePsy);

            consultationListView.setCellFactory(param -> new ListCell<>() {
                @Override
                protected void updateItem(Consultation c, boolean empty) {
                    super.updateItem(c, empty);
                    if (empty || c == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        Label label = new Label(c.getDate() + " à " + c.getTime() + " | " + c.getRaison());

                        ComboBox<String> statusCombo = new ComboBox<>();
                        statusCombo.getItems().addAll("en attente", "confirmé", "replanifier", "annuler", "terminé");
                        statusCombo.setValue(c.getStatus());

                        TextField excuseField = new TextField();
                        excuseField.setPromptText("Motif...");
                        excuseField.setVisible(c.getStatus().equals("replanifier") || c.getStatus().equals("annuler"));
                        excuseField.setText(c.getExcuse());

                        statusCombo.setOnAction(e -> {
                            String newStatus = statusCombo.getValue();
                            c.setStatus(newStatus);

                            boolean showExcuse = newStatus.equals("replanifier") || newStatus.equals("annuler");
                            excuseField.setVisible(showExcuse);

                            // vider l’excuse si pas nécessaire
                            if (!showExcuse) {
                                c.setExcuse(null);
                                try {
                                    new ServiceConsultation().modifier(c);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                        });

                        excuseField.setOnAction(e -> {
                            c.setExcuse(excuseField.getText());
                            try {
                                new ServiceConsultation().modifier(c);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        });

                        HBox hBox = new HBox(10, label, statusCombo, excuseField);
                        setGraphic(hBox);
                    }
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void retour() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherPsy.fxml"));
            Parent root = loader.load();
            btnRetour.getScene().setRoot(root); // 👈 Affiche la scène précédente dans la même fenêtre
        } catch (IOException e) {
            e.printStackTrace();
        }
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
