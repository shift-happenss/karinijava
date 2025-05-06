package org.example.controllers.houss;

import org.example.entities.Question;
import org.example.entities.Reponse;
import org.example.entities.ReponseSoumise;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.example.services.QuestionService;
import org.example.services.ReponseSoumiseService;

import java.util.List;

public class PasserExamenController {

    @FXML private Label questionLabel;
    @FXML private TextField reponseField;
    @FXML private Label scoreLabel;

    private List<Question> questions;
    private int index = 0;
    private int score = 0;
    private int examenId;
    private Question questionActuelle;

    private final QuestionService qs = new QuestionService();
    private final ReponseSoumiseService rss = new ReponseSoumiseService();

    public void setExamenId(int id) {
        this.examenId = id;
        questions = qs.getQuestionsByExamenId(id);
        afficherQuestion();
    }

    private void afficherQuestion() {
        if (index < questions.size()) {
            questionActuelle = questions.get(index);
            questionLabel.setText(questionActuelle.getTexte());
            reponseField.clear();
        } else {
            scoreLabel.setText("Votre note : " + score + "/" + questions.size());
        }
    }

    @FXML
    private void soumettreReponse() {
        String texteSoumis = reponseField.getText();
        Reponse bonneReponse = rss.getBonneReponseByQuestionId(questionActuelle.getId()); // Utilisation correcte
        boolean estCorrect = rss.estBonneReponse(bonneReponse.getId(), texteSoumis);
        if (estCorrect) score++;

        ReponseSoumise r = new ReponseSoumise(questionActuelle, bonneReponse, texteSoumis, estCorrect);
        rss.ajouterReponseSoumise(r);

        index++;
        afficherQuestion();
    }
}
