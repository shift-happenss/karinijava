package tn.esprit.entities;

public class Formation {
    private Integer id;

    private String titre;

    private String description;
    private String cible;

    private String formateur;

    private String etat ;

    private String url_video;

    private String url_image;

    private String url_fichier;
    private Categorie categorie;
    private int likes = 0;
    private int examen_id =0;

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getExamen_id() {
        return examen_id;
    }

    public void setExamen_id(int examen_id) {
        this.examen_id = examen_id;
    }

    private String contenuTexte;


    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public String getCible() {
        return cible;
    }

    public void setCible(String cible) {
        this.cible = cible;
    }

    public String getFormateur() {
        return formateur;
    }

    public void setFormateur(String formateur) {
        this.formateur = formateur;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String geturl_video() {
        return url_video;
    }

    public void seturl_video(String url_video) {
        this.url_video = url_video;
    }

    public String geturl_image() {
        return url_image;
    }

    public void seturl_image(String url_image) {
        this.url_image = url_image;
    }

    public String geturl_fichier() {
        return url_fichier;
    }

    public void seturl_fichier(String url_fichier) {
        this.url_fichier = url_fichier;
    }

    public String getContenuTexte() {
        return contenuTexte;
    }

    public void setContenuTexte(String contenuTexte) {
        this.contenuTexte = contenuTexte;
    }

    @Override
    public String toString() {
        return "Formation{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", formateur='" + formateur + '\'' +
                '}';
    }

}
