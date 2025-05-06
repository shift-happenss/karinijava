package org.example.controllers.Categorie;

import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.entities.Categorie;
import org.example.services.ServiceCategorie;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Comparator;

public class ListeCategoriesController {

    @FXML
    private VBox categoriesContainer;
    @FXML
    private ComboBox<String> triComboBox;

    private final ServiceCategorie serviceCategorie = new ServiceCategorie();
    @FXML
    private TextField searchField;

    private ObservableList<Categorie> allCategories = FXCollections.observableArrayList();
    private FilteredList<Categorie> filteredCategories;
    private SortedList<Categorie> sortedCategories;
    @FXML
    private VBox detailsBox;
    @FXML
    private Label labelNom;
    @FXML
    private Label labelDescription;

    @FXML
    public void initialize() {
        try {
            allCategories.addAll(serviceCategorie.getList());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        filteredCategories = new FilteredList<>(allCategories, p -> true);
        sortedCategories = new SortedList<>(filteredCategories);

        chargerCategories();

        searchField.textProperty().addListener((obs, oldValue, newValue) -> {
            filteredCategories.setPredicate(c -> {
                if (newValue == null || newValue.isEmpty()) return true;
                String lower = newValue.toLowerCase();
                return c.getName().toLowerCase().contains(lower);
            });
            chargerCategories();
        });

        // Ajout des options de tri
        triComboBox.setItems(FXCollections.observableArrayList(
                "Nom (A-Z)",
                "Nom (Z-A)"
        ));

        triComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                appliquerTri(newVal);
                chargerCategories(); // Très important pour rafraîchir l'affichage après tri
            }
        });
    }


    private void chargerCategories() {
        categoriesContainer.getChildren().clear();
        for (Categorie categorie : sortedCategories) {
            HBox card = creerCarteCategorie(categorie);
            categoriesContainer.getChildren().add(card);
        }
    }



    private HBox creerCarteCategorie(Categorie categorie) {
        HBox card = new HBox(10);
        card.getStyleClass().add("category-card");
        card.setPrefWidth(600);

        VBox infoBox = new VBox(5);
        Label nomLabel = new Label("Nom: " + categorie.getName());
        Label descLabel = new Label("Description: " + categorie.getDescription());
        infoBox.getChildren().addAll(nomLabel, descLabel);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox buttonBox = new HBox(5);
        Button modifierBtn = new Button("Modifier");
        modifierBtn.getStyleClass().add("btn-primary");
        modifierBtn.setOnAction(e -> modifierCategorie(categorie));

        Button supprimerBtn = new Button("Supprimer");
        supprimerBtn.getStyleClass().add("btn-danger");
        supprimerBtn.setOnAction(e -> supprimerCategorie(categorie));

        buttonBox.getChildren().addAll(modifierBtn, supprimerBtn);

        card.getChildren().addAll(infoBox, spacer, buttonBox);
        card.setOnMouseClicked(e -> afficherCategorieSelectionnee(categorie));

        return card;
    }

    @FXML
    private void ajouterCategorie() {
        ouvrirFormulaireCategorie(null);

    }

    private void modifierCategorie(Categorie categorie) {
        ouvrirFormulaireCategorie(categorie);
    }

    private void supprimerCategorie(Categorie categorie) {
        try {
            serviceCategorie.supprimer(categorie.getId());
            allCategories.remove(categorie);
            // Mettre à jour le filteredList et sortedList
            filteredCategories.setPredicate(null); // Réinitialiser le filtre
            chargerCategories(); // Rafraîchir l'affichage

            afficherAlerte("Succès", "Catégorie supprimée avec succès");
        } catch (Exception e) {
            afficherAlerte("Erreur", "Erreur lors de la suppression: " + e.getMessage());
        }
    }


    private void ouvrirFormulaireCategorie(Categorie categorie) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Categorie/FormulaireCategorie.fxml"));
            Parent root = loader.load();

            FormulaireCategorieController controller = loader.getController();
            controller.setCategorie(categorie);
            controller.setListeCategoriesController(this);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle(categorie == null ? "Ajouter Catégorie" : "Modifier Catégorie");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();rafraichirListe();
        } catch (IOException e) {
            afficherAlerte("Erreur", "Erreur lors de l'ouverture du formulaire");
        }
    }

    public void rafraichirListe() {
        try {
            allCategories.setAll(serviceCategorie.getList());
            filteredCategories.setPredicate(null); // Réinitialiser le filtre
            chargerCategories(); // Forcer le rafraîchissement de l'affichage
        } catch (SQLException e) {
            afficherAlerte("Erreur", "Erreur lors du rafraîchissement: " + e.getMessage());
        }
    }


    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void appliquerTri(String critere) {
        Comparator<Categorie> comparator = Comparator.comparing(Categorie::getName, String.CASE_INSENSITIVE_ORDER);

        if ("Nom (Z-A)".equals(critere)) {
            comparator = comparator.reversed();
        }

        sortedCategories.setComparator(comparator);
    }

    private void afficherCategorieSelectionnee(Categorie categorie) {
        labelNom.setText("Nom : " + categorie.getName());
        labelDescription.setText("Description : " + categorie.getDescription());

        if (!detailsBox.isVisible()) {
            detailsBox.setOpacity(0);
            detailsBox.setVisible(true);

            FadeTransition fade = new FadeTransition(Duration.millis(300), detailsBox);
            fade.setFromValue(0);
            fade.setToValue(1);
            fade.play();
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