package org.example.test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.utils.ThemeManager;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/emploi/AfficherEmploi.fxml")); // ✅ remplace par ton fichier .fxml

        try {
            Parent root = loader.load();
            Scene scene = new Scene(root, 1000, 640);

            // Applique le thème enregistrépuisqu’on démarre
            ThemeManager.applyTheme(scene);

            primaryStage.setScene(scene);
            primaryStage.setTitle("Interface Emploi");
            primaryStage.show();
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors du chargement de l'interface : " + e.getMessage(), e);
        }
    }

    public static void main(String[] args) {
        testOpenAIConnection();
        launch(args);
    }

    private static void testOpenAIConnection() {
        System.out.println("Test de connexion à l'API OpenAI...");
        try {
            URL url = new URL("https://api.openai.com");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            int responseCode = connection.getResponseCode();
            System.out.println("Code de réponse OpenAI: " + responseCode);
        } catch (Exception e) {
            System.err.println("Échec de connexion à l'API OpenAI: " + e.getMessage());
        }
    }


}
