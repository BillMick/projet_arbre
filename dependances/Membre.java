import java.util.LinkedList;
// import java.util.List;

// import java.util.ArrayList;

public class Membre {
    private String nom;
    private String prenom;
    private String email;
    // private int annee_courante; // année d'exercice en cours
    private final LinkedList<Arbre> nominations = new LinkedList<>();
    private Recette cotisation;
    // private ArrayList<Recette> cotisations; // liste de cotisations
    // peut-être penser à une variable "cotisation" qui va contenir la cotisation courante (de l'année budgétaire en cours..)

    public Membre(String nom, String prenom, String email) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
    }

    public String nom() { return this.nom; }
    public String prenom() { return this.prenom; }
    public String email() { return this.email; }

    public void nominateTree(Arbre tree) {
        if (nominations.size() >= 5) {
            nominations.pollFirst(); // Remove the oldest nomination
        }
        nominations.add(tree);
        // tree.addVote(); // Increment vote for the tree
    }

    public LinkedList<Arbre> getProposedTrees() {
        return this.nominations;
    }

    public Recette cotisation() {
        return this.cotisation;
    }
}

// prévoir une fonction de recharge de compte pour les membres.