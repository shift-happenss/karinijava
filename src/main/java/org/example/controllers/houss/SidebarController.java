package org.example.controllers.houss;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SidebarController {

    @FXML
    private void goToCours() throws IOException {
        openWindow("/houss/cours_interface.fxml", "Cours");
    }

    @FXML
    private void goToExamens() throws IOException {
        openWindow("/houss/examen_interface.fxml", "Examens");
    }

    @FXML
    private void goToQuestions() throws IOException {
        openWindow("/houss/question_interface.fxml", "Questions");
    }

    @FXML
    private void goToReponses() throws IOException {
        openWindow("/houss/reponse_interface.fxml", "Réponses");
    }

    private void openWindow(String path, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.show();
    }
}
