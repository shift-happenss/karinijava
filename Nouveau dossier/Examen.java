package entities;

import javafx.beans.property.*;

public class Examen {
    private final IntegerProperty id;
    private final StringProperty titre;
    private final StringProperty description;
    private final DoubleProperty note;
    private final ObjectProperty<Cours> cours; // Clé étrangère vers Cours

    public Examen() {
        this.id = new SimpleIntegerProperty();
        this.titre = new SimpleStringProperty();
        this.description = new SimpleStringProperty();
        this.note = new SimpleDoubleProperty();
        this.cours = new SimpleObjectProperty<>();
    }

    public Examen(String titre, String description, double note, Cours cours) {
        this();
        this.titre.set(titre);
        this.description.set(description);
        this.note.set(note);
        this.cours.set(cours);
    }

    public Examen(int id, String titre, String description, double note, Cours cours) {
        this(titre, description, note, cours);
        this.id.set(id);
    }

    // === Getters et Setters classiques ===
    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public String getTitre() {
        return titre.get();
    }

    public void setTitre(String titre) {
        this.titre.set(titre);
    }

    public StringProperty titreProperty() {
        return titre;
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public double getNote() {
        return note.get();
    }

    public void setNote(double note) {
        this.note.set(note);
    }

    public DoubleProperty noteProperty() {
        return note;
    }

    public Cours getCours() {
        return cours.get();
    }

    public void setCours(Cours cours) {
        this.cours.set(cours);
    }

    public ObjectProperty<Cours> coursProperty() {
        return cours;
    }

    // === Ajout utile pour TableView ===
    public StringProperty coursNomProperty() {
        Cours coursValue = cours.get();
        if (coursValue != null && coursValue.titreProperty() != null) {
            return coursValue.titreProperty();
        } else {
            return new SimpleStringProperty("Aucun cours");
        }
    }

    @Override
    public String toString() {
        return titre.get() + " (" + note.get() + ") - " + (cours.get() != null ? cours.get().getTitre() : "Aucun cours");
    }
}
