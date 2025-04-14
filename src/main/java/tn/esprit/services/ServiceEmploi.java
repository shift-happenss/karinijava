package tn.esprit.services;

import tn.esprit.entities.Emploi;
import tn.esprit.entities.User;
import tn.esprit.utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceEmploi {

    private final Connection conn;

    public ServiceEmploi() throws SQLException {
        conn = MyDataBase.getInstance().getMyConnection();
    }

    // Méthode pour ajouter un Emploi
    public void ajouterEmploi(Emploi e) {
        String req = "INSERT INTO emploi (titre, description, proprietaire_id) VALUES (?, ?, ?)";
        try (PreparedStatement pst = conn.prepareStatement(req)) {
            pst.setString(1, e.getTitre());
            pst.setString(2, e.getDescription());
            pst.setInt(3, e.getProprietaireId());
            pst.executeUpdate();
            System.out.println("✅ Emploi ajouté avec succès !");
        } catch (SQLException ex) {
            System.out.println("❌ Erreur lors de l'ajout de l'emploi : " + ex.getMessage());
        }
    }

    public List<Emploi> afficherEmplois() {
        List<Emploi> emplois = new ArrayList<>();
        String req = "SELECT e.*, u.nom AS proprietaire_nom FROM emploi e JOIN user u ON e.proprietaire_id = u.id";

        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(req)) {
            while (rs.next()) {
                Emploi e = new Emploi(
                        rs.getInt("id"),
                        rs.getString("titre"),
                        rs.getString("description"),
                        rs.getInt("proprietaire_id")
                );
                e.setProprietaireNom(rs.getString("proprietaire_nom")); // fonctionne maintenant
                emplois.add(e);
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // pour voir la ligne exacte de l’erreur
        }
        return emplois;
    }


    // Méthode pour modifier un emploi existant
    public void modifierEmploi(Emploi e) {
        String req = "UPDATE emploi SET titre = ?, description = ?, proprietaire_id = ? WHERE id = ?";
        try (PreparedStatement pst = conn.prepareStatement(req)) {
            pst.setString(1, e.getTitre());
            pst.setString(2, e.getDescription());
            pst.setInt(3, e.getProprietaireId());
            pst.setInt(4, e.getId());
            pst.executeUpdate();
            System.out.println("✏️ Emploi modifié avec succès !");
        } catch (SQLException ex) {
            System.out.println("❌ Erreur lors de la modification de l'emploi : " + ex.getMessage());
        }
    }

    // Méthode pour supprimer un emploi
    public void supprimerEmploi(int id) {
        String req = "DELETE FROM emploi WHERE id = ?";
        try (PreparedStatement pst = conn.prepareStatement(req)) {
            pst.setInt(1, id);
            pst.executeUpdate();
            System.out.println("🗑️ Emploi supprimé avec succès !");
        } catch (SQLException ex) {
            System.out.println("❌ Erreur lors de la suppression de l'emploi : " + ex.getMessage());
        }
    }
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String req = "SELECT id, nom FROM user";
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(req)) {
            while (rs.next()) {
                users.add(new User(rs.getInt("id"), rs.getString("nom")));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return users;
    }

}
