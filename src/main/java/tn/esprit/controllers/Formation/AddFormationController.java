package tn.esprit.controllers.Formation;


import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import tn.esprit.entities.Categorie;
import tn.esprit.entities.Formation;
import tn.esprit.services.ServiceCategorie;
import tn.esprit.services.ServiceFormation;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.scene.control.Button;

public class AddFormationController implements Initializable {

    @FXML private TextField titreField;
    @FXML private TextField descriptionField;
    @FXML private TextField cibleField;
    @FXML private TextField formateurField;
    @FXML private TextField urlVideoField;
    @FXML private TextField urlImageField;
    @FXML private TextField urlFichierField;
    @FXML private TextArea contenuTexteArea;
    @FXML private Button ajouterButton;
    @FXML private ComboBox<Categorie> categorieComboBox;

    @FXML
    private Label fileNameLabel;
    @FXML
    private Label imageFileLabel;
    private File selectedImageFile;

    private File selectedFile;
    ServiceFormation serviceFormation = new ServiceFormation();
    ServiceCategorie serviceCategorie=new ServiceCategorie();
    private displayFormationsController parentController;

    public void setParentController(displayFormationsController controller) {
        this.parentController = controller;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<Categorie> categories = serviceCategorie.getAll();// récupère toutes les catégories
        System.out.println(categories);
        categorieComboBox.setItems(FXCollections.observableArrayList(categories));

        // Afficher juste le nom dans la combo
        categorieComboBox.setConverter(new StringConverter<Categorie>() {
            @Override
            public String toString(Categorie categorie) {
                return categorie != null ? categorie.getName() : "";
            }

            @Override
            public Categorie fromString(String string) {
                return null; // pas nécessaire ici
            }
        });
    }
    @FXML
    private void handleBackButton(ActionEvent event) throws IOException {
        navigateBackToFormations();
    }

    private void navigateBackToFormations() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Formation/displayFormations.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        displayFormationsController controller = loader.getController();
        controller.refreshFormations();

        Stage stage = (Stage) ((Node) ajouterButton).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Liste des Formations");
    }


    @FXML
    private void ajouterFormation() {
        if (!validerSaisie()) {
            return;
        }
        try {
            Formation f = new Formation();
            f.setTitre(titreField.getText());
            f.setDescription(descriptionField.getText());
            f.setCible(cibleField.getText());
            f.setFormateur(formateurField.getText());
            f.seturl_video(urlVideoField.getText());

            String urlFichier = selectedFile != null ? selectedFile.getAbsolutePath() : "";
            String urlImage= selectedImageFile !=null ? selectedImageFile.getAbsolutePath() :"";
            // Vérifie si un fichier a bien été sélectionné (facultatif)
            if (urlFichier.isEmpty()) {
                System.out.println("Aucun fichier n'a été sélectionné.");
                return;
            }
            f.seturl_image(urlImage);
            f.seturl_fichier(urlFichier);
            f.setContenuTexte(contenuTexteArea.getText());
            f.setEtat("Non démarrée");

            // Exemple : ici on affecte une catégorie par son nom (ex: "Développement")
            /*Categorie categorie = new Categorie(8,"Développement", "Formation technique");
            ServiceCategorie sc=new ServiceCategorie();*/
            Categorie selectedCategorie = categorieComboBox.getValue();

            if (selectedCategorie == null) {
                showAlert("oups","Veuillez choisir une catégorie");
                return;
            }

            f.setCategorie(selectedCategorie);

            serviceFormation.ajouter(f);
            showAlert("Succès", "Formation ajoutée avec succès !");
            clearForm();
            if (parentController != null) {
                parentController.refreshFormations();
            }

            navigateBackToFormations();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de l'ajout de la formation.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }

    private void clearForm() {
        titreField.clear();
        descriptionField.clear();
        cibleField.clear();
        formateurField.clear();
        urlVideoField.clear();

        if (fileNameLabel != null) {
            fileNameLabel.setText("Aucun fichier sélectionné");
        }
        if (imageFileLabel != null) {
            imageFileLabel.setText("Aucune image sélectionnée");
        }

        selectedFile = null;
        selectedImageFile = null;
        if (categorieComboBox != null) {
            categorieComboBox.getSelectionModel().clearSelection();
        }

        contenuTexteArea.clear();
    }

    @FXML
    private void handleUploadFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir un fichier");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf"),
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"),
                new FileChooser.ExtensionFilter("Tous les fichiers", "*.*")
        );

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            selectedFile = file;
            fileNameLabel.setText(file.getName());
        }
    }
    @FXML
    private void choisirImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        File file = fileChooser.showOpenDialog(((Node) event.getSource()).getScene().getWindow());
        if (file != null) {
            selectedImageFile = file;
            imageFileLabel.setText(file.getName());
        }
    }

    private boolean validerSaisie() {
        StringBuilder erreurs = new StringBuilder();

        if (titreField.getText().trim().isEmpty()) {
            erreurs.append("- Le titre est obligatoire\n");
        }

        if (descriptionField.getText().trim().isEmpty()) {
            erreurs.append("- La description est obligatoire\n");
        }

        if (formateurField.getText().trim().isEmpty()) {
            erreurs.append("- Le nom du formateur est obligatoire\n");
        }

        if (cibleField.getText().trim().isEmpty()) {
            erreurs.append("- La cible est obligatoire\n");
        }

        if (categorieComboBox.getValue() == null) {
            erreurs.append("- Veuillez choisir une catégorie\n");
        }

        if (urlVideoField.getText() != null && !urlVideoField.getText().trim().isEmpty()) {
            if (!urlVideoField.getText().startsWith("http")) {
                erreurs.append("- L’URL de la vidéo doit commencer par http/https\n");
            }
        }

        if (erreurs.length() > 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText("Veuillez corriger les erreurs suivantes :");
            alert.setContentText(erreurs.toString());
            alert.showAndWait();
            return false;
        }

        return true;
    }


}

