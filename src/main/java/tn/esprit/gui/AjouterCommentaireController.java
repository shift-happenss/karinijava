package tn.esprit.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import tn.esprit.models.Commentaire;
import tn.esprit.services.CommentaireService;

public class AjouterCommentaireController {

    @FXML
    private TextField txtProprietereId;
    @FXML
    private TextArea txtContenu;
    @FXML
    private TextArea txtReponse;

    private final CommentaireService commentaireService = new CommentaireService();

    // L'ID de la ressource sera passé ici
    private int ressourceId;

    // Cette méthode sera appelée pour définir l'ID de la ressource
    public void setRessourceId(int ressourceId) {
        this.ressourceId = ressourceId;
    }

    // Cette méthode sera appelée lorsque l'utilisateur clique sur "Ajouter Commentaire"
    @FXML
    public void handleAjouterCommentaire() {
        try {
            int proprietereId = Integer.parseInt(txtProprietereId.getText());
            String contenu = txtContenu.getText();
            String reponse = txtReponse.getText();

            // Afficher les valeurs avant l'insertion pour déboguer
            System.out.println("Proprietere ID: " + proprietereId);
            System.out.println("Contenu: " + contenu);
            System.out.println("Réponse: " + reponse);
            System.out.println("Ressource ID: " + ressourceId); // Assurez-vous que ressourceId est défini

            // Crée un nouvel objet Commentaire
            Commentaire commentaire = new Commentaire();
            commentaire.setProprietereId(proprietereId);
            commentaire.setRessourceId(ressourceId);
            commentaire.setContenu(contenu);
            commentaire.setReponse(reponse);

            // Appelle le service pour ajouter le commentaire dans la base de données
            commentaireService.ajouterCommentaire(commentaire);

            // Affiche un message de succès
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText(null);
            alert.setContentText("Commentaire ajouté avec succès !");
            alert.showAndWait();

            // Réinitialiser les champs du formulaire
            resetForm();

        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez entrer des IDs valides.");
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();  // Capture toute autre exception
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Une erreur s'est produite lors de l'ajout du commentaire.");
            alert.showAndWait();
        }
    }


    // Méthode pour afficher des alertes
    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Réinitialise les champs du formulaire après l'ajout du commentaire
    private void resetForm() {
        txtProprietereId.clear();
        txtContenu.clear();
        txtReponse.clear();
    }
}
