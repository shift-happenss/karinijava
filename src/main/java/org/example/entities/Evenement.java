package org.example.entities;

import java.time.LocalDate;

public class Evenement {

    private int id;
    private String nom;
    private String type;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String contenu;
    private String lieu;
    private double latitude;
    private double longitude;
    private String imageUrl;       // <<< nouveau


    // Constructeur vide
    public Evenement() {
    }

    // Constructeur avec id (pour les objets récupérés de la base)
    public Evenement(int id, String nom, String type, LocalDate dateDebut, LocalDate dateFin, String contenu, String lieu, double latitude, double longitude,String imageUrl) {
        this.id = id;
        this.nom = nom;
        this.type = type;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.contenu = contenu;
        this.lieu = lieu;
        this.latitude = latitude;
        this.longitude = longitude;
        this.imageUrl = imageUrl;
    }

    // Constructeur sans id (pour les ajouts)
    public Evenement(String nom, String type, LocalDate dateDebut, LocalDate dateFin, String contenu, String lieu, double latitude, double longitude,String imageUrl) {
        this.nom = nom;
        this.type = type;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.contenu = contenu;
        this.lieu = lieu;
        this.latitude = latitude;
        this.longitude = longitude;
        this.imageUrl = imageUrl;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    @Override
    public String toString() {
        return "Evenement{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", type='" + type + '\'' +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", contenu='" + contenu + '\'' +
                ", lieu='" + lieu + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
