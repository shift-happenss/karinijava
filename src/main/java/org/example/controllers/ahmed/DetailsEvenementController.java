package org.example.controllers.ahmed;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.entities.Evenement;

public class DetailsEvenementController {

    @FXML private Label lblNom;
    @FXML private Label lblType;
    @FXML private Label lblDateDebut;
    @FXML private Label lblDateFin;
    @FXML private Label lblContenu;
    @FXML private Label lblLieu;
    @FXML private Label lblLatitude;
    @FXML private Label lblLongitude;
    @FXML private Button btnMap;
    @FXML
    private ImageView imgEvent;

    private Evenement evenement;

    public void setEvenement(Evenement ev) {
        this.evenement = ev;
        lblNom.setText(ev.getNom());
        lblType.setText(ev.getType()); // Retirer "Type : "
        lblDateDebut.setText(ev.getDateDebut().toString()); // Formater si nécessaire
        lblDateFin.setText(ev.getDateFin().toString());
        lblContenu.setText(ev.getContenu());
        lblLieu.setText(ev.getLieu());
        lblLatitude.setText(String.valueOf(ev.getLatitude())); // Conversion directe
        lblLongitude.setText(String.valueOf(ev.getLongitude()));

        if(ev.getImageUrl() != null) {
            imgEvent.setImage(new Image(ev.getImageUrl()));
        }
    }

    @FXML
    private void handleRetour() {
        Stage stage = (Stage) lblNom.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleMap() {
        double lat = evenement.getLatitude();
        double lng = evenement.getLongitude();

        // Création de la WebView
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();

        // Chargement de la page HTML avec paramètres
        String url = getClass()
                .getResource("/map/openst.html")
                .toExternalForm() + "?lat=" + lat + "&lng=" + lng;
        webEngine.load(url);

        // Fenêtre secondaire
        BorderPane root = new BorderPane(webView);
        Stage mapStage = new Stage();
        mapStage.initOwner(btnMap.getScene().getWindow());
        mapStage.initModality(Modality.WINDOW_MODAL);
        mapStage.setTitle("Localisation de l'événement");
        mapStage.setScene(new Scene(root, 1000, 640));
        mapStage.show();
    }

}
