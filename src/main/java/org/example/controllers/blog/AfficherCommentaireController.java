package org.example.controllers.blog;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.entities.Commentaire;
import org.example.services.CommentaireService;

import java.util.List;

public class AfficherCommentaireController {

    @FXML
    private TableView<Commentaire> tableCommentaires;
    @FXML
    private TableColumn<Commentaire, Integer> colId;
    @FXML
    private TableColumn<Commentaire, Integer> colProprietereId;
    @FXML
    private TableColumn<Commentaire, Integer> colRessourceId;
    @FXML
    private TableColumn<Commentaire, String> colContenu;
    @FXML
    private TableColumn<Commentaire, String> colReponse;

    private final CommentaireService commentaireService = new CommentaireService();

    @FXML
    public void initialize() {
        // Associe les colonnes avec les propriétés de l'objet Commentaire
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colProprietereId.setCellValueFactory(new PropertyValueFactory<>("proprietereId"));
        colRessourceId.setCellValueFactory(new PropertyValueFactory<>("ressourceId"));
        colContenu.setCellValueFactory(new PropertyValueFactory<>("contenu"));
        colReponse.setCellValueFactory(new PropertyValueFactory<>("reponse"));

        // Charge les commentaires depuis la base de données
        List<Commentaire> commentaires = commentaireService.getAllCommentaires();
        tableCommentaires.getItems().addAll(commentaires);
    }
}
