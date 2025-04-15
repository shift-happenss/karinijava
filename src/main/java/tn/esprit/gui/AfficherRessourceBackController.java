package tn.esprit.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import tn.esprit.models.Ressource;
import tn.esprit.services.RessourceService;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AfficherRessourceBackController implements Initializable {

    @FXML
    private ListView<Ressource> listViewRessources;

    private RessourceService ressourceService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ressourceService = new RessourceService();
        loadResources();

        // Handle double-click on a list item to open details
        listViewRessources.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Double-click
                Ressource selectedRessource = listViewRessources.getSelectionModel().getSelectedItem();
                if (selectedRessource != null) {
                    openShowRessourcePage(selectedRessource);
                }
            }
        });
    }

    private void loadResources() {
        List<Ressource> ressources = ressourceService.afficherRessources();

        if (ressources == null || ressources.isEmpty()) {
            listViewRessources.setPlaceholder(new Label("Aucune ressource disponible"));
        } else {
            ObservableList<Ressource> observableRessources = FXCollections.observableArrayList(ressources);
            listViewRessources.setItems(observableRessources);
        }

        // Personnalisation des cellules de la ListView
        listViewRessources.setCellFactory(param -> new ListCell<Ressource>() {
            @Override
            protected void updateItem(Ressource ressource, boolean empty) {
                super.updateItem(ressource, empty);
                if (empty || ressource == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    ImageView imageView = new ImageView();
                    if (ressource.getUrlImage() != null && !ressource.getUrlImage().isEmpty()) {
                        try {
                            String imageUrl = "file:" + ressource.getUrlImage(); // Corriger le chemin si nécessaire
                            Image image = new Image(imageUrl, 50, 50, true, true);
                            imageView.setImage(image);
                        } catch (Exception e) {
                            System.out.println("Erreur de chargement image : " + ressource.getUrlImage());
                            imageView.setImage(null);
                        }
                    }

                    // Affichage formaté
                    String displayText = String.format("""
                    📝 Titre        : %s
                    📄 Description  : %s
                    📘 Contenu      : %s
                    """,
                            ressource.getTitre(),
                            ressource.getDescription(),
                            ressource.getContenuTexte() != null ? ressource.getContenuTexte() : "N/A"
                    );

                    setText(displayText);
                    setGraphic(imageView);
                    setStyle("-fx-padding: 10px; -fx-font-size: 13px; -fx-border-color: #ccc; -fx-border-radius: 5px;");
                }
            }
        });
    }


    private void openShowRessourcePage(Ressource ressource) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/showRessource.fxml"));
            Parent root = loader.load();

            // Pass the selected resource to the detail controller
            ShowRessourceController controller = loader.getController();
            controller.initialize(ressource);

            Stage stage = new Stage();
            stage.setTitle("Détails de la Ressource");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur de chargement");
            alert.setContentText("Impossible de charger la page showRessource.fxml.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleCreateRessourceClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterRessource.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Créer une Ressource");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur de chargement");
            alert.setContentText("Impossible de charger la page AjouterRessource.fxml.");
            alert.showAndWait();
        }
    }

    @FXML
    public void handleRefreshClick() {
        loadResources();
    }

}