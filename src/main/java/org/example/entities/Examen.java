package org.example.entities;

public class Examen {
    private int id;
    private Cours cours;
    private String titre;
    private String description;
    private double note;

    // Constructeur avec id
    public Examen(int id, Cours cours, String titre, String description, double note) {
        this.id = id;
        this.cours = cours;
        this.titre = titre;
        this.description = description;
        this.note = note;
    }
    public Examen() {
    }

    // Constructeur sans id
    public Examen(Cours cours, String titre, String description, double note) {
        this.cours = cours;
        this.titre = titre;
        this.description = description;
        this.note = note;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cours getCours() {
        return cours;
    }

    public void setCours(Cours cours) {
        this.cours = cours;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getNote() {
        return note;
    }

    public void setNote(double note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "Examen{" +
                "id=" + id +
                ", cours=" + cours +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", note=" + note +
                '}';
    }
}
