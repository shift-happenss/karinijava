package controllers;

import entities.Reponse;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import services.ReponseService;

public class ModifierReponseController {

    @FXML
    private TextField champTexte;

    @FXML
    private TextField champEstCorrecte;

    private Reponse reponseAModifier;
    private final ReponseService reponseService = new ReponseService();

    public void setReponse(Reponse r) {
        this.reponseAModifier = r;
        champTexte.setText(r.getTexte());
        champEstCorrecte.setText(r.getEstCorrecte());
    }

    @FXML
    private void modifierReponse() {
        String nouveauTexte = champTexte.getText().trim();
        String nouvelleValeur = champEstCorrecte.getText().trim();

        reponseAModifier.setTexte(nouveauTexte);
        reponseAModifier.setEstCorrecte(nouvelleValeur);

        reponseService.modifierReponse(reponseAModifier);
        champTexte.getScene().getWindow().hide();

    }
}
