package dependances;

// import java.util.ArrayList;

public class Membre {
    private String nom;
    private String prenom;
    private String email;
    // private ArrayList<Recette> cotisations; // liste de cotisations
    // peut-être penser à une variable "cotisation" qui va contenir la cotisation courante (de l'année budgétaire en cours..)

    public Membre(String nom, String prenom, String email) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
    }

    public String nom() { return this.nom;}
    public String prenom() { return this.prenom;}
    public String email() { return this.email;}
}

// prévoir une fonction de recharge de compte pour les membres.