package entities;

public class Question {
    private int id;
    private Examen examen; // Association objet
    private String texte;
    private String type;

    // Constructeurs
    public Question(int id, Examen examen, String texte, String type) {
        this.id = id;
        this.examen = examen;
        this.texte = texte;
        this.type = type;
    }

    public Question(Examen examen, String texte, String type) {
        this.examen = examen;
        this.texte = texte;
        this.type = type;
    }

    public Question() {}

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Examen getExamen() { return examen; }
    public void setExamen(Examen examen) { this.examen = examen; }

    public String getTexte() { return texte; }
    public void setTexte(String texte) { this.texte = texte; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", examen=" + (examen != null ? examen.getId() : "null") +
                ", texte='" + texte + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

}
