package org.example.controllers.ahmed;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.entities.*;
import org.example.services.*;
import org.example.utils.MyDataBase;
import jfxtras.scene.control.agenda.Agenda;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.scene.control.agenda.Agenda.AppointmentImplLocal;

import java.sql.Connection;
import java.util.List;

public class DetailsEmploiController {

    @FXML private Label lblTitre, lblDescription, lblProprietaire;
    @FXML private VBox vboxEvenements, vboxCours, vboxExamens, vboxFormations;
    @FXML private Button btnCalendrier;

    private Emploi emploi;
    private Connection cnx;

    public void setEmploi(Emploi emp) {
        this.emploi = emp;
        this.cnx    = MyDataBase.getInstance().getMyConnection();
        lblTitre.setText(emp.getTitre());
        lblDescription.setText(emp.getDescription());
        lblProprietaire.setText(
                emp.getProprietaireNom()!=null
                        ? "Propriétaire : " + emp.getProprietaireNom()
                        : "Propriétaire ID : " + emp.getProprietaireId()
        );
        loadAll();
    }

    private void loadAll() {
        loadSection(vboxEvenements,    new ServiceEmploiEvenement(cnx).getEvenementsByEmploi(emploi.getId()),     e -> "Événement : " + e.getNom());
        loadSection(vboxCours,         new ServiceEmploiCours(cnx).getCoursByEmploi(emploi.getId()),             c -> "Cours : "    + c.getTitre());
        loadSection(vboxExamens,       new ServiceEmploiExamen(cnx).getExamensByEmploi(emploi.getId()),            e -> "Examen : "   + e.getTitre());
        loadSection(vboxFormations,    new ServiceEmploiFormation(cnx).getFormationsByEmploi(emploi.getId()),      f -> "Formation : "+ f.getTitre());
    }

    private <T> void loadSection(VBox container, List<T> items, java.util.function.Function<T,String> labelGen) {
        container.getChildren().clear();
        if (items.isEmpty()) {
            Label none = new Label("Aucun item associé.");
            none.setStyle("-fx-font-size:14px; -fx-text-fill:#FF0000;");
            container.getChildren().add(none);
        } else {
            for (T o : items) {
                Label lbl = new Label(labelGen.apply(o));
                lbl.setStyle("-fx-font-size:14px; -fx-text-fill:#00008B;");
                container.getChildren().addAll(lbl, new Separator());
                FadeTransition ft = new FadeTransition(Duration.seconds(0.8), lbl);
                ft.setFromValue(0); ft.setToValue(1); ft.play();
            }
        }
    }

    @FXML private void handleRetour() {
        ((Stage)lblTitre.getScene().getWindow()).close();
    }

    @FXML
    private void handleCalendrier() {
        // Création de l’Agenda
        Agenda agenda = new Agenda();

        // 1) Définition des groupes de style
        Agenda.AppointmentGroup groupEvent  = new Agenda.AppointmentGroupImpl().withStyleClass("group0");
        Agenda.AppointmentGroup groupCours  = new Agenda.AppointmentGroupImpl().withStyleClass("group1");
        Agenda.AppointmentGroup groupExam   = new Agenda.AppointmentGroupImpl().withStyleClass("group2");
        Agenda.AppointmentGroup groupForm   = new Agenda.AppointmentGroupImpl().withStyleClass("group3");
        agenda.appointmentGroups().addAll(groupEvent, groupCours, groupExam, groupForm);

        // 2) Ajout des Événements
        new ServiceEmploiEvenement(cnx)
                .getEvenementsByEmploi(emploi.getId())
                .forEach(e -> {
                    AppointmentImplLocal appt = new AppointmentImplLocal()
                            .withStartLocalDateTime(e.getDateDebut().atStartOfDay())
                            .withEndLocalDateTime(e.getDateFin().atStartOfDay().plusHours(1))
                            .withSummary("Événement : " + e.getNom())
                            .withAppointmentGroup(groupEvent);
                    agenda.appointments().add(appt);
                });

        // 3) Ajout des Cours
        new ServiceEmploiCours(cnx)
                .getCoursByEmploi(emploi.getId())
                .forEach(c -> {
                    AppointmentImplLocal appt = new AppointmentImplLocal()
                            .withStartLocalDateTime(c.getDateDebut().toLocalDate().atStartOfDay())
                            .withEndLocalDateTime(c.getDateFin().toLocalDate().atStartOfDay())
                            .withSummary("Cours : " + c.getTitre())
                            .withAppointmentGroup(groupCours);
                    agenda.appointments().add(appt);
                });

        // 6) Charger le CSS dédié pour les groupes
        agenda.getStylesheets().add(getClass().getResource("/css/agenda.css").toExternalForm());

        // 7) Affichage dans une nouvelle fenêtre
        Stage dialog = new Stage();
        dialog.setTitle("Calendrier : " + emploi.getTitre());
        dialog.setScene(new Scene(agenda, 1000, 640));
        dialog.show();
    }

}
