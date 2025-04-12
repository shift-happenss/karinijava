package consultation.psy.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;

import java.io.IOException;

public class HomeController {

    @FXML
    void allerVersAdmin(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/DashboardAdmin.fxml"));
            ((Button) event.getSource()).getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void allerVersUtilisateur(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/DashboardUser.fxml"));
            ((Button) event.getSource()).getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
