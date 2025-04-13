package tn.esprit.controllers.Categorie;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.entities.Categorie;
import tn.esprit.services.ServiceCategorie;

public class FormulaireCategorieController {

    @FXML
    private TextField nomField;
    @FXML
    private TextArea descriptionField;

    private Categorie categorie;
    private ListeCategoriesController listeCategoriesController;
    private final ServiceCategorie serviceCategorie = new ServiceCategorie();

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
        if (categorie != null) {
            nomField.setText(categorie.getName());
            descriptionField.setText(categorie.getDescription());
        }
    }

    public void setListeCategoriesController(ListeCategoriesController controller) {
        this.listeCategoriesController = controller;
    }

    @FXML
    private void enregistrer() {
        try {
            if (nomField.getText().isEmpty()) {
                afficherErreur("Le nom est obligatoire");
                return;
            }

            if (categorie == null) {
                // Ajout
                Categorie nouvelleCategorie = new Categorie(
                        nomField.getText(),
                        descriptionField.getText()
                );
                serviceCategorie.ajouter(nouvelleCategorie);
            } else {
                // Modification
                categorie.setName(nomField.getText());
                categorie.setDescription(descriptionField.getText());
                serviceCategorie.modifier(categorie);
            }

            listeCategoriesController.rafraichirListe();
            fermer();
        } catch (Exception e) {
            afficherErreur("Erreur lors de l'enregistrement: " + e.getMessage());
        }
    }

    @FXML
    private void annuler() {
        fermer();
    }

    private void fermer() {
        ((Stage) nomField.getScene().getWindow()).close();
    }

    private void afficherErreur(String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.ERROR
        );
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}