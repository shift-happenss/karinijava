package entities;

public class ReponseSoumise {
    private int id;
    private Question question;
    private Reponse reponse;
    private String texte;
    private boolean estCorrect;

    public ReponseSoumise() {}

    public ReponseSoumise(Question question, Reponse reponse, String texte, boolean estCorrect) {
        this.question = question;
        this.reponse = reponse;
        this.texte = texte;
        this.estCorrect = estCorrect;
    }
    public ReponseSoumise(int id, String texte) {
        this.id = id;
        this.texte = texte;
    }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Question getQuestion() { return question; }
    public void setQuestion(Question question) { this.question = question; }

    public Reponse getReponse() { return reponse; }
    public void setReponse(Reponse reponse) { this.reponse = reponse; }

    public String getTexte() { return texte; }
    public void setTexte(String texte) { this.texte = texte; }

    public boolean isEstCorrect() { return estCorrect; }
    public void setEstCorrect(boolean estCorrect) { this.estCorrect = estCorrect; }
}