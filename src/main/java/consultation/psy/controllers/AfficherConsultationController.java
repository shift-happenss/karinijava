package consultation.psy.controllers;

import consultation.psy.entities.Consultation;
import consultation.psy.services.ServiceConsultation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AfficherConsultationController {

    @FXML private ListView<Consultation> consultationListView; // Utilisation de Consultation ici
    @FXML private Button btnRetour;
    @FXML private TextField searchField;
    @FXML private DatePicker datePicker;

    private ObservableList<Consultation> observableList; // ObservableList de Consultation

    @FXML
    public void initialize() {
        ServiceConsultation service = new ServiceConsultation();
        try {
            observableList = FXCollections.observableArrayList();

            List<Consultation> consultations = service.getList();

            // Remplissage de la observableList avec des objets Consultation
            observableList.addAll(consultations);

            // Paramétrer la ListView pour afficher les données de consultation
            consultationListView.setItems(observableList);
            datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    trierParDateSelectionnee(newValue);
                } else {
                    consultationListView.setItems(observableList); // réinitialiser si on efface la date
                }
            });

            // Configuration des cellules pour afficher les détails de chaque consultation
            consultationListView.setCellFactory(param -> new ListCell<Consultation>() {
                @Override
                protected void updateItem(Consultation consultation, boolean empty) {
                    super.updateItem(consultation, empty);
                    if (empty || consultation == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        String displayText = "📅 " + consultation.getDate() +
                                " à " + consultation.getTime() +
                                " | 🧠 " + consultation.getPsy().getNom() +
                                " | 📝 " + consultation.getRaison() +
                                " | Statut: " + consultation.getStatus();

                        // Ajouter l'excuse si elle existe
                        if (consultation.getExcuse() != null && !consultation.getExcuse().isEmpty()) {
                            displayText += " | 📌 Excuse: " + consultation.getExcuse();
                        }

                        setText(displayText); // Affichage sous forme de texte
                    }
                }
            });

            // Gestion du double clic sur une consultation
            consultationListView.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    Consultation selectedConsultation = consultationListView.getSelectionModel().getSelectedItem();
                    if (selectedConsultation != null) {
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierConsultation.fxml"));
                            Parent root = loader.load();
                            ModifierConsultationController controller = loader.getController();
                            controller.setConsultation(selectedConsultation);

                            Stage stage = new Stage();
                            stage.setTitle("Modifier Consultation");
                            stage.setScene(new Scene(root));
                            stage.showAndWait();

                            // Recharge la liste après modification
                            initialize();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Filtrage des consultations en fonction du texte dans le champ de recherche
    @FXML
    private void filterConsultations() {
        String filterText = searchField.getText().toLowerCase();
        if (filterText.isEmpty()) {
            consultationListView.setItems(observableList);
            return;
        }

        ObservableList<Consultation> filteredList = FXCollections.observableArrayList();

        for (Consultation c : observableList) {
            if (c.getDate().toLowerCase().contains(filterText) ||
                    c.getTime().toLowerCase().contains(filterText) ||
                    c.getRaison().toLowerCase().contains(filterText) ||
                    c.getStatus().toLowerCase().contains(filterText) ||
                    (c.getPsy() != null && c.getPsy().getNom().toLowerCase().contains(filterText)) ||
                    (c.getExcuse() != null && c.getExcuse().toLowerCase().contains(filterText))) {
                filteredList.add(c);
            }
        }


        // Mettre à jour les éléments de la ListView avec les résultats filtrés
        consultationListView.setItems(filteredList);
    }

    @FXML
    private void sortConsultations(String sortBy, boolean ascending) {
        FXCollections.sort(observableList, (c1, c2) -> {
            int comparisonResult = 0;

            if (sortBy.equals("date")) {
                comparisonResult = c1.getDate().compareTo(c2.getDate()); // Sorting by Date
            } else if (sortBy.equals("psy")) {
                comparisonResult = c1.getPsy().getNom().compareTo(c2.getPsy().getNom()); // Sorting by Psy Name
            }

            return ascending ? comparisonResult : -comparisonResult;
        });

        consultationListView.setItems(observableList);
    }

    @FXML
    private void onSortByDateAsc() {
        sortConsultations("date", true);  // Sort by date in ascending order
    }

    @FXML
    private void onSortByDateDesc() {
        sortConsultations("date", false);  // Sort by date in descending order
    }

    @FXML
    private void onSortByRaisonAsc() {
        sortConsultations("psy", true);  // Sort by Psy name in ascending order
    }

    @FXML
    private void onSortByRaisonDesc() {
        sortConsultations("psy", false);  // Sort by Psy name in descending order
    }


    // Naviguer vers la page de gestion des psychologues
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

    // Retour à l'écran précédent
    @FXML
    void retour() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterConsultation.fxml"));
            Parent root = loader.load();
            btnRetour.getScene().setRoot(root); // Affiche la scène précédente dans la même fenêtre
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void trierParDateSelectionnee(java.time.LocalDate dateChoisie) {
        ObservableList<Consultation> sortedList = FXCollections.observableArrayList();

        for (Consultation c : observableList) {
            try {
                java.time.LocalDate dateConsultation = java.time.LocalDate.parse(c.getDate());
                if (dateConsultation.equals(dateChoisie)) {
                    sortedList.add(c);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        consultationListView.setItems(sortedList);
    }

}
