package org.example.controllers.ahmed;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.entities.*;
import org.example.services.*;
import org.example.utils.MyDataBase;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AjouterEvenementPourEmploiController {

    @FXML private VBox vboxEvenements;
    @FXML private VBox vboxCours;
    @FXML private VBox vboxExamens;
    @FXML private VBox vboxFormations;
    @FXML private Button btnValider;

    private Emploi emploi;
    private Connection cnx;

    // Services
    private ServiceEvenement srvEv;
    private ServiceEmploiEvenement srvEmplEv;
    private ServiceEmploiCours srvEmplCours;
    private ServiceEmploiExamen srvEmplExam;
    private ServiceEmploiFormation srvEmplForm;
    private CoursService srvCours;
    private ExamenService srvExam;
    private ServiceFormation srvForm;

    public void setEmploi(Emploi emploi) {
        this.emploi = emploi;
        try {
            cnx = MyDataBase.getInstance().getMyConnection();
            srvEv       = new ServiceEvenement();
            srvCours    = new CoursService();
            srvExam     = new ExamenService();
            srvForm     = new ServiceFormation();
            srvEmplEv   = new ServiceEmploiEvenement(cnx);
            srvEmplCours= new ServiceEmploiCours(cnx);
            srvEmplExam = new ServiceEmploiExamen(cnx);
            srvEmplForm = new ServiceEmploiFormation(cnx);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        loadAll();
    }

    private void loadAll() {
        try {
            loadList(vboxEvenements, srvEv.afficherEvenements());
            loadList(vboxCours, srvCours.afficher());
            loadList(vboxExamens, srvExam.afficher());
            loadList(vboxFormations, srvForm.getList());
        } catch (SQLException e) {
            e.printStackTrace(); // À remplacer par une meilleure gestion si besoin
            // Tu peux aussi afficher une alerte JavaFX :
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de chargement");
            alert.setHeaderText("Erreur lors du chargement des données");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }

    }


    private <T> void loadList(VBox container, List<T> items) {
        container.getChildren().clear();
        for (T o : items) {
            CheckBox cb = new CheckBox(getLabel(o));
            cb.setUserData(o);
            container.getChildren().add(cb);
        }
    }

    private String getLabel(Object o) {
        if (o instanceof Evenement)     return ((Evenement)o).getNom();
        if (o instanceof Cours)         return ((Cours)o).getTitre();
        if (o instanceof Examen)        return ((Examen)o).getTitre();
        if (o instanceof Formation)     return ((Formation)o).getTitre();
        return "";
    }

    @FXML
    private void handleValider() {
        // Collecte par type
        List<Evenement>     selEv   = new ArrayList<>();
        List<Cours>         selCours= new ArrayList<>();
        List<Examen>        selExam = new ArrayList<>();
        List<Formation>     selForm = new ArrayList<>();

        for (Node n : vboxEvenements.getChildren())    if (n instanceof CheckBox cb && cb.isSelected()) selEv.add((Evenement)cb.getUserData());
        for (Node n : vboxCours.getChildren())         if (n instanceof CheckBox cb && cb.isSelected()) selCours.add((Cours)cb.getUserData());
        for (Node n : vboxExamens.getChildren())       if (n instanceof CheckBox cb && cb.isSelected()) selExam.add((Examen)cb.getUserData());
        for (Node n : vboxFormations.getChildren())    if (n instanceof CheckBox cb && cb.isSelected()) selForm.add((Formation)cb.getUserData());

        if (!selEv.isEmpty())   srvEmplEv.addEvenementsToEmploi(emploi.getId(), selEv);
        if (!selCours.isEmpty())srvEmplCours.addCoursToEmploi(emploi.getId(), selCours);
        if (!selExam.isEmpty()) srvEmplExam.addExamensToEmploi(emploi.getId(), selExam);
        if (!selForm.isEmpty()) srvEmplForm.addFormationsToEmploi(emploi.getId(), selForm);

        ((Stage)btnValider.getScene().getWindow()).close();
    }
}
