package org.example.entities;

import java.util.Objects;

public class Psy {
    private int id;
    private String nom;
    private int numerotel;
    private String mail;
    private String specialite;
    private String datedispo;
    private String timedispo;

public Psy() {}
    public Psy(int id, String nom, int numerotel, String mail, String specialite, String datedispo, String timedispo) {
        this.id = id;
        this.nom = nom;
        this.numerotel = numerotel;
        this.mail = mail;
        this.specialite = specialite;
        this.datedispo = datedispo;
        this.timedispo = timedispo;
    }

    public Psy(String nom, int numerotel, String mail, String specialite, String datedispo, String timedispo) {
        this.nom = nom;
        this.numerotel = numerotel;
        this.mail = mail;
        this.specialite = specialite;
        this.datedispo = datedispo;
        this.timedispo = timedispo;
    }

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

    public int getNumerotel() {
        return numerotel;
    }

    public void setNumerotel(int numerotel) {
        this.numerotel = numerotel;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getSpecialite() {
        return specialite;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }

    public String getDatedispo() {
        return datedispo;
    }

    public void setDatedispo(String datedispo) {
        this.datedispo = datedispo;
    }

    public String getTimedispo() {
        return timedispo;
    }

    public void setTimedispo(String timedispo) {
        this.timedispo = timedispo;
    }

    @Override
    public String toString() {
        return "Psy{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", numerotel=" + numerotel +
                ", mail='" + mail + '\'' +
                ", specialite='" + specialite + '\'' +
                ", datedispo='" + datedispo + '\'' +
                ", timedispo='" + timedispo + '\'' +
                '}';
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Psy psy = (Psy) obj;
        return id == psy.id; // Comparer par ID
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


}
