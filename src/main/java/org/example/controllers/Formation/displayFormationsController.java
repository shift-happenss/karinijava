package org.example.controllers.Formation;

import javafx.application.HostServices;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.entities.Formation;
import org.example.services.ServiceFormation;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class displayFormationsController implements Initializable {

    @FXML
    private VBox formationsContainer;
    @FXML
    private TextField searchField;
    @FXML private Button addFormationButton;
    private ServiceFormation formationService = new ServiceFormation();
    private HostServices hostServices;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadFormations();
        setupEventHandlers();
    }

    private void loadFormations() {
        formationsContainer.getChildren().clear();
        List<Formation> formations = formationService.getAll();

        for (Formation formation : formations) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Formation/FormationItem.fxml"));
                Node node = loader.load();

                FormationItemController controller = loader.getController();
                controller.setFormation(formation);
                controller.setParentController(this);
                controller.setHostServices(this.hostServices);
                formationsContainer.getChildren().add(node);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }}
        private void navigateToAddFormation() {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Formation/AddFormation.fxml"));
                Parent root = loader.load();

                AddFormationController addController = loader.getController();
                addController.setParentController(this);

                Stage stage = (Stage) addFormationButton.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Ajouter une Formation");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        private void setupEventHandlers() {
            addFormationButton.setOnAction(event -> navigateToAddFormation());
        }



        public void refreshFormations() {
            loadFormations();
        }
    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }

   @FXML
    public void allerVersadmin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin.fxml")); // adapte le chemin
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();


            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    }

