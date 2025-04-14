package entities;

import java.util.Objects;

public class Question {
    private int id;
    private int examenId;
    private String texte;
    private String type;

    public Question(int id, int examenId, String texte, String type) {
        this.id = id;
        this.examenId = examenId;
        this.texte = texte;
        this.type = type;
    }

    public Question(int examenId, String texte, String type) {
        this.examenId = examenId;
        this.texte = texte;
        this.type = type;
    }

    public Question() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getExamenId() {
        return examenId;
    }

    public void setExamenId(int examenId) {
        this.examenId = examenId;
    }

    public String getTexte() {
        return texte;
    }

    public void setTexte(String texte) {
        this.texte = texte;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return texte + " (" + type + ")";
    }
}