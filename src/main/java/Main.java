import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;  // Utilise VBox ici au lieu de AnchorPane
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Charger le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/interface_cours_etudiant.fxml")); // Assurez-vous que le chemin est correct
            VBox root = loader.load();  // Charge un VBox ici


            // Initialiser la scène
            primaryStage.setTitle("Application Cours");
            primaryStage.setScene(new Scene(root, 800, 600)); // Vous pouvez ajuster les dimensions ici si nécessaire
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();  // Affiche les erreur s si le fichier FXML ne peut pas être chargé
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
