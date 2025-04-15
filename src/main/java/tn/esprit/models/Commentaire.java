package tn.esprit.models;

import java.io.Serializable;

public class Commentaire implements Serializable {
    private int id;
    private Integer proprietereId;  // Nullable dans la base, donc Integer au lieu de int
    private Integer ressourceId;    // Nullable aussi
    private String contenu;
    private String reponse;

    public Commentaire() {
    }

    public Commentaire(int id, Integer proprietereId, Integer ressourceId, String contenu, String reponse) {
        this.id = id;
        this.proprietereId = proprietereId;
        this.ressourceId = ressourceId;
        this.contenu = contenu;
        this.reponse = reponse;
    }

    // Getters & Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getProprietereId() {
        return proprietereId;
    }

    public void setProprietereId(Integer proprietereId) {
        this.proprietereId = proprietereId;
    }

    public Integer getRessourceId() {
        return ressourceId;
    }

    public void setRessourceId(Integer ressourceId) {
        this.ressourceId = ressourceId;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public String getReponse() {
        return reponse;
    }

    public void setReponse(String reponse) {
        this.reponse = reponse;
    }

    @Override
    public String toString() {
        return "Commentaire{" +
                "id=" + id +
                ", proprietereId=" + proprietereId +
                ", ressourceId=" + ressourceId +
                ", contenu='" + contenu + '\'' +
                ", reponse='" + reponse + '\'' +
                '}';
    }
}
