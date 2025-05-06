package org.example.controllers;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.entities.Psy;
import org.example.services.ServicePsy;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.List;

public class AffpsyController {

    @FXML private ListView<Psy> listViewPsy;
    @FXML private Button btnRetour;
    @FXML private TextField searchField;

    private ObservableList<Psy> observableList;


    @FXML
    public void initialize() {
        try {
            // Récupération des psy depuis le service
            List<Psy> psys = new ServicePsy().getList();
            observableList = FXCollections.observableArrayList(psys); // Initialisation de observableList

            // Ajout des éléments dans la ListView
            listViewPsy.setItems(observableList);

            listViewPsy.setCellFactory(param -> new ListCell<>() {
                @Override
                protected void updateItem(Psy p, boolean empty) {
                    super.updateItem(p, empty);
                    if (empty || p == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        Label label = new Label(p.getNom() + " | " + p.getSpecialite() + " | " + p.getMail()+ " | " + p.getNumerotel()+ " | " + p.getDatedispo()+ " | " + p.getTimedispo());


                        HBox hBox = new HBox(15, label);
                        setGraphic(hBox);
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void filterPsys() {
        String filterText = searchField.getText().toLowerCase();
        ObservableList<Psy> filteredList = FXCollections.observableArrayList();

        for (Psy p : observableList) {
            if (p.getNom().toLowerCase().contains(filterText) ||
                    p.getSpecialite().toLowerCase().contains(filterText) ||
                    p.getMail().toLowerCase().contains(filterText)) {
                filteredList.add(p);
            }
        }

        listViewPsy.setItems(filteredList);
    }

    @FXML
    private void sortPsys(String sortBy, boolean ascending) {
        FXCollections.sort(observableList, (p1, p2) -> {
            int comparisonResult = 0;

            if (sortBy.equals("name")) {
                comparisonResult = p1.getNom().compareTo(p2.getNom()); // Sorting by Name
            } else if (sortBy.equals("specialite")) {
                comparisonResult = p1.getSpecialite().compareTo(p2.getSpecialite()); // Sorting by Specialty
            }

            return ascending ? comparisonResult : -comparisonResult;
        });

        listViewPsy.setItems(observableList);
    }

    @FXML
    private void onSortByNameAsc() {
        sortPsys("name", true);  // Sort by name in ascending order
    }

    @FXML
    private void onSortByNameDesc() {
        sortPsys("name", false);  // Sort by name in descending order
    }

    @FXML
    private void onSortBySpecialiteAsc() {
        sortPsys("specialite", true);  // Sort by specialty in ascending order
    }

    @FXML
    private void onSortBySpecialiteDesc() {
        sortPsys("specialite", false);  // Sort by specialty in descending order
    }
    @FXML
    private void onSortByDateAsc() {
        sortPsys("date", true);  // Sort by date in ascending order
    }

    @FXML
    private void onSortByDateDesc() {
        sortPsys("date", false);  // Sort by date in descending order
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
