package org.example.entities;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Reponse {
    private int id;
    private Question question;
    private final StringProperty texte = new SimpleStringProperty();
    private final StringProperty estCorrecte = new SimpleStringProperty();

    public Reponse() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public String getTexte() {
        return texte.get();
    }

    public void setTexte(String texte) {
        this.texte.set(texte);
    }

    public StringProperty texteProperty() {
        return texte;
    }

    public String getEstCorrecte() {
        return estCorrecte.get();
    }

    public void setEstCorrecte(String estCorrecte) {
        this.estCorrecte.set(estCorrecte);
    }

    public StringProperty estCorrecteProperty() {
        return estCorrecte;
    }
}
