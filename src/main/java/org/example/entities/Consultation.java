package org.example.entities;

public class Consultation {
    private int id;
    private String date;
    private String time;
    private String raison;
    private String status;
    private String excuse = "";
    private Psy psy;
    //private String numeroTel="+21651980220";

    public Consultation() {}

    /*public String getNumeroTel() {
        return numeroTel;
    }

    public void setNumeroTel(String numeroTel) {
        this.numeroTel = numeroTel;
    }
*/
    public Consultation(int id, String date, String time, String raison, String status, String excuse, Psy psy) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.raison = raison;
        this.status = status;
        this.excuse = ""; // toujours vide sauf modification par psy
        this.psy = psy;
    }
    public Consultation(String date, String time, String raison, String status, String excuse, Psy psy) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.raison = raison;
        this.status = status;
        this.excuse = ""; // toujours vide sauf modification par psy
        this.psy = psy;
    }
    public Consultation(int id, String date, String time, String raison, String status, String excuse) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.raison = raison;
        this.status = status;
        this.excuse = ""; // toujours vide sauf modification par psy

    }

    public Consultation(String date, String time, String raison, String status, String excuse) {
        this.date = date;
        this.time = time;
        this.raison = raison;
        this.status = status;
        this.excuse = ""; // toujours vide sauf modification par psy
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRaison() {
        return raison;
    }

    public void setRaison(String raison) {
        this.raison = raison;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getExcuse() {
        return excuse;
    }

    public void setExcuse(String excuse) {
        this.excuse = excuse;
    }

    public Psy getPsy() {
        return psy;
    }

    public void setPsy(Psy psy) {
        this.psy = psy;
    }

    @Override
    public String toString() {
        return "Consultation{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", raison='" + raison + '\'' +
                ", status='" + status + '\'' +
                ", excuse='" + excuse + '\'' +
                ", psy=" + psy +
                '}';
    }
}
