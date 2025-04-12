package consultation.psy.controllers;

import consultation.psy.entities.Consultation;
import consultation.psy.services.ServiceConsultation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;


import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AfficherConsultationController {

    @FXML
    private ListView<String> consultationListView;
    @FXML private Button btnRetour;


    @FXML
    public void initialize() {
        ServiceConsultation service = new ServiceConsultation();
        try {
            List<Consultation> consultations = service.getList();

            ObservableList<String> observableList = FXCollections.observableArrayList();

            for (Consultation c : consultations) {
                String item = "📅 " + c.getDate() +
                        " à " + c.getTime() +
                        " | 🧠 " + c.getPsy().getNom() +
                        " | 📝 " + c.getRaison() +
                        " | Statut: " + c.getStatus();
                // ✅ Ajouter l'excuse si elle existe
                if (c.getExcuse() != null && !c.getExcuse().isEmpty()) {
                    item += " | 📌 Excuse: " + c.getExcuse();
                }

                observableList.add(item);
            }

            consultationListView.setItems(observableList);

            //gestion du double-clic
            consultationListView.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    int selectedIndex = consultationListView.getSelectionModel().getSelectedIndex();
                    if (selectedIndex != -1) {
                        try {
                            Consultation selected = new ServiceConsultation().getList().get(selectedIndex);

                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierConsultation.fxml"));
                            Parent root = loader.load();

                            ModifierConsultationController controller = loader.getController();
                            controller.setConsultation(selected);

                            Stage stage = new Stage();
                            stage.setTitle("Modifier Consultation");
                            stage.setScene(new Scene(root));
                            stage.showAndWait();

                            // 🔁 Recharge la liste après modification/suppression
                            initialize();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void allerVersPsy() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterPsy.fxml"));
            Parent root = loader.load();
            consultationListView.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Erreur de navigation : " + e.getMessage()).show();
        }
    }

    @FXML
    void retour() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterConsultation.fxml"));
            Parent root = loader.load();
            btnRetour.getScene().setRoot(root); // 👈 Affiche la scène précédente dans la même fenêtre
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





}
