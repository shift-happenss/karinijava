package controllers;

import entities.Reponse;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.ReponseService;

public class AjouterReponseController {

    @FXML
    private TextField reponseField;  // Zone de texte pour la réponse (texte + info sur estCorrecte)

    private int idQuestion;  // ID de la question associée

    private final ReponseService reponseService = new ReponseService();

    public void setIdQuestion(int idQuestion) {
        this.idQuestion = idQuestion;
    }

    @FXML
    public void ajouterReponse() {
        // Récupérer le texte de la réponse
        String texteReponse = reponseField.getText();

        // Déterminer si la réponse est correcte en détectant "true" ou "false"
        String estCorrecte = texteReponse.contains("true") ? "true" : "false";

        // Créer et remplir l'objet réponse
        Reponse reponse = new Reponse();
        reponse.setTexte(texteReponse);
        reponse.setEstCorrecte(estCorrecte);

        // Ajouter la réponse
        reponseService.ajouterReponse(reponse, idQuestion);

        // 🔒 Fermer la fenêtre après ajout
        Stage stage = (Stage) reponseField.getScene().getWindow();
        stage.close();
    }
}
