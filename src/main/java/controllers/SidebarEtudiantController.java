package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SidebarEtudiantController {

    @FXML
    void goToMesCours(ActionEvent event) {
        loadScene(event, "/fxml/interface_cours_etudiant.fxml");
    }

    @FXML
    void goToMesExamens(ActionEvent event) {
        loadScene(event, "/fxml/examen_etudiant_interface.fxml");
    }



    private void loadScene(ActionEvent event, String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.out.println("Erreur de chargement de l'interface : " + fxmlPath);
            e.printStackTrace();
        }
    }
}
