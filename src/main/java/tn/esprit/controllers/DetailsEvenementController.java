package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import tn.esprit.entities.Evenement;

public class DetailsEvenementController {

    @FXML
    private Label lblNom;
    @FXML
    private Label lblType;
    @FXML
    private Label lblDateDebut;
    @FXML
    private Label lblDateFin;
    @FXML
    private Label lblContenu;
    @FXML
    private Label lblLieu;
    @FXML
    private Label lblLatitude;
    @FXML
    private Label lblLongitude;

    public void setEvenement(Evenement ev) {
        lblNom.setText(ev.getNom());
        lblType.setText("Type : " + ev.getType());
        lblDateDebut.setText("Début : " + ev.getDateDebut());
        lblDateFin.setText("Fin : " + ev.getDateFin());
        lblContenu.setText("Contenu : " + ev.getContenu());
        lblLieu.setText("Lieu : " + ev.getLieu());
        lblLatitude.setText("Latitude : " + ev.getLatitude());
        lblLongitude.setText("Longitude : " + ev.getLongitude());
    }

    @FXML
    private void handleRetour() {
        Stage stage = (Stage) lblNom.getScene().getWindow();
        stage.close();
    }
}
