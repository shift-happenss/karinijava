package org.example.entities;

import java.io.Serializable;

public class Ressource implements Serializable {
    private int id;
    private int categoryId;
    private String titre;
    private String description;
    private String type;
    private String urlVideo;
    private String urlImage;
    private String urlFichier;
    private String contenuTexte;

    public Ressource() {
    }

    public Ressource(int id, int categoryId, String titre, String description, String type,
                     String urlVideo, String urlImage, String urlFichier, String contenuTexte) {
        this.id = id;
        this.categoryId = categoryId;
        this.titre = titre;
        this.description = description;
        this.type = type;
        this.urlVideo = urlVideo;
        this.urlImage = urlImage;
        this.urlFichier = urlFichier;
        this.contenuTexte = contenuTexte;
    }

    // Getters & Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrlVideo() {
        return urlVideo;
    }

    public void setUrlVideo(String urlVideo) {
        this.urlVideo = urlVideo;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getUrlFichier() {
        return urlFichier;
    }

    public void setUrlFichier(String urlFichier) {
        this.urlFichier = urlFichier;
    }

    public String getContenuTexte() {
        return contenuTexte;
    }

    public void setContenuTexte(String contenuTexte) {
        this.contenuTexte = contenuTexte;
    }

    @Override
    public String toString() {
        return "Ressource {" +
                "id=" + id +
                ", categoryId=" + categoryId +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", type='" + type + '\'' +
                ", urlVideo='" + urlVideo + '\'' +
                ", urlImage='" + urlImage + '\'' +
                ", urlFichier='" + urlFichier + '\'' +
                ", contenuTexte='" + contenuTexte + '\'' +
                '}';
    }
}
