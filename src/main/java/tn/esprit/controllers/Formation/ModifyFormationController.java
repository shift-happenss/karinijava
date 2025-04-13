package tn.esprit.controllers.Formation;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import tn.esprit.entities.Categorie;
import tn.esprit.entities.Formation;
import tn.esprit.services.ServiceCategorie;
import tn.esprit.services.ServiceFormation;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ModifyFormationController implements Initializable {

    @FXML private TextField titreField;
    @FXML private ComboBox<Categorie> categorieComboBox;
    @FXML private TextField descriptionField;
    @FXML private TextField cibleField;
    @FXML private TextField formateurField;
    @FXML private TextField urlVideoField;
    @FXML private Label imagePathLabel;
    @FXML private Label filePathLabel;
    @FXML private TextArea contenuTextArea;

    private Formation formationToModify;
    private File selectedImageFile;
    private File selectedFile;
    private displayFormationsController parentController;

    private final ServiceFormation serviceFormation = new ServiceFormation();
    private final ServiceCategorie serviceCategorie = new ServiceCategorie();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize category combo box
        List<Categorie> categories = serviceCategorie.getAll();
        categorieComboBox.getItems().addAll(categories);

        categorieComboBox.setConverter(new StringConverter<Categorie>() {
            @Override
            public String toString(Categorie categorie) {
                return categorie != null ? categorie.getName() : "";
            }

            @Override
            public Categorie fromString(String string) {
                return null;
            }
        });
    }

    public void setFormationToModify(Formation formation) {
        this.formationToModify = formation;
        populateFields();
    }

    public void setParentController(displayFormationsController controller) {
        this.parentController = controller;
    }

    private void populateFields() {
        if (formationToModify != null) {
            titreField.setText(formationToModify.getTitre());
            descriptionField.setText(formationToModify.getDescription());
            cibleField.setText(formationToModify.getCible());
            formateurField.setText(formationToModify.getFormateur());
            urlVideoField.setText(formationToModify.geturl_video());
            contenuTextArea.setText(formationToModify.getContenuTexte());

            // Set category
            categorieComboBox.getSelectionModel().select(formationToModify.getCategorie());

            // Set file paths
            if (formationToModify.geturl_image() != null && !formationToModify.geturl_image().isEmpty()) {
                imagePathLabel.setText(new File(formationToModify.geturl_image()).getName());
            }
            if (formationToModify.geturl_fichier() != null && !formationToModify.geturl_fichier().isEmpty()) {
                filePathLabel.setText(new File(formationToModify.geturl_fichier()).getName());
            }
        }
    }

    @FXML
    private void handleSave(ActionEvent event) {
        if (!validateInputs()) {
            return;
        }

        try {
            // Update formation object
            formationToModify.setTitre(titreField.getText());
            formationToModify.setDescription(descriptionField.getText());
            formationToModify.setCible(cibleField.getText());
            formationToModify.setFormateur(formateurField.getText());
            formationToModify.seturl_video(urlVideoField.getText());
            formationToModify.setContenuTexte(contenuTextArea.getText());
            formationToModify.setCategorie(categorieComboBox.getValue());

            // Update files if new ones were selected
            if (selectedImageFile != null) {
                formationToModify.seturl_image(selectedImageFile.getAbsolutePath());
            }
            if (selectedFile != null) {
                formationToModify.seturl_fichier(selectedFile.getAbsolutePath());
            }

            // Save to database
            serviceFormation.modifier(formationToModify);

            showAlert("Succès", "Formation modifiée avec succès!");
            navigateBackToList();

        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de la modification: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleImageSelection(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
        );

        File file = fileChooser.showOpenDialog(((Node) event.getSource()).getScene().getWindow());
        if (file != null) {
            selectedImageFile = file;
            imagePathLabel.setText(file.getName());
        }
    }

    @FXML
    private void handleFileSelection(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir un fichier");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PDF", "*.pdf"),
                new FileChooser.ExtensionFilter("Tous les fichiers", "*.*")
        );

        File file = fileChooser.showOpenDialog(((Node) event.getSource()).getScene().getWindow());
        if (file != null) {
            selectedFile = file;
            filePathLabel.setText(file.getName());
        }
    }

    @FXML
    private void handleBackButton(ActionEvent event) throws IOException {
        navigateBackToList();
    }

    private void navigateBackToList() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Formation/displayFormations.fxml"));
        Parent root = loader.load();

        displayFormationsController controller = loader.getController();
        controller.refreshFormations();

        Stage stage = (Stage) titreField.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Liste des Formations");
    }

    private boolean validateInputs() {
        StringBuilder errors = new StringBuilder();

        if (titreField.getText().trim().isEmpty()) {
            errors.append("- Le titre est obligatoire\n");
        }
        if (descriptionField.getText().trim().isEmpty()) {
            errors.append("- La description est obligatoire\n");
        }
        if (formateurField.getText().trim().isEmpty()) {
            errors.append("- Le formateur est obligatoire\n");
        }
        if (categorieComboBox.getValue() == null) {
            errors.append("- La catégorie est obligatoire\n");
        }

        if (errors.length() > 0) {
            showAlert("Erreur de validation", errors.toString());
            return false;
        }
        return true;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}