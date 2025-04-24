package tn.esprit.test;

import tn.esprit.entities.Categorie;
import tn.esprit.entities.Formation;
import tn.esprit.services.ServiceCategorie;
import tn.esprit.services.ServiceFormation;
import tn.esprit.utils.MyDataBase;

import java.sql.SQLException;
import java.util.List;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        ServiceCategorie sc=new ServiceCategorie();
        ServiceFormation sf=new ServiceFormation();
        try {
            //test crud categorie
            sc.ajouter(new Categorie("java","java formation"));
            System.out.println("Ajouter !!");
            System.out.println("\n📋 Liste des catégories :");
            List<Categorie> categories = sc.afficher();
            for (Categorie c : categories) {
                System.out.println("📁 ID: " + c.getId() + " | Nom: " + c.getName() + " | Description: " + c.getDescription());
            }
            sc.modifier(new Categorie(1,"jjj","hhhhhhhhh"));
//            sp.modifier(new Personne(1,"Koussay", "bb", 7 ));
           System.out.println("Modifier !!");
            sc.supprimer(2);

               /* Formation formation = new Formation();
                formation.setTitre("Formation Java");
                formation.setDescription("Cours complet Java SE");
                formation.setCible("Étudiant");
                formation.setFormateur("Mme Nermine Errokh");
                formation.setEtat("Non démarrée");
                formation.seturl_video("https://youtube.com/java-course");
                formation.seturl_image("https://image.com/java.png");
                formation.seturl_fichier("https://drive.com/java-pdf");
                formation.setContenuTexte("Contenu détaillé de la formation...");
                formation.setCategorie(new Categorie(3,"css","css for"));

                sf.ajouter(formation);
                System.out.println("✅ Formation ajoutée");
                sf.supprimer(1);
            // 4. Afficher les formations
            List<Formation> formations = sf.afficher();
            for (Formation f : formations) {
                System.out.println("🎓 " + f.getId() + " | " + f.getTitre() + " | " + f.getFormateur());
            }*/

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}