package tn.esprit.controllers.Formation;

import javafx.application.HostServices;
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
import tn.esprit.entities.Formation;
import tn.esprit.services.ServiceFormation;

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
        loadFormations(""); // appel par défaut sans filtre
    }

    private void loadFormations(String keyword) {
        formationsContainer.getChildren().clear();
        List<Formation> formations = formationService.getAll();

        for (Formation formation : formations) {
            if (keyword == null || keyword.isEmpty() ||
                    formation.getTitre().toLowerCase().contains(keyword.toLowerCase()) ) {

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
            }
        }
    }

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
            searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                loadFormations(newValue); // recharge avec le mot-clé
            });
        }



        public void refreshFormations() {
            loadFormations();
        }


    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }
    }
