import entities.Cours;
import services.CoursService;

public class Main {
    public static void main(String[] args) {
        // Créer une instance de CoursService
        CoursService service = new CoursService();

        // Ajouter un nouveau cours
        Cours c1 = new Cours(101, "Maths", "2025-04-04", "https://lien-cours.com");
        service.ajouterCours(c1);  // Assurez-vous que cette méthode enregistre bien le cours dans la base

        // Afficher la liste des cours après l'ajout
        System.out.println("Liste des cours après ajout :");
        service.listerCours().forEach(System.out::println);

        // Modifier le cours ajouté (ID doit correspondre à un élément existant)
        c1.setNom("Mathématiques avancées");  // Mise à jour du nom
        service.modifierCours(c1);  // Modifier dans la base de données

        // Afficher la liste après la modification
        System.out.println("\nListe des cours après modification :");
        service.listerCours().forEach(System.out::println);

        // Supprimer le cours avec ID=101
        service.supprimerCours(101);

        // Afficher la liste après suppression
        System.out.println("\nListe des cours après suppression :");
        service.listerCours().forEach(System.out::println);
    }
}
