package org.example.controllers.Formation;

import javafx.application.HostServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import org.example.entities.Formation;
import org.example.services.ServiceFormation;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DisplayFormationsFrontController implements Initializable {

    @FXML
    private VBox formationsContainer;

    private final ServiceFormation serviceFormation = new ServiceFormation();
    private HostServices hostServices;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        afficherFormations();
    }

    private void afficherFormations() {
        List<Formation> formations = serviceFormation.getAll();
        formationsContainer.getChildren().clear();

        for (Formation f : formations) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Formation/FormationCardFront.fxml"));
                Node card = loader.load();
                FormationCardFrontController controller = loader.getController();
                controller.setFormation(f);
                controller.setHostServices(hostServices);
                formationsContainer.getChildren().add(card);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }


}
