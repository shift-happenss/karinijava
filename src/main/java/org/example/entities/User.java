package org.example.entities;

public class User {
    private int id;
    private String name;
    private String prenom;
    private String email;
    private String password;
    private long numtel;
    private String role;
    private String status = "inactive";  // valeur par défaut
    private String resetToken;
    private String urlImage;

    public User() {
    }
    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }


    public User(int id, String name, String prenom, String email, String password, long numtel, String role, String status, String resetToken, String urlImage) {
        this.id = id;
        this.name = name;
        this.prenom = prenom;
        this.email = email;
        this.password = password;
        this.numtel = numtel;
        this.role = role;
        this.status = status;
        this.resetToken = resetToken;
        this.urlImage = urlImage;
    }

    // Getters & Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getNumtel() {
        return numtel;
    }

    public void setNumtel(long numtel) {
        this.numtel = numtel;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", numtel=" + numtel +
                ", role='" + role + '\'' +
                ", status='" + status + '\'' +
                ", resetToken='" + resetToken + '\'' +
                ", urlImage='" + urlImage + '\'' +
                '}';
    }
}
