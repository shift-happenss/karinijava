package services;

import connection.MaConnexion;
import entities.Question;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionService {

    Connection cnx = MaConnexion.getInstance();

    public void ajouter(Question question) {
        String req = "INSERT INTO question (examen_id, texte, type) VALUES (?, ?, ?)";
        try {
            PreparedStatement pst = cnx.prepareStatement(req);
            pst.setInt(1, question.getExamenId());
            pst.setString(2, question.getTexte());
            pst.setString(3, question.getType());
            pst.executeUpdate();
            System.out.println("Question ajoutée !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void modifier(Question question) {
        String req = "UPDATE question SET examen_id=?, texte=?, type=? WHERE id=?";
        try {
            PreparedStatement pst = cnx.prepareStatement(req);
            pst.setInt(1, question.getExamenId());
            pst.setString(2, question.getTexte());
            pst.setString(3, question.getType());
            pst.setInt(4, question.getId());
            pst.executeUpdate();
            System.out.println("Question modifiée !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void supprimer(int id) {
        String req = "DELETE FROM question WHERE id=?";
        try {
            PreparedStatement pst = cnx.prepareStatement(req);
            pst.setInt(1, id);
            pst.executeUpdate();
            System.out.println("Question supprimée !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Question> afficher() {
        List<Question> list = new ArrayList<>();
        String req = "SELECT * FROM question";
        try {
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(req);
            while (rs.next()) {
                list.add(new Question(
                        rs.getInt("id"),
                        rs.getInt("examen_id"),
                        rs.getString("texte"),
                        rs.getString("type")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
