package tn.esprit;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // On stocke le contenu FXML dans une variable "root"
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/AfficherRessource.fxml")));

            Scene scene = new Scene(root);
            primaryStage.setTitle("Post");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "Erreur lors du chargement du fichier FXML", ex);
        } catch (NullPointerException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "FXML introuvable : chemin incorrect ?", ex);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
