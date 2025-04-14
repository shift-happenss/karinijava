package tn.esprit.entities;

public class User {

    private int id;
    private String nom;

    // Constructeur vide
    public User() {
    }

    // Constructeur avec paramètres
    public User(int id, String nom) {
        this.id = id;
        this.nom = nom;
    }

    // Getters & Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                '}';
    }
}
