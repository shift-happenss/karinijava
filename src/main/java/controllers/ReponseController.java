package controllers;

import entities.Question;
import entities.Reponse;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import services.ReponseService;

import java.io.IOException;
import java.util.List;

public class ReponseController {

    @FXML
    private VBox reponseContainer;

    @FXML
    private Label labelQuestionContenu;

    private Question question;
    private final ReponseService reponseService = new ReponseService();

    // Méthode pour définir la question et afficher les réponses associées
    public void setQuestion(Question question) {
        this.question = question;

        // ✅ Utiliser getTexte() au lieu de getContenu()
        labelQuestionContenu.setText(question.getTexte());
        afficherReponses();
    }

    // Méthode pour afficher les réponses associées à la question
    private void afficherReponses() {
        reponseContainer.getChildren().clear();

        if (question == null) {
            Label label = new Label("❗ Aucune question sélectionnée.");
            label.setStyle("-fx-text-fill: white;");
            reponseContainer.getChildren().add(label);
            return;
        }

        List<Reponse> reponses = reponseService.getReponsesParQuestion(question);

        if (reponses.isEmpty()) {
            Label label = new Label("📭 Aucune réponse trouvée pour cette question.");
            label.setStyle("-fx-text-fill: white;");
            reponseContainer.getChildren().add(label);
            return;
        }

        // Affichage des réponses dans des boîtes stylées
        for (Reponse r : reponses) {
            VBox reponseBox = new VBox(5);
            reponseBox.setStyle("-fx-background-color: #2980b9; -fx-padding: 15; -fx-background-radius: 10;");

            Label texteLabel = new Label("📝 " + r.getTexte());
            texteLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");

            Label correcteLabel = new Label("✔ Est Correcte : " + r.getEstCorrecte());
            correcteLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

            Button btnModifier = new Button("✏ Modifier");
            Button btnSupprimer = new Button("🗑 Supprimer");
            btnModifier.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white;");
            btnSupprimer.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white;");

            // ✅ Action de suppression
            btnSupprimer.setOnAction(e -> {
                reponseService.supprimerReponse(r.getId());
                afficherReponses(); // Rafraîchir après suppression
            });

            // ✅ Action de modification
            btnModifier.setOnAction(e -> {
                ouvrirPopupModifier(r);
            });

            VBox buttonBox = new VBox(5, btnModifier, btnSupprimer);
            reponseBox.getChildren().addAll(texteLabel, correcteLabel, buttonBox);

            reponseContainer.getChildren().add(reponseBox);
        }
    }

    // Méthode pour ouvrir la popup d'ajout d'une réponse
    @FXML
    void ouvrirPopupAjout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/popup_ajout_reponse.fxml"));
            Parent root = loader.load();

            AjouterReponseController controller = loader.getController();
            controller.setIdQuestion(question.getId());

            Stage stage = new Stage();
            stage.setTitle("Ajouter une Réponse");
            stage.setScene(new Scene(root));

            // ✅ Quand la popup se ferme, rafraîchir l'affichage des réponses
            stage.setOnHiding(event -> afficherReponses());

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour ouvrir la popup de modification d'une réponse
    private void ouvrirPopupModifier(Reponse reponse) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/popup_modifier_reponse.fxml"));
            Parent root = loader.load();

            ModifierReponseController controller = loader.getController();
            controller.setReponse(reponse);  // Passer la réponse à modifier

            Stage stage = new Stage();
            stage.setTitle("Modifier la Réponse");
            stage.setScene(new Scene(root));

            // ✅ Rafraîchir les réponses après modification
            stage.setOnHiding(event -> afficherReponses());

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
