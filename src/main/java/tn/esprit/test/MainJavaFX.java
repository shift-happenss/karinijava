package tn.esprit.test;

import javafx.stage.Stage;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import javafx.scene.Parent;
public class MainJavaFX extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        //Parent root = FXMLLoader.load(getClass().getResource("/Categorie/ListeCategories.fxml"));
        Parent root = FXMLLoader.load(getClass().getResource("/Formation/displayFormationsFront.fxml"));

        Scene scene = new Scene(root);
        stage.setTitle("Ajouter Formation");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}