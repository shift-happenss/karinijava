package tn.esprit.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import tn.esprit.models.Ressource;
import tn.esprit.services.RessourceService;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AfficherRessourceController implements Initializable {

    @FXML
    private ListView<Ressource> listViewRessources;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Create the RessourceService and fetch the list of resources
        RessourceService service = new RessourceService();
        List<Ressource> ressources = service.afficherRessources();

        // Add resources to the ListView
        listViewRessources.getItems().addAll(ressources);

        // Display formatted info in each cell of the ListView
        listViewRessources.setCellFactory(param -> new ListCell<Ressource>() {
            @Override
            protected void updateItem(Ressource ressource, boolean empty) {
                super.updateItem(ressource, empty);
                if (empty || ressource == null) {
                    setText(null);
                    setGraphic(null); // No graphic when empty
                } else {
                    // Create an ImageView to display the image
                    ImageView imageView = new ImageView();
                    if (ressource.getUrlImage() != null && !ressource.getUrlImage().isEmpty()) {
                        try {
                            // Load the image using the URL (use "file:" for local files)
                            String imageUrl = "file:" + ressource.getUrlImage(); // Ensure you are using the correct path
                            Image image = new Image(imageUrl);
                            imageView.setImage(image);
                            imageView.setFitWidth(50); // Set the width of the image
                            imageView.setFitHeight(50); // Set the height of the image
                        } catch (Exception e) {
                            // Handle any image loading exceptions
                            imageView.setImage(null); // Set to null if image fails to load
                            System.out.println("Failed to load image: " + ressource.getUrlImage());
                        }
                    } else {
                        imageView.setImage(null); // Handle case when there is no image URL
                    }

                    // Format the display text for each resource
                    String displayText = String.format(
                            """
                            📝 Titre        : %s
                            📄 Description  : %s
                            📘 Contenu      : %s
                            """,
                            ressource.getTitre(),
                            ressource.getDescription(),
                            ressource.getContenuTexte() != null ? ressource.getContenuTexte() : "N/A"
                    );

                    // Set the text and graphic (image)
                    setText(displayText);
                    setGraphic(imageView); // Display the image as the graphic of the cell

                    // Style the cell
                    setStyle("-fx-padding: 10px; -fx-font-size: 13px; -fx-border-color: #ccc; -fx-border-radius: 5px;");
                }
            }
        });

        // Handle double-click event on the ListView
        listViewRessources.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Double-click to open details
                Ressource selected = listViewRessources.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    openShowRessourceWindow(selected); // Open the detailed view
                }
            }
        });
    }

    // Method to open the ShowRessource window and pass the selected resource to the new window
    private void openShowRessourceWindow(Ressource ressource) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/showfrontRessource.fxml"));
            Parent root = loader.load();

            // Pass the selected ressource to the ShowFrontRessourceController
            ShowFrontRessourceController controller = loader.getController();
            controller.setRessource(ressource);

            // Create a new stage and show the detailed window
            Stage stage = new Stage();
            stage.setTitle("Détail de la ressource");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
