package org.example.controllers.ahmed;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Platform;
import javafx.util.Duration;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.entities.Emploi;
import org.example.services.ServiceEmploi;
import javafx.scene.control.TextField;
import org.example.utils.ThemeManager;

import java.awt.event.MouseEvent;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import java.io.IOException;
import java.util.List;

public class AfficherEmploiController {

    @FXML
    private VBox vboxEmplois;

    // Nouveaux champs pour la recherche
    @FXML private TextField searchTitreField;
    @FXML private TextField searchDescriptionField;
    @FXML private TextField searchOwnerField;

    @FXML private Button themeToggleButton;

    @FXML private Button btnVoirNews;
    @FXML private Button btnAjouterEmploi, btnEvenements, btnRetourAdmin;

    @FXML private Button translateButton;
    @FXML private Label titleLabel;


    private ResourceBundle resourceBundle;
    private Locale currentLocale = Locale.FRENCH;

    private ServiceEmploi serviceEmploi;


    public AfficherEmploiController() {
        serviceEmploi = new ServiceEmploi();
    }




    @FXML
    public void initialize() {
        // 1) Listeners sur les TextFields pour un rafraîchissement à chaque frappe
        searchTitreField.textProperty().addListener((obs, oldV, newV) -> refresh());
        searchDescriptionField.textProperty().addListener((obs, oldV, newV) -> refresh());
        searchOwnerField.textProperty().addListener((obs, oldV, newV) -> refresh());
        resourceBundle = ResourceBundle.getBundle("messages", currentLocale);
        updateTexts();
        Platform.runLater(() -> {
            Scene scene = themeToggleButton.getScene();
            if (scene != null) {
                ThemeManager.applyTheme(scene);
                updateButtonText();
            }
        });
        // 2) Premier chargement
        refresh();
    }
    @FXML
    private void onToggleTheme(ActionEvent event) {
        Scene scene = ((Button) event.getSource()).getScene();
        ThemeManager.toggle(scene);
        updateButtonText();
    }

    private void updateButtonText() {
        if (ThemeManager.isDark()) {
            themeToggleButton.setText("☀️ Mode clair");
        } else {
            themeToggleButton.setText("🌙 Mode sombre");
        }
    }

    public void refresh() {
        vboxEmplois.getChildren().clear();
        List<Emploi> emplois = serviceEmploi.afficherEmplois().stream()
                .filter(emp -> {
                    String t = emp.getTitre().toLowerCase();
                    String q = searchTitreField.getText().toLowerCase().trim();
                    return q.isEmpty() || t.contains(q);
                })
                .filter(emp -> {
                    String d = emp.getDescription().toLowerCase();
                    String q = searchDescriptionField.getText().toLowerCase().trim();
                    return q.isEmpty() || d.contains(q);
                })
                .filter(emp -> {
                    String owner = emp.getProprietaireNom() != null
                            ? emp.getProprietaireNom().toLowerCase()
                            : "";
                    String q = searchOwnerField.getText().toLowerCase().trim();
                    return q.isEmpty() || owner.contains(q);
                })
                .collect(Collectors.toList());

        for (Emploi emp : emplois) {
            // Créer l'HBox de la ligne
            HBox hbox = new HBox(10);
            hbox.getStyleClass().add("item-box");

            // Partie gauche : Conteneur pour les informations de l'emploi
            VBox infoBox = new VBox(5);
            Label titreLabel = new Label(resourceBundle.getString("titre.label") + emp.getTitre());
            titreLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #00008B;");
            Label descriptionLabel = new Label(resourceBundle.getString("description.label") + emp.getDescription());
            descriptionLabel.setStyle("-fx-text-fill: #00008B;");
            Label ownerLabel = new Label(resourceBundle.getString("proprietaire.label") + emp.getProprietaireNom());
            ownerLabel.setStyle("-fx-text-fill: #00008B;");
            infoBox.getChildren().addAll(titreLabel, descriptionLabel, ownerLabel);
            // Laisser infoBox occuper tout l'espace disponible
            HBox.setHgrow(infoBox, Priority.ALWAYS);

            // Partie droite : Conteneur pour les boutons d'action
            HBox btnBox = new HBox(5);

            Button btnDetails = new Button(resourceBundle.getString("details.button"));
            btnDetails.setStyle("-fx-background-color: #FFD700; -fx-text-fill: #00008B;");
            btnDetails.setOnAction(event -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/emploi/DetailsEmploi.fxml"));
                    AnchorPane root = loader.load();
                    DetailsEmploiController controller = loader.getController();
                    controller.setEmploi(emp);
                    Stage stage = new Stage();
                    stage.setTitle("Détails Emploi");
                    stage.setScene(new Scene(root, 1000, 640));
                    stage.show();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });

            Button btnModifier = new Button(resourceBundle.getString("modifier.button"));
            btnModifier.setStyle("-fx-background-color: #FFD700; -fx-text-fill: #00008B;");
            btnModifier.setOnAction(event -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/emploi/ModifierEmploi.fxml"));
                    AnchorPane root = loader.load();
                    ModifierEmploiController controller = loader.getController();
                    controller.setEmploi(emp);
                    controller.setOnEmploiModifie(() -> refresh());
                    Stage stage = new Stage();
                    stage.setTitle("Modifier Emploi");
                    stage.setScene(new Scene(root, 800, 640));
                    stage.show();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });

            Button btnSupprimer = new Button(resourceBundle.getString("supprimer.button"));
            btnSupprimer.setStyle("-fx-background-color: #FFD700; -fx-text-fill: #00008B;");
            btnSupprimer.setOnAction(event -> {
                serviceEmploi.supprimerEmploi(emp.getId());
                refresh();
            });

            Button btnAjouterEvenement = new Button(resourceBundle.getString("ajouter.evenement.button"));
            btnAjouterEvenement.setStyle("-fx-background-color: #FFD700; -fx-text-fill: #00008B;");
            btnAjouterEvenement.setOnAction(event -> {
                try {
                    System.out.println(getClass().getResource("/emploi/AjouterEvenementPourEmploi.fxml"));
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/emploi/AjouterEvenementPourEmploi.fxml"));
                    Parent root = loader.load();
                    AjouterEvenementPourEmploiController controller = loader.getController();
                    controller.setEmploi(emp);
                    Stage stage = new Stage();
                    stage.setTitle("Ajouter des événements");
                    stage.setScene(new Scene(root, 700, 640));
                    stage.show();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });

            btnBox.getChildren().addAll(btnDetails, btnModifier, btnSupprimer, btnAjouterEvenement);

            // Ajouter le conteneur d'information et le conteneur de boutons dans la ligne
            hbox.getChildren().addAll(infoBox, btnBox);
            vboxEmplois.getChildren().add(hbox);
        }
    }


    @FXML
    private void handleAjouterEmploi() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/emploi/AjouterEmploi.fxml"));
            AnchorPane root = loader.load();
            AjouterEmploiController controller = loader.getController();
            controller.setOnEmploiAjoute(() -> {
                refresh();
            });
            Stage stage = new Stage();
            stage.setTitle("Ajouter Emploi");
            stage.setScene(new Scene(root, 800, 640));
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    @FXML
    private void handleVoirEvenements() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/evenement/AfficherEvenement.fxml"));
            AnchorPane root = loader.load();

            Scene scene = new Scene(root, 1000, 640);
            ThemeManager.applyTheme(scene);              // ← on applique le thème
            Stage stage = new Stage();
            stage.setTitle("Liste des Événements");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    @FXML
    private void handleVoirNews(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/news/NewsView.fxml")
            );
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Actualités");

            Scene scene = new Scene(root, 1000, 640);
            scene.getStylesheets().add(
                    getClass().getResource("/css/news.css").toExternalForm()
            );
            stage.setScene(scene);
            stage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void onTranslate(ActionEvent event) {
        currentLocale = currentLocale.equals(Locale.FRENCH)
                ? Locale.ENGLISH
                : Locale.FRENCH;
        resourceBundle = ResourceBundle.getBundle("messages", currentLocale);
        updateTexts();
        refresh(); // Rafraîchir pour mettre à jour les éléments dynamiques
    }

    // Méthode pour mettre à jour tous les textes
    private void updateTexts() {
        translateButton.setText(resourceBundle.getString("translate.button"));
        titleLabel.setText(resourceBundle.getString("liste.emplois.title"));
        btnAjouterEmploi.setText(resourceBundle.getString("ajouter.emploi.button"));
        btnEvenements.setText(resourceBundle.getString("voir.evenements.button"));
        btnRetourAdmin.setText(resourceBundle.getString("retour.admin.button"));
        btnVoirNews.setText(resourceBundle.getString("voir.news.button"));
        searchTitreField.setPromptText(resourceBundle.getString("rechercher.titre.prompt"));
        searchDescriptionField.setPromptText(resourceBundle.getString("rechercher.description.prompt"));
        searchOwnerField.setPromptText(resourceBundle.getString("rechercher.owner.prompt"));
    }


}
