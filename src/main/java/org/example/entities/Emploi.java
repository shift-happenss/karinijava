package org.example.entities;

public class Emploi {

    private int id;
    private String titre;
    private String description;
    private int proprietaireId;
    private String proprietaireNom; // <-- Ajout

    // Constructeurs
    public Emploi() {}

    public Emploi(int id, String titre, String description, int proprietaireId) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.proprietaireId = proprietaireId;
    }

    public Emploi(String titre, String description, int proprietaireId) {
        this.titre = titre;
        this.description = description;
        this.proprietaireId = proprietaireId;
    }

    // Getters & Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getProprietaireId() {
        return proprietaireId;
    }

    public void setProprietaireId(int proprietaireId) {
        this.proprietaireId = proprietaireId;
    }

    public String getProprietaireNom() {
        return proprietaireNom;
    }

    public void setProprietaireNom(String proprietaireNom) {
        this.proprietaireNom = proprietaireNom;
    }

    @Override
    public String toString() {
        return "Emploi{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", proprietaireId=" + proprietaireId +
                ", proprietaireNom='" + proprietaireNom + '\'' +
                '}';
    }

}
