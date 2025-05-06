package org.example.controllers.houss;

import org.example.entities.Examen;
import org.example.entities.Question;
import org.example.entities.Reponse;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.services.ExamenService;
import org.example.services.QuestionService;
import org.example.services.ReponseService;

import java.util.*;

public class QuizController {

    @FXML
    private VBox questionsContainer;

    @FXML
    private Button submitButton;

    private final QuestionService questionService = new QuestionService();
    private final ReponseService reponseService = new ReponseService();
    private final ExamenService examenService = new ExamenService();

    private final Map<Question, Object> userResponses = new HashMap<>();
    private Examen examen;

    public void setExamen(Examen examen) {
        this.examen = examen;
        loadQuestions();
    }

    private void loadQuestions() {
        if (examen == null) return;

        questionsContainer.getChildren().clear();
        List<Question> questions = questionService.getQuestionsByExamen(examen.getId());

        for (Question q : questions) {
            VBox questionBox = new VBox(10);
            questionBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.9); -fx-padding: 15; -fx-background-radius: 8;");

            Label typeLabel = new Label("Type : " + q.getType());
            typeLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #666;");

            Label questionLabel = new Label(q.getTexte());
            questionLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #333;");
            questionLabel.setWrapText(true);

            questionBox.getChildren().addAll(typeLabel, questionLabel);

            switch (q.getType().toLowerCase()) {
                case "choix multiple" -> {
                    VBox checkboxGroup = new VBox(5);
                    List<CheckBox> checkBoxes = new ArrayList<>();
                    List<Reponse> options = reponseService.getReponsesByQuestionId(q.getId());

                    for (Reponse r : options) {
                        CheckBox cb = new CheckBox(r.getTexte());
                        cb.setStyle("-fx-text-fill: #333; -fx-font-size: 14px;");
                        cb.setWrapText(true);
                        checkBoxes.add(cb);
                        checkboxGroup.getChildren().add(cb);
                    }

                    userResponses.put(q, checkBoxes);
                    questionBox.getChildren().add(checkboxGroup);
                }

                case "vrai/faux" -> {
                    ToggleGroup toggleGroup = new ToggleGroup();
                    HBox radioContainer = new HBox(10);

                    RadioButton vrai = new RadioButton("Vrai");
                    vrai.setStyle("-fx-text-fill: #333; -fx-font-size: 14px;");
                    vrai.setToggleGroup(toggleGroup);
                    vrai.setUserData("vrai");

                    RadioButton faux = new RadioButton("Faux");
                    faux.setStyle("-fx-text-fill: #333; -fx-font-size: 14px;");
                    faux.setToggleGroup(toggleGroup);
                    faux.setUserData("faux");

                    radioContainer.getChildren().addAll(vrai, faux);
                    userResponses.put(q, toggleGroup);
                    questionBox.getChildren().add(radioContainer);
                }

                case "texte", "réponse ouverte" -> {
                    TextField tf = new TextField();
                    tf.setStyle("-fx-font-size: 14px;");
                    tf.setPromptText("Votre réponse...");
                    userResponses.put(q, tf);
                    questionBox.getChildren().add(tf);
                }
            }

            questionsContainer.getChildren().add(questionBox);
        }
    }

    @FXML
    private void handleSubmit() {
        if (examen == null) return;

        int correctAnswers = 0;
        int totalQuestions = userResponses.size();

        for (Map.Entry<Question, Object> entry : userResponses.entrySet()) {
            Question question = entry.getKey();
            Object responseWidget = entry.getValue();

            List<Reponse> correctReps = reponseService.getBonneReponseByQuestionId(question.getId());
            Set<String> expectedAnswers = new HashSet<>();
            for (Reponse r : correctReps) {
                if ("vrai".equalsIgnoreCase(r.getEstCorrecte())) {
                    expectedAnswers.add(r.getTexte().trim().toLowerCase());
                }
            }

            switch (question.getType().toLowerCase()) {
                case "choix multiple" -> {
                    List<CheckBox> checkBoxes = (List<CheckBox>) responseWidget;
                    Set<String> selectedAnswers = new HashSet<>();

                    for (CheckBox cb : checkBoxes) {
                        if (cb.isSelected()) {
                            selectedAnswers.add(cb.getText().trim().toLowerCase());
                        }
                    }

                    if (selectedAnswers.equals(expectedAnswers)) {
                        correctAnswers++;
                    }

                    for (String rep : selectedAnswers) {
                        reponseService.ajouterReponseSoumise(question.getId(), rep);
                    }
                }

                case "vrai/faux" -> {
                    ToggleGroup group = (ToggleGroup) responseWidget;
                    if (group.getSelectedToggle() != null) {
                        String selectedValue = group.getSelectedToggle().getUserData().toString().toLowerCase();
                        if (!correctReps.isEmpty() &&
                                correctReps.get(0).getEstCorrecte().equalsIgnoreCase(selectedValue)) {
                            correctAnswers++;
                        }
                        reponseService.ajouterReponseSoumise(question.getId(), selectedValue);
                    }
                }

                case "texte", "réponse ouverte" -> {
                    TextField tf = (TextField) responseWidget;
                    String answerText = tf.getText().trim().toLowerCase();

                    if (!correctReps.isEmpty() &&
                            correctReps.get(0).getTexte().trim().toLowerCase().equals(answerText)) {
                        correctAnswers++;
                    }

                    reponseService.ajouterReponseSoumise(question.getId(), answerText);
                }
            }
        }

        double score = totalQuestions > 0 ? ((double) correctAnswers / totalQuestions) * 20 : 0;
        examenService.updateNoteForExam(examen.getId(), (int) Math.round(score));

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Résultat");
        alert.setHeaderText("Examen terminé !");
        alert.setContentText(String.format("✅ Votre note est : %.1f/20 (%d/%d réponses correctes)",
                score, correctAnswers, totalQuestions));
        alert.showAndWait();

        Stage stage = (Stage) submitButton.getScene().getWindow();
        stage.close();
    }
}
