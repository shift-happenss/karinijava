package controllers;

import entities.Cours;
import entities.Examen;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import services.CoursService;
import services.ExamenService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;


import java.io.IOException;


public class ExamenController {

    @FXML private TextField txtTitreExamen;
    @FXML private TextArea txtDescriptionExamen;
    @FXML private TextField txtNote;
    @FXML private ComboBox<Cours> comboCours;
    @FXML private TableView<Examen> tableView;
    @FXML private TableColumn<Examen, Number> idColumn;
    @FXML private TableColumn<Examen, String> titreColumn;
    @FXML private TableColumn<Examen, String> descriptionColumn;
    @FXML private TableColumn<Examen, Number> noteColumn;
    @FXML private TableColumn<Examen, String> coursColumn;

    private final ExamenService examenService = new ExamenService();
    private final CoursService coursService = new CoursService();
    private ObservableList<Examen> examens;

    private Examen selectedExamen = null;

    @FXML
    public void initialize() {
        comboCours.setItems(FXCollections.observableArrayList(coursService.listerCours()));

        idColumn.setCellValueFactory(data -> data.getValue().idProperty());
        titreColumn.setCellValueFactory(data -> data.getValue().titreProperty());
        descriptionColumn.setCellValueFactory(data -> data.getValue().descriptionProperty());
        noteColumn.setCellValueFactory(data -> data.getValue().noteProperty());
        coursColumn.setCellValueFactory(data -> data.getValue().getCours().titreProperty());

        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                selectedExamen = newSel;
                txtTitreExamen.setText(newSel.getTitre());
                txtDescriptionExamen.setText(newSel.getDescription());
                txtNote.setText(String.valueOf(newSel.getNote()));
                comboCours.setValue(newSel.getCours());
            }
        });

        afficherExamens();
    }

    private void afficherExamens() {
        examens = FXCollections.observableArrayList(examenService.listerExamens());
        tableView.setItems(examens);
    }

    @FXML
    private void ajouterExamen() {
        try {
            String titre = txtTitreExamen.getText();
            String description = txtDescriptionExamen.getText();
            double note = Double.parseDouble(txtNote.getText());
            Cours cours = comboCours.getValue();

            if (titre.isEmpty() || description.isEmpty() || cours == null) {
                showAlert("Tous les champs sont obligatoires.");
                return;
            }

            Examen examen = new Examen(titre, description, note, cours);
            examenService.ajouterExamen(examen);
            afficherExamens();
            clearFields();
        } catch (NumberFormatException e) {
            showAlert("Note invalide !");
        }
    }

    @FXML
    private void modifierExamen() {
        if (selectedExamen == null) {
            showAlert("Veuillez sélectionner un examen.");
            return;
        }

        try {
            selectedExamen.setTitre(txtTitreExamen.getText());
            selectedExamen.setDescription(txtDescriptionExamen.getText());
            selectedExamen.setNote(Double.parseDouble(txtNote.getText()));
            selectedExamen.setCours(comboCours.getValue());

            examenService.modifierExamen(selectedExamen);
            afficherExamens();
            clearFields();
        } catch (NumberFormatException e) {
            showAlert("Note invalide !");
        }
    }

    @FXML
    private void supprimerExamen() {
        if (selectedExamen == null) {
            showAlert("Veuillez sélectionner un examen.");
            return;
        }

        examenService.supprimerExamen(selectedExamen.getId());
        afficherExamens();
        clearFields();
    }

    private void clearFields() {
        txtTitreExamen.clear();
        txtDescriptionExamen.clear();
        txtNote.clear();
        comboCours.getSelectionModel().clearSelection();
        selectedExamen = null;
    }

    public void goToQuestionInterface(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/question_interface.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Interface Question");
            stage.setScene(new Scene(root));
            stage.show();

            // Optionnel : fermer la fenêtre actuelle
            ((Stage)(((Node) event.getSource()).getScene().getWindow())).close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    public void handleGoToCours(ActionEvent event) {
        try {
            // Chemin correct vers le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/cours_interface.fxml"));

            // Charger le fichier FXML
            AnchorPane root = loader.load();

            // Créer une nouvelle scène et l'afficher
            Stage stage = new Stage();
            stage.setTitle("Cours Interface");
            stage.setScene(new Scene(root));
            stage.show();

            // Fermer la fenêtre actuelle (optionnel)
            Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
