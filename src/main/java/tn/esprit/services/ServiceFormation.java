package tn.esprit.services;

import tn.esprit.entities.Categorie;
import tn.esprit.entities.Formation;
import tn.esprit.utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceFormation implements IService<Formation> {

    private Connection connection;

    public ServiceFormation() {
        connection = MyDataBase.getInstance().getMyConnection();
    }

    @Override
    public void ajouter(Formation formation) throws SQLException {
        String sql = "INSERT INTO formation (titre, description, cible, formateur, etat, url_video, url_image, url_fichier, contenu_texte, categorie_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement pst = connection.prepareStatement(sql);
        pst.setString(1, formation.getTitre());
        pst.setString(2, formation.getDescription());
        pst.setString(3, formation.getCible());
        pst.setString(4, formation.getFormateur());
        pst.setString(5, formation.getEtat());
        pst.setString(6, formation.geturl_video());
        pst.setString(7, formation.geturl_image());
        pst.setString(8, formation.geturl_fichier());
        pst.setString(9, formation.getContenuTexte());
        pst.setInt(10, formation.getCategorie().getId());
        formation.setLikes(0);
        pst.executeUpdate();
    }

    @Override
    public void modifier(Formation formation) throws SQLException {
        String sql = "UPDATE formation SET titre = ?, description = ?, cible = ?, formateur = ?, etat = ?, url_video = ?, url_image = ?, url_fichier = ?, contenu_texte = ?, categorie_id = ? WHERE id = ?";
        PreparedStatement pst = connection.prepareStatement(sql);
        pst.setString(1, formation.getTitre());
        pst.setString(2, formation.getDescription());
        pst.setString(3, formation.getCible());
        pst.setString(4, formation.getFormateur());
        pst.setString(5, formation.getEtat());
        pst.setString(6, formation.geturl_video());
        pst.setString(7, formation.geturl_image());
        pst.setString(8, formation.geturl_fichier());
        pst.setString(9, formation.getContenuTexte());
        pst.setInt(10, formation.getCategorie().getId());
        pst.setInt(11, formation.getId());
        formation.setLikes(0);

        pst.executeUpdate();
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM formation WHERE id = ?";
        PreparedStatement pst = connection.prepareStatement(sql);
        pst.setInt(1, id);
        pst.executeUpdate();
    }

    @Override
    public List<Formation> afficher() throws SQLException {
        List<Formation> formations = new ArrayList<>();
        String sql = "SELECT * FROM formation";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);

        while (rs.next()) {
            Formation f = new Formation();
            f.setId(rs.getInt("id"));
            f.setTitre(rs.getString("titre"));
            f.setDescription(rs.getString("description"));
            f.setCible(rs.getString("cible"));
            f.setFormateur(rs.getString("formateur"));
            f.setEtat(rs.getString("etat"));
            f.seturl_video(rs.getString("url_video"));
            f.seturl_image(rs.getString("url_image"));
            f.seturl_fichier(rs.getString("url_fichier"));
            f.setContenuTexte(rs.getString("contenu_texte"));
            f.setLikes(0);

            // Il faudra instancier un objet Categorie à partir de l'ID, ou le laisser null selon ton design
            formations.add(f);
        }

        return formations;
    }

    public List<Formation> getAll() {
        List<Formation> formations = new ArrayList<>();
        String req = "SELECT f.*, c.id AS cat_id, c.name AS cat_nom FROM formation f " +
                "JOIN categorie c ON f.categorie_id = c.id";

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(req);

            while (rs.next()) {
                Formation f = new Formation();
                f.setId(rs.getInt("id"));
                f.setTitre(rs.getString("titre"));
                f.setDescription(rs.getString("description"));
                f.setCible(rs.getString("cible"));
                f.setFormateur(rs.getString("formateur"));
                f.setEtat(rs.getString("etat"));
                f.seturl_video(rs.getString("url_video"));
                f.seturl_image(rs.getString("url_image"));
                f.seturl_fichier(rs.getString("url_fichier"));
                f.setContenuTexte(rs.getString("contenu_Texte"));

                // Récupération de la catégorie associée
                Categorie c = new Categorie();
                c.setId(rs.getInt("cat_id"));
                c.setName(rs.getString("cat_nom"));


                f.setCategorie(c);

                formations.add(f);
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors du chargement des formations : " + e.getMessage());
        }

        return formations;
    }

}
